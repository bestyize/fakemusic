package com.yize.fakemusic.qqmusic;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import com.yize.fakemusic.config.DefaultParams;
import com.yize.fakemusic.config.SharePreferencesManager;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.List;
import tool.sun.BASE64Decoder;


/**
 * 文件名       ：SongDetails.java
 * 作用         :搜索歌曲，获取下载链接,既可以单个以可以批量
 * By           :亦泽
 * Date         :2019-1-19 21.11
 * 微信公众号    ：从来不想
 *
 * SearchTool 预留了四个接口
 * public String getLyricBySongid(MusicInfo musicInfo)
 * public void saveLyricToLocal(MusicInfo musicInfo)
 * public String getLyricFromLocal(MusicInfo musicInfo)
 * public void getHotCommontBySongId(String songid)
 *
 *
 */

public class SongDetails {
    private String folder;

    public SongDetails(String folder) {
        this.folder = folder;
    }

    /**
     * 获取网页内容
     * @param link
     * @param mode
     * @return
     * @throws Exception
     */
    private String getWebContent(String link,int mode)throws Exception{
        URL url=new URL(link);
        HttpURLConnection conn=(HttpURLConnection)url.openConnection();
        conn.setRequestMethod("GET");
        conn.setConnectTimeout(5000);
        //conn.setRequestProperty("Host","c.y.qq.com");
        //conn.setRequestProperty("Connection","keep-alive");
        // conn.setRequestProperty("Accept","*/*");
        //conn.setRequestProperty("Accept-Encoding","gzip,deflate,br");
        if(mode==0){
            conn.setRequestProperty("Referer","https://y.qq.com/n/yqq/song/001OyHbk2MSIi4.html");
        }
        //conn.setRequestProperty("Accept-Language","zh-CN,zh;q=0.9");

        BufferedReader reader=new BufferedReader(new InputStreamReader(conn.getInputStream(),"utf-8"));
        StringBuilder response=new StringBuilder();
        String line;
        while((line=reader.readLine())!=null){
            response.append(line);
        }
        conn.disconnect();
        reader.close();
        return response.toString();
    }

    /**
     * 通过songId获取歌词，返回歌词
     * @param musicInfo
     * @return
     */
    public String getLyricBySongid(MusicInfo musicInfo){
        if(checkLyricExist(musicInfo)){

            return getLyricFromLocal(musicInfo);
        }
        try {
            String requestLink="https://c.y.qq.com/lyric/fcgi-bin/fcg_query_lyric.fcg?nobase64=0&musicid="+musicInfo.getSongid();
            String response=getWebContent(requestLink,0);
            if(response.length()>100){
                response=response.substring(response.indexOf("{"),response.lastIndexOf("}")+1);
            }
            JsonObject jsonObject= (JsonObject) new JsonParser().parse(response);
            String lyricBase64=jsonObject.get("lyric").getAsString();
            BASE64Decoder decoder=new BASE64Decoder();
            String lyric=new String(decoder.decodeBuffer(lyricBase64),"utf-8");
            musicInfo.setLyric(lyric);
            saveLyricToLocal(musicInfo);
            return lyric;
        } catch (Exception e) {
            e.printStackTrace();
            return e.toString();
        }

    }


    /**
     * 搜到的歌词保存到本地
     * @param musicInfo
     */
    public void saveLyricToLocal(MusicInfo musicInfo){
        File file=new File(DefaultParams.SAVE_FLODER.replace("alover",folder) +musicInfo.getSongname()+"_"+musicInfo.getSingersName()+".lrc");
        try {
            FileWriter writer=new FileWriter(file);
            writer.write(musicInfo.getLyric());
            writer.flush();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * 检查是不是已经下载
     * @param musicInfo
     * @return
     */
    private boolean checkLyricExist(MusicInfo musicInfo){
        File file=new File(DefaultParams.SAVE_FLODER.replace("alover",folder)+musicInfo.getSongname()+"_"+musicInfo.getSingersName()+".lrc");
        if(file.exists()){
            return true;
        }
        return false;
    }

    /**
     * 检查本地读取歌词
     * @param musicInfo
     * @return
     */
    public String getLyricFromLocal(MusicInfo musicInfo){
        File file=new File(DefaultParams.SAVE_FLODER.replace("alover",folder)+musicInfo.getSongname()+"_"+musicInfo.getSingersName()+".lrc");
        try {
            BufferedReader reader=new BufferedReader(new FileReader(file));
            StringBuilder sb=new StringBuilder();
            String line;
            while((line=reader.readLine())!=null){
                sb.append(line);
            }
            reader.close();
            return sb.toString();

        }catch (IOException e){
            return "Error";
        }

    }

    /**
     * 获取某首歌的热评价
     * @param songid
     */
    public void getHotCommontBySongId(String songid){

        try {
            String response=getWebContent("https://c.y.qq.com/base/fcgi-bin/fcg_global_comment_h5.fcg?g_tk=5381&biztype=1&topid="+songid+"&cmd=8&pagenum=0&pagesize=1",1);
            response=response.substring(response.indexOf("hot_comment"));
            response=response.substring(response.indexOf("["),response.indexOf("]")+1);
            //SearchActivity.show(response);
            Gson gson=new Gson();
            List<Comment> commentList=gson.fromJson(response,new TypeToken<List<Comment>>(){}.getType());
            int i=0;
            for(Comment myComment:commentList){
                //SearchActivity.show((i++)+"\t昵称："+myComment.getNick()+"QQ:"+myComment.getRootcommentuin()+"\t赞："+myComment.getPraisenum()+"\t评论："+myComment.getRootcommentcontent()+"\n");
            }

        }catch (Exception e){

        }
    }

    /**
     * 获取MV地址
     * @param musicInfo
     * @return
     */
    public String getVideoDownloadLink(MusicInfo musicInfo) {

        try {
            String vid=musicInfo.getVid();
            String data = "{\"getMVInfo\":{\"module\":\"video.VideoDataServer\",\"method\":\"get_video_info_batch\",\"param\":{\"vidlist\":[\"yizevid\"],\"required\":[\"vid\",\"sid\",\"gmid\",\"type\",\"name\",\"cover_pic\",\"video_switch\",\"msg\"],\"from\":\"h5.mvplay\"}},\"getMVUrl\":{\"module\":\"gosrf.Stream.MvUrlProxy\",\"method\":\"GetMvUrls\",\"param\":{\"vids\":[\"yizevid\"],\"from\":\"h5.mvplay\"},\"request_typet\":10001}}";
            data = data.replaceAll("yizevid", vid);
            data = URLEncoder.encode(data, "UTF-8");
            String requestLink = "https://u.y.qq.com/cgi-bin/musicu.fcg?g_tk=5381&uin=0&ct=23&cv=0&format=json&callback=qmv_jsonp_2&data=" + data;
            URL url = new URL(requestLink);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setConnectTimeout(4000);
            BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
            reader.close();
            conn.disconnect();
            String response = sb.toString();
            String qualityType = response.substring(response.indexOf("\"getMVUrl\":") + 6, response.indexOf("\"hls\":"));
            qualityType = qualityType.substring(qualityType.indexOf("\"mp4\":") + 6, qualityType.lastIndexOf("]") + 1);
            Gson gson=new Gson();
            List<MVInfo> mvInfoList=gson.fromJson(qualityType,new TypeToken<List<MVInfo>>(){}.getType());
            for(int i=mvInfoList.size()-1;i>=0;i--){
                String[] downloadLinks=mvInfoList.get(i).getFreeflow_url();
                if(downloadLinks.length>=1){
                    return downloadLinks[0];
                }
            }
            return "未发现";


        } catch (IOException e) {
            return e.toString();
        }
    }




    public String lyricFormat(String params){
        return params.replaceAll("\\[[\\d:.]+\\]","\n");
    }

    /**
     * 解析MV下载地址
     */
    class MVInfo{
        private String[] freeflow_url;

        public String[] getFreeflow_url() {
            return freeflow_url;
        }

        public void setFreeflow_url(String[] freeflow_url) {
            this.freeflow_url = freeflow_url;
        }
    }


}

