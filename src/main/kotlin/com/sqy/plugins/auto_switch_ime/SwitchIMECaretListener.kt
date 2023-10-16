package com.sqy.plugins.auto_switch_ime

import com.intellij.openapi.diagnostic.Logger
import com.intellij.openapi.editor.actionSystem.EditorActionHandler
import com.intellij.openapi.editor.event.CaretEvent
import com.intellij.openapi.editor.event.CaretListener
import com.intellij.openapi.editor.impl.EditorImpl


class SwitchIMECaretListener : CaretListener {
    var caretPositionChange : Int = 0

    override fun caretPositionChanged(event: CaretEvent) {
        caretPositionChange++
        // println("caretPositionChanged")
    }


}