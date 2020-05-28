package sk.ferinaf.secrethitler.models

data class Player(val name: String) {
    var role: Roles = Roles.LIBERAL
    var notHitlerReveal = false
    var governmentRole: GovernmentRole? = null
    var isMale = true
    var alive = true
    var eligible = true
    var lastVote: Boolean? = null
    var investigated = false
}
