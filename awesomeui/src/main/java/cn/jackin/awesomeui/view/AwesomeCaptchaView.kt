package cn.jackin.awesomeui.view

import android.content.Context
import android.content.res.TypedArray
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Rect
import android.graphics.drawable.Drawable
import android.text.InputFilter
import android.text.TextPaint
import android.util.AttributeSet
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.widget.AppCompatEditText
import cn.jackin.awesomeui.R
import kotlin.math.max
import kotlin.math.roundToInt

typealias OnCaptchaFinishListener = (text: CharSequence, length: Int) -> Unit

class AwesomeCaptchaView(context: Context, attrs: AttributeSet?) : AppCompatEditText(context, attrs) {

    private var mTextColor = 0
    private var mMaxLength = 4              // 输入的最大长度
    private var mStrokeWidth = 0            // 边框宽度
    private var mStrokeHeight = 0           // 边框高度
    private var mStrokeMargin = 20.0f       // 边框之间的距离
    private var mStrokeDrawable: Drawable? = null   // 方框的背景
    private var mOnInputFinishListener: OnCaptchaFinishListener? = null

    private val mRect: Rect = Rect()

    init {
        val typedArray: TypedArray = context.obtainStyledAttributes(attrs, R.styleable.AwesomeCaptchaView)
        for (i in 0 until typedArray.indexCount) {
            when (val index: Int = typedArray.getIndex(i)) {
                R.styleable.AwesomeCaptchaView_boxWidth -> {
                    mStrokeWidth = typedArray.getDimension(index, 60f).roundToInt()
                }
                R.styleable.AwesomeCaptchaView_boxHeight -> {
                    mStrokeHeight = typedArray.getDimension(index, 60f).roundToInt()
                }
                R.styleable.AwesomeCaptchaView_boxMargin -> {
                    mStrokeMargin = typedArray.getDimension(index, 20f)
                }
                R.styleable.AwesomeCaptchaView_boxBackground -> {
                    mStrokeDrawable = typedArray.getDrawable(index)
                }
                R.styleable.AwesomeCaptchaView_boxCount -> {
                    mMaxLength = typedArray.getInteger(index, 4)
                }
            }
        }
        typedArray.recycle()

        if (mStrokeDrawable == null) {
            throw NullPointerException("stroke drawable not allowed to be null!")
        }
        isLongClickable = false
        isCursorVisible = false
        setMaxLength(mMaxLength)
        setBackgroundColor(Color.TRANSPARENT)
    }

    private fun setMaxLength(maxLength: Int) {
        filters = if (maxLength >= 0) {
            arrayOf<InputFilter>(InputFilter.LengthFilter(maxLength))
        } else {
            arrayOfNulls<InputFilter>(0)
        }
    }

    override fun onTextContextMenuItem(id: Int): Boolean {
        return false
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        var width: Int = measuredWidth
        var height: Int = measuredHeight
        val widthMode: Int = MeasureSpec.getMode(widthMeasureSpec)
        val heightMode: Int = MeasureSpec.getMode(heightMeasureSpec)

        // 判断高度是否小于推荐高度
        if (height < mStrokeHeight) {
            height = mStrokeHeight
        }

        // 判断宽度是否小于推荐宽度
        val recommendWidth = mStrokeWidth * mMaxLength + mStrokeMargin * (mMaxLength - 1)
        if (width < recommendWidth) {
            width = recommendWidth.roundToInt()
        }

        setMeasuredDimension(
            MeasureSpec.makeMeasureSpec(width, widthMode),
            MeasureSpec.makeMeasureSpec(height, heightMode)
        )
    }

    override fun onDraw(canvas: Canvas) {
        mTextColor = currentTextColor
        setTextColor(Color.TRANSPARENT)
        setTextColor(mTextColor)
        // 重绘背景颜色
        drawStrokeBackground(canvas)
        // 重绘文本
        drawText(canvas)
    }

    /**
     * 重绘背景
     */
    private fun drawStrokeBackground(canvas: Canvas) {
        // 绘制方框背景颜色
        mRect.left = 0
        mRect.top = 0
        mRect.right = mStrokeWidth
        mRect.bottom = mStrokeHeight
        // 当前激活的索引
        val activatedIndex = max(0, editableText.length)
        val count: Int = canvas.save()
        for (i in 0 until mMaxLength) {
            mStrokeDrawable?.bounds = mRect
            if (activatedIndex == i) {
                mStrokeDrawable?.state = intArrayOf(android.R.attr.state_focused)
            } else {
                mStrokeDrawable?.state = intArrayOf(android.R.attr.state_enabled)
            }
            mStrokeDrawable?.draw(canvas)
            val dx: Float = mRect.right + mStrokeMargin
            // 移动画布
            canvas.save()
            canvas.translate(dx, 0f)
        }
        canvas.restoreToCount(count)
        canvas.translate(0f, 0f)
    }

    /**
     * 重绘文本
     */
    private fun drawText(canvas: Canvas) {
        val count: Int = canvas.saveCount
        canvas.translate(0f, 0f)
        val length: Int = editableText.length
        for (i in 0 until length) {
            val text: String = java.lang.String.valueOf(editableText[i])
            val textPaint: TextPaint = paint
            textPaint.color = mTextColor
            // 获取文本大小
            textPaint.getTextBounds(text, 0, 1, mRect)
            // 计算(x,y) 坐标
            val x: Int =
                (mStrokeWidth / 2 + (mStrokeWidth + mStrokeMargin) * i - mRect.centerX()).roundToInt()
            val y: Int = canvas.height / 2 + mRect.height() / 2
            canvas.drawText(text, x.toFloat(), y.toFloat(), textPaint)
        }
        canvas.restoreToCount(count)
    }

    override fun onTextChanged(
        text: CharSequence?,
        start: Int,
        lengthBefore: Int,
        lengthAfter: Int
    ) {
        super.onTextChanged(text, start, lengthBefore, lengthAfter)
        val textLength: Int = editableText.length
        if (textLength == mMaxLength) {
            hideSoftInput()
            mOnInputFinishListener?.invoke(editableText.toString(), mMaxLength)
        }
    }

    private fun hideSoftInput() {
        val imm: InputMethodManager =
            context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(windowToken, InputMethodManager.HIDE_NOT_ALWAYS)
    }

    fun setOnTextFinishListener(onInputFinishListener: OnCaptchaFinishListener?) {
        mOnInputFinishListener = onInputFinishListener
    }
}