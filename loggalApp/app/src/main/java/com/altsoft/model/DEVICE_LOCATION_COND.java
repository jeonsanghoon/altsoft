package com.altsoft.model;

public class DEVICE_LOCATION_COND
{
    public String MODE;
    public Integer STATION_CODE;
    public Integer COMPANY_CODE;
    public String SEARCH_CODE;
    /// 위도(검색위치 기준:현위치)
    public Double LATITUDE;
    /// 경도(검색위치 기준:현위치)
    public Double LONGITUDE;
    /// 거리 (m 기준)
    public Double DISTANCE;
    public String LOCATION_NAME;
    /// <summary>
    /// 로컬박스명 또는 소유주로 검색 %
    /// </summary>
    public String SEARCH_TEXT;
    public Integer PAGE;
    public Integer PAGE_COUNT;
}
