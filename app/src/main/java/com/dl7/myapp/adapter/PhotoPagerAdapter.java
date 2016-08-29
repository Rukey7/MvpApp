package com.dl7.myapp.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import com.dl7.myapp.R;
import com.dl7.myapp.utils.ImageLoader;

import java.util.List;

import uk.co.senab.photoview.PhotoView;

/**
 * Created by long on 2016/8/29.
 * 图片浏览适配器
 */
public class PhotoPagerAdapter extends PagerAdapter {

    private List<String> mImgList;
    private Context mContext;


    public PhotoPagerAdapter(Context context, List<String> imgList) {
        this.mContext = context;
        this.mImgList = imgList;
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
    public Object instantiateItem(ViewGroup container, int position) {
//        View view = LayoutInflater.from(mContext).inflate(R.layout.adapter_photo_set, null, false);
//        PhotoView photo = (PhotoView) view.findViewById(R.id.iv_photo);
        PhotoView photo = new PhotoView(mContext);
        ImageLoader.loadFitCenter(mContext, mImgList.get(position), photo, R.mipmap.icon_default);
        container.addView(photo);
        return photo;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }
}
