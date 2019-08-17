package com.yize.fakemusic.config;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.yize.fakemusic.MainActivity;
import com.yize.fakemusic.R;
import com.yize.fakemusic.qqmusic.DownloadInfo;
import com.yize.fakemusic.qqmusic.MusicInfo;

import java.io.Serializable;
import java.util.List;

public class StartupActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_startup);
        checkPermission();
        getConfigFromServer();
    }

    private void getConfigFromServer(){
        final ServerConfigListener listener=new ServerConfigListener() {
            @Override
            public void onSuccess(ServerConfig serverConfig) {
                judgeWhatToDo(serverConfig);

            }

            @Override
            public void onFailed(String reason) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Intent intent=new Intent(StartupActivity.this,MainActivity.class);
                        startActivity(intent);
                        finish();

                    }
                });
            }
        };

        new Thread(new Runnable() {
            @Override
            public void run() {
                String response= new DownloadInfo().getWebContent("http://freedraw.xyz/fakemusic/alover.config");
                try {
                    Thread.sleep(1500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if(response.length()>20){
                    listener.onSuccess(parseJsonWithGson(response));
                }else {
                    listener.onFailed("未知原因");
                }


            }
        }).start();

    }


    private ServerConfig parseJsonWithGson(String jsonData){
        Gson gson=new Gson();
        List<ServerConfig> serverConfigList=gson.fromJson(jsonData,new TypeToken<List<ServerConfig>>(){}.getType());
        ServerConfig serverConfig=serverConfigList.get(0);
        return serverConfig;
    }

    private void judgeWhatToDo(final ServerConfig serverConfig){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {

                new SharePreferencesManager().addConfig("notice_daily",serverConfig.getNotice_content_daily(),getApplicationContext());

                if(serverConfig.getAppname().contains("fakemusic")){

                }

                if(serverConfig.getNotice_suspend_use().contains("YES")){
                    String suspend=serverConfig.getNotice_content_suspend_use();
                    noticeUser("停止使用",suspend,"YES");
                    return;
                }
                if(Double.valueOf(serverConfig.getLatested_version())>3.3){
                    String newVersionLink=serverConfig.getNotice_content_downloadlink();
                    noticeUser("检测到新版本，请下载新版本",newVersionLink,"YES");
                    return;
                }
                if(serverConfig.getNotice_startup().contains("YES")){
                    String commenContent=serverConfig.getNotice_content_common();
                }
                if(serverConfig.getNotice_join_qq().contains("YES")){
                    String qqlink=serverConfig.getNotice_content_join_qq();
                }

                if(serverConfig.getNotice_daily().contains("YES")){
                    String daily=serverConfig.getNotice_content_common();
                }
                if(serverConfig.getNotice_update().contains("YES")){
                    String newVersionLink=serverConfig.getNotice_content_downloadlink();
                }
                String offialSite=serverConfig.getNotice_content_offical_site();

                Intent intent=new Intent(StartupActivity.this,MainActivity.class);
                startActivity(intent);
                finish();

            }
        });
    }

    private void noticeUser(Object... params){
        String noticeTitle=params[0].toString();
        final String noticeContent=params[1].toString();
        final String enable=params[2].toString();
        AlertDialog.Builder dialog=new AlertDialog.Builder(StartupActivity.this);
        dialog.setTitle(noticeTitle);
        dialog.setMessage(noticeContent);
        dialog.setCancelable(false);
        dialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Uri uri= Uri.parse(noticeContent);
                Intent intent=new Intent(Intent.ACTION_VIEW,uri);
                startActivity(intent);
                finish();
            }
        });

        dialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                if(enable.contains("NO")){
                    System.exit(0);

                }else {
                    Intent intent=new Intent(StartupActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }


            }
        });
        dialog.show();
    }

    private void checkPermission(){
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.INTERNET)!= PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.INTERNET},1);
        }
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},2);
        }
        if(ContextCompat.checkSelfPermission(this,Manifest.permission.READ_EXTERNAL_STORAGE)!=PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},3);
        }
        if(ContextCompat.checkSelfPermission(this,Manifest.permission.FOREGROUND_SERVICE)!=PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.FOREGROUND_SERVICE},3);
        }

    }






}


interface ServerConfigListener{
    void onSuccess(ServerConfig serverConfig);
    void onFailed(String reason);
}



class ServerConfig implements Serializable {
    private String appname;
    private String latested_version;
    private String notice_startup;
    private String notice_update;
    private String notice_suspend_use;
    private String notice_force_update;
    private String notice_join_qq;
    private String notice_daily;
    private String notice_content_daily;
    private String notice_content_startup;
    private String notice_content_update;
    private String notice_content_suspend_use;
    private String notice_content_join_qq;
    private String notice_content_common;
    private String notice_content_downloadlink;
    private String notice_content_offical_site;
    private String notice_content_svip_key;

    public String getNotice_content_daily() {
        return notice_content_daily;
    }

    public void setNotice_content_daily(String notice_content_daily) {
        this.notice_content_daily = notice_content_daily;
    }

    public String getAppname() {
        return appname;
    }

    public void setAppname(String appname) {
        this.appname = appname;
    }

    public String getLatested_version() {
        return latested_version;
    }

    public void setLatested_version(String latested_version) {
        this.latested_version = latested_version;
    }

    public String getNotice_startup() {
        return notice_startup;
    }

    public void setNotice_startup(String notice_startup) {
        this.notice_startup = notice_startup;
    }

    public String getNotice_update() {
        return notice_update;
    }

    public void setNotice_update(String notice_update) {
        this.notice_update = notice_update;
    }

    public String getNotice_suspend_use() {
        return notice_suspend_use;
    }

    public void setNotice_suspend_use(String notice_suspend_use) {
        this.notice_suspend_use = notice_suspend_use;
    }

    public String getNotice_force_update() {
        return notice_force_update;
    }

    public void setNotice_force_update(String notice_force_update) {
        this.notice_force_update = notice_force_update;
    }

    public String getNotice_join_qq() {
        return notice_join_qq;
    }

    public void setNotice_join_qq(String notice_join_qq) {
        this.notice_join_qq = notice_join_qq;
    }

    public String getNotice_daily() {
        return notice_daily;
    }

    public void setNotice_daily(String notice_daily) {
        this.notice_daily = notice_daily;
    }

    public String getNotice_content_startup() {
        return notice_content_startup;
    }

    public void setNotice_content_startup(String notice_content_startup) {
        this.notice_content_startup = notice_content_startup;
    }

    public String getNotice_content_update() {
        return notice_content_update;
    }

    public void setNotice_content_update(String notice_content_update) {
        this.notice_content_update = notice_content_update;
    }

    public String getNotice_content_suspend_use() {
        return notice_content_suspend_use;
    }

    public void setNotice_content_suspend_use(String notice_content_suspend_use) {
        this.notice_content_suspend_use = notice_content_suspend_use;
    }

    public String getNotice_content_join_qq() {
        return notice_content_join_qq;
    }

    public void setNotice_content_join_qq(String notice_content_join_qq) {
        this.notice_content_join_qq = notice_content_join_qq;
    }

    public String getNotice_content_common() {
        return notice_content_common;
    }

    public void setNotice_content_common(String notice_content_common) {
        this.notice_content_common = notice_content_common;
    }

    public String getNotice_content_downloadlink() {
        return notice_content_downloadlink;
    }

    public void setNotice_content_downloadlink(String notice_content_downloadlink) {
        this.notice_content_downloadlink = notice_content_downloadlink;
    }

    public String getNotice_content_offical_site() {
        return notice_content_offical_site;
    }

    public void setNotice_content_offical_site(String notice_content_offical_site) {
        this.notice_content_offical_site = notice_content_offical_site;
    }

    public String getNotice_content_svip_key() {
        return notice_content_svip_key;
    }

    public void setNotice_content_svip_key(String notice_content_svip_key) {
        this.notice_content_svip_key = notice_content_svip_key;
    }
}
