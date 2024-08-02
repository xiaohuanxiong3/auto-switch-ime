package com.sqy.plugins.auto_switch_ime.areaDecide

import com.intellij.lang.Language
import com.intellij.psi.PsiElement
import com.sqy.plugins.auto_switch_ime.PsiFileLanguage
import com.sqy.plugins.auto_switch_ime.PsiElementLocation

object AreaDeciderDelegate {

    fun isCommentArea(language: Language, psiElement: PsiElement, isLineEnd : Boolean) : Boolean{
        return PsiFileLanguage.getAreaDecider(language)?.isCommentArea(psiElement, isLineEnd)?:false
    }

    fun isCodeArea(language: Language, psiElement: PsiElement, isLineEnd : Boolean) : Boolean {
        return PsiFileLanguage.getAreaDecider(language)?.isCodeArea(psiElement, isLineEnd)?:false
    }

    fun getPsiElementLocation(language: Language, psiElement: PsiElement, isLineEnd : Boolean) : PsiElementLocation {
        return PsiFileLanguage.getAreaDecider(language)?.getPsiElementLocation(psiElement, isLineEnd)?: PsiElementLocation()
    }

}