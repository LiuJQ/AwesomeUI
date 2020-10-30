package cn.jackin.sample.samples

import android.content.Context
import android.text.InputType
import android.view.View
import android.widget.Toast
import cn.jackin.awesomeui.ActionType
import cn.jackin.awesomeui.bottomsheet.AwesomeBSDialog
import cn.jackin.awesomeui.bottomsheet.model.ActionItemModel
import cn.jackin.awesomeui.dialog.AwesomeDialog
import cn.jackin.sample.R
import com.google.android.material.bottomsheet.BottomSheetDialog

object AwesomeSample {

    /**
     * 全屏居中信息弹窗
     */
    fun popMessage(context: Context) {
        AwesomeDialog.MessageBuilder(context)
                .setTitle("提示")
                .setMessage("What the fuck ?")
                .addAction("Yes", ActionType.POSITIVE) { dialog ->
                    dialog.dismiss()
                    Toast.makeText(context, "Yes clicked", Toast.LENGTH_SHORT).show()
                }
                .addAction("No", ActionType.NEGATIVE) { dialog ->
                    dialog.dismiss()
                    Toast.makeText(context, "No clicked", Toast.LENGTH_SHORT).show()
                }
                .setCanceledOnTouchOutside(false)
                .create().show()
    }

    /**
     * 底部上拉信息弹窗
     */
    fun popBSMessage(context: Context) {
        AwesomeBSDialog.MessageBuilder(context)
                .setTitle("Tips")
                .setMessage("What the fuck ?")
                .addAction("Yes", ActionType.POSITIVE) { dialog ->
                    dialog.dismiss()
                    Toast.makeText(context, "Yes clicked", Toast.LENGTH_SHORT).show()
                }
                .addAction("No", ActionType.NEGATIVE) { dialog ->
                    dialog.dismiss()
                    Toast.makeText(context, "No clicked", Toast.LENGTH_SHORT).show()
                }
                .create().show()
    }

    /**
     * 底部上拉输入弹窗
     */
    fun popBSInput(context: Context) {
        AwesomeBSDialog.InputBuilder(context)
                .setTitle("Tips")
                .setHint("What's in your mind ?")
                .enableSwitch()
                .setInputType(InputType.TYPE_CLASS_TEXT)
                .setTextReceiver { text: String? ->
                    Toast.makeText(context, "你输入了： $text", Toast.LENGTH_SHORT).show()
                }
                .addAction("OK", ActionType.POSITIVE) { dialog ->
                    dialog.dismiss()
                    Toast.makeText(context, "OK clicked", Toast.LENGTH_SHORT).show()
                }
                .addAction("Cancel", ActionType.NEGATIVE) { dialog ->
                    dialog.dismiss()
                    Toast.makeText(context, "Cancel clicked", Toast.LENGTH_SHORT).show()
                }
                .create().show()
    }

    /**
     * 底部上拉列表弹窗
     */
    fun popBSAction(context: Context) {
        val builder = AwesomeBSDialog.ActionListBuilder(context, true)
                .setTitle("This is Title !!")
                .setAddCancel(true)
                .setCancelText("取消")
        for (i in 1..3) {
            builder.addItem(
                    ActionItemModel(
                            "Item $i",
                            "Item $i",
                            null
                    )
            )
        }
        builder
                .setCheckedIndex(2)
                .setOnActionItemClickListener(object : AwesomeBSDialog.OnActionItemClickListener {
                    override fun onClick(
                            dialog: BottomSheetDialog,
                            itemView: View,
                            position: Int,
                            tag: String
                    ) {
                        Toast.makeText(context, "No $position, $tag", Toast.LENGTH_SHORT).show()
                        dialog.dismiss()
                    }
                })
                .create().show()
    }

    /**
     * 底部上拉九宫格弹窗
     */
    fun popBSActionGrid(context: Context) {
        AwesomeBSDialog.ActionGridBuilder(context)
                .setTitle("分享到")
                .setAddCancel(true)
                .setCancelText("取消")
                .addItem("微信", R.drawable.icon_share_wx_friend)
                .addItem("朋友圈", R.drawable.icon_share_wx_moment)
                .addItem("微博", R.drawable.icon_share_weibo)
                .addItem("私信", R.drawable.icon_share_email_chat)
                .addItem("保存本地", R.drawable.icon_share_save)
                .setOnActionItemClickListener(object : AwesomeBSDialog.OnActionItemClickListener {
                    override fun onClick(
                            dialog: BottomSheetDialog,
                            itemView: View,
                            position: Int,
                            tag: String
                    ) {
                        Toast.makeText(context, "No $position, $tag", Toast.LENGTH_SHORT).show()
                    }
                })
                .create().show()
    }

    fun popLoading(context: Context) {
        AwesomeDialog.LoadingBuilder(context).setMessage("识别中").create().show()
    }
}