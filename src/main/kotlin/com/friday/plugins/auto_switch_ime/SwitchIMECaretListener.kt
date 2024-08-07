package com.friday.plugins.auto_switch_ime

import com.intellij.openapi.editor.event.CaretEvent
import com.intellij.openapi.editor.event.CaretListener


class SwitchIMECaretListener : CaretListener {

    var caretPositionChange : Int = 0

    override fun caretPositionChanged(event: CaretEvent) {
        caretPositionChange++
    }

}