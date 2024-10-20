#include "library.h"
#include <atomic>
#include "windows.h"
#include "imm.h"

#ifndef IMC_SETCONVERSIONMODE
#define IMC_SETCONVERSIONMODE 0x0002
#endif

#ifndef IMC_GETCONVERSIONMODE
#define IMC_GETCONVERSIONMODE 0x0001
#endif

std::atomic<int> lastReceived(0);

// 是否切换到 IME_CMODE_NATIVE
// 拿不到输入法上下文，弃用
void switchToNative_deprecated(bool flag) {
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

void switchToNative(bool flag, int current) {
    int expected = lastReceived.load();
    while (current > expected) {
        if (lastReceived.compare_exchange_strong(expected, current)) {
            HWND hWnd = GetForegroundWindow();
            HWND imeWnd = ImmGetDefaultIMEWnd(hWnd);
            if (flag) {
                // 插件使用PostMessage时不起作用，原因未知
                SendMessage(imeWnd, WM_IME_CONTROL, IMC_SETCONVERSIONMODE, IME_CMODE_NATIVE);
            } else {
                SendMessage(imeWnd, WM_IME_CONTROL, IMC_SETCONVERSIONMODE, IME_CMODE_ALPHANUMERIC);
            }
            return;
        }
        // 如果 compare_exchange_strong 失败，expected 会被更新为 lastReceived 的当前值
    }
}

void switchToZh(int seq){
    // HWND hWnd = GetForegroundWindow();
    // HWND imeWnd = ImmGetDefaultIMEWnd(hWnd);
    // SendMessage(imeWnd, WM_IME_CONTROL, IMC_SETCONVERSIONMODE, IME_CMODE_NATIVE);
    switchToNative(true, seq);
}

void switchToEn(int seq){
    // HWND hWnd = GetForegroundWindow();
    // HWND imeWnd = ImmGetDefaultIMEWnd(hWnd);
    // SendMessage(imeWnd, WM_IME_CONTROL, IMC_SETCONVERSIONMODE, IME_CMODE_ALPHANUMERIC);
    switchToNative(false,seq);
}