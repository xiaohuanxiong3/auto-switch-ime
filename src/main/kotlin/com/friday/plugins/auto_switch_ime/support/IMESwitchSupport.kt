package com.friday.plugins.auto_switch_ime.support

import com.friday.plugins.auto_switch_ime.support.IMEStatus.*
import com.sun.jna.Library
import com.sun.jna.Native
import com.sun.jna.Platform

object IMESwitchSupport {

    interface IMEWin : Library {
        fun isEn() : Boolean

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

    val LIBRARYWin = if (Platform.isWindows()) Native.load("windows/libswitchIMEWin.dll", IMEWin::class.java)
    else null

    val LIBRARYMac =
        if (Platform.isMac()) Native.load("macos/libswitchIMEMac.dylib", IMEMac::class.java)
        else null

    val toEnId = "com.apple.keylayout.ABC"

    val toZhId = "com.apple.inputmethod.SCIM.ITABC"

    // 标识输入法切换方法调用序号，防止由于多线程问题导致的输入法切换错误问题
    // 序号本身的正确性选择相信IDEA
    // 暂时在windows上做测试
    // 似乎所有涉及seq自增的操作都是在EDT线程上处理的
    // 那@Volatile感觉没有必要
    var seq : Int = 0

    // 这里没有判断具体的输入法
    // 所以在windows端使用微软输入法之外的输入法以及mac端使用系统输入法之外的输入法可能会出问题
    fun switchTo(seq: Int, imeStatus: IMEStatus) {
        when(Platform.getOSType()) {
            Platform.WINDOWS -> {
                when (imeStatus) {
                    EN -> {
                        LIBRARYWin!!.switchToEn(seq)
                    }
                    OTHER -> {
                        LIBRARYWin!!.switchToZh(seq)
                    }
                    NONE -> {

                    }
                }
            }
            Platform.MAC -> {
                when (imeStatus) {
                    EN -> {
                        LIBRARYMac!!.switchTo(toEnId)
                    }
                    OTHER -> {
                        LIBRARYMac!!.switchTo(toZhId)
                    }
                    NONE -> {

                    }
                }
            }
            else -> {
                throw AssertionError("未知操作系统!")
            }
        }
    }

    fun getImeStatus() : IMEStatus {
        return when (Platform.getOSType()) {
            Platform.WINDOWS -> {
                if (LIBRARYWin!!.isEn()) EN else OTHER
            }
            Platform.MAC -> {
                EN
            }
            else -> {
                throw AssertionError("未知操作系统!")
            }
        }
    }
}