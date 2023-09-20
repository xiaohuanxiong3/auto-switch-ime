#include "library.h"
#include "windows.h"
#define IMC_GETCONVERSIONMODE 0x001

void switchToZh(){
    HWND hWnd = GetForegroundWindow();
    HWND imeWnd = ImmGetDefaultIMEWnd(hWnd);
    int result = SendMessage(imeWnd, WM_IME_CONTROL, IMC_GETCONVERSIONMODE, 0);
    if (result == 0) {
        keybd_event(VK_SHIFT, 0, 0, 0);
        keybd_event(VK_SHIFT, 0, KEYEVENTF_KEYUP, 0);
    }
}

void switchToEn(){
    HWND hWnd = GetForegroundWindow();
    HWND imeWnd = ImmGetDefaultIMEWnd(hWnd);
    int result = SendMessage(imeWnd, WM_IME_CONTROL, IMC_GETCONVERSIONMODE, 0);
    if (result != 0) {
        keybd_event(VK_SHIFT, 0, 0, 0);
        keybd_event(VK_SHIFT, 0, KEYEVENTF_KEYUP, 0);
    }
}


