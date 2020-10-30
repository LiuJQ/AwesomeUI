package cn.jackin.awesomeui.dialog

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.TextView
import cn.jackin.awesomeui.ActionHandler
import cn.jackin.awesomeui.ActionType
import cn.jackin.awesomeui.AwesomeDialogAction
import cn.jackin.awesomeui.R

abstract class AwesomeDialogBuilder<T>(private val mContext: Context) {
    private var mCancelable = true
    private var mCanceledOnTouchOutside = true

    private var mTitle: String? = null
    private val mActions: MutableList<AwesomeDialogAction> = ArrayList()

    private var mDialog: AwesomeDialog? = null

    @Suppress("UNCHECKED_CAST")
    open fun setTitle(title: String?): T {
        if (title != null && title.isNotEmpty()) {
            mTitle = title + mContext.getString(R.string.tool_fixellipsize)
        }
        return this as T
    }

    open fun setTitle(resId: Int): T {
        return setTitle(mContext.resources.getString(resId))
    }

    @Suppress("UNCHECKED_CAST")
    open fun setCancelable(cancelable: Boolean): T {
        mCancelable = cancelable
        return this as T
    }

    @Suppress("UNCHECKED_CAST")
    open fun setCanceledOnTouchOutside(canceledOnTouchOutside: Boolean): T {
        mCanceledOnTouchOutside = canceledOnTouchOutside
        return this as T
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
        return addAction(mContext.resources.getString(strResId), listener)
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
        val action: AwesomeDialogAction = AwesomeDialogAction(
            str
        )
            .type(type)
            .listener(listener)
        mActions.add(action)
        return this as T
    }

    open fun show() {
        create().show()
    }

    @SuppressLint("InflateParams")
    open fun create(): AwesomeDialog {
        mDialog = AwesomeDialog(mContext)
        val rootView =
            LayoutInflater.from(mContext).inflate(R.layout.awesomeui_dialog_content, null, false)
        onCreateTitle(rootView)
        onCreateActions(rootView)

        val contentContainer = rootView.findViewById<FrameLayout>(R.id.awesome_dlg_content)
        val contentView = onCreateContent(mDialog!!, mContext)
        contentContainer.addView(contentView)

        val rootLayoutParams = ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        mDialog!!.setContentView(rootView, rootLayoutParams)
        mDialog!!.setCancelable(mCancelable)
        mDialog!!.setCanceledOnTouchOutside(mCanceledOnTouchOutside)
        return mDialog as AwesomeDialog
    }

    protected open fun onCreateTitle(parent: View) {
        val tv = parent.findViewById<TextView>(R.id.awesome_dlg_title)
        tv.text = mTitle
    }

    protected abstract fun onCreateContent(
            dialog: AwesomeDialog,
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
                button.setOnClickListener{ _ ->
                    it.mActionListener?.invoke(mDialog!!)
                }
            }
        } else {
            actionContainer.visibility = View.GONE
        }
    }
}