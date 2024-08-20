package com.friday.plugins.auto_switch_ime.trigger

import com.friday.plugins.auto_switch_ime.service.AutoSwitchIMEService
import com.intellij.openapi.actionSystem.DataContext
import com.intellij.openapi.editor.Caret
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.editor.actionSystem.EditorActionHandler

class ArrowKeysPressedHandler : EditorActionHandler {

    private val myOriginalHandler : EditorActionHandler?

    // 似乎 ArrowKeysPressedHandler 是在EDT线程上运行的
    // 那@Volatile感觉没有必要
    private var lastMoveTime : Long = 0

    private val MOVE_ALLOW_INTERVAL : Long = 300

    constructor(originalHandler : EditorActionHandler) {
        myOriginalHandler = originalHandler
    }

    override fun doExecute(editor: Editor, caret: Caret?, dataContext: DataContext?) {
        if (System.currentTimeMillis() - lastMoveTime > MOVE_ALLOW_INTERVAL) {
            AutoSwitchIMEService.prepareWithNoPsiFileChanged(editor)
            myOriginalHandler?.execute(editor, caret, dataContext)
            AutoSwitchIMEService.handleWithNoPsiFileChanged(editor, IMESwitchTrigger.ARROW_KEYS_PRESSED)
            lastMoveTime = System.currentTimeMillis()
        } else {
            myOriginalHandler?.execute(editor, caret, dataContext)
        }
    }

    override fun isEnabledForCaret(editor: Editor, caret: Caret, dataContext: DataContext?): Boolean {
        return true
    }

    private fun beforeExecute() {

    }

    private fun afterExecute() {

    }
}