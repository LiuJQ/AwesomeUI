package cn.jackin.awesomeui.bottomsheet.model

/**
 * @see NORMAL 普通级别
 * @see WARNING 警告级别
 */
sealed class AwesomeBSLevel {
    /**
     * 普通级别
     */
    object NORMAL : AwesomeBSLevel()

    /**
     * 警告级别
     */
    object WARNING : AwesomeBSLevel()
}