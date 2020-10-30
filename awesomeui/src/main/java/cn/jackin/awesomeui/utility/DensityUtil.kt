package cn.jackin.awesomeui.utility

import android.content.res.Resources
import android.util.DisplayMetrics

class DensityUtil {
    companion object {
        /**
         * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
         */
        @JvmStatic
        fun dip2px(dpValue: Float): Int {
            return (dpValue * getScreenDensity() + 0.5f).toInt()
        }

        /**
         * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
         */
        @JvmStatic
        fun px2dip(pxValue: Float): Int {
            return (pxValue / getScreenDensity() + 0.5f).toInt()
        }

        /**
         * 根据手机的分辨率从 sp 的单位 转成为 px(像素)
         */
        @JvmStatic
        fun sp2px(spValue: Float): Float {
            val fontScale: Float = getDisplayMetrics().scaledDensity
            return (spValue * fontScale + 0.5f)
        }

        /**
         * 获取屏幕密度(density).
         *
         * @return 屏幕密度
         */
        @JvmStatic
        fun getScreenDensity(): Float {
            return Resources.getSystem().displayMetrics.density
        }

        /**
         * 获取屏幕密度(displayMetrics).
         *
         * @return 屏幕密度
         */
        @JvmStatic
        fun getDisplayMetrics(): DisplayMetrics {
            return Resources.getSystem().displayMetrics
        }
    }
}