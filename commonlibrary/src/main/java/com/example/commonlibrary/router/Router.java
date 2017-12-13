package com.example.commonlibrary.router;

import java.util.HashMap;
import java.util.Map;

/**
 * 项目名称:    NewFastFrame
 * 创建人:      陈锦军
 * 创建时间:    2017/12/9     20:49
 * QQ:         1981367757
 */

public class Router {
    private static Router instance;
    private Map<String,BaseProvider> providerMap;



    public static Router getInstance() {
        if (instance == null) {
            instance=new Router();
        }
        return instance;
    }



    private Router(){
        providerMap=new HashMap<>();
    }


    public void registerProvider(String provideName,BaseProvider baseProvider){
        if (providerMap == null) {
            providerMap=new HashMap<>();
        }
        providerMap.put(provideName, baseProvider);
    }


    public void unRegisterProvider(String providerName){
        if (providerMap != null && providerMap.containsKey(providerName)) {
            providerMap.remove(providerName);
        }
    }

}
