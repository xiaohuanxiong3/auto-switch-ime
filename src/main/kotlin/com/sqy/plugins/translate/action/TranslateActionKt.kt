package com.sqy.plugins.translate.action

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.CommonDataKeys
import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.diagnostic.Logger
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.editor.RangeMarker
import com.intellij.openapi.editor.ScrollType
import com.intellij.openapi.editor.markup.*
import com.intellij.openapi.util.Disposer
import com.intellij.openapi.util.TextRange
import com.intellij.ui.JBColor
import com.sqy.plugins.translate.service.TranslationUIManager
import com.sqy.plugins.translate.ui.BalloonPositionTracker

class TranslateActionKt : AnAction() {

    private val logger = Logger.getInstance(TranslateActionKt::class.java)

    override fun actionPerformed(e: AnActionEvent) {
        // TODO: insert action logic here
        if (ApplicationManager.getApplication().isHeadlessEnvironment) {
            return
        }

        val project = e.project
        val editor = e.getData(CommonDataKeys.EDITOR)?:return

        val markupModel = editor.markupModel
        val selectionModel = editor.selectionModel
        val isColumnSelectionMode = editor.caretModel.caretCount > 1
        val selectionRange = TextRange(selectionModel.selectionStart,selectionModel.selectionEnd)

        val starts: IntArray = selectionModel.blockSelectionStarts
        val ends: IntArray = selectionModel.blockSelectionEnds
        val text: String = selectionModel.getSelectedText(true).toString()
                .replace(Regex("\\s+")," ").trim()

        val startLine by lazy { editor.offsetToVisualPosition(selectionRange.startOffset).line }
        val endLine by lazy { editor.offsetToVisualPosition(selectionRange.endOffset).line }
        val highlightAttributes = if (startLine == endLine) {
            HIGHLIGHT_ATTRIBUTES
        } else {
            MULTILINE_HIGHLIGHT_ATTRIBUTES
        }

        val highlighters = ArrayList<RangeHighlighter>(starts.size)

        try {
            for (i in starts.indices) {
                highlighters += markupModel.addRangeHighlighter(
                        starts[i],
                        ends[i],
                        HighlighterLayer.SELECTION - 1,
                        highlightAttributes,
                        HighlighterTargetArea.EXACT_RANGE
                )
            }

            editor.scrollingModel.scrollToCaret(ScrollType.MAKE_VISIBLE)

            val caretRangeMarker = editor.createCaretRangeMarker(selectionRange)
            val tracker = BalloonPositionTracker(editor, caretRangeMarker)
            val balloon = TranslationUIManager.showBalloon(editor, text, tracker)

            if (balloon.disposed) {
                markupModel.removeHighlighters(highlighters)
            } else {
                Disposer.register(balloon) { markupModel.removeHighlighters(highlighters) }
            }
        } catch (thr: Throwable) {
            thr.printStackTrace()
            markupModel.removeHighlighters(highlighters)
        }
        // BalloonPopupBuilderImpl(ContainerUtil.createWeakMap(),editor)
    }

    fun Editor.createCaretRangeMarker(selectionRange: TextRange): RangeMarker {
        return document
                .createRangeMarker(selectionRange)
                .apply {
                    isGreedyToLeft = true
                    isGreedyToRight = true
                }
    }


    override fun update(e: AnActionEvent) {
        val project = e.project
        val editor = e.getData(CommonDataKeys.EDITOR)
        var menuAllowed = false
        if (project != null && editor != null) {
            menuAllowed = editor.caretModel.currentCaret.hasSelection()
        }
        // 只有在存在project 和 editor时 并且选择了文本 才提供翻译选项
        e.presentation.isEnabledAndVisible = menuAllowed
    }

    private companion object {
        val EFFECT_COLOR = JBColor(0xFFEE6000.toInt(), 0xFFCC7832.toInt())

        val HIGHLIGHT_ATTRIBUTES: TextAttributes = TextAttributes().apply {
            effectType = EffectType.LINE_UNDERSCORE
            effectColor = EFFECT_COLOR
        }

        val MULTILINE_HIGHLIGHT_ATTRIBUTES: TextAttributes = TextAttributes().apply {
            effectType = EffectType.BOXED
            effectColor = EFFECT_COLOR
        }

        private fun MarkupModel.removeHighlighters(highlighters: Collection<RangeHighlighter>) {
            for (highlighter in highlighters) {
                removeHighlighter(highlighter)
                highlighter.dispose()
            }
        }
    }
}