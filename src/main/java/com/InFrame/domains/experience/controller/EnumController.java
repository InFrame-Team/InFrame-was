package com.InFrame.domains.experience.controller;

import com.InFrame.domains.experience.entity.enums.Category;
import com.InFrame.domains.experience.entity.enums.DetailField;
import com.InFrame.domains.experience.entity.enums.ProfessionalField;
import com.InFrame.domains.experience.resdto.EnumResponseDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/enums")
public class EnumController {
    @GetMapping("/categories")
    public ResponseEntity<?> getCategory() {
        List<EnumResponseDto> categories = Arrays.stream(Category.values())
                .map(category -> new EnumResponseDto(category.name(), category.getDescription()))
                .collect(Collectors.toList());
        return ResponseEntity.ok(categories);
    }

    @GetMapping("/detailFields")
    public ResponseEntity<?> getDetailField() {
        List<EnumResponseDto> detailFields = Arrays.stream(DetailField.values())
                .map(detailField -> new EnumResponseDto(detailField.name(), detailField.getDescription()))
                .collect(Collectors.toList());
        return ResponseEntity.ok(detailFields);
    }

    @GetMapping("/professionalFields")
    public ResponseEntity<?> getProfessionalField() {
        List<EnumResponseDto> professionalFields = Arrays.stream(ProfessionalField.values())
                .map(professionalField -> new EnumResponseDto(professionalField.name(), professionalField.getDescription()))
                .collect(Collectors.toList());
        return ResponseEntity.ok(professionalFields);
    }


}
