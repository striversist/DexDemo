package com.fs.dexdemo.dynamic.impl;

import android.content.Context;
import android.content.Intent;

import com.fs.dexdemo.dynamic.Dynamic;

public class DynamicImpl implements Dynamic {

    @Override
    public String sayHello() {
        return new StringBuilder(getClass().getName()).append(" is loaded by DexClassLoader").toString();
    }

    @Override
    public void start(Context context) {
        Intent intent = new Intent();
        intent.setClassName(context, "com.fs.dexdemo.dynamic.impl.DynamicService");
        context.startService(intent);
    }
}
