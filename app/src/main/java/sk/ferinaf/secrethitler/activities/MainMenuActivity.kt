package sk.ferinaf.secrethitler.activities

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main_menu.*
import sk.ferinaf.secrethitler.R
import sk.ferinaf.secrethitler.common.BaseActivity
import sk.ferinaf.secrethitler.common.asColor
import sk.ferinaf.secrethitler.common.customSetStatusBarColor
import sk.ferinaf.secrethitler.dialogs.ConfirmDialog

class MainMenuActivity : BaseActivity() {

    override var fullScreen = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_menu)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            val orangeColor = R.color.backgroundOrange.asColor()
            window.navigationBarColor = orangeColor
            window.customSetStatusBarColor(orangeColor)
        }
    }

    override fun onResume() {
        super.onResume()
        mainMenu_startButton.isEnabled = true
    }

    fun startButtonPressed(sender: View) {
        mainMenu_startButton.isEnabled = false
        val intent = Intent(this, AddPlayersActivity::class.java)
        startActivity(intent)
    }

    fun settingsButtonPressed(sender: View){
        Toast.makeText(this, "To do ...", Toast.LENGTH_SHORT).show()
//        val intent = Intent(this, GameActivity::class.java)
//        startActivity(intent)
    }

    fun rulesButtonPressed(sender: View){
        Toast.makeText(this, "To do ... vibrations enabled", Toast.LENGTH_SHORT).show()
        val editor = getSharedPreferences("Settings", 0).edit()
        editor.putBoolean("vibrations", true)
        editor.apply()

    }

    fun aboutButtonPressed(sender: View){
        Toast.makeText(this, "To do ... vibrations disabled", Toast.LENGTH_SHORT).show()
        val editor = getSharedPreferences("Settings", 0).edit()
        editor.putBoolean("vibrations", false)
        editor.apply()
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
