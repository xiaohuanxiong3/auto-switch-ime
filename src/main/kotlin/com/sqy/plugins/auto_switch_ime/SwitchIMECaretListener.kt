package com.sqy.plugins.auto_switch_ime

import com.intellij.openapi.diagnostic.Logger
import com.intellij.openapi.editor.actionSystem.EditorActionHandler
import com.intellij.openapi.editor.event.CaretEvent
import com.intellij.openapi.editor.event.CaretListener
import com.intellij.openapi.editor.impl.EditorImpl


class SwitchIMECaretListener : CaretListener {

    private val logger : Logger = Logger.getInstance(SwitchIMECaretListener::class.java)

    var offset : Int? = 0

    var caretPositionChangeCause : CaretPositionChangeCause? = null

    var caretPositionChange : Int = 0

    override fun caretPositionChanged(event: CaretEvent) {
        caretPositionChange++
//        when(caretPositionChangeCause) {
//            CaretPositionChangeCause.TYPED -> {
//                println("caretPositionChanged caused by keyTyped")
//            }
//            CaretPositionChangeCause.MOUSE_PRESSED -> {
//                println("caretPositionChanged caused by mouse-press")
//            }
//            CaretPositionChangeCause.ONE_CARET_MOVE -> {
//                println("caretPositionChanged caused by caret-move one step")
//            }
//            else -> println("caretPositionChanged caused by other action")
//        }
        println("caretPositionChanged")
        // offset = event.caret?.offset

//        event.editor.project?.let {
//            val psiFile = PsiDocumentManagerImpl(it).getPsiFile(event.editor.document)
//            val psiElement = psiFile?.findElementAt(offset!!)
//        }
//        logger.info("fuck")
    }


}