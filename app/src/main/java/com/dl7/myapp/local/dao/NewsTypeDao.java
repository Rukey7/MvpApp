package com.dl7.myapp.local.dao;

import android.content.Context;

import com.dl7.myapp.local.table.DaoSession;
import com.dl7.myapp.local.table.NewsTypeBean;
import com.dl7.myapp.local.table.NewsTypeBeanDao;
import com.dl7.myapp.utils.AssetsHelper;
import com.dl7.myapp.utils.GsonHelper;

import java.util.List;

/**
 * Created by long on 2016/8/31.
 * 新闻分类数据访问
 */
public class NewsTypeDao {

    // 所有栏目
    private static List<NewsTypeBean> sAllChannels;


    private NewsTypeDao() {
    }

    /**
     * 更新本地数据，如果数据库新闻列表栏目为 0 则添加头 3 个栏目
     * @param context
     * @param daoSession
     */
    public static void updateLocalData(Context context, DaoSession daoSession) {
        sAllChannels = GsonHelper.convertEntities(AssetsHelper.readData(context, "NewsChannel"), NewsTypeBean.class);
        NewsTypeBeanDao beanDao = daoSession.getNewsTypeBeanDao();
        if (beanDao.count() == 0) {
            beanDao.insertInTx(sAllChannels.subList(0, 3));
        }
    }

    /**
     * 获取所有栏目
     * @return
     */
    public static List<NewsTypeBean> getAllChannels() {
        return sAllChannels;
    }
}
