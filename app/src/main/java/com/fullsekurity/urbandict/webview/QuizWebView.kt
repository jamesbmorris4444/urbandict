package com.fullsekurity.urbandict.webview

import android.webkit.WebView
import android.webkit.WebViewClient


class QuizWebView : WebViewClient() {

    override fun shouldOverrideUrlLoading(view: WebView, url: String?): Boolean {
        view.loadUrl(url)
        return true
    }
}