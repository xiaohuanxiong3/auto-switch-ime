package com.friday.plugins.auto_switch_ime.areaDecide

import com.friday.plugins.auto_switch_ime.PsiElementLocation
import com.intellij.lang.Language
import com.intellij.psi.PsiElement
import org.jetbrains.kotlin.idea.KotlinLanguage

object KotlinAreaDecider : AreaDecider {

    override fun isCommentArea(psiElement: PsiElement, isLineEnd: Boolean): Boolean {
        return false
    }

    override fun isCodeArea(psiElement: PsiElement, isLineEnd: Boolean): Boolean {
        return false
    }

    override fun getPsiElementLocation(psiElement: PsiElement, isLineEnd: Boolean): PsiElementLocation {
        return PsiElementLocation()
    }

    override fun language(): Language {
        return KotlinLanguage.INSTANCE
    }

}