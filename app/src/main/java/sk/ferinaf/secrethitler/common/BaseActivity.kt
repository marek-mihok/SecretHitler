package sk.ferinaf.secrethitler.common

import android.annotation.SuppressLint
import android.os.*
import androidx.appcompat.app.AppCompatActivity
import android.view.View
import sk.ferinaf.secrethitler.R
import sk.ferinaf.secrethitler.dialogs.ConfirmDialog

@SuppressLint("Registered")
abstract class BaseActivity :  AppCompatActivity() {

    // Help functions to add dialog to really go back
    open var askToGetBack: Boolean = false
    abstract var fullScreen: Boolean

    override fun onCreate(savedInstanceState: Bundle?) {

        // Handle cutouts
        if (hasCutout) {
            setTheme(R.style.AppTheme_NoActionBarNoFullScreen)
        } else {
            setTheme(R.style.AppTheme_NoActionBar)
        }

        super.onCreate(savedInstanceState)
        hideNav()
    }

    override fun onResume() {
        super.onResume()
        hideNav()
    }

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        hideNav() // to hide when using chatHeads
    }

    // Function to hide navigation bar needs to be called at onCreate
    private fun hideNav() {
        if (fullScreen) {
            window.decorView.apply {
                systemUiVisibility = (View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY)
            }
        }
    }

    override fun onBackPressed() {
        if(askToGetBack) {
            val confirmDialog = ConfirmDialog()
            confirmDialog.isCancelable = false
            confirmDialog.show(supportFragmentManager, "confirm dialog")
            confirmDialog.onConfirm = {
                if (it) super.onBackPressed()
            }
        } else {
            super.onBackPressed()
        }
    }

}