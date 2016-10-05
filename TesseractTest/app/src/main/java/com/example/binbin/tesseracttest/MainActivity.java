package com.example.binbin.tesseracttest;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;

import com.googlecode.tesseract.android.TessBaseAPI;

import java.io.File;

public class MainActivity extends AppCompatActivity {

    private Bitmap srcBitmap;
    EditText editText;
    String result;
    Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        handler = new Handler(){

            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);

                editText.setText(result);

            }
        };

        editText = (EditText)findViewById(R.id.editText);

        srcBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.test);

        new Thread(new Runnable() {
            @Override
            public void run() {

                result = doOcr(srcBitmap,"/chi_sim");
                Message msg = new Message();
                handler.sendMessage(msg);

            }
        }).start();











    }

    /**
     * 进行图片识别
     *
     * @param bitmap
     *            待识别图片
     * @param language
     *            识别语言
     * @return 识别结果字符串
     */
    public String doOcr(Bitmap bitmap, String language) {
        TessBaseAPI baseApi = new TessBaseAPI();

        baseApi.init(getSDPath(), language);

        // 必须加此行，tess-two要求BMP必须为此配置
        bitmap = bitmap.copy(Bitmap.Config.ARGB_8888, true);

        baseApi.setImage(bitmap);

        String text = baseApi.getUTF8Text();

        baseApi.clear();
        baseApi.end();

        return text;
    }



    /**
     * 获取sd卡的路径
     *
     * @return 路径的字符串
     */
    public static String getSDPath() {
        File sdDir = null;
        boolean sdCardExist = Environment.getExternalStorageState().equals(
                android.os.Environment.MEDIA_MOUNTED); // 判断sd卡是否存在
        if (sdCardExist) {
            sdDir = Environment.getExternalStorageDirectory();// 获取外存目录
        }
        return sdDir.toString();
    }


}
