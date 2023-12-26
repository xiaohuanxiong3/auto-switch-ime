package com.sqy.plugins.copy

import com.intellij.codeInsight.editorActions.CopyPastePreProcessor
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.editor.RawText
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiFile
import com.intellij.psi.util.elementType

class CustomCopyPastePreProcessor : CopyPastePreProcessor {

    override fun preprocessOnCopy(p0: PsiFile?, p1: IntArray?, p2: IntArray?, p3: String?): String? {
        p0?.let {
            p3?.let {
                if (p1 != null && p1.size == 1 && p2 != null && p2.size == 1) {
                    val startElement = p0.findElementAt(p1[0])
                    val endElement = p0.findElementAt(if (p2[0]-1 > p1[0]) p2[0]-1 else p1[0])
                    if (startElement.elementType.toString().contains("COMMENT")
                        && endElement.elementType.toString().contains("COMMENT")) {
                        return p3.replace("/"," ").replace("*"," ")
                    }
                }
            }
        }
        return p3
    }

    override fun preprocessOnPaste(p0: Project?, p1: PsiFile?, p2: Editor?, p3: String?, p4: RawText?): String {
        if (p3 == null) {
            throw AssertionError("report null in preprocessOnPaste in CustomCopyPastePreProcessor");
        }
        return p3
    }
}