package sk.ferinaf.secrethitler.activities

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.activity_game.*
import sk.ferinaf.secrethitler.R
import sk.ferinaf.secrethitler.common.BaseActivity
import sk.ferinaf.secrethitler.fragments.GameFragment
import sk.ferinaf.secrethitler.fragments.PlayersFragment
import sk.ferinaf.secrethitler.fragments.PolicyFragment
import sk.ferinaf.secrethitler.widgets.GameBottomNavigation

class GameActivity : BaseActivity() {

    override var fullScreen = true
    override var askToGetBack = true

    private val gameFragment = GameFragment()
    val policyFragment = PolicyFragment()
    private val playersFragment = PlayersFragment()

    private var activeFragment: Fragment = gameFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game)

        policyFragment.onCardDiscard = {
            Log.d("policyDiscard", it.toString())
        }

        policyFragment.onCardEnact = {
            Log.d("policyEnact", it.toString())
        }

        game_navigation?.onSelectItem(GameBottomNavigation.NavigationItem.GAME)

        game_navigation.onSelect = { item ->
            when (item) {
                GameBottomNavigation.NavigationItem.GAME -> {
                    selectFragment(gameFragment)
                }
                GameBottomNavigation.NavigationItem.POLICY -> {
                    selectFragment(policyFragment)
                }
                GameBottomNavigation.NavigationItem.PLAYERS -> {
                    selectFragment(playersFragment)
                }
            }
        }

        supportFragmentManager.beginTransaction().add(R.id.game_main_container, playersFragment, "3").hide(playersFragment).commit()
        supportFragmentManager.beginTransaction().add(R.id.game_main_container, policyFragment, "2").hide(policyFragment).commit()
        supportFragmentManager.beginTransaction().add(R.id.game_main_container, gameFragment, "1").commit()
    }

    private fun selectFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction().hide(activeFragment).show(fragment).commit()
        activeFragment = fragment
    }

}