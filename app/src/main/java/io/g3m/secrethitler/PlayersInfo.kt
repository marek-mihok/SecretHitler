package io.g3m.secrethitler

import java.util.*
import kotlin.collections.ArrayList

object PlayersInfo {

    private var playerNames: ArrayList<String> = arrayListOf<String>()
    private var playerRoles: ArrayList<Int> = arrayListOf<Int>()


    // Set names of players and assign them secret roles
    fun setNames(players: ArrayList<String>){
        this.playerNames = players

        /*
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

        */

        this.playerRoles = ArrayList<Int>(Collections.nCopies(getPlayersCount(), 0))
        val hitlerPosition = (0 until this.getPlayersCount()).random()
        this.playerRoles[hitlerPosition] = 2

        addNthFascist(1)

        if (getPlayersCount() > 6) {
            addNthFascist(2)
        }

        if (getPlayersCount() > 8) {
            addNthFascist(3)
        }

    }


    // Help function for assigning roles of fascists
    private fun addNthFascist(n: Int){
        val fascistPos = (0 until this.getPlayersCount()-n).random()

        /*print(playerRoles)
        print("\n")
        print("\n")
        print(fascistPos)
        print("\n")*/

        var counter = -1
        var index = -1
        while (counter != fascistPos){
            index += 1
            if (playerRoles[index] == 0) {counter += 1}
        }

        playerRoles[index] = 1
    }


    // Get array of players (hitler and fascists for other fascist)
    // for i-th player to show in role assigning process
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


    fun getPlayersCount(): Int {
        return this.playerNames.size
    }


    fun getPlayerNames(): ArrayList<String>{
        val names = this.playerNames
        return names
    }


    fun getPlayerRoles(): ArrayList<Int>{
        val roles = this.playerRoles
        return roles
    }


    fun getFascists(): ArrayList<String> {
        val fascists: ArrayList<String> = arrayListOf<String>()

        for (i in 0 until this.playerNames.size) {
            if (this.playerRoles[i] == 1) {
                fascists.add(this.playerNames[i])
            }
        }

        return fascists
    }


    fun getHitler(): String {
        var hitler = ""
        for (i in 0 until this.playerNames.size) {
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
        return this.playerRoles[i] > 0
    }

}