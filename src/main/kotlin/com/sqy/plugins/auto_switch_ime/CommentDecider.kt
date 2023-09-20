package com.sqy.plugins.auto_switch_ime

import com.intellij.psi.PsiElement

/**
 * 注释判断器
 */
interface CommentDecider {

    /**
     * 判断是否是注释
     */
    fun isComment(psiElement: PsiElement) : Boolean
}