<?php
    require_once 'conexion.php';

    //comprobamos la solicitud:
    if($_SERVER["REQUEST_METHOD"] == "DELETE"){

        //decodificamos el JSON que recibimos, lo suyo es que recibamos el correo
        $body = json_decode(file_get_contents("php://input"), true);

        //almacenamos en una variable el correo que recibimos del body
        $correo = $body['correo'];

        try {
            
            //comprobamos si ese correo está activo en una consulta y la preparamos
            $consulta_activos = "SELECT * FROM activos where correo like :correo";
            $preparacion_activos = $conexion -> prepare($consulta_activos);

            //al parámetro de la consulta le asignamos el correo de la variable
            $preparacion_activos ->bindParam(':correo', $correo);
            $preparacion_activos->execute();

            //si el correo existe...
            if($preparacion_activos->rowCount()>0){
                //ELIMINAMOS AL USUARIO DE LOS ACTIVOS

                $eliminacion = "DELETE FROM activos WHERE correo like :correo";
                $preparacion_eliminacion = $conexion ->prepare($eliminacion);
                $preparacion_eliminacion ->bindParam(':correo', $correo);

                //comprobamos que se haya eliminado correctamente el usuario de los activos
                if ($preparacion_eliminacion->execute()) {
                    print json_encode(
                        array(
                            'status' => '1',
                            'mensaje' => 'Usuario eliminado de la tabla activos correctamente.'
                        )
                    );
                } else {
                    print json_encode(
                        array(
                            'estado' => '2',
                            'mensaje' => 'No se pudo eliminar el usuario de la tabla activos.'
                        )
                    );
                }
            } else {
                print json_encode(
                    array(
                        'estado' => '3',
                        'mensaje' => 'El correo no se encuentra en la tabla activos.'
                    )
                );
            }
        } catch (PDOException $e) {
            print json_encode(
                array(
                    'estado' => '4',
                    'mensaje' => 'Error en la base de datos: ' . $e->getMessage()
                )
            );
        }
    }
?>