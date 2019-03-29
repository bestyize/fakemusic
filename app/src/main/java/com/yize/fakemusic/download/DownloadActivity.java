package com.yize.fakemusic.download;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.yize.fakemusic.R;
import com.yize.fakemusic.config.DefaultParams;
import com.yize.fakemusic.config.SharePreferencesManager;
import com.yize.fakemusic.qqmusic.MusicInfo;
import com.yize.fakemusic.qqmusic.SearchListener;
import com.yize.fakemusic.qqmusic.SearchTool;
import com.yize.fakemusic.qqmusic.SongDetails;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class DownloadActivity extends AppCompatActivity implements View.OnClickListener {
    private DownloadService.DownloadBinder downloadBinder;
    private ServiceConnection conn=new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            downloadBinder=(DownloadService.DownloadBinder)service;
            controlPanel();

        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };

    private Button btn_download,btn_download_add,btn_download_remove;
    private EditText et_downloadlink;
    private RecyclerView rv_download_list;
    private TextView tv_download_lyric,tv_download_name,tv_download_singer,tv_download_album,tv_downloaded_status;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_download);
        initView();
        initDownloadService();
//        controlPanel();


    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindService(conn);
    }

    private void initView(){
        Toolbar toolbar_title=(Toolbar)findViewById(R.id.toolbar_title);
        setSupportActionBar(toolbar_title);
        tv_download_lyric=(TextView)findViewById(R.id.tv_download_lyric);
        tv_download_name=(TextView)findViewById(R.id.tv_download_name);
        tv_download_singer=(TextView)findViewById(R.id.tv_download_singer);
        tv_download_album=(TextView)findViewById(R.id.tv_download_album);
        tv_downloaded_status=(TextView)findViewById(R.id.tv_downloaded_status);
    }

    private void controlPanel(){
        Intent intent=getIntent();
        final String quality=intent.getStringExtra("quality");
        final MusicInfo musicInfo= (MusicInfo) intent.getSerializableExtra("musicInfo");
        tv_download_name.setText(musicInfo.getSongname());
        tv_download_singer.setText(musicInfo.getSingersName());
        tv_download_album.setText(musicInfo.getAlbumname());
        tv_downloaded_status.setText("正在下载");

        if(quality.contains("MP3")||quality.contains("FLAC")){
            final SearchListener listener=new SearchListener() {
                @Override
                public void onSuccess(MusicInfo musicInfo) {

                    switch (quality){
                        case "FLAC":
                            String flacDownloadLink=musicInfo.getFlacDownloadLink();
                            String saveFileName=musicInfo.getSaveFileName()+ ".flac";
                            checkProgress(musicInfo,"FLAC");
                            downloadBinder.startDownload(flacDownloadLink,saveFileName);
                            checkProgress(musicInfo,"FLAC");
                            break;
                        case "MP3":
                            String mp3DownloadLink=musicInfo.getHmp3DownloadLink();
                            if(musicInfo.getSize320()>0){
                                mp3DownloadLink=musicInfo.getHmp3DownloadLink();
                            }else if(musicInfo.getSize128()>0){
                                mp3DownloadLink=musicInfo.getLmp3DownloadLink();
                            }else {
                                Toast.makeText(DownloadActivity.this,"没有对应品质音乐，无法下载",Toast.LENGTH_SHORT).show();
                                return;
                            }
                            String mp3SaveFileName=musicInfo.getSaveFileName()+ ".mp3";
                            downloadBinder.startDownload(mp3DownloadLink,mp3SaveFileName);
                            checkProgress(musicInfo,"MP3");
                            break;
                        default :
                            break;

                    }

                }

                @Override
                public void onFailed(String reason) {

                }
            };


            final SearchListener lyricListener=new SearchListener() {
                @Override
                public void onSuccess(MusicInfo musicInfo) {
                    final String lyric=musicInfo.getLyric();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            tv_download_lyric.setText(lyric);
                        }
                    });

                }

                @Override
                public void onFailed(String reason) {

                }
            };


            new Thread(new Runnable() {
                @Override
                public void run() {
                    findDownloadLink(musicInfo,listener);
                    findLyric(musicInfo,lyricListener);
                }
            }).start();
        }


        if(quality.contains("MP4")){
            final SearchListener mvListener=new SearchListener() {
                @Override
                public void onSuccess(final MusicInfo musicInfo) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            tv_download_lyric.setText("开始为您下载MV："+musicInfo.getSongname());
                            String mvSaveFileName=musicInfo.getSaveFileName()+ ".mp4";
                            downloadBinder.startDownload(musicInfo.getMvDownloadLink(),mvSaveFileName);
                            checkProgress(musicInfo,"MP4");
                        }
                    });

                }

                @Override
                public void onFailed(String reason) {

                }
            };
            new Thread(new Runnable() {
                @Override
                public void run() {
                    findMVDownloadLink(musicInfo,mvListener);
                }
            }).start();
        }



    }





    private void findDownloadLink(MusicInfo musicInfo,SearchListener listener){
        musicInfo=new SearchTool().getDownloadLinkInQuickMode(musicInfo);
        listener.onSuccess(musicInfo);
    }

    private void findLyric(final MusicInfo musicInfo,final SearchListener listener){
        String folder=new SharePreferencesManager().getConfig("save_path","alover",this);
        String lyricOrign=new SongDetails(folder).getLyricBySongid(musicInfo);
        String lyric=new SongDetails(folder).lyricFormat(lyricOrign);
        musicInfo.setLyric(lyric);
        listener.onSuccess(musicInfo);
    }


    private void findMVDownloadLink(final MusicInfo musicInfo,final SearchListener listener){
        String folder=new SharePreferencesManager().getConfig("save_path","alover",this);
        String mvDownloadLink=new SongDetails(folder).getVideoDownloadLink(musicInfo);
        musicInfo.setMvDownloadLink(mvDownloadLink);
        listener.onSuccess(musicInfo);
    }

    private void checkProgress(final MusicInfo musicInfo, final String quality){
        new Thread(new Runnable() {
            @Override
            public void run() {
                String saveFileName=musicInfo.getSaveFileName()+".flac";
                long fileSize=musicInfo.getSizeflac();
                if(quality.contains("MP3")){
                    saveFileName=saveFileName.replace(".flac",".mp3");
                    fileSize=musicInfo.getSize320();
                }
                else if (quality.contains("MP4")){
                    saveFileName=saveFileName.replace(".flac","mp4");
                }
                if(fileSize==0){
                    fileSize=1;
                }
                final String fileName=saveFileName;
                final long totalFileSize=fileSize;
                final boolean downloaded=false;
                while(!downloaded){
                    try {

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                String folder=new SharePreferencesManager().getConfig("save_path","alover",getApplicationContext());
                                File file=new File(DefaultParams.SAVE_FLODER.replace("alover",folder)+fileName);
                                long localFileSize=file.length();
                                long progress=localFileSize*100/totalFileSize;
                                tv_downloaded_status.setText("正在下载:"+progress+"%");
                                if(localFileSize==totalFileSize){
                                    tv_downloaded_status.setText("下载完成");
                                }
                            }
                        });
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }



            }
        }).start();
    }








    @Override
    public void onClick(View v) {
        if(downloadBinder==null){
            return;
        }
        switch (v.getId()){

            default :
                break;
        }
    }


    private void initDownloadService(){
        Intent intent=new Intent(this,DownloadService.class);
        bindService(intent,conn,BIND_AUTO_CREATE);
        startService(intent);
    }


}
