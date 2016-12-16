package com.dl7.mvp.adapter;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.ImageView;

import com.daimajia.numberprogressbar.NumberProgressBar;
import com.dl7.downloaderlib.entity.FileInfo;
import com.dl7.downloaderlib.model.DownloadStatus;
import com.dl7.mvp.R;
import com.dl7.mvp.local.table.VideoInfo;
import com.dl7.mvp.rxbus.RxBus;
import com.dl7.mvp.utils.DefIconFactory;
import com.dl7.mvp.utils.ImageLoader;
import com.dl7.mvp.utils.StringUtils;
import com.dl7.recycler.adapter.BaseQuickAdapter;
import com.dl7.recycler.adapter.BaseViewHolder;

/**
 * Created by long on 2016/12/15.
 * Video 下载适配器
 */
public class VideoCacheAdapter extends BaseQuickAdapter<VideoInfo> {

    private final RxBus mRxBus;

    public VideoCacheAdapter(Context context, RxBus rxBus) {
        super(context);
        mRxBus = rxBus;
    }

    @Override
    protected int attachLayoutRes() {
        return R.layout.adapter_video_cache;
    }

    @Override
    protected void convert(final BaseViewHolder holder, final VideoInfo item) {
        setTag(holder, item.getVideoUrl());
        ImageView ivThumb = holder.getView(R.id.iv_thumb);
        ImageLoader.loadFitCenter(mContext, item.getCover(), ivThumb, DefIconFactory.provideIcon());
        NumberProgressBar pbDownload = holder.getView(R.id.pb_download);
        pbDownload.setMax((int) item.getTotalSize());
        holder.setText(R.id.tv_total_size, StringUtils.convertStorageNoB(item.getTotalSize()));
        _switchViews(holder, item);

        holder.getView(R.id.btn_download).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                _handleClick(holder, item);
            }
        });
    }

    /**
     * 更新视图
     * @param holder
     * @param item
     */
    private void _switchViews(BaseViewHolder holder, VideoInfo item) {
        switch (item.getDownloadStatus()) {
            case DownloadStatus.DOWNLOADING:
                NumberProgressBar pbDownload = holder.getView(R.id.pb_download);
                holder.setText(R.id.tv_load_size, StringUtils.convertStorageNoB(item.getLoadedSize()) + "/")
                        .setText(R.id.tv_speed, StringUtils.convertStorageNoB(item.getDownloadSpeed()));
                pbDownload.setProgress((int) item.getLoadedSize());
            case DownloadStatus.START:
                if (!holder.isVisible(R.id.pb_download) || !holder.isSelected(R.id.btn_download)) {
                    holder.setVisible(R.id.pb_download, true)
                            .setSelected(R.id.btn_download, true)
                            .setTextColor(R.id.tv_speed, ContextCompat.getColor(mContext, R.color.download_normal));
                }
                break;
            case DownloadStatus.STOP:
                if (holder.isVisible(R.id.pb_download) || holder.isSelected(R.id.btn_download)) {
                    holder.setVisible(R.id.pb_download, false)
                            .setSelected(R.id.btn_download, false)
                            .setText(R.id.tv_speed, "下载暂停")
                            .setTextColor(R.id.tv_speed, ContextCompat.getColor(mContext, R.color.download_stop))
                            .setText(R.id.tv_load_size, "");
                }
                break;
            case DownloadStatus.COMPLETE:
                mRxBus.post(item);
            case DownloadStatus.CANCEL:
                // 移除
                removeItem(item);
                break;
            case DownloadStatus.ERROR:
                holder.setText(R.id.tv_speed, "异常出错，请重新下载")
                        .setVisible(R.id.pb_download, false)
                        .setSelected(R.id.btn_download, false)
                        .setTextColor(R.id.tv_speed, ContextCompat.getColor(mContext, R.color.download_error));
                break;
        }
    }

    /**
     * 点击处理
     * @param holder
     * @param item
     */
    private void _handleClick(BaseViewHolder holder, VideoInfo item) {
        switch (item.getDownloadStatus()) {
            case DownloadStatus.NORMAL:
            case DownloadStatus.ERROR:
            case DownloadStatus.STOP:
                holder.setText(R.id.tv_speed, "处理中..")
                        .setTextColor(R.id.tv_speed, ContextCompat.getColor(mContext, R.color.download_normal));
//                DownloaderWrapper.start(info);
                break;

            case DownloadStatus.DOWNLOADING:
                holder.setText(R.id.tv_speed, "即将暂停")
                        .setTextColor(R.id.tv_speed, ContextCompat.getColor(mContext, R.color.download_normal));
//                DownloaderWrapper.stop(info);
                break;

            default:
                break;
        }
    }

    /*********************************************************************/

    /**
     * 更新下载状态
     *
     * @param info 应用信息
     */
    private void _updateDownload(VideoInfo info) {
        BaseViewHolder holder = getTag(info.getVideoUrl());
        if (holder == null) {
            return;
        }
        _switchViews(holder, info);
    }

    /**
     * 查找 VideoInfo
     * url:http://flv2.bn.netease.com/videolib3/1501/28/wlncJ2098/SD/wlncJ2098-mobile.mp4
     *
     * @param name wlncJ2098-mobile.mp4 -> 文件名
     * @return
     */
    private VideoInfo _find(String name) {
//        int index;
        for (VideoInfo info : mData) {
            if (name.equals(StringUtils.clipFileName(info.getVideoUrl()))) {
                return info;
            }
//            index = info.getVideoUrl().lastIndexOf("/");
//            if (index != -1) {
//                // 截取出文件名
//                if (info.getVideoUrl().substring(index).equals(name)) {
//                    return info;
//                }
//            }
        }
        return null;
    }

    /**
     * 更新下载状态
     *
     * @param fileInfo 文件信息
     */
    public void updateDownload(FileInfo fileInfo) {
        VideoInfo info = _find(fileInfo.getName());
        if (info == null) {
            return;
        }
        info.setDownloadStatus(fileInfo.getStatus());
        info.setTotalSize(fileInfo.getTotalBytes());
        info.setLoadedSize(fileInfo.getLoadBytes());
        info.setDownloadSpeed(fileInfo.getSpeed());

        _updateDownload(info);
    }
}
