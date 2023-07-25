package com.sqy.plugins.translation.ui

import com.intellij.icons.AllIcons
import com.intellij.openapi.Disposable
import com.intellij.openapi.ide.CopyPasteManager
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.JBMenuItem
import com.intellij.openapi.ui.JBPopupMenu
import com.intellij.openapi.ui.VerticalFlowLayout
import com.intellij.ui.JBColor
import com.intellij.ui.PopupMenuListenerAdapter
import com.intellij.ui.components.panels.NonOpaquePanel
import com.intellij.util.ui.JBUI
import com.intellij.util.ui.components.BorderLayoutPanel
import com.sqy.plugins.translation.TranslationResult
import com.sqy.plugins.translation.ui.UI.disabled
import com.sqy.plugins.translation.ui.util.ScrollSynchronizer
import java.awt.*
import java.awt.datatransfer.StringSelection
import java.awt.event.FocusAdapter
import java.awt.event.FocusEvent
import javax.swing.*
import javax.swing.event.PopupMenuEvent
import kotlin.properties.Delegates

class TranslationPane(
    private val project: Project?,
    private val maxWidth: Int
) : NonOpaquePanel(VerticalFlowLayout(JBUI.scale(GAP), JBUI.scale(GAP))), Disposable {

    private val originalViewer = Viewer()
    private val translationViewer = Viewer()

    private lateinit var originalComponent: JComponent
    private lateinit var translationComponent: JComponent

    @Suppress("SpellCheckingInspection")
    private var onRevalidateHandler: (() -> Unit)? = null

    private val sourceLanguageLabel = JLabel("auto")

    private val sourceRowComponent = flow(
            JLabel("源语言: ").apply {
                setFont(this)
            },
            sourceLanguageLabel.apply {
                setFont(this)
            }
    )

    private val targetLanguageLabel = JLabel("auto")

    private val targetRowComponent = flow(
            JLabel("目标语言: ").apply {
                setFont(this)
            },
            targetLanguageLabel.apply {
                setFont(this)
            }
    )

    private fun setFont(component : JComponent) =
            UI.getFonts(FONT_SIZE_DEFAULT, FONT_SIZE_PHONETIC).let {
                (primaryFont, phoneticFont) ->
                   component.font = primaryFont
            }

    var translationResult : TranslationResult ?
        by Delegates.observable(null) { _, oldValue:TranslationResult?, newValue:TranslationResult? ->
            if (newValue !== oldValue) {
                update()
            }
        }

    init {
        init()
    }

    private fun init() {
        originalComponent = onWrapViewer(originalViewer)
        translationComponent = onWrapViewer(translationViewer)

        if (originalComponent is JScrollPane && translationComponent is JScrollPane) {
            ScrollSynchronizer.syncScroll(
                (originalComponent as JScrollPane).verticalScrollBar,
                (translationComponent as JScrollPane).verticalScrollBar
            )
        }

        onRowCreated(sourceRowComponent)
        onRowCreated(originalComponent)
        onRowCreated(targetRowComponent)
        onRowCreated(translationComponent)

        add(sourceRowComponent)
        add(originalComponent)

        add(targetRowComponent)
        add(translationComponent)

        initColorScheme()
        initActions()

        minimumSize = Dimension(0, 0)

        border = JBUI.Borders.empty(OFFSET, OFFSET, OFFSET, -GAP)
        maximumSize = Dimension(maxWidth, Int.MAX_VALUE)
    }

    protected open fun onRowCreated(row: JComponent) {
        if (row !is ScrollPane) {
            val border = row.border
            val toMerge = JBUI.Borders.emptyRight(OFFSET + GAP)
            row.border = if (border != null) JBUI.Borders.merge(border, toMerge, false) else toMerge
        }
    }

//    protected abstract fun onCreateLanguageComponent(): T

    protected open fun onWrapViewer(viewer: Viewer): JComponent {
        val maxHeight = if (isOriginalOrTranslationViewer(viewer)) MAX_VIEWER_SMALL else MAX_VIEWER_HEIGHT
        val scrollPane = object : ScrollPane(viewer) {
            override fun getPreferredSize(): Dimension {
                val preferredSize = super.getPreferredSize()
                if (preferredSize.height > maxHeight) {
                    return Dimension(preferredSize.width, maxHeight)
                }
                return preferredSize
            }
        }

        viewer.border = JBUI.Borders.emptyRight(OFFSET + GAP)

//        if (isDictViewer(viewer)) {
//            dictViewerScrollWrapper = scrollPane
//        }

        return scrollPane
    }

    protected fun isOriginalOrTranslationViewer(viewer: Viewer): Boolean {
        return viewer === originalViewer || viewer === translationViewer
    }


    private fun initColorScheme() {
        originalViewer.foreground = JBColor(0xEE6000, 0xCC7832)
        translationViewer.foreground = JBColor(0x170591, 0xFFC66D)
    }

    private fun initActions() {
        originalViewer.setupPopupMenu()
        originalViewer.setFocusListener(translationViewer)
        translationViewer.setupPopupMenu()
        translationViewer.setFocusListener(originalViewer)
    }

    protected open fun onBeforeFoldingExpand() {}

    protected open fun onFoldingExpanded() {}

    private fun Viewer.setFocusListener(vararg vs: Viewer) {
        addFocusListener(object : FocusAdapter() {
            override fun focusGained(e: FocusEvent?) {
                for (v in vs) {
                    v.select(0, 0)
                }
            }
        })
    }

    override fun dispose() {
        reset()
    }

    open fun reset() {
        translationResult = null
    }

    @Suppress("SpellCheckingInspection")
    fun onRevalidate(handler: () -> Unit) {
        onRevalidateHandler = handler
    }

    private fun JTextPane.setupPopupMenu() {
        componentPopupMenu = JBPopupMenu().apply {
            val copy = JBMenuItem("复制", AllIcons.Actions.Copy).apply {
                disabledIcon = AllIcons.Actions.Copy.disabled()
                addActionListener { copy() }
            }

            val copyAll = JBMenuItem("复制全部", AllIcons.Actions.Copy).apply {
                disabledIcon = AllIcons.Actions.Copy.disabled()
                addActionListener {
                    CopyPasteManager.getInstance().setContents(StringSelection(this@setupPopupMenu.text))
                }
            }

            add(copy)
            add(copyAll)
            addPopupMenuListener(object : PopupMenuListenerAdapter() {
                override fun popupMenuWillBecomeVisible(e: PopupMenuEvent) {
                    val hasSelectedText = !selectedText.isNullOrBlank()
                    copy.isEnabled = hasSelectedText
                    // translate.isEnabled = hasSelectedText
                    copyAll.isEnabled = !this@setupPopupMenu.text.isNullOrBlank()
                }
            })
        }
    }


    private fun JLabel.setupPopupMenu() {
        val copy = JBMenuItem("复制", AllIcons.Actions.Copy)
        copy.addActionListener { CopyPasteManager.getInstance().setContents(StringSelection(text)) }
        componentPopupMenu = JBPopupMenu().apply { add(copy) }
    }

    /**
     * 这里其实算不上update，只能说是set
     */
    private fun update() {
        translationResult.let {
            if (it != null) {
                updateComponents(it)
            } else {
                resetComponents()
            }
        }
    }

    private fun updateComponents(translationResult: TranslationResult) {
        updateLanguageLabel(translationResult)
        updateTextViewer(translationResult)
    }

    /**
     * 更新源语言和目标语言标签（这里其实算不上update，只能说是set）
     */
    private fun updateLanguageLabel(translationResult: TranslationResult) {
        sourceLanguageLabel.updateText(translationResult.srcLanguage)
        targetLanguageLabel.updateText(translationResult.targetLanguage)
    }

    /**
     * 更新源语言和目标语言文本（这里其实算不上update，只能说是set）
     */
    private fun updateTextViewer(translationResult: TranslationResult) {
        updateViewer(originalViewer,originalComponent,translationResult.original)
        updateViewer(translationViewer,translationComponent,translationResult.translation)
    }

    private fun updateViewer(viewer: Viewer, wrapper: JComponent, text: String?) {
        with(viewer) {
            updateText(text)
            wrapper.isVisible = isVisible
        }
    }

    private fun Viewer.updateText(text: String?) {
        this.text = text
        isVisible = !text.isNullOrEmpty()
        caretPosition = 0
    }

    private fun JLabel.updateText(text: String?) {
        this.text = text
        isVisible = !text.isNullOrEmpty()
    }

    private fun resetComponents() {

    }

    private fun Viewer.empty() {
        isVisible = false
        document.apply {
            if (length > 0) remove(0,length)
        }
    }

    private fun JLabel.empty() {
        isVisible = false
        text = null
    }

    companion object {
        const val GAP = 5

        private const val FONT_SIZE_LARGE = 18f
        private const val FONT_SIZE_DEFAULT = 14
        private const val FONT_SIZE_PHONETIC = 12

        private const val OFFSET = 10

        const val MAX_VIEWER_SMALL = 200
        const val MAX_VIEWER_HEIGHT = 250

        private val TOP_MARGIN_BORDER = JBUI.Borders.emptyTop(8)

        private fun flow(vararg components: JComponent): JComponent {
            val gap = JBUI.scale(GAP)
            val panel = NonOpaquePanel(FlowLayout(FlowLayout.LEFT, gap, 0))
            panel.border = JBUI.Borders.emptyLeft(-GAP)

            for (component in components) {
                panel.add(component)
            }

            return panel
        }

        @Suppress("unused")
        private fun flow2(left: JComponent, right: JComponent): JComponent {
            return BorderLayoutPanel()
                .andTransparent()
                .addToLeft(left)
                .addToCenter(right)
        }

        private fun spaceBetween(left: JComponent, right: JComponent): JComponent {
            return BorderLayoutPanel()
                .andTransparent()
                .addToLeft(left)
                .addToRight(right)
        }

//        private fun getStarButtonToolTipText(favoriteId: Long?): String {
//            return if (favoriteId == null) {
//                message("tooltip.addToWordBook")
//            } else {
//                message("tooltip.removeFromWordBook")
//            }
//        }
    }
}