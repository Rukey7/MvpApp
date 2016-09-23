package com.dl7.myapp.adapter;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;

import com.dl7.helperlibrary.adapter.BaseQuickAdapter;
import com.dl7.helperlibrary.adapter.BaseViewHolder;
import com.dl7.myapp.R;
import com.dl7.myapp.api.bean.BeautyPhotoBean;
import com.dl7.myapp.utils.DefIconFactory;
import com.dl7.myapp.utils.ImageLoader;
import com.orhanobut.logger.Logger;

import java.util.List;

/**
 * Created by long on 2016/9/5.
 * 美图 Adapter
 */
@Deprecated
public class BeautyPhotoAdapter extends BaseQuickAdapter<BeautyPhotoBean> {

    // 图片的宽度
    private int mPhotoWidth;

    public BeautyPhotoAdapter(Context context) {
        super(context);
        int widthPixels = context.getResources().getDisplayMetrics().widthPixels;
        int marginPixels = context.getResources().getDimensionPixelOffset(R.dimen.photo_margin_width);
        mPhotoWidth = widthPixels - marginPixels;
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
        int photoHeight = _calcPhotoHeight(item.getPixel());
        if (photoHeight != -1) {
            ImageLoader.loadFitOverride(mContext, item.getImgsrc(), ivPhoto, DefIconFactory.provideIcon(),
                    mPhotoWidth, photoHeight);
        } else {
            ImageLoader.loadFit(mContext, item.getImgsrc(), ivPhoto, DefIconFactory.provideIcon());
        }
        holder.setText(R.id.tv_title, item.getTitle());
        holder.getConvertView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });
    }

    /**
     * 计算图片要显示的高度
     * @param pixel
     * @return
     */
    private int _calcPhotoHeight(String pixel) {
        int height = -1;
        int index = pixel.indexOf("*");
        if (index != -1) {
            try {
                int widthPixel = Integer.parseInt(pixel.substring(0, index));
                int heightPixel = Integer.parseInt(pixel.substring(index + 1));
                height = (int) (heightPixel * (mPhotoWidth * 1.0f / widthPixel));
            } catch (NumberFormatException e) {
                Logger.e(e.toString());
                return -1;
            }
        }

        return height;
    }
}
