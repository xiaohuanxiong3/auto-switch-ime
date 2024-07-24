package com.sqy.plugins.auto_switch_ime

import com.intellij.openapi.editor.Editor
import com.intellij.openapi.editor.ex.FocusChangeListener
import com.intellij.psi.PsiDocumentManager
import com.intellij.psi.PsiFile
import java.util.concurrent.ConcurrentHashMap

/**
 * 为 CustomEditorFactoryListener 的 editorCreated 做兜底操作
 * 因为我自己在用时会出现输入法切换不起作用，关闭并重新打开文件后就能解决问题的情况，原因暂时未知
 */
class FocusGainedListener : FocusChangeListener {

    private val caretListenerMap : ConcurrentHashMap<Editor, SwitchIMECaretListener> = EditorMap.caretListenerMap
    private val psiFileMap : ConcurrentHashMap<Editor, PsiFile> = EditorMap.psiFileMap

    override fun focusGained(editor: Editor) {
        if (!caretListenerMap.containsKey(editor) || !psiFileMap.containsKey(editor)) {
            editor.project?.let { project ->
                PsiDocumentManager.getInstance(project).getPsiFile(editor.document)
            }?.let { psiFile -> {
                val caretListener = SwitchIMECaretListener()
                // 缓存 CaretListener
                caretListenerMap[editor] = caretListener
                // 缓存 PsiFile
                psiFileMap[editor] = psiFile
            }}
        }
    }
}