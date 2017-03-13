package com.waibao.team.tuyou.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.util.ArrayList;

/**
 * Created by Azusa on 2016/4/5.
 */
public class FilesUtil {

    /**
     * 将图片压缩
     *
     * @param datas 原图片URL集合
     * @return 缩放后图片URL集合
     */
    public static ArrayList<String> scaleFile(ArrayList<String> datas) {
        ArrayList<String> imgs = new ArrayList<>();
        for (int i = 0; i < datas.size(); i++) {
            imgs.add(scal(datas.get(i).replace("file://", "")));
        }
        return imgs;
    }

    public static String scal(String path) {
        File outputFile = new File(path);
        long fileSize = outputFile.length(); //源文件大小
        final long fileMaxSize = 200 * 1024; //图片最大为200k，超过则压缩
        if (fileSize >= fileMaxSize) {
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;  //设为true，只返回图片大小，不返回bitmap对象
            BitmapFactory.decodeFile(path, options);
            options.inSampleSize = calculateInSampleSize(options, 800, 800);
            options.inJustDecodeBounds = false;
            //设定好长宽和压缩比例后获取压缩后的bitmap
            Bitmap bitmap = BitmapFactory.decodeFile(path, options);
            outputFile = new File(createImageFile().getPath()); //获取一个temp文件
            FileOutputStream fos;
            try {
                fos = new FileOutputStream(outputFile);
                /**
                 * param ：格式，图片质量，输入流
                 * 将bitmap对象写到临时文件路径
                 */
                bitmap.compress(Bitmap.CompressFormat.JPEG, 50, fos);
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (!bitmap.isRecycled()) {
                bitmap.recycle();
            } else {
                //防止内存不足时，bitmap对象已被回收，数据没有写到SD卡上
                File tempFile = outputFile;
                outputFile = new File(createImageFile().getPath());
                copyFileUsingFileChannels(tempFile, outputFile);
            }

        }
        return outputFile.getAbsolutePath();

    }

    //创建一个临时文件
    public static Uri createImageFile() {
        // 设置临时文件的文件名和文件夹路径
        String imageFileName = "imgtemp" + System.currentTimeMillis();
        File dir = new File(Environment.getExternalStorageDirectory() + "/tuyou/imgtemp");
        if (!dir.exists()) {
            dir.mkdir();
        }
        File image = null;
        try {
            // 在临时文件目录中创建一个空文件，使用给定前缀和后缀生成其名称。
            image = File.createTempFile(
                    imageFileName,  /* 文件名*/
                    ".jpg",         /* 文件后缀 */
                    dir      /* 文件所在路径 */
            );
        } catch (IOException e) {
            e.printStackTrace();
        }
        return Uri.fromFile(image);
    }

    //获取压缩比例
    public static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // 源图片的高度和宽度
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;
        if (height > reqHeight || width > reqWidth) {
            // 计算出实际宽高和目标宽高的比率
            final int heightRatio = Math.round((float) height / (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);
            // 选择宽和高中最小的比率作为inSampleSize的值，这样可以保证最终图片的宽和高
            // 一定都会大于等于目标的宽和高。
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
        }
        return inSampleSize;
    }

    //复制一个文件
    public static void copyFileUsingFileChannels(File source, File dest) {
        FileChannel inputChannel = null;
        FileChannel outputChannel = null;
        try {
            try {
                inputChannel = new FileInputStream(source).getChannel();
                outputChannel = new FileOutputStream(dest).getChannel();
                outputChannel.transferFrom(inputChannel, 0, inputChannel.size());
            } catch (IOException e) {
                e.printStackTrace();
            }
        } finally {
            try {
                inputChannel.close();
                outputChannel.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
