package com.sqy.plugins.auto_switch_ime.util

import com.intellij.lang.Language
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.fileEditor.FileEditorManager
import com.intellij.openapi.fileEditor.TextEditor
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiFile


object PsiFileUtil {

    fun getLanguage(psiFile: PsiFile?) : Language? {
        return psiFile?.language
    }

    fun getEditor(psiFile: PsiFile?) : Editor? {
        psiFile?.let {
            val project: Project = it.project
            val virtualFile = it.virtualFile

            if (virtualFile != null) {
                val fileEditorManager = FileEditorManager.getInstance(project)
                val editors = fileEditorManager.getEditors(virtualFile)
                if (editors.isNotEmpty()) {
                    return (editors[0] as? TextEditor)?.editor // 返回第一个匹配的编辑器
                }
            }
        }
        return null
    }

}