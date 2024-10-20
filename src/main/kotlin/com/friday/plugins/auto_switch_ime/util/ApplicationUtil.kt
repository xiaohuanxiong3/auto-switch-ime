package com.friday.plugins.auto_switch_ime.util

import com.intellij.openapi.application.Application
import com.intellij.openapi.application.ApplicationManager

object ApplicationUtil {

    private val application: Application by lazy {
        ApplicationManager.getApplication()
    }

    fun executeOnPooledThread(runnable: () -> Unit) {
        application.executeOnPooledThread(runnable)
    }

    fun invokeLater(runnable: () -> Unit) {
        application.invokeLater(runnable)
    }

    fun runReadAction(runnable: () -> Unit) {
        application.runReadAction(runnable)
    }
}