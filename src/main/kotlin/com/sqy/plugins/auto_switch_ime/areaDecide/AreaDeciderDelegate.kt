package com.sqy.plugins.auto_switch_ime.areaDecide

import com.intellij.lang.Language
import com.intellij.psi.impl.source.tree.LeafPsiElement

object AreaDeciderDelegate {

    fun isCommentArea(language: Language, psiElement: LeafPsiElement, isLineEnd : Boolean) : Boolean{
        return PsiFileLanguage.getAreaDecider(language)?.isCommentArea(psiElement, isLineEnd)?:false
    }

    fun isCodeArea(language: Language, psiElement: LeafPsiElement, isLineEnd : Boolean) : Boolean {
        return PsiFileLanguage.getAreaDecider(language)?.isCodeArea(psiElement, isLineEnd)?:false
    }

}