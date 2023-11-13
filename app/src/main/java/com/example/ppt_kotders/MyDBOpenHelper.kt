
import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteException
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import com.example.ppt_kotders.JugadorModelo

class MyDBOpenHelper (context: Context, factory: SQLiteDatabase.CursorFactory?) :
    SQLiteOpenHelper(context, DATABASE_NAME, factory, DATABASE_VERSION) {
    val TAG = "SQLite"
    companion object {

        val DATABASE_VERSION = 1
        val DATABASE_NAME = "kot.db"

        val TABLE_JUGADORES = "JUGADORES"
        val COLUMN_ID = "id"
        val COLUMN_NOMBRE = "nombre"
        val COLUMN_PUNTOS = "puntos"

        val TABLE_PARTIDAS = "PARTIDAS"
        val COLUMN_PARTIDA_ID = "id_partida"
        val COLUMN_JUGADOR_NOMBRE = "jugador_nombre"
        val COLUMN_RESULTADO = "resultado"
        val COLUMN_FECHAHORA = "fechahora"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        try {
            // Crear la tabla Jugadores
            val createTableJugadores = "CREATE TABLE $TABLE_JUGADORES " +
                    "($COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "$COLUMN_NOMBRE VARCHAR(60), " +
                    "$COLUMN_PUNTOS INTEGER DEFAULT 0)"
            db!!.execSQL(createTableJugadores)

            // Crear la tabla Partidas
            val createTablePartidas = "CREATE TABLE $TABLE_PARTIDAS" +
                    "(${COLUMN_PARTIDA_ID}_ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "${COLUMN_JUGADOR_NOMBRE} VARCHAR (60)," +
                    "$COLUMN_RESULTADO VARCHAR(10), " +
                    "$COLUMN_FECHAHORA DATETIME DEFAULT CURRENT_TIMESTAMP)"

            db.execSQL(createTablePartidas)
        } catch (e: SQLiteException) {
            Log.e("$TAG (onCreate)", e.message.toString())
        }
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        TODO("Not yet implemented")
    }

    override fun onOpen(db: SQLiteDatabase?) {
        super.onOpen(db)
        Log.d("${ContentValues.TAG} (onOpen)", "¡¡Base de datos abierta!!")
    }

    fun addPlayer(nombre: String, puntos: Int) {
        // Se crea un ArrayMap<>() haciendo uso de ContentValues()
        val data = ContentValues()
        data.put(COLUMN_NOMBRE, nombre)
        data.put(COLUMN_PUNTOS, puntos)

        // Se abre la BD en modo escritura
        val db = this.writableDatabase
        db.insert(TABLE_JUGADORES, null, data)
        db.close()
    }

    fun addGame(nombreJugador: String, result: String) {
        // Se crea un ArrayMap<>() haciendo uso de ContentValues()
        val data = ContentValues()
        data.put(COLUMN_JUGADOR_NOMBRE, nombreJugador)
        data.put(COLUMN_RESULTADO, result)

        // Se abre la BD en modo escritura
        val db = this.writableDatabase
        db.insert(TABLE_PARTIDAS, null, data)
        db.close()
    }

    fun getUserID(playername: String):Int{ // Devuelve un objeto vacio sino encuentra uno
        var id = 0;
        val db = this.readableDatabase;
        val columnas = arrayOf(COLUMN_ID, COLUMN_NOMBRE, COLUMN_PUNTOS)
        val condicion = "$COLUMN_NOMBRE = ?"
        val valoresCondicion = arrayOf(playername)
        val cursor = db.query(TABLE_JUGADORES, columnas, condicion, valoresCondicion, null, null, null)
        if (cursor.moveToFirst()) {
            val columnIndex = cursor.getColumnIndex(COLUMN_ID)
            if(columnIndex!=-1){
                id = cursor.getInt(columnIndex)
                cursor.close()
                db.close()
                return id
            }
        }
        cursor.close()
        db.close()
        return id; // sino devuelve vacio

    }

    fun getUser(playerId:Int):JugadorModelo{
        var jugadorModelo : JugadorModelo?=null
        val db = this.readableDatabase
        val columnas = arrayOf(COLUMN_ID, COLUMN_NOMBRE, COLUMN_PUNTOS)
        val condicion = "$COLUMN_ID = ?" // Puedes ajustar esta condición según tus necesidades
        val valoresCondicion = arrayOf(playerId.toString()) // Puedes ajustar el valor según el jugador que estás buscando
        val cursor = db.query(TABLE_JUGADORES, columnas, condicion, valoresCondicion, null, null, null)

        if (cursor.moveToFirst()) {
            val columnIndex = cursor.getColumnIndex(COLUMN_ID);
            val nameIndex = cursor.getColumnIndex(COLUMN_NOMBRE)
            val puntosIndex = cursor.getColumnIndex(COLUMN_PUNTOS)

            if(columnIndex!=-1){

                val id = cursor.getInt(columnIndex)
                val nombre = cursor.getString(nameIndex)
                val puntos = cursor.getInt(puntosIndex)
                jugadorModelo = JugadorModelo(id,nombre,puntos)

                cursor.close()
                db.close()

                return jugadorModelo;

            }

        }
        cursor.close()
        db.close()

        jugadorModelo = JugadorModelo(0,"",0);

        return jugadorModelo;
    }

    fun updatePoints(jugadorModelo: JugadorModelo){

        val newpoints = jugadorModelo.puntuacion + 1 // Los puntos del jugador han cambiado
        val db=this.writableDatabase;

        val valores = ContentValues()
        valores.put(COLUMN_PUNTOS, newpoints)
        val condicion = "$COLUMN_ID = ?"
        val valoresCondicion = arrayOf(jugadorModelo.id.toString())
        val filasActualizadas = db.update(TABLE_JUGADORES, valores, condicion, valoresCondicion)
        if (filasActualizadas > 0) {
            print( " 1 row afected ")
        }else {
            print("Error Update Row ")
        }

            db.close()

    }

    /*fun obtenerDatosPartidas(): List<Pair<String, String>> {

        val datosPartidas = mutableListOf<Pair<String, String>>()

        try {
            val db = this.readableDatabase
            val query = "SELECT $COLUMN_RESULTADO, $COLUMN_FECHAHORA FROM $TABLE_PARTIDAS"
            val cursor = db.rawQuery(query, null)

            if (cursor.moveToFirst()) {
                do {
                    val resultado = cursor.getString(cursor.getColumnIndex(COLUMN_RESULTADO))
                    val fechaHora = cursor.getString(cursor.getColumnIndex(COLUMN_FECHAHORA))

                    val estadoFechaHora = Pair(resultado, fechaHora)
                    datosPartidas.add(estadoFechaHora)
                } while (cursor.moveToNext())
            }

            cursor.close()
            db.close()

        } catch (e: SQLiteException) {
            Log.e(TAG, "Error al obtener datos de partidas: ${e.message}")
        }

        return datosPartidas
    }*/
}







