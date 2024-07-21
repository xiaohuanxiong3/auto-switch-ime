package com.sqy.plugins.auto_switch_ime.cause

import com.sqy.plugins.auto_switch_ime.handler.*

enum class CaretPositionChangeCause(val description: String) {

    CHAR_TYPED("字符输入"),
    MOUSE_CLICKED("鼠标点击"),
    ARROW_KEYS_PRESSED("按下方向键"),
    ENTER("按下回车键");

    companion object {

        fun getIMESwitchHandler(caretPositionChangeCause: CaretPositionChangeCause) : IMESwitchHandler? {
            return when(caretPositionChangeCause.description) {
                CHAR_TYPED.description -> CharTypedCausedIMESwitchHandler
                MOUSE_CLICKED.description -> MouseClickedCausedIMESwitchHandler
                ARROW_KEYS_PRESSED.description -> ArrowKeysPressedCausedIMESwitchHandler
                ENTER.description -> EnterCausedIMESwitchHandler
                else -> null
            }
        }

    }
}