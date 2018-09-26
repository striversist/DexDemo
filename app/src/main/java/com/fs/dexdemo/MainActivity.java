package com.fs.dexdemo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.fs.dexdemo.dynamic.Dynamic;
import com.fs.dexdemo.utils.FileUtils;

import java.io.File;
import java.io.IOException;

import dalvik.system.DexClassLoader;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.load_btn:
                loadDexClass();
                break;
        }
    }

    private void loadDexClass() {
        File cacheFile = FileUtils.getCacheDir(getApplicationContext());
        String internalPath = cacheFile.getAbsolutePath() + File.separator + "dynamic_dex.jar";
        File desFile = new File(internalPath);
        try {
            if (!desFile.exists()) {
                desFile.createNewFile();
                FileUtils.copyFiles(this, "dynamic_dex.jar", desFile);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        // 下面开始加载dex class
        DexClassLoader dexClassLoader = new DexClassLoader(internalPath, cacheFile.getAbsolutePath(), null, getClassLoader());
        try {
            Class libClazz = dexClassLoader.loadClass("com.fs.dexdemo.dynamic.impl.DynamicImpl");
            Dynamic dynamic = (Dynamic) libClazz.newInstance();
            if (dynamic != null)
                Toast.makeText(this, dynamic.sayHello(), Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
