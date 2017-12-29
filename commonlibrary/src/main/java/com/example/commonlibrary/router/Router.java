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
    private Map<String, BaseProvider> providerMap;


    public static Router getInstance() {
        if (instance == null) {
            instance = new Router();
        }
        return instance;
    }


    private Router() {
        providerMap = new HashMap<>();
    }


    public void clearCache() {
        for (Map.Entry<String, BaseProvider> entry :
                providerMap.entrySet()) {
            entry.getValue().removeAll();
        }
        providerMap.clear();
    }


    public RouterResult deal(RouterRequest routerRequest) {
        if (routerRequest != null) {
            BaseProvider baseProvider = providerMap.get(routerRequest.getProvideName());
            BaseAction baseAction;
            if (baseProvider!=null) {
                baseAction = baseProvider.getAction(routerRequest.getActionName());
                if (baseAction!=null) {
                    return baseAction.invoke(routerRequest);
                }
            }
        }
        return null;
    }


    public void registerProvider(String name, BaseAction baseAction) {
        if (name.contains(":")) {
            String providerName = name.split(":")[0];
            String actionName = name.split(":")[1];
            BaseProvider baseProvider = providerMap.get(providerName);
            if (baseProvider != null) {
                baseProvider.putAction(actionName, baseAction);
            } else {
                baseProvider = new BaseProvider(providerName);
                providerMap.put(providerName, baseProvider);
            }
            baseProvider.putAction(actionName, baseAction);
        }


    }


    public void unRegisterProvider(String providerName) {
        if (providerMap != null && providerMap.containsKey(providerName)) {
            providerMap.remove(providerName);
        }
    }

}
