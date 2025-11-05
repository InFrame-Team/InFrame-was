package com.InFrame.domains.experience.entity.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

// 분야 카테고리
@Getter
@RequiredArgsConstructor
public enum Category {
    MASTER_ARTISAN("장인/명인"),
    YOUNG_ENTREPRENEUR("청년사업가"),
    LOCAL_MERCHANT("골목상인"),
    ARTIST("예술인/문화인");

    private final String description;
}
