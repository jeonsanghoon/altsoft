package com.altsoft.model.keyword;

import java.math.BigDecimal;

/// 코드클래스
public class CODE_DATA
{
    /// 코드(숫자형)
    public Integer CODE;
    /// 조회용 코드(예-SEARCH_CATEGORY_CODE)
    public String SEARCH_CODE;
    /// 코드명
    public String NAME;
    /// 위도
    public BigDecimal LATITUDE;
    /// 경도
    public BigDecimal LONGITUDE;
    /// T_CATEGORY_KEYWORD 테이블의 CK_CODE
    public Integer CK_CODE;
}

