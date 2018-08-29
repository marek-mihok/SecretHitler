package io.g3m.secrethitler

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.woxthebox.draglistview.DragListView
import kotlinx.android.synthetic.main.activity_choose_players.*

class ChoosePlayersActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_choose_players)

        hideNav()

    }


    override fun onResume() {
        super.onResume()
        hideNav()
    }

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        //hideNav()
    }

    // Function to hide navigation bar needs to be called at onCreate
    fun hideNav() {
        window.decorView.apply {
            systemUiVisibility = (View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                    or View.SYSTEM_UI_FLAG_FULLSCREEN
                    or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY)
        }
    }
}
