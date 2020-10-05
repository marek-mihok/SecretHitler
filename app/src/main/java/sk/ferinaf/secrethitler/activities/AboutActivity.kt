package sk.ferinaf.secrethitler.activities

import android.os.Bundle
import android.text.method.LinkMovementMethod
import kotlinx.android.synthetic.main.activity_about.*
import sk.ferinaf.secrethitler.R
import sk.ferinaf.secrethitler.common.BaseActivity

class AboutActivity : BaseActivity() {

    override var statusBarColor: Int? = R.color.secretGray
    override var navigationColor: Int? = R.color.secretGray
    override var fullScreen: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_about)

        license_textView?.setText(R.string.license_blah)
        license_textView?.movementMethod = LinkMovementMethod.getInstance()

        about_app_textView?.movementMethod = LinkMovementMethod.getInstance()
        about_developer_textView?.movementMethod = LinkMovementMethod.getInstance()
    }

}