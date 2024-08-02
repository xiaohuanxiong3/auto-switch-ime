package com.sqy.plugins.auto_switch_ime.psi

import com.intellij.psi.impl.PsiTreeChangeEventImpl
import com.intellij.psi.impl.PsiTreeChangePreprocessor

class CustomPsiTreeChangePreprocessorListener : PsiTreeChangePreprocessor {

    override fun treeChanged(p0: PsiTreeChangeEventImpl) {
//         println(p0)
    }

}