package com.yize.fakemusic.download;

import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;

import com.yize.fakemusic.config.DefaultParams;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import com.yize.fakemusic.config.*;
import java.net.URL;

public class DownloadTask extends AsyncTask <String,Long,Integer>{
    //下载完的状态
    private static final int DOWNLOAD_SUCCESS=0;
    private static final int DOWNLOAD_FAILED=1;
    private static final int DOWNLOAD_CANCELED=2;
    private static final int DOWNLOAD_PAUSED=3;

    //检查状态
    private boolean isCanceled=false;
    private boolean isPaused=false;


    //记录上一次进度
    private long lastProgress;
    //注册一个接口，方便状态回调
    private DownloadListener downloadListener;
    public DownloadTask(DownloadListener downloadListener){
        this.downloadListener=downloadListener;
    }


    /**
     * 在子线程中执行的操作，传入下载地址等，返回的是整数，使用Progress发送给onProgressUpdate
     * @param params 需要传入两个参数，第一个是下载地址，第二个是文件名称
     * @return
     */
    @Override
    protected Integer doInBackground(String... params) {
        String downloadUrl=params[0];
        long totalLength=getContentLength(downloadUrl);
        if(totalLength==0){
            return DOWNLOAD_FAILED;
        }
        long downloadedLength=0;

        String saveFolder=params[2];
        File folder=new File(saveFolder);
        if(!folder.exists()){
            folder.mkdirs();
        }
        String fileName=params[1];
        File file=new File(saveFolder+fileName);
        if(file.exists()){
            downloadedLength=file.length();
            if(downloadedLength==totalLength){
                return DOWNLOAD_SUCCESS;
            }
            if(downloadedLength>totalLength){
                file.delete();
                downloadedLength=0;
            }
        }

        try {
            RandomAccessFile raf=new RandomAccessFile(file,"rwd");
            raf.seek(downloadedLength);
            long startPos=downloadedLength;
            long endPos=totalLength;
            URL url=new URL(downloadUrl);
            HttpURLConnection conn=(HttpURLConnection)url.openConnection();
            conn.setRequestMethod("GET");
            conn.setConnectTimeout(4000);
            conn.setReadTimeout(4000);
            conn.setRequestProperty("User-Agent","Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/71.0.3578.80 Safari/537.36");
            //断点下载
            conn.setRequestProperty("Range","bytes="+startPos+"-"+endPos);
            BufferedInputStream inputStream=new BufferedInputStream(conn.getInputStream());
            //缓冲
            byte b[]=new byte[4096];
            int len;
            while((len=inputStream.read(b))!=-1){
                if(isCanceled){
                    return DOWNLOAD_CANCELED;
                }else if(isPaused){
                    return  DOWNLOAD_PAUSED;
                }else{
                    raf.write(b,0,len);
                    downloadedLength+=len;
                    publishProgress(downloadedLength,totalLength);
                }

            }
            raf.close();
            inputStream.close();
            conn.disconnect();
            return DOWNLOAD_SUCCESS;
        } catch (Exception e) {
            e.printStackTrace();
            return DOWNLOAD_FAILED;
        }

    }

    @Override
    protected void onPostExecute(Integer state) {
        super.onPostExecute(state);
        switch (state){
            case DOWNLOAD_SUCCESS:
                downloadListener.onSuccess();
                break;
            case DOWNLOAD_FAILED:
                downloadListener.onFailed("未知原因");
                break;
            case DOWNLOAD_CANCELED:
                downloadListener.onCanceled();
                break;
            case DOWNLOAD_PAUSED:
                downloadListener.onPaused();
                break;
            default :
                break;
        }

    }

    @Override
    protected void onProgressUpdate(Long... values) {
        super.onProgressUpdate(values);
        long downloadedLength=values[0];
        long totalLength=values[1];
        long progress=downloadedLength*100/totalLength;
        if(progress>lastProgress){
            downloadListener.onProgress(progress);
            lastProgress=progress;
        }
    }

    private long getContentLength(String downloadLink){
        try {
            URL url=new URL(downloadLink);
            HttpURLConnection conn=(HttpURLConnection)url.openConnection();
            conn.setRequestMethod("GET");
            conn.setConnectTimeout(4000);
            conn.setReadTimeout(4000);
            long contentLength=conn.getContentLength();
            conn.disconnect();
            return contentLength;
        } catch (Exception e) {
            Log.i("获取文件大小失败：",e.toString());
            return 0;
        }

    }

    public void pauseDownload(){
        isPaused=true;
    }
    public void canceledDownload(){
        isCanceled=true;
    }


}
