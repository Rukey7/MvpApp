package com.dl7.helperlibrary.helper;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;

import com.dl7.helperlibrary.adapter.BaseQuickAdapter;
import com.dl7.helperlibrary.divider.DividerGridItemDecoration;
import com.dl7.helperlibrary.divider.DividerItemDecoration;
import com.dl7.helperlibrary.listener.OnRequestDataListener;


/**
 * Created by long on 2016/3/30.
 * 视图帮助类
 */
public class RecyclerViewHelper {

    private RecyclerViewHelper() {
        throw new RuntimeException("RecyclerViewHelper cannot be initialized!");
    }


    /**
     * 配置垂直列表RecyclerView
     * @param view
     */
    public static void initRecyclerViewV(Context context, RecyclerView view, boolean isDivided,
                                         RecyclerView.Adapter adapter) {
        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        view.setHasFixedSize(true);
        view.setLayoutManager(layoutManager);
        view.setItemAnimator(new DefaultItemAnimator());
        if (isDivided) {
            view.addItemDecoration(new DividerItemDecoration(context, DividerItemDecoration.VERTICAL_LIST));
        }
        view.setAdapter(adapter);
    }

    public static void initRecyclerViewV(Context context, RecyclerView view, RecyclerView.Adapter adapter) {
        initRecyclerViewV(context, view, false, adapter);
    }

    public static void initRecyclerViewV(Context context, RecyclerView view, boolean isDivided, BaseQuickAdapter adapter,
                                         OnRequestDataListener listener) {
        initRecyclerViewV(context, view, isDivided, adapter);
        adapter.enableLoadMore(true);
        adapter.setRequestDataListener(listener);
    }

    public static void initRecyclerViewV(Context context, RecyclerView view, BaseQuickAdapter adapter,
                                         OnRequestDataListener listener) {
        initRecyclerViewV(context, view, false, adapter, listener);
    }

    /**
     * 配置水平列表RecyclerView
     * @param view
     */
    public static void initRecyclerViewH(Context context, RecyclerView view, boolean isDivided,
                                         RecyclerView.Adapter adapter) {
        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        view.setHasFixedSize(true);
        view.setLayoutManager(layoutManager);
        view.setItemAnimator(new DefaultItemAnimator());
        if (isDivided) {
            view.addItemDecoration(new DividerItemDecoration(context, DividerItemDecoration.HORIZONTAL_LIST));
        }
    }

    public static void initRecyclerViewH(Context context, RecyclerView view, RecyclerView.Adapter adapter) {
        initRecyclerViewH(context, view, false, adapter);
    }

    /**
     * 配置网格列表RecyclerView
     * @param view
     */
    public static void initRecyclerViewG(Context context, RecyclerView view, boolean isDivided,
                                         RecyclerView.Adapter adapter) {
        GridLayoutManager layoutManager = new GridLayoutManager(context, 3);
        view.setHasFixedSize(true);
        view.setLayoutManager(layoutManager);
        view.setItemAnimator(new DefaultItemAnimator());
        if (isDivided) {
            view.addItemDecoration(new DividerGridItemDecoration(context));
        }
    }

    public static void initRecyclerViewG(Context context, RecyclerView view, RecyclerView.Adapter adapter) {
        initRecyclerViewG(context, view, false, adapter);
    }

    /**
     * 启动拖拽和滑动
     * @param view 视图
     * @param adapter 适配器
     */
    public static void startDragAndSwipe(RecyclerView view, BaseQuickAdapter adapter) {
        ItemTouchHelper.Callback callback = new SimpleItemTouchHelperCallback(adapter);
        final ItemTouchHelper itemTouchHelper = new ItemTouchHelper(callback);
        itemTouchHelper.attachToRecyclerView(view);
        adapter.setDragStartListener(new OnStartDragListener() {
            @Override
            public void onStartDrag(RecyclerView.ViewHolder viewHolder) {
                itemTouchHelper.startDrag(viewHolder);
            }
        });
        adapter.setDragColor(Color.LTGRAY);
    }
}
