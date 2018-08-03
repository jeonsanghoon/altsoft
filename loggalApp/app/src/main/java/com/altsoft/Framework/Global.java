package com.altsoft.Framework;

import com.altsoft.dao.LOGIN_INFO;

public class Global {
    static MapInfo _mapInfo;
    public static MapInfo getMapInfo ( ) {
        if (_mapInfo == null) {
            _mapInfo = new MapInfo();
        }
        return _mapInfo;
    }
    public static void setMapInfo(MapInfo val) {
        _mapInfo = val;
    }
    static Common _common;
    public static Common getCommon ( ) {
        if (_common == null) {
            _common = new Common();
        }
        return _common;
    }

    static LOGIN_INFO _loginInfo;
    public static LOGIN_INFO getLoginInfo ( ) {
        if (_loginInfo == null) {
            _loginInfo = new LOGIN_INFO();
        }
        return _loginInfo;
    }
    public static void SetLoginInfo(LOGIN_INFO login) {
        _loginInfo = login;
    }


}
