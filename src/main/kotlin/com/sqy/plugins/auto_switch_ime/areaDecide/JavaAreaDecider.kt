package com.sqy.plugins.auto_switch_ime.areaDecide

import com.intellij.lang.Language
import com.intellij.lang.java.JavaLanguage
import com.intellij.psi.PsiComment
import com.intellij.psi.PsiElement
import com.intellij.psi.impl.source.tree.LeafPsiElement
import com.intellij.psi.javadoc.PsiDocTag

object JavaAreaDecider : AreaDecider {

    override fun isCommentArea(psiElement: PsiElement, isLineEnd: Boolean): Boolean {
        psiElement.let {
            it as? LeafPsiElement
        }?.let {
            return isCommentArea(it,isLineEnd)
        }
        // 此处理论上不会到达
        return false
    }

    fun isCommentArea(psiElement: LeafPsiElement, isLineEnd: Boolean): Boolean {
        // 单行注释区
        if (psiElement is PsiComment) {
            return true
        }
        // 文档注释区
        if (psiElement.toString() == "PsiDocToken:DOC_COMMENT_DATA") {
            return true
        }
        if (psiElement.treePrev == null) {
            return false
        }
        // 单行注释区末尾
        if (isLineEnd && psiElement.treePrev is PsiComment) {
            return true
        }
        // 文档注释区行尾
        if (isLineEnd && psiElement.treePrev.toString() == "PsiDocToken:DOC_COMMENT_DATA") {
            return true
        }
        // 文档区，行尾，前面是类似 @param xxx
        if (isLineEnd && (psiElement.treePrev is PsiDocTag
                    || psiElement.treePrev.treeParent is PsiDocTag)) {
            return true
        }
        if (psiElement.treeNext == null) {
            return false
        }
        // 文档注释区行首
        if (psiElement.treePrev.toString() == "PsiDocToken:DOC_COMMENT_LEADING_ASTERISKS"
            && (psiElement.treeNext.toString() == "PsiDocToken:DOC_COMMENT_LEADING_ASTERISKS"
                        || psiElement.treeNext.toString() == "PsiDocToken:DOC_COMMENT_END")) {
            return true
        }
        return false
    }

    override fun isCodeArea(psiElement: PsiElement, isLineEnd: Boolean): Boolean {
        psiElement.let {
            it as? LeafPsiElement
        }?.let {
            return isCodeArea(it,isLineEnd)
        }
        // 此处应该永远不会到达
        throw Error("插件内部错误!")
    }

    fun isCodeArea(psiElement: LeafPsiElement, isLineEnd: Boolean): Boolean {
//        if (psiElement is PsiIdentifier
//            || psiElement.treeParent is PsiExpression
//            || psiElement.treeParent is PsiExpressionList
//            || psiElement.treeParent is PsiStatement
//            || (!isLineEnd && psiElement is PsiWhiteSpace)
//            || isLineEnd && psiElement.treePrev != null
//            && (psiElement.treePrev is PsiExpression
//                    || psiElement.treePrev is PsiStatement
//                    || psiElement.treePrev is Constants
//                    || psiElement.treePrev is PsiKeyword
//                    || psiElement.treePrev is PsiErrorElement)) {
//            return true
//        }
//        return false
        return true
    }

    override fun language(): Language {
        return JavaLanguage.INSTANCE
    }
}