package com.dl7.myapp.module.special;

import com.dl7.myapp.api.bean.SpecialBean;
import com.dl7.myapp.entity.SpecialItem;
import com.dl7.myapp.module.base.IBaseView;

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
    void loadBanner(SpecialBean specialBean);
}
