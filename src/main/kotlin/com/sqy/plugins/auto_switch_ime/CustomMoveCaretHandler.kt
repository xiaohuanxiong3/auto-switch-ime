package com.sqy.plugins.auto_switch_ime

import com.intellij.openapi.actionSystem.DataContext
import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.editor.Caret
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.editor.actionSystem.EditorActionHandler
import com.intellij.openapi.editor.event.CaretListener
import com.intellij.psi.PsiFile
import com.sqy.plugins.support.IMESwitchSupport
import java.util.concurrent.ConcurrentHashMap

class CustomMoveCaretHandler : EditorActionHandler {

    private val myOriginalHandler : EditorActionHandler?

    private val caretListenerMap : ConcurrentHashMap<Editor,SwitchIMECaretListener> = EditorMap.caretListenerMap
    private val psiFileMap : ConcurrentHashMap<Editor, PsiFile> = EditorMap.psiFileMap

    constructor(originalHandler : EditorActionHandler) {
        myOriginalHandler = originalHandler
    }

    override fun doExecute(editor: Editor, caret: Caret?, dataContext: DataContext?) {
        AutoSwitchIMEService.prepare(editor)
        myOriginalHandler?.execute(editor, caret,dataContext)
        AutoSwitchIMEService.handle(editor,IMEChangeCause.ONE_CARET_MOVE)
    }

    override fun isEnabledForCaret(editor: Editor, caret: Caret, dataContext: DataContext?): Boolean {
        return true
    }

    private fun beforeExecute() {

    }

    private fun afterExecute() {

    }
}