package com.example.dexinjpg;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;

import dalvik.system.DexClassLoader;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        readDex();
    }

    public void readDex() {
        try (InputStream is = getAssets().open("new.jpg")) {
            byte[] jpgBytes = new byte[is.available()];
            is.read(jpgBytes);
            // 分离JPG文件内容和DEX文件内容
            int jpgSize = indexOf(jpgBytes, new byte[]{(byte) 0xFF, (byte) 0xD9}) + 2;
            byte[] jpgContent = Arrays.copyOfRange(jpgBytes, 0, jpgSize);
            byte[] dexContent = Arrays.copyOfRange(jpgBytes, jpgSize, jpgBytes.length);

            File dexFile = new File(getFilesDir(), "test2.dex");
            try (OutputStream os1 = new FileOutputStream(new File(getFilesDir(), "new2.jpg"));
                 OutputStream os2 = new FileOutputStream(dexFile)) {
                os1.write(jpgContent);
                os2.write(dexContent);
            }

            String dexPath = dexFile.getAbsolutePath();
            String dexOutputDir = getFilesDir().getAbsolutePath();
            ClassLoader classLoader = new DexClassLoader(dexPath, dexOutputDir, null, getClassLoader());
            Class<?> clazz = classLoader.loadClass("com.example.fridademo20230301.Dynamic.DynamicCheck");
            Object obj = clazz.newInstance();
            Method method = clazz.getDeclaredMethod("check", byte[].class);
            method.setAccessible(true);
            boolean result = (boolean) method.invoke(obj, "233".getBytes());
            Log.i(TAG, "result = " + result);
        } catch (IOException | ClassNotFoundException | IllegalAccessException | InstantiationException | NoSuchMethodException | InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    private static int indexOf(byte[] source, byte[] target) {
        for (int i = 0; i < source.length - target.length + 1; i++) {
            boolean match = true;
            for (int j = 0; j < target.length; j++) {
                if (source[i + j] != target[j]) {
                    match = false;
                    break;
                }
            }
            if (match) {
                return i;
            }
        }
        return -1;
    }


}