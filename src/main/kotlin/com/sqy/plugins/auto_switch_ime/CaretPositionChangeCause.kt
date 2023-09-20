package com.sqy.plugins.auto_switch_ime

enum class CaretPositionChangeCause(
        val id : String
) {
    TYPED("type"),
    MOUSE_PRESSED("mouse-press"),
    ONE_CARET_MOVE("one-caret-move")
}