package com.zerolaunch.minimal

import android.annotation.SuppressLint
import android.app.Activity
import android.app.ActivityManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.webkit.JavascriptInterface
import android.webkit.WebChromeClient
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import org.json.JSONArray
import org.json.JSONObject

class MainActivity : Activity() {

    private lateinit var webView: WebView

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        webView = WebView(this)
        setContentView(webView)

        val settings: WebSettings = webView.settings
        settings.javaScriptEnabled = true
        settings.domStorageEnabled = true
        settings.databaseEnabled = true
        settings.cacheMode = WebSettings.LOAD_DEFAULT
        settings.mediaPlaybackRequiresUserGesture = false

        webView.webViewClient = WebViewClient()
        webView.webChromeClient = WebChromeClient()
        webView.addJavascriptInterface(AndroidBridge(this), "AndroidBridge")

        webView.loadUrl("file:///android_asset/index.html")
    }

    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        // A home screen launcher should not exit when pressing Back on the home screen
        webView.evaluateJavascript("if (typeof closeDrawer === 'function') closeDrawer();", null)
    }

    inner class AndroidBridge(private val context: Context) {

        @JavascriptInterface
        fun getInstalledAppsJson(): String {
            val pm = context.packageManager
            val mainIntent = Intent(Intent.ACTION_MAIN, null).apply {
                addCategory(Intent.CATEGORY_LAUNCHER)
            }
            val resolveInfos = pm.queryIntentActivities(mainIntent, 0)
            val arr = JSONArray()

            for (info in resolveInfos) {
                val pkg = info.activityInfo.packageName
                if (pkg == context.packageName) continue
                val label = info.loadLabel(pm).toString()
                val obj = JSONObject()
                obj.put("packageName", pkg)
                obj.put("label", label)
                obj.put("category", "App")
                arr.put(obj)
            }
            return arr.toString()
        }

        @JavascriptInterface
        fun launchPackage(packageName: String): Boolean {
            return try {
                val launchIntent = context.packageManager.getLaunchIntentForPackage(packageName)
                if (launchIntent != null) {
                    launchIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    context.startActivity(launchIntent)
                    true
                } else {
                    false
                }
            } catch (e: Exception) {
                false
            }
        }

        @JavascriptInterface
        fun getSystemMemoryJson(): String {
            val actManager = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
            val memInfo = ActivityManager.MemoryInfo()
            actManager.getMemoryInfo(memInfo)

            val totalGb = memInfo.totalMem.toDouble() / (1024.0 * 1024.0 * 1024.0)
            val availGb = memInfo.availMem.toDouble() / (1024.0 * 1024.0 * 1024.0)
            val usedGb = totalGb - availGb

            val obj = JSONObject()
            obj.put("totalGb", String.format("%.1f", totalGb).toDouble())
            obj.put("usedGb", String.format("%.1f", usedGb).toDouble())
            return obj.toString()
        }
    }
}
