package com.example.commonlibrary.router;

import java.util.HashMap;
import java.util.Map;

/**
 * 项目名称:    NewFastFrame
 * 创建人:      陈锦军
 * 创建时间:    2017/12/9     20:51
 * QQ:         1981367757
 */

public class BaseProvider {
    private Map<String,BaseAction> actionMap=new HashMap<>();


    private String name;

    public BaseProvider(String name) {
        this.name = name;
    }


    public String getName() {
        return name;
    }

    public void putAction(String actionName, BaseAction baseAction){
        if (actionMap == null) {
            actionMap=new HashMap<>();
        }
        actionMap.put(actionName, baseAction);
    }




    public void removeAll(){
        if (actionMap != null) {
            actionMap.clear();
        }
    }




    public BaseAction getAction(String actionName){
        if (actionMap != null) {
            return actionMap.get(actionName);
        }
        return null;
    }



    public void removeAction(String actionName){
        if (actionName != null && actionMap != null && actionMap.containsKey(actionName)) {
            actionMap.remove(actionName);
        }
    }

}
