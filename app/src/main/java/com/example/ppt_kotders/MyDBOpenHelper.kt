
import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteException
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import com.example.ppt_kotders.Ubicacion
import com.example.ppt_kotders.models.JuegoModelo
import com.example.ppt_kotders.models.JugadorModelo
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.schedulers.Schedulers

class MyDBOpenHelper (context: Context, factory: SQLiteDatabase.CursorFactory?) :
    SQLiteOpenHelper(context, DATABASE_NAME, factory, DATABASE_VERSION) {
    val TAG = "SQLite"

    companion object {

        val DATABASE_VERSION = 2
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
        val COLUMN_LATITUD = "latitud"
        val COLUMN_LONGITUD = "longitud"
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
                    "$COLUMN_JUGADOR_NOMBRE VARCHAR (60)," +
                    "$COLUMN_RESULTADO VARCHAR(10), " +
                    "$COLUMN_FECHAHORA DATETIME DEFAULT CURRENT_TIMESTAMP," +
                    "$COLUMN_LATITUD DOUBLE," +
                    "$COLUMN_LONGITUD DOUBLE)"
            db.execSQL(createTablePartidas)
        } catch (e: SQLiteException) {
            Log.e("$TAG (onCreate)", e.message.toString())
        }
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        if ((oldVersion < newVersion) && (newVersion == 2)){
            db?.execSQL("ALTER TABLE $TABLE_PARTIDAS ADD COLUMN $COLUMN_LATITUD DOUBLE")
            db?.execSQL("ALTER TABLE $TABLE_PARTIDAS ADD COLUMN $COLUMN_LONGITUD DOUBLE")
        }

    }

    override fun onOpen(db: SQLiteDatabase?) {
        super.onOpen(db)
        Log.d("${ContentValues.TAG} (onOpen)", "¡¡Base de datos abierta!!")
    }

    // Agrega un nuevo jugador a la BD
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

    // Agrega un nuevo juego a la BD
    fun addGame(nombreJugador: String, result: String): Observable<Unit> {
        return Observable.create { emitter ->
            try {
                Log.d("---- GPS", "LAT: ${Ubicacion.getLatitud()} - LON : ${Ubicacion.getLongitud()} ")
                val data = ContentValues()
                data.put(COLUMN_JUGADOR_NOMBRE, nombreJugador)
                data.put(COLUMN_RESULTADO, result)
                data.put(COLUMN_LATITUD, Ubicacion.getLatitud())
                data.put(COLUMN_LONGITUD, Ubicacion.getLongitud())

                // Se abre la BD en modo escritura
                val db = this.writableDatabase
                db.insert(TABLE_PARTIDAS, null, data)
                db.close()

                emitter.onNext(Unit)
                emitter.onComplete()
            } catch (e: Exception) {
                emitter.onError(e)
            }
        }.subscribeOn(Schedulers.io())
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

    fun getUserIDWithRX(playername: String): Observable<Int> { // Devuelve un objeto vacio sino encuentra uno
        return Observable.create { emitter ->
            try {
                var id = 0
                val db = this.readableDatabase
                val columnas = arrayOf(COLUMN_ID, COLUMN_NOMBRE, COLUMN_PUNTOS)
                val condicion = "$COLUMN_NOMBRE = ?"
                val valoresCondicion = arrayOf(playername)
                val cursor = db.query(TABLE_JUGADORES, columnas, condicion, valoresCondicion, null, null, null)
                if (cursor.moveToFirst()) {
                    val columnIndex = cursor.getColumnIndex(COLUMN_ID)
                    if (columnIndex != -1) {
                        id = cursor.getInt(columnIndex)
                        cursor.close()
                        db.close()
                        emitter.onNext(id)
                        emitter.onComplete()
                    }
                }
                cursor.close()
                db.close()
            } catch (e: Exception) {
                emitter.onError(e)
            }
        }.subscribeOn(Schedulers.io()) // sino devuelve vacio
    }

    fun getUser(playerId: Int): Observable<JugadorModelo> {
        return Observable.create { emitter ->
            try {
                var jugadorModelo: JugadorModelo? = null
                val db = this.readableDatabase
                val columnas = arrayOf(COLUMN_ID, COLUMN_NOMBRE, COLUMN_PUNTOS)
                val condicion = "$COLUMN_ID = ?"
                val valoresCondicion = arrayOf(playerId.toString())
                val cursor = db.query(TABLE_JUGADORES, columnas, condicion, valoresCondicion, null, null, null)

                if (cursor.moveToFirst()) {
                    val columnIndex = cursor.getColumnIndex(COLUMN_ID)
                    val nameIndex = cursor.getColumnIndex(COLUMN_NOMBRE)
                    val puntosIndex = cursor.getColumnIndex(COLUMN_PUNTOS)

                    if (columnIndex != -1) {
                        val id = cursor.getInt(columnIndex)
                        val nombre = cursor.getString(nameIndex)
                        val puntos = cursor.getInt(puntosIndex)
                        jugadorModelo = JugadorModelo(id, nombre, puntos)

                        cursor.close()
                        db.close()

                        emitter.onNext(jugadorModelo)
                        emitter.onComplete()
                    }
                }
                cursor.close()
                db.close()

                jugadorModelo = JugadorModelo(0, "", 0)
                emitter.onNext(jugadorModelo)
                emitter.onComplete()
            } catch (e: Exception) {
                emitter.onError(e)
            }
        }.subscribeOn(Schedulers.io())
    }

    fun updatePoints(jugadorModelo: JugadorModelo): Observable<Unit> {
        return Observable.create { emitter ->
            try {
                val newpoints = jugadorModelo.puntuacion + 1
                val db = this.writableDatabase
                val valores = ContentValues()
                valores.put(COLUMN_PUNTOS, newpoints)
                val condicion = "$COLUMN_ID = ?"
                val valoresCondicion = arrayOf(jugadorModelo.id.toString())
                val filasActualizadas = db.update(TABLE_JUGADORES, valores, condicion, valoresCondicion)
                if (filasActualizadas > 0) {
                    emitter.onNext(Unit)
                    emitter.onComplete()
                } else {
                    emitter.onError(Exception("Error al actualizar la columna"))
                }
                db.close()
            } catch (e: Exception) {
                emitter.onError(e)
            }
        }.subscribeOn(Schedulers.io())
    }

    fun listPlayerGames(idUser: Int): Observable<MutableList<JuegoModelo>> {
        return getUser(idUser)
            .flatMap { jugador ->
                if (jugador != null) {
                    Observable.create<MutableList<JuegoModelo>> { emitter ->
                        try {
                            val db = this.readableDatabase
                            val columnas = arrayOf(COLUMN_RESULTADO, COLUMN_FECHAHORA)
                            val condicion = "$COLUMN_JUGADOR_NOMBRE = ?"
                            val valoresCondicion = arrayOf(jugador.nombre)

                            val cursor = db.query(TABLE_PARTIDAS, columnas, condicion, valoresCondicion, null, null, null)

                            val columnIndexResultado = cursor.getColumnIndex(COLUMN_RESULTADO)
                            val columnIndexFechaHora = cursor.getColumnIndex(COLUMN_FECHAHORA)

                            val list = mutableListOf<JuegoModelo>()

                            while (cursor.moveToNext()) {
                                val resultado = cursor.getString(columnIndexResultado)
                                val fechaHora = cursor.getString(columnIndexFechaHora).toString()

                                val item = JuegoModelo(jugador.nombre, resultado, fechaHora,0.0,0.0)

                                list.add(item)
                            }

                            cursor.close()
                            db.close()

                            emitter.onNext(list)
                            emitter.onComplete()
                        } catch (e: Exception) {
                            emitter.onError(e)
                        }
                    }
                } else {
                    Observable.error(NullPointerException("El jugador es nulo"))
                }
            }
            .subscribeOn(Schedulers.io())
    }

}








