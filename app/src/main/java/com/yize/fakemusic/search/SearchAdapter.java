package com.yize.fakemusic.search;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.yize.fakemusic.R;
import com.yize.fakemusic.config.SharePreferencesManager;
import com.yize.fakemusic.download.DownloadActivity;
import com.yize.fakemusic.download.DownloadListAdapter;
import com.yize.fakemusic.download.DownloadListener;
import com.yize.fakemusic.qqmusic.MusicInfo;
import com.yize.fakemusic.qqmusic.SearchListener;
import com.yize.fakemusic.qqmusic.SearchTool;

import java.util.List;

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.ViewHolder> {

    private List<MusicInfo> musicInfoList;
    private Context context;



    static class ViewHolder extends RecyclerView.ViewHolder{
        ImageView imageview_quality;
        TextView music_name,singer_name,music_subtitle;
        Button button_mp3,button_flac,button_mv;
        LinearLayout ll_song_info,ll_mv,ll_flac,ll_mp3;
        public ViewHolder(View view) {
            super(view);
            ll_song_info=(LinearLayout)view.findViewById(R.id.ll_song_info);
            ll_mv=(LinearLayout)view.findViewById(R.id.ll_mv);
            ll_flac=(LinearLayout)view.findViewById(R.id.ll_flac);
            ll_mp3=(LinearLayout)view.findViewById(R.id.ll_mp3);
            imageview_quality=(ImageView)view.findViewById(R.id.imageview_quality);
            music_name=(TextView)view.findViewById(R.id.music_name);
            singer_name=(TextView)view.findViewById(R.id.singer_name);
            music_subtitle=(TextView)view.findViewById(R.id.music_subtitle);
            button_mp3=(Button)view.findViewById(R.id.button_mp3);
            button_flac=(Button)view.findViewById(R.id.button_flac);
            button_mv=(Button)view.findViewById(R.id.button_mv);

        }
    }

    public SearchAdapter(List<MusicInfo> musicInfoList){
        this.musicInfoList=musicInfoList;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        if(context==null){
            context=viewGroup.getContext();
        }
        View view= LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.adapter_music_list,viewGroup,false);
        final ViewHolder holder=new ViewHolder(view);
//        View view=LayoutInflater.from(context).inflate(R.layout.adapter_music_list,viewGroup);
//        ViewHolder holder=new ViewHolder(view);




        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int i) {
        final MusicInfo musicInfo=musicInfoList.get(i);
        holder.music_name.setText(musicInfo.getSongname());
        holder.music_subtitle.setText(musicInfo.getAlbumname());
        holder.singer_name.setText(musicInfo.getSingersName());


        holder.button_flac.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(context,DownloadActivity.class);
                intent.putExtra("musicInfo",musicInfo);
                intent.putExtra("quality","FLAC");
                v.getContext().startActivity(intent);




            }
        });

        holder.button_mp3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(context,DownloadActivity.class);
                intent.putExtra("musicInfo",musicInfo);
                intent.putExtra("quality","MP3");
                v.getContext().startActivity(intent);
            }
        });


        holder.button_mv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(context,DownloadActivity.class);
                intent.putExtra("musicInfo",musicInfo);
                intent.putExtra("quality","MP4");
                v.getContext().startActivity(intent);
            }
        });

        String workMode=new SharePreferencesManager().getConfig("work_mode","mode_normal",context);
        if(workMode.contains("mode_unormal")){
            if(musicInfo.getVid().length()<1){
                holder.ll_mv.setVisibility(View.GONE);
            }else {
                holder.ll_mv.setVisibility(View.VISIBLE);
            }
            holder.ll_flac.setVisibility(View.GONE);
            holder.imageview_quality.setImageResource(R.mipmap.hq_icon);

        }else{

            if(musicInfo.getVid().length()<1){
                holder.ll_mv.setVisibility(View.GONE);
            }else {
                holder.ll_mv.setVisibility(View.VISIBLE);
            }
            if(musicInfo.getSizeflac()>0){
                holder.imageview_quality.setImageResource(R.mipmap.sq_icon);
                holder.ll_flac.setVisibility(View.VISIBLE);
            }else {
                holder.ll_flac.setVisibility(View.GONE);
                holder.imageview_quality.setImageResource(R.mipmap.hq_icon);
            }
        }

//        if(musicInfo.getPay().toString().contains("payplay=1.0")){
//            holder.ll_flac.setVisibility(View.GONE);
//            holder.ll_mp3.setVisibility(View.GONE);
//
//        }


    }

    @Override
    public int getItemCount() {
        return musicInfoList.size();
    }


}