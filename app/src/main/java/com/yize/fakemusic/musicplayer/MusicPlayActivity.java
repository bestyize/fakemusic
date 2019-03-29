package com.yize.fakemusic.musicplayer;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.yize.fakemusic.R;
import com.yize.fakemusic.config.DefaultParams;
import com.yize.fakemusic.config.SharePreferencesManager;
import com.yize.fakemusic.filemanager.LocalMusicInfo;
import com.yize.fakemusic.qqmusic.SongDetails;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MusicPlayActivity extends AppCompatActivity {
    protected TextView tv_lyric,tv_album_name,tv_singer_name,tv_song_name,tv_played_time,tv_playing_time;
    protected ImageView iv_play,iv_donate;
    protected SeekBar seekbar_music;
    protected MediaPlayer mediaPlayer=new MediaPlayer();
    int i=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music_play);
        initView();

    }

    private void initView(){
        tv_lyric=(TextView)findViewById(R.id.tv_lyric);
        tv_album_name=(TextView)findViewById(R.id.tv_album_name);
        tv_singer_name=(TextView)findViewById(R.id.tv_singer_name);
        tv_song_name=(TextView)findViewById(R.id.tv_song_name);
        tv_played_time=(TextView)findViewById(R.id.tv_played_time);
        tv_playing_time=(TextView)findViewById(R.id.tv_playing_time);
        iv_play=(ImageView)findViewById(R.id.iv_play);
        iv_donate=(ImageView)findViewById(R.id.iv_donate);

        seekbar_music=(SeekBar)findViewById(R.id.seekbar_music);


        Intent playIntent=getIntent();
        LocalMusicInfo musicInfo=(LocalMusicInfo)playIntent.getSerializableExtra("localMusicInfo");
        String fileLocation=playIntent.getStringExtra("music_file_location");
        String songName=musicInfo.getSongName();
        String singerName=musicInfo.getSingerName();
        String albumName=musicInfo.getAlbumName();

        tv_song_name.setText(songName);
        tv_singer_name.setText(singerName);
        tv_album_name.setText(albumName);
        tv_lyric.setText(getLyric(musicInfo));
        initMediaPlayer(fileLocation);

        if(!mediaPlayer.isPlaying()){
            mediaPlayer.start();
            iv_play.setImageResource(R.mipmap.play_playing);
            new PlayThread().start();
        }

        iv_play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!mediaPlayer.isPlaying()){
                    mediaPlayer.start();
                    iv_play.setImageResource(R.mipmap.play_playing);
                    new PlayThread().start();
                }else{
                    mediaPlayer.pause();
                    iv_play.setImageResource(R.mipmap.play_pause);
                }
            }
        });

        seekbar_music.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                int progress=seekbar_music.getProgress();
                mediaPlayer.seekTo(progress);
            }
        });



    }


    private String getLyric(LocalMusicInfo musicInfo){
        String folder=new SharePreferencesManager().getConfig("save_path","alover",getApplicationContext());
        File file=new File(DefaultParams.SAVE_FLODER.replace("alover",folder)+musicInfo.getSongName()+"_"+musicInfo.getSingerName()+".lrc");
        try {
            BufferedReader reader=new BufferedReader(new FileReader(file));
            StringBuilder sb=new StringBuilder();
            String line;
            while((line=reader.readLine())!=null){
                sb.append(line);
            }
            reader.close();
            String lyric=new SongDetails(folder).lyricFormat(sb.toString());
            return lyric;

        }catch (IOException e){
            return "暂无歌词";
        }

    }

    private void initMediaPlayer(String fileLocation){

        try {
            //File file=new File(fileLocation);
            mediaPlayer.setDataSource(fileLocation);
            mediaPlayer.prepare();

            int lengthOfMUsicTime=mediaPlayer.getDuration();
            seekbar_music.setMax(lengthOfMUsicTime);
            Date date=new Date(lengthOfMUsicTime);
            SimpleDateFormat sdf=new SimpleDateFormat("mm:ss");
            String time=sdf.format(date);
            tv_playing_time.setText(time);


            mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    Toast.makeText(MusicPlayActivity.this,"播放完毕",Toast.LENGTH_SHORT).show();
                }
            });




        }catch (Exception e){
//            new MainActivity().show("播放错误："+e.toString());
        }
    }



    private void showPlayTime(final String time){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                tv_played_time.setText(time);
            }
        });

    }

    class PlayThread extends Thread{
        @Override
        public void run() {
            super.run();

            while(seekbar_music.getProgress()<=seekbar_music.getMax()){
                //设置进度条的进度
                //得到当前音乐的播放位置
                int  currentPosition=mediaPlayer.getCurrentPosition();
                Log.i("test","currentPosition"+currentPosition);
                Date date=new Date(currentPosition);
                SimpleDateFormat sdf=new SimpleDateFormat("mm:ss");
                String time=sdf.format(date);
                showPlayTime(time);
                seekbar_music.setProgress(currentPosition);
                //让进度条每一秒向前移动
                SystemClock.sleep(1000);

                if (!mediaPlayer.isPlaying()){
                    break;

                }

            }
        }
    }




    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(mediaPlayer.isPlaying()){
            mediaPlayer.stop();
        }
    }
}
