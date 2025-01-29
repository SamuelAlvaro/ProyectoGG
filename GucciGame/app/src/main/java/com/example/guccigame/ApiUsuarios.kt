import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface ApiUsuarios {

    //METODO PARA OBTENER TODOS LOS CLIENTES
    @GET("consultaUsuarios.php")
    fun obtenerClientes(): Call<RespuestaUsuarios>

    //MÉTODO PARA OBTENER UN CLIENTE
    @GET("consultarUnUsuario.php")
    fun obtenerUncliente(
        @Query("correo") correo : String): Call<UsuariosResponse<Any?>>

    //MÉTODO PARA AÑADIR UN CLIENTE
    @POST("añadirUsuario.php")
    fun registrarUsuario(@Body usuario: Usuarios): Call<Void>

}