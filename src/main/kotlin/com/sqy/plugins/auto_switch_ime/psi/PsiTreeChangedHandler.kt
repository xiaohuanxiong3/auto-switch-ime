package com.sqy.plugins.auto_switch_ime.psi

import com.intellij.openapi.editor.Editor
import com.intellij.psi.PsiLiteralExpression

@Deprecated("似乎用不上了")
interface PsiTreeChangedHandler {

    /**
     * 处理字符串字面量的创建
     */
    fun handleStringLiteralExpressionAddition(editor: Editor, literalExpression: PsiLiteralExpression)

}