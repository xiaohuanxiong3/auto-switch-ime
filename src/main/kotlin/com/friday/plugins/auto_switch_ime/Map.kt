package com.friday.plugins.auto_switch_ime

import com.intellij.openapi.components.Service
import com.intellij.openapi.editor.Editor
import com.intellij.psi.PsiFile
import java.util.concurrent.ConcurrentHashMap

class Map {

    @Service
    class EditorPsiFileMap : ConcurrentHashMap<Editor,PsiFile>()

    @Service
    class PsiFileEditorMap : ConcurrentHashMap<PsiFile, Editor>()

    @Service
    class EditorCaretListenerMap() : ConcurrentHashMap<Editor, SwitchIMECaretListener>()

    @Service
    class EditorPsiElementLocationMap : ConcurrentHashMap<Editor, PsiElementLocation>()

    companion object{
        /**
         * 存储editor和caretListener的映射关系
         */
//        val caretListenerMap : ConcurrentHashMap<Editor,SwitchIMECaretListener> = ApplicationManager.getApplication().getService(EditorCaretListenerMap::class.java)
        val caretListenerMap : ConcurrentHashMap<Editor, SwitchIMECaretListener> = ConcurrentHashMap<Editor, SwitchIMECaretListener>()
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
    }
}