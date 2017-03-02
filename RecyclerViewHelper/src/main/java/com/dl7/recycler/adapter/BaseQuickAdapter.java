package com.dl7.recycler.adapter;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.support.annotation.LayoutRes;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.dl7.recycler.R;
import com.dl7.recycler.helper.ItemTouchHelperAdapter;
import com.dl7.recycler.helper.OnStartDragListener;
import com.dl7.recycler.helper.SimpleItemTouchHelperCallback;
import com.dl7.recycler.listener.OnItemMoveListener;
import com.dl7.recycler.listener.OnRecyclerViewItemClickListener;
import com.dl7.recycler.listener.OnRecyclerViewItemLongClickListener;
import com.dl7.recycler.listener.OnRemoveDataListener;
import com.dl7.recycler.listener.OnRequestDataListener;
import com.github.ybq.android.spinkit.SpinKitView;
import com.github.ybq.android.spinkit.SpriteFactory;
import com.github.ybq.android.spinkit.Style;
import com.github.ybq.android.spinkit.sprite.Sprite;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by long on 2016/4/21.
 * 适配器基类
 */
public abstract class BaseQuickAdapter<T> extends RecyclerView.Adapter<RecyclerView.ViewHolder>
        implements ItemTouchHelperAdapter {

    public static final int HEADER_VIEW = 0x00000111;
    public static final int LOADING_VIEW = 0x00000222;
    public static final int FOOTER_VIEW = 0x00000333;
    public static final int EMPTY_VIEW = 0x00000555;
    public static final int FULL_VIEW = 0x00000666;
    public static final int FULL_VIEW_2 = 0x00000777;
    public static final int FULL_VIEW_3 = 0x00000888;

    protected Context mContext;
    protected int mLayoutResId;
    protected LayoutInflater mLayoutInflater;
    protected List<T> mData;
    private View mParentView;
    // head and footer
    private View mHeaderView;
    private View mFooterView;
    // listener
    private OnRecyclerViewItemClickListener mItemClickListener;
    private OnRecyclerViewItemLongClickListener mItemLongClickListener;
    private OnRequestDataListener onRequestDataListener;
    private OnRemoveDataListener mRemoveDataListener;
    // drag and swipe
    private OnStartDragListener mDragStartListener;
    private OnItemMoveListener mItemMoveListener;
    private SimpleItemTouchHelperCallback mDragCallback;
    private int mDragFixCount = 0;  // 固定数量，从0开始算
    private Drawable mDragFixDrawable;
    // load more
    private boolean mIsLoadMoreEnable;
    private boolean mIsLoadingNow;
    private boolean mIsNoMoreData;
    private String mLoadingStr;
    private View mLoadingView;
    private TextView mLoadingDesc;
    private SpinKitView mLoadingIcon;
    // empty
    private View mEmptyView;


    public BaseQuickAdapter(Context context) {
        this(context, null);
    }

    public BaseQuickAdapter(Context context, List<T> data) {
        mLayoutResId = attachLayoutRes();
        if (mLayoutResId == 0) {
            throw new IllegalAccessError("Layout resource ID must be valid!");
        }
        if (data == null) {
            mData = new ArrayList<>();
        } else {
            this.mData = data;
        }
        this.mContext = context;
        this.mLayoutInflater = LayoutInflater.from(mContext);
    }

    /**
     * 绑定布局
     *
     * @return
     */
    @LayoutRes
    protected abstract int attachLayoutRes();

    /**
     * Implement this method and use the helper to adapt the view to the given item.
     *
     * @param holder A fully initialized helper.
     * @param item   The item that needs to be displayed.
     */
    protected abstract void convert(BaseViewHolder holder, T item);

    @Override
    public int getItemCount() {
        int count = mData.size() + getHeaderViewsCount() + getFooterViewsCount();
        if (count == 0 && mEmptyView != null) {
            return 1;
        }
        if (mIsLoadMoreEnable && mData.size() != 0) {
            count++;
        }
        return count;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        if (mHeaderView != null && position == 0) {
            return HEADER_VIEW;
        } else if ((mData.size() + getHeaderViewsCount() + getFooterViewsCount()) == 0 && mEmptyView != null) {
            return EMPTY_VIEW;
        } else if (mIsLoadMoreEnable) {
            if (position == (getItemCount() - 1)) {
                return LOADING_VIEW;
            } else if (mFooterView != null && position == (getItemCount() - 2)) {
                return FOOTER_VIEW;
            }
        } else if (mFooterView != null && position == (getItemCount() - 1)) {
            return FOOTER_VIEW;
        }
        return getDefItemViewType(position - getHeaderViewsCount());
    }

    /**
     * 获取 ItemView 类型，对于多种布局的 RecyclerView 有用
     * @param position
     * @return
     */
    protected int getDefItemViewType(int position) {
        return super.getItemViewType(position);
    }

    /**
     * Called when a view created by this adapter has been attached to a window.
     * simple to solve item will layout using all
     * {@link #_setFullSpan(RecyclerView.ViewHolder)}
     *
     * @param holder
     */
    @Override
    public void onViewAttachedToWindow(RecyclerView.ViewHolder holder) {
        super.onViewAttachedToWindow(holder);
        int type = holder.getItemViewType();
        if (type == EMPTY_VIEW || type == HEADER_VIEW || type == FOOTER_VIEW || type == LOADING_VIEW || type == FULL_VIEW
                || type == FULL_VIEW_2 || type == FULL_VIEW_3) {
            _setFullSpan(holder);
        }
    }

    /**
     * When set to true, the item will layout using all span area. That means, if orientation
     * is vertical, the view will have full width; if orientation is horizontal, the view will
     * have full height.
     * if the hold view use StaggeredGridLayoutManager they should using all span area
     *
     * @param holder True if this item should traverse all spans.
     */
    protected void _setFullSpan(RecyclerView.ViewHolder holder) {
        if (holder.itemView.getLayoutParams() instanceof StaggeredGridLayoutManager.LayoutParams) {
            StaggeredGridLayoutManager.LayoutParams params = (StaggeredGridLayoutManager.LayoutParams) holder.itemView.getLayoutParams();
            params.setFullSpan(true);
        }
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        RecyclerView.LayoutManager manager = recyclerView.getLayoutManager();
        if (manager instanceof GridLayoutManager) {
            final GridLayoutManager gridManager = ((GridLayoutManager) manager);
            gridManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                @Override
                public int getSpanSize(int position) {
                    int type = getItemViewType(position);
                    return (type == EMPTY_VIEW || type == HEADER_VIEW || type == FOOTER_VIEW || type == LOADING_VIEW
                            || type == FULL_VIEW || type == FULL_VIEW_2 || type == FULL_VIEW_3) ? gridManager.getSpanCount() : 1;
                }
            });
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (mParentView == null) {
            mParentView = parent;
        }
        BaseViewHolder baseViewHolder;
        switch (viewType) {
            case LOADING_VIEW:
                baseViewHolder = new BaseViewHolder(mLoadingView);
                break;
            case HEADER_VIEW:
                baseViewHolder = new BaseViewHolder(mHeaderView);
                break;
            case EMPTY_VIEW:
                baseViewHolder = new BaseViewHolder(mEmptyView);
                break;
            case FOOTER_VIEW:
                baseViewHolder = new BaseViewHolder(mFooterView);
                break;
            default:
                baseViewHolder = onCreateDefViewHolder(parent, viewType);
                // 设置用于单项刷新的tag标识
                baseViewHolder.itemView.setTag(R.id.view_holder_tag, baseViewHolder);
                _initItemClickListener(baseViewHolder);
                break;
        }
        return baseViewHolder;
    }

    /**
     * 创建 ViewHolder
     * @param parent    parent
     * @param viewType  ItemView 类型，对于多种布局的 RecyclerView 有用
     * @return BaseViewHolder
     */
    protected BaseViewHolder onCreateDefViewHolder(ViewGroup parent, int viewType) {
        return createBaseViewHolder(parent, mLayoutResId);
    }

    protected BaseViewHolder createBaseViewHolder(ViewGroup parent, int layoutResId) {
        View view = mLayoutInflater.inflate(layoutResId, parent, false);
        return new BaseViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        switch (holder.getItemViewType()) {
            case LOADING_VIEW:
                _loadMore();
                break;
            case HEADER_VIEW:
                break;
            case EMPTY_VIEW:
                break;
            case FOOTER_VIEW:
                break;
            default:
                convert((BaseViewHolder) holder, mData.get(holder.getLayoutPosition() - getHeaderViewsCount()));
                _setDragFixBackground(holder, position);
                break;
        }
    }


    /************************************* 加载更多 ****************************************/

    public void setRequestDataListener(OnRequestDataListener listener) {
        this.onRequestDataListener = listener;
        if (!mIsLoadMoreEnable) {
            this.enableLoadMore(true);
        }
    }

    public void enableLoadMore(boolean isEnable) {
        this.mIsLoadMoreEnable = isEnable;
        _initLoadingView();
    }

    public void setLoadStyle(Style style) {
        Sprite sprite = SpriteFactory.create(style);
        _initLoadingView();
        mLoadingIcon.setIndeterminateDrawable(sprite);
    }

    public void setLoadDesc(String desc) {
        _initLoadingView();
        mLoadingStr = desc;
        mLoadingDesc.setText(mLoadingStr);
    }

    public void setLoadColor(int color) {
        mLoadingDesc.setTextColor(color);
        mLoadingIcon.getIndeterminateDrawable().setColor(color);
    }

    /**
     * 加载完成
     */
    public void loadComplete() {
        mIsLoadingNow = false;
    }

    /**
     * 没有更多数据，后面不再加载数据
     */
    public void noMoreData() {
        mIsLoadingNow = false;
        mIsNoMoreData = true;
        mLoadingIcon.setVisibility(View.GONE);
        mLoadingDesc.setText(R.string.no_more_data);
    }

    /**
     * 加载数据异常，重新进入可再加载数据
     */
    public void loadAbnormal() {
        mIsLoadingNow = false;
        mLoadingIcon.setVisibility(View.GONE);
        mLoadingDesc.setText(R.string.load_abnormal);
    }

    private void _initLoadingView() {
        if (mLoadingView == null) {
            mLoadingView = mLayoutInflater.inflate(R.layout.layout_load_more, null);
            mLoadingView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT));
            mLoadingDesc = (TextView) mLoadingView.findViewById(R.id.tv_loading_desc);
            mLoadingIcon = (SpinKitView) mLoadingView.findViewById(R.id.iv_loading_icon);
            mLoadingStr = mContext.getResources().getString(R.string.loading_desc);
            mLoadingDesc.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!mIsLoadingNow && !mIsNoMoreData) {
                        _loadMore();
                    }
                }
            });
        }
    }

    private void _loadMore() {
        if (!mIsLoadingNow && onRequestDataListener != null && !mIsNoMoreData) {
            if (mLoadingIcon.getVisibility() == View.GONE) {
                mLoadingIcon.setVisibility(View.VISIBLE);
                mLoadingDesc.setText(mLoadingStr);
            }
            mIsLoadingNow = true;
            onRequestDataListener.onLoadMore();
        }
    }

    /************************************* 头尾视图 ****************************************/

    public View getHeaderView() {
        return mHeaderView;
    }

    public void addHeaderView(View headerView) {
        headerView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));
        mHeaderView = headerView;
        notifyDataSetChanged();
    }

    public View getFooterView() {
        return mFooterView;
    }

    public void addFooterView(View footerView) {
        footerView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));
        mFooterView = footerView;
        notifyDataSetChanged();
    }

    public int getHeaderViewsCount() {
        return mHeaderView == null ? 0 : 1;
    }

    public int getFooterViewsCount() {
        return mFooterView == null ? 0 : 1;
    }

    /************************************空数据****************************************/

    public View getEmptyView() {
        return mEmptyView;
    }

    public void setEmptyView(View emptyView) {
        mEmptyView = emptyView;
//        mEmptyView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
//                ViewGroup.LayoutParams.MATCH_PARENT));
    }

    public int getEmptyViewCount() {
        return mEmptyView == null ? 0 : 1;
    }

    /************************************数据操作****************************************/

    /**
     * Get the data item associated with the specified position in the data set.
     *
     * @param position Position of the item whose data we want within the adapter's
     *                 data set.
     * @return The data at the specified position.
     */
    public T getItem(int position) {
        return mData.get(position);
    }

    /**
     * Get the data of list
     *
     * @return
     */
    public List<T> getData() {
        return mData;
    }

    /**
     * 更新数据，替换原有数据
     *
     * @param items
     */
    public void updateItems(List<T> items) {
        mData = new ArrayList<>(items);
        notifyDataSetChanged();
        mIsNoMoreData = false;
    }

    private void _addItem(int position, T item) {
        if (mData == null || mData.size() == 0) {
            mData = new ArrayList<>();
            mData.add(item);
        } else {
            mData.add(position, item);
        }
    }

    private void _addItemList(int position, List<T> items) {
        if (mData == null || mData.size() == 0) {
            mData = new ArrayList<>();
        }
        mData.addAll(position, items);
    }

    /**
     * 首部插入一条数据
     *
     * @param item 数据
     */
    public void addItem(T item) {
        _addItem(0, item);
        notifyItemInserted(0);
    }

    /**
     * 插入一条数据
     *
     * @param item     数据
     * @param position 插入位置
     */
    public void addItem(T item, int position) {
        position = Math.min(position, mData.size());
        _addItem(position, item);
        notifyItemInserted(_calcPosition(position));
    }

    /**
     * 尾部插入一条数据
     * @param item 数据
     */
    public void addLastItem(T item) {
        _addItem(mData.size(), item);
        notifyItemInserted(_calcPosition(mData.size()));
    }

    /**
     * 在列表尾添加一串数据
     *
     * @param items
     */
    public void addItems(List<T> items) {
        _addItemList(mData.size(), items);
        int position = _calcPosition(mData.size());
        for (T item : items) {
            notifyItemInserted(position++);
        }
    }

    /**
     * 在列表尾添加一串数据
     *
     * @param items
     */
    public void addItems(List<T> items, int position) {
        position = Math.min(position, mData.size());
        _addItemList(position, items);
        int pos = _calcPosition(position);
        for (T item : items) {
            notifyItemInserted(pos++);
        }
    }

    /**
     * 移除一条数据
     *
     * @param position 位置
     */
    public void removeItem(int position) {
        if (position > mData.size() - 1) {
            return;
        }
        int pos = _calcPosition(position);
        if (mRemoveDataListener != null) {
            // 放在 mData.remove(pos) 前，不然外面获取不到数据
            mRemoveDataListener.onRemove(pos);
        }
        mData.remove(position);
        notifyItemRemoved(pos);
    }

    /**
     * 移除一条数据
     *
     * @param item 数据
     */
    public void removeItem(T item) {
        int pos = 0;
        for (T info : mData) {
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
        mData.clear();
        notifyDataSetChanged();
    }

    /**
     * 计算位置，算上头部
     * @param position
     * @return
     */
    private int _calcPosition(int position) {
        if (mHeaderView != null) {
            position++;
        }
        return position;
    }

    /************************************监听****************************************/

    /**
     * Register a callback to be invoked when an item in this AdapterView has
     * been clicked.
     *
     * @param listener The callback that will be invoked.
     */
    public void setOnItemClickListener(OnRecyclerViewItemClickListener listener) {
        this.mItemClickListener = listener;
    }

    /**
     * Register a callback to be invoked when an item in this AdapterView has
     * been clicked and held
     *
     * @param listener The callback that will run
     */
    public void setOnItemLongClickListener(OnRecyclerViewItemLongClickListener listener) {
        this.mItemLongClickListener = listener;
    }

    /**
     * 设置移除监听
     * @param removeDataListener
     */
    public void setRemoveDataListener(OnRemoveDataListener removeDataListener) {
        mRemoveDataListener = removeDataListener;
    }

    /**
     * init the baseViewHolder to register onRecyclerViewItemClickListener and onRecyclerViewItemLongClickListener
     *
     * @param baseViewHolder
     */
    private void _initItemClickListener(final BaseViewHolder baseViewHolder) {
        if (mItemClickListener != null) {
            baseViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mItemClickListener.onItemClick(v, baseViewHolder.getLayoutPosition());
                }
            });
        }
        if (mItemLongClickListener != null) {
            baseViewHolder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    return mItemLongClickListener.onItemLongClick(v, baseViewHolder.getLayoutPosition());
                }
            });
        }
        if (mDragCallback != null && mDragFixCount > 0) {
            baseViewHolder.itemView.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    if (baseViewHolder.getLayoutPosition() < mDragFixCount) {
                        mDragCallback.setEnable(false);
                    } else {
                        mDragCallback.setEnable(true);
                    }
                    return false;
                }
            });
        }
    }

    /************************************拖拽滑动****************************************/

    @Override
    public boolean onItemMove(int fromPosition, int toPosition) {
        if (fromPosition >= mDragFixCount && toPosition >= mDragFixCount) {
            Collections.swap(mData, fromPosition, toPosition);
            notifyItemMoved(fromPosition, toPosition);
            if (mItemMoveListener != null) {
                mItemMoveListener.onItemMove(fromPosition, toPosition);
            }
            return true;
        }
        return false;
    }

    @Override
    public void onItemDismiss(int position) {
        removeItem(position);
    }

    public void setDragStartListener(OnStartDragListener dragStartListener) {
        mDragStartListener = dragStartListener;
    }

    public void setDragCallback(SimpleItemTouchHelperCallback dragCallback) {
        mDragCallback = dragCallback;
    }

    protected void startDrag(RecyclerView.ViewHolder viewHolder) {
        if (mDragStartListener != null) {
            mDragStartListener.onStartDrag(viewHolder);
        }
    }

    public void setItemMoveListener(OnItemMoveListener itemMoveListener) {
        mItemMoveListener = itemMoveListener;
    }

    /**
     * 该方法在添加列表数据前调用
     * @param dragFixCount  固定的数量
     */
    public void setDragFixCount(int dragFixCount) {
        mDragFixCount = dragFixCount;
        if (mDragFixDrawable == null) {
            mDragFixDrawable = ContextCompat.getDrawable(mContext, R.drawable.shape_drag_default);
        }
    }

    public void setDragFixDrawable(int fixColor) {
        mDragFixDrawable = new ColorDrawable(fixColor);
    }

    public void setDragFixDrawable(Drawable drawable) {
        mDragFixDrawable = drawable;
    }

    public void setDragColor(int dragColor) {
        BaseViewHolder.setDragColor(dragColor);
    }

    public void setDragDrawable(Drawable drawable) {
        BaseViewHolder.setDragDrawable(drawable);
    }

    /**
     * 设置固定项的背景色
     * @param holder
     * @param position
     */
    private void _setDragFixBackground(RecyclerView.ViewHolder holder, int position) {
        if (position < mDragFixCount) {
            holder.itemView.setBackgroundDrawable(mDragFixDrawable);
        }
    }

    /**
     * 设置拖拽控制标志位
     * eg: ItemTouchHelper.UP | ItemTouchHelper.DOWN | ItemTouchHelper.LEFT |
     *  ItemTouchHelper.RIGHT | ItemTouchHelper.START | ItemTouchHelper.END
     * @param dragFlags
     */
    public void setDragFlags(int dragFlags) {
        if (mDragCallback != null) {
            mDragCallback.setDragFlags(dragFlags);
        }
    }

    /**
     * 设置滑动控制标志位
     * eg: ItemTouchHelper.UP | ItemTouchHelper.DOWN | ItemTouchHelper.LEFT |
     *  ItemTouchHelper.RIGHT | ItemTouchHelper.START | ItemTouchHelper.END
     * @param swipeFlags
     */
    public void setSwipeFlags(int swipeFlags) {
        if (mDragCallback != null) {
            mDragCallback.setSwipeFlags(swipeFlags);
        }
    }

    /************************************* Tag标志 ****************************************/

    /**
     * 给BaseViewHolder设置Tag
     * @param viewHolder    目标BaseViewHolder
     * @param tag   tag标志
     */
    public void setTag(BaseViewHolder viewHolder, Object tag) {
        viewHolder.itemView.setTag(tag);
    }

    /**
     * 根据tag标志获取BaseViewHolder
     * @param tag   tag标志
     * @return  目标BaseViewHolder
     */
    public BaseViewHolder getTag(Object tag) {
        View view = mParentView.findViewWithTag(tag);
        if (view == null) {
            return null;
        }
        return  (BaseViewHolder) view.getTag(R.id.view_holder_tag);
    }
}
