package com.yize.fakemusic.search;

import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.SearchView;
import android.widget.TextView;

import com.yize.fakemusic.R;

import java.util.ArrayList;
import java.util.List;

import com.yize.fakemusic.config.SettingActivity;
import com.yize.fakemusic.config.SharePreferencesManager;
import com.yize.fakemusic.qqmusic.*;

public class SearchActivity extends AppCompatActivity {
    private SearchView search_view;
    private TextView tv_on_loading;
    public static final int UPDATE_TEXT=1;
    List<MusicInfo> musicInfoList=new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        initView();
    }

    private void initView(){
        tv_on_loading=(TextView)findViewById(R.id.tv_on_loading);
        tv_on_loading.setVisibility(View.GONE);
        search_view=(SearchView)findViewById(R.id.search_view);

        search_view.setIconified(false);
        search_view.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                if(musicInfoList.size()>0){
                    musicInfoList.clear();
                }

                tv_on_loading.setVisibility(View.VISIBLE);
                controlPanel();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                return false;
            }
        });
    }


    private void controlPanel() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                String max_num=new SharePreferencesManager().getConfig("max_num","100", getApplicationContext());

                String query=search_view.getQuery().toString();
                musicInfoList=new SearchTool().searchMusic(query,Integer.valueOf(max_num));//先获取列表
                Message message=new Message();
                message.what=UPDATE_TEXT;
                handler.sendMessage(message);
            }
        }).start();

    }



    private Handler handler=new Handler(){
        public void handleMessage(Message msg){
            switch(msg.what){
                case UPDATE_TEXT:
                    initList();
                    break;
                default:
                    break;
            }
        }
    };


    private void initList(){
        RecyclerView recyclerView=(RecyclerView)findViewById(R.id.recycleView_music_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(SearchActivity.this));
        recyclerView.setAdapter(new SearchAdapter(musicInfoList));
        tv_on_loading.setVisibility(View.GONE);
    }



}