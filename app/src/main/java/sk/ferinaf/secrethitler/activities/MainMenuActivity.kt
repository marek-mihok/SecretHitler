package sk.ferinaf.secrethitler.activities

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main_menu.*
import sk.ferinaf.secrethitler.R

class MainMenuActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.AppTheme_NoActionBar)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_menu)
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

}
