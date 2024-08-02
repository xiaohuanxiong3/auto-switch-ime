package com.sqy.plugins.auto_switch_ime.psi

import com.intellij.openapi.editor.Editor
import com.intellij.psi.PsiLiteralExpression

object KotlinPsiTreeChangedHandler : PsiTreeChangedHandler {

    override fun handleStringLiteralExpressionAddition(editor: Editor, literalExpression: PsiLiteralExpression) {

    }

}