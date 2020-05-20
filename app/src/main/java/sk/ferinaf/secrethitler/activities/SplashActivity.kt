package sk.ferinaf.secrethitler.activities

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import sk.ferinaf.secrethitler.common.hasCutout

class SplashActivity : AppCompatActivity() {

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            val mHasCutout = window.decorView.rootWindowInsets.displayCutout != null

            if (mHasCutout != hasCutout) {
                val sp = getSharedPreferences("Settings", 0)
                val edit = sp.edit()

                edit.putBoolean("HAS_CUTOUT", mHasCutout)
                edit.apply()
            }

            finish()
            startActivity(Intent(this, MainMenuActivity::class.java))
            overridePendingTransition(0, 0)
        }
    }

}