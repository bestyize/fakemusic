package com.yize.fakemusic.qqmusic;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.List;

/**
 * 文件名       ：DownloadInfo.java
 * 作用         :获取下载链接的逻辑
 * 备注         :使用了Gson
 * By           :亦泽
 * Date         :2019-1-19 21.11
 * 微信公众号    ：从来不想
 *
 * DownloadInfo 预留了四个接口，仅限于包内调用
 * protected String getWebContent(final String requestLink)
 * protected List<MusicInfo> parseJsonWithGson(String parentJson)
 * protected String [] getMusicDownloadLinkInQuickMode(String subLink)
 * protected String [] getMusicDownloadLink(String requestLink)
 *
 *
 */

public class DownloadInfo {

    /**
     * 抓来的几个服务器
     */
    protected static final String serverAdress[]={"http://streamoc.music.tc.qq.com/","http://isure.stream.qqmusic.qq.com/","http://dl.stream.qqmusic.qq.com/"};

    /**
     * 抓网页内容的，接受Json数据
     * @param requestLink
     * @return
     */
    public String getWebContent(final String requestLink){
        try {
            URL url = new URL(requestLink);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setConnectTimeout(4000);
            if (conn.getResponseCode() == 200||conn.getResponseCode()==206) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
                StringBuilder response = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }
                reader.close();
                conn.disconnect();
                return response.toString();

            }
            return "Error";
        }catch (Exception e){
            return "Error";
        }



    }


    /**
     * 通过Gson来解析处理后的Json数据
     * @param parentJson 处理后的Json数据
     * @return
     */
    protected List<MusicInfo> parseJsonWithGson(String parentJson){
        //parentJson.replaceAll("/","").replaceAll("<","").replaceAll(">","").replaceAll("_","");
        Gson gson=new Gson();
        List<MusicInfo> musicInfoList=gson.fromJson(parentJson,new TypeToken<List<MusicInfo>>(){}.getType());
        int count=0;
        for(int i=0;i<musicInfoList.size();i++){
            String singersName="";
            String p=musicInfoList.get(i).getPay().toString();
            Log.i("pay",p);
            String singers=gson.toJson(musicInfoList.get(i).getSinger());
            List<Singer> singerList=gson.fromJson(singers,new TypeToken<List<Singer>>(){}.getType());
            for(Singer singer:singerList){
                if(singerList.size()>1){
                    singersName=singerList.get(0).getName()+"&"+singerList.get(1).getName();
                    break;
                }else{
                    singersName=singerList.get(0).getName();
                }
            }
            musicInfoList.get(i).setSingersName(singersName);
            String saveFileName=musicInfoList.get(i).getSongname()+"_"+musicInfoList.get(i).getSingersName()+"_"+musicInfoList.get(i).getAlbumname();
            musicInfoList.get(i).setSaveFileName(saveFileName);
        }
        return musicInfoList;


    }

    /**
     * 使用默认的路线解析，比较快
     * @param subLink
     * @return
     */
    protected String [] getMusicDownloadLinkInQuickMode(String subLink){
        subLink=subLink.substring(0,subLink.indexOf("fromtag="))+"fromtag=8";
        String link=serverAdress[0]+subLink;
        String flacDownloadLink=link.replace("C400","F000").replace(".m4a",".flac");
        String apeDownloadLink=link.replace("C400","A000").replace(".m4a",".ape");
        String hmp3DownloadLink=link.replace("C400","M800").replace(".m4a",".mp3");
        String lmp3DownloadLink=link.replace("C400","M500").replace(".m4a",".mp3");
        String links[]={flacDownloadLink,apeDownloadLink,hmp3DownloadLink,lmp3DownloadLink,link};
        return links;

    }


    /**
     * 查询多个服务器状态，以确保可用，比较可靠，但是速度很慢，这就是为什么比前者慢接近一半的原因了
     * @param requestLink
     * @return
     */
    protected String [] getMusicDownloadLink(String requestLink){
        try {
            requestLink=requestLink.substring(0,requestLink.indexOf("fromtag="))+"fromtag=";
            for(int i=0;i<serverAdress.length;i++){
                for(int j=8;j<100;j++){
                    String link=serverAdress[i]+requestLink+j;

                    URL url = new URL(serverAdress[i]+requestLink+j);
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestMethod("GET");
                    conn.setConnectTimeout(4000);
                    int responseCode=conn.getResponseCode();
                    conn.disconnect();
                    if(responseCode==200){
                        //return link;
                        String flacDownloadLink=link.replace("C400","F000").replace(".m4a",".flac");
                        String apeDownloadLink=link.replace("C400","A000").replace(".m4a",".ape");
                        String hmp3DownloadLink=link.replace("C400","M800").replace(".m4a",".mp3");
                        String lmp3DownloadLink=link.replace("C400","M500").replace(".m4a",".mp3");
                        String links[]={flacDownloadLink,apeDownloadLink,hmp3DownloadLink,lmp3DownloadLink,link};
                        return links;
                    }
                }
            }
        }catch (Exception e){

        }
        return null;
    }

    /**
     * 日志工具
     * @param obj
     */
    private static void show(Object ...obj){
        Log.i("Status:",obj[0].toString());
    }
}