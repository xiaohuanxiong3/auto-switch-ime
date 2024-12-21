package com.friday.plugins.auto_switch_ime.util

import com.friday.plugins.auto_switch_ime.Constants
import com.friday.plugins.auto_switch_ime.language.LanguageSpecificTool
import com.friday.plugins.auto_switch_ime.ui.AutoSwitchIMEStatusBarIconWidget
import com.intellij.injected.editor.EditorWindow
import com.intellij.lang.Language
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.wm.WindowManager
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

    fun getEditor(editor: Editor): Editor {
        return editor.let {
            it as? EditorWindow
        }?.delegate?:editor
    }

    fun updateStatusBarWidgetStatus(editor: Editor) {
        getPsiFile(editor)?.let { psiFile ->
            updateStatusBarWidgetStatus(editor, psiFile.language)
        }
    }

    fun updateStatusBarWidgetStatus(editor: Editor, language: Language) {
        editor.project?.let { project ->
            WindowManager.getInstance().getStatusBar(project).getWidget(Constants.STATUS_BAR_WIDGET_ID)?.let {
                it as? AutoSwitchIMEStatusBarIconWidget
            }?.setStatus(LanguageSpecificTool.getLanguageAutoSwitchStatus(language))
        }
    }

}