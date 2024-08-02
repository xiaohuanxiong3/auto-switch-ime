package com.sqy.plugins.auto_switch_ime.psi

import com.intellij.openapi.editor.Editor
import com.intellij.psi.PsiLiteralExpression
import com.intellij.psi.impl.source.tree.ElementType
import com.intellij.psi.impl.source.tree.java.PsiLiteralExpressionImpl
import com.sqy.plugins.auto_switch_ime.Map
import com.sqy.plugins.auto_switch_ime.PsiElementLocation
import java.util.concurrent.ConcurrentHashMap

object JavaPsiTreeChangedHandler : PsiTreeChangedHandler{

    private val psiElementLocationMap : ConcurrentHashMap<Editor, PsiElementLocation> = Map.psiElementLocationMap

    override fun handleStringLiteralExpressionAddition(editor: Editor, literalExpression: PsiLiteralExpression) {
        literalExpression.let {
            it as? PsiLiteralExpressionImpl
        }?.let { literal ->
            if (ElementType.STRING_LITERALS.contains(literal.literalElementType)) {
                psiElementLocationMap[editor]!!.setLocationId(literalExpression)
                psiElementLocationMap[editor]!!.isSecondLanguageEnabled = true
                psiElementLocationMap[editor]!!.isCommentArea = false
            }
        }
    }

}