package sk.ferinaf.secrethitler.activities

import android.os.Bundle
import kotlinx.android.synthetic.main.activity_settings.*
import sk.ferinaf.secrethitler.R
import sk.ferinaf.secrethitler.common.BaseActivity
import sk.ferinaf.secrethitler.common.SettingsCategory
import sk.ferinaf.secrethitler.common.getSettingsFor

class SettingsActivity : BaseActivity() {
    override var fullScreen = false

    override var statusBarColor: Int? = R.color.secretGray
    override var navigationColor: Int? = R.color.secretGray

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        val sp = getSharedPreferences("Settings", 0)

        val vibrations = getSettingsFor(SettingsCategory.VIBRATIONS)
        settings_vibration_settingsItem?.switch?.isChecked = vibrations
        settings_vibration_settingsItem?.switch?.setOnCheckedChangeListener { _, isChecked ->
            val editor = sp.edit()
            editor.putBoolean("vibrations", isChecked)
            editor.apply()
        }

        val notHitler = getSettingsFor(SettingsCategory.NOT_HITLER)
        settings_notHitler_settingsItem?.switch?.isChecked = notHitler
        settings_notHitler_settingsItem?.switch?.setOnCheckedChangeListener { _, isChecked ->
            val editor = sp.edit()
            editor.putBoolean("not_hitler", isChecked)
            editor.apply()
        }

        val fastVote = getSettingsFor(SettingsCategory.FAST_VOTING)
        settings_fastVote_settingsItem?.switch?.isChecked = fastVote
        settings_fastVote_settingsItem?.switch?.setOnCheckedChangeListener { _, isChecked ->
            val editor = sp.edit()
            editor.putBoolean("fast_vote", isChecked)
            editor.apply()
        }

        val pileSize = getSettingsFor(SettingsCategory.PILE_SIZE)
        settings_pileSize_settingsItem?.switch?.isChecked = pileSize
        settings_pileSize_settingsItem?.switch?.setOnCheckedChangeListener { _, isChecked ->
            val editor = sp.edit()
            editor.putBoolean("pile_size", isChecked)
            editor.apply()
        }
    }
}