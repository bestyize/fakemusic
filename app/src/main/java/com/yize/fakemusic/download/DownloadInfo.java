package com.yize.fakemusic.download;

public class DownloadInfo {
    private String downloadLink;
    private String fileName;
    private long fileLength;
    private long lastModifiedTime;

    public DownloadInfo(String downloadLink, String fileName, long fileLength, long lastModifiedTime) {
        this.downloadLink = downloadLink;
        this.fileName = fileName;
        this.fileLength = fileLength;
        this.lastModifiedTime = lastModifiedTime;
    }

    public String getDownloadLink() {
        return downloadLink;
    }

    public void setDownloadLink(String downloadLink) {
        this.downloadLink = downloadLink;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public long getFileLength() {
        return fileLength;
    }

    public void setFileLength(long fileLength) {
        this.fileLength = fileLength;
    }

    public long getLastModifiedTime() {
        return lastModifiedTime;
    }

    public void setLastModifiedTime(long lastModifiedTime) {
        this.lastModifiedTime = lastModifiedTime;
    }
}
