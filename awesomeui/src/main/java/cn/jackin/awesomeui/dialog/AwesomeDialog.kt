package cn.jackin.awesomeui.dialog

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.Window
import android.widget.TextView
import androidx.annotation.LayoutRes
import cn.jackin.awesomeui.R

class AwesomeDialog(context: Context): AwesomeBaseDialog(context, R.style.AwesomeUI_Dialog) {

    init {
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE)
        setCancelable(true)
        setCanceledOnTouchOutside(true)
    }

    class CustomBuilder(val context: Context) :
        AwesomeDialogBuilder<CustomBuilder>(context) {
        private var customContentView: View? = null

        fun setContentView(@LayoutRes contentViewRes: Int): CustomBuilder {
            customContentView = LayoutInflater.from(context).inflate(contentViewRes, null, false)
            return this
        }

        fun setContentView(contentView: View): CustomBuilder {
            customContentView = contentView
            return this
        }

        override fun onCreateContent(dialog: AwesomeDialog, context: Context): View? {
            return customContentView
        }
    }

    class MessageBuilder(context: Context) :
        AwesomeDialogBuilder<MessageBuilder>(context) {
        private var mMessage: CharSequence? = null

        fun setMessage(message: CharSequence): MessageBuilder {
            mMessage = message
            return this
        }

        @SuppressLint("InflateParams")
        override fun onCreateContent(dialog: AwesomeDialog, context: Context): View? {
            val content = LayoutInflater.from(context)
                .inflate(R.layout.awesomeui_dialog_content_message, null, false)
            content.findViewById<TextView>(R.id.awesome_content_message).text = mMessage
            return content
        }
    }

    class LoadingBuilder(context: Context) : AwesomeTipsBuilder<LoadingBuilder>(context) {
        init {
            setCancelable(false)
            setCanceledOnTouchOutside(false)
        }
        private var message: CharSequence? = null

        fun setMessage(value: CharSequence): LoadingBuilder {
            message = value
            return this
        }

        @SuppressLint("InflateParams")
        override fun onCreateContent(dialog: AwesomeDialog, context: Context): View? {
            val loading = LayoutInflater.from(context)
                .inflate(R.layout.awesomeui_dialog_tips_loading, null, false)
            if (message.isNullOrEmpty()) {
                message = context.getString(R.string.common_processing)
            }
            loading.findViewById<TextView>(R.id.awesome_tips_message).text = message
            return loading
        }
    }
}