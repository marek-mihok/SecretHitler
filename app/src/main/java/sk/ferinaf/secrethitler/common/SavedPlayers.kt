package sk.ferinaf.secrethitler.common

import org.json.JSONArray
import sk.ferinaf.secrethitler.App

object SavedPlayers {

    private const val SAVED_PLAYERS = "saved_players"
    private val sp = App.context.getSharedPreferences("Settings", 0)

    val players: ArrayList<String>
        get() {
            val playersString = sp.getString(SAVED_PLAYERS, "[]")
            return JSONArray(playersString).toArrayList()
        }

    fun addPlayer(name: String): Boolean {
        val playersString = sp.getString(SAVED_PLAYERS, "[]")
        val playersJsonArray = JSONArray(playersString)
        val contains = playersJsonArray.toArrayList<String>().contains(name)

        return if(contains) {
            false
        } else {
            val spEdit = sp.edit()
            playersJsonArray.put(name)
            spEdit.putString(SAVED_PLAYERS, playersJsonArray.toString())
            spEdit.apply()
            true
        }
    }

    fun replacePlayer(name: String) {
        val playersArray = JSONArray(sp.getString(SAVED_PLAYERS, "[]")).toArrayList<String>()
        val idx = playersArray.indexOf(name)
        if (idx >= 0) {
            playersArray[idx] = name
            val spEdit = sp.edit()
            spEdit.putString(SAVED_PLAYERS, playersArray.toString())
            spEdit.apply()
        }
    }

    fun removePlayer(name: String) {
        val playersArray = JSONArray(sp.getString(SAVED_PLAYERS, "[]")).toArrayList<String>()
        if(playersArray.remove(name)) {
            val spEdit = sp.edit()
            spEdit.putString(SAVED_PLAYERS, playersArray.toString())
            spEdit.apply()
        }
    }

    fun replaceList(newList: ArrayList<String>) {
        val spEdit = sp.edit()
        spEdit.putString(SAVED_PLAYERS, newList.toJSONString())
        spEdit.apply()
    }

}