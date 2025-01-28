<?php

//ESTE PHP TRATA DE AÑADIR A UN USUARIO COMO ACTIVO...
require_once 'conexion.php';

if ($_SERVER["REQUEST_METHOD"] == "POST") {
    // Decodificamos el formato JSON que recibe
    $body = json_decode(file_get_contents("php://input"), true);

    // Extraemos los datos del JSON QUE SE ENVIA, PERO SÓLO QUEREMOS EL CORREO Y EL NIVEL

    $correo = $body['correo'];
    $nivel = $body['nivel'];
    $passwd = $body['passwd']


    try {
    
        // Preparar consulta
        $consulta = "INSERT INTO activos (correo, maxnivel) VALUES (:correo, :nivel, :passwd)";
        $statement = $conexion->prepare($consulta);

        // Asignar parámetros
        
        $statement->bindParam(':correo', $correo);
        $statement->bindParam(':nivel', $nivel);
        $statement->bindParam(':passwd', $passwd);

        // Ejecutar consulta
        if ($statement->execute()) {
            // Código exitoso
            print json_encode(
                array(
                    'status' => '10',
                    'mensaje' => 'El usuario esta activo!!'
                )
            );
        } else {
            // Error al insertar
            print json_encode(
                array(
                    'estado' => '20',
                    'mensaje' => 'No se pudo agregar el usuario en Activos'
                )
            );
        }
    } catch (PDOException $e) {
        // Manejo de errores
        print json_encode(
            array(
                'estado' => '3',
                'mensaje' => 'Error en la base de datos: ' . $e->getMessage()
            )
        );
    }
}
?>
