package sk.ferinaf.secrethitler.common

import sk.ferinaf.secrethitler.widgets.PolicyCard

object GameState {
    var drawPile: ArrayList<PolicyCard.PolicyType> = arrayListOf()
    var discardPile: ArrayList<PolicyCard.PolicyType> = arrayListOf()

    var enactedFascist = 0
    var enactedLiberal = 0

    fun isVetoAllowed(): Boolean = enactedFascist == 5

    private var pileInitiated = false

    fun enactPolicy(type: PolicyCard.PolicyType) {
        when (type) {
            PolicyCard.PolicyType.FASCIST -> {
                enactedFascist += 1
            }
            PolicyCard.PolicyType.LIBERAL -> {
                enactedLiberal += 1
            }
        }
    }

    fun discardPolicy(type: PolicyCard.PolicyType) {
        discardPile.add(type)
    }

    fun vetoApplied() {}

    private fun initDrawPile() {
        for (i in 1..6) {
            drawPile.add(PolicyCard.PolicyType.LIBERAL)
        }

        for (i in 1..11) {
            drawPile.add(PolicyCard.PolicyType.FASCIST)
        }

        drawPile.shuffle()
    }

    fun drawCards(): List<PolicyCard.PolicyType> {
        if (!pileInitiated) {
            initDrawPile()
        }

        if (drawPile.size < 3) {
            drawPile.addAll(discardPile)
            discardPile.clear()
            drawPile.shuffle()
        }

        val drawn = drawPile.take(3)
        drawPile = ArrayList(drawPile.drop(3))

        return drawn
    }
}