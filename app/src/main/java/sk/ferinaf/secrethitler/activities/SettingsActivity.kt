package sk.ferinaf.secrethitler.activities

import android.os.Bundle
import kotlinx.android.synthetic.main.activity_settings.*
import sk.ferinaf.secrethitler.R
import sk.ferinaf.secrethitler.common.BaseActivity

class SettingsActivity : BaseActivity() {
    override var fullScreen = false

    override var statusBarColor: Int? = R.color.secretGray
    override var navigationColor: Int? = R.color.secretGray

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        val sp = getSharedPreferences("Settings", 0)

        val vibrations = sp.getBoolean("vibrations", true)
        settings_vibration_settingsItem?.switch?.isChecked = vibrations
        settings_vibration_settingsItem?.switch?.setOnCheckedChangeListener { _, isChecked ->
            val editor = sp.edit()
            editor.putBoolean("vibrations", isChecked)
            editor.apply()
        }
    }
}