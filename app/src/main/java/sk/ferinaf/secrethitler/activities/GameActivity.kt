package sk.ferinaf.secrethitler.activities

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.activity_game.*
import sk.ferinaf.secrethitler.R
import sk.ferinaf.secrethitler.common.BaseActivity
import sk.ferinaf.secrethitler.common.GameState
import sk.ferinaf.secrethitler.common.PlayersInfo
import sk.ferinaf.secrethitler.common.asString
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
    val playersFragment = PlayersFragment()

    private var mGameFragment: Fragment = gameFragment
    private var mPolicyFragment: Fragment = policyFragment
    private var mPlayersFragment: Fragment = playersFragment

    private var activeFragment: Fragment = gameFragment

    private val ruSure by lazy { R.string.exit_game.asString() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game)

        Log.d("state_hitler", PlayersInfo.getHitler().name)

        returnQuestion = ruSure

        game_navigation?.selectItem(GameBottomNavigation.NavigationItem.GAME)

        game_navigation.onSelect = { item ->
            when (item) {
                GameBottomNavigation.NavigationItem.GAME -> {
                    selectFragment(mGameFragment)
                    gameFragment.updateData()
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

        // Set "observers"
        GameState.onLiberalEnacted = {
            Log.d("state", "fascist applied")
        }

        GameState.onFascistEnacted = {
            // Powers is not ignored
            Log.d("state", "fascist applied")
        }

        GameState.onVetoApplied = {
            Log.d("state", "veto applied")
        }

        GameState.onElectionTrackerAdvance = {
            Log.d("state", "tracer advance")
        }

        GameState.onElectionTrackerRestart = {
            Log.d("state", "tracer restart")
        }

        GameState.onElectionTrackerFail = {
            // Any powers granted by policy is ignored now
            Log.d("state", "enacted: $it")
        }
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

    fun switchToBoard(welcomeDialog: GameFragment.WelcomeDialog) {
        selectFragment(mGameFragment)
        game_navigation?.selectItem(GameBottomNavigation.NavigationItem.GAME)
        (mGameFragment as? GameFragment)?.let {
            it.presentWelcomeDialog(welcomeDialog)
        }
    }

    fun switchToEnact() {
        selectFragment(mPolicyFragment)
        game_navigation?.selectItem(GameBottomNavigation.NavigationItem.POLICY)
    }

    fun switchToPlayers() {
        selectFragment(mPlayersFragment)
        game_navigation?.selectItem(GameBottomNavigation.NavigationItem.PLAYERS)
    }

}
