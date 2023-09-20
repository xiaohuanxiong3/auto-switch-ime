package com.sqy.plugins.auto_switch_ime

import com.intellij.codeInsight.editorActions.enter.EnterHandlerDelegate
import com.intellij.codeInsight.editorActions.enter.EnterHandlerDelegateAdapter
import com.intellij.openapi.actionSystem.DataContext
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.editor.actionSystem.EditorActionHandler
import com.intellij.openapi.util.Ref
import com.intellij.psi.PsiFile
import java.util.concurrent.ConcurrentHashMap

class CustomEnterHandlerDelegate : EnterHandlerDelegateAdapter() {

    private val caretListenerMap : ConcurrentHashMap<Editor, SwitchIMECaretListener> = EditorMap.caretListenerMap
    private val psiFileMap : ConcurrentHashMap<Editor, PsiFile> = EditorMap.psiFileMap

    override fun preprocessEnter(file: PsiFile, editor: Editor, caretOffset: Ref<Int>, caretAdvance: Ref<Int>, dataContext: DataContext, originalHandler: EditorActionHandler?): EnterHandlerDelegate.Result {
        caretListenerMap[editor]!!.caretPositionChange = 0
        return EnterHandlerDelegate.Result.Continue
    }

    override fun postProcessEnter(file: PsiFile, editor: Editor, dataContext: DataContext): EnterHandlerDelegate.Result {
        val caretListener = caretListenerMap[editor]!!
        val psiFile = caretListenerMap.getOrDefault(editor,null)
        if (caretListener.caretPositionChange > 0) {
            //
            println("caretPositionChanged caused by enter action")
            caretListener.caretPositionChange = 0
        }

        return EnterHandlerDelegate.Result.Continue
    }
}