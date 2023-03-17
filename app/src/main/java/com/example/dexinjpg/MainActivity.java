package com.example.dexinjpg;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;

import org.apache.commons.io.FileUtils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import dalvik.system.DexClassLoader;
import dalvik.system.DexFile;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        readDex();
    }
    public void readDex() {
        try {
            // 读取包含DEX文件的JPG文件
            InputStream is = getAssets().open("new.jpg");
            byte[] jpgBytes = new byte[is.available()];
            is.read(jpgBytes);
            is.close();

            // 分离JPG文件内容和DEX文件内容
            int jpgSize = indexOf(jpgBytes, new byte[] {(byte) 0xFF, (byte) 0xD9}) + 2;
            byte[] jpgContent = Arrays.copyOfRange(jpgBytes, 0, jpgSize);
            byte[] dexContent = Arrays.copyOfRange(jpgBytes, jpgSize, jpgBytes.length);
            OutputStream os2 = new FileOutputStream(new File(getFilesDir(), "test2.dex"));
            os2.write(dexContent);
            os2.close();
            // 保存JPG文件和DEX文件
            OutputStream os1 = new FileOutputStream(new File(getFilesDir(), "new2.jpg"));
            os1.write(jpgContent);
            os1.close();
            File dexFile = new File(getFilesDir(), "test2.dex");
            String dexPath = dexFile.getAbsolutePath();
            String dexOutputDir =getFilesDir().getAbsolutePath();
            ClassLoader classLoader = new DexClassLoader(dexPath, dexOutputDir, null, getClassLoader());
            Class<?> clazz = classLoader.loadClass("com.example.fridademo20230301.Dynamic.DynamicCheck");
            Object obj =clazz.newInstance();
            Method method = clazz.getDeclaredMethod("check", byte[].class);
            method.setAccessible(true);
            Boolean result= (Boolean) method.invoke(obj,"233".getBytes());
            System.out.println(result);
            // 加载Dex文件中的类


        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    private static int indexOf(byte[] source, byte[] target) {
        for (int i = 0; i < source.length - target.length + 1; i++) {
            boolean match = true;
            for (int j = 0; j < target.length; j++) {
                if (source[i+j] != target[j]) {
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