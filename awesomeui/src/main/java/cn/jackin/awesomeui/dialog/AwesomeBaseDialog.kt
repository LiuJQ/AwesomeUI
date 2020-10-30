package cn.jackin.awesomeui.dialog

import android.content.Context
import androidx.appcompat.app.AppCompatDialog

open class AwesomeBaseDialog(context: Context, themeResId: Int) :
    AppCompatDialog(context, themeResId) {
    private var cancelable = false

    override fun setCancelable(cancelable: Boolean) {
        super.setCancelable(cancelable)
        if (this.cancelable != cancelable) {
            this.cancelable = cancelable
            onSetCancelable(cancelable)
        }
    }

    protected open fun onSetCancelable(cancelable: Boolean) {
        //override to process something when cancelable is set
    }
}