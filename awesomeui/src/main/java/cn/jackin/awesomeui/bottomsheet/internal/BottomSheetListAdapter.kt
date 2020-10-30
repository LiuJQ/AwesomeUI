package cn.jackin.awesomeui.bottomsheet.internal

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import cn.jackin.awesomeui.R
import cn.jackin.awesomeui.bottomsheet.model.ActionItemModel

class BottomSheetListAdapter(private val mNeedMark: Boolean) : RecyclerView.Adapter<BottomSheetListAdapter.VH>() {
    private var mHeaderView: View? = null
    private var mFooterView: View? = null
    private val mData = mutableListOf<ActionItemModel>()
    private var mCheckedIndex = -1
    private var mOnItemClickListener: OnItemClickListener? = null

    fun setCheckedIndex(checkedIndex: Int) {
        mCheckedIndex = checkedIndex
        notifyDataSetChanged()
    }

    fun setOnItemClickListener(onItemClickListener: OnItemClickListener?) {
        mOnItemClickListener = onItemClickListener
    }

    fun setData(
        headerView: View?,
        footerView: View?,
        data: List<ActionItemModel>?
    ) {
        mHeaderView = headerView
        mFooterView = footerView
        mData.clear()
        if (data != null) {
            mData.addAll(data)
        }
        notifyDataSetChanged()
    }

    override fun getItemViewType(position: Int): Int {
        return if (position == 0 && mHeaderView != null) {
            ITEM_TYPE_HEADER
        } else if (position == itemCount - 1 && mFooterView != null) {
            ITEM_TYPE_FOOTER
        } else {
            ITEM_TYPE_NORMAL
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        if (viewType == ITEM_TYPE_HEADER) {
            return VH(mHeaderView!!)
        } else if (viewType == ITEM_TYPE_FOOTER) {
            return VH(mFooterView!!)
        }
        val vh = VH(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.awesomeui_bottomsheet_listitem, parent, false)
        )
        vh.itemView.setOnClickListener {
            if (mOnItemClickListener != null) {
                val adapterPosition = vh.adapterPosition
                val dataPos =
                    if (mHeaderView != null) adapterPosition - 1 else adapterPosition
                mOnItemClickListener!!.onItemClick(vh, dataPos, mData[dataPos])
            }
        }
        return vh
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        @Suppress("NAME_SHADOWING") var position = position
        if (holder.itemViewType != ITEM_TYPE_NORMAL) {
            return
        }
        if (mHeaderView != null) {
            position--
        }
        val itemModel: ActionItemModel = mData[position]
        holder.itemText.text = itemModel.text
        holder.itemText.tag = itemModel.tag
    }

    override fun getItemCount(): Int {
        return mData.size + (if (mHeaderView != null) 1 else 0) + if (mFooterView != null) 1 else 0
    }

    class VH(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val itemText: TextView = itemView.findViewById(R.id.awesome_bs_item_text)
    }

    companion object {
        const val ITEM_TYPE_HEADER = 1
        const val ITEM_TYPE_FOOTER = 2
        const val ITEM_TYPE_NORMAL = 3
    }

}