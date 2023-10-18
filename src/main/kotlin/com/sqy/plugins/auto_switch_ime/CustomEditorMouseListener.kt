package com.sqy.plugins.auto_switch_ime

import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.editor.event.EditorMouseEvent
import com.intellij.openapi.editor.event.EditorMouseListener
import com.intellij.psi.PsiFile
import com.sqy.plugins.support.IMESwitchSupport
import java.util.concurrent.ConcurrentHashMap

class CustomEditorMouseListener() : EditorMouseListener {

    override fun mousePressed(event: EditorMouseEvent) {
        AutoSwitchIMEService.prepare(event.editor)
    }

    override fun mouseClicked(event: EditorMouseEvent) {
        AutoSwitchIMEService.handle(event.editor,CaretPositionChangeCause.MOUSE_CLIKED)
    }

}