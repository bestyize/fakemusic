package com.yize.fakemusic.download;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import com.yize.fakemusic.R;
import com.yize.fakemusic.config.DefaultParams;
import com.yize.fakemusic.config.SharePreferencesManager;

import java.io.File;

public class DownloadService extends Service {
    private Context context;

    private DownloadTask downloadTask;
    private DownloadListener downloadListener=new DownloadListener() {
        @Override
        public void onSuccess() {
            downloadTask=null;
            noticeUser("下载成功");
            //Toast.makeText(DownloadService.this,"下载成功",Toast.LENGTH_SHORT).show();
            stopForeground(true);
            getNotificationManager().notify(10086,getNotification("下载成功",-1));
        }

        @Override
        public void onFailed(String reason) {
            downloadTask=null;
            noticeUser("本首歌没有资源，下载失败");
            stopForeground(true);
            getNotificationManager().notify(10086,getNotification("下载失败",-1));
        }

        @Override
        public void onCanceled() {
            downloadTask=null;
            noticeUser("取消下载");
            stopForeground(true);
        }

        @Override
        public void onPaused() {
            downloadTask=null;
            noticeUser("暂停下载");
        }

        @Override
        public void onProgress(Long progress) {

            getNotificationManager().notify(10086,getNotification("正在下载",Integer.valueOf(String.valueOf(progress))));

        }
    };


    private DownloadBinder downloadBinder=new DownloadBinder();
    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        return downloadBinder;
    }
    class DownloadBinder extends Binder{
        private String fileName;
        public void startDownload(String downloadLink,String fileName){
            if(downloadTask==null){
                this.fileName=fileName;
                String folderName=new SharePreferencesManager().getConfig("save_path","alover",getApplicationContext());
                String folder=DefaultParams.SAVE_FLODER.replace("alover",folderName);
                downloadTask=new DownloadTask(downloadListener);
                downloadTask.execute(downloadLink,fileName,folder);



                //noticeUser("微信公众号：从来不想 开始下载...");
                getNotificationManager();
                startForeground(10086,getNotification("正在下载...",0));

            }

        }

        public void pauseDownload(){
            if(downloadTask!=null){
                downloadTask.pauseDownload();
            }
        }
        public void canceledDownload(){
            if(downloadTask!=null){
                downloadTask.canceledDownload();
            }
            else{
                String folder=new SharePreferencesManager().getConfig("save_path","alover",getApplicationContext());
                File file=new File(DefaultParams.SAVE_FLODER.replace("alover",folder) +fileName);
                if(file.exists()){
                    file.delete();
                }
                noticeUser("已取消");
                getNotificationManager().cancel(10086);
                stopForeground(true);
            }

        }

    }

    @Override
    public void onCreate() {
        super.onCreate();
        context=getApplicationContext();
    }



    /**
     * 提醒用户
     * @param notice
     */
    protected void noticeUser(String notice){
        Toast.makeText(getApplicationContext(),notice,Toast.LENGTH_SHORT).show();

        Log.i("notice:",notice);
    }
    private NotificationManager manager;

    private NotificationManager getNotificationManager(){
        if(manager==null){
            manager=(NotificationManager)getSystemService(NOTIFICATION_SERVICE);
        }
        return manager;
    }


    private Notification getNotification(String title,int progress){
//        Intent intent=new Intent(DownloadService.this,DownloadActivity.class);
//        PendingIntent pendingIntent=PendingIntent.getActivity(this,0,intent,0);
        Notification.Builder builder;
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.O){
            manager.createNotificationChannel(new NotificationChannel("10086","10086",NotificationManager.IMPORTANCE_LOW));
            builder=new Notification.Builder(this,"10086");
        }
        else {
            builder=new Notification.Builder(this);
        }
        builder.setContentTitle(title);
        if(progress>0){
            builder.setContentText(progress+"%");
            builder.setProgress(100,progress,false);
        }
//        builder.setContentIntent(pendingIntent);
        builder.setSmallIcon(R.mipmap.ic_launcher_round);
        builder.setWhen(System.currentTimeMillis());
        builder.setSubText("微信公众号：从来不想");
        return builder.build();

    }








}
