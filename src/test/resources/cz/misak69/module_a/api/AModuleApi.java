package cz.misak69.module_a.api;


import cz.misak69.module_b.impl.BModuleImpl;

public class AModuleApi {

    public static String MODULE_A_API = "Api of module A";

    public void testMethod(Object o) {

        System.out.print(BModuleImpl.MODULE_B_IMPL);
    }


}