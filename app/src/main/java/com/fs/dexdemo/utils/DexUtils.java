package com.fs.dexdemo.utils;

import android.content.Context;

import java.io.File;
import java.lang.reflect.Array;
import java.lang.reflect.Field;

import dalvik.system.DexClassLoader;

public class DexUtils {

    public static boolean injectDexAtFirst(Context context, File optimizedDirectory, File... dexFiles) {
        boolean result = true;
        try {
            injectDexAtFirst(context.getClassLoader(), optimizedDirectory, dexFiles);
        } catch (NoSuchFieldException e) {
            result = false;
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            result = false;
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            result = false;
            e.printStackTrace();
        }
        return result;
    }

    private static void injectDexAtFirst(ClassLoader classLoader, File optimizedDirectory, File... dexFiles) throws NoSuchFieldException, NoSuchMethodException, IllegalAccessError, IllegalAccessException {
        StringBuilder sb = new StringBuilder();
        for (File file : dexFiles) {
            sb.append(file.getAbsolutePath()).append(":");
        }
        //加载所有外部dex文件
        DexClassLoader dexClassLoader = new DexClassLoader(sb.deleteCharAt(sb.length() - 1).toString(),
                optimizedDirectory.getAbsolutePath(), null, ClassLoader.getSystemClassLoader());
        //获取系统 dexElements
        Object pathElements = getClassLoaderElements(classLoader);
        //获取DexClassLoader dexElements
        Object dexElements = getClassLoaderElements(dexClassLoader);

        Field pathListField = ReflectUtil.findFiled(classLoader.getClass(), "pathList");
        Object pathList = pathListField.get(classLoader);
        Field dexElementsFiled = ReflectUtil.findFiled(pathList.getClass(), "dexElements");
        //将系统与外部dexElements合并
        Object arrayAppend = arrayAppend(dexElements, pathElements);
        //修改系统 dexElements
        dexElementsFiled.set(pathList, arrayAppend);
    }

    /**
     * 将所有Array类型的数据按顺序合并成一个Array数据
     */
    private static Object arrayAppend(Object... elements) {
        int length = 0;
        for (Object element : elements) {
            length += Array.getLength(element);
        }
        Object array = Array.newInstance(elements[0].getClass().getComponentType(), length);

        for (int i = 0, j = 0, k = 0, elementLength = Array.getLength(elements[k]); i < length; i++) {
            Array.set(array, i, Array.get(elements[k], i - j));
            if (i - j == elementLength - 1) {
                j += elementLength;
                k++;
                if (k < elements.length) {
                    elementLength = Array.getLength(elements[k]);
                }
            }
        }
        return array;
    }

    /**
     * 获取ClassLoader 中 dexElements 成员变量
     */
    private static Object getClassLoaderElements(ClassLoader classLoader) throws NoSuchMethodException, IllegalAccessException {
        Field pathListField = ReflectUtil.findFiled(classLoader.getClass(), "pathList");
        Object pathList = pathListField.get(classLoader);
        Field dexElementsFiled = ReflectUtil.findFiled(pathList.getClass(), "dexElements");
        return dexElementsFiled.get(pathList);
    }
}
