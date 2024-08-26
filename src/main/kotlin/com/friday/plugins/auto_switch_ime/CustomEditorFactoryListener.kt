package com.friday.plugins.auto_switch_ime

import com.friday.plugins.auto_switch_ime.areaDecide.PsiElementLocation
import com.friday.plugins.auto_switch_ime.service.AutoSwitchIMEService
import com.friday.plugins.auto_switch_ime.trigger.MouseClickedHandler
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.editor.event.*
import com.intellij.openapi.editor.ex.FocusChangeListener
import com.intellij.openapi.editor.impl.EditorImpl
import com.intellij.psi.PsiDocumentManager
import com.intellij.psi.PsiFile
import java.awt.event.InputMethodEvent
import java.awt.event.InputMethodListener
import java.util.concurrent.ConcurrentHashMap

class CustomEditorFactoryListener : EditorFactoryListener {

    private val caretListenerMap : ConcurrentHashMap<Editor,SwitchIMECaretListener> = MyMap.caretListenerMap
    private val editorPsiFileMap : ConcurrentHashMap<Editor,PsiFile> = MyMap.editorPsiFileMap
    private val psiFileMap : ConcurrentHashMap<PsiFile, Editor> = MyMap.psiFileEditorMap
    private val psiElementLocationMap : ConcurrentHashMap<Editor, PsiElementLocation> = MyMap.psiElementLocationMap
    private val documentListenerMap : ConcurrentHashMap<Editor, DocumentChangeCountListener> = MyMap.documentListenerMap

    // editor 是 文本编辑器
    override fun editorCreated(event: EditorFactoryEvent) {
        event.editor.project?.let {
            PsiDocumentManager.getInstance(it).getPsiFile(event.editor.document)
        }?.let { psiFile ->
            val caretListener = SwitchIMECaretListener()
            val documentListener = DocumentChangeCountListener()
            // 缓存 CaretListener
            caretListenerMap[event.editor] = caretListener
            // 缓存 Editor -> PsiFile 映射关系
            editorPsiFileMap[event.editor] = psiFile
            // 缓存 PsiFile -> Editor 映射关系
            psiFileMap[psiFile] = event.editor
            // 缓存 PsiElementLocation
            psiElementLocationMap[event.editor] = PsiElementLocation()
            // 缓存 DocumentListener
            documentListenerMap[event.editor] = documentListener
            // 添加自定义的 CaretListener
            event.editor.caretModel.addCaretListener(caretListener)
            // 添加自定义的 EditorMouseListener（EditorImpl 调用 release() 方法时会清空 myMouseListeners,故此处只需添加，不用管删除）
            event.editor.addEditorMouseListener(MouseClickedHandler())
            event.editor.document.addDocumentListener(documentListener)
            // 添加自定义的 FocusChangeListener（删除如上）
            event.editor.let {
                it as? EditorImpl
            }?.addFocusListener(FocusLostListener())
            // 添加自定义的 InputMethodListener（处理中文输入未确认时不会触发CharTyped事件问题）
            event.editor.contentComponent.addInputMethodListener(MyInputMethodListener(event.editor))
        }
    }

    override fun editorReleased(event: EditorFactoryEvent) {
        caretListenerMap[event.editor]
        // 删除 CaretListener 缓存
        caretListenerMap.remove(event.editor)
        // 删除 PsiFile -> Editor 缓存
        editorPsiFileMap[event.editor]?.let {
            psiFileMap.remove(it)
        }
        // 删除 Editor -> PsiFile 缓存
        editorPsiFileMap.remove(event.editor)
        // 删除 PsiElementLocation 缓存
        psiElementLocationMap.remove(event.editor)
        // 删除 DocumentListener 缓存
        documentListenerMap.remove(event.editor)
        // 删除自定义的 CaretListener
//        caretListener?.let {
//            event.editor.caretModel.removeCaretListener(it)
//        }
    }

    class DocumentChangeCountListener : DocumentListener {
        var numberOfChanges = 0

        override fun documentChanged(event: DocumentEvent) {
            numberOfChanges++
        }
    }

    class FocusLostListener : FocusChangeListener {

        override fun focusLost(editor: Editor) {
            // 编辑器失去焦点时重置PsiElementLocation
            MyMap.psiElementLocationMap[editor]?.reset()
        }
    }

    class SwitchIMECaretListener : CaretListener {

        var caretPositionChange : Int = 0

        override fun caretPositionChanged(event: CaretEvent) {
            caretPositionChange++
        }

    }

    class MyInputMethodListener(val editor: Editor) : InputMethodListener {

        // 假设 event.committedCharacterCount == 0 且 event.text != null 时，会取消选择已选择文本
        // 你要问我为什么这么喜欢假设，问就是不会
        override fun inputMethodTextChanged(event: InputMethodEvent?) {
            event?.let {
                if (event.committedCharacterCount == 0 && event.text != null && editor.selectionModel.hasSelection()) {
                    AutoSwitchIMEService.handleWhenSelectionUnSelected(editor)
                }
            }
        }

        override fun caretPositionChanged(event: InputMethodEvent?) {

        }

    }

}