package com.yize.fakemusic.filemanager;


import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.content.FileProvider;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.yize.fakemusic.R;
import com.yize.fakemusic.config.DefaultParams;
import com.yize.fakemusic.config.SharePreferencesManager;
import com.yize.fakemusic.musicplayer.*;

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

public class LocalMusicAdapter extends RecyclerView.Adapter<LocalMusicAdapter.ViewHolder> {
    private List<LocalMusicInfo> localMusicInfos;
    private Context context;
    private long lastClickTime=0L;
    private static final int CLICK_FILTER_TIME_BETWEEN_TWO_CLICK=500;


    static class ViewHolder extends RecyclerView.ViewHolder{
        TextView tv_song_name,tv_song_detail;
        Button btn_download_flac,btn_download_mp3,btn_show_mv;
        LinearLayout ll_music_info;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_song_name=(TextView)itemView.findViewById(R.id.tv_song_name);
            tv_song_detail=(TextView)itemView.findViewById(R.id.tv_song_detail);
            btn_download_flac=(Button)itemView.findViewById(R.id.btn_download_flac);
            btn_download_mp3=(Button)itemView.findViewById(R.id.btn_download_mp3);
            btn_show_mv=(Button)itemView.findViewById(R.id.btn_show_mv);
            ll_music_info=(LinearLayout)itemView.findViewById(R.id.ll_music_info);
        }
    }

    public LocalMusicAdapter(List<LocalMusicInfo> localMusicInfos){
        this.localMusicInfos=localMusicInfos;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        if(context==null){
            context=viewGroup.getContext();
        }
        View view= LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.apapter_local_music,viewGroup,false);
        ViewHolder holder=new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, int i) {
        final LocalMusicInfo musicInfo=localMusicInfos.get(i);
        viewHolder.tv_song_name.setText(musicInfo.getSongName());
        viewHolder.tv_song_detail.setText(musicInfo.getSingerName()+"|"+musicInfo.getAlbumName());
        final String musicType=musicInfo.getSongType();
        viewHolder.btn_download_flac.setVisibility(View.GONE);
        viewHolder.btn_download_mp3.setVisibility(View.GONE);
        String fileType=".flac";
        if(musicType.contains("flac")){
            viewHolder.btn_download_flac.setVisibility(View.VISIBLE);
            viewHolder.btn_show_mv.setVisibility(View.GONE);
            viewHolder.btn_download_mp3.setVisibility(View.GONE);
        }
        if(musicType.contains("mp3")){
            viewHolder.btn_download_mp3.setVisibility(View.VISIBLE);
            viewHolder.btn_show_mv.setVisibility(View.GONE);
            viewHolder.btn_download_flac.setVisibility(View.GONE);
            fileType=".mp3";
        }
        if(musicType.contains("mp4")){
            viewHolder.btn_show_mv.setVisibility(View.VISIBLE);
            fileType=".mp4";
            viewHolder.btn_download_mp3.setVisibility(View.GONE);
            viewHolder.btn_download_flac.setVisibility(View.GONE);
        }

        final String type=fileType;

        viewHolder.btn_download_flac.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(System.currentTimeMillis()-lastClickTime<CLICK_FILTER_TIME_BETWEEN_TWO_CLICK){
                    return;
                }
                lastClickTime=System.currentTimeMillis();
                Intent intent=new Intent(context,MusicPlayActivity.class);
                intent.putExtra("localMusicInfo",musicInfo);
                intent.putExtra("music_file_location",musicInfo.getMusicFileLocation()+".flac");
                context.startActivity(intent);
            }
        });

        viewHolder.btn_download_mp3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(System.currentTimeMillis()-lastClickTime<CLICK_FILTER_TIME_BETWEEN_TWO_CLICK){
                    return;
                }
                lastClickTime=System.currentTimeMillis();
                Intent intent=new Intent(context,MusicPlayActivity.class);
                intent.putExtra("localMusicInfo",musicInfo);
                intent.putExtra("music_file_location",musicInfo.getMusicFileLocation()+".mp3");
                context.startActivity(intent);
            }
        });

        viewHolder.ll_music_info.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Toast.makeText(context,"长按"+musicInfo.getSongName(),Toast.LENGTH_SHORT).show();
                try {
                    String folder=new SharePreferencesManager().getConfig("save_path","alover",context);
                    File file=new File(DefaultParams.SAVE_FLODER.replace("alover",folder)+musicInfo.getSongName()+"_"+musicInfo.getSingerName()+"_"+musicInfo.getAlbumName()+type);

                    Uri uri=null;

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        uri= FileProvider.getUriForFile(context,"com.yize.fakemusic.filemanager"+".fileprovider",file );
                    } else {
                        uri = Uri.fromFile(file);
                    }

                    Intent intent=new Intent(Intent.ACTION_SEND);
                    intent.putExtra(Intent.EXTRA_STREAM,uri);
                    intent.setType("audio/*");
                    intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    context.startActivity(intent);

                } catch (Exception e) {
                    e.printStackTrace();
                }
                return true;
            }
        });

    }

    @Override
    public int getItemCount() {
        return localMusicInfos.size();
    }



}