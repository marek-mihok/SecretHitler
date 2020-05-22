package sk.ferinaf.secrethitler.activities

import android.os.Bundle
import kotlinx.android.synthetic.main.activity_voting.view.*
import sk.ferinaf.secrethitler.R
import sk.ferinaf.secrethitler.common.BaseActivity

class VotingActivity : BaseActivity() {

    override var fullScreen = true
    override var askToGetBack = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_voting)

        supportFragmentManager.beginTransaction()
    }
}