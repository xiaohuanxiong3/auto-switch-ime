package com.sqy.plugins.translation.ui

import com.intellij.openapi.Disposable
import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.popup.Balloon
import com.intellij.openapi.util.Disposer
import com.intellij.openapi.wm.WindowManager
import com.intellij.ui.BalloonImpl
import com.intellij.ui.components.JBPanel
import com.intellij.ui.components.panels.NonOpaquePanel
import com.intellij.ui.popup.BalloonPopupBuilderImpl
import com.intellij.util.containers.ContainerUtil
import com.intellij.util.ui.*
import com.sqy.plugins.translation.TranslationResult
import com.sqy.plugins.translation.engine.BaiduTranslationEngine
import com.sqy.plugins.translation.service.TranslationService
import com.sqy.plugins.translation.service.TranslationUIManager
import com.sqy.plugins.translation.ui.icon.Spinner
import java.awt.*
import javax.swing.JComponent
import javax.swing.SwingUtilities

class TranslationBalloon(
        private val editor: Editor,
        private val text: String
) : Disposable {

    private val project: Project? = editor.project

    private val layout = CardLayout()

    private val contentPanel = JBPanel<JBPanel<*>>(layout)

    private val errorPanel = TranslationFailedComponent().apply {
        maximumSize = JBDimension(MAX_WIDTH, Int.MAX_VALUE)
    }

    private val processPane = ProcessComponent(Spinner(), JBUI.insets(INSETS, INSETS * 2))

    private val translationContentPane = NonOpaquePanel(FrameLayout())

    private val translationPane = TranslationPane(project, getMaxWidth(project))

    private val balloon: Balloon

    private var isShowing = false
    private var _disposed = false
    val disposed get() = _disposed || balloon.isDisposed

    init {
        initTranslationPanel()
        initContentPanel()

        balloon = createBalloon(contentPanel)
        initActions()

        Disposer.register(TranslationUIManager.disposable(project), balloon)
        // 如果使用`Disposer.register(balloon, this)`的话，
        // `TranslationBalloon`在外部以子`Disposable`再次注册时将会使之无效。
        Disposer.register(balloon) { Disposer.dispose(this) }
        Disposer.register(this, processPane)
        Disposer.register(this, translationPane)

//        ApplicationManager
//                .getApplication()
//                .messageBus
//                .connect(this)
//                .subscribe(SettingsChangeListener.TOPIC, this)
    }

    private fun initContentPanel() = contentPanel
            .withFont(UI.defaultFont)
            .andTransparent()
            .apply {
                add(CARD_PROCESSING, processPane)
                add(CARD_TRANSLATION, translationContentPane)
                add(CARD_ERROR, errorPanel)
            }

    private fun initTranslationPanel() {
//        presenter.supportedLanguages.let { (source, target) ->
//            translationPane.setSupportedLanguages(source, target)
//        }

        translationContentPane.apply {
//            add(pinButton.apply {
//                border = JBEmptyBorder(5, 0, 0, 0)
//                isVisible = false
//                alignmentX = Component.RIGHT_ALIGNMENT
//                alignmentY = Component.TOP_ALIGNMENT
//            })
            add(translationPane)
        }
    }

    private fun initActions() {
        with(translationPane) {
            onRevalidate { if (!disposed) balloon.revalidate() }
//            onLanguageChanged { src, target ->
//                run {
//                    presenter.updateLastLanguages(src, target)
//                    translate(src, target)
//                }
//            }
//            onNewTranslate { text, src, target ->
//                invokeLater { showOnTranslationDialog(text, src, target) }
//            }
//            onSpellFixed { spell ->
//                val targetLang = presenter.getTargetLang(spell)
//                invokeLater { showOnTranslationDialog(spell, Lang.AUTO, targetLang) }
//            }
        }

        errorPanel.onRetry { onTranslate() }
//        Toolkit.getDefaultToolkit().addAWTEventListener(eventListener, AWTEvent.MOUSE_MOTION_EVENT_MASK)
    }

    override fun dispose() {
        if (_disposed) {
            return
        }

        _disposed = true
        isShowing = false

        balloon.hide()
//        Toolkit.getDefaultToolkit().removeAWTEventListener(eventListener)
    }

    fun hide() {
        if (!disposed) {
            Disposer.dispose(this)
        }
    }

    fun show(tracker: PositionTracker<Balloon>, position: Balloon.Position) {
        check(!disposed) { "Balloon has been disposed." }

//        if (!presenter.translator.checkConfiguration()) {
//            hide()
//            return
//        }

        if (!isShowing) {
            isShowing = true
            balloon.show(tracker, position)
            onTranslate()
        }

//        showCard(CARD_PROCESSING)
//        ApplicationManager.getApplication().apply {
//            executeOnPooledThread {
//                Thread.sleep(1000)
//                showCard(CARD_TRANSLATION)
//            }
//            executeOnPooledThread {
//                Thread.sleep(2000)
//                showCard(CARD_ERROR)
//            }
//        }

//        Thread.sleep(1000)
//        showCard(CARD_TRANSLATION)
//        Thread.sleep(1000)
//        showCard(CARD_ERROR)
//        Thread.sleep(1000)
    }

    private fun onTranslate() {
//        val targetLang = presenter.getTargetLang(text)
//        translate(Lang.AUTO, targetLang)
        translate(text)
    }

    fun translate(text: String) = ApplicationManager.getApplication()
            .getService(TranslationService::class.java).translate(this,text)

    private fun showCard(card: String) {
        // 使用`SwingUtilities.invokeLater`似乎要比使用`Application.invokeLater`更好，
        // `Application.invokeLater`有时候会得不到想要的效果，UI组件不会自动调整尺寸。
        SwingUtilities.invokeLater {
            if (!disposed) {
                layout.show(contentPanel, card)
                if (card == CARD_PROCESSING) {
                    processPane.resume()
                } else {
                    processPane.suspend()
                }
                balloon.revalidate()
            }
        }
    }

    fun showStartTranslate() {
        if (!disposed) {
            showCard(CARD_PROCESSING)
            errorPanel.update(null as Throwable?)
        }
    }

     fun showTranslation(translation: TranslationResult, fromCache: Boolean) {
        if (!disposed) {
            translationPane.translationResult = translation
            showCard(CARD_TRANSLATION)
        }
    }

    fun showError(throwable: Throwable) {
        if (!disposed) {
            errorPanel.update(throwable)
            showCard(CARD_ERROR)
        }
    }

    companion object {

        private const val MAX_WIDTH = 500
        private const val MIN_ERROR_PANEL_WIDTH = 300
        private const val INSETS = 20

        private const val CARD_PROCESSING = "processing"
        private const val CARD_ERROR = "error"
        private const val CARD_TRANSLATION = "translation"

        private fun createBalloon(content: JComponent): Balloon = BalloonPopupBuilderImpl(ContainerUtil.createWeakMap(),content)
                .setShadow(true)
                .setDialogMode(true)
                .setRequestFocus(true)
                .setHideOnAction(true)
                .setHideOnCloseClick(true)
                .setHideOnKeyOutside(false)
                .setHideOnFrameResize(true)
                .setHideOnClickOutside(true)
                .setBlockClicksThroughBalloon(true)
                .setCloseButtonEnabled(false)
                .setAnimationCycle(200)
                .setBorderColor(Color.darkGray.toAlpha(35))
                .setFillColor(JBUI.CurrentTheme.CustomFrameDecorations.paneBackground())
                .createBalloon()
                .apply {
                    this as BalloonImpl
                    setHideListener { hide() }
                }

        private fun getMaxWidth(project: Project?): Int {
            val maxWidth = (WindowManager.getInstance().getFrame(project)?.width ?: 0) * 0.45
            return maxOf(maxWidth.toInt(), MAX_WIDTH)
        }

        private fun Color.toAlpha(alpha: Int) = Color(red, green, blue, alpha)
    }

}