package com.example.chat.util;

import android.text.TextUtils;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * 项目名称:    TestChat
 * 创建人:        陈锦军
 * 创建时间:    2016/10/9      17:31
 * QQ:             1981367757
 */
public class JsonUtil {
        public static String getString(JSONObject jsonObject, String tag) {
                try {
                        String result = "";
                        if (jsonObject == null) {
                                return result;
                        }
                        if (jsonObject.has(tag)) {
                                result = jsonObject.getString(tag);
                        }

                        return result;
                } catch (JSONException e) {
                        e.printStackTrace();
                        return "";
                }
        }

        public static Integer getInt(JSONObject jsonObject, String tag) {
                String result = getString(jsonObject, tag);
                if (!TextUtils.isEmpty(result)) {
                        return Integer.parseInt(result);
                } else {
                        return 0;
                }

        }

        public static boolean getBoolean(JSONObject jsonObject, String name, boolean defaultValue) {
                boolean result = defaultValue;
                try {
                        result = jsonObject.getBoolean(name);
                } catch (JSONException e) {
                        e.printStackTrace();
                }
                return result;
        }

        public static Long getLong(JSONObject jsonObject, String tag) {
                String result = getString(jsonObject, tag);
                if (!TextUtils.isEmpty(result)) {
                        return Long.parseLong(result);
                } else {
                        return 0L;
                }
        }
}
