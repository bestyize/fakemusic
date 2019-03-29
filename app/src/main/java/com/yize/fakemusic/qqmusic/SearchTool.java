package com.yize.fakemusic.qqmusic;

import android.util.Log;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

/**
 * 文件名       ：SearchTool.java
 * 作用         :搜索歌曲，获取下载链接,既可以单个以可以批量
 * By           :亦泽
 * Date         :2019-1-19 21.11
 * 微信公众号    ：从来不想
 *
 * SearchTool 预留了五个接口
 * public List<MusicInfo> searchMusic(String musicName, int number)
 * public MusicInfo getDownloadLinkInStableMode(MusicInfo musicInfo)
 * public List<MusicInfo> getDownloadLinkInStableMode(List<MusicInfo> musicInfoList)
 * public MusicInfo getDownloadLinkInQuickMode(MusicInfo musicInfo)
 * public List<MusicInfo>  getDownloadLinkInQuickMode(List<MusicInfo> musicInfoList)
 *
 *
 */
public class SearchTool {
    private List<MusicInfo> musicInfoList=new ArrayList<>();
    private DownloadInfo downloadInfo=new DownloadInfo();

    /**
     * 搜索音乐，可以是歌曲或者歌手或者专辑
     * @param musicName 搜索关键词
     * @param number  搜索数量
     * @return 返回一段基本信息，包括歌曲的基本信息，也包括歌曲的歌手信息，songmid,专辑信息，保存的文件名
     */
    public List<MusicInfo> searchMusic(String musicName, int number) {
        String encodeKeyWord = URLEncoder.encode(musicName);
        String searchEnginer = "https://c.y.qq.com/soso/fcgi-bin/client_search_cp?cr=1&n=" + number + "&w=" + encodeKeyWord;
        String response=downloadInfo.getWebContent(searchEnginer);
        String responseJson=response.substring(response.indexOf("\"list\":[{")+7,response.lastIndexOf(",\"totalnum\""));
        musicInfoList=downloadInfo.parseJsonWithGson(responseJson);
        return musicInfoList;
    }



    /**
     * 用比较稳定的方法抓取下载链接，这个接口是我用Fidder抓到的,可以查到多个服务器，批量抓链接
     * @param musicInfo 单个音乐的简单信息
     * @return 返回带下载链接的歌曲信息
     */
    public MusicInfo getDownloadLinkInStableMode(MusicInfo musicInfo){
        String songmid=musicInfo.getSongmid();
        String data = "{\"req\":{\"module\":\"CDN.SrfCdnDispatchServer\",\"method\":\"GetCdnDispatch\",\"param\":{\"guid\":\"2985825869\",\"calltype\":0,\"userip\":\"\"}},\"req_0\":{\"module\":\"vkey.GetVkeyServer\",\"method\":\"CgiGetVkey\",\"param\":{\"guid\":\"2985825869\",\"songmid\":[\"yize_songmid\"],\"songtype\":[0],\"uin\":\"0\",\"loginflag\":1,\"platform\":\"20\"}},\"comm\":{\"uin\":0,\"format\":\"json\",\"ct\":20,\"cv\":0}}";
        String request = data.replace("yize_songmid", songmid);
        String postUrl = "https://u.y.qq.com/cgi-bin/musicu.fcg?data="+URLEncoder.encode(request);
        try {
            URL url = new URL(postUrl);
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
                String sublink=response.substring(response.indexOf("purl")+7,response.indexOf("qmdlfromtag"));
                String musicDownloadLink[]=downloadInfo.getMusicDownloadLink(sublink);
                musicInfo.setFlacDownloadLink(musicDownloadLink[0]);
                musicInfo.setApeDownloadLink(musicDownloadLink[1]);
                musicInfo.setHmp3DownloadLink(musicDownloadLink[2]);
                musicInfo.setLmp3DownloadLink(musicDownloadLink[3]);
                musicInfo.setDownloadLink(musicDownloadLink[4]);

            }
        }catch (Exception e){

        }
        return musicInfo;
    }


    /**
     * 用比较稳定的方法抓取下载链接，这个接口是我用Fidder抓到的,可以查到多个服务器，批量抓链接
     * @param musicInfoList 搜索来的歌曲列表的基本信息，主要是利用songmid
     * @return 返回含有下载链接的批量列表的信息
     */
    public List<MusicInfo> getDownloadLinkInStableMode(List<MusicInfo> musicInfoList) {

        for(MusicInfo musicInfo:musicInfoList){
            getDownloadLinkInStableMode(musicInfo);
        }
        return musicInfoList;
    }


    /**
     * 用普通的方法抓取下载链接，这个接口也是我用Fidder抓到的,但只有一个服务器，相对来说搜索速度比稳固的要快
     * @param musicInfo 单个歌曲的简单信息
     * @return 单个歌曲带下载链接的信息
     */
    public MusicInfo getDownloadLinkInQuickMode(MusicInfo musicInfo){
        String sublink=null;
        String songmid=musicInfo.getSongmid();
        String postLink = "http://c.y.qq.com/base/fcgi-bin/fcg_music_express_mobile3.fcg?cid=205361747&uin=0&songmid=yize_songmid&filename=yize_filename&guid=2985825869";
        postLink = postLink.replace("yize_songmid", songmid).replace("yize_filename", "C400" + songmid + ".m4a");

        try{
            URL url = new URL(postLink);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setConnectTimeout(4000);
            int responseCode=conn.getResponseCode();
            if (responseCode==200||responseCode==206) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
                StringBuilder response = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }
                reader.close();
                conn.disconnect();
                String veky = response.substring(response.indexOf("vkey") + 7, response.lastIndexOf("\""));
                String filename = response.substring(response.indexOf("filename") + 11, response.lastIndexOf(",") - 1);
                sublink = filename + "?vkey=" + veky + "&guid=2985825869&uin=0&fromtag=8";
                String musicDownloadLink[]=downloadInfo.getMusicDownloadLinkInQuickMode(sublink);
                musicInfo.setFlacDownloadLink(musicDownloadLink[0]);
                musicInfo.setApeDownloadLink(musicDownloadLink[1]);
                musicInfo.setHmp3DownloadLink(musicDownloadLink[2]);
                musicInfo.setLmp3DownloadLink(musicDownloadLink[3]);
                musicInfo.setDownloadLink(musicDownloadLink[4]);
            }
        }catch (Exception e){
            Log.i("Te",e.toString());
        }
        return musicInfo;
    }

    /**
     * 用普通的方法抓取下载链接，这个接口也是我用Fidder抓到的,但只有一个服务器，相对来说搜索速度比稳固的要快
     * @param musicInfoList
     * @return 带下载链接的歌曲信息列表
     */
    public List<MusicInfo>  getDownloadLinkInQuickMode(List<MusicInfo> musicInfoList){
        if (musicInfoList.size()==0||musicInfoList==null){
            return null;
        }

        String sublink=null;
        for(MusicInfo musicInfo:musicInfoList){
            getDownloadLinkInQuickMode(musicInfo);
        }
        return musicInfoList;

    }


    /**
     * 日志
     * @param obj
     */
    private static void show(Object ...obj){
        Log.i("Status:",obj[0].toString());
    }

}
