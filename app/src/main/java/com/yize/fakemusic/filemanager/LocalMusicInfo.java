package com.yize.fakemusic.filemanager;


import java.io.Serializable;

public class LocalMusicInfo implements Serializable {
    private String songName;
    private String songType;
    private String singerName;
    private String albumName;
    private long FileSize;
    private long lastModifiedTime;
    private String musicFileLocation;

    public LocalMusicInfo(String songName, String singerName,String albumName,String songType, long fileSize, long lastModifiedTime,String musicFileLocation) {
        this.songName = songName;
        this.singerName=singerName;
        this.albumName=albumName;
        this.songType = songType;
        this.FileSize = fileSize;
        this.lastModifiedTime = lastModifiedTime;
        this.musicFileLocation=musicFileLocation;
    }

    public String getSongName() {
        return songName;
    }

    public void setSongName(String songName) {
        this.songName = songName;
    }


    public String getSingerName() {
        return singerName;
    }

    public void setSingerName(String singerName) {
        this.singerName = singerName;
    }

    public String getAlbumName() {
        return albumName;
    }

    public void setAlbumName(String albumName) {
        this.albumName = albumName;
    }

    public String getSongType() {
        return songType;
    }

    public void setSongType(String songType) {
        this.songType = songType;
    }

    public long getFileSize() {
        return FileSize;
    }

    public void setFileSize(long fileSize) {
        FileSize = fileSize;
    }

    public long getLastModifiedTime() {
        return lastModifiedTime;
    }

    public void setLastModifiedTime(long lastModifiedTime) {
        this.lastModifiedTime = lastModifiedTime;
    }

    public String getMusicFileLocation() {
        return musicFileLocation;
    }

    public void setMusicFileLocation(String musicFileLocation) {
        this.musicFileLocation = musicFileLocation;
    }
}
