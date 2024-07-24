package com.sqy.plugins.auto_switch_ime.util

import com.intellij.lang.Language
import com.intellij.psi.PsiFile

object PsiFileUtil {

    fun getLanguage(psiFile: PsiFile?) : Language? {
        return psiFile?.language
    }

}