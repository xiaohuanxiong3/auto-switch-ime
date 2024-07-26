package com.sqy.plugins.auto_switch_ime.util

import com.intellij.notification.NotificationGroup
import com.intellij.notification.NotificationGroupManager
import com.intellij.notification.NotificationType
import com.intellij.notification.Notifications

object NotificationUtil {

    private const val GROUP_ID = "auto-switch-ime-plugin"

    private const val MESSAGE_TITLE: String = "输入法自动切换插件"

    private val notificationGroup: NotificationGroup = NotificationGroupManager.getInstance().getNotificationGroup(GROUP_ID)

    fun info(message: String) {
        val notification = notificationGroup.createNotification(
            MESSAGE_TITLE,  // 通知的标题
            message,  // 通知的内容
            NotificationType.INFORMATION  // 通知的类型（信息、警告、错误）
        )
        Notifications.Bus.notify(notification)
    }

    fun warn(message: String) {
        val notification = notificationGroup.createNotification(
            MESSAGE_TITLE,  // 通知的标题
            message,  // 通知的内容
            NotificationType.WARNING  // 通知的类型（信息、警告、错误）
        )
        Notifications.Bus.notify(notification)
    }

    fun error(message: String) {
        val notification = notificationGroup.createNotification(
            MESSAGE_TITLE,  // 通知的标题
            message,  // 通知的内容
            NotificationType.ERROR  // 通知的类型（信息、警告、错误）
        )
        Notifications.Bus.notify(notification)
    }
}