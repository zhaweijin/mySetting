package com.hiveview.cloudtv.settings.download;

import com.hiveview.cloudtv.settings.download.IDownloadCallback;

interface IDownloadService {
    void registerCallback(IDownloadCallback cb);
    void unregisterCallback(IDownloadCallback cb);
    void startDownload(boolean isGroupUser);
    void stopDownload();
    boolean isDownloading();
}