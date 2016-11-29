package com.dl7.recycler.listener;


import android.view.View;

/**
 * Interface definition for a callback to be invoked when an item in this
 * view has been clicked and held
 */
public interface OnRecyclerViewItemLongClickListener {
    /**
     * callback method to be invoked when an item in this view has been
     * click and held
     *
     * @param view     The view whihin the AbsListView that was clicked
     * @param position The position of the view int the adapter
     * @return true if the callback consumed the long click ,false otherwise
     */
    public boolean onItemLongClick(View view, int position);
}
