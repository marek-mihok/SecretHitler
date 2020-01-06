package io.g3m.secrethitler

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.Toast
import io.g3m.secrethitler.common.PlayersInfo
import kotlinx.android.synthetic.main.activity_add_players.*
import android.view.View.OnTouchListener
import android.view.inputmethod.InputMethodManager


class AddPlayersActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_players)

        val sp = getSharedPreferences("Settings", 0)

        val player1 = sp.getString("Player1", "")
        if(player1 != "") {
            player1Input.editText?.setText(player1)
        }

        val player2 = sp.getString("Player2", "")
        if(player2 != "") {
            player2Input.editText?.setText(player2)
        }

        val player3 = sp.getString("Player3", "")
        if(player3 != "") {
            player3Input.editText?.setText(player3)
        }

        val player4 = sp.getString("Player4", "")
        if(player4 != "") {
            player4Input.editText?.setText(player4)
        }

        val player5 = sp.getString("Player5", "")
        if(player5 != "") {
            player5Input.editText?.setText(player5)
        }

        val player6 = sp.getString("Player6", "")
        if(player6 != "") {
            player6Input.editText?.setText(player6)
        }

        val player7 = sp.getString("Player7", "")
        if(player7 != "") {
            player7Input.editText?.setText(player7)
        }

        val player8 = sp.getString("Player8", "")
        if(player8 != "") {
            player8Input.editText?.setText(player8)
        }

        val player9 = sp.getString("Player9", "")
        if(player9 != "") {
            player9Input.editText?.setText(player9)
        }

        val player10 = sp.getString("Player10", "")
        if(player10 != "") {
            player10Input.editText?.setText(player10)
        }

        scrollView.setOnTouchListener(OnTouchListener { v, event ->
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            this.currentFocus?.let { view ->
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0)
            }

            false
        })
    }

    fun onStartPressed(v: View) {

        val textInputArray = arrayOf(
                player1Input,
                player2Input,
                player3Input,
                player4Input,
                player5Input,
                player6Input,
                player7Input,
                player8Input,
                player9Input,
                player10Input)

        val names = arrayListOf<String>()
        for(input in textInputArray) {
            val name = input.editText?.text.toString()
            if(name.isNotEmpty()) {
                names.add(name)
            }
        }

        val sp = getSharedPreferences("Settings", 0).edit()
        sp.putString("Player1", player1Input.editText?.text.toString())
        sp.putString("Player2", player2Input.editText?.text.toString())
        sp.putString("Player3", player3Input.editText?.text.toString())
        sp.putString("Player4", player4Input.editText?.text.toString())
        sp.putString("Player5", player5Input.editText?.text.toString())
        sp.putString("Player6", player6Input.editText?.text.toString())
        sp.putString("Player7", player7Input.editText?.text.toString())
        sp.putString("Player8", player8Input.editText?.text.toString())
        sp.putString("Player9", player9Input.editText?.text.toString())
        sp.putString("Player10", player10Input.editText?.text.toString())
        sp.apply()

        if(names.size < 5) {
            Toast.makeText(this, "Add at least 5 players", Toast.LENGTH_SHORT).show()
        } else {
            val intent = Intent(this, RevealIdentity::class.java)
            PlayersInfo.setNames(names)
            intent.putExtra("playerIndex", 0)
            startActivity(intent)
            finish()
        }
    }
}
