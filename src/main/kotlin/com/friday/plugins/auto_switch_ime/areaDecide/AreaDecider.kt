package com.friday.plugins.auto_switch_ime.areaDecide

import com.friday.plugins.auto_switch_ime.PsiElementLocation
import com.intellij.lang.Language
import com.intellij.psi.PsiElement

/**
 * 区域判断器
 */
interface AreaDecider {

    /**
     * 判断是否是注释区
     */
    fun isCommentArea(psiElement: PsiElement, isLineEnd : Boolean) : Boolean

    /**
     * 判断是否是代码区
     */
    fun isCodeArea(psiElement: PsiElement, isLineEnd : Boolean) : Boolean

    /**
     * 根据PsiElement 获取其对应的 PsiElementLocation
     */
    fun getPsiElementLocation(psiElement: PsiElement, isLineEnd: Boolean): PsiElementLocation

    /**
     * 语言
     */
    fun language() : Language

}