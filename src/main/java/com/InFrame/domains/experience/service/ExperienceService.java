package com.InFrame.domains.experience.service;

import com.InFrame.common.exception.CustomException;
import com.InFrame.common.exception.error.ErrorCode;
import com.InFrame.common.service.S3UploadService;
import com.InFrame.domains.experience.entity.Experience;
import com.InFrame.domains.experience.repository.ExperienceRepository;
import com.InFrame.domains.experience.reqdto.ExperienceRequestDto;
import com.InFrame.domains.experience.resdto.ExperienceDetailResponseDto;
import com.InFrame.domains.experience.resdto.ExperienceResponseDto;
import com.InFrame.domains.experience.resdto.ExperienceSummaryResponseDto;
import com.InFrame.domains.host.entity.Host;
import com.InFrame.domains.host.repository.HostRepository;
import com.InFrame.domains.review.entity.Review;
import com.InFrame.domains.review.repository.ReviewRepository;
import com.InFrame.domains.user.entity.Role;
import com.InFrame.domains.user.entity.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ExperienceService {
    private final ExperienceRepository experienceRepository;
    private final ReviewRepository reviewRepository;
    private final HostRepository hostRepository;
    private final VectorStore vectorStore;
    private final S3UploadService s3UploadService;

    // 체험 생성
    @Transactional
    public ExperienceResponseDto createExperience(User user, ExperienceRequestDto requestDto) {
        if (user.getRole() != Role.HOST) {
            throw new CustomException(ErrorCode.FORBIDDEN_ACCESS);
        }

        Host host = user.getHost();

        if (host == null) {
            throw new CustomException(ErrorCode.HOST_NOT_FOUND);
        }

        Experience experience = requestDto.toEntity(host);
        Experience savedExperience = experienceRepository.save(experience);

        // VectorDB에 체험 정보 인덱싱
        indexExperience(savedExperience);

        return  ExperienceResponseDto.from(savedExperience, host);
    }

    // 체험 이미지 업로드
    @Transactional
    public ExperienceResponseDto uploadExperienceImages(User user, Long experienceId, List<MultipartFile> images) {
        // 1. 체험 정보 조회
        Experience experience = experienceRepository.findById(experienceId)
                .orElseThrow(() -> new CustomException(ErrorCode.EXPERIENCE_NOT_FOUND));

        // 2. 권한 확인
        if (user.getRole() != Role.HOST || !Objects.equals(experience.getHost().getId(), user.getHost().getId())) {
            throw new CustomException(ErrorCode.FORBIDDEN_ACCESS);
        }

        // 3. S3 업로드
        String folderName = "experiences/" + experience.getId();
        List<String> imageUrls = images.stream()
                .map(file -> s3UploadService.uploadFile(file, folderName))
                .collect(Collectors.toList());

        // 4. 이미지 URL 업데이트 및 저장
        experience.updateImageUrls(imageUrls);
        Experience updatedExperience = experienceRepository.save(experience);

        return ExperienceResponseDto.from(updatedExperience, updatedExperience.getHost());
    }

    // 체험 상세 조회
    @Transactional(readOnly = true)
    public ExperienceDetailResponseDto getExperienceDetail(Long experienceId) {
        // 1. 체험(Experience) 조회
        Experience experience = experienceRepository.findById(experienceId)
                .orElseThrow(() -> new CustomException(ErrorCode.EXPERIENCE_NOT_FOUND));

        // 2. 호스트(Host) 정보 조회
        Host host = experience.getHost();

        // 3. 해당 체험의 리뷰(Review) 목록 조회
        List<Review> reviews = reviewRepository.findAllByExperienceId(experienceId);

        return ExperienceDetailResponseDto.from(experience, host, reviews);
    }

    // AI 기반 체험 추천
    @Transactional(readOnly = true)
    public List<ExperienceResponseDto> recommendExperiences(String query, int topK) {

        // 1. VectorStore에서 유사한 Document 검색
        List<Document> documents = vectorStore.similaritySearch(
                SearchRequest.builder()
                        .query(query)
                        .topK(topK)
                        .filterExpression("type == 'experience'")
                        .build()
        );

        if (documents.isEmpty()) {
            return List.of();
        }

        // 2. Document 메타데이터에서 experienceId 리스트 추출
        List<Long> experienceIds = documents.stream()
                .map(doc -> doc.getMetadata().get("experienceId"))
                .filter(Objects::nonNull)
                .map(Object::toString)
                .map(Long::valueOf)
                .distinct()
                .toList();

        if (experienceIds.isEmpty()) {
            return List.of();
        }

        // 3. MySQL에서 ID 목록으로 체험 정보 일괄 조회
        Map<Long, Experience> experienceMap = experienceRepository.findAllByIdWithHost(experienceIds)
                .stream()
                .collect(Collectors.toMap(Experience::getId, e -> e));

        // 4. AI가 정렬한 순서를 기준으로 DTO 리스트 생성
        return documents.stream()
                .map(doc -> {
                    Object idMeta = doc.getMetadata().get("experienceId");
                    if (idMeta == null) return null;

                    Long expId = Long.valueOf(idMeta.toString());
                    Experience exp = experienceMap.get(expId);
                    if (exp == null) return null;

                    return ExperienceResponseDto.from(exp, exp.getHost());
                })
                .filter(Objects::nonNull)
                .toList();
    }

    // 체험 정보를 VectorDB에 인덱싱
    private void indexExperience(Experience experience) {
        try {
            // 1. AI가 이해할 수 있도록 엔티티 정보를 텍스트로 변환
            String content = buildExperienceContent(experience);

            // 2. 메타데이터 설정 (필터링 및 식별용)
            Map<String, Object> metadata = new HashMap<>();
            metadata.put("experienceId", experience.getId());
            metadata.put("hostId", experience.getHost().getId());
            metadata.put("type", "experience");
            metadata.put("title", experience.getTitle());

            if (experience.getProfessionalField() != null) {
                metadata.put("professionalField", experience.getProfessionalField().name());
            }
            if (experience.getDetailField() != null) {
                metadata.put("detailField", experience.getDetailField().name());
            }

            // 3. Document 객체 생성 및 VectorStore에 추가
            Document doc = new Document(content, metadata);
            vectorStore.add(List.of(doc));

            log.info("Indexed experience {} into VectorStore", experience.getId());
        } catch (Exception e) {
            // 인덱싱 작업이 실패하더라도, 체험 생성 트랜잭션은 롤백시키지 않음
            log.error("Failed to index experience {} into VectorStore", experience.getId(), e);
        }
    }

    // VectorDB에 저장할 텍스트 생성
    private String buildExperienceContent(Experience e) {
        Host h = e.getHost();

        StringBuilder sb = new StringBuilder();

        if (h != null) {
            sb.append("호스트 사업장 이름: ").append(h.getBusinessName()).append("\n");
            if (h.getCategory() != null) sb.append("호스트 카테고리: ").append(h.getCategory().getDescription()).append("\n");
            if (h.getDescription() != null) sb.append("호스트 소개: ").append(h.getDescription()).append("\n");
            if (h.getAddressBase() != null) sb.append("위치: ").append(h.getAddressBase()).append("\n");
        }

        if (e.getTitle() != null) sb.append("체험 제목: ").append(e.getTitle()).append("\n");
        if (e.getDescription() != null) sb.append("체험 설명: ").append(e.getDescription()).append("\n");
        if (e.getProfessionalField() != null) sb.append("전문 분야: ").append(e.getProfessionalField().getDescription()).append("\n");
        if (e.getDetailField() != null) sb.append("세부 분야: ").append(e.getDetailField().getDescription()).append("\n");
        if (e.getCertifications() != null) sb.append("보유 자격증/인증: ").append(e.getCertifications()).append("\n");
        sb.append("가격: ").append(e.getPrice()).append("원\n");
        sb.append("소요 시간: ").append(e.getDurationInHours()).append("시간\n");
        if (e.getMaxCapacityPerSlot() > 0) sb.append("최대 인원: ").append(e.getMaxCapacityPerSlot()).append("명\n");

        return sb.toString();
    }

    // 호스트가 등록한 모든 체험 목록 조회
    @Transactional(readOnly = true)
    public List<ExperienceSummaryResponseDto> getExperiencesByHostUser(User user) {
        if (user.getRole() != Role.HOST) {
            throw new CustomException(ErrorCode.FORBIDDEN_ACCESS);
        }

        Host host = user.getHost();
        if (host == null) {
            throw new CustomException(ErrorCode.HOST_NOT_FOUND);
        }
        Long hostId = host.getId();

        // 1. 호스트의 모든 체험 정보 (이미지 포함) 조회
        List<Experience> experiences = experienceRepository.findAllByHostIdWithImages(hostId);

        if (experiences.isEmpty()) {
            return List.of();
        }

        // 2. 호스트 체험들의 리뷰 요약 정보 (평점, 개수) 일괄 조회
        record ReviewSummary(Double avgRating, Long reviewCount) {}

        Map<Long, ReviewSummary> reviewSummaryMap = reviewRepository.findExperienceReviewSummaryByHostId(hostId).stream()
                .collect(Collectors.toMap(
                        row -> (Long) row[0], // experienceId
                        row -> new ReviewSummary(
                                ((Number) row[1]).doubleValue(), // AVG(r.rating)
                                (Long) row[2] // COUNT(r)
                        )
                ));

        // 3. DTO로 매핑
        return experiences.stream()
                .map(experience -> {
                    ReviewSummary summary = reviewSummaryMap.getOrDefault(experience.getId(), new ReviewSummary(0.0, 0L));
                    // 체험 이미지 리스트가 비어있지 않다면 첫 번째 이미지를 대표 이미지로 사용
                    String mainImageUrl = experience.getImageUrls().isEmpty() ? null : experience.getImageUrls().get(0);

                    Double rating = summary.avgRating;
                    Long reviewCount = summary.reviewCount;

                    return new ExperienceSummaryResponseDto(
                            experience.getId(),
                            mainImageUrl,
                            experience.getTitle(),
                            experience.getDescription(),
                            experience.getPrice(),
                            rating,
                            experience.getDurationInHours(),
                            reviewCount
                    );
                })
                .toList();
    }

    // 특정 호스트 ID의 모든 체험 목록 조회
    @Transactional(readOnly = true)
    public List<ExperienceSummaryResponseDto> getExperiencesByHostId(Long hostId) {
        // 1. 호스트 존재 여부 확인
        if (!hostRepository.existsById(hostId)) {
            throw new CustomException(ErrorCode.HOST_NOT_FOUND);
        }

        // 2. 호스트의 모든 체험 정보 (이미지 포함) 조회
        List<Experience> experiences = experienceRepository.findAllByHostIdWithImages(hostId);

        if (experiences.isEmpty()) {
            return List.of();
        }

        // 3. 호스트 체험들의 리뷰 요약 정보 (평점, 개수) 일괄 조회
        record ReviewSummary(Double avgRating, Long reviewCount) {}

        Map<Long, ReviewSummary> reviewSummaryMap = reviewRepository.findExperienceReviewSummaryByHostId(hostId).stream()
                .collect(Collectors.toMap(
                        row -> (Long) row[0], // experienceId
                        row -> new ReviewSummary(
                                ((Number) row[1]).doubleValue(), // AVG(r.rating)
                                (Long) row[2] // COUNT(r)
                        )
                ));

        // 4. DTO로 매핑
        return experiences.stream()
                .map(experience -> {
                    ReviewSummary summary = reviewSummaryMap.getOrDefault(experience.getId(), new ReviewSummary(0.0, 0L));
                    String mainImageUrl = experience.getImageUrls().isEmpty() ? null : experience.getImageUrls().get(0);

                    Double rating = summary.avgRating;
                    Long reviewCount = summary.reviewCount;

                    return new ExperienceSummaryResponseDto(
                            experience.getId(),
                            mainImageUrl,
                            experience.getTitle(),
                            experience.getDescription(),
                            experience.getPrice(),
                            rating,
                            experience.getDurationInHours(),
                            reviewCount
                    );
                })
                .toList();
    }
}
