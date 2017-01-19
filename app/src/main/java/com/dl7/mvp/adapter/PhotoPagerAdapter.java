package com.dl7.mvp.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.dl7.mvp.R;
import com.dl7.mvp.local.table.BeautyPhotoInfo;
import com.dl7.mvp.utils.ImageLoader;
import com.github.ybq.android.spinkit.SpinKitView;

import java.util.Collections;
import java.util.List;

import uk.co.senab.photoview.PhotoView;
import uk.co.senab.photoview.PhotoViewAttacher;

/**
 * Created by long on 2016/8/29.
 * 图片浏览适配器
 */
public class PhotoPagerAdapter extends PagerAdapter {

    // 限制 Adapter 在倒数第3个位置时启动加载更多回调
    private final static int LOAD_MORE_LIMIT = 3;
    private List<BeautyPhotoInfo> mImgList;
    private Context mContext;
    private OnTapListener mTapListener;
    private OnLoadMoreListener mLoadMoreListener;
    private boolean mIsLoadMore = false;


    public PhotoPagerAdapter(Context context, List<BeautyPhotoInfo> imgList) {
        this.mContext = context;
        this.mImgList = imgList;
    }

    public PhotoPagerAdapter(Context context) {
        this.mContext = context;
        this.mImgList = Collections.EMPTY_LIST;
    }

    public void updateData(List<BeautyPhotoInfo> imgList) {
        this.mImgList = imgList;
        notifyDataSetChanged();
    }

    public void addData(List<BeautyPhotoInfo> imgList) {
        mImgList.addAll(imgList);
        notifyDataSetChanged();
        mIsLoadMore = false;
    }

    @Override
    public int getCount() {
        return mImgList.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.adapter_photo_pager, null, false);
        final PhotoView photo = (PhotoView) view.findViewById(R.id.iv_photo);
        final SpinKitView loadingView = (SpinKitView) view.findViewById(R.id.loading_view);
        final TextView tvReload = (TextView) view.findViewById(R.id.tv_reload);

        if ((position >= mImgList.size() - LOAD_MORE_LIMIT) && !mIsLoadMore) {
            if (mLoadMoreListener != null) {
                mIsLoadMore = true;
                mLoadMoreListener.onLoadMore();
            }
        }

        final RequestListener<String, GlideDrawable> requestListener = new RequestListener<String, GlideDrawable>() {
            @Override
            public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                loadingView.setVisibility(View.GONE);
                tvReload.setVisibility(View.VISIBLE);
                return false;
            }

            @Override
            public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                loadingView.setVisibility(View.GONE);
                tvReload.setVisibility(View.GONE);
                photo.setImageDrawable(resource);
                return true;
            }
        };
        ImageLoader.loadCenterCrop(mContext, mImgList.get(position % mImgList.size()).getImgsrc(), photo, requestListener);
        photo.setOnPhotoTapListener(new PhotoViewAttacher.OnPhotoTapListener() {
            @Override
            public void onPhotoTap(View view, float x, float y) {
                if (mTapListener != null) {
                    mTapListener.onPhotoClick();
                }
            }
        });
        tvReload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tvReload.setVisibility(View.GONE);
                loadingView.setVisibility(View.VISIBLE);
                ImageLoader.loadCenterCrop(mContext, mImgList.get(position % mImgList.size()).getImgsrc(), photo, requestListener);
            }
        });
        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    /**
     * 是否收藏
     * @param position
     * @return
     */
    public boolean isLoved(int position) {
        return mImgList.get(position).isLove();
    }

    /**
     * 是否点赞
     * @param position
     * @return
     */
    public boolean isPraise(int position) {
        return mImgList.get(position).isPraise();
    }

    /**
     * 是否下载
     * @param position
     * @return
     */
    public boolean isDownload(int position) {
        return mImgList.get(position).isDownload();
    }

    /**
     * 获取对应数据
     * @param position
     * @return
     */
    public BeautyPhotoInfo getData(int position) {
        return mImgList.get(position);
    }

    public BeautyPhotoInfo getData(String url) {
        for (BeautyPhotoInfo bean : mImgList) {
            if (bean.getImgsrc().equals(url)) {
                return bean;
            }
        }
        return null;
    }

    public void setTapListener(OnTapListener listener) {
        mTapListener = listener;
    }

    public void setLoadMoreListener(OnLoadMoreListener loadMoreListener) {
        mLoadMoreListener = loadMoreListener;
    }

    public interface OnTapListener {
        void onPhotoClick();
    }

    public interface OnLoadMoreListener {
        void onLoadMore();
    }
}
