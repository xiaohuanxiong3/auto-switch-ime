package com.friday.plugins.auto_switch_ime.areaDecide

import com.friday.plugins.auto_switch_ime.PsiElementLocation
import com.intellij.lang.Language
import com.intellij.psi.PsiDocCommentBase
import com.intellij.psi.PsiElement
import com.intellij.psi.util.PsiTreeUtil
import org.jetbrains.kotlin.idea.KotlinLanguage
import org.jetbrains.kotlin.psi.KtLiteralStringTemplateEntry
import org.jetbrains.kotlin.psi.KtStringTemplateExpression

object KotlinAreaDecider : AreaDecider {
    private const val LINE_COMMENT_NAME = "PsiComment(EOL_COMMENT)"

    override fun isCommentArea(psiElement: PsiElement, isLineEnd: Boolean): Boolean {
        return false
    }

    override fun isCodeArea(psiElement: PsiElement, isLineEnd: Boolean): Boolean {
        return false
    }

    override fun getPsiElementLocation(psiElement: PsiElement, isLineEnd: Boolean): PsiElementLocation {
        val psiElementLocation = PsiElementLocation()
        // 单行注释区
        if (psiElement.toString() == LINE_COMMENT_NAME) {
            psiElementLocation.setLocationId(psiElement.prevSibling,psiElement.nextSibling)
            psiElementLocation.isSecondLanguageEnabled = true
            psiElementLocation.doSwitchWhenFirstInThisLocation = true
            psiElementLocation.switchToSecondLanguageWhenFirstInThisLocation = true
            return psiElementLocation
        }
        // 单行注释区末尾
        if (isLineEnd && psiElement.prevSibling != null && psiElement.prevSibling.toString() == LINE_COMMENT_NAME) {
            psiElementLocation.setLocationId(psiElement.prevSibling.prevSibling,psiElement)
            psiElementLocation.isSecondLanguageEnabled = true
            psiElementLocation.doSwitchWhenFirstInThisLocation = true
            psiElementLocation.switchToSecondLanguageWhenFirstInThisLocation = true
            return psiElementLocation
        }
        // 文档注释区
        val psiDocComment = PsiTreeUtil.getParentOfType(psiElement, PsiDocCommentBase::class.java)
        if (psiDocComment != null) {
            psiElementLocation.setLocationId(psiDocComment)
            psiElementLocation.isSecondLanguageEnabled = true
            psiElementLocation.doSwitchWhenFirstInThisLocation = true
            psiElementLocation.switchToSecondLanguageWhenFirstInThisLocation = true
            return psiElementLocation
        }
        // 字符串模板区域中的字符串字面量区域
        var ktLiteralStringTemplateEntry : KtLiteralStringTemplateEntry?
        if (psiElement.toString() == "PsiElement(CLOSING_QUOTE)" && psiElement.text == "\"") {
            ktLiteralStringTemplateEntry = getParentOfType(psiElement.prevSibling, KtLiteralStringTemplateEntry::class.java)
        } else {
            ktLiteralStringTemplateEntry = getParentOfType(psiElement, KtLiteralStringTemplateEntry::class.java)
        }
//        ktLiteralStringTemplateEntry = getParentOfType(psiElement, KtLiteralStringTemplateEntry::class.java)
        if (ktLiteralStringTemplateEntry != null) {
            psiElementLocation.setLocationId(PsiTreeUtil.getParentOfType(ktLiteralStringTemplateEntry, KtStringTemplateExpression::class.java)!!)
            psiElementLocation.isSecondLanguageEnabled = true
            psiElementLocation.doSwitchWhenFirstInThisLocation = false
            psiElementLocation.switchToSecondLanguageWhenFirstInThisLocation = false
            return psiElementLocation
        }
        // 其他区域
        psiElementLocation.inStrictCodeLocation()
        return psiElementLocation
    }

    override fun language(): Language {
        return KotlinLanguage.INSTANCE
    }

    private fun <T : PsiElement> getParentOfType(element: PsiElement, parent: Class<T> ) : T?{
        if (parent.isInstance(element)) {
            return parent.cast(element)
        }
        return PsiTreeUtil.getParentOfType(element, parent)
    }

}