package com.dl7.recycler.listener;

/**
 * Created by Rukey7 on 2016/9/1.
 * Item 移动监听
 */
public interface OnItemMoveListener {

    /**
     * Item 移动
     * @param fromPosition  初始位置
     * @param toPosition    移动位置
     */
    void onItemMove(int fromPosition, int toPosition);
}
