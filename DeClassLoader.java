package com.company;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class DeClassLoader extends ClassLoader {
    private String mLibPath;

    public DeClassLoader(String path) {
        mLibPath = path;
    }

    @Override
    protected Class<?> findClass(String name) throws ClassNotFoundException {
        String fileName = getFileName(name);

        File file = new File(mLibPath,fileName);

        try {
            FileInputStream is = new FileInputStream(file);

            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            int len = 0;
            byte b = 0;
            try {
                while ((len = is.read()) != -1) {
                    //将数据异或一个数字2进行解密
                    b = (byte) (len ^ 2);
                    bos.write(b);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            byte[] data = bos.toByteArray();
            is.close();
            bos.close();
            byteProcess(data,data.length);
            return defineClass(name,data,0,data.length);

        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return super.findClass(name);
    }

    //获取要加载 的class文件名
    private String getFileName(String name) {
        int index = name.lastIndexOf('.');
        if(index == -1){
            return name+".xlass";
        }else{
            return name.substring(index+1)+".class";
        }
    }

    public static void main(String[] args) throws ClassNotFoundException {
        DeClassLoader loader = new DeClassLoader("D:\\jixiang");
        Class<?> aClass = loader.findClass("Hello");
        try {
            Object obj = aClass.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void byteProcess(byte[] bs,int len){
        for(int index=0;index<len; index++){
            int tmp=bs[index]&0x000000ff;
            bs[index] = (byte)((255-tmp)& 0xff);
        }
    }

}
