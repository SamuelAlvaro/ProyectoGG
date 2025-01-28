<?php

try {
    //code...

    require_once 'conexion.php';

    $consulta ="SELECT * FROM activos";
    /*La variable conexion esta creada desde el archivo de conexion de la base de datos.
        Entonces es nuestro constructor y nos permite preparar la consulta
    */
    $statement = $conexion->prepare($consulta);
    $statement -> execute(); //se ejecuta la consulta preparada

    //recogemos la consulta
    $activos = $statement->fetchAll(PDO::FETCH_ASSOC);

    //Este es el JSON que nos devolverá si la consulta se responde OK
    echo json_encode([
        "status" => "success",
        "data" => $activos
    ]);
    

    /*
    //recorremos los usuarios y los mostramos con un foreach
    echo 'Los usuarios son: <br>';
    foreach ($usuarios as $usuario) {
        echo "Nombre: " . $usuario['nombre'] . "<br>";
        echo "Correo: " . $usuario['correo'] . "<br>";
        echo "Nivel: " . $usuario['nivel'] . "<br><br>";
    }
    */
} catch (PDOException $th) {
    echo json_encode([
        "status" => "error",
        "message" => $e->getMessage()
    ]);
}


?>