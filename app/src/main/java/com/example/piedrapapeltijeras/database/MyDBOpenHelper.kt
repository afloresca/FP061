package com.example.piedrapapeltijeras.database

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteException
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log

class MyDBOpenHelper (context: Context, factory: SQLiteDatabase.CursorFactory?) :
SQLiteOpenHelper(context, DATABASE_NAME, factory, DATABASE_VERSION) {
    val TAG = "SQLite"
    companion object {
        val DATABASE_VERSION = 1
        val DATABASE_NAME = "rps.db"

        val TABLE_PLAYERS = "Players"
        val COLUMN_PLAYER_ID = "player_id"
        val COLUMN_NAME = "player_name"
        val COLUMN_COINS = "coins"

        val TABLE_GAMES = "Games"
        val COLUMN_GAME_ID = "game_id"
        val COLUMN_PLAYER_GESTURE = "player_gesture"
        val COLUMN_OPPONENT_GESTURE = "opponent_gesture"
        val COLUMN_RESULT = "result"
        val COLUMN_COINS_CHANGE = "coins_change"
        val COLUMN_TIMESTAMP = "timestamp"
    }

    /**
     * Este método es llamado cuando se crea la base de datos por primera vez.
     * Debe producirse la creación de todas las tablas que formen la BD
     */

    override fun onCreate(db: SQLiteDatabase?) {
        try {
            // Crear la tabla Players
            val createTablePlayers = "CREATE TABLE $TABLE_PLAYERS " +
                    "($COLUMN_PLAYER_ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "$COLUMN_NAME TEXT, " +
                    "$COLUMN_COINS INTEGER DEFAULT 0)"
            db!!.execSQL(createTablePlayers)

            // Crear la tabla Games
            val createTableGames = "CREATE TABLE $TABLE_GAMES " +
                    "($COLUMN_GAME_ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "$COLUMN_PLAYER_ID INTEGER," +
                    "$COLUMN_PLAYER_GESTURE TEXT, " +
                    "$COLUMN_OPPONENT_GESTURE TEXT, " +
                    "$COLUMN_RESULT TEXT, " +
                    "$COLUMN_COINS_CHANGE INTEGER DEFAULT 10, " +
                    "$COLUMN_TIMESTAMP DATETIME DEFAULT CURRENT_TIMESTAMP, " +
                    "FOREIGN KEY($COLUMN_PLAYER_ID) REFERENCES $TABLE_PLAYERS($COLUMN_PLAYER_ID))"
            db.execSQL(createTableGames)
        } catch (e: SQLiteException) {
            Log.e("$TAG (onCreate)", e.message.toString())
        }
    }

    /**
     * Este método se invocará cuando la base de datos necesite ser actualizada.
     * Se utiliza para hacer DROPs, añadir tablas o cualquier acción que
     * actualice el esquema de la BD
     */
    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        try {
            val dropTablePlayers = "DROP TABLE IF EXISTS $TABLE_PLAYERS"
            db!!.execSQL(dropTablePlayers)
            onCreate(db)
        } catch (e: SQLiteException) {
            Log.e("$TAG (onUpgrade)", e.message.toString())
        }
    }

    /**
     * Método opciona. Se llamará a este método después de abrir la base de
     * datos, antes de ello, comprobará si está en modo lectura. Se llama justo
     * después de establecer la conexión y crear el esquema
     */
    override fun onOpen(db: SQLiteDatabase?) {
        super.onOpen(db)
        Log.d("$TAG (onOpen)", "¡¡Base de datos abierta!!")
    }

    // Método para añadir un jugador a la tabla players
    fun addPlayer(name: String, coins: String) {
        // Se crea un ArrayMap<>() haciendo uso de ContentValues()
        val data = ContentValues()
        data.put(COLUMN_NAME, name)
        data.put(COLUMN_COINS, coins)

        // Se abre la BD en modo escritura
        val db = this.writableDatabase
        db.insert(TABLE_PLAYERS, null, data)
        db.close()
    }

    // Método para actualizar el número de monedas de un jugador si gana la partida
    fun updateCoinsForPlayer(playerId: Int, coinsChange: Int) {
        //Se abre la BD en modo escritura
        val db = this.writableDatabase

        // Obtiene el número actual de monedas del jugador
        val currentCoins = getCurrentCoinsForPlayer(playerId)

        // Suma la cantidad de monedas preestablecida si gana la partida al número actual de monedas
        val updateCoins = currentCoins + coinsChange

        // Actualiza el número de monedas en la BD
        val data = ContentValues()
        data.put(COLUMN_COINS, updateCoins)
        db.update(TABLE_PLAYERS, data, "$COLUMN_PLAYER_ID = ?", arrayOf(playerId.toString()))
    }

    // Método para obtener el número actual de monedas de un jugador
    private fun getCurrentCoinsForPlayer(playerId: Int) : Int {
        val db = this.readableDatabase
        val cursor = db.query(
            TABLE_PLAYERS,
            arrayOf(COLUMN_COINS),
            "$COLUMN_PLAYER_ID = ?",
            arrayOf(playerId.toString()),
            null,
            null,
            null
        )
        var coins = 0 // Valor predeterminado si no se encuentra la columna

        try {
            if (cursor.moveToFirst()) {
                val columnIndex = cursor.getColumnIndex(COLUMN_COINS)
                if (columnIndex != -1) {
                    coins = cursor.getInt(columnIndex)
                } else {
                    Log.e(TAG, "La columna $COLUMN_COINS no se encontró en el cursor.")
                }
            } else {
                Log.e(TAG, "No se encontró el jugador con ID $playerId.")
            }
        } finally {
            cursor.close()
        }

        return coins
    }

    // Método para eliminar un jugador de la tabla players
    fun delPlayer(playerId: Int) : Int {
        val args = arrayOf(playerId.toString())

        // Se abre la BD en modo escritura
        val db = this.writableDatabase

        // Se puede elegir un sistema u otro
        val result = db.delete(TABLE_PLAYERS, "$COLUMN_PLAYER_ID = ?", args)
        // db.execSQL("DELETE FROM $TABLE_PLAYERS WHERE $COLUMNA_ID = ?", args)

        db.close()
        return result
    }

    // Método para añadir una partida en la tabla games
    fun addGame(playerId: Int, playerGesture: String, opponentGesture: String, result: String, coinsChange: Int, timestamp: String) {
        // Se crea un ArrayMap<>() haciendo uso de ContentValues()
        val data = ContentValues()
        data.put(COLUMN_PLAYER_ID, playerId)
        data.put(COLUMN_PLAYER_GESTURE, playerGesture)
        data.put(COLUMN_OPPONENT_GESTURE, opponentGesture)
        data.put(COLUMN_RESULT, result)
        data.put(COLUMN_COINS_CHANGE, coinsChange)
        data.put(COLUMN_TIMESTAMP, timestamp)

        // Se abre la BD en modo escritura
        val db = this.writableDatabase
        db.insert(TABLE_GAMES, null, data)
        db.close()
    }

}