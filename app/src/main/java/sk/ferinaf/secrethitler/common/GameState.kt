package sk.ferinaf.secrethitler.common

import sk.ferinaf.secrethitler.widgets.PolicyCard

object GameState {
    var vetoAllowed = false

    var drawPile: ArrayList<PolicyCard.PolicyType> = arrayListOf()
    var discardPile: ArrayList<PolicyCard.PolicyType> = arrayListOf()

    var enactedFascist = 0
    var enactedLiberal = 0
}