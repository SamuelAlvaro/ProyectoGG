<?php
try {
    //code...

require_once 'conexion.php';

$correo = "ejemplo@ejemplo.com";
$consulta ="SELECT * FROM usuarios where correo like '$correo'";

$statement = $conexion->prepare($consulta);
$statement -> execute(); //se ejecuta la consulta preparada

//recogemos la consulta
$usuario = $statement->fetchAll(PDO::FETCH_ASSOC);


//devolvemos un JSON con los resultados para recogerlos en la app
echo json_encode([
    "status" => "success",
    "data" => $usuario
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
} catch (PDOException $e) {
    echo json_encode([
        "status" => "error",
        "message" => $e->getMessage()
    ]);
}

?>