package com.friday.plugins.auto_switch_ime.trigger

import com.friday.plugins.auto_switch_ime.service.AutoSwitchIMEService
import com.intellij.openapi.actionSystem.DataContext
import com.intellij.openapi.editor.Caret
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.editor.actionSystem.EditorActionHandler

class ArrowKeysPressedHandler : EditorActionHandler {

    private val myOriginalHandler : EditorActionHandler?

    constructor(originalHandler : EditorActionHandler) {
        myOriginalHandler = originalHandler
    }

    override fun doExecute(editor: Editor, caret: Caret?, dataContext: DataContext?) {
        AutoSwitchIMEService.prepareWithNoPsiFileChanged(editor)
        myOriginalHandler?.execute(editor, caret, dataContext)
        AutoSwitchIMEService.handleWithNoPsiFileChanged(editor, IMESwitchTrigger.ARROW_KEYS_PRESSED)
    }

    override fun isEnabledForCaret(editor: Editor, caret: Caret, dataContext: DataContext?): Boolean {
        return true
    }

    private fun beforeExecute() {

    }

    private fun afterExecute() {

    }
}