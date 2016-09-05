package com.dl7.myapp.adapter;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;

import com.dl7.helperlibrary.adapter.BaseQuickAdapter;
import com.dl7.helperlibrary.adapter.BaseViewHolder;
import com.dl7.myapp.R;
import com.dl7.myapp.api.bean.BeautyPhotoBean;
import com.dl7.myapp.utils.ImageLoader;
import com.orhanobut.logger.Logger;

import java.util.List;

/**
 * Created by long on 2016/9/5.
 * 美图 Adapter
 */
public class BeautyPhotoAdapter extends BaseQuickAdapter<BeautyPhotoBean> {


    public BeautyPhotoAdapter(Context context) {
        super(context);
    }

    public BeautyPhotoAdapter(Context context, List<BeautyPhotoBean> data) {
        super(context, data);
    }

    @Override
    protected int attachLayoutRes() {
        return R.layout.adapter_beauty_photo;
    }

    @Override
    protected void convert(BaseViewHolder holder, final BeautyPhotoBean item) {
        ImageView ivPhoto = holder.getView(R.id.iv_photo);
        int[] photoSize = _clipPhotoSize(item.getPixel());
        Logger.i(holder.itemView.getWidth()+"");
        if (photoSize != null) {
            ImageLoader.loadFitOverride(mContext, item.getImgsrc(), ivPhoto, R.mipmap.photo_default,
                    photoSize[0], photoSize[1]);
//            ImageLoader.loadCenterCrop(mContext, item.getImgsrc(), ivPhoto, R.mipmap.icon_default);
        } else {
            ImageLoader.loadFit(mContext, item.getImgsrc(), ivPhoto, R.mipmap.photo_default);
        }
        holder.setText(R.id.tv_title, item.getTitle());
        holder.getConvertView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });
    }

    private int[] _clipPhotoSize(String pixel) {
        int[] size = new int[2];
        int index = pixel.indexOf("*");
        if (index != -1) {
            try {
                size[0] = Integer.parseInt(pixel.substring(0, index));
                size[1] = Integer.parseInt(pixel.substring(index + 1));
            } catch (NumberFormatException e) {
                Logger.e(e.toString());
                return null;
            }
        }

        return size;
    }
}
