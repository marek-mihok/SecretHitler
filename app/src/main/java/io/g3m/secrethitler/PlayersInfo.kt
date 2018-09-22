package io.g3m.secrethitler

object PlayersInfo {

    private var playerNames: ArrayList<String> = arrayListOf<String>()
    private var playerRoles: ArrayList<Int> = arrayListOf<Int>()
    private var hitlerKnowsFascists = false

    fun setNames(players: ArrayList<String>){
        this.playerNames = players

        when (this.playerNames.size) {
            5 -> {
                this.playerRoles = arrayListOf(0, 0, 0, 1, 2)
                this.hitlerKnowsFascists = true
            }

            6 -> {
                this.playerRoles = arrayListOf(0, 0, 0, 0, 1, 2)
                this.hitlerKnowsFascists = true
            }

            7 -> {
                this.playerRoles = arrayListOf(0, 0, 0, 0, 1, 1, 2)
                this.hitlerKnowsFascists = false
            }

            8 -> {
                this.playerRoles = arrayListOf(0, 0, 0, 0, 0, 1, 1, 2)
                this.hitlerKnowsFascists = false
            }

            9 -> {
                this.playerRoles = arrayListOf(0, 0, 0, 0, 0, 1, 1, 1, 2)
                this.hitlerKnowsFascists = false
            }

            10 -> {
                this.playerRoles = arrayListOf(0, 0, 0, 0, 0, 0, 1, 1, 1, 2)
                this.hitlerKnowsFascists = false
            }
        }

        this.playerRoles.shuffle()
    }


    fun getFascists(): ArrayList<String> {
        val fascists: ArrayList<String> = arrayListOf<String>()

        for (i in 0..this.playerNames.size - 1) {
            if (this.playerRoles[i] == 1) {
                fascists.add(this.playerNames[i])
            }
        }

        return fascists
    }


    fun getHitler(): String {
        var hitler = ""
        for (i in 0..this.playerNames.size - 1) {
            if (this.playerRoles[i] == 2) {
                hitler = this.playerNames[i]
            }
        }
        return hitler
    }

    fun getPlayerName(i: Int): String {
        return this.playerNames[i]
    }

    fun getPlayerIdentity(i: Int): Int {
        return this.playerRoles[i]
    }

    fun isPlayerFascist(i: Int): Boolean {
        return (((this.playerRoles[i] == 2) && this.hitlerKnowsFascists) || (this.playerRoles[i] == 1))
    }


    // Get array of revealed fascists for i-th player
    fun getRevealedFascists(i: Int): ArrayList<String> {

        val otherFascists = getFascists()
        otherFascists.remove(getPlayerName(i))

        // one hitler, one fascist other are liberals
        if (getPlayersCount() <= 6 && getPlayerIdentity(i) == 2) {
            return arrayListOf("", otherFascists[0], "")
        }

        // return when player is fascist
        if (getPlayerIdentity(i) == 1) {
            when (this.playerNames.size) {
                5 -> return arrayListOf(getHitler(), "", "")
                6 -> return arrayListOf(getHitler(), "", "")
                7 -> return arrayListOf(getHitler(), otherFascists[0], "")
                8 -> return arrayListOf(getHitler(), otherFascists[0], "")
                9 -> return arrayListOf(getHitler(), otherFascists[0], otherFascists[1])
                10 -> return arrayListOf(getHitler(), otherFascists[0], otherFascists[1])
            }
        }

        return arrayListOf("", "", "")
    }

    fun playerKnowsHitler(i: Int): Boolean {
        return this.playerRoles[i] == 1
    }

    fun getPlayersCount(): Int {
        return this.playerNames.size
    }

    fun getPlayerNames(): ArrayList<String>{
        val names = this.playerNames
        return names
    }
}