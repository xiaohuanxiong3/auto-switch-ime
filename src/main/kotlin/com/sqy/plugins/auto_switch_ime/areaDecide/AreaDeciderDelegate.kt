package com.sqy.plugins.auto_switch_ime.areaDecide

import com.intellij.lang.Language
import com.intellij.psi.impl.source.tree.LeafPsiElement

object AreaDeciderDelegate {

    fun isCommentArea(language: Language, psiElement: LeafPsiElement, isLineEnd : Boolean) : Boolean{
        return when(language) {
            PsiFileLanguage.JAVA.language -> {
                PsiFileLanguage.JAVA.areaDecider.isCommentArea(psiElement, isLineEnd)
            }

            PsiFileLanguage.KOTLIN.language -> {
                PsiFileLanguage.KOTLIN.areaDecider.isCommentArea(psiElement, isLineEnd)
            }

            else -> {
                false
            }
        }
    }

    fun isCodeArea(language: Language, psiElement: LeafPsiElement, isLineEnd : Boolean) : Boolean {
        return when(language) {
            PsiFileLanguage.JAVA.language -> {
                PsiFileLanguage.JAVA.areaDecider.isCodeArea(psiElement, isLineEnd)
            }

            PsiFileLanguage.KOTLIN.language -> {
                PsiFileLanguage.KOTLIN.areaDecider.isCodeArea(psiElement, isLineEnd)
            }

            else -> {
                false
            }
        }
    }
}