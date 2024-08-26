package com.friday.plugins.auto_switch_ime.areaDecide

import com.friday.plugins.auto_switch_ime.language.PsiFileLanguage
import com.intellij.lang.Language
import com.intellij.psi.PsiElement

object AreaDeciderDelegate {

    fun getPsiElementLocation(language: Language, psiElement: PsiElement, editorOffset : Int, isLineEnd : Boolean) : PsiElementLocation {
        return PsiFileLanguage.getAreaDecider(language)?.getPsiElementLocation(psiElement, editorOffset, isLineEnd)?: PsiElementLocation()
    }

}