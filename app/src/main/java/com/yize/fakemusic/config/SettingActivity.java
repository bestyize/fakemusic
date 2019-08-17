package com.yize.fakemusic.config;

import android.content.SharedPreferences;
import android.os.Environment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.yize.fakemusic.config.*;

import com.yize.fakemusic.R;

import java.io.File;

public class SettingActivity extends AppCompatActivity {
    private LinearLayout ll_change_folder,ll_change_max_num;
    private Toolbar toolbar_title;
    private Switch switch_mode;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        initView();
    }

    private void initView(){
        toolbar_title=(Toolbar)findViewById(R.id.toolbar_title);
        toolbar_title.setTitle("设置");
        setSupportActionBar(toolbar_title);
        ll_change_folder=(LinearLayout) findViewById(R.id.ll_change_folder);
        ll_change_max_num=(LinearLayout)findViewById(R.id.ll_change_max_num);

        ll_change_folder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showChangeFolderAlert();


            }
        });

        ll_change_max_num.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showChangeMaxSearchAlert();
            }
        });
        switch_mode=(Switch)findViewById(R.id.switch_mode);
        String workMode=new SharePreferencesManager().getConfig("work_mode","mode_normal",getApplicationContext());
        if(workMode.contains("mode_unormal")){
            switch_mode.setChecked(true);

        }else{
            switch_mode.setChecked(false);

        }
        //Toast.makeText(getApplicationContext(),workMode,Toast.LENGTH_SHORT).show();
        switch_mode.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(switch_mode.isChecked()){
                    //写sharedPerference
                    new SharePreferencesManager().addConfig("work_mode","mode_unormal",getApplicationContext());
                    //Toast.makeText(getApplicationContext(),"写sharedPerference ",Toast.LENGTH_SHORT).show();
                }else{
                    new SharePreferencesManager().addConfig("work_mode","mode_normal",getApplicationContext());
                }
            }
        });
    }


    private void showChangeFolderAlert(){
        LayoutInflater inflater=LayoutInflater.from(this);
        View layout=inflater.inflate(R.layout.save_path_setting,null);
        final AlertDialog.Builder builder=new AlertDialog.Builder(SettingActivity.this);
        builder.setView(layout);
        builder.setCancelable(false);
        //builder.create().show();
        Button btn_save_path=(Button)layout.findViewById(R.id.btn_save_path);
        Button btn_cancel_path=(Button)layout.findViewById(R.id.btn_cancel_path);
        TextView tv_setting_title=(TextView)layout.findViewById(R.id.tv_setting_title);
        tv_setting_title.setText("设置最大搜索数量");
        final AlertDialog dialog = builder.show();
        final EditText et_save_path=(EditText)layout.findViewById(R.id.et_save_path);
        et_save_path.setHint(new SharePreferencesManager().getConfig("save_path","alover",getApplicationContext()));
        btn_save_path.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               File file=new File(Environment.getExternalStorageDirectory().getPath()+File.separator+et_save_path.getText().toString());

                new SharePreferencesManager().addConfig("save_path",et_save_path.getText().toString(),getApplicationContext());
                if(!file.exists()){
                    try{
                        file.mkdirs();
                    }catch (Exception e){
                        et_save_path.setHint("不合法的路径名，请重新输入");
                    }

                }
                dialog.dismiss();

            }
        });


        btn_cancel_path.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();

            }
        });
    }




    private void showChangeMaxSearchAlert(){
        LayoutInflater inflater=LayoutInflater.from(this);
        View layout=inflater.inflate(R.layout.save_path_setting,null);
        final AlertDialog.Builder builder=new AlertDialog.Builder(SettingActivity.this);
        builder.setView(layout);
        builder.setCancelable(false);
        //builder.create().show();
        Button btn_save_path=(Button)layout.findViewById(R.id.btn_save_path);
        Button btn_cancel_path=(Button)layout.findViewById(R.id.btn_cancel_path);
        final AlertDialog dialog = builder.show();
        final EditText et_save_path=(EditText)layout.findViewById(R.id.et_save_path);
        et_save_path.setHint(new SharePreferencesManager().getConfig("max_num","100",getApplicationContext()));
        btn_save_path.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //File file=new File(Environment.getExternalStorageDirectory().getPath()+File.separator+et_save_path.getText().toString());
                new SharePreferencesManager().addConfig("max_num",et_save_path.getText().toString(),getApplicationContext());

                dialog.dismiss();

            }
        });


        btn_cancel_path.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();

            }
        });



    }


}