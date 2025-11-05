package com.InFrame.domains.experience.entity.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

// 상세분야
@Getter
@RequiredArgsConstructor
public enum DetailField {
    CERAMIC_CRAFT("도자기 공예"),
    LEATHER_CRAFT("가죽 공예"),
    METAL_SILVER_CRAFT("금속/은공예"),
    SOAP_CANDLE_DIFFUSER("비누/캔들/디퓨저 제작"),
    WOOD_CRAFT("목공예"),
    DRAWING_WATERCOLOR("드로잉/수채화"),
    PERFUME_MAKING("향수 만들기"),
    KNITTING_EMBROIDERY("뜨개/자수 클래스");

    private final String description;
}
