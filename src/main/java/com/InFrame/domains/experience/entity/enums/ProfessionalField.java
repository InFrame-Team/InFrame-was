package com.InFrame.domains.experience.entity.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

// 전문분야
@Getter
@RequiredArgsConstructor
public enum ProfessionalField {
    CRAFT_CREATION("공예/창작"),
    FOOD_DESSERT("음식/디저트"),
    FLOWER_GARDENING("플라워/가드닝"),
    CULTURE_TRADITION("문화/전통체험"),
    MUSIC_ART("음악/예술"),
    LIFE_HEALING("라이프/힐링"),
    LOCAL_TOUR("지역탐방/체험투어"),
    PHOTO_CONTENT("사진/콘텐츠");

    private final String description;
}
