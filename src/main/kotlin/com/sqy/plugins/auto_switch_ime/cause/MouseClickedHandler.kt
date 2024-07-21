package com.sqy.plugins.auto_switch_ime.cause

import com.intellij.openapi.editor.event.EditorMouseEvent
import com.intellij.openapi.editor.event.EditorMouseListener
import com.sqy.plugins.auto_switch_ime.service.AutoSwitchIMEService

class MouseClickedHandler : EditorMouseListener {

    override fun mousePressed(event: EditorMouseEvent) {
        AutoSwitchIMEService.prepare(event.editor)
    }

    override fun mouseClicked(event: EditorMouseEvent) {
        AutoSwitchIMEService.handle(event.editor, CaretPositionChangeCause.MOUSE_CLICKED)
    }

}