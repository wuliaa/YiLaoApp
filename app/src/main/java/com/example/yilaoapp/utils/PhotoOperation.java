package com.example.yilaoapp.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class PhotoOperation {

    /**
     * 将bitmap转化为Byte数组
     * @param bitmap
     * @return  byteArray
     */
    public byte[] Bitmap2ByteArray(Bitmap bitmap){
        ByteArrayOutputStream byStream = new ByteArrayOutputStream();
        //将bitmap压缩成png，压缩到图片原本质量80%，最后保存在压缩数据的输出流 byStream
        bitmap.compress(Bitmap.CompressFormat.PNG, 80, byStream);
        byte[] byteArray = byStream.toByteArray();
        return  byteArray;
    }

    /**
     * 将Byte数组转化为bitmap
     * @param byteArray
     * @return
     */
    public Bitmap ByteArray2Bitmap(byte[] byteArray){
        ByteArrayInputStream byStream = new ByteArrayInputStream(byteArray);
        Bitmap bitmap = BitmapFactory.decodeStream(byStream);
        return bitmap;
    }

    /**
     * 通过图片路径将图片转化为byte数组
     * @param path
     * @return
     * @throws FileNotFoundException
     */
    public byte[] Path2ByteArray(String path) throws FileNotFoundException {
        FileInputStream fis = new FileInputStream(path);
        Bitmap bitmap  = BitmapFactory.decodeStream(fis);
        byte[] byteArray = Bitmap2ByteArray(bitmap);
        return byteArray;
    }
}
