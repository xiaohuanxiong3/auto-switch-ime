package com.sqy.plugins.support

import com.sun.jna.Library
import com.sun.jna.Native
import com.sun.jna.Platform

object IMESwitchSupport {

    interface IMEWin : Library {
        fun switchToZh()

        fun switchToEn()
    }

    interface IMEMac : Library {
        fun switchTo(key : String)
    }

//    init {
//        System.setProperty("jna.debug_load.jna", "true")
//        System.setProperty("jna.debug_load", "true")
//    }

    val LIBRARYWin = if (Platform.isWindows()) Native.load("libswitchIMEWin.dll",IMEWin::class.java)
    else null

    val LIBRARYMac =
        if (Platform.isMac()) Native.load("libswitchIMEMac.dylib",IMEMac::class.java)
        else null

    val toEnId = "com.apple.keylayout.ABC"

    val toZhId = "com.apple.inputmethod.SCIM.ITABC"

    fun switchToZh() {
        when(Platform.getOSType()) {
            Platform.WINDOWS -> {
                LIBRARYWin!!.switchToZh()
            }
            Platform.MAC -> {
                LIBRARYMac!!.switchTo(toZhId)
            }
            else -> {
                throw AssertionError("未知操作系统!")
            }
        }
    }

    fun switchToEn() {
        when(Platform.getOSType()) {
            Platform.WINDOWS -> {
                LIBRARYWin!!.switchToEn()
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