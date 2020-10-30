package cn.jackin.awesomeui.bottomsheet

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.FrameLayout
import android.widget.TextView
import cn.jackin.awesomeui.R
import com.google.android.material.bottomsheet.BottomSheetDialog

abstract class AwesomeBottomSheetBuilder<T>(private val context: Context) {

    private var mTitle: CharSequence? = null
    private var mAddCancelBtn: Boolean = false
    private var mCancelText: String? = null
    private var mDialog: BottomSheetDialog? = null

    @Suppress("UNCHECKED_CAST")
    fun setTitle(title: CharSequence): T {
        mTitle = title
        return this as T
    }

    @Suppress("UNCHECKED_CAST")
    fun setAddCancel(addCancelBtn: Boolean): T {
        mAddCancelBtn = addCancelBtn
        return this as T
    }

    @Suppress("UNCHECKED_CAST")
    fun setCancelText(cancelText: String): T {
        mCancelText = cancelText
        return this as T
    }

    open fun show() {
        create().show()
    }

    open fun create(): BottomSheetDialog {
        mDialog = BottomSheetDialog(context)
        val rootView =
            LayoutInflater.from(context).inflate(R.layout.awesomeui_dialog_bottomsheet, null, false)
        onCreateTitle(rootView)
        onCreateActions(rootView)

        val contentContainer = rootView.findViewById<FrameLayout>(R.id.awesome_bs_content)
        val contentView = onCreateContent(mDialog!!, context)
        contentContainer.addView(contentView, ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT))

        mDialog!!.setContentView(rootView)
        return mDialog!!
    }

    protected open fun onCreateTitle(parent: View) {
        val tv = parent.findViewById<TextView>(R.id.awesome_bs_title)
        val divider = parent.findViewById<View>(R.id.awesome_bs_title_divider)
        if (mTitle.isNullOrBlank()) {
            tv.visibility = View.GONE
            divider.visibility = View.GONE
        } else {
            tv.visibility = View.VISIBLE
            divider.visibility = View.VISIBLE
            tv.text = mTitle
        }
    }

    protected abstract fun onCreateContent(
        dialog: BottomSheetDialog,
        context: Context
    ): View?

    protected open fun onCreateActions(parent: View) {
        val cancel = parent.findViewById<Button>(R.id.awesome_bs_cancel)
        val divider = parent.findViewById<View>(R.id.awesome_bs_cancel_divider)
        if (mAddCancelBtn) {
            cancel.visibility = View.VISIBLE
            divider.visibility = View.VISIBLE
            cancel.text = mCancelText ?: "cancel"
            cancel.setOnClickListener{
                mDialog!!.dismiss()
            }
        } else {
            cancel.visibility = View.GONE
            divider.visibility = View.GONE
        }
    }
}