package com.friday.plugins.auto_switch_ime.areaDecide

import com.friday.plugins.auto_switch_ime.PsiElementLocation
import com.friday.plugins.auto_switch_ime.PsiFileLanguage
import com.intellij.lang.Language
import com.intellij.psi.PsiElement

object AreaDeciderDelegate {

    fun getPsiElementLocation(language: Language, psiElement: PsiElement, isLineEnd : Boolean) : PsiElementLocation {
        return PsiFileLanguage.getAreaDecider(language)?.getPsiElementLocation(psiElement, isLineEnd)?: PsiElementLocation()
    }

}