package com.friday.plugins.auto_switch_ime.ui

import com.intellij.openapi.project.Project
import com.intellij.openapi.util.Disposer
import com.intellij.openapi.wm.StatusBarWidget
import com.intellij.openapi.wm.impl.status.widget.StatusBarEditorBasedWidgetFactory

/**
 * @Author Handsome Young
 * @Date 2024/9/6 11:44
 */
class MyStatusBarWidgetFactory : StatusBarEditorBasedWidgetFactory() {

    override fun getId(): String {
        return "auto-switch-ime-widget-factory"
    }

    override fun getDisplayName(): String {
        return "AUTO-SWITCH-IME"
    }

    override fun createWidget(project: Project): StatusBarWidget {
        return AutoSwitchIMEStatusBarIconWidget(project)
    }

    override fun disposeWidget(widget: StatusBarWidget) {
        Disposer.dispose(widget)
    }
}