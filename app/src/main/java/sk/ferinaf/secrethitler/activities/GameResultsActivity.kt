package sk.ferinaf.secrethitler.activities

import android.os.Bundle
import kotlinx.android.synthetic.main.activity_game_results.*
import sk.ferinaf.secrethitler.R
import sk.ferinaf.secrethitler.common.BaseActivity
import sk.ferinaf.secrethitler.common.GameState
import sk.ferinaf.secrethitler.common.asColor
import sk.ferinaf.secrethitler.widgets.ConfirmButton

class GameResultsActivity : BaseActivity() {
    override var fullScreen = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game_results)

        val liberalsWin = intent.getBooleanExtra("liberalsWin", true)

        if (liberalsWin) {
            // Liberals win
            gameResults_winningTeam_textView?.setText(R.string.liberals_win)
            gameResults_winningTeam_imageView?.setImageResource(R.drawable.ic_dove)
            gameResults_winningTeam_cardView?.setCardBackgroundColor(R.color.blueDark.asColor())
            gameResults_firstTeam_playersTable?.setLiberals()
            gameResults_secondTeam_playersTable?.setFascists()
        } else {
            // Fascists win
            gameResults_winningTeam_textView?.setText(R.string.fascists_win)
            gameResults_winningTeam_imageView?.setImageResource(R.drawable.ic_skull)
            gameResults_winningTeam_cardView?.setCardBackgroundColor(R.color.redDark.asColor())
            gameResults_firstTeam_playersTable?.setFascists()
            gameResults_secondTeam_playersTable?.setLiberals()
        }

        confirmGameEnd_confirmButton?.textView?.setText(R.string.hold_to_finish)
        confirmGameEnd_confirmButton?.duration = 1000
        confirmGameEnd_confirmButton?.listener = object : ConfirmButton.ProgressListener {
            override fun onFinish() {
                finish()
            }
        }
    }
}