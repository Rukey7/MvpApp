package com.dl7.mvp.module.news.special;

import com.dl7.mvp.api.bean.SpecialInfo;
import com.dl7.mvp.adapter.item.SpecialItem;
import com.dl7.mvp.module.base.IBaseView;

import java.util.List;

/**
 * Created by long on 2016/8/26.
 * 专题View接口
 */
public interface ISpecialView extends IBaseView {

    /**
     * 显示数据
     * @param specialItems 新闻
     */
    void loadData(List<SpecialItem> specialItems);

    /**
     * 添加头部
     * @param specialBean
     */
    void loadBanner(SpecialInfo specialBean);
}
