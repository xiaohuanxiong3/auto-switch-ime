package com.sqy.plugins.auto_switch_ime

import com.intellij.openapi.editor.Editor
import com.intellij.openapi.editor.event.EditorMouseEvent
import com.intellij.openapi.editor.event.EditorMouseListener
import com.intellij.psi.PsiFile
import java.util.concurrent.ConcurrentHashMap

class CustomEditorMouseListener() : EditorMouseListener {

    private val caretListenerMap : ConcurrentHashMap<Editor, SwitchIMECaretListener> = EditorMap.caretListenerMap
    private val psiFileMap : ConcurrentHashMap<Editor, PsiFile> = EditorMap.psiFileMap

    override fun mousePressed(event: EditorMouseEvent) {
        caretListenerMap[event.editor]!!.caretPositionChange = 0
    }

    override fun mouseClicked(event: EditorMouseEvent) {
        val caretListener = caretListenerMap[event.editor]!!
        val psiFile = caretListenerMap.getOrDefault(event.editor,null)
        if (caretListener.caretPositionChange > 0) {
            //
            println("caretPositionChanged caused by mouse-press")
            caretListener.caretPositionChange = 0
        }
    }

}