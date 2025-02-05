import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.guccigame.UsuariosSqlite

class DBHelper(context: Context) : SQLiteOpenHelper(context, "game.db", null, 1) {

    // Definir la tabla de usuarios con los nuevos campos
    override fun onCreate(db: SQLiteDatabase?) {
        val createTableQuery = """
            CREATE TABLE IF NOT EXISTS Usuarios (
                correo TEXT PRIMARY KEY,
                password TEXT,
                usuario TEXT,
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

    // Método para insertar un nuevo usuario
    fun insertarUsuario(usuario: UsuariosSqlite): Long {
        val db = writableDatabase
        val values = ContentValues().apply {
            put("correo", usuario.correo)
            put("password", usuario.password)  // Añadido el campo de password
            put("usuario", usuario.usuario)    // Añadido el campo de usuario
            put("puntuacion", usuario.puntuacion)
        }
        return db.insert("Usuarios", null, values)
    }

    // Método para obtener todos los usuarios
    fun obtenerUsuarios(): List<UsuariosSqlite> {
        val db = readableDatabase
        val usuarios = mutableListOf<UsuariosSqlite>()

        val cursor = db.query(
            "Usuarios",  // Nombre de la tabla
            arrayOf("correo", "password", "usuario", "puntuacion"),  // Incluimos los nuevos campos
            null,  // No filtramos filas
            null,  // No filtramos con un WHERE
            null,  // No agrupamos
            null,  // No ordenamos
            null   // Sin límite
        )

        // Añadimos lo que recoja en el cursor en la lista de usuarios
        while (cursor.moveToNext()) {
            val correo = cursor.getString(cursor.getColumnIndexOrThrow("correo"))
            val password = cursor.getString(cursor.getColumnIndexOrThrow("password"))  // Obtenemos el password
            val usuario = cursor.getString(cursor.getColumnIndexOrThrow("usuario"))  // Obtenemos el nombre de usuario
            val puntuacion = cursor.getInt(cursor.getColumnIndexOrThrow("puntuacion"))
            usuarios.add(UsuariosSqlite(correo, password, usuario, puntuacion))  // Añadimos los nuevos atributos
        }
        cursor.close()

        return usuarios
    }

    // Método para verificar si un usuario ya existe (con el nuevo nombre de usuario)
    fun usuarioExiste(usuario: String): Boolean {
        val db = readableDatabase
        val cursor: Cursor = db.query(
            "Usuarios",  // Nombre de la tabla
            arrayOf("usuario"),  // Queremos solo la columna de usuario
            "usuario = ?",  // Filtro para buscar por nombre de usuario
            arrayOf(usuario),  // Argumentos para el filtro
            null,  // No agrupamos
            null,  // No ordenamos
            null   // Sin límite
        )

        val existe = cursor.count > 0  // Si encuentra un registro, existe
        cursor.close()

        return existe
    }

    // Método para verificar si un correo ya está registrado
    fun correoExiste(correo: String): Boolean {
        val db = readableDatabase
        val cursor: Cursor = db.query(
            "Usuarios",  // Nombre de la tabla
            arrayOf("correo"),  // Queremos solo la columna de correo
            "correo = ?",  // Filtro para buscar por correo
            arrayOf(correo),  // Argumentos para el filtro
            null,  // No agrupamos
            null,  // No ordenamos
            null   // Sin límite
        )

        val existe = cursor.count > 0  // Si encuentra un registro, existe
        cursor.close()

        return existe
    }

    // Método para validar si un usuario y contraseña coinciden
    fun validarCredenciales(usuario: String, password: String): Boolean {
        val db = readableDatabase
        val cursor = db.query(
            "Usuarios",  // Nombre de la tabla
            arrayOf("usuario", "password"),  // Queremos las columnas de usuario y password
            "usuario = ? AND password = ?",  // Filtro para buscar por usuario y contraseña
            arrayOf(usuario, password),  // Argumentos para el filtro
            null,  // No agrupamos
            null,  // No ordenamos
            null   // Sin límite
        )

        val existe = cursor.count > 0  // Si encuentra un registro, las credenciales son válidas
        cursor.close()

        return existe
    }

}
