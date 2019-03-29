package com.yize.fakemusic.download;

public interface DownloadListener {
    void onSuccess();
    void onFailed(String reason);
    void onCanceled();
    void onPaused();
    void onProgress(Long progress);
}
interface DownloadItemClickListener{
    void onItemClick(int position);
}
