package sk.ferinaf.secrethitler.common

import sk.ferinaf.secrethitler.fragments.PlayersFragment
import sk.ferinaf.secrethitler.widgets.PolicyCard

object GameState {
    var drawPile: ArrayList<PolicyCard.PolicyType> = arrayListOf()
    var discardPile: ArrayList<PolicyCard.PolicyType> = arrayListOf()

    var enactedFascist = 0
    var enactedLiberal = 0
    var electionTracker = 0

    private var pileInitiated = false

    var beforeSpecialEvent1 = true
    var beforeSpecialEvent2 = true
    var beforeSpecialEvent3 = true
    var beforeSpecialEvent4 = true
    var beforeSpecialEvent5 = true

    fun resetDefault() {
        drawPile.clear()
        discardPile.clear()
        pileInitiated = false
        enactedFascist = 0
        enactedLiberal = 0
        electionTracker = 0
        beforeSpecialEvent1 = true
        beforeSpecialEvent2 = true
        beforeSpecialEvent3 = true
        beforeSpecialEvent4 = true
        beforeSpecialEvent5 = true
    }

    var onVetoApplied: ()->Unit = {}
    var onElectionTrackerAdvance: ()->Unit = {}
    var onElectionTrackerFail: (type: PolicyCard.PolicyType)->Unit = {}
    var onElectionTrackerRestart: ()->Unit = {}
    var onLiberalEnacted: ()->Unit = {}
    var onFascistEnacted: ()->Unit = {}

    fun isVetoAllowed(): Boolean = enactedFascist == 5

    fun enactPolicy(type: PolicyCard.PolicyType) {
        when (type) {
            PolicyCard.PolicyType.FASCIST -> {
                enactedFascist += 1
                onFascistEnacted()
            }
            PolicyCard.PolicyType.LIBERAL -> {
                enactedLiberal += 1
                onLiberalEnacted()
            }
        }
    }


    fun discardPolicy(type: PolicyCard.PolicyType) {
        discardPile.add(type)
    }


    fun vetoApplied() {
        onVetoApplied()
        advanceElectionTracker()
    }


    fun initDrawPile() {
        for (i in 1..6) {
            drawPile.add(PolicyCard.PolicyType.LIBERAL)
        }

        for (i in 1..11) {
            drawPile.add(PolicyCard.PolicyType.FASCIST)
        }

        drawPile.shuffle()
        pileInitiated = true
    }


    private fun checkDrawPile() {
        if (!pileInitiated) {
            initDrawPile()
        }

        if (drawPile.size < 3) {
            drawPile.addAll(discardPile)
            discardPile.clear()
            drawPile.shuffle()
        }
    }


    fun drawCards(): List<PolicyCard.PolicyType> {
        checkDrawPile()

        val drawn = drawPile.take(3)
        drawPile = ArrayList(drawPile.drop(3))

        return drawn
    }


    fun peekCards(): List<PolicyCard.PolicyType> {
        checkDrawPile()
        return drawPile.take(3)
    }


    fun restartElectionTracker() {
        if (electionTracker != 0) {
            onElectionTrackerRestart()
            electionTracker = 0
        }
    }


    private fun enactFirst(): PolicyCard.PolicyType {
        checkDrawPile()
        val drawn = drawPile[0]
        drawPile = ArrayList(drawPile.drop(1))

        when (drawn) {
            PolicyCard.PolicyType.FASCIST -> {
                enactedFascist += 1
            }
            PolicyCard.PolicyType.LIBERAL -> {
                enactedLiberal += 1
            }
        }
        return drawn
    }


    fun advanceElectionTracker() {
        electionTracker += 1
        if (electionTracker == 3) {
            val type = enactFirst()
            onElectionTrackerFail(type)
            electionTracker = 0
        } else {
            onElectionTrackerAdvance()
        }
    }

    enum class PresidentActions {
        INVESTIGATE, SPECIAL_ELECTION, PEEK_POLICY, EXECUTION
    }

    fun getCurrentAction(): PresidentActions? {
        return if (enactedFascist == 1 && beforeSpecialEvent1) {
            if (PlayersInfo.players.size > 8) {
                PresidentActions.INVESTIGATE
            } else { null }
        } else if (enactedFascist == 2 && beforeSpecialEvent2) {
            if (PlayersInfo.players.size > 6) {
                PresidentActions.INVESTIGATE
            } else { null }
        } else if (enactedFascist == 3 && beforeSpecialEvent3) {
            if (PlayersInfo.players.size > 6) {
                PresidentActions.SPECIAL_ELECTION
            } else {
                PresidentActions.PEEK_POLICY
            }
        } else if (enactedFascist == 4 && beforeSpecialEvent4) {
            PresidentActions.EXECUTION
        } else if (enactedFascist == 5 && beforeSpecialEvent5) {
            PresidentActions.EXECUTION
        } else {
            null
        }
    }
}