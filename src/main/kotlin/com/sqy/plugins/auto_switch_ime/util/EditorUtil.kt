package com.sqy.plugins.auto_switch_ime.util

import com.intellij.lang.Language
import com.intellij.openapi.editor.Editor
import com.intellij.psi.PsiDocumentManager
import com.intellij.psi.PsiFile

object EditorUtil {

    fun getPsiFile(editor: Editor) : PsiFile? {
        return editor.project?.let { project ->
            PsiDocumentManager.getInstance(project).getPsiFile(editor.document)
        }
    }

    fun getLanguage(editor: Editor): Language? {
        return getPsiFile(editor)?.language
    }

}