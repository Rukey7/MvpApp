package com.dl7.recycler.adapter;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.util.SparseArray;
import android.view.ViewGroup;

import com.dl7.recycler.entity.MultiItemEntity;

import java.util.List;

/**
 * https://github.com/CymChad/BaseRecyclerViewAdapterHelper
 */
public abstract class BaseMultiItemQuickAdapter<T extends MultiItemEntity> extends BaseQuickAdapter {

    static final int MULTI_ADAPTER_NULL_LAYOUT = -1;
    /**
     * layouts indexed with their types
     */
    private SparseArray<Integer> layouts;

    /**
     * Same as QuickAdapter#QuickAdapter(Context,int) but with
     * some initialization data.
     *
     * @param data    A new list is created out of this one to avoid mutable list
     */
    public BaseMultiItemQuickAdapter(Context context, List<T> data) {
        super(context, data);
        attachItemType();
        if (layouts == null || layouts.size() == 0) {
            throw new IllegalAccessError("Please add item use 'addItemType(int type, int layoutResId)' " +
                    "in the 'attachItemType()' method");
        }
    }
    public BaseMultiItemQuickAdapter(Context context) {
        this(context, null);
    }

    @Override
    protected int getDefItemViewType(int position) {
        return ((MultiItemEntity) mData.get(position)).getItemType();
    }

    @Override
    protected BaseViewHolder onCreateDefViewHolder(ViewGroup parent, int viewType) {
        return createBaseViewHolder(parent, getLayoutId(viewType));
    }

    private int getLayoutId(int viewType) {
        return layouts.get(viewType);
    }

    protected void addItemType(int type, @LayoutRes int layoutResId) {
        if (layouts == null) {
            layouts = new SparseArray<>();
        }
        layouts.put(type, layoutResId);
    }

    @Override
    protected int attachLayoutRes() {
        return MULTI_ADAPTER_NULL_LAYOUT;
    }

    @Override
    protected void convert(BaseViewHolder holder, Object item) {
        convert(holder, (T) item);
    }

    protected abstract void attachItemType();

    protected abstract void convert(BaseViewHolder holder, T item);

}


