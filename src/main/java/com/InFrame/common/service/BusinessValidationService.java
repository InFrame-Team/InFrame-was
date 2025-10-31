package com.InFrame.common.service;

import com.InFrame.common.dto.BusinessValidationRequestDto;
import com.InFrame.common.dto.BusinessValidationResponseDto;
import com.InFrame.common.exception.CustomException;
import com.InFrame.common.exception.error.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class BusinessValidationService {

    private final RestTemplate restTemplate;

    @Value("${api.public-data.validate-url}")
    private String apiUrl;

    @Value("${api.public-data.service-key}")
    private String serviceKey;

    public boolean validateBusinessNumber(String businessNumber) {
        // 1. 10자리 숫자인지 간단히 확인
        if (businessNumber == null || !businessNumber.matches("\\d{10}")) {
            throw new CustomException(ErrorCode.INVALID_BUSINESS_NUMBER_FORMAT);
        }

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        headers.set("Authorization", "Infuser " + serviceKey);

        BusinessValidationRequestDto requestBody = new BusinessValidationRequestDto(List.of(businessNumber));
        HttpEntity<BusinessValidationRequestDto> entity = new HttpEntity<>(requestBody, headers);

        try {
            ResponseEntity<BusinessValidationResponseDto> response = restTemplate.postForEntity(
                    apiUrl, entity, BusinessValidationResponseDto.class
            );

            BusinessValidationResponseDto responseBody = response.getBody();

            // 2. 응답이 비어있거나, data가 없거나, match_cnt가 0이면 조회 실패
            if (responseBody == null || responseBody.data() == null
                    || responseBody.data().isEmpty() || responseBody.matchCnt() == 0) {
                throw new CustomException(ErrorCode.INVALID_BUSINESS_NUMBER);
            }

            // 3. 응답 본문의 data[0]에서 상태 코드 확인
            BusinessValidationResponseDto.BusinessStatusDto statusDto = responseBody.data().get(0);

            // 4. b_stt_cd (사업자 상태 코드) 확인
            switch (statusDto.b_stt_cd()) {
                case "01": // 계속사업자
                    return true;
                case "02": // 휴업자
                case "03": // 폐업자
                    throw new CustomException(ErrorCode.INACTIVE_BUSINESS_NUMBER);
                default:
                    throw new CustomException(ErrorCode.INVALID_BUSINESS_NUMBER);
            }

        } catch (HttpClientErrorException e) {
            log.error("[Business API Error] Client Error: {}, Body: {}", e.getStatusCode(), e.getResponseBodyAsString());
            throw new CustomException(ErrorCode.BUSINESS_API_FAILED);
        } catch (RestClientException e) {
            log.error("[Business API Error] RestClientException: {}", e.getMessage());
            throw new CustomException(ErrorCode.BUSINESS_API_FAILED);
        }
    }
}
