package com.dl7.mvp.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.support.v4.view.MotionEventCompat;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;

import com.dl7.mvp.R;
import com.dl7.mvp.utils.MeasureUtils;


/**
 * Created by long on 2017/3/2.
 * 状态变化的按钮，替换原标签库里的那个，之前写太乱了，而且在列表中使用会出错乱
 */
public class SimpleButton extends View {

    // 3种外形模式：圆角矩形、圆弧、直角矩形
    public final static int SHAPE_ROUND_RECT = 101;
    public final static int SHAPE_ARC = 102;
    public final static int SHAPE_RECT = 103;
    // 类型模式：正常、可选中、图标选中消失、图标选中切换
    public final static int MODE_NORMAL = 201;
    public final static int MODE_CHECK = 202;
    public final static int MODE_ICON_CHECK_INVISIBLE = 203;
    public final static int MODE_ICON_CHECK_CHANGE = 204;

    // 显示外形
    private int mTagShape = SHAPE_ROUND_RECT;
    // 显示类型
    private int mTagMode = MODE_NORMAL;
    // 画笔
    private Paint mPaint;
    // 背景色
    private int mBgColor = Color.WHITE;
    // 边框颜色
    private int mBorderColor = Color.parseColor("#ff333333");
    // 原始标签颜色
    private int mTextColor = Color.parseColor("#ff666666");
    // 选中状态背景色
    private int mBgColorChecked = Color.WHITE;
    // 选中状态边框颜色
    private int mBorderColorChecked = Color.parseColor("#ff333333");
    // 选中状态字体颜色
    private int mTextColorChecked = Color.parseColor("#ff666666");
    // 遮罩颜色
    private int mScrimColor = Color.argb(0x66, 0xc0, 0xc0, 0xc0);
    // 字体大小
    private float mTextSize;
    // 字体宽度和高度
    private int mFontLen;
    private int mFontH;
    private int mFontLenChecked;
    // 基线偏移距离
    private float mBaseLineDistance;
    // 边框大小
    private float mBorderWidth;
    // 边框角半径
    private float mRadius;
    // 内容
    private String mText;
    // 选中时内容
    private String mTextChecked;
    // 显示的文字
    private String mShowText;
    // 字体水平空隙
    private int mHorizontalPadding;
    // 字体垂直空隙
    private int mVerticalPadding;
    // 边框矩形
    private RectF mRect;
    // 装饰的icon
    private Drawable mDecorateIcon;
    // 变化模式下的icon
    private Drawable mDecorateIconChange;
    // 设置图标的位置，只支持左右两边
    private int mIconGravity = Gravity.LEFT;
    // icon和文字间距
    private int mIconPadding = 0;
    // icon大小
    private int mIconSize = 0;
    // 是否选中
    private boolean mIsChecked = false;
    // 是否自动切换选中状态，不使能可以灵活地选择切换，通过用于等待网络返回再做切换
    private boolean mIsAutoToggleCheck = false;
    // 是否被按住
    private boolean mIsPressed = false;


    public SimpleButton(Context context) {
        this(context, null);
    }

    public SimpleButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        _init(context, attrs);
    }

    private void _init(Context context, AttributeSet attrs) {
        mBorderWidth = MeasureUtils.dp2px(context, 0.5f);
        mRadius = MeasureUtils.dp2px(context, 5f);
        mHorizontalPadding = (int) MeasureUtils.dp2px(context, 5f);
        mVerticalPadding = (int) MeasureUtils.dp2px(context, 5f);
        mIconPadding = (int) MeasureUtils.dp2px(context, 3f);
        mTextSize = MeasureUtils.sp2px(context, 14f);

        if (attrs != null) {
            final TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.SimpleButton);
            try {
                mTagShape = a.getInteger(R.styleable.SimpleButton_sb_shape, SimpleButton.SHAPE_ROUND_RECT);
                mTagMode = a.getInteger(R.styleable.SimpleButton_sb_mode, MODE_NORMAL);
                if (mTagMode == MODE_CHECK || mTagMode == MODE_ICON_CHECK_INVISIBLE || mTagMode == MODE_ICON_CHECK_CHANGE) {
                    mIsAutoToggleCheck = true;
                    mIsChecked = a.getBoolean(R.styleable.SimpleButton_sb_checked, false);
                    mDecorateIconChange = a.getDrawable(R.styleable.SimpleButton_sb_icon_change);
                }
                mIsAutoToggleCheck = a.getBoolean(R.styleable.SimpleButton_sb_auto_check, mIsAutoToggleCheck);

                mText = a.getString(R.styleable.SimpleButton_sb_text);
                mTextChecked = a.getString(R.styleable.SimpleButton_sb_text_check);
                mTextSize = a.getDimension(R.styleable.SimpleButton_sb_text_size, mTextSize);
                mBgColor = a.getColor(R.styleable.SimpleButton_sb_bg_color, Color.WHITE);
                mBorderColor = a.getColor(R.styleable.SimpleButton_sb_border_color, Color.parseColor("#ff333333"));
                mTextColor = a.getColor(R.styleable.SimpleButton_sb_text_color, Color.parseColor("#ff666666"));
                mBgColorChecked = a.getColor(R.styleable.SimpleButton_sb_bg_color_check, mBgColor);
                mBorderColorChecked = a.getColor(R.styleable.SimpleButton_sb_border_color_check, mBorderColor);
                mTextColorChecked = a.getColor(R.styleable.SimpleButton_sb_text_color_check, mTextColor);
                mBorderWidth = a.getDimension(R.styleable.SimpleButton_sb_border_width, mBorderWidth);
                mRadius = a.getDimension(R.styleable.SimpleButton_sb_border_radius, mRadius);
                mHorizontalPadding = (int) a.getDimension(R.styleable.SimpleButton_sb_horizontal_padding, mHorizontalPadding);
                mVerticalPadding = (int) a.getDimension(R.styleable.SimpleButton_sb_vertical_padding, mVerticalPadding);
                mIconPadding = (int) a.getDimension(R.styleable.SimpleButton_sb_icon_padding, mIconPadding);
                mDecorateIcon = a.getDrawable(R.styleable.SimpleButton_sb_icon);
                mIconGravity = a.getInteger(R.styleable.SimpleButton_sb_gravity, Gravity.LEFT);
            } finally {
                a.recycle();
            }
        }

        if (mTagMode == MODE_ICON_CHECK_CHANGE && mDecorateIconChange == null) {
            throw new RuntimeException("You must set the drawable by 'tag_icon_change' property in MODE_ICON_CHECK_CHANGE mode");
        }
        // 如果没有图标则把 mIconPadding 设为0
        if (mDecorateIcon == null && mDecorateIconChange == null) {
            mIconPadding = 0;
        }
        mRect = new RectF();
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        setClickable(true);
    }

    private int _adjustText(int maxWidth) {
        if (mPaint.getTextSize() != mTextSize) {
            mPaint.setTextSize(mTextSize);
            final Paint.FontMetrics fontMetrics = mPaint.getFontMetrics();
            // 文字高度
            mFontH = (int) (fontMetrics.descent - fontMetrics.ascent);
            // 用来设置基线的偏移量,再加上 getHeight() / 2 就是基线坐标，尼玛折腾了我好久
            mBaseLineDistance = (int) Math.ceil((fontMetrics.descent - fontMetrics.ascent) / 2 - fontMetrics.descent);
        }
        // 计算文字宽度
        if (TextUtils.isEmpty(mText)) {
            mText = "";
        }
        mFontLen = (int) mPaint.measureText(mText);
        if (TextUtils.isEmpty(mTextChecked)) {
            mFontLenChecked = mFontLen;
        } else {
            mFontLenChecked = (int) mPaint.measureText(mTextChecked);
        }
        // 计算图标大小
        if ((mDecorateIcon != null || mDecorateIconChange != null) && mIconSize != mFontH) {
            mIconSize = mFontH;
        }
        // 计算出了文字外所需要占用的宽度
        int allPadding;
        if (mTagMode == MODE_ICON_CHECK_INVISIBLE && mIsChecked) {
            allPadding = mHorizontalPadding * 2;
        } else if (mDecorateIcon == null && !mIsChecked) {
            allPadding = mHorizontalPadding * 2;
        } else {
            allPadding = mIconPadding + mIconSize + mHorizontalPadding * 2;
        }
        // 设置显示的文字
        if (mIsChecked && !TextUtils.isEmpty(mTextChecked)) {
            if (mFontLenChecked + allPadding > maxWidth) {
                float pointWidth = mPaint.measureText(".");
                // 计算能显示的字体长度
                float maxTextWidth = maxWidth - allPadding - pointWidth * 3;
                mShowText = _clipShowText(mTextChecked, mPaint, maxTextWidth);
                mFontLenChecked = (int) mPaint.measureText(mShowText);
            } else {
                mShowText = mTextChecked;
            }
        } else if (mFontLen + allPadding > maxWidth) {
            float pointWidth = mPaint.measureText(".");
            // 计算能显示的字体长度
            float maxTextWidth = maxWidth - allPadding - pointWidth * 3;
            mShowText = _clipShowText(mText, mPaint, maxTextWidth);
            mFontLen = (int) mPaint.measureText(mShowText);
        } else {
            mShowText = mText;
        }

        return allPadding;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int allPadding = _adjustText(getMeasuredWidth());
        int fontLen = mIsChecked ? mFontLenChecked : mFontLen;
        // 如果为精确测量 MeasureSpec.EXACTLY，则直接使用测量的大小，否则让控件实现自适应
        // 如果你用了精确测量则 mHorizontalPadding 和 mVerticalPadding 会对最终大小判定无效
        int width = (MeasureSpec.getMode(widthMeasureSpec) == MeasureSpec.EXACTLY) ?
                MeasureSpec.getSize(widthMeasureSpec) : allPadding + fontLen;
        int height = (MeasureSpec.getMode(heightMeasureSpec) == MeasureSpec.EXACTLY) ?
                MeasureSpec.getSize(heightMeasureSpec) : mVerticalPadding * 2 + mFontH;
        setMeasuredDimension(width, height);
        // 计算图标放置位置
        if (mDecorateIcon != null || mDecorateIconChange != null) {
            int top = (height - mIconSize) / 2;
            int left;
            if (mIconGravity == Gravity.RIGHT) {
                int padding = (width - mIconSize - fontLen - mIconPadding) / 2;
                left = width - padding - mIconSize;
            } else {
                left = (width - mIconSize - fontLen - mIconPadding) / 2;
            }
            if (mTagMode == MODE_ICON_CHECK_CHANGE && mIsChecked && mDecorateIconChange != null) {
                mDecorateIconChange.setBounds(left, top, mIconSize + left, mIconSize + top);
            } else if (mDecorateIcon != null) {
                mDecorateIcon.setBounds(left, top, mIconSize + left, mIconSize + top);
            }
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        // 设置矩形边框
        mRect.set(mBorderWidth, mBorderWidth, w - mBorderWidth, h - mBorderWidth);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        // 圆角
        float radius = mRadius;
        if (mTagShape == SHAPE_ARC) {
            radius = mRect.height() / 2;
        } else if (mTagShape == SHAPE_RECT) {
            radius = 0;
        }
        // 绘制背景
        mPaint.setStyle(Paint.Style.FILL);
        if (mIsChecked) {
            mPaint.setColor(mBgColorChecked);
        } else {
            mPaint.setColor(mBgColor);
        }
        canvas.drawRoundRect(mRect, radius, radius, mPaint);
        // 绘制边框
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(mBorderWidth);
        if (mIsChecked) {
            mPaint.setColor(mBorderColorChecked);
        } else {
            mPaint.setColor(mBorderColor);
        }
        canvas.drawRoundRect(mRect, radius, radius, mPaint);
        // 绘制文字
        mPaint.setStyle(Paint.Style.FILL);
        if (mIsChecked) {
            mPaint.setColor(mTextColorChecked);
            int padding = mTagMode == MODE_ICON_CHECK_INVISIBLE ? 0 : mIconSize + mIconPadding;
            canvas.drawText(mShowText,
                    mIconGravity == Gravity.RIGHT ? (getWidth() - mFontLenChecked - padding) / 2
                            : (getWidth() - mFontLenChecked - padding) / 2 + padding,
                    getHeight() / 2 + mBaseLineDistance, mPaint);
        } else {
            mPaint.setColor(mTextColor);
            int padding = mDecorateIcon == null ? 0 : mIconSize + mIconPadding;
            canvas.drawText(mShowText,
                    mIconGravity == Gravity.RIGHT ? (getWidth() - mFontLen - padding) / 2
                            : (getWidth() - mFontLen - padding) / 2 + padding,
                    getHeight() / 2 + mBaseLineDistance, mPaint);
        }
        // 绘制Icon
        if (mTagMode == MODE_ICON_CHECK_CHANGE && mIsChecked && mDecorateIconChange != null) {
            mDecorateIconChange.setColorFilter(mPaint.getColor(), PorterDuff.Mode.SRC_IN);
            mDecorateIconChange.draw(canvas);
        } else if (mTagMode == MODE_ICON_CHECK_INVISIBLE && mIsChecked) {
            // Don't need to draw
        } else if (mDecorateIcon != null) {
            mDecorateIcon.setColorFilter(mPaint.getColor(), PorterDuff.Mode.SRC_IN);
            mDecorateIcon.draw(canvas);
        }
        // 绘制半透明遮罩
        if (mIsPressed) {
            mPaint.setColor(mScrimColor);
            canvas.drawRoundRect(mRect, radius, radius, mPaint);
        }
    }

    /**
     * ==================================== 触摸点击控制 ====================================
     */

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (MotionEventCompat.getActionMasked(event)) {
            case MotionEvent.ACTION_DOWN:
                mIsPressed = true;
                invalidate();
                break;
            case MotionEvent.ACTION_MOVE:
                if (mIsPressed && !_isViewUnder(event.getX(), event.getY())) {
                    mIsPressed = false;
                    invalidate();
                }
                break;
            case MotionEvent.ACTION_UP:
                if (_isViewUnder(event.getX(), event.getY())) {
                    _toggleTagCheckStatus();
                }
            case MotionEvent.ACTION_CANCEL:
                if (mIsPressed) {
                    mIsPressed = false;
                    invalidate();
                }
                break;
        }
        return super.onTouchEvent(event);
    }

    /**
     * 判断是否在 Tag 控件内
     *
     * @param x
     * @param y
     * @return
     */
    private boolean _isViewUnder(float x, float y) {
        return x >= 0 && x < getWidth() &&
                y >= 0 && y < getHeight();
    }

    /**
     * 切换tag选中状态
     */
    private void _toggleTagCheckStatus() {
        if (mIsAutoToggleCheck) {
            setChecked(!mIsChecked);
        }
    }

    public boolean isChecked() {
        return mIsChecked;
    }

    /**
     * 设置选中状态
     *
     * @param checked
     */
    public void setChecked(boolean checked) {
        if (mIsChecked == checked) {
            return;
        }
        mIsChecked = checked;
        // 注意，这里用 requestLayout() 而不是只用 invalidate()，因为 invalidate() 没法回调 onMeasure() 进行测量，
        // 如果控件自适应大小的话是有可能改变大小的，所以加上 requestLayout()
        requestLayout();
        invalidate();
        if (mCheckListener != null) {
            mCheckListener.onCheckedChanged(mIsChecked);
        }
    }

    /**
     * 裁剪Text
     * @param oriText
     * @param paint
     * @param maxTextWidth
     * @return
     */
    private String _clipShowText(String oriText, Paint paint, float maxTextWidth) {
        float tmpWidth = 0;
        StringBuilder strBuilder = new StringBuilder();
        for (int i = 0; i < oriText.length(); i++) {
            char c = oriText.charAt(i);
            float cWidth = paint.measureText(String.valueOf(c));
            // 计算每个字符的宽度之和，如果超过能显示的长度则退出
            if (tmpWidth + cWidth > maxTextWidth) {
                break;
            }
            strBuilder.append(c);
            tmpWidth += cWidth;
        }
        // 末尾添加3个.并设置为显示字符
        strBuilder.append("...");
        return strBuilder.toString();
    }

    /**
     * ==================================== 接口 ====================================
     */

    public int getTagShape() {
        return mTagShape;
    }

    public void setTagShape(int tagShape) {
        mTagShape = tagShape;
        _update();
    }

    public int getTagMode() {
        return mTagMode;
    }

    public void setTagMode(int tagMode) {
        mTagMode = tagMode;
        _update();
    }

    public int getBgColor() {
        return mBgColor;
    }

    public void setBgColor(int bgColor) {
        mBgColor = bgColor;
        // 设置颜色调用这个就行，回调onDraw()
        invalidate();
    }

    public int getBorderColor() {
        return mBorderColor;
    }

    public void setBorderColor(int borderColor) {
        mBorderColor = borderColor;
        invalidate();
    }

    public int getTextColor() {
        return mTextColor;
    }

    public void setTextColor(int textColor) {
        mTextColor = textColor;
        invalidate();
    }

    public int getBgColorChecked() {
        return mBgColorChecked;
    }

    public void setBgColorChecked(int bgColorChecked) {
        mBgColorChecked = bgColorChecked;
        invalidate();
    }

    public int getBorderColorChecked() {
        return mBorderColorChecked;
    }

    public void setBorderColorChecked(int borderColorChecked) {
        mBorderColorChecked = borderColorChecked;
        invalidate();
    }

    public int getTextColorChecked() {
        return mTextColorChecked;
    }

    public void setTextColorChecked(int textColorChecked) {
        mTextColorChecked = textColorChecked;
        invalidate();
    }

    public int getScrimColor() {
        return mScrimColor;
    }

    public void setScrimColor(int scrimColor) {
        mScrimColor = scrimColor;
        invalidate();
    }

    public float getTextSize() {
        return mTextSize;
    }

    public void setTextSize(float textSize) {
        mTextSize = textSize;
    }

    public float getBorderWidth() {
        return mBorderWidth;
    }

    public void setBorderWidth(float borderWidth) {
        mBorderWidth = borderWidth;
    }

    public float getRadius() {
        return mRadius;
    }

    public void setRadius(float radius) {
        mRadius = radius;
        invalidate();
    }

    public String getText() {
        return mText;
    }

    public void setText(String text) {
        mText = text;
        _update();
    }

    public String getTextChecked() {
        return mTextChecked;
    }

    public void setTextChecked(String textChecked) {
        mTextChecked = textChecked;
        _update();
    }

    public int getHorizontalPadding() {
        return mHorizontalPadding;
    }

    public void setHorizontalPadding(int horizontalPadding) {
        mHorizontalPadding = horizontalPadding;
        _update();
    }

    public int getVerticalPadding() {
        return mVerticalPadding;
    }

    public void setVerticalPadding(int verticalPadding) {
        mVerticalPadding = verticalPadding;
        _update();
    }

    public Drawable getDecorateIcon() {
        return mDecorateIcon;
    }

    public void setDecorateIcon(Drawable decorateIcon) {
        mDecorateIcon = decorateIcon;
        _update();
    }

    public Drawable getDecorateIconChange() {
        return mDecorateIconChange;
    }

    public void setDecorateIconChange(Drawable decorateIconChange) {
        mDecorateIconChange = decorateIconChange;
        _update();
    }

    public int getIconPadding() {
        return mIconPadding;
    }

    public void setIconPadding(int iconPadding) {
        mIconPadding = iconPadding;
        _update();
    }

    public boolean isAutoToggleCheck() {
        return mIsAutoToggleCheck;
    }

    public void setAutoToggleCheck(boolean autoToggleCheck) {
        mIsAutoToggleCheck = autoToggleCheck;
    }

    /**
     * 调用这些接口进行属性设置如果最后可能会改变按钮的大小的话最后调用一下这个接口，以刷新界面，建议属性直接在布局里设置
     * 只需要回调onDraw()的话调用invalidate()就可以了
     */
    private void _update() {
        requestLayout();
        invalidate();
    }

    /**
     * ==================================== 点击监听 ====================================
     */

    private OnCheckedChangeListener mCheckListener;


    public void setCheckListener(OnCheckedChangeListener onCheckedChangeListener) {
        mCheckListener = onCheckedChangeListener;
    }

    public interface OnCheckedChangeListener {
        void onCheckedChanged(boolean isChecked);
    }
}
