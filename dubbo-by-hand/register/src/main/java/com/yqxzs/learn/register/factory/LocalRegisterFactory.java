package com.yqxzs.learn.register.factory;

import com.yqxzs.learn.register.local.LocalMapRegister;

/**
 * @author edz
 */
public class LocalRegisterFactory {
    private static LocalMapRegister localMapRegister = new LocalMapRegister();

    public static LocalMapRegister getLocalMapRegister() {
        return localMapRegister;
    }
}
