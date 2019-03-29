package com.yize.fakemusic.config;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences;

/**
 * SharePreferencesManager 管理
 */
public class SharePreferencesManager {

    /**
     * SharedPreferfences配置文件
     * @param name  参数名
     * @param values 参数值
     * @param context 上下文
     */
    public void addConfig(String name,String values, Context context){
        SharedPreferences.Editor editor=context.getSharedPreferences("data",Context.MODE_PRIVATE).edit();
        editor.putString(name,values);
        editor.apply();
    }
    public void addConfig(String name,Integer values, Context context){
        SharedPreferences.Editor editor=context.getSharedPreferences("data",Context.MODE_PRIVATE).edit();
        editor.putInt(name,values);
        editor.apply();
    }
    public void addConfig(String name,Boolean values, Context context){
        SharedPreferences.Editor editor=context.getSharedPreferences("data",Context.MODE_PRIVATE).edit();
        editor.putBoolean(name,values);
        editor.apply();
    }

    /**
     * 读取本地配置
     * @param name 参数名
     * @param context 上下文
     * @return 参数值
     */
    public String getConfig(String name,String defaultValue, Context context){
        SharedPreferences pref=context.getSharedPreferences("data",Context.MODE_PRIVATE);
        return pref.getString(name,defaultValue);
    }


}
