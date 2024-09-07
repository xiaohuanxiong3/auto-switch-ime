package com.friday.plugins.auto_switch_ime.ui

import com.friday.plugins.auto_switch_ime.Constants
import com.friday.plugins.auto_switch_ime.language.PsiFileLanguage
import com.friday.plugins.auto_switch_ime.ui.AutoSwitchIMEStatusBarIconWidget.IconStatus.*
import com.friday.plugins.auto_switch_ime.util.EditorUtil
import com.intellij.openapi.fileEditor.FileEditorManagerEvent
import com.intellij.openapi.fileEditor.FileEditorManagerListener
import com.intellij.openapi.fileEditor.TextEditor
import com.intellij.openapi.project.Project
import com.intellij.openapi.util.IconLoader
import com.intellij.openapi.wm.StatusBar
import com.intellij.openapi.wm.StatusBarWidget
import com.intellij.openapi.wm.impl.status.EditorBasedWidget
import com.intellij.util.Consumer
import org.jetbrains.annotations.NotNull
import java.awt.event.MouseEvent
import java.util.*
import javax.swing.Icon


/**
 * @Author Handsome Young
 * @Date 2024/9/6 09:22
 */
class AutoSwitchIMEStatusBarIconWidget(project: Project) : EditorBasedWidget(project) {

    private val iconCache: EnumMap<IconStatus, Icon> = EnumMap(IconStatus::class.java)

    private var currentStatus: IconStatus? = null

    override fun ID(): String {
        return Constants.STATUS_BAR_WIDGET_ID
    }

    init {
        // 图标来自 https://www.iconfont.cn/user/detail?spm=a313x.search_index.0.d214f71f6.1b3f3a81UWzGc1&uid=2392&nid=v2gJTcCFjB3u 三土_Ly
        iconCache[ENABLE] = IconLoader.getIcon("/icons/ENABLE.svg", AutoSwitchIMEStatusBarIconWidget::class.java)
        iconCache[DISABLE] = IconLoader.getIcon("/icons/DISABLE.svg", AutoSwitchIMEStatusBarIconWidget::class.java)
        iconCache[DEACTIVATE] = IconLoader.getIcon("/icons/DEACTIVATE.svg", AutoSwitchIMEStatusBarIconWidget::class.java)
    }

    override fun getPresentation(): StatusBarWidget.WidgetPresentation? {
        return IconPresentation()
    }

    override fun install(statusBar: StatusBar) {
        myStatusBar = statusBar

        statusBar.updateWidget(ID())
        val project: Project = statusBar.project ?: return
        project.messageBus.connect(this).subscribe<FileEditorManagerListener>(
            FileEditorManagerListener.FILE_EDITOR_MANAGER,
            object : FileEditorManagerListener {
                override fun selectionChanged(@NotNull event: FileEditorManagerEvent) {
                    if (myStatusBar != null) {
                        // 关闭所有tab页
                        if (event.newEditor == null) {
                            setStatus(null)
                            currentStatus = null
                            return
                        }
                        event.newEditor?.let {
                            it as? TextEditor
                        }?.let {
                            setStatus(PsiFileLanguage.getEditorStatusBarWidgetStatus(it.editor))
                        }
                    }
                }
            })
    }

    inner class IconPresentation : StatusBarWidget.IconPresentation {

        override fun getIcon(): Icon? {
            return getFocusedEditor()?.let { editor ->
                     currentStatus = PsiFileLanguage.getEditorStatusBarWidgetStatus(editor)
                     when(currentStatus) {
                         ENABLE -> iconCache[ENABLE]
                         DISABLE -> iconCache[DISABLE]
                         DEACTIVATE -> iconCache[DEACTIVATE]
                         else -> null
                    }
                }
        }

        override fun getTooltipText(): String? {
            return getFocusedEditor()?.run { currentStatus?.s }
        }

        override fun getClickConsumer(): Consumer<MouseEvent>? {
            return Consumer {
                    getFocusedEditor()?.let {  editor ->
                        when (currentStatus) {
                            ENABLE -> {
                                setStatus(DISABLE)
                                PsiFileLanguage.toggleIMESwitchSetting(EditorUtil.getLanguage(editor))
                            }
                            DISABLE -> {
                                setStatus(ENABLE)
                                PsiFileLanguage.toggleIMESwitchSetting(EditorUtil.getLanguage(editor))
                            }
                            DEACTIVATE -> {

                            }
                            else -> {}
                        }
                    }
            }
        }

    }

    fun setStatus(status: IconStatus?) {
        if (currentStatus == status) {
            return
        }
        this.currentStatus = status
        myStatusBar?.updateWidget(ID())
    }

    fun forceUpdateWidget() {
        myStatusBar?.updateWidget(ID())
    }

    enum class IconStatus(val s: String) {
        /**
         * 开启输入法自动切换
         */
        ENABLE("开启输入法自动切换"),

        /**
         * 关闭输入法自动切换
         */
        DISABLE("关闭输入法自动切换"),

        /**
         * 本Editor对应的语言暂未适配输入法自动切换
         */
        DEACTIVATE("该语言还未实现输入法自动切换")
    }

}