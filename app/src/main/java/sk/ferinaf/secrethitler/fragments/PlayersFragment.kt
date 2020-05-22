package sk.ferinaf.secrethitler.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.fragment_players.*
import sk.ferinaf.secrethitler.R
import sk.ferinaf.secrethitler.activities.VotingActivity
import sk.ferinaf.secrethitler.adapters.PlayersListAdapter
import sk.ferinaf.secrethitler.common.PlayersInfo
import sk.ferinaf.secrethitler.common.asString
import sk.ferinaf.secrethitler.dialogs.ConfirmDialog
import java.util.*

class PlayersFragment : Fragment() {

    private val playersListAdapter = PlayersListAdapter()

//    private val nominateChancellor by lazy { "NOMINATE CHANCELLOR" }
    private val presidentString by lazy { R.string.president.asString() }
    private val passDeviceTo by lazy { R.string.pass_device_to.asString() }
    private val notYet by lazy { R.string.notYet.asString() }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val mView = inflater.inflate(R.layout.fragment_players, container, false)
        return mView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        players_list_recycler_view?.adapter = playersListAdapter
        players_list_recycler_view?.layoutManager = LinearLayoutManager(context)

        players_bottom_button?.setOnClickListener {
            val passToPresident = ConfirmDialog()

            passToPresident.afterCreated = { passToPresident.apply {
                val mTitle = "$passDeviceTo $presidentString"
                title?.text = mTitle
                detailText?.visibility = View.VISIBLE
                detailText?.text = PlayersInfo.getPresident()?.name?.toUpperCase(Locale.ROOT)
                noButton?.secondaryText = notYet
            }}

            passToPresident.onConfirm = { confirmed ->
                if (confirmed) {
                    // TODO: Pass
                    val voteActivityIntent = Intent(context, VotingActivity::class.java)
                    startActivity(voteActivityIntent)
                }
            }

            fragmentManager?.let {
                passToPresident.show(it, "pass_to_president")
            }

        }
    }

}

