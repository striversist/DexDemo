package com.fs.dexdemo;

import android.Manifest;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.fs.dexdemo.dynamic.Dynamic;
import com.fs.dexdemo.utils.AESHelper;
import com.fs.dexdemo.utils.DexUtils;
import com.fs.dexdemo.utils.FileUtils;
import com.fs.dexdemo.utils.LogUtil;
import com.tbruyelle.rxpermissions.RxPermissions;

import java.io.File;
import java.io.IOException;
import java.util.Enumeration;

import dalvik.system.DexClassLoader;
import dalvik.system.DexFile;
import rx.functions.Action1;

public class MainActivity extends AppCompatActivity {

    private static final String AES_KEY = "abcdefghabcdefgh";
    private static AESHelper sAESHelper = new AESHelper();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        String[] permissions = new String[]{
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
        };
        new RxPermissions(this)
                .request(permissions)//访问手机状态权限
                .subscribe(new Action1<Boolean>() {
                    @Override
                    public void call(Boolean aBoolean) {
                        if (!aBoolean) {
                            finish();
                        }
                    }
                });
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.load_btn:
                loadDexClass();
                break;
            case R.id.encrypt_btn:
                encrypt();
                break;
            case R.id.decrypt_btn:
                decrypt();
                break;
        }
    }

    private void loadDexClass() {
        File cacheFile = FileUtils.getInternalCacheDir(getApplicationContext());
        String internalPath = cacheFile.getAbsolutePath() + File.separator + "dynamic_dex.jar";
        File dexFile = new File(internalPath);
        try {
            if (!dexFile.exists()) {
                dexFile.createNewFile();
                FileUtils.copyFiles(this, "dynamic_dex.jar", dexFile);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        showClassFromDex(dexFile);
        if (DexUtils.injectDexAtFirst(this, cacheFile.getAbsoluteFile(), dexFile)) {
            Toast.makeText(this, "Dex inject success!", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Dex inject failed!", Toast.LENGTH_SHORT).show();
        }

        // 下面开始加载dex class
//        DexClassLoader dexClassLoader = new DexClassLoader(internalPath, cacheFile.getAbsolutePath(), null, getClassLoader());
        ClassLoader dexClassLoader = getClassLoader();
        try {
            Class libClazz = dexClassLoader.loadClass("com.fs.dexdemo.dynamic.impl.DynamicImpl");
            Dynamic dynamic = (Dynamic) libClazz.newInstance();
            if (dynamic != null) {
                Toast.makeText(this, dynamic.sayHello(), Toast.LENGTH_SHORT).show();
                dynamic.start(this);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void encrypt() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                String sourceFile = "/sdcard/tmp/cat.jpg";
                String destFile = "/sdcard/tmp/cat.encrypted.jpg";
                sAESHelper.encryptFile(AES_KEY, sourceFile, destFile);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(MainActivity.this, "Encrypted", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }).start();
    }

    private void decrypt() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                String sourceFile = "/sdcard/tmp/cat.encrypted.jpg";
                String destFile = "/sdcard/tmp/cat.decrypted.jpg";
                sAESHelper.decryptFile(AES_KEY, sourceFile, destFile);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(MainActivity.this, "Decrypted", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }).start();
    }

    private void showClassFromDex(File dexFile) {
        String path = dexFile.getAbsolutePath();
        try {
            DexFile dx = DexFile.loadDex(path, File.createTempFile("opt", "dex",
                    getCacheDir()).getPath(), 0);
            // Print all classes in the DexFile
            for (Enumeration<String> classNames = dx.entries(); classNames.hasMoreElements(); ) {
                String className = classNames.nextElement();
                LogUtil.d("class: " + className);
            }
        } catch (IOException e) {
            LogUtil.d("Error opening " + path);
        }
    }
}
