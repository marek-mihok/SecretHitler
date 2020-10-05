package sk.ferinaf.secrethitler.activities

import android.os.Bundle
import android.view.View
import android.webkit.WebView
import android.webkit.WebViewClient
import kotlinx.android.synthetic.main.activity_full_rules.*
import sk.ferinaf.secrethitler.R
import sk.ferinaf.secrethitler.common.BaseActivity

class RulesActivity : BaseActivity() {

    override var fullScreen = false

    override var statusBarColor: Int? = R.color.secretGray
    override var navigationColor: Int? = R.color.secretGray

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_full_rules)
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()

        rules_webView?.visibility = View.GONE
        rules_webView?.loadUrl("file:///android_asset/rules.html")
        rules_webView?.webViewClient = object: WebViewClient() {
            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)
                loader_rules?.visibility = View.GONE
                rules_webView?.visibility = View.VISIBLE
            }
        }
    }

}