package com.fs.dexdemo.utils;

import java.lang.reflect.Field;

/**
 * 反射工具
 */

public class ReflectUtil {

    public static Field findFiled(Class classType, String fieldName) throws NoSuchMethodException {
        while (classType != null) {
            try {
                Field field = classType.getDeclaredField(fieldName);
                if (!field.isAccessible()) {
                    field.setAccessible(true);
                }
                return field;
            } catch (NoSuchFieldException e) {
                classType = classType.getSuperclass();
            }
        }
        throw new NoSuchMethodException(String.format("field: %s can not found", fieldName));
    }
}
