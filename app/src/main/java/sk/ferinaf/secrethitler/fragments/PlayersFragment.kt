package sk.ferinaf.secrethitler.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.fragment_players.*
import sk.ferinaf.secrethitler.R
import sk.ferinaf.secrethitler.adapters.PlayersListAdapter

class PlayersFragment : Fragment() {

    private val playersListAdapter = PlayersListAdapter()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val mView = inflater.inflate(R.layout.fragment_players, container, false)
        return mView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        players_list_recycler_view?.adapter = playersListAdapter
        players_list_recycler_view?.layoutManager = LinearLayoutManager(context)
    }

}

