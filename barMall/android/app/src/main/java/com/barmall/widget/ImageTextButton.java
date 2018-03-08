package com.barmall.widget;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;

import com.barmall.R;

/**
 * 一个描述文字始终跟在图标下面的简单的自定义button
 * 在XML中
 * 通过android:button来设置图标
 * 通过android:text来设置图标下的描述文字
 * 通过android:gravity来设置图标在整个view中的显示位置
 */

/**
 * Created by zisha on 2018/3/8.
 */
public class ImageTextButton extends View {
    public static final int DEFAULT_TEXT_SIZE = 12;
    public static final int DEFAULT_TEXT_COLOR = 0xFFDDDDDD;
    public static final int DEFAULT_SHADOW_RADIUS = 0;
    public static final int DEFAULT_SHADOW_DX = 0;
    public static final int DEFAULT_SHADOW_DY = 0;
    public static final int DEFAULT_MAX_TEXT_LENGTH = 8;
    public static final int DEFAULT_DRAWABLE_WIDTH = 0;
    public static final int DEFAULT_DRAWABLE_HEIGHT = 0;


    private Context mContext;

    private int mGravity = Gravity.CENTER;
    private Drawable mSrcImage;
    private int mDrawablePadding;
    private int mMaxTextLength;

    private CharSequence mText;
    private Paint mTextPaint;
    private Paint mRoundPaint;
    private int mTextSize = DEFAULT_TEXT_SIZE;
    private ColorStateList mTextColor = ColorStateList.valueOf(DEFAULT_TEXT_COLOR);
    private float mShadowRadius = DEFAULT_SHADOW_RADIUS;
    private float mShadowDx = DEFAULT_SHADOW_DX;
    private float mShadowDy = DEFAULT_SHADOW_DY;
    private int mShadowColor = Color.BLACK;
    private int mDrawableWidth = DEFAULT_DRAWABLE_WIDTH;
    private int mDrawableHeight = DEFAULT_DRAWABLE_HEIGHT;

    public ImageTextButton(Context context) {
        this(context, null);
    }

    public ImageTextButton(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ImageTextButton(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        initAttribute(context, attrs);
        initTextPaint();
    }

    private void initAttribute(Context context, AttributeSet attrs) {
        mContext = context;

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.ImageTextButton);
        mGravity = a.getInt(R.styleable.ImageTextButton_android_gravity, mGravity);
        mSrcImage = a.getDrawable(R.styleable.ImageTextButton_android_button);
        mDrawablePadding = a.getDimensionPixelSize(R.styleable.ImageTextButton_android_drawablePadding, 0);

        mText = a.getText(R.styleable.ImageTextButton_android_text);
        mTextSize = a.getDimensionPixelSize(R.styleable.ImageTextButton_android_textSize,
                (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, mTextSize, mContext.getResources().getDisplayMetrics()));
        mTextColor = a.getColorStateList(R.styleable.ImageTextButton_android_textColor);
        mMaxTextLength = a.getInt(R.styleable.ImageTextButton_maxTextLength, DEFAULT_MAX_TEXT_LENGTH);
        mShadowRadius = a.getFloat(R.styleable.ImageTextButton_android_shadowRadius, mShadowRadius);
        mShadowDx = a.getFloat(R.styleable.ImageTextButton_android_shadowDx, mShadowRadius);
        mShadowDy = a.getFloat(R.styleable.ImageTextButton_android_shadowDy, mShadowRadius);
        mShadowColor = a.getInt(R.styleable.ImageTextButton_android_shadowColor, mShadowColor);
        mDrawableWidth = a.getDimensionPixelSize(R.styleable.ImageTextButton_drawableHeight, DEFAULT_DRAWABLE_WIDTH);
        mDrawableHeight = a.getDimensionPixelSize(R.styleable.ImageTextButton_drawableHeight, DEFAULT_DRAWABLE_HEIGHT);
        a.recycle();

        if (mSrcImage != null) {
            mSrcImage.setCallback(this);
            mSrcImage.setState(getDrawableState());
        }

        if (mText != null && mText.length() > mMaxTextLength) {
            mText = mText.subSequence(0, mMaxTextLength - 1) + "...";
        }

        if (mTextColor == null) {
            mTextColor = ColorStateList.valueOf(DEFAULT_TEXT_COLOR);
        }
    }

    public void setRoundBackground(int colorResId) {
        if (colorResId <= 0) {
            mRoundPaint.setColor(0);
        }

        mRoundPaint.setColor(mContext.getResources().getColor(colorResId));
    }

    /**
     * 更改显示的图标
     * @param resId 资源ID
     */
    public void setButton(int resId) {
        if (resId <= 0) {
            mSrcImage = null;
            invalidate();
            return;
        }

        mSrcImage = getResources().getDrawable(resId);
        if (mSrcImage != null) {
            mSrcImage.setCallback(this);
            mSrcImage.setState(getDrawableState());
        }
        invalidate();
    }

    private void initTextPaint() {
        mTextPaint = new Paint();
        mTextPaint.setAntiAlias(true);
        mTextPaint.setTextSize(mTextSize);
        mTextPaint.setColor(mTextColor.getColorForState(getDrawableState(), 0));
        mTextPaint.setShadowLayer(mShadowRadius, mShadowDx, mShadowDy, mShadowColor);

        mRoundPaint = new Paint();
        mRoundPaint.setAntiAlias(true);
        mRoundPaint.setColor(0);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawButtonWithText(canvas);
    }

    public void setText(int resId) {
        if (resId <= 0) {
            setText(null);
        } else {
            setText(mContext.getString(resId));
        }
    }

    public void setText(CharSequence text) {
        mText = text;
        if (mText != null && mText.length() > mMaxTextLength) {
            mText = mText.subSequence(0, mMaxTextLength - 1) + "...";
        }

        invalidate();
    }

    public void setTextAndColor(CharSequence text, int colorResId) {
        mTextColor = ColorStateList.valueOf(mContext.getResources().getColor(colorResId));
        mTextPaint.setColor(mTextColor.getColorForState(getDrawableState(), 0));
        setText(text);
    }

    private void updateTextColor() {
        mTextPaint.setColor(mTextColor.getColorForState(getDrawableState(), 0));
        invalidate();
    }

    private String getText() {
        return mText == null ? "" : mText.toString();
    }

    private float getTextSize() {
        return mText == null ? 0 : mTextPaint.getTextSize();
    }

    private void drawButtonWithText(Canvas canvas) {
        if (mSrcImage == null) {
            return;
        }
        int imageWidth = (mDrawableWidth == DEFAULT_DRAWABLE_WIDTH) ? mSrcImage.getIntrinsicWidth() : mDrawableWidth;
        int imageHeight = (mDrawableHeight == DEFAULT_DRAWABLE_HEIGHT) ? mSrcImage.getIntrinsicHeight() : mDrawableHeight;
        float textWidth = mTextPaint.measureText(getText());
        float calculateWidth = Math.max(imageWidth, textWidth);

        int left = 0;
        int top = (int) ((getHeight() - imageHeight - getTextSize() - mDrawablePadding) / 2);
        if (mGravity == Gravity.RIGHT) {
            left = (int) (getWidth() - (calculateWidth + imageWidth) / 2 - getPaddingRight());
        } else if (mGravity == Gravity.LEFT) {
            left = (int) ((calculateWidth - imageWidth) / 2) + getPaddingLeft();
        } else {
            left = (getWidth() >> 1) - (imageWidth >> 1);
        }

        if (mRoundPaint.getColor() != 0) {
            canvas.drawCircle(left + (imageWidth >> 1), top + (imageHeight >> 1), Math.max(imageWidth >> 1, imageHeight >> 1), mRoundPaint);
        }

        mSrcImage.setBounds(left, top, left + imageWidth, top + imageHeight);
        mSrcImage.draw(canvas);

        if (mText == null) {
            return;
        }

        float textLeft = left + imageWidth / 2 - textWidth / 2;
        float textTop = top + imageHeight + mTextPaint.getTextSize() + mDrawablePadding;
        canvas.drawText(mText, 0, mText.length(), textLeft, textTop, mTextPaint);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected boolean verifyDrawable(Drawable who) {
        return who == mSrcImage || super.verifyDrawable(who);
    }

    @Override
    protected void drawableStateChanged() {
        super.drawableStateChanged();
        updateTextColor();
        Drawable d = mSrcImage;
        if (d != null && d.isStateful()) {
            d.setState(getDrawableState());
        }
    }

}
