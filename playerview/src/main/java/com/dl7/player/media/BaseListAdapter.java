package com.dl7.player.media;

import android.content.Context;
import android.widget.BaseAdapter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by long on 2016/6/7.
 * ListView的基础适配器
 */
public abstract class BaseListAdapter<T> extends BaseAdapter {

    protected Context mContext;
    protected List<T> mDatas;


    public BaseListAdapter(Context context) {
        this.mContext = context;
        this.mDatas = new ArrayList<>();
    }

    public BaseListAdapter(Context context, List<T> datas) {
        this.mContext = context;
        this.mDatas = datas;
    }

    public BaseListAdapter(Context context, T[] datas) {
        this.mContext = context;
        this.mDatas = new ArrayList<T>();
        Collections.addAll(mDatas, datas);
    }


    @Override
    public int getCount() {
        return mDatas.size();
    }

    @Override
    public T getItem(int position) {
        return mDatas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }



    /**
     * 更新数据，替换原有数据
     * @param items
     */
    public void updateItems(List<T> items) {
        mDatas = items;
        notifyDataSetChanged();
    }

    /**
     * 插入一条数据
     * @param item 数据
     */
    public void addItem(T item) {
        mDatas.add(0, item);
        notifyDataSetChanged();
    }

    /**
     * 插入一条数据
     * @param item 数据
     * @param position 插入位置
     */
    public void addItem(T item, int position) {
        position = Math.min(position, mDatas.size());
        mDatas.add(position, item);
        notifyDataSetChanged();
    }

    /**
     * 在列表尾添加一串数据
     * @param items
     */
    public void addItems(List<T> items) {
        mDatas.addAll(items);
    }

    /**
     * 移除一条数据
     * @param position 位置
     */
    public void removeItem(int position) {
        if (position > mDatas.size() - 1) {
            return;
        }
        mDatas.remove(position);
        notifyDataSetChanged();
    }

    /**
     * 移除一条数据
     * @param item 数据
     */
    public void removeItem(T item) {
        int pos = 0;
        for (T info : mDatas) {
            if (item.hashCode() == info.hashCode()) {
                removeItem(pos);
                break;
            }
            pos++;
        }
    }

    /**
     * 清除所有数据
     */
    public void cleanItems() {
        mDatas.clear();
        notifyDataSetChanged();
    }
}
