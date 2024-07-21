package com.sqy.plugins.auto_switch_ime.areaDecide

import com.intellij.lang.Language
import com.intellij.psi.PsiElement

object AreaDeciderDelegate {

    fun isCommentArea(language: Language, psiElement: PsiElement, isLineEnd : Boolean) : Boolean{
        return PsiFileLanguage.getAreaDecider(language)?.isCommentArea(psiElement, isLineEnd)?:false
    }

    fun isCodeArea(language: Language, psiElement: PsiElement, isLineEnd : Boolean) : Boolean {
        return PsiFileLanguage.getAreaDecider(language)?.isCodeArea(psiElement, isLineEnd)?:false
    }

}