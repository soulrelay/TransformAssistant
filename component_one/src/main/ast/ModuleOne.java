import com.me.anotation.AST;

public class ModuleOne {

    @AST(type = AST.TYPE.SOURCE, value = AST.ID.MODULE_INIT, level = 1)//插入源，插桩类型是模块初始化，优先级是1
    public static void autoInitModule() {
        new com.transform.assistant.common_lib.TestUtils().afterConnected();
    }

    @AST(type = AST.TYPE.SOURCE, value = AST.ID.ROUTER_INIT, level = 1)//插入源，插桩类型是路由器自动注册，优先级是1
    public static void autoInitRouter() {
        new com.transform.assistant.common_lib.TestUtils().addRouter();

    }


}
