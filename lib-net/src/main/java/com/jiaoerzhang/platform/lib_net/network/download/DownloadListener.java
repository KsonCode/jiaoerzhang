package com.jiaoerzhang.platform.lib_net.network.download;

public interface DownloadListener {
    void onStartDownload();

    void onProgress(int progress);

    void onFinishDownload();

    void onFail(String errorInfo);

}
