package com.fs.dexdemo.utils;

import android.content.Context;

import org.apache.commons.lang3.reflect.FieldUtils;

import java.lang.reflect.Array;

import dalvik.system.DexClassLoader;

public class DexUtils {

    public static boolean injectDexAtFirst(Context context, String dexPath, String dexOptPath) {
        try {
            // 获取系统的dexElements
            Object baseDexElements = getDexElements(getPathList(getPathClassLoader(context)));

            // 获取patch的dexElements
            DexClassLoader patchDexClassLoader = new DexClassLoader(dexPath, dexOptPath, dexPath, getPathClassLoader(context));
            Object patchDexElements = getDexElements(getPathList(patchDexClassLoader));

            // 组合最新的dexElements
            Object allDexElements = combineArray(patchDexElements, baseDexElements);

            // 将最新的dexElements添加到系统的classLoader中
            Object pathList = getPathList(getPathClassLoader(context));

            FieldUtils.writeField(pathList, "dexElements", allDexElements);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }

    public static ClassLoader getPathClassLoader(Context context) {
//        return DexUtils.class.getClassLoader();
        return context.getClassLoader();
    }

    /**
     * 反射调用getPathList方法，获取数据
     *
     * @param classLoader
     * @return
     * @throws ClassNotFoundException
     * @throws NoSuchFieldException
     * @throws IllegalAccessException
     */
    public static Object getPathList(ClassLoader classLoader) throws IllegalAccessException {
        return FieldUtils.readField(classLoader, "pathList");
    }

    /**
     * 反射调用pathList对象的dexElements数据
     *
     * @param pathList
     * @return
     * @throws NoSuchFieldException
     * @throws IllegalAccessException
     */
    public static Object getDexElements(Object pathList) throws IllegalAccessException {
        LogUtil.d("Reflect To Get DexElements");
        return FieldUtils.readField(pathList, "dexElements");
    }

    /**
     * 拼接dexElements，将patch的dex插入到原来dex的头部
     *
     * @param firstElement
     * @param secondElement
     * @return
     */
    public static Object combineArray(Object firstElement, Object secondElement) {
        LogUtil.d("Combine DexElements");

        // 取得一个数组的Class对象, 如果对象是数组，getClass只能返回数组类型，而getComponentType可以返回数组的实际类型
        Class objTypeClass = firstElement.getClass().getComponentType();

        int firstArrayLen = Array.getLength(firstElement);
        int secondArrayLen = Array.getLength(secondElement);
        int allArrayLen = firstArrayLen + secondArrayLen;

        Object allObject = Array.newInstance(objTypeClass, allArrayLen);
        for (int i = 0; i < allArrayLen; i++) {
            if (i < firstArrayLen) {
                Array.set(allObject, i, Array.get(firstElement, i));
            } else {
                Array.set(allObject, i, Array.get(secondElement, i - firstArrayLen));
            }
        }
        return allObject;
    }
}
