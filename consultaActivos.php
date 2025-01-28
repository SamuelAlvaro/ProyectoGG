<?php

try {
    //recogemos todos los activos en este php

    require_once 'conexion.php';

    $consulta ="SELECT * FROM activos";
    /*La variable conexion esta creada desde el archivo de conexion de la base de datos.
        Entonces es nuestro constructor y nos permite preparar la consulta
    */
    $statement = $conexion->prepare($consulta);
    $statement -> execute(); //se ejecuta la consulta preparada

    //recogemos la consulta
    $activos = $statement->fetchAll(PDO::FETCH_ASSOC);

    echo json_encode([
        "status" => "success",
        "data" => $activos
    ]);
    
} catch (PDOException $th) {
    echo json_encode([
        "status" => "error",
        "message" => $e->getMessage()
    ]);
}


?>