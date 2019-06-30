package com.jiaoerzhang.platform.lib_net.network.rx.download;

/**
 * <pre>
 * </pre>
 */
public interface DownLoadListener
{
    void onProgress(int progress, boolean downSuc, boolean downFailed);
}
