<?php
require_once 'conexion.php';

if ($_SERVER["REQUEST_METHOD"] == "POST") {
    // Decodificamos el formato JSON que recibe
    $body = json_decode(file_get_contents("php://input"), true);

    // Extraemos los datos del JSON
    $correo = $body['correo'];
    $passwd = $body['passwd'];
    $nivel = $body['nivel'];
    $nombre = $body['nombre'];

    try {
        // Verificamos si el correo ya está en la tabla 'activos'
        $query_activos = "SELECT * FROM activos WHERE correo = :correo";
        $statement_activos = $conexion->prepare($query_activos);
        $statement_activos->bindParam(':correo', $correo);
        $statement_activos->execute();
        
        // Verificamos si el correo ya está en la tabla 'usuarios'
        $query_usuarios = "SELECT * FROM usuarios WHERE correo = :correo";
        $statement_usuarios = $conexion->prepare($query_usuarios);
        $statement_usuarios->bindParam(':correo', $correo);
        $statement_usuarios->execute();

        // Si el correo existe en ambos, no lo permitimos
        if ($statement_activos->rowCount() > 0 && $statement_usuarios->rowCount() > 0) {
            print json_encode(
                array(
                    'estado' => '4',
                    'mensaje' => 'El correo ya esta registrado en ambas tablas.'
                )
            );
        } else { //EN los siguientes casos vamos a añadirle en activos
            // Si el correo no está en la tabla 'activos' pero está en 'usuarios', lo actualizamos y además lo ponemos en activos
            if ($statement_activos->rowCount() == 0 && $statement_usuarios->rowCount() > 0) {

                // Actualizar el usuario en la tabla 'usuarios'
                $update_query = "UPDATE usuarios SET passwd = :passwd, nivel = :nivel, nombre = :nombre WHERE correo = :correo";
                $statement_update = $conexion->prepare($update_query);
                $statement_update->bindParam(':passwd', $passwd);
                $statement_update->bindParam(':nivel', $nivel);
                $statement_update->bindParam(':nombre', $nombre);
                $statement_update->bindParam(':correo', $correo);

              

                if ($statement_update->execute()) {
                    print json_encode(
                        array(
                            'status' => '2',
                            'mensaje' => 'El usuario ha sido actualizado correctamente.'
                        )
                    );
                } else {
                    print json_encode(
                        array(
                            'estado' => '21',
                            'mensaje' => 'No se pudo actualizar el usuario.'
                        )
                    );
                }
            } else {
                // Si el correo no está en ninguna de las dos tablas,significa que no ha estado antes: insertamos los datos del usuario en ambas
                $insert_query = "INSERT INTO usuarios (passwd, correo, nivel, nombre) VALUES (:passwd, :correo, :nivel, :nombre)";
                $statement_insert = $conexion->prepare($insert_query);
                $statement_insert->bindParam(':passwd', $passwd);
                $statement_insert->bindParam(':correo', $correo);
                $statement_insert->bindParam(':nivel', $nivel);
                $statement_insert->bindParam(':nombre', $nombre);

                

                if ($statement_insert->execute()) {
                    print json_encode(
                        array(
                            'status' => '1',
                            'mensaje' => 'Se ha creado el usuario correctamente!!'
                        )
                    );
                } else {
                    print json_encode(
                        array(
                            'estado' => '22',
                            'mensaje' => 'No se pudo crear el usuario.'
                        )
                    );
                }
            }
            include 'declararActivo.php';
        }
    } catch (PDOException $e) {
        print json_encode(
            array(
                'estado' => '3',
                'mensaje' => 'Error en la base de datos: ' . $e->getMessage()
            )
        );
    }
}
?>
