package cn.jackin.awesomeui.dialog

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import cn.jackin.awesomeui.R

abstract class AwesomeTipsBuilder<T>(private val mContext: Context) {
    private var mCancelable = true
    private var mCanceledOnTouchOutside = true

    private lateinit var mDialog: AwesomeDialog

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

    open fun create(): AwesomeDialog {
        mDialog = AwesomeDialog(mContext)
        val rootView =
            LayoutInflater.from(mContext).inflate(R.layout.awesomeui_dialog_tips, null, false)

        val contentContainer = rootView.findViewById<FrameLayout>(R.id.awesome_tips_content)
        val contentView = onCreateContent(mDialog, mContext)
        contentContainer.addView(contentView)

        val rootLayoutParams = ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        mDialog.setContentView(rootView, rootLayoutParams)
        mDialog.setCancelable(mCancelable)
        mDialog.setCanceledOnTouchOutside(mCanceledOnTouchOutside)
        return mDialog
    }

    protected abstract fun onCreateContent(
            dialog: AwesomeDialog,
            context: Context
    ): View?
}