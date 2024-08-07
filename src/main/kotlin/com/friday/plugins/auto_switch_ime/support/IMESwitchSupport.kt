package com.friday.plugins.auto_switch_ime.support

import com.sun.jna.Library
import com.sun.jna.Native
import com.sun.jna.Platform

object IMESwitchSupport {

    interface IMEWin : Library {
        fun switchToZh(seq : Int)

        fun switchToEn(seq : Int)
    }

    interface IMEMac : Library {
        fun switchTo(key : String)
    }

//    init {
//        System.setProperty("jna.debug_load.jna", "true")
//        System.setProperty("jna.debug_load", "true")
//    }

    val LIBRARYWin = if (Platform.isWindows()) Native.load("libswitchIMEWin.dll", IMEWin::class.java)
    else null

    val LIBRARYMac =
        if (Platform.isMac()) Native.load("libswitchIMEMac.dylib", IMEMac::class.java)
        else null

    val toEnId = "com.apple.keylayout.ABC"

    val toZhId = "com.apple.inputmethod.SCIM.ITABC"

    // 标识输入法切换方法调用序号，防止由于多线程问题导致的输入法切换错误问题
    // 序号本身的正确性选择相信IDEA
    // 暂时在windows上做测试
    var seq : Int = 0

    fun switchToZh(seq: Int) {
        when(Platform.getOSType()) {
            Platform.WINDOWS -> {
                LIBRARYWin!!.switchToZh(seq)
            }
            Platform.MAC -> {
                LIBRARYMac!!.switchTo(toZhId)
            }
            else -> {
                throw AssertionError("未知操作系统!")
            }
        }
    }

    fun switchToEn(seq: Int) {
        when(Platform.getOSType()) {
            Platform.WINDOWS -> {
                LIBRARYWin!!.switchToEn(seq)
            }
            Platform.MAC -> {
                LIBRARYMac!!.switchTo(toEnId)
            }
            else -> {
                throw AssertionError("未知操作系统!")
            }
        }
    }
}