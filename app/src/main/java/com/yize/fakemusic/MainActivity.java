package com.yize.fakemusic;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.service.notification.NotificationListenerService;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.yize.fakemusic.config.ConfigActivity;
import com.yize.fakemusic.config.SharePreferencesManager;
import com.yize.fakemusic.download.DownloadActivity;
import com.yize.fakemusic.filemanager.LocalMusicAdapter;
import com.yize.fakemusic.filemanager.LocalMusicInfo;
import com.yize.fakemusic.filemanager.LocalMusicManager;
import com.yize.fakemusic.search.SearchActivity;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private List<LocalMusicInfo> localMusicInfoList=new ArrayList<>();
    private Toolbar toolbar_title;
    private RadioGroup rg_main_button;
    private RadioButton rb_main_button_rank,rb_main_button_search,rb_main_button_comment,rb_main_button_myself;
    private TextView tv_no_music_downloaded;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        checkPermission();
        showLocalFile();
    }

    private void initView(){
        toolbar_title=(Toolbar)findViewById(R.id.toolbar_title);
        toolbar_title.setTitle("本地音乐");
        setSupportActionBar(toolbar_title);
        tv_no_music_downloaded=(TextView)findViewById(R.id.tv_no_music_downloaded);
        rg_main_button=(RadioGroup)findViewById(R.id.rg_main_button);
        rb_main_button_rank=(RadioButton)findViewById(R.id.rb_main_button_rank);
        rb_main_button_search=(RadioButton)findViewById(R.id.rb_main_button_search);
        rb_main_button_comment=(RadioButton)findViewById(R.id.rb_main_button_comment);
        rb_main_button_myself=(RadioButton)findViewById(R.id.rb_main_button_myself);
        rg_main_button.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId){
                    case R.id.rb_main_button_rank:
                        //show ranking page
                        toolbar_title.setTitle("排行榜");
                        break;
                    case R.id.rb_main_button_search:
                        Intent searchIntent=new Intent(MainActivity.this,SearchActivity.class);
                        startActivity(searchIntent);
                        toolbar_title.setTitle("搜索");
                        break;
                    case R.id.rb_main_button_comment:
                        //show local file
                        showLocalFile();
                        toolbar_title.setTitle("本地文件");
                        break;
                    case R.id.rb_main_button_myself:
                        //show myself
                        toolbar_title.setTitle("我的");

                        Intent myIntent=new Intent(MainActivity.this, ConfigActivity.class);
                        startActivity(myIntent);
                        MainActivity.this.overridePendingTransition(0, 0);
                        break;
                    default :
                        break;
                }
            }
        });
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_toolbar,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.main_toolbar_delete:
//                show("删除");
                break;
            case R.id.main_toobar_search:
                Intent intent=new Intent(this, SearchActivity.class);
                startActivity(intent);
                break;
            default :
                break;
        }
        return true;
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){

            default :
                break;
        }
    }


    public void showLocalFile(){
        setTitle("本地文件");
        String folder=new SharePreferencesManager().getConfig("save_path","alover",this);
        LocalMusicManager fileManager=new LocalMusicManager(folder);
        String myFolder=fileManager.getRootStorePath().replace("alover",folder);
        localMusicInfoList=fileManager.getLocalMusicInfo(myFolder);

        RecyclerView localMusicRecycleView=(RecyclerView)findViewById(R.id.main_recycleview);
        localMusicRecycleView.setLayoutManager(new LinearLayoutManager(this));
        LocalMusicAdapter localMusicAdapter=new LocalMusicAdapter(localMusicInfoList);
        if(localMusicInfoList.size()>0){
            tv_no_music_downloaded.setVisibility(View.GONE);
        }
        else
        {
            tv_no_music_downloaded.setVisibility(View.VISIBLE);
        }
        localMusicRecycleView.setAdapter(localMusicAdapter);
    }

    public void showRanking(){

    }

    public void showMyself(){

    }

    /**
     * 检查权限，网络读写等
     */
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
