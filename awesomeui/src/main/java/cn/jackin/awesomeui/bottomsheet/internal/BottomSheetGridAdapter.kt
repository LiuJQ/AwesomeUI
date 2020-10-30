package cn.jackin.awesomeui.bottomsheet.internal

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import cn.jackin.awesomeui.R
import cn.jackin.awesomeui.bottomsheet.model.ActionItemModel

class BottomSheetGridAdapter : RecyclerView.Adapter<BottomSheetGridAdapter.VH>() {
    private val mData = mutableListOf<ActionItemModel>()
    private var mOnItemClickListener: OnItemClickListener? = null

    fun setOnItemClickListener(onItemClickListener: OnItemClickListener?) {
        mOnItemClickListener = onItemClickListener
    }

    fun setData(data: List<ActionItemModel>?) {
        mData.clear()
        if (data != null) {
            mData.addAll(data)
        }
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val vh = VH(
            LayoutInflater
                .from(parent.context)
                .inflate(R.layout.awesomeui_bottomsheet_griditem, parent, false)
        )
        vh.itemView.setOnClickListener {
            if (mOnItemClickListener != null) {
                val dataPos = vh.adapterPosition
                mOnItemClickListener!!.onItemClick(vh, dataPos, mData[dataPos])
            }
        }
        return vh
    }

    override fun getItemCount(): Int {
        return mData.size
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        holder.bindData(mData[position])
    }

    class VH(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val itemImage: ImageView = itemView.findViewById(R.id.awesome_bs_item_grid_image)
        private val itemText: TextView = itemView.findViewById(R.id.awesome_bs_item_grid_text)

        fun bindData(model: ActionItemModel) {
            val (text, _, imageRes) = model
            itemText.text = text
            imageRes?.let {
                itemImage.setImageResource(it)
            }
        }
    }
}