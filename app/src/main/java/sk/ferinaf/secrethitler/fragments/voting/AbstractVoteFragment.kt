package sk.ferinaf.secrethitler.fragments.voting

import androidx.fragment.app.Fragment
import sk.ferinaf.secrethitler.models.Player

abstract class AbstractVoteFragment : Fragment() {
    abstract var votingPlayer: Player?
    abstract var nominee: Player?
}