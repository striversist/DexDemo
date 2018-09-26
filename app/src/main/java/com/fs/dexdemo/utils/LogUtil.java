package com.fs.dexdemo.utils;

import android.text.TextUtils;
import android.util.Log;

/**
 * 如果用于android平台，将信息记录到“LogCat”。如果用于java平台，将信息记录到“Console”
 * 使用logger封装
 */
public class LogUtil {
    private static final String TAG = "LogUtil";

    private static boolean DEBUG_ENABLE = true;// 是否调试模式

    /**
     * 在application调用初始化
     */
    public static void logInit(boolean debug) {
        DEBUG_ENABLE = debug;
        Log.e(TAG, "DEBUG_ENABLE:" + DEBUG_ENABLE);

    }
    public static String customTagPrefix = "";

    private LogUtil() {
    }

    public static boolean allowD = true;
    public static boolean allowE = true;
    public static boolean allowI = true;
    public static boolean allowV = true;
    public static boolean allowW = true;
    public static boolean allowWtf = true;

    private static String generateTag(StackTraceElement caller) {
        String tag = "%s.%s(L:%d)";
        String callerClazzName = caller.getClassName();
        callerClazzName = callerClazzName.substring(callerClazzName.lastIndexOf(".") + 1);
        tag = String.format(tag, callerClazzName, caller.getMethodName(), caller.getLineNumber());
        tag = TextUtils.isEmpty(customTagPrefix) ? tag : customTagPrefix + ":" + tag;
        return tag;
    }

    public static CustomLogger customLogger;

    public interface CustomLogger {
        void d(String tag, String content);

        void d(String tag, String content, Throwable tr);

        void e(String tag, String content);

        void e(String tag, String content, Throwable tr);

        void i(String tag, String content);

        void i(String tag, String content, Throwable tr);

        void v(String tag, String content);

        void v(String tag, String content, Throwable tr);

        void w(String tag, String content);

        void w(String tag, String content, Throwable tr);

        void w(String tag, Throwable tr);

        void wtf(String tag, String content);

        void wtf(String tag, String content, Throwable tr);

        void wtf(String tag, Throwable tr);
    }

    public static void d(String content) {
        if (DEBUG_ENABLE) {
            if (!allowD) return;
            StackTraceElement caller = OtherUtil.getCallerStackTraceElement();
            String tag = generateTag(caller);

            if (customLogger != null) {
                customLogger.d(tag, content);
            } else {
                Log.d(tag, content);
            }
        }
    }

    public static void d(String content, Throwable tr) {
        if (DEBUG_ENABLE) {
            if (!allowD) return;
            StackTraceElement caller = OtherUtil.getCallerStackTraceElement();
            String tag = generateTag(caller);

            if (customLogger != null) {
                customLogger.d(tag, content, tr);
            } else {
                Log.d(tag, content, tr);
            }
        }
    }

    public static void e(String content) {
        if (DEBUG_ENABLE) {
            if (!allowE) return;
            StackTraceElement caller = OtherUtil.getCallerStackTraceElement();
            String tag = generateTag(caller);

            if (customLogger != null) {
                customLogger.e(tag, content);
            } else {
                Log.e(tag, content);
            }
        }
    }

    public static void e(String content, Throwable tr) {
        if (DEBUG_ENABLE) {
            if (!allowE) return;
            StackTraceElement caller = OtherUtil.getCallerStackTraceElement();
            String tag = generateTag(caller);

            if (customLogger != null) {
                customLogger.e(tag, content, tr);
            } else {
                Log.e(tag, content, tr);
            }
        }
    }

    public static void i(String content) {
        if (DEBUG_ENABLE) {
            if (!allowI) return;
            StackTraceElement caller = OtherUtil.getCallerStackTraceElement();
            String tag = generateTag(caller);

            if (customLogger != null) {
                customLogger.i(tag, content);
            } else {
                Log.i(tag, content);
            }
        }
    }

    public static void i(String content, Throwable tr) {
        if (DEBUG_ENABLE) {
            if (!allowI) return;
            StackTraceElement caller = OtherUtil.getCallerStackTraceElement();
            String tag = generateTag(caller);

            if (customLogger != null) {
                customLogger.i(tag, content, tr);
            } else {
                Log.i(tag, content, tr);
            }
        }
    }

    public static void v(String content) {
        if (DEBUG_ENABLE) {
            if (!allowV) return;
            StackTraceElement caller = OtherUtil.getCallerStackTraceElement();
            String tag = generateTag(caller);

            if (customLogger != null) {
                customLogger.v(tag, content);
            } else {
                Log.v(tag, content);
            }
        }
    }

    public static void v(String content, Throwable tr) {
        if (DEBUG_ENABLE) {
            if (!allowV) return;
            StackTraceElement caller = OtherUtil.getCallerStackTraceElement();
            String tag = generateTag(caller);

            if (customLogger != null) {
                customLogger.v(tag, content, tr);
            } else {
                Log.v(tag, content, tr);
            }
        }
    }

    public static void w(String content) {
        if (DEBUG_ENABLE) {
            if (!allowW) return;
            StackTraceElement caller = OtherUtil.getCallerStackTraceElement();
            String tag = generateTag(caller);

            if (customLogger != null) {
                customLogger.w(tag, content);
            } else {
                Log.w(tag, content);
            }
        }
    }

    public static void w(String content, Throwable tr) {
        if (DEBUG_ENABLE) {
            if (!allowW) return;
            StackTraceElement caller = OtherUtil.getCallerStackTraceElement();
            String tag = generateTag(caller);

            if (customLogger != null) {
                customLogger.w(tag, content, tr);
            } else {
                Log.w(tag, content, tr);
            }
        }
    }

    public static void w(Throwable tr) {
        if (DEBUG_ENABLE) {
            if (!allowW) return;
            StackTraceElement caller = OtherUtil.getCallerStackTraceElement();
            String tag = generateTag(caller);

            if (customLogger != null) {
                customLogger.w(tag, tr);
            } else {
                Log.w(tag, tr);
            }
        }
    }


    public static void wtf(String content) {
        if (DEBUG_ENABLE) {
            if (!allowWtf) return;
            StackTraceElement caller = OtherUtil.getCallerStackTraceElement();
            String tag = generateTag(caller);

            if (customLogger != null) {
                customLogger.wtf(tag, content);
            } else {
                Log.wtf(tag, content);
            }
        }
    }

    public static void wtf(String content, Throwable tr) {
        if (DEBUG_ENABLE) {
            if (!allowWtf) return;
            StackTraceElement caller = OtherUtil.getCallerStackTraceElement();
            String tag = generateTag(caller);

            if (customLogger != null) {
                customLogger.wtf(tag, content, tr);
            } else {
                Log.wtf(tag, content, tr);
            }
        }
    }

    public static void wtf(Throwable tr) {
        if (DEBUG_ENABLE) {
            if (!allowWtf) return;
            StackTraceElement caller = OtherUtil.getCallerStackTraceElement();
            String tag = generateTag(caller);

            if (customLogger != null) {
                customLogger.wtf(tag, tr);
            } else {
                Log.wtf(tag, tr);
            }
        }
    }
}
