package com.fs.dexdemo.dynamic.impl;

import com.fs.dexdemo.dynamic.Dynamic;

public class DynamicImpl implements Dynamic {

    @Override
    public String sayHello() {
        return new StringBuilder(getClass().getName()).append(" is loaded by DexClassLoader").toString();
    }
}
