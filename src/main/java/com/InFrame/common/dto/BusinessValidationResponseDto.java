package com.InFrame.common.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

public record BusinessValidationResponseDto(
        @JsonProperty("status_code")
        String statusCode,

        @JsonProperty("match_cnt")
        int matchCnt,

        @JsonProperty("data")
        List<BusinessStatusDto> data
) {
    // "data" 배열 내부 객체
    public record BusinessStatusDto(
            @JsonProperty("b_no")
            String b_no, // 사업자번호

            @JsonProperty("b_stt")
            String b_stt, // "계속사업자", "휴업자", "폐업자"

            @JsonProperty("b_stt_cd")
            String b_stt_cd, // "01", "02", "03"

            @JsonProperty("tax_type")
            String tax_type, // "부가가치세 일반과세자" 등

            @JsonProperty("tax_type_cd")
            String tax_type_cd,

            @JsonProperty("end_dt")
            String end_dt, // 폐업일자

            @JsonProperty("utcc_yn")
            String utcc_yn, // 단위과세전환여부

            @JsonProperty("tax_type_change_dt")
            String tax_type_change_dt, // 최근과세유형전환일자

            @JsonProperty("invoice_apply_dt")
            String invoice_apply_dt // 세금계산서적용일자
    ) {}
}