package com.friday.plugins.auto_switch_ime.trigger

import com.friday.plugins.auto_switch_ime.service.AutoSwitchIMEService
import com.intellij.openapi.editor.event.EditorMouseEvent
import com.intellij.openapi.editor.event.EditorMouseListener

class MouseClickedHandler : EditorMouseListener {

    override fun mousePressed(event: EditorMouseEvent) {
        AutoSwitchIMEService.prepareWithNoPsiFileChanged(event.editor)
    }

    override fun mouseClicked(event: EditorMouseEvent) {
        AutoSwitchIMEService.handleWithNoPsiFileChanged(event.editor, IMESwitchTrigger.MOUSE_CLICKED)
    }

}