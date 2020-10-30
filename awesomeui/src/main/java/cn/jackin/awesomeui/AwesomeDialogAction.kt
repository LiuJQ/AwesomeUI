package cn.jackin.awesomeui

class AwesomeDialogAction(val mActionText: CharSequence?) {
    var mActionType: ActionType = ActionType.NEUTRAL
    var mActionListener: ActionHandler? = null

    fun type(actionType: ActionType): AwesomeDialogAction {
        this.mActionType = actionType
        return this
    }

    fun listener(listener: ActionHandler?): AwesomeDialogAction {
        this.mActionListener = listener
        return this
    }
}