package sk.ferinaf.secrethitler.activities

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main_menu.*
import sk.ferinaf.secrethitler.R
import sk.ferinaf.secrethitler.common.BaseActivity
import sk.ferinaf.secrethitler.dialogs.ConfirmDialog

class MainMenuActivity : BaseActivity() {

    override var fullScreen = false
    override var navigationColor: Int? = R.color.backgroundOrange
    override var statusBarColor: Int? = R.color.backgroundOrange

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_menu)

        mainMenu_startButton?.setOnClickListener {
            startButtonPressed()
        }

        rulesButton?.setOnClickListener {
            rulesButtonPressed()
        }

        settingsButton?.setOnClickListener {
            settingsButtonPressed()
        }

        aboutButton?.setOnClickListener {
            aboutButtonPressed()
        }
    }

    override fun onResume() {
        super.onResume()
        mainMenu_startButton.isEnabled = true
    }

    private fun startButtonPressed() {
        mainMenu_startButton.isEnabled = false
        val intent = Intent(this, AddPlayersActivity::class.java)
        startActivity(intent)
    }

    private fun rulesButtonPressed() {
        Toast.makeText(this, "TODO", Toast.LENGTH_SHORT).show()
    }

    private fun settingsButtonPressed() {
        val intent = Intent(this, SettingsActivity::class.java)
        startActivity(intent)
    }

    private fun aboutButtonPressed() {
        Toast.makeText(this, "TODO", Toast.LENGTH_SHORT).show()
    }

    override fun onBackPressed() {
        val confirmDialog = ConfirmDialog()
        confirmDialog.afterCreated = {
            confirmDialog.title?.text = "Do you want to quit?"
        }
        confirmDialog.onConfirm = { confirmed ->
            if (confirmed) {
                finish()
            }
        }
        confirmDialog.show(supportFragmentManager, "exit_dialog")
    }

}
