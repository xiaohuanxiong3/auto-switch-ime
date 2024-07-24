package com.sqy.plugins.auto_switch_ime.cause

enum class CaretPositionChangeCause(val description: String) {

    CHAR_TYPED("字符输入"),
    MOUSE_CLICKED("鼠标点击"),
    ARROW_KEYS_PRESSED("按下方向键"),
    ENTER("按下回车键");

}