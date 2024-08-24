package com.friday.plugins.auto_switch_ime.areaDecide

import com.friday.plugins.auto_switch_ime.PsiElementLocation
import com.intellij.lang.Language
import com.intellij.psi.PsiElement

/**
 * 区域判断器
 */
interface AreaDecider {

    /**
     * 根据PsiElement 获取其对应的 PsiElementLocation
     */
    fun getPsiElementLocation(psiElement: PsiElement, editorOffset: Int, isLineEnd: Boolean): PsiElementLocation

    /**
     * 语言
     */
    fun language() : Language

}