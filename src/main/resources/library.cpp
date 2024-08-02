#include "library.h"
#include "windows.h"
#include "imm.h"

#ifndef IMC_SETCONVERSIONMODE
#define IMC_SETCONVERSIONMODE 0x0002
#endif

#ifndef IMC_GETCONVERSIONMODE
#define IMC_GETCONVERSIONMODE 0x0001
#endif

HANDLE hMutex;

// 是否切换到 IME_CMODE_NATIVE
// 拿不到输入法上下文，弃用
void switchToNative(bool flag) {
    HWND hWnd = GetForegroundWindow();
    HIMC hIMC = ImmGetContext(hWnd);
    if (hIMC) {
        // 获取当前 IME 状态
        DWORD dwConversion, dwSentence;
        ImmGetConversionStatus(hIMC, &dwConversion, &dwSentence);
        // 切换为本地语言输入模式（这里是中文）
        if (flag) {
            if (!(dwConversion & IME_CMODE_NATIVE)) {
                ImmSetConversionStatus(hIMC, IME_CMODE_NATIVE, dwSentence);
            }
        }
        // 切换为英文输入模式
        else {
            if (!(dwConversion & IME_CMODE_ALPHANUMERIC)) {
                ImmSetConversionStatus(hIMC, IME_CMODE_ALPHANUMERIC, dwSentence);
            }
        }
    }
}

void switchToZh(){
    HWND hWnd = GetForegroundWindow();
    HWND imeWnd = ImmGetDefaultIMEWnd(hWnd);
    // 插件使用PostMessage时不起作用，原因未知
    SendMessage(imeWnd, WM_IME_CONTROL, IMC_SETCONVERSIONMODE, IME_CMODE_NATIVE);
}

void switchToEn(){
    HWND hWnd = GetForegroundWindow();
    HWND imeWnd = ImmGetDefaultIMEWnd(hWnd);
    SendMessage(imeWnd, WM_IME_CONTROL, IMC_SETCONVERSIONMODE, IME_CMODE_ALPHANUMERIC);
}




