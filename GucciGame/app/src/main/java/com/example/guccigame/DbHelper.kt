import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.guccigame.UsuariosSqlite

class DBHelper(context: Context) : SQLiteOpenHelper(context, "game.db", null, 1) {

    // Definir la tabla de usuarios
    override fun onCreate(db: SQLiteDatabase?) {
        val createTableQuery = """
            CREATE TABLE IF NOT EXISTS Usuarios (
                correo TEXT PRIMARY KEY,
                puntuacion INTEGER DEFAULT 0
            )
        """
        db?.execSQL(createTableQuery)
    }

    // Si la versión de la base de datos cambia, se actualizará la estructura
    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("DROP TABLE IF EXISTS Usuarios")
        onCreate(db)
    }

    fun insertarUsuario(usuario: UsuariosSqlite): Long {
        val db = writableDatabase
        val values = ContentValues().apply {
            put("correo", usuario.correo)
            put("puntuacion", usuario.puntuacion)
        }

        return db.insert("Usuarios", null, values)
    }

    fun obtenerUsuarios(): List<UsuariosSqlite> {
        val db = readableDatabase
        val usuarios = mutableListOf<UsuariosSqlite>()

        //Hay que indicar todos los parámetros, y ponerlos a null si no los usamos en la consulta
        val cursor = db.query(
            "Usuarios",  // Nombre de la tabla
            arrayOf("correo", "puntuacion"),  // Columnas que queremos seleccionar
            null,  // No filtramos filas
            null,  // No filtramos con un WHERE
            null,  // No agrupamos
            null,  // No ordenamos
            null   // Sin límite
        )

        //añadimos lo que recoja en el cursor en la lista de usuarios
        while (cursor.moveToNext()) {
            val correo = cursor.getString(cursor.getColumnIndexOrThrow("correo"))
            val puntuacion = cursor.getInt(cursor.getColumnIndexOrThrow("puntuacion"))
            usuarios.add(UsuariosSqlite(correo, puntuacion))
        }
        cursor.close()

        //Devolvemos la lista
        return usuarios
    }
}
