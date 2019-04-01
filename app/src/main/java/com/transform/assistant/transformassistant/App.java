package com.transform.assistant.transformassistant;

import android.app.Application;
import android.util.Log;

import com.me.anotation.AST;

public class App extends Application {

    public void onCreate() {
        super.onCreate();
        initModule();
        initRouter();
    }

    @AST(type = AST.TYPE.TARGET, value = AST.ID.MODULE_INIT, level = AST.LEVEL.BEFORE)
//插桩目标点，模块初始化的插桩，插桩方式为前插
    private void initModule() {
        Log.e("TAG", "Module初始化本地逻辑");
    }

    @AST(type = AST.TYPE.TARGET, value = AST.ID.ROUTER_INIT, level = AST.LEVEL.AFTER)
//插桩目标点，路由初始化的插桩，插桩方式为后插
    private void initRouter() {
        Log.e("TAG", "Router初始化本地逻辑");
    }
}
