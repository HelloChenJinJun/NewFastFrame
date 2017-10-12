package com.example.chat.listener;

import java.util.List;

/**
 * 项目名称:    TestChat
 * 创建人:        陈锦军
 * 创建时间:    2017/1/3      16:12
 * QQ:             1981367757
 */
public interface LocationChangedListener {
        void onLocationChanged(List<String> addressList, double latitude, double longitude);

        void onLocationFailed(int errorId, String errorMsg);
}
