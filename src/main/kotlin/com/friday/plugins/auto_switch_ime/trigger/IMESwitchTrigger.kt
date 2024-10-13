package com.friday.plugins.auto_switch_ime.trigger

enum class IMESwitchTrigger(val description: String) {

    CHAR_TYPE("字符输入"),
    MOUSE_CLICKED("鼠标点击"),
    ARROW_KEYS_PRESSED("按下方向键"),
    AN_ACTION_HAPPENED("发生动作"),
    EDITOR_FOCUS_GAINED("光标切换到新的编辑器")
    ;

}