package cn.jackin.awesomeui.bottomsheet

import android.annotation.SuppressLint
import android.content.Context
import android.content.DialogInterface
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import cn.jackin.awesomeui.ActionHandler
import cn.jackin.awesomeui.ActionType
import cn.jackin.awesomeui.AwesomeDialogAction
import cn.jackin.awesomeui.R
import cn.jackin.awesomeui.bottomsheet.model.AwesomeBSLevel
import cn.jackin.awesomeui.utility.DensityUtil
import com.google.android.material.bottomsheet.BottomSheetDialog

abstract class AwesomeBSDialogBuilder<T>(private val context: Context) {
    private var mTitle: String? = null
    private val mActions: MutableList<AwesomeDialogAction> = ArrayList()
    protected var mLevel: AwesomeBSLevel = AwesomeBSLevel.NORMAL

    private lateinit var mDialog: BottomSheetDialog

    @Suppress("UNCHECKED_CAST")
    open fun setTitle(title: String?): T {
        if (title != null && title.isNotEmpty()) {
            mTitle = title + context.getString(R.string.tool_fixellipsize)
        }
        return this as T
    }

    open fun setTitle(resId: Int): T {
        return setTitle(context.resources.getString(resId))
    }

    /**
     * 添加对话框底部的操作按钮
     */
    @Suppress("UNCHECKED_CAST")
    open fun addAction(action: AwesomeDialogAction?): T {
        if (action != null) {
            mActions.add(action)
        }
        return this as T
    }

    /**
     * 添加正常类型的操作按钮
     *
     * @param strResId 文案resId
     * @param listener 点击回调事件
     */
    open fun addAction(strResId: Int, listener: ActionHandler?): T {
        return addAction(context.resources.getString(strResId), listener)
    }

    /**
     * 添加正常类型的操作按钮
     *
     * @param str      文案
     * @param listener 点击回调事件
     */
    open fun addAction(str: CharSequence?, listener: ActionHandler?): T {
        return addAction(str, ActionType.NEUTRAL, listener)
    }

    /**
     * 添加操作按钮
     *
     * @param str      文案
     * @param type     类型属性
     * @param listener 点击回调事件
     */
    @Suppress("UNCHECKED_CAST")
    open fun addAction(
            str: CharSequence?,
            type: ActionType,
            listener: ActionHandler?
    ): T {
        val action: AwesomeDialogAction = AwesomeDialogAction(str)
            .type(type)
            .listener(listener)
        mActions.add(action)
        return this as T
    }

    open fun show() {
        create().show()
    }

    @SuppressLint("InflateParams")
    open fun create(): BottomSheetDialog {
        mDialog = BottomSheetDialog(context)
        mDialog.setOnShowListener {
            processOnShow(it)
        }
        val rootView =
            LayoutInflater.from(context).inflate(R.layout.awesomeui_dialog_content, null, false)
        onCreateTitle(rootView)
        onCreateActions(rootView)

        val contentContainer = rootView.findViewById<FrameLayout>(R.id.awesome_dlg_content)
        val contentView = onCreateContent(mDialog, context)
        contentContainer.addView(contentView)

        mDialog.setContentView(rootView)
        return mDialog
    }

    @Suppress("DEPRECATION")
    protected open fun onCreateTitle(parent: View) {
        val tv = parent.findViewById<TextView>(R.id.awesome_dlg_title)
        tv.text = mTitle

        if (mLevel == AwesomeBSLevel.WARNING) {
            tv.setTextColor(context.resources.getColor(R.color.common_red))
        }
    }

    protected abstract fun onCreateContent(
        dialog: BottomSheetDialog,
        context: Context
    ): View?

    protected open fun onCreateActions(parent: View) {
        val actionContainer = parent.findViewById<LinearLayout>(R.id.awesome_dlg_actions)
        val actionNegative = actionContainer.findViewById<Button>(R.id.awesome_action_negative)
        val actionNeutral = actionContainer.findViewById<Button>(R.id.awesome_action_neutral)
        val actionPositive = actionContainer.findViewById<Button>(R.id.awesome_action_positive)
        actionNegative.visibility = View.GONE
        actionNeutral.visibility = View.GONE
        actionPositive.visibility = View.GONE
        if (mActions.size > 0) {
            actionContainer.visibility = View.VISIBLE
            mActions.forEach {
                val button: Button = when (it.mActionType) {
                    ActionType.POSITIVE -> {
                        actionPositive
                    }
                    ActionType.NEGATIVE -> {
                        actionNegative
                    }
                    ActionType.NEUTRAL -> {
                        actionNeutral
                    }
                }

                button.visibility = View.VISIBLE
                button.text = it.mActionText
                button.setOnClickListener { _ ->
                    it.mActionListener?.invoke(mDialog)
                    deliverResult(it.mActionType)
                }
            }
        } else {
            actionContainer.visibility = View.GONE
        }

        if (mLevel == AwesomeBSLevel.WARNING) {
//            actionPositive.setBackgroundResource(R.drawable.common_red_shape_10radius)
            swapNegativeAndPositiveBtn(actionContainer)
        }
    }

    private fun swapNegativeAndPositiveBtn(actionContainer: ViewGroup) {
        val childViews = ArrayList<View>()
        for (i in 0 until actionContainer.childCount) {
            childViews.add(actionContainer.getChildAt(i))
        }
        actionContainer.removeAllViews()
        for (i in (childViews.size - 1) downTo 0) {
            val params = childViews[i].layoutParams as LinearLayout.LayoutParams
            if (i == (childViews.size - 1)) {
                params.marginStart = 0
            } else {
                params.marginStart = DensityUtil.dip2px(10f)
            }
            actionContainer.addView(childViews[i], params)
        }
    }

    /**
     * 传递动作意图，以此作为子类给外部传递数据的时机
     * @param actionType 动作按钮类型
     */
    protected open fun deliverResult(actionType: ActionType) {
        //override to process something when user clicked action button
    }

    /**
     * 对话框onShow事件，子类可定制处理方案
     * @param dialogInterface 对话框接口
     */
    protected open fun processOnShow(dialogInterface: DialogInterface) {
        //override to process something when dialog shown
    }
}