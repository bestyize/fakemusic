package com.yize.fakemusic.filemanager;

import android.os.Environment;

import com.yize.fakemusic.config.DefaultParams;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class LocalMusicManager {
    private String folder;

    public LocalMusicManager(String folder) {
        this.folder = folder;
    }

    public String getRootStorePath(){
//        return Environment.getExternalStorageDirectory().getPath()+ File.separator+"Music"+File.separator;
        return DefaultParams.SAVE_FLODER;
    }


    public List<LocalMusicInfo> getLocalMusicInfo(String filePath){
        List<LocalMusicInfo> mFileInfo=new ArrayList<>();
        File file=new File(filePath);
        if(!file.exists()){
            file.mkdirs();
        }
        File files[]=file.listFiles();
        for(File myfile:files){
            String fileName=myfile.getName();
            if(fileName.contains("_")&&(fileName.endsWith(".flac")||fileName.endsWith(".mp3")||fileName.endsWith(".mp4"))&&myfile.isFile()){
                String songName=myfile.getName().substring(0,myfile.getName().indexOf("_"));
                String singerName=fileName.substring(fileName.indexOf("_")+1,fileName.lastIndexOf("_"));
                String albumName=fileName.substring(fileName.lastIndexOf("_")+1,fileName.lastIndexOf("."));
                String songType=fileName.substring(fileName.lastIndexOf("."));
                String musicFileLocation=getRootStorePath()+fileName.substring(0,fileName.lastIndexOf("."));
                long fileSize=myfile.length();
                long lastMOdifiedTime=myfile.lastModified();
                LocalMusicInfo fileInfo=new LocalMusicInfo(songName,singerName,albumName,songType,fileSize,lastMOdifiedTime,musicFileLocation);
                mFileInfo.add(fileInfo);
            }

        }
        return mFileInfo;
    }
}
