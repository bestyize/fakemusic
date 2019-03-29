package com.yize.fakemusic.config;

import android.content.ActivityNotFoundException;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.yize.fakemusic.R;

public class ConfigActivity extends AppCompatActivity implements View.OnClickListener {
    private Toolbar toolbar_title;
    private LinearLayout ll_my_setting,ll_money,ll_use_help,ll_wechat;
    private TextView tv_notice_myself;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_config);
        initView();
    }

    private void initView(){
        toolbar_title=(Toolbar)findViewById(R.id.toolbar_title);
        toolbar_title.setTitle("我的");
        setSupportActionBar(toolbar_title);
        tv_notice_myself=(TextView)findViewById(R.id.tv_notice_myself);
        tv_notice_myself.setText(new SharePreferencesManager().getConfig("notice_daily","希音音乐|无损音质",getApplicationContext()));

        ll_my_setting=(LinearLayout)findViewById(R.id.ll_my_setting);
        ll_money=(LinearLayout)findViewById(R.id.ll_money);
        ll_use_help=(LinearLayout)findViewById(R.id.ll_use_help);
        ll_wechat=(LinearLayout)findViewById(R.id.ll_wechat);

        ll_my_setting.setOnClickListener(this);
        ll_money.setOnClickListener(this);
        ll_use_help.setOnClickListener(this);
        ll_wechat.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.ll_my_setting:
                Intent intent1=new Intent(ConfigActivity.this,SettingActivity.class);
                startActivity(intent1);
                break;
            case R.id.ll_money:
                goToOfficalSite();
                break;
            case R.id.ll_use_help:
                ClipboardManager clipboardManager1=(ClipboardManager)getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clipData1=ClipData.newPlainText("公众号","从来不想");
                clipboardManager1.setPrimaryClip(clipData1);
                Toast.makeText(ConfigActivity.this,"请到公众号后台回复：使用教程",Toast.LENGTH_LONG).show();
                goToWeChat();
                break;
            case R.id.ll_wechat:
                ClipboardManager clipboardManager=(ClipboardManager)getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clipData=ClipData.newPlainText("公众号","从来不想");
                clipboardManager.setPrimaryClip(clipData);
                Toast.makeText(ConfigActivity.this,"已复制到粘贴板，请搜索：从来不想",Toast.LENGTH_LONG).show();
                goToWeChat();

                break;

            default:

                break;
        }
    }

    /**
     * 跳转到微信
     */
    private void goToWeChat(){
        try {
            Intent intent = new Intent(Intent.ACTION_MAIN);
            ComponentName cmp = new ComponentName("com.tencent.mm","com.tencent.mm.ui.LauncherUI");
            //ComponentName cmp = new ComponentName("com.tencent.mm","com.tencent.mm.plugin.base.stub.WXCustomSchemeEntryActivity");
            intent.addCategory(Intent.CATEGORY_LAUNCHER);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setComponent(cmp);
            startActivity(intent);
        } catch (ActivityNotFoundException e) {
            // TODO: handle exception

        }
    }

    private void goToOfficalSite(){
        Uri uri = Uri.parse("http://www.xiyin.cf");
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(intent);
    }
}
