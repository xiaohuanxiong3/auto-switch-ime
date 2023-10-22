package com.sqy.plugins.auto_switch_ime

import com.intellij.codeInsight.editorActions.TypedHandlerDelegate
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.fileTypes.FileType
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiFile

class CustomTypedDelegate : TypedHandlerDelegate() {

    override fun beforeCharTyped(c: Char, project: Project, editor: Editor, file: PsiFile, fileType: FileType): Result {
        if (c == '/') {
            AutoSwitchIMEService.prepare(editor)
        }
        return Result.CONTINUE
    }

    override fun charTyped(c: Char, project: Project, editor: Editor, file: PsiFile): Result {
        if (c == '/') {
            AutoSwitchIMEService.handle(editor,CaretPositionChangeCause.TYPED)
        }
        return Result.CONTINUE
    }
}