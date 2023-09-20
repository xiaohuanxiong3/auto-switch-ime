package com.sqy.plugins.auto_switch_ime

import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.diagnostic.Logger
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.editor.event.CaretListener
import com.intellij.openapi.editor.event.EditorFactoryEvent
import com.intellij.openapi.editor.event.EditorFactoryListener
import com.intellij.openapi.editor.ex.FocusChangeListener
import com.intellij.openapi.editor.impl.EditorImpl
import com.intellij.psi.PsiDocumentManager
import com.intellij.psi.PsiFile
import com.intellij.util.applyIf
import com.intellij.util.ui.UIUtil
import java.awt.event.FocusEvent
import java.awt.event.KeyAdapter
import java.awt.event.KeyEvent
import java.util.concurrent.ConcurrentHashMap

class CustomEditorFactoryListener : EditorFactoryListener {

    private val caretListenerMap : ConcurrentHashMap<Editor,SwitchIMECaretListener> = EditorMap.caretListenerMap
    private val psiFileMap : ConcurrentHashMap<Editor,PsiFile> = EditorMap.psiFileMap

    override fun editorCreated(event: EditorFactoryEvent) {
        val caretListener = SwitchIMECaretListener()
        // 缓存 CaretListener
        caretListenerMap[event.editor] = caretListener
        // 缓存 PsiFile
        event.editor.project?.let {
            PsiDocumentManager.getInstance(it).getPsiFile(event.editor.document)
        }?.let {
            psiFileMap[event.editor] = it
        }
        // 添加自定义的 CaretListener
        event.editor.caretModel.addCaretListener(caretListener)
        // 添加自定义的 EditorMouseListener
        event.editor.addEditorMouseListener(CustomEditorMouseListener())
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

    class CustomFocusChangeListener(private val map: ConcurrentHashMap<Editor,CaretListener>)
        : FocusChangeListener {

        // 获取全局的切换输入法定时任务服务
//        private val switchIMEService : SwitchIMEService =
//                ApplicationManager.getApplication().getService(SwitchIMEService::class.java)

        override fun focusGained(editor: Editor, event: FocusEvent) {
            // println("focusGained:" + editor.hashCode() + "----cause:" + event.cause)
            val caretListener = map[editor]
//            caretListener?.let {
//                switchIMEService.setCaretListener(it)
//            }
            super.focusGained(editor, event)
        }

        override fun focusLost(editor: Editor, event: FocusEvent) {
            // println("focusLost:" + editor.hashCode() + "----cause:" + event.cause)
            // switchIMEService.resetCaretListener()
            super.focusLost(editor, event)
        }
    }

}