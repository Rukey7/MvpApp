package com.dl7.tag;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.SparseBooleanArray;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.dl7.tag.utils.ColorsFactory;
import com.dl7.tag.utils.MeasureUtils;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by long on 2016/7/20.
 * TagLayout
 */
public class TagLayout extends ViewGroup {

    private Paint mPaint;
    // 背景色
    private int mBgColor;
    // 边框颜色
    private int mBorderColor;
    // 边框大小
    private float mBorderWidth;
    // 边框角半径
    private float mRadius;
    // Tag之间的垂直间隙
    private int mVerticalInterval;
    // Tag之间的水平间隙
    private int mHorizontalInterval;
    // 边框矩形
    private RectF mRect;
    // 可用的最大宽度
    private int mAvailableWidth;

    private int mTagBgColor;
    private int mTagBorderColor;
    private int mTagTextColor;
    private int mTagBgColorCheck;
    private int mTagBorderColorCheck;
    private int mTagTextColorCheck;
    private float mTagBorderWidth;
    private float mTagTextSize;
    private float mTagRadius;
    private int mTagHorizontalPadding;
    private int mTagVerticalPadding;
    private TagView.OnTagClickListener mTagClickListener;
    private TagView.OnTagLongClickListener mTagLongClickListener;
    private TagView.OnTagCheckListener mTagCheckListener;
    private TagView.OnTagCheckListener mInsideTagCheckListener;
    // 这个用来保存设置监听器之前的TagView
    private List<TagView> mTagViews = new ArrayList<>();
    private SparseBooleanArray mCheckSparseArray = new SparseBooleanArray();
    // 显示模式
    private int mTagShape;
    private int mFitTagNum;
    private int mIconPadding;
    private boolean mIsPressFeedback;
    // 显示类型
    private int mTagMode;
    // 固定状态的TagView
    private TagView mFitTagView;
    // 使能随机颜色
    private boolean mEnableRandomColor;


    public TagLayout(Context context) {
        this(context, null);
    }

    public TagLayout(Context context, AttributeSet attrs) {
        this(context, attrs, -1);
    }

    public TagLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        _init(context, attrs, defStyleAttr);
    }

    private void _init(Context context, AttributeSet attrs, int defStyleAttr) {
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mRect = new RectF();

        final TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.TagLayout);
        try {
            mTagMode = a.getInteger(R.styleable.TagLayout_tag_layout_mode, TagView.MODE_NORMAL);
            mTagShape = a.getInteger(R.styleable.TagLayout_tag_layout_shape, TagView.SHAPE_ROUND_RECT);
            mIsPressFeedback = a.getBoolean(R.styleable.TagLayout_tag_layout_press_feedback, false);
            mEnableRandomColor = a.getBoolean(R.styleable.TagLayout_tag_layout_random_color, false);
            mFitTagNum = a.getInteger(R.styleable.TagLayout_tag_layout_fit_num, TagView.INVALID_VALUE);
            mBgColor = a.getColor(R.styleable.TagLayout_tag_layout_bg_color, Color.WHITE);
            mBorderColor = a.getColor(R.styleable.TagLayout_tag_layout_border_color, Color.WHITE);
            mBorderWidth = a.getDimension(R.styleable.TagLayout_tag_layout_border_width, MeasureUtils.dp2px(context, 0.5f));
            mRadius = a.getDimension(R.styleable.TagLayout_tag_layout_border_radius, MeasureUtils.dp2px(context, 5f));
            mHorizontalInterval = (int) a.getDimension(R.styleable.TagLayout_tag_layout_horizontal_interval, MeasureUtils.dp2px(context, 5f));
            mVerticalInterval = (int) a.getDimension(R.styleable.TagLayout_tag_layout_vertical_interval, MeasureUtils.dp2px(context, 5f));

            mTagBgColor = a.getColor(R.styleable.TagLayout_tag_view_bg_color, Color.WHITE);
            mTagBorderColor = a.getColor(R.styleable.TagLayout_tag_view_border_color, Color.parseColor("#ff333333"));
            mTagTextColor = a.getColor(R.styleable.TagLayout_tag_view_text_color, Color.parseColor("#ff666666"));
            if (mIsPressFeedback || mTagMode == TagView.MODE_SINGLE_CHOICE || mTagMode == TagView.MODE_MULTI_CHOICE) {
                mTagBgColorCheck = a.getColor(R.styleable.TagLayout_tag_view_bg_color_check, mTagTextColor);
                mTagBorderColorCheck = a.getColor(R.styleable.TagLayout_tag_view_border_color_check, mTagTextColor);
                mTagTextColorCheck = a.getColor(R.styleable.TagLayout_tag_view_text_color_check, Color.WHITE);
            } else {
                mTagBgColorCheck = a.getColor(R.styleable.TagLayout_tag_view_bg_color_check, mTagBgColor);
                mTagBorderColorCheck = a.getColor(R.styleable.TagLayout_tag_view_border_color_check, mTagBorderColor);
                mTagTextColorCheck = a.getColor(R.styleable.TagLayout_tag_view_text_color_check, mTagTextColor);
            }
            mTagBorderWidth = a.getDimension(R.styleable.TagLayout_tag_view_border_width, MeasureUtils.dp2px(context, 0.5f));
            mTagTextSize = a.getFloat(R.styleable.TagLayout_tag_view_text_size, 13.0f);
            mTagRadius = a.getDimension(R.styleable.TagLayout_tag_view_border_radius, MeasureUtils.dp2px(context, 5f));
            mTagHorizontalPadding = (int) a.getDimension(R.styleable.TagLayout_tag_view_horizontal_padding, MeasureUtils.dp2px(context, 5f));
            mTagVerticalPadding = (int) a.getDimension(R.styleable.TagLayout_tag_view_vertical_padding, MeasureUtils.dp2px(context, 5f));
            mIconPadding = (int) a.getDimension(R.styleable.TagLayout_tag_view_icon_padding, MeasureUtils.dp2px(context, 3f));
        } finally {
            a.recycle();
        }
        // 如果想要自己绘制内容，则必须设置这个标志位为false，否则onDraw()方法不会调用
        setWillNotDraw(false);
        setPadding(mHorizontalInterval, mVerticalInterval, mHorizontalInterval, mVerticalInterval);
        // 设置对应模式
        if (mTagMode == TagView.MODE_CHANGE) {
            mFitTagView = _initTagView("换一换", TagView.MODE_CHANGE);
            addView(mFitTagView);
        } else if (mTagMode == TagView.MODE_EDIT) {
            mFitTagView = _initTagView("", TagView.MODE_EDIT);
            addView(mFitTagView);
        } else if (mTagMode == TagView.MODE_SINGLE_CHOICE || mTagMode == TagView.MODE_MULTI_CHOICE) {
            mIsPressFeedback = true;
            mInsideTagCheckListener = new TagView.OnTagCheckListener() {
                @Override
                public void onTagCheck(int position, String text, boolean isChecked) {
                    if (mTagCheckListener != null) {
                        mTagCheckListener.onTagCheck(position, text, isChecked);
                    }
                    for (int i = 0; i < mTagViews.size(); i++) {
                        if (mTagViews.get(i).getTagText().toString().equals(text)) {
                            mCheckSparseArray.put(i, isChecked);
                        } else if (mTagMode == TagView.MODE_SINGLE_CHOICE && mCheckSparseArray.get(i)) {
                            mTagViews.get(i).cleanTagCheckStatus();
                            mCheckSparseArray.put(i, false);
                        }
                    }
                }
            };
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int widthSpecSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightSpecSize = MeasureSpec.getSize(heightMeasureSpec);
        int heightSpecMode = MeasureSpec.getMode(heightMeasureSpec);
        // 计算可用宽度，为测量宽度减去左右padding值，这个放在measureChildren前面，在子视图中会用到这个参数
        mAvailableWidth = widthSpecSize - getPaddingLeft() - getPaddingRight();
        // 测量子视图
        measureChildren(widthMeasureSpec, heightMeasureSpec);
        int childCount = getChildCount();
        int tmpWidth = 0;
        int measureHeight = 0;
        int maxLineHeight = 0;
        for (int i = 0; i < childCount; i++) {
            View child = getChildAt(i);
            // 记录该行的最大高度
            if (maxLineHeight == 0) {
                maxLineHeight = child.getMeasuredHeight();
            } else {
                maxLineHeight = Math.max(maxLineHeight, child.getMeasuredHeight());
            }
            // 统计该行TagView的总宽度
            tmpWidth += child.getMeasuredWidth() + mHorizontalInterval;
            // 如果超过可用宽度则换行
            if (tmpWidth - mHorizontalInterval > mAvailableWidth) {
                // 统计TagGroup的测量高度，要加上垂直间隙
                measureHeight += maxLineHeight + mVerticalInterval;
                // 重新赋值
                tmpWidth = child.getMeasuredWidth() + mHorizontalInterval;
                maxLineHeight = child.getMeasuredHeight();
            }
        }
        // 统计TagGroup的测量高度，加上最后一行
        measureHeight += maxLineHeight;

        // 设置测量宽高，记得算上padding
        if (childCount == 0) {
            setMeasuredDimension(0, 0);
        } else if (heightSpecMode == MeasureSpec.UNSPECIFIED || heightSpecMode == MeasureSpec.AT_MOST) {
            setMeasuredDimension(widthSpecSize, measureHeight + getPaddingTop() + getPaddingBottom());
        } else {
            setMeasuredDimension(widthSpecSize, heightSpecSize);
        }
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int childCount = getChildCount();
        if (childCount <= 0) {
            return;
        }

        mAvailableWidth = getMeasuredWidth() - getPaddingLeft() - getPaddingRight();
        // 当前布局使用的top坐标
        int curTop = getPaddingTop();
        // 当前布局使用的left坐标
        int curLeft = getPaddingLeft();
        int maxHeight = 0;
        for (int i = 0; i < childCount; i++) {
            View child = getChildAt(i);

            if (maxHeight == 0) {
                maxHeight = child.getMeasuredHeight();
            } else {
                maxHeight = Math.max(maxHeight, child.getMeasuredHeight());
            }

            int width = child.getMeasuredWidth();
            int height = child.getMeasuredHeight();
            // 超过一行做换行操作
            if (width + curLeft > mAvailableWidth + getPaddingLeft()) {
                curLeft = getPaddingLeft();
                // 计算top坐标，要加上垂直间隙
                curTop += maxHeight + mVerticalInterval;
                maxHeight = child.getMeasuredHeight();
            }
            // 设置子视图布局
            child.layout(curLeft, curTop, curLeft + width, curTop + height);
            // 计算left坐标，要加上水平间隙
            curLeft += width + mHorizontalInterval;
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mRect.set(mBorderWidth, mBorderWidth, w - mBorderWidth, h - mBorderWidth);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        // 绘制背景
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setColor(mBgColor);
        canvas.drawRoundRect(mRect, mRadius, mRadius, mPaint);
        // 绘制边框
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(mBorderWidth);
        mPaint.setColor(mBorderColor);
        canvas.drawRoundRect(mRect, mRadius, mRadius, mPaint);
    }

    /**
     * ==================================== 设置TagGroup ====================================
     */

    public int getBgColor() {
        return mBgColor;
    }

    public void setBgColor(int bgColor) {
        mBgColor = bgColor;
    }

    public int getBorderColor() {
        return mBorderColor;
    }

    public void setBorderColor(int borderColor) {
        mBorderColor = borderColor;
    }

    public float getBorderWidth() {
        return mBorderWidth;
    }

    public void setBorderWidth(float borderWidth) {
        mBorderWidth = MeasureUtils.dp2px(getContext(), borderWidth);
    }

    public float getRadius() {
        return mRadius;
    }

    public void setRadius(float radius) {
        mRadius = radius;
    }

    public int getVerticalInterval() {
        return mVerticalInterval;
    }

    public void setVerticalInterval(int verticalInterval) {
        mVerticalInterval = verticalInterval;
    }

    public int getHorizontalInterval() {
        return mHorizontalInterval;
    }

    public void setHorizontalInterval(int horizontalInterval) {
        mHorizontalInterval = horizontalInterval;
    }

    protected int getAvailableWidth() {
        return mAvailableWidth;
    }

    public int getFitTagNum() {
        return mFitTagNum;
    }

    public void setFitTagNum(int fitTagNum) {
        mFitTagNum = fitTagNum;
    }

    /**
     * ==================================== 设置TagView ====================================
     */

    private TagView _initTagView(String text, @TagView.TagMode int tagMode) {
        TagView tagView = new TagView(getContext(), text);
        if (mEnableRandomColor) {
            _setTagRandomColors(tagView);
        } else {
            tagView.setBgColor(mTagBgColor);
            tagView.setBorderColor(mTagBorderColor);
            tagView.setOriTextColor(mTagTextColor);
            tagView.setBgColorChecked(mTagBgColorCheck);
            tagView.setBorderColorChecked(mTagBorderColorCheck);
            tagView.setTextColorChecked(mTagTextColorCheck);
        }
        tagView.setBorderWidth(mTagBorderWidth);
        tagView.setRadius(mTagRadius);
        tagView.setTextSize(mTagTextSize);
        tagView.setHorizontalPadding(mTagHorizontalPadding);
        tagView.setVerticalPadding(mTagVerticalPadding);
        tagView.setPressFeedback(mIsPressFeedback);
        tagView.setTagClickListener(mTagClickListener);
        tagView.setTagLongClickListener(mTagLongClickListener);
        tagView.setTagCheckListener(mInsideTagCheckListener);
        tagView.setTagShape(mTagShape);
        tagView.setTagMode(tagMode);
        tagView.setCompoundDrawablePadding(mIconPadding);
        mTagViews.add(tagView);
        tagView.setTag(mTagViews.size() - 1);
        return tagView;
    }

    private void _setTagRandomColors(TagView tagView) {
        int[] color = ColorsFactory.provideColor();
        if (mIsPressFeedback) {
            tagView.setOriTextColor(color[1]);
            tagView.setBgColor(Color.WHITE);
            tagView.setBgColorChecked(color[0]);
            tagView.setBorderColorChecked(color[0]);
            tagView.setTextColorChecked(Color.WHITE);
        } else {
            tagView.setBgColor(color[1]);
            tagView.setOriTextColor(mTagTextColor);
            tagView.setBgColorChecked(color[1]);
            tagView.setBorderColorChecked(color[0]);
            tagView.setTextColorChecked(mTagTextColor);
        }
        tagView.setBorderColor(color[0]);
    }

    public int getTagBgColor() {
        return mTagBgColor;
    }

    public void setTagBgColor(int tagBgColor) {
        mTagBgColor = tagBgColor;
    }

    public int getTagBorderColor() {
        return mTagBorderColor;
    }

    public void setTagBorderColor(int tagBorderColor) {
        mTagBorderColor = tagBorderColor;
    }

    public int getTagTextColor() {
        return mTagTextColor;
    }

    public void setTagTextColor(int tagTextColor) {
        mTagTextColor = tagTextColor;
    }

    public float getTagBorderWidth() {
        return mTagBorderWidth;
    }

    public void setTagBorderWidth(float tagBorderWidth) {
        mTagBorderWidth = MeasureUtils.dp2px(getContext(), tagBorderWidth);
        if (mFitTagView != null) {
            mFitTagView.setBorderWidth(mTagBorderWidth);
        }
    }

    public float getTagTextSize() {
        return mTagTextSize;
    }

    public void setTagTextSize(float tagTextSize) {
        mTagTextSize = tagTextSize;
        if (mFitTagView != null) {
            mFitTagView.setTextSize(tagTextSize);
        }
    }

    public float getTagRadius() {
        return mTagRadius;
    }

    public void setTagRadius(float tagRadius) {
        mTagRadius = tagRadius;
        if (mFitTagView != null) {
            mFitTagView.setRadius(mTagRadius);
        }
    }

    public int getTagHorizontalPadding() {
        return mTagHorizontalPadding;
    }

    public void setTagHorizontalPadding(int tagHorizontalPadding) {
        mTagHorizontalPadding = tagHorizontalPadding;
        if (mFitTagView != null) {
            mFitTagView.setHorizontalPadding(mTagHorizontalPadding);
        }
    }

    public int getTagVerticalPadding() {
        return mTagVerticalPadding;
    }

    public void setTagVerticalPadding(int tagVerticalPadding) {
        mTagVerticalPadding = tagVerticalPadding;
        if (mFitTagView != null) {
            mFitTagView.setVerticalPadding(mTagVerticalPadding);
        }
    }

    public boolean isPressFeedback() {
        return mIsPressFeedback;
    }

    public void setPressFeedback(boolean pressFeedback) {
        mIsPressFeedback = pressFeedback;
        if (mFitTagView != null) {
            mFitTagView.setPressFeedback(mIsPressFeedback);
        }
    }

    public TagView.OnTagClickListener getTagClickListener() {
        return mTagClickListener;
    }

    public void setTagClickListener(TagView.OnTagClickListener tagClickListener) {
        mTagClickListener = tagClickListener;
        // 避免先调用设置TagView，后设置监听器导致前面设置的TagView不能响应点击
        for (TagView tagView : mTagViews) {
            tagView.setTagClickListener(mTagClickListener);
        }
    }

    public TagView.OnTagLongClickListener getTagLongClickListener() {
        return mTagLongClickListener;
    }

    public void setTagLongClickListener(TagView.OnTagLongClickListener tagLongClickListener) {
        mTagLongClickListener = tagLongClickListener;
        for (TagView tagView : mTagViews) {
            tagView.setTagLongClickListener(mTagLongClickListener);
        }
    }

    public TagView.OnTagCheckListener getTagCheckListener() {
        return mTagCheckListener;
    }

    public void setTagCheckListener(TagView.OnTagCheckListener tagCheckListener) {
        mTagCheckListener = tagCheckListener;
    }

    public void setTagShape(@TagView.TagShape int tagShape) {
        mTagShape = tagShape;
    }

    public void setEnableRandomColor(boolean enableRandomColor) {
        mEnableRandomColor = enableRandomColor;
    }

    public void setIconPadding(int padding) {
        mIconPadding = padding;
        if (mFitTagView != null) {
            mFitTagView.setCompoundDrawablePadding(mIconPadding);
        }
    }

    /**
     * ==================================== 添加/删除TagView ====================================
     */

    /**
     * add Tag
     *
     * @param text tag content
     */
    public void addTag(String text) {
        if (mTagMode == TagView.MODE_CHANGE || (mTagMode == TagView.MODE_EDIT && mFitTagView != null)) {
            addView(_initTagView(text, TagView.MODE_NORMAL), getChildCount() - 1);
        } else {
            addView(_initTagView(text, mTagMode));
        }
    }

    /**
     * add Tag
     *
     * @param text tag content
     */
    public void addTagWithIcon(String text, int iconResId) {
        TagView tagView;
        if (mTagMode == TagView.MODE_CHANGE || (mTagMode == TagView.MODE_EDIT && mFitTagView != null)) {
            tagView = _initTagView(text, TagView.MODE_NORMAL);
        } else {
            tagView = _initTagView(text, mTagMode);
        }
        tagView.setIconRes(iconResId);
        tagView.setCompoundDrawablePadding(mIconPadding);
        if (mTagMode == TagView.MODE_CHANGE || (mTagMode == TagView.MODE_EDIT && mFitTagView != null)) {
            addView(tagView, getChildCount() - 1);
        } else {
            addView(tagView);
        }
    }


    /**
     * 设置对应位置Tag
     * @param startPos
     */
    private void _refreshPositionTag(int startPos) {
        for (int i = startPos; i < mTagViews.size(); i++) {
            mTagViews.get(i).setTag(i);
        }
    }

    /**
     * delete tag
     * @param position
     */
    public void deleteTag(int position) {
        if (position < 0 || position >= getChildCount()) {
            Toast.makeText(getContext(), "Invalid position", Toast.LENGTH_SHORT).show();
            return;
        }
        if (mTagMode == TagView.MODE_CHANGE || (mTagMode == TagView.MODE_EDIT && mFitTagView != null)) {
            if (position == getChildCount() - 1) {
                // 最后一项为固定项不删除
                return;
            }
            // 第0项为固定项
            mTagViews.remove(position + 1);
        } else if (mTagMode == TagView.MODE_EDIT) {
            // 第0项为固定项
            mTagViews.remove(position + 1);
        } else {
            mTagViews.remove(position);
        }
        removeViewAt(position);
        _refreshPositionTag(position);
    }

    /**
     * add Tags
     *
     * @param textList tag list
     */
    public void addTags(String... textList) {
        for (String text : textList) {
            addTag(text);
        }
    }

    /**
     * add Tags
     *
     * @param textList tag list
     */
    public void addTags(List<String> textList) {
        for (String text : textList) {
            addTag(text);
        }
    }

    /**
     * clean Tags
     */
    public void cleanTags() {
        if (mTagMode == TagView.MODE_CHANGE || (mTagMode == TagView.MODE_EDIT && mFitTagView != null)) {
            removeViews(0, getChildCount() - 1);
            mTagViews.clear();
            mCheckSparseArray.clear();
            mTagViews.add(mFitTagView);
        } else {
            removeAllViews();
            mTagViews.clear();
        }
        postInvalidate();
    }

    /**
     * set Tags
     *
     * @param textList tag list
     */
    public void setTags(String... textList) {
        cleanTags();
        addTags(textList);
    }

    /**
     * set Tags
     *
     * @param textList tag list
     */
    public void setTags(List<String> textList) {
        cleanTags();
        addTags(textList);
    }

    /**
     * update Tags
     *
     * @param textList tag list
     */
    public void updateTags(String... textList) {
        int startPos = 0;
        int minSize;
        if (mTagMode == TagView.MODE_CHANGE || (mTagMode == TagView.MODE_EDIT && mFitTagView != null)) {
            startPos = 1;
            minSize = Math.min(textList.length, mTagViews.size() - 1);
        } else {
            minSize = Math.min(textList.length, mTagViews.size());
        }
        for (int i = 0; i < minSize; i++) {
            mTagViews.get(i + startPos).setTagText(textList[i]);
        }
        if (mEnableRandomColor) {
            startPos = 0;
            if (mTagMode == TagView.MODE_EDIT) {
                startPos = 1;
            }
            for (int i = startPos; i < mTagViews.size(); i++) {
                _setTagRandomColors(mTagViews.get(i));
            }
            postInvalidate();
        }
    }

    /**
     * update Tags
     *
     * @param textList tag list
     */
    public void updateTags(List<String> textList) {
        updateTags((String[]) textList.toArray());
    }

    /**
     * get checked tags
     * @return
     */
    public List<String> getCheckedTags() {
        List<String> checkTags = new ArrayList<>();
        for (int i = 0; i < mCheckSparseArray.size(); i++) {
            if (mCheckSparseArray.valueAt(i)) {
                checkTags.add(mTagViews.get(mCheckSparseArray.keyAt(i)).getTagText().toString());
            }
        }
        return checkTags;
    }

    /**
     * set tag to be checked
     * @param text
     */
    public void setCheckTag(String text) {
        if (mTagMode == TagView.MODE_SINGLE_CHOICE) {
            for (TagView tagView : mTagViews) {
                if (tagView.getTagText().toString().equals(text)) {
                    tagView.setChecked(true);
                }
            }
        }
    }

    public void setCheckTag(int... indexs) {
        if (mTagMode == TagView.MODE_SINGLE_CHOICE) {
            for (int i : indexs) {
                if (mTagViews.get(i) != null) {
                    mTagViews.get(i).setChecked(true);
                }
            }
        }
    }

    /**
     * delete checked tags
     */
    public void deleteCheckedTags() {
        for (int i = mCheckSparseArray.size() - 1; i >= 0; i--) {
            if (mCheckSparseArray.valueAt(i)) {
                deleteTag(mCheckSparseArray.keyAt(i));
                mCheckSparseArray.delete(mCheckSparseArray.keyAt(i));
            }
        }
    }

    /**
     * exit edit mode
     */
    public void exitEditMode() {
        if (mTagMode == TagView.MODE_EDIT) {
            if (mFitTagView != null) {
                mFitTagView.exitEditMode();
                removeViewAt(getChildCount() - 1);
                mFitTagView = null;
            }
        }
    }

    /**
     * entry edit mode
     */
    public void entryEditMode() {
        if (mTagMode == TagView.MODE_NORMAL || mTagMode == TagView.MODE_EDIT) {
            mTagMode = TagView.MODE_EDIT;
            mFitTagView = _initTagView("", TagView.MODE_EDIT);
            addView(mFitTagView);
        }
    }
}
