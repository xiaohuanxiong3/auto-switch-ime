package com.sqy.plugins.auto_switch_ime.areaDecide

import com.intellij.lang.Language
import com.intellij.psi.impl.source.tree.LeafPsiElement
import com.sqy.plugins.auto_switch_ime.areaDecide.AreaDecider
import org.jetbrains.kotlin.idea.KotlinLanguage

object KotlinAreaDecider : AreaDecider {

    override fun isCommentArea(psiElement: LeafPsiElement, isLineEnd: Boolean): Boolean {
        return false
    }

    override fun isCodeArea(psiElement: LeafPsiElement, isLineEnd: Boolean): Boolean {
        return false
    }

    override fun language(): Language {
        return KotlinLanguage.INSTANCE
    }


}