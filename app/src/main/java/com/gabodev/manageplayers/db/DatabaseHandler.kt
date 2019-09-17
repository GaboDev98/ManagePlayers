package com.gabodev.manageplayers.db

import android.util.Log
import android.content.Context
import android.content.ContentValues
import android.database.sqlite.SQLiteDatabase
import com.gabodev.manageplayers.model.Player
import android.database.sqlite.SQLiteOpenHelper

class DatabaseHandler (context: Context) : SQLiteOpenHelper(context, DB_NAME, null, DB_VERSION) {

    override fun onCreate(db: SQLiteDatabase?) {
        val CREATE_TABLE = "CREATE TABLE $TABLE_NAME " +
                "($ID INTEGER PRIMARY Key, $ID_PLAYER INTEGER, $FIRST_NAME TEXT, $LAST_NAME TEXT)"
        db?.execSQL(CREATE_TABLE)
    }

    override fun onUpgrade(p0: SQLiteDatabase?, p1: Int, p2: Int) {
        // Called when the database needs to be upgraded
    }

    // Inserting (Creating) data
    fun addPlayer(player: Player): Boolean {
        var _success: Long = -1
        var existPlayer: Player = getPlayerById(player.id)
        if (existPlayer.id > 0) {
            Log.v("DB_LOCAL", "ExistDB")
        } else {
            // Create and/or open a database that will be used for reading and writing.
            val db = this.writableDatabase
            val values = ContentValues()
            values.put(ID_PLAYER, player.id_player)
            values.put(FIRST_NAME, player.first_name)
            values.put(LAST_NAME, player.last_name)
            _success = db.insert(TABLE_NAME, null, values)
            db.close()
            Log.v("InsertedID", "$_success")
        }
        return (Integer.parseInt("$_success") != -1)
    }

    // Inserting (Creating) data
    fun updatePlayer(player: Player): Boolean {
        val db = this.writableDatabase
        val values = ContentValues()
        values.put(ID_PLAYER, player.id_player)
        values.put(FIRST_NAME, player.first_name)
        values.put(LAST_NAME, player.last_name)
        val _success = db.update(TABLE_NAME, values, ID + "=?", arrayOf(player.id.toString())).toLong()
        db.close()
        return Integer.parseInt("$_success") != -1
    }

    // Get all players
    fun getPlayerById(id: Int): Player {
        val db = readableDatabase
        val selectALLQuery = "SELECT * FROM $TABLE_NAME WHERE Id = $id LIMIT 1"
        val cursor = db.rawQuery(selectALLQuery, null)
        var player = Player()
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                do {

                    player.id = cursor.getString(cursor.getColumnIndex(ID)).toInt()
                    player.id_player = cursor.getString(cursor.getColumnIndex(ID_PLAYER)).toInt()
                    player.first_name = cursor.getString(cursor.getColumnIndex(FIRST_NAME))
                    player.last_name = cursor.getString(cursor.getColumnIndex(LAST_NAME))

                } while (cursor.isFirst)
            }
        }
        cursor.close()
        db.close()
        return player
    }

    // Get all players
    fun getAllPlayers(): List<Player> {
        val db = readableDatabase
        val selectALLQuery = "SELECT * FROM $TABLE_NAME LIMIT 25"
        val cursor = db.rawQuery(selectALLQuery, null)
        var listPlayers: MutableList<Player> = ArrayList()
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                do {

                    var player = Player()

                    player.id = cursor.getString(cursor.getColumnIndex(ID)).toInt()
                    player.id_player = cursor.getString(cursor.getColumnIndex(ID_PLAYER)).toInt()
                    player.first_name = cursor.getString(cursor.getColumnIndex(FIRST_NAME))
                    player.last_name = cursor.getString(cursor.getColumnIndex(LAST_NAME))

                    listPlayers.add(player)

                } while (cursor.moveToNext())
            }
        }
        cursor.close()
        db.close()
        return listPlayers
    }

    // Get all players
    fun getAllPlayersByName(name: String): List<Player> {
        val db = readableDatabase
        val selectALLQuery = "SELECT * FROM $TABLE_NAME WHERE $FIRST_NAME LIKE '%$name%' OR $LAST_NAME LIKE '%$name%' LIMIT 25"
        val cursor = db.rawQuery(selectALLQuery, null)
        var listPlayers: MutableList<Player> = ArrayList()
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                do {

                    var player = Player()

                    player.id = cursor.getString(cursor.getColumnIndex(ID)).toInt()
                    player.id_player = cursor.getString(cursor.getColumnIndex(ID_PLAYER)).toInt()
                    player.first_name = cursor.getString(cursor.getColumnIndex(FIRST_NAME))
                    player.last_name = cursor.getString(cursor.getColumnIndex(LAST_NAME))

                    listPlayers.add(player)

                } while (cursor.moveToNext())
            }
        }
        cursor.close()
        db.close()
        return listPlayers
    }

    companion object {
        private const val DB_NAME = "PlayerDB"
        private const val DB_VERSION = 1
        private const val TABLE_NAME = "players"
        private const val ID = "Id"
        private const val ID_PLAYER = "IdPlayer"
        private const val FIRST_NAME = "FirstName"
        private const val LAST_NAME = "LastName"
    }
}
