package com.sqy.plugins.copy

import com.intellij.codeInsight.editorActions.CopyPastePreProcessor
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.editor.RawText
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiFile

class CustomCopyPastePreProcessor : CopyPastePreProcessor {

    override fun preprocessOnCopy(p0: PsiFile?, p1: IntArray?, p2: IntArray?, p3: String?): String? {
        return p3?.replace("/"," ")?.replace("*"," ")
    }

    override fun preprocessOnPaste(p0: Project?, p1: PsiFile?, p2: Editor?, p3: String?, p4: RawText?): String {
        if (p3 == null) {
            throw AssertionError("report null in preprocessOnPaste in CustomCopyPastePreProcessor");
        }
        return p3
    }
}