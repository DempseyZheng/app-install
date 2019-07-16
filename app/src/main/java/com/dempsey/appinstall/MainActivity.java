package com.dempsey.appinstall;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.tbruyelle.rxpermissions2.RxPermissions;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import io.reactivex.functions.Consumer;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity
        extends AppCompatActivity
        implements View.OnClickListener
{

    /** 下载 */
    private Button       mBtnDownload;
    private Call.Factory mOkHttpClient;
    private String       url = "http://192.168.1.188/apk.php?n=app-debug.apk";
    private File         mDir;
    private EditText     mEtUrl;
    private Context mContext;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mContext=this;
        initView();
        mEtUrl.setText(url);



        mDir = new File(Environment.getExternalStorageDirectory() + "/APK");
        final RxPermissions rxPermissions = new RxPermissions(this);
        rxPermissions
                .request(Manifest.permission.READ_EXTERNAL_STORAGE,
                         Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .subscribe(new Consumer<Boolean>() {
                    @Override
                    public void accept(Boolean aBoolean)
                            throws Exception
                    {
                        if (aBoolean.booleanValue()){
                            if (!mDir.exists()) {
                                mDir.mkdir();
                            }
                        }
                    }
                });


mOkHttpClient=new OkHttpClient();
    }

    private void initView() {
        mBtnDownload = (Button) findViewById(R.id.btn_download);
        mBtnDownload.setOnClickListener(this);
        mEtUrl = (EditText) findViewById(R.id.et_url);
    }
    private void install(String filePath) {
        DebugLogger.i( "开始执行安装: " + filePath);
        File   apkFile = new File(filePath);
        Intent intent  = new Intent(Intent.ACTION_VIEW);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            DebugLogger.i( "版本大于 N ，开始使用 fileProvider 进行安装");
            intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            Uri contentUri = FileProvider.getUriForFile(
                    mContext
                    , "你的包名.fileprovider"
                    , apkFile);
            intent.setDataAndType(contentUri, "application/vnd.android.package-archive");
        } else {
            DebugLogger.i( "正常进行安装");
            intent.setDataAndType(Uri.fromFile(apkFile), "application/vnd.android.package-archive");
        }
        startActivity(intent);
    }
    public void downloadFile(final String url, final String path)
    {
        DebugLogger.e("start download file" + path);
        final Request request = new Request.Builder().url(url)
                                                     .build();
        final Call call = mOkHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onResponse(Call call, Response response)
                    throws IOException
            {
                DebugLogger.e("开始下载");
                InputStream      is  = null;
                byte[]           buf = new byte[2048];
                int              len = 0;
                FileOutputStream fos = null;
                try {
                    is = response.body()
                                 .byteStream();
                    File file = new File(path);

                    fos = new FileOutputStream(file);
                    while ((len = is.read(buf)) != -1) {
                        fos.write(buf, 0, len);
                    }
                    fos.flush();
                    // 如果下载文件成功，第一个参数为文件的绝对路径
                    DebugLogger.e("下载成功:" + file.getAbsolutePath());
                    install(file.getAbsolutePath());
                } catch (IOException e) {
                    DebugLogger.e("下载失败", e);
                } finally {
                    try {
                        if (is != null) {
                            is.close();
                        }
                    } catch (IOException e) {
                    }
                    try {
                        if (fos != null) {
                            fos.close();
                        }
                    } catch (IOException e) {
                    }
                }
            }

            @Override
            public void onFailure(Call call, IOException e) {
                DebugLogger.e("下载失败", e);
            }

        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            default:
                break;
            case R.id.btn_download:
                String url=mEtUrl.getText().toString().trim();
                String fileName=url.substring(url.lastIndexOf("=")+1);
                downloadFile(url, mDir + "/" +  fileName);
                break;
        }
    }
}
