package sk.ferinaf.secrethitler.models

data class Player(val name: String) {
    var role: Roles = Roles.LIBERAL
    var notHitlerReveal = false
    var governmentRole: GovernmentRole? = null
    var alive = true
    var eligible = true
}