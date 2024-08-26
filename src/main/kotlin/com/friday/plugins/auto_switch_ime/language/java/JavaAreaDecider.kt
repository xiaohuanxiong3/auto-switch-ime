package com.friday.plugins.auto_switch_ime.language.java

import com.friday.plugins.auto_switch_ime.areaDecide.AreaDecider
import com.friday.plugins.auto_switch_ime.areaDecide.PsiElementLocation
import com.intellij.lang.Language
import com.intellij.lang.java.JavaLanguage
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiLiteralExpression
import com.intellij.psi.impl.source.tree.ElementType
import com.intellij.psi.impl.source.tree.java.PsiLiteralExpressionImpl
import com.intellij.psi.javadoc.PsiDocComment

object JavaAreaDecider : AreaDecider {

    override fun getPsiElementLocation(psiElement: PsiElement, editorOffset: Int, isLineEnd: Boolean): PsiElementLocation {
        val psiElementLocation = PsiElementLocation()
        // 单行注释区
        if (psiElement.toString() == "PsiComment(END_OF_LINE_COMMENT)" && editorOffset != psiElement.textOffset) {
            psiElementLocation.setLocationId(psiElement.prevSibling,psiElement.nextSibling)
            psiElementLocation.isSecondLanguageEnabled = true
            psiElementLocation.doSwitchWhenFirstInThisLocation = true
            psiElementLocation.switchToSecondLanguageWhenFirstInThisLocation = true
            return psiElementLocation
        }
        // 单行注释区末尾
        if (isLineEnd && psiElement.prevSibling != null && psiElement.prevSibling.toString() == "PsiComment(END_OF_LINE_COMMENT)") {
            psiElementLocation.setLocationId(psiElement.prevSibling.prevSibling,psiElement)
            psiElementLocation.isSecondLanguageEnabled = true
            psiElementLocation.doSwitchWhenFirstInThisLocation = true
            psiElementLocation.switchToSecondLanguageWhenFirstInThisLocation = true
            return psiElementLocation
        }
        // 文档注释区
        if (psiElement.parent != null) {
            if (psiElement.parent is PsiDocComment && editorOffset != psiElement.parent.textOffset) {
                psiElementLocation.setLocationId(psiElement.parent)
                psiElementLocation.isSecondLanguageEnabled = true
                psiElementLocation.doSwitchWhenFirstInThisLocation = true
                psiElementLocation.switchToSecondLanguageWhenFirstInThisLocation = true
                return psiElementLocation
            } else if (psiElement.parent.parent != null) {
                if (psiElement.parent.parent is PsiDocComment && editorOffset != psiElement.parent.parent.textOffset) {
                    psiElementLocation.setLocationId(psiElement.parent.parent)
                    psiElementLocation.isSecondLanguageEnabled = true
                    psiElementLocation.doSwitchWhenFirstInThisLocation = true
                    psiElementLocation.switchToSecondLanguageWhenFirstInThisLocation = true
                    return psiElementLocation
                } else if (psiElement.parent.parent.parent != null && psiElement.parent.parent.parent is PsiDocComment && editorOffset != psiElement.parent.parent.parent.textOffset) {
                    psiElementLocation.setLocationId(psiElement.parent.parent.parent)
                    psiElementLocation.isSecondLanguageEnabled = true
                    psiElementLocation.doSwitchWhenFirstInThisLocation = true
                    psiElementLocation.switchToSecondLanguageWhenFirstInThisLocation = true
                    return psiElementLocation
                }
            }
        }
        // 字符串字面量区域
        if (psiElement.parent != null && psiElement.parent is PsiLiteralExpression) {
            psiElement.parent.let {
                it as? PsiLiteralExpressionImpl
            }?.let {
                if (ElementType.STRING_LITERALS.contains(it.literalElementType)) {
                    psiElementLocation.setLocationId(psiElement.parent)
                    psiElementLocation.isSecondLanguageEnabled = true
                    psiElementLocation.doSwitchWhenFirstInThisLocation = false
                    psiElementLocation.switchToSecondLanguageWhenFirstInThisLocation = false
                    return psiElementLocation
                }
            }
        }
        // 其他区域
        psiElementLocation.inOtherLocation()
        return psiElementLocation
    }

    override fun language(): Language {
        return JavaLanguage.INSTANCE
    }
}