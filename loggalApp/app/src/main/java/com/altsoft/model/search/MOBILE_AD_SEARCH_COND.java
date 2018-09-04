package com.altsoft.model.search;

import java.math.BigDecimal;

/// 모바일 배너 검색리스트 조건
public class MOBILE_AD_SEARCH_COND
{
    /// 페이지당 데이터건수
    public Integer PAGE_COUNT;
    /// 페이지번호
    public Integer PAGE;
    /// 위도
    public BigDecimal LATITUDE;
    /// 경도
    public BigDecimal LONGITUDE;
    /// UTC Time Zone 분단위 디폴트 9 * 60 = 540
    public Integer TIME_ZONE;
    /// 카테고리코드
    public Integer CATEGORY_CODE;
    /// 키워드 코드
    public Integer KEYWORD_CODE;
    /// 검색어
    public String KEYWORD_NAME;
    /// 검색위치로 부터 거리
    public BigDecimal DISTANCE;
    /// 로그인 아이디(이메일)
    public String USER_ID;
}