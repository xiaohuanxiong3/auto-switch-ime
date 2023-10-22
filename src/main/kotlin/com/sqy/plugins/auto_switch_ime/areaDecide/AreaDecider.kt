package com.sqy.plugins.auto_switch_ime.areaDecide

import com.intellij.lang.Language
import com.intellij.psi.impl.source.tree.LeafPsiElement

/**
 * 区域判断器
 */
interface AreaDecider {

    /**
     * 判断是否是注释区
     */
    fun isCommentArea(psiElement: LeafPsiElement, isLineEnd : Boolean) : Boolean

    /**
     * 判断是否是代码区
     */
    fun isCodeArea(psiElement: LeafPsiElement, isLineEnd : Boolean) : Boolean

    /**
     * 语言
     */
    fun language() : Language

}