package com.sqy.plugins.auto_switch_ime

import com.intellij.openapi.editor.Editor
import com.intellij.openapi.editor.event.EditorFactoryEvent
import com.intellij.openapi.editor.event.EditorFactoryListener
import com.intellij.psi.PsiDocumentManager
import com.intellij.psi.PsiFile
import com.sqy.plugins.auto_switch_ime.cause.MouseClickedHandler
import java.util.concurrent.ConcurrentHashMap

class CustomEditorFactoryListener : EditorFactoryListener {

    private val caretListenerMap : ConcurrentHashMap<Editor,SwitchIMECaretListener> = EditorMap.caretListenerMap
    private val psiFileMap : ConcurrentHashMap<Editor,PsiFile> = EditorMap.psiFileMap

    override fun editorCreated(event: EditorFactoryEvent) {
        event.editor.project?.let {
            PsiDocumentManager.getInstance(it).getPsiFile(event.editor.document)
        }?.let { psiFile ->
            val caretListener = SwitchIMECaretListener()
            // 缓存 CaretListener
            caretListenerMap[event.editor] = caretListener
            // 缓存 PsiFile
            psiFileMap[event.editor] = psiFile
            // 添加自定义的 CaretListener
            event.editor.caretModel.addCaretListener(caretListener)
            // 添加自定义的 EditorMouseListener（EditorImpl 调用 release() 方法时会清空 myMouseListeners,故此处只需添加，不用管删除）
            event.editor.addEditorMouseListener(MouseClickedHandler())
            // 添加自定义的 FocusChangeListener（删除如上）
//            event.editor.let {
//                it as? EditorImpl
//            }?.addFocusListener(FocusGainedListener())
        }
    }

    override fun editorReleased(event: EditorFactoryEvent) {
        val caretListener = caretListenerMap[event.editor]
        // 删除 CaretListener 缓存
        caretListenerMap.remove(event.editor)
        // 删除 PsiFile 缓存
        psiFileMap.remove(event.editor)
        // 删除自定义的 CaretListener
        caretListener?.let {
            event.editor.caretModel.removeCaretListener(it)
        }
    }

}