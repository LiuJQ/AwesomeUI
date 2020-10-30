package cn.jackin.awesomeui.bottomsheet.internal

import androidx.recyclerview.widget.RecyclerView
import cn.jackin.awesomeui.bottomsheet.model.ActionItemModel

interface OnItemClickListener {
    fun onItemClick(vh: RecyclerView.ViewHolder, dataPos: Int, model: ActionItemModel)
}