package com.friday.plugins.auto_switch_ime.language.kotlin

import com.friday.plugins.auto_switch_ime.areaDecide.AreaDecider
import com.friday.plugins.auto_switch_ime.areaDecide.PsiElementLocation
import com.intellij.lang.Language
import com.intellij.psi.PsiDocCommentBase
import com.intellij.psi.PsiElement
import com.intellij.psi.util.PsiTreeUtil
import org.jetbrains.kotlin.idea.KotlinLanguage
import org.jetbrains.kotlin.psi.KtLiteralStringTemplateEntry
import org.jetbrains.kotlin.psi.KtStringTemplateExpression

object KotlinAreaDecider : AreaDecider {
    private const val LINE_COMMENT_NAME = "PsiComment(EOL_COMMENT)"

    override fun getPsiElementLocation(psiElement: PsiElement, editorOffset: Int, isLineEnd: Boolean): PsiElementLocation {
        val psiElementLocation = PsiElementLocation()
        // 单行注释区
        if (psiElement.toString() == LINE_COMMENT_NAME && editorOffset != psiElement.textOffset ) {
            psiElementLocation.setLocationId(psiElement.prevSibling,psiElement.nextSibling)
            psiElementLocation.isSecondLanguageEnabled = true
            psiElementLocation.doSwitchWhenFirstInThisLocation = true
            psiElementLocation.switchToSecondLanguageWhenFirstInThisLocation = true
            return psiElementLocation
        }
        // 单行注释区末尾
        if (isLineEnd && psiElement.prevSibling != null &&
            (psiElement.prevSibling.toString() == LINE_COMMENT_NAME
                    || (psiElement.prevSibling.lastChild != null && psiElement.prevSibling.lastChild.toString() == LINE_COMMENT_NAME))) {
            psiElementLocation.setLocationId(psiElement.prevSibling.prevSibling,psiElement)
            psiElementLocation.isSecondLanguageEnabled = true
            psiElementLocation.doSwitchWhenFirstInThisLocation = true
            psiElementLocation.switchToSecondLanguageWhenFirstInThisLocation = true
            return psiElementLocation
        }
        // 文档注释区
        val psiDocComment = PsiTreeUtil.getParentOfType(psiElement, PsiDocCommentBase::class.java)
        if (psiDocComment != null && editorOffset != psiDocComment.textOffset) {
            psiElementLocation.setLocationId(psiDocComment)
            psiElementLocation.isSecondLanguageEnabled = true
            psiElementLocation.doSwitchWhenFirstInThisLocation = true
            psiElementLocation.switchToSecondLanguageWhenFirstInThisLocation = true
            return psiElementLocation
        }
        // 字符串模板区域中的字符串字面量区域
        val ktLiteralStringTemplateEntry : KtLiteralStringTemplateEntry?
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
        psiElementLocation.inOtherLocation()
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