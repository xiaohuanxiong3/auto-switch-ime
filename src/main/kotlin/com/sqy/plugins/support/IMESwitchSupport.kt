package com.sqy.plugins.support

import com.sun.jna.Library
import com.sun.jna.Native

object IMESwitchSupport {

    interface IME : Library {
        fun switchToZh()

        fun switchToEn()
    }

    val LIBRARY = Native.load("libswitchIME",IME::class.java)
}