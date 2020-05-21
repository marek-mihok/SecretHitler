package sk.ferinaf.secrethitler.activities

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.activity_game.*
import sk.ferinaf.secrethitler.R
import sk.ferinaf.secrethitler.common.BaseActivity
import sk.ferinaf.secrethitler.fragments.ConfirmVetoFragment
import sk.ferinaf.secrethitler.fragments.GameFragment
import sk.ferinaf.secrethitler.fragments.PlayersFragment
import sk.ferinaf.secrethitler.fragments.PolicyFragment
import sk.ferinaf.secrethitler.widgets.GameBottomNavigation
import sk.ferinaf.secrethitler.widgets.PolicyCard

class GameActivity : BaseActivity() {

    override var fullScreen = true
    override var askToGetBack = true

    private val gameFragment = GameFragment()
    val policyFragment = PolicyFragment()
    private val playersFragment = PlayersFragment()

    private var mGameFragment: Fragment = gameFragment
    private var mPolicyFragment: Fragment = policyFragment
    private var mPlayersFragment: Fragment = playersFragment

    private var activeFragment: Fragment = gameFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game)

        game_navigation?.onSelectItem(GameBottomNavigation.NavigationItem.GAME)

        game_navigation.onSelect = { item ->
            when (item) {
                GameBottomNavigation.NavigationItem.GAME -> {
                    selectFragment(mGameFragment)
                }
                GameBottomNavigation.NavigationItem.POLICY -> {
                    selectFragment(mPolicyFragment)
                }
                GameBottomNavigation.NavigationItem.PLAYERS -> {
                    selectFragment(mPlayersFragment)
                }
            }
        }

        supportFragmentManager.beginTransaction().add(R.id.game_main_container, mPlayersFragment, "3").hide(mPlayersFragment).commit()
        supportFragmentManager.beginTransaction().add(R.id.game_main_container, mPolicyFragment, "2").hide(mPolicyFragment).commit()
        supportFragmentManager.beginTransaction().add(R.id.game_main_container, mGameFragment, "1").commit()
    }

    private fun selectFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction().hide(activeFragment).show(fragment).commit()
        activeFragment = fragment
    }

    fun showVetoPresident(pass: PolicyCard.PolicyType, discard: PolicyCard.PolicyType) {
        val vetoFragment = ConfirmVetoFragment(pass, discard)
        supportFragmentManager.beginTransaction().add(R.id.game_main_container, vetoFragment, "2").hide(activeFragment).commit()
        mPolicyFragment = vetoFragment
        activeFragment = mPolicyFragment
    }

    fun hideVetoPresident() {
        supportFragmentManager.beginTransaction().remove(mPolicyFragment).commit()
        mPolicyFragment = policyFragment
        selectFragment(mPolicyFragment)
    }

}
