package com.yize.fakemusic.qqmusic;


import java.io.Serializable;

public class MusicInfo implements Serializable {
    private String albummid;
    private String albumname;
    private Object singer;
    private String singersName;
    private int size128;
    private int size320;
    private int sizeape;
    private int sizeflac;
    private int sizeogg;
    private String songid;//用来配合songmid抓歌词
    private String songmid;
    private String songname;
    private String strMediaMid;
    private String vid;
    private String downloadLink;
    private String flacDownloadLink;
    private String apeDownloadLink;
    private String hmp3DownloadLink;
    private String lmp3DownloadLink;
    private String saveFileName;
    private String lyric;
    private String mvDownloadLink;
    private Object pay;

    public Object getPay() {
        return pay;
    }

    public void setPay(Object pay) {
        this.pay = pay;
    }

    public String getSaveFileName() {
        return saveFileName;
    }

    public void setSaveFileName(String saveFileName) {
        this.saveFileName = saveFileName;
    }

    public String getDownloadLink() {
        return downloadLink;
    }

    public void setDownloadLink(String downloadLink) {
        this.downloadLink = downloadLink;
    }

    public String getAlbummid() {
        return albummid;
    }

    public void setAlbummid(String albummid) {
        this.albummid = albummid;
    }

    public String getAlbumname() {
        return albumname;
    }

    public void setAlbumname(String albumname) {
        this.albumname = albumname;
    }

    public Object getSinger() {
        return singer;
    }

    public void setSinger(Object singer) {
        this.singer = singer;
    }


    public String getSingersName() {
        return singersName;
    }

    public void setSingersName(String singersName) {
        this.singersName = singersName;
    }

    public String getSongid() {
        return songid;
    }

    public void setSongid(String songid) {
        this.songid = songid;
    }

    public int getSize128() {
        return size128;
    }

    public void setSize128(int size128) {
        this.size128 = size128;
    }

    public int getSize320() {
        return size320;
    }

    public void setSize320(int size320) {
        this.size320 = size320;
    }

    public int getSizeape() {
        return sizeape;
    }

    public void setSizeape(int sizeape) {
        this.sizeape = sizeape;
    }

    public int getSizeflac() {
        return sizeflac;
    }

    public void setSizeflac(int sizeflac) {
        this.sizeflac = sizeflac;
    }

    public int getSizeogg() {
        return sizeogg;
    }

    public void setSizeogg(int sizeogg) {
        this.sizeogg = sizeogg;
    }

    public String getSongmid() {
        return songmid;
    }

    public void setSongmid(String songmid) {
        this.songmid = songmid;
    }

    public String getSongname() {
        return songname;
    }

    public void setSongname(String songname) {
        this.songname = songname;
    }

    public String getStrMediaMid() {
        return strMediaMid;
    }

    public void setStrMediaMid(String strMediaMid) {
        this.strMediaMid = strMediaMid;
    }

    public String getVid() {
        return vid;
    }

    public void setVid(String vid) {
        this.vid = vid;
    }

    public String getFlacDownloadLink() {
        return flacDownloadLink;
    }

    public void setFlacDownloadLink(String flacDownloadLink) {
        this.flacDownloadLink = flacDownloadLink;
    }

    public String getApeDownloadLink() {
        return apeDownloadLink;
    }

    public void setApeDownloadLink(String apeDownloadLink) {
        this.apeDownloadLink = apeDownloadLink;
    }

    public String getHmp3DownloadLink() {
        return hmp3DownloadLink;
    }

    public void setHmp3DownloadLink(String hmp3DownloadLink) {
        this.hmp3DownloadLink = hmp3DownloadLink;
    }

    public String getLmp3DownloadLink() {
        return lmp3DownloadLink;
    }

    public void setLmp3DownloadLink(String lmp3DownloadLink) {
        this.lmp3DownloadLink = lmp3DownloadLink;
    }

    public String getLyric() {
        return lyric;
    }

    public void setLyric(String lyric) {
        this.lyric = lyric;
    }

    public String getMvDownloadLink() {
        return mvDownloadLink;
    }

    public void setMvDownloadLink(String mvDownloadLink) {
        this.mvDownloadLink = mvDownloadLink;
    }

}

class Singer{
    private String name;
    private int id;
    private String mid;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMid() {
        return mid;
    }

    public void setMid(String mid) {
        this.mid = mid;
    }
}


class Comment{
    private String avatarurl;//头像
    private String nick;//昵称
    private int praisenum;//赞的数量
    private String rootcommentcontent;
    private String rootcommentuin;//评论者的QQ号

    public String getAvatarurl() {
        return avatarurl;
    }

    public void setAvatarurl(String avatarurl) {
        this.avatarurl = avatarurl;
    }

    public String getNick() {
        return nick;
    }

    public void setNick(String nick) {
        this.nick = nick;
    }

    public int getPraisenum() {
        return praisenum;
    }

    public void setPraisenum(int praisenum) {
        this.praisenum = praisenum;
    }

    public String getRootcommentcontent() {
        return rootcommentcontent;
    }

    public void setRootcommentcontent(String rootcommentcontent) {
        this.rootcommentcontent = rootcommentcontent;
    }

    public String getRootcommentuin() {
        return rootcommentuin;
    }

    public void setRootcommentuin(String rootcommentuin) {
        this.rootcommentuin = rootcommentuin;
    }
}