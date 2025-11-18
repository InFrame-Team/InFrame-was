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
    KNITTING_EMBROIDERY("뜨개/자수 클래스"),

    KOREAN_FOOD("한식"),
    WESTERN_FUSION("양식/퓨전"),
    BAKING("베이킹"),
    CAKE("케이크"),
    CHOCOLATE("초콜릿"),
    COFFEE_BEVERAGE("커피/음료"),
    ALCOHOL("주류"),

    FLOWER_GARDENING("플라워 가드닝"),
    FLOWER_ARRANGEMENT("꽃꽂이"),
    BOUQUET("부케"),
    DRY_FLOWER("드라이 플라워"),
    PLANT_CARE("식물 관리"),
    TERRARIUM("테라리움"),
    GARDENING("가드닝"),
    HORTICULTURAL_THERAPY("원예치료"),
    PLANT_DESIGN("식물 디자인"),

    TEA_CEREMONY("다도"),
    HANBOK("한복"),
    TRADITIONAL_KNOT("전통매듭"),
    KOREAN_FOLK_PAINTING("한국화/민화"),
    CALLIGRAPHY("서예"),
    TRADITIONAL_LIQUOR("전통주"),
    ARCHITECTURE("건축"),
    GUGAK("국악"),

    INSTRUMENT_LESSON("악기 레슨"),
    VOCAL_TRAINING("보컬"),
    DRAWING_CLASS("드로잉"),
    OIL_PAINTING("유화/페인팅"),
    MUSICAL_THEATER("뮤지컬/연극"),

    YOGA_PILATES("요가/필라테스"),
    MEDITATION("명상"),
    PERSONAL_COLOR("퍼스널컬러"),
    PERFUME("향수"),
    TARO_SAJU("타로/사주"),
    ORGANIZING_STORAGE("정리수납"),
    FINANCIAL_TECH("제테크"),
    HOBBY_DANCE("취미 댄스"),

    HISTORY_TOUR("역사 투어"),
    GOURMET_TOUR("맛집 투어"),
    TREKKING_HIKING("트레킹/등산"),
    CAMPING_OUTDOOR("캠핑/아웃도어"),
    LOCAL_SPECIALTY_EXPERIENCE("특산물 체험"),
    BICYCLE("자전거"),
    CITY_WALK("시티워크"),
    RURAL_STAY("농어촌 스테이"),

    SMARTPHONE_PHOTOGRAPHY("스마트폰 사진"),
    PORTRAIT_PHOTOGRAPHY("인물 사진"),
    SNS_MARKETING("SNS 마케팅"),
    YOUTUBE_SHORTFORM("유튜브/숏폼"),
    WRITING("글쓰기"),
    PHOTOSHOP("포토샵"),
    WEBTOON_EMOTICON("웹툰/이모티콘"),
    DRONE_PHOTOGRAPHY("드론 촬영");

    private final String description;
}
