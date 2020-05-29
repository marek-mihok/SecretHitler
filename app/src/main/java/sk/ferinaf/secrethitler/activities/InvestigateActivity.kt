package sk.ferinaf.secrethitler.activities

import android.os.Bundle
import sk.ferinaf.secrethitler.R
import sk.ferinaf.secrethitler.common.BaseActivity
import sk.ferinaf.secrethitler.fragments.investigation.InvestigationPickPlayerFragment

class InvestigateActivity : BaseActivity() {

    override var fullScreen = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_investigate)

        supportFragmentManager.beginTransaction().add(R.id.investigate_container, InvestigationPickPlayerFragment()).commit()
    }
}