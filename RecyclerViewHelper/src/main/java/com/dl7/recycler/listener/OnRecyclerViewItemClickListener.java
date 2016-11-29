package com.dl7.recycler.listener;


import android.view.View;

/**
 * Interface definition for a callback to be invoked when an item in this
 * AdapterView has been clicked.
 */
public interface OnRecyclerViewItemClickListener {
    /**
     * Callback method to be invoked when an item in this AdapterView has
     * been clicked.
     *
     * @param view     The view within the AdapterView that was clicked (this
     *                 will be a view provided by the adapter)
     * @param position The position of the view in the adapter.
     */
    public void onItemClick(View view, int position);
}
