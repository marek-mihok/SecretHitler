package sk.ferinaf.secrethitler.common

import android.annotation.SuppressLint
import android.os.*
import androidx.appcompat.app.AppCompatActivity
import android.view.View
import androidx.fragment.app.DialogFragment
import sk.ferinaf.secrethitler.R
import sk.ferinaf.secrethitler.dialogs.ConfirmDialog

@SuppressLint("Registered")
abstract class BaseActivity :  AppCompatActivity() {

    // Help functions to add dialog to really go back
    open var askToGetBack: Boolean = false
    open var returnQuestion: String? = null
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
                        or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
//                        or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
//                        or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
//                        or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        )
            }
        }
    }

    override fun onBackPressed() {
        if(askToGetBack) {
            val confirmDialog = ConfirmDialog()
            confirmDialog.isCancelable = false
            confirmDialog.show(supportFragmentManager, "confirm dialog")
            if (returnQuestion != null) {
                confirmDialog.afterCreated = {
                    confirmDialog.title?.text = returnQuestion
                }
            }
            confirmDialog.onConfirm = {
                if (it) super.onBackPressed()
            }
        } else {
            super.onBackPressed()
        }
    }

}