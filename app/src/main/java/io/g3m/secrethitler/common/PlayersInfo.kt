package io.g3m.secrethitler.common

import java.util.*
import kotlin.collections.ArrayList

object PlayersInfo {

    private var playerNames: ArrayList<String> = arrayListOf<String>()
    private var playerRoles: ArrayList<Int> = arrayListOf<Int>()


    // Set names of players and assign them secret roles
    fun setNames(players: ArrayList<String>){
        playerNames = players
        playerRoles = ArrayList<Int>(Collections.nCopies(getPlayersCount(), 0))

        // Randomly pick hitler position
        val hitlerPosition = (0 until getPlayersCount()).random()
        playerRoles[hitlerPosition] = 2

        // Add fascists depending on player count (1st, 2nd, 3rd ... )
        addNthFascist(1)

        if (getPlayersCount() > 6) {
            addNthFascist(2)
        }

        if (getPlayersCount() > 8) {
            addNthFascist(3)
        }

    }


    // Help function for assigning roles of fascists
    private fun addNthFascist(n: Int) {
        // Number of non fascist players
        val fascistPos = (0 until getPlayersCount() -n).random()
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
    fun getRevealedFascists(playerIndex: Int): ArrayList<String> {

        val otherFascists = getFascists()
        otherFascists.remove(getPlayerName(playerIndex))

        // one hitler, one fascist other are liberals
        if (getPlayersCount() <= 6 && getPlayerIdentity(playerIndex) == 2) {
            return arrayListOf("", otherFascists[0], "")
        }

        // return when player is fascist
        if (getPlayerIdentity(playerIndex) == 1) {
            when (playerNames.size) {
                5 -> return arrayListOf(getHitlerName(), "", "")
                6 -> return arrayListOf(getHitlerName(), "", "")
                7 -> return arrayListOf(getHitlerName(), otherFascists[0], "")
                8 -> return arrayListOf(getHitlerName(), otherFascists[0], "")
                9 -> return arrayListOf(getHitlerName(), otherFascists[0], otherFascists[1])
                10 -> return arrayListOf(getHitlerName(), otherFascists[0], otherFascists[1])
            }
        }

        return arrayListOf("", "", "")
    }


    fun getPlayersCount(): Int {
        return playerNames.size
    }


    fun getPlayerNames(): ArrayList<String>{
        return playerNames
    }


    fun getPlayerRoles(): ArrayList<Int>{
        return playerRoles
    }


    fun getFascists(): ArrayList<String> {
        val fascists: ArrayList<String> = arrayListOf<String>()

        for (i in 0 until playerNames.size) {
            if (playerRoles[i] == 1) {
                fascists.add(playerNames[i])
            }
        }

        return fascists
    }


    fun getHitlerName(): String {
        var hitler = ""
        for (i in 0 until playerNames.size) {
            if (playerRoles[i] == 2) {
                hitler = playerNames[i]
            }
        }
        return hitler
    }


    fun getPlayerName(i: Int): String {
        return playerNames[i]
    }


    fun getPlayerIdentity(i: Int): Int {
        return playerRoles[i]
    }


    fun isPlayerFascist(i: Int): Boolean {
        return playerRoles[i] > 0
    }

}