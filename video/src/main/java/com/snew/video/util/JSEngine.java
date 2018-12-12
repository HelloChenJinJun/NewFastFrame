package com.snew.video.util;

/**
 * 项目名称:    Update
 * 创建人:      陈锦军
 * 创建时间:    2018/11/29     0:39
 */

import com.example.commonlibrary.BaseApplication;

import org.mozilla.javascript.Context;
import org.mozilla.javascript.Function;
import org.mozilla.javascript.NativeJavaObject;
import org.mozilla.javascript.NativeObject;
import org.mozilla.javascript.Scriptable;
import org.mozilla.javascript.ScriptableObject;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.LineNumberReader;

/**
 * JS解析封装
 */
public class JSEngine {
    private StringBuffer sb;

    public JSEngine(String name) {
        LineNumberReader reader;
        try {
            reader = new LineNumberReader(new InputStreamReader(BaseApplication.getInstance().getAssets().open(name)));
            String temp;
            sb = new StringBuffer();
            while ((temp = reader.readLine()) != null) {
                sb.append(temp).append("\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * 执行JS
     */
    public String runScript(String functionParams, String methodName) {
        Context rhino = Context.enter();
        rhino.setOptimizationLevel(-1);
        try {
            Scriptable scope = rhino.initStandardObjects();

            ScriptableObject.putProperty(scope, "javaContext", Context.javaToJS(this, scope));
            ScriptableObject.putProperty(scope, "javaLoader", Context.javaToJS(JSEngine.class.getClassLoader(), scope));
            rhino.evaluateString(scope, sb.toString(), JSEngine.class.getName(), 1, null);
            Function function = (Function) scope.get(methodName, scope);
            Object result = function.call(rhino, scope, scope, new Object[]{functionParams});
            if (result instanceof String) {
                return (String) result;
            } else if (result instanceof NativeJavaObject) {
                return (String) ((NativeJavaObject) result).getDefaultValue(String.class);
            } else if (result instanceof NativeObject) {
                return (String) ((NativeObject) result).getDefaultValue(String.class);
            }
            return result.toString();//(String) function.call(rhino, scope, scope, functionParams);
        } finally {
            Context.exit();
        }
    }
}
