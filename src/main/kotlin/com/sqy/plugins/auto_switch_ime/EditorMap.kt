package com.sqy.plugins.auto_switch_ime

import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.components.Service
import com.intellij.openapi.editor.Editor
import com.intellij.psi.PsiFile
import java.util.concurrent.ConcurrentHashMap

class EditorMap : ConcurrentHashMap<Editor,PsiFile>() {

    @Service
    class EditorPsiFileMap : ConcurrentHashMap<Editor,PsiFile>()

    @Service
    class EditorCaretListenerMap() : ConcurrentHashMap<Editor, SwitchIMECaretListener>()

    companion object{
        /**
         * 存储editor和caretListener的映射关系
         */
        val caretListenerMap : ConcurrentHashMap<Editor,SwitchIMECaretListener> = ApplicationManager.getApplication().getService(EditorCaretListenerMap::class.java)
        /**
         * 存储editor和psiFile的映射关系
         */
        val psiFileMap : ConcurrentHashMap<Editor,PsiFile> = ApplicationManager.getApplication().getService(EditorPsiFileMap::class.java)
    }
}