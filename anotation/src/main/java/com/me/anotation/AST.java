package com.me.anotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.SOURCE)
@Target(ElementType.METHOD)
public @interface AST {

    /**
     * 类型  0插入点  1 需要插入的原始代码块
     */
    int type();

    /**
     * 插桩的类型的id，支持多种需求的代码插桩
     */
    int value();

    /**
     * 类型  为0 时   0表示前插   1表示后插
     * 类型  为1 时   level表示优先级  0在最前  值越大  插入时排序的优先级越低
     */
    int level();

    /**
     * 类型  0插入点  1 需要插入的代码块
     */
    interface TYPE {
        int TARGET = 0;
        int SOURCE = 1;
    }

    /**
     * 插入类型的id
     */
    interface ID {
        int MODULE_INIT = 0x0001;//模块初始化的插桩
        int ROUTER_INIT = 0x0002;//路由注册的插桩
    }

    /**
     * 类型  为0 时   0表示前插   1表示后插
     */
    interface LEVEL {
        int BEFORE = 0;
        int AFTER = 1;
    }
}
