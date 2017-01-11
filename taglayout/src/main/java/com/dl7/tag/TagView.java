package com.dl7.tag;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.PathEffect;
import android.graphics.PorterDuff;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.support.annotation.IntDef;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.MotionEventCompat;
import android.text.TextUtils;
import android.text.method.ArrowKeyMovementMethod;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewParent;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

import com.dl7.tag.drawable.RotateDrawable;
import com.dl7.tag.utils.MeasureUtils;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


/**
 * Created by long on 2016/7/20.
 * TagView
 */
public class TagView extends TextView {

    // 无效数值
    public final static int INVALID_VALUE = -1;

    private Paint mPaint;
    private Paint mBorderPaint;
    // 背景色
    private int mBgColor;
    // 边框颜色
    private int mBorderColor;
    // 选中状态背景色
    private int mBgColorChecked = INVALID_VALUE;
    // 选中状态边框颜色
    private int mBorderColorChecked = INVALID_VALUE;
    // 选中状态字体颜色
    private int mTextColorChecked = INVALID_VALUE;
    // 边框大小
    private float mBorderWidth;
    // 边框角半径
    private float mRadius;
    // Tag内容
    private CharSequence mTagText;
    // 选中时Tag内容
    private CharSequence mTagTextChecked;
    // 字体水平空隙
    private int mHorizontalPadding;
    // 字体垂直空隙
    private int mVerticalPadding;
    // 边框矩形
    private RectF mRect;
    // 调整标志位，只做一次
    private boolean mIsAdjusted = false;
    // 标签是否被按住
    private boolean mIsTagPress = false;
    // 宽度固定
    private int mFitWidth = INVALID_VALUE;
    // 是否使能按压反馈
    private boolean mIsPressFeedback = false;
    // 原始标签颜色
    private int mTextColor = INVALID_VALUE;


    public TagView(Context context, String text) {
        super(context);
        setText(text);
        _init(context, null);
    }

    public TagView(Context context, AttributeSet attrs) {
        super(context, attrs);
        _init(context, attrs);
    }

    /**
     * 初始化
     *
     * @param context
     */
    private void _init(Context context, AttributeSet attrs) {
        mRect = new RectF();
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setStyle(Paint.Style.FILL);
        mBorderPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mBorderPaint.setStyle(Paint.Style.STROKE);
        mTagText = getText();
        // 设置字体占中
        setGravity(Gravity.CENTER);

        if (attrs != null) {
            final TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.TagView);
            try {
                mTagShape = a.getInteger(R.styleable.TagView_tag_shape, TagView.SHAPE_ROUND_RECT);
                mTagMode = a.getInteger(R.styleable.TagView_tag_mode, MODE_NORMAL);
                if (mTagMode == MODE_SINGLE_CHOICE || mTagMode == MODE_MULTI_CHOICE ||
                        mTagMode == MODE_ICON_CHECK_INVISIBLE || mTagMode == MODE_ICON_CHECK_CHANGE) {
                    mIsPressFeedback = true;
                    mIsAutoToggleCheck = true;
                    // 如果有文字切换，需要注意控制文字长度是否一行内能显示完全
                    mTagTextChecked = a.getString(R.styleable.TagView_tag_text_check);
                    mIsChecked = a.getBoolean(R.styleable.TagView_tag_checked, false);
                    if (mIsChecked) {
                        setText(mTagTextChecked);
                    }
                }
                mIsAutoToggleCheck = a.getBoolean(R.styleable.TagView_tag_auto_check, mIsAutoToggleCheck);
                mIsPressFeedback = a.getBoolean(R.styleable.TagView_tag_press_feedback, mIsPressFeedback);

                mBgColor = a.getColor(R.styleable.TagView_tag_bg_color, Color.WHITE);
                mBorderColor = a.getColor(R.styleable.TagView_tag_border_color, Color.parseColor("#ff333333"));
                mTextColor = a.getColor(R.styleable.TagView_tag_text_color, Color.parseColor("#ff666666"));
                if (mIsPressFeedback) {
                    mBgColorChecked = a.getColor(R.styleable.TagView_tag_bg_color_check, mTextColor);
                    mBorderColorChecked = a.getColor(R.styleable.TagView_tag_border_color_check, mTextColor);
                    mTextColorChecked = a.getColor(R.styleable.TagView_tag_text_color_check, Color.WHITE);
                } else {
                    mBgColorChecked = a.getColor(R.styleable.TagView_tag_bg_color_check, mBgColor);
                    mBorderColorChecked = a.getColor(R.styleable.TagView_tag_border_color_check, mBorderColor);
                    mTextColorChecked = a.getColor(R.styleable.TagView_tag_text_color_check, mTextColor);
                }
                mBorderWidth = a.getDimension(R.styleable.TagView_tag_border_width, MeasureUtils.dp2px(context, 0.5f));
                mBorderPaint.setStrokeWidth(mBorderWidth);
                mRadius = a.getDimension(R.styleable.TagView_tag_border_radius, MeasureUtils.dp2px(context, 5f));
                mHorizontalPadding = (int) a.getDimension(R.styleable.TagView_tag_horizontal_padding, MeasureUtils.dp2px(context, 5f));
                mVerticalPadding = (int) a.getDimension(R.styleable.TagView_tag_vertical_padding, MeasureUtils.dp2px(context, 5f));
                mIconPadding = (int) a.getDimension(R.styleable.TagView_tag_icon_padding, MeasureUtils.dp2px(context, 3f));
                Drawable iconDrawable = a.getDrawable(R.styleable.TagView_tag_icon);
                if (iconDrawable != null) {
                    mDecorateIcon = iconDrawable.getConstantState().newDrawable();
                }
                Drawable changeDrawable = a.getDrawable(R.styleable.TagView_tag_icon_change);
                if (changeDrawable != null) {
                    mIconCheckChange = changeDrawable.getConstantState().newDrawable();
                }
            } finally {
                a.recycle();
            }
        }
        setPadding(mHorizontalPadding, mVerticalPadding, mHorizontalPadding, mVerticalPadding);
        if (mDecorateIcon != null) {
            setCompoundDrawablePadding(mIconPadding);
        }
        setOriTextColor(mTextColor);

        setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mTagMode == MODE_EDIT) {
                    return;
                }
                if (mTagClickListener != null) {
                    mTagClickListener.onTagClick((int) getTag(), String.valueOf(mTagText), mTagMode);
                }
            }
        });
        setOnLongClickListener(new OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (mTagMode == MODE_EDIT) {
                    return false;
                }
                if (mTagLongClickListener != null) {
                    mTagLongClickListener.onTagLongClick((int) getTag(), String.valueOf(mTagText), mTagMode);
                }
                return mTagMode != MODE_EDIT;
            }
        });
    }

    @Override
    protected boolean getDefaultEditable() {
        return true;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int fitHeightSpec;
        if (MeasureSpec.getMode(heightMeasureSpec) != MeasureSpec.EXACTLY) {
            // 设置标签固定高度，标签只有一行
            int tagHeight = MeasureUtils.getFontHeight(getTextSize()) + mVerticalPadding * 2;
            fitHeightSpec = MeasureSpec.makeMeasureSpec(tagHeight, MeasureSpec.EXACTLY);
        } else {
            fitHeightSpec = heightMeasureSpec;
        }
        ViewParent parent = getParent();
        if (parent instanceof TagLayout) {
            int fitTagNum = ((TagLayout) getParent()).getFitTagNum();
            if (fitTagNum == INVALID_VALUE) {
                super.onMeasure(widthMeasureSpec, fitHeightSpec);
            } else {
                int availableWidth = ((TagLayout) getParent()).getAvailableWidth();
                int horizontalInterval = ((TagLayout) getParent()).getHorizontalInterval();
                int width = (availableWidth - (fitTagNum - 1) * horizontalInterval) / fitTagNum;
                // 设置标签固定宽度
                int fitWidthSpec = MeasureSpec.makeMeasureSpec(width, MeasureSpec.EXACTLY);
                super.onMeasure(fitWidthSpec, fitHeightSpec);
                mFitWidth = width;
            }
            if (!mIsChecked) {
                _adjustText();
            }
        } else {
            super.onMeasure(widthMeasureSpec, fitHeightSpec);
            _initIcon(INVALID_VALUE);
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
        float radius = mRadius;
        if (mTagShape == SHAPE_ARC) {
            radius = mRect.height() / 2;
        } else if (mTagShape == SHAPE_RECT) {
            radius = 0;
        }
        final boolean isChecked = mIsTagPress || mIsChecked;
        // 绘制背景
        if (isChecked) {
            mPaint.setColor(mBgColorChecked);
        } else {
            mPaint.setColor(mBgColor);
        }
        canvas.drawRoundRect(mRect, radius, radius, mPaint);
        // 绘制边框
        if (isChecked) {
            mBorderPaint.setColor(mBorderColorChecked);
        } else {
            mBorderPaint.setColor(mBorderColor);
        }
        canvas.drawRoundRect(mRect, radius, radius, mBorderPaint);

        super.onDraw(canvas);
    }

    /**
     * 调整内容，如果超出可显示的范围则做裁剪
     */
    private void _adjustText() {
        if (mIsAdjusted) {
            return;
        }
        mIsAdjusted = true;
        // 获取最大可用宽度
        int availableWidth;
        if (mFitWidth == INVALID_VALUE) {
            availableWidth = ((TagLayout) getParent()).getAvailableWidth();
        } else {
            availableWidth = mFitWidth;
        }
        mPaint.setTextSize(getTextSize());

        // 计算字符串长度
        float textWidth = mPaint.measureText(String.valueOf(mTagText));
        if (mTagMode != MODE_CHANGE && mDecorateIcon != null) {
            availableWidth -= MeasureUtils.getFontHeight(getTextSize()) + mIconPadding;
        }
        if (mTagMode == MODE_CHANGE) {
            availableWidth -= MeasureUtils.getFontHeight(getTextSize()) + mIconPadding;
            // 如果“换一换”三个字宽度超出，则改用一个"换"字
            if (textWidth + mHorizontalPadding * 2 > availableWidth) {
                textWidth = mPaint.measureText("换");
                if (textWidth + mHorizontalPadding * 2 > availableWidth) {
                    // 一个"换"字也超出就不显示字符,并把 DrawablePadding 清零让 Icon 居中
                    setText("");
                    mTagText = "";
                    textWidth = 0;
                    setCompoundDrawablePadding(0);
                } else {
                    setText("换");
                    mTagText = "换";
                }
            }
        }
        // 如果可用宽度不够用，则做裁剪处理，末尾不3个.
        else if (textWidth + mHorizontalPadding * 2 > availableWidth) {
            float pointWidth = mPaint.measureText(".");
            // 计算能显示的字体长度
            float maxTextWidth = availableWidth - mHorizontalPadding * 2 - pointWidth * 3;
            float tmpWidth = 0;
            StringBuilder strBuilder = new StringBuilder();
            for (int i = 0; i < mTagText.length(); i++) {
                char c = mTagText.charAt(i);
                float cWidth = mPaint.measureText(String.valueOf(c));
                // 计算每个字符的宽度之和，如果超过能显示的长度则退出
                if (tmpWidth + cWidth > maxTextWidth) {
                    break;
                }
                strBuilder.append(c);
                tmpWidth += cWidth;
            }
            // 末尾添加3个.并设置为显示字符
            strBuilder.append("...");
            setText(strBuilder.toString());
            textWidth = mPaint.measureText(strBuilder.toString());
        }

        _initIcon(textWidth);
    }

    /**
     * ==================================== 开放接口 ====================================
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

    public int getOriTextColor() {
        return mTextColor;
    }

    public void setOriTextColor(int textColor) {
        mTextColor = textColor;
        setTextColor(textColor);
        if (mDecorateIcon != null) {
            mDecorateIcon.setColorFilter(mTextColor, PorterDuff.Mode.SRC_IN);
        }
    }

    public int getBgColorChecked() {
        return mBgColorChecked;
    }

    public void setBgColorChecked(int bgColorChecked) {
        mBgColorChecked = bgColorChecked;
    }

    public int getBorderColorChecked() {
        return mBorderColorChecked;
    }

    public void setBorderColorChecked(int borderColorChecked) {
        mBorderColorChecked = borderColorChecked;
    }

    public int getTextColorChecked() {
        return mTextColorChecked;
    }

    public void setTextColorChecked(int textColorChecked) {
        mTextColorChecked = textColorChecked;
    }

    public float getBorderWidth() {
        return mBorderWidth;
    }

    public void setBorderWidth(float borderWidth) {
        mBorderWidth = borderWidth;
        mBorderPaint.setStrokeWidth(mBorderWidth);
    }

    public float getRadius() {
        return mRadius;
    }

    public void setRadius(float radius) {
        mRadius = radius;
    }

    public int getHorizontalPadding() {
        return mHorizontalPadding;
    }

    public void setHorizontalPadding(int horizontalPadding) {
        mHorizontalPadding = horizontalPadding;
        setPadding(mHorizontalPadding, mVerticalPadding, mHorizontalPadding, mVerticalPadding);
    }

    public int getVerticalPadding() {
        return mVerticalPadding;
    }

    public void setVerticalPadding(int verticalPadding) {
        mVerticalPadding = verticalPadding;
        setPadding(mHorizontalPadding, mVerticalPadding, mHorizontalPadding, mVerticalPadding);
    }

    public CharSequence getTagText() {
        return mTagText;
    }

    public void setTagText(CharSequence tagText) {
        mTagText = tagText;
        setText(tagText);
        mIsAdjusted = false;
    }

    public boolean isPressFeedback() {
        return mIsPressFeedback;
    }

    public void setPressFeedback(boolean pressFeedback) {
        mIsPressFeedback = pressFeedback;
    }

    /**
     * ==================================== 点击监听 ====================================
     */

    // 点击监听器
    private OnTagClickListener mTagClickListener;
    private OnTagLongClickListener mTagLongClickListener;
    private OnTagCheckListener mTagCheckListener;

    public void setTagClickListener(OnTagClickListener tagClickListener) {
        mTagClickListener = tagClickListener;
    }

    public void setTagLongClickListener(OnTagLongClickListener onTagLongClickListener) {
        mTagLongClickListener = onTagLongClickListener;
    }

    public void setTagCheckListener(OnTagCheckListener onTagCheckListener) {
        mTagCheckListener = onTagCheckListener;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (!mIsPressFeedback || mTagMode == MODE_EDIT) {
            return super.onTouchEvent(event);
        }
        switch (MotionEventCompat.getActionMasked(event)) {
            case MotionEvent.ACTION_DOWN:
                mIsTagPress = true;
                _switchIconColor();
                break;
            case MotionEvent.ACTION_MOVE:
                if (mIsTagPress && !_isViewUnder(event.getX(), event.getY())) {
                    mIsTagPress = false;
                    _switchIconColor();
                }
                break;
            case MotionEvent.ACTION_UP:
                if (_isViewUnder(event.getX(), event.getY())) {
                    _toggleTagCheckStatus();
                }
            case MotionEvent.ACTION_CANCEL:
                if (mIsTagPress) {
                    mIsTagPress = false;
                    _switchIconColor();
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
     * 点击监听器
     */
    public interface OnTagClickListener {
        void onTagClick(int position, String text, @TagMode int tagMode);
    }

    public interface OnTagLongClickListener {
        void onTagLongClick(int position, String text, @TagMode int tagMode);
    }

    public interface OnTagCheckListener {
        void onTagCheck(int position, String text, boolean isChecked);
    }

    /**
     * ==================================== 显示模式 ====================================
     */

    // 3种外形模式：圆角矩形、圆弧、直角矩形
    public final static int SHAPE_ROUND_RECT = 101;
    public final static int SHAPE_ARC = 102;
    public final static int SHAPE_RECT = 103;
    // 类型模式：正常、编辑、换一换、单选、多选、图标选中消失、图标选中切换
    public final static int MODE_NORMAL = 201;
    public final static int MODE_EDIT = 202;
    public final static int MODE_CHANGE = 203;
    public final static int MODE_SINGLE_CHOICE = 204;
    public final static int MODE_MULTI_CHOICE = 205;
    public final static int MODE_ICON_CHECK_INVISIBLE = 206;
    public final static int MODE_ICON_CHECK_CHANGE = 207;

    // 显示外形
    private int mTagShape = SHAPE_ROUND_RECT;
    // 显示类型
    private int mTagMode = MODE_NORMAL;
    // 装饰的icon
    private Drawable mDecorateIcon;
    private Drawable mIconCheckChange;
    // icon和文件间距
    private int mIconPadding = 0;
    // icon的左偏移量和选中时左偏移量
    private int mIconLeft = 0;
    private int mIconLeftChecked = 0;
    // icon大小
    private int mIconSize = 0;
    // 是否选中
    private boolean mIsChecked = false;
    // 是否自动切换选中状态，不使能可以灵活地选择切换，通过用于等待网络返回再做切换
    private boolean mIsAutoToggleCheck = false;
    // 是否初始化Icon
    private boolean mIsInitIcon = false;
    // 保存选中状态
    private boolean mSaveChecked = false;
    // 虚线路径
    private PathEffect mPathEffect = new DashPathEffect(new float[]{10, 5}, 0);


    public int getTagShape() {
        return mTagShape;
    }

    public void setTagShape(@TagShape int tagShape) {
        mTagShape = tagShape;
    }

    public int getTagMode() {
        return mTagMode;
    }

    public void setTagMode(@TagMode int tagMode) {
        mTagMode = tagMode;
        if (mTagMode == MODE_SINGLE_CHOICE || mTagMode == MODE_MULTI_CHOICE) {
            setPressFeedback(true);
            mIsAutoToggleCheck = true;
        } else if (mTagMode == MODE_EDIT) {
            _initEditMode();
        }
    }

    public void setIconRes(int iconResId) {
        mDecorateIcon = ContextCompat.getDrawable(getContext(), iconResId);
    }

    @Override
    public void setCompoundDrawablePadding(int pad) {
        mIconPadding = pad;
        super.setCompoundDrawablePadding(pad);
    }

    public boolean isAutoToggleCheck() {
        return mIsAutoToggleCheck;
    }

    public void setAutoToggleCheck(boolean autoToggleCheck) {
        mIsAutoToggleCheck = autoToggleCheck;
    }

    /**
     * 切换tag选中状态
     */
    private void _toggleTagCheckStatus() {
        if (mIsAutoToggleCheck) {
            _setTagCheckStatus(!mIsChecked);
            if (mTagCheckListener != null) {
                mTagCheckListener.onTagCheck((int) getTag(), String.valueOf(mTagText), mIsChecked);
            }
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
        _setTagCheckStatus(checked);
        if (mTagCheckListener != null) {
            mTagCheckListener.onTagCheck((int) getTag(), String.valueOf(mTagText), mIsChecked);
        }
    }

    /**
     * 清除选中状态
     */
    public void cleanTagCheckStatus() {
        _setTagCheckStatus(false);
    }

    private void _setTagCheckStatus(boolean isChecked) {
        mIsChecked = isChecked;
        if (mTagTextChecked != null) {
            setText(mIsChecked ? mTagTextChecked : mTagText);
            mDecorateIcon.setBounds(mIsChecked ? mIconLeftChecked : mIconLeft, 0,
                    mIconSize + (mIsChecked ? mIconLeftChecked : mIconLeft), mIconSize);
        }
        _switchIconStatus();
        _switchIconColor();
    }

    /**
     * 初始化 ICON
     *
     * @param textWidth
     */
    private void _initIcon(float textWidth) {
        if (!mIsInitIcon && (mTagMode == MODE_CHANGE || mDecorateIcon != null)) {
            mIconSize = MeasureUtils.getFontHeight(getTextSize());
            mIconLeft = 0;
            if (textWidth == INVALID_VALUE) {
                mPaint.setTextSize(getTextSize());
                textWidth = mPaint.measureText(String.valueOf(getText()));
                mIconLeft = (int) ((getMeasuredWidth() - textWidth - mIconSize) / 2) - mHorizontalPadding - mIconPadding / 2;
            } else if (mFitWidth != INVALID_VALUE) {
                mIconLeft = (int) ((getMeasuredWidth() - textWidth - mIconSize) / 2) - mHorizontalPadding - mIconPadding / 2;
            }
            if (mIconLeft < 0) {
                // 正常自适应大小时mIconLeft=0，固定大小时大于0，小于0需要处理下
                mIconLeft = 0;
            }
            if (!TextUtils.isEmpty(mTagTextChecked)) {
                textWidth = mPaint.measureText(String.valueOf(mTagTextChecked));
                mIconLeftChecked = (int) ((getMeasuredWidth() - textWidth - mIconSize) / 2) - mHorizontalPadding - mIconPadding / 2;
                if (mIconLeftChecked < 0) {
                    mIconLeftChecked = 0;
                }
            }
            if (mTagMode == MODE_CHANGE) {
                Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.ic_change);
                mDecorateIcon = new RotateDrawable(bitmap, mIconLeft);
            }
            if (mDecorateIcon != null) {
                mDecorateIcon.setBounds(mIconLeft, 0, mIconSize + mIconLeft, mIconSize);
                mDecorateIcon.setColorFilter(mTextColor, PorterDuff.Mode.SRC_IN);
                setCompoundDrawables(mDecorateIcon, null, null, null);
            }
            if (mIconCheckChange != null) {
                mIconCheckChange.setBounds(mIconLeft, 0, mIconSize + mIconLeft, mIconSize);
                mIconCheckChange.setColorFilter(mTextColorChecked, PorterDuff.Mode.SRC_IN);
            }
            mIsInitIcon = true;
        }
    }

    /**
     * 切换Icon状态
     */
    private void _switchIconStatus() {
        if (mDecorateIcon != null) {
            if (mTagMode == MODE_ICON_CHECK_INVISIBLE) {
                setCompoundDrawables(mIsChecked ? null : mDecorateIcon, null, null, null);
            } else if (mTagMode == MODE_ICON_CHECK_CHANGE) {
                setCompoundDrawables(mIsChecked ? mIconCheckChange : mDecorateIcon, null, null, null);
            }
        }
    }

    private void _switchIconColor() {
        boolean isCheck = mIsChecked || mIsTagPress;
        setTextColor(isCheck ? mTextColorChecked : mTextColor);
        if (mDecorateIcon != null) {
            mDecorateIcon.setColorFilter(isCheck ? mTextColorChecked : mTextColor, PorterDuff.Mode.SRC_IN);
        }
    }

    /**
     * 初始化编辑模式
     */
    private void _initEditMode() {
        setClickable(true);
        setFocusable(true);
        setFocusableInTouchMode(true);
        setHint("添加标签");
        mBorderPaint.setPathEffect(mPathEffect);
        mBgColor = Color.WHITE;
        setHintTextColor(Color.parseColor("#ffaaaaaa"));
        setOriTextColor(Color.BLACK);
        setMovementMethod(ArrowKeyMovementMethod.getInstance());
        requestFocus();
        setOnEditorActionListener(new OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_NULL
                        && (event != null && event.getKeyCode() == KeyEvent.KEYCODE_ENTER
                        && event.getAction() == KeyEvent.ACTION_DOWN)) {
                    if (!TextUtils.isEmpty(getText())) {
                        ((TagLayout) getParent()).addTag(getText().toString());
                        setText("");
                        _closeSoftInput();
                    }
                    return true;
                }
                return false;
            }
        });
    }

    /**
     * 离开编辑模式
     */
    public void exitEditMode() {
        if (mTagMode == MODE_EDIT) {
            clearFocus();
            setFocusable(false);
            setFocusableInTouchMode(false);
            setHint(null);
            setMovementMethod(null);
            _closeSoftInput();
        }
    }

    /**
     * 关闭软键盘
     */
    private void _closeSoftInput() {
        InputMethodManager inputMethodManager = (InputMethodManager) getContext()
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        //如果软键盘已经开启
        if (inputMethodManager.isActive()) {
            inputMethodManager.hideSoftInputFromWindow(getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    @IntDef({SHAPE_ROUND_RECT, SHAPE_ARC, SHAPE_RECT})
    @Retention(RetentionPolicy.SOURCE)
    @Target(ElementType.PARAMETER)
    public @interface TagShape {
    }

    @IntDef({MODE_NORMAL, MODE_EDIT, MODE_CHANGE, MODE_SINGLE_CHOICE, MODE_MULTI_CHOICE, MODE_ICON_CHECK_INVISIBLE, MODE_ICON_CHECK_CHANGE})
    @Retention(RetentionPolicy.SOURCE)
    @Target(ElementType.PARAMETER)
    public @interface TagMode {
    }
}
