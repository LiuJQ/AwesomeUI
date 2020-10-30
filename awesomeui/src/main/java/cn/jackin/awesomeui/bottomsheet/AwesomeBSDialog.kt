package cn.jackin.awesomeui.bottomsheet

import android.annotation.SuppressLint
import android.content.Context
import android.content.DialogInterface
import android.text.InputFilter
import android.text.TextWatcher
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.CheckBox
import android.widget.FrameLayout
import android.widget.TextView
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import cn.jackin.awesomeui.ActionType
import cn.jackin.awesomeui.R
import cn.jackin.awesomeui.bottomsheet.internal.*
import cn.jackin.awesomeui.bottomsheet.model.ActionItemModel
import cn.jackin.awesomeui.bottomsheet.model.AwesomeBSLevel
import cn.jackin.awesomeui.utility.DensityUtil
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.textfield.TextInputEditText

/**
 * Awesome BottomSheet Dialog
 */
class AwesomeBSDialog {

    interface OnActionItemClickListener {
        fun onClick(dialog: BottomSheetDialog, itemView: View, position: Int, tag: String)
    }

    /**
     * 完全自定义View（透明主题）
     */
    class CustomBuilder(val context: Context) {
        private lateinit var mDialog: BottomSheetDialog

        private var customContentView: View? = null

        fun setContentView(@LayoutRes contentViewRes: Int): CustomBuilder {
            customContentView = LayoutInflater.from(context).inflate(contentViewRes, null, false)
            return this
        }

        fun setContentView(contentView: View): CustomBuilder {
            customContentView = contentView
            return this
        }

        fun create(): BottomSheetDialog {
            mDialog = BottomSheetDialog(context, R.style.AwesomeUI_Dialog_BottomSheet_Transparent)
            customContentView?.let {
                mDialog.setContentView(it)
            }
            return mDialog
        }
    }

    /**
     * 默认主题完全自定义View样式
     */
    class CommonBuilder(val context: Context) : AwesomeBSDialogBuilder<CommonBuilder>(context) {

        private var customContentView: View? = null

        fun setContentView(@LayoutRes contentViewRes: Int): CommonBuilder {
            customContentView = LayoutInflater.from(context).inflate(contentViewRes, null, false)
            return this
        }

        fun setContentView(contentView: View): CommonBuilder {
            customContentView = contentView
            return this
        }

        override fun onCreateContent(dialog: BottomSheetDialog, context: Context): View? {
            return customContentView
        }

    }

    class MessageBuilder(context: Context) : AwesomeBSDialogBuilder<MessageBuilder>(context) {

        private var mMessage: CharSequence? = null
        fun setMessage(message: CharSequence): MessageBuilder {
            mMessage = message
            return this
        }

        /**
         * 设置提示弹窗级别
         * @see AwesomeBSLevel
         */
        fun setLevel(level: AwesomeBSLevel): MessageBuilder {
            mLevel = level
            return this
        }

        @SuppressLint("InflateParams")
        override fun onCreateContent(dialog: BottomSheetDialog, context: Context): View? {
            val content = LayoutInflater.from(context)
                .inflate(R.layout.awesomeui_dialog_content_message, null, false)
            content.findViewById<TextView>(R.id.awesome_content_message).text = mMessage
            return content
        }
    }

    class InputBuilder(var context: Context) : AwesomeBSDialogBuilder<InputBuilder>(context) {
        private var mHintRes: String? = null
        private var mInputType: Int? = null
        private var mTextWatcher: TextWatcher? = null
        private var mTextReceiver: ((text: String?) -> Unit)? = null
        private var mMaxLength: Int? = null
        private var mOriginText: String? = null
        private var switchVisible = false
        private lateinit var mEditText: TextInputEditText

        fun setHint(hintRes: Int): InputBuilder {
            mHintRes = context.resources.getString(hintRes)
            return this
        }

        fun setHint(hintRes: String?): InputBuilder {
            mHintRes = hintRes
            return this
        }

        fun setInputType(inputType: Int): InputBuilder {
            mInputType = inputType
            return this
        }

        fun setTextWatcher(watcher: TextWatcher): InputBuilder {
            mTextWatcher = watcher
            return this
        }

        fun setTextReceiver(receiver: ((text: String?) -> Unit)?): InputBuilder {
            mTextReceiver = receiver
            return this
        }

        fun setMaxLength(length: Int): InputBuilder {
            mMaxLength = length
            return this
        }

        fun setOriginText(text: String): InputBuilder {
            mOriginText = text
            return this
        }

        fun enableSwitch(): InputBuilder {
            switchVisible = true
            return this
        }

        override fun onCreateContent(dialog: BottomSheetDialog, context: Context): View? {
            val content = LayoutInflater.from(context)
                .inflate(R.layout.awesomeui_dialog_content_input, null, false)
            mEditText = content.findViewById(R.id.awesome_input_edt)
            mEditText.imeOptions = EditorInfo.IME_ACTION_DONE
            mHintRes?.let {
                mEditText.hint = it
            }
            mTextWatcher?.let {
                mEditText.addTextChangedListener(it)
            }
            mInputType?.let {
                mEditText.inputType = it
            }
            mMaxLength?.let {
                mEditText.filters = arrayOf(InputFilter.LengthFilter(it))
            }
            mOriginText?.let {
                mEditText.setText(it)
            }
            dialog.setOnDismissListener {
                val imm: InputMethodManager = mEditText.context
                    .getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                if (imm.isActive) {
                    imm.hideSoftInputFromWindow(mEditText.windowToken, 0)
                }
            }
            with(content.findViewById<CheckBox>(R.id.awesome_input_checkbox)) {
                visibility = if (switchVisible) View.VISIBLE else View.GONE
                setOnCheckedChangeListener { _, isChecked ->
                    if (isChecked) {
                        mEditText.transformationMethod = HideReturnsTransformationMethod.getInstance()
                    } else {
                        mEditText.transformationMethod = PasswordTransformationMethod.getInstance()
                    }
                }
            }
            return content
        }

        override fun deliverResult(actionType: ActionType) {
            if (actionType == ActionType.NEGATIVE) {
                return
            }
            val result: String? = if (mEditText.text != null) mEditText.text.toString() else null
            mTextReceiver?.invoke(result)
        }

        override fun processOnShow(dialogInterface: DialogInterface) {
            super.processOnShow(dialogInterface)
            val dialog = dialogInterface as BottomSheetDialog
            val bottomSheet: FrameLayout = dialog.findViewById(com.google.android.material.R.id.design_bottom_sheet)!!
            val bottomSheetBehavior: BottomSheetBehavior<FrameLayout> = BottomSheetBehavior.from(bottomSheet)
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
        }
    }

    class ActionListBuilder(context: Context, needRightCheck: Boolean) :
        AwesomeBottomSheetBuilder<ActionListBuilder>(context) {
        private val mNeedRightCheck: Boolean = needRightCheck
        private val mItems = mutableListOf<ActionItemModel>()
        private var mCheckedIndex: Int? = 0
        private var mOnItemClickListener: OnActionItemClickListener? = null

        fun setCheckedIndex(checkedIndex: Int): ActionListBuilder {
            mCheckedIndex = checkedIndex
            return this
        }

        fun setOnActionItemClickListener(listener: OnActionItemClickListener): ActionListBuilder {
            mOnItemClickListener = listener
            return this
        }

        fun addItem(item: ActionItemModel): ActionListBuilder {
            mItems.add(item)
            return this
        }

        fun addItem(textAdTag: String): ActionListBuilder {
            mItems.add(
                ActionItemModel(
                    textAdTag,
                    textAdTag,
                    null
                )
            )
            return this
        }

        fun addItem(text: CharSequence, tag: String): ActionListBuilder {
            mItems.add(
                ActionItemModel(
                    text,
                    tag,
                    null
                )
            )
            return this
        }

        override fun onCreateContent(dialog: BottomSheetDialog, context: Context): View? {
            val recyclerView = RecyclerView(context)
            recyclerView.overScrollMode = RecyclerView.OVER_SCROLL_NEVER
            val adapter = BottomSheetListAdapter(mNeedRightCheck)
            recyclerView.adapter = adapter
            recyclerView.layoutManager = object : LinearLayoutManager(context) {
                override fun generateDefaultLayoutParams(): RecyclerView.LayoutParams {
                    return RecyclerView.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT
                    )
                }
            }
            recyclerView.addItemDecoration(BottomSheetListItemDecoration(context))
            adapter.setData(null, null, mItems)
            adapter.setOnItemClickListener(object : OnItemClickListener {
                override fun onItemClick(
                    vh: RecyclerView.ViewHolder,
                    dataPos: Int,
                    model: ActionItemModel
                ) {
                    if (mOnItemClickListener != null) {
                        mOnItemClickListener!!.onClick(dialog, vh.itemView, dataPos, model.tag)
                    }
                }
            })
            mCheckedIndex?.let {
                adapter.setCheckedIndex(it)
                recyclerView.scrollToPosition(it)
            }
            return recyclerView
        }
    }

    class ActionGridBuilder(context: Context) :
        AwesomeBottomSheetBuilder<ActionGridBuilder>(context) {
        private val mItems = mutableListOf<ActionItemModel>()
        private var mOnItemClickListener: OnActionItemClickListener? = null

        fun setOnActionItemClickListener(listener: OnActionItemClickListener): ActionGridBuilder {
            mOnItemClickListener = listener
            return this
        }

        fun addItem(textAdTag: String, imageRes: Int): ActionGridBuilder {
            mItems.add(
                ActionItemModel(
                    textAdTag,
                    textAdTag,
                    imageRes
                )
            )
            return this
        }

        fun addItem(text: CharSequence, tag: String, imageRes: Int): ActionGridBuilder {
            mItems.add(
                ActionItemModel(
                    text,
                    tag,
                    imageRes
                )
            )
            return this
        }

        override fun onCreateTitle(parent: View) {
            super.onCreateTitle(parent)
            parent.findViewById<View>(R.id.awesome_bs_title_divider).visibility = View.GONE
        }

        override fun onCreateContent(dialog: BottomSheetDialog, context: Context): View? {
            val recyclerView = RecyclerView(context)
            recyclerView.overScrollMode = RecyclerView.OVER_SCROLL_NEVER
            val adapter = BottomSheetGridAdapter()
            recyclerView.adapter = adapter
            recyclerView.layoutManager = object : GridLayoutManager(context, DEFAULT_COLUMN_COUNT) {
                override fun generateDefaultLayoutParams(): RecyclerView.LayoutParams {
                    return RecyclerView.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT
                    )
                }
            }
            recyclerView.addItemDecoration(BottomSheetGridItemDecoration(DensityUtil.dip2px(10f)))
            adapter.setData(mItems)
            adapter.setOnItemClickListener(object : OnItemClickListener {
                override fun onItemClick(
                    vh: RecyclerView.ViewHolder,
                    dataPos: Int,
                    model: ActionItemModel
                ) {
                    if (mOnItemClickListener != null) {
                        mOnItemClickListener!!.onClick(dialog, vh.itemView, dataPos, model.tag)
                    }
                }
            })
            return recyclerView
        }

        companion object {
            const val DEFAULT_COLUMN_COUNT = 4
        }
    }
}