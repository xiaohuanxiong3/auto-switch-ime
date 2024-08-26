package com.friday.plugins.auto_switch_ime

import com.friday.plugins.auto_switch_ime.areaDecide.PsiElementLocation
import com.intellij.openapi.editor.Editor
import com.intellij.psi.PsiFile
import java.util.concurrent.ConcurrentHashMap

class MyMap {

    companion object{
        /**
         * 存储editor和caretListener的映射关系
         */
//        val caretListenerMap : ConcurrentHashMap<Editor,SwitchIMECaretListener> = ApplicationManager.getApplication().getService(EditorCaretListenerMap::class.java)
        val caretListenerMap : ConcurrentHashMap<Editor, CustomEditorFactoryListener.SwitchIMECaretListener> = ConcurrentHashMap<Editor, CustomEditorFactoryListener.SwitchIMECaretListener>()
        /**
         * 存储editor和psiFile的映射关系
         */
//        val editorPsiFileMap : ConcurrentHashMap<Editor,PsiFile> = ApplicationManager.getApplication().getService(EditorPsiFileMap::class.java)
        val editorPsiFileMap : ConcurrentHashMap<Editor,PsiFile> = ConcurrentHashMap<Editor,PsiFile>()
        /**
         * 存储psiFile和editor的映射关系
         */
//        val psiFileEditorMap : ConcurrentHashMap<PsiFile,Editor> = ApplicationManager.getApplication().getService(PsiFileEditorMap::class.java)
        val psiFileEditorMap : ConcurrentHashMap<PsiFile,Editor> = ConcurrentHashMap<PsiFile,Editor>()
        /**
         * 存储editor 和 editor中上一次光标移动触发输入法切换时 光标所在的位置 对应的psiElement 对应的PsiElementLocation
         */
//        val psiElementLocationMap : ConcurrentHashMap<Editor, PsiElementLocation> = ApplicationManager.getApplication().getService(EditorPsiElementLocationMap::class.java)
        val psiElementLocationMap : ConcurrentHashMap<Editor, PsiElementLocation> = ConcurrentHashMap<Editor, PsiElementLocation>()

        /**
         * 存储editor和 自定义的 documentListener的映射关系
         */
        val documentListenerMap : ConcurrentHashMap<Editor, CustomEditorFactoryListener.DocumentChangeCountListener> = ConcurrentHashMap<Editor, CustomEditorFactoryListener.DocumentChangeCountListener>()
    }
}