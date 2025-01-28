<?php
try {
    //code...

include 'conexion.php';

$correo = "ejemplo@ejemplo.com";
$consulta ="SELECT * FROM preguntas";

$statement = $conexion->prepare($consulta);
$statement -> execute(); //se ejecuta la consulta preparada

//recogemos la consulta
$preguntas = $statement->fetchAll(PDO::FETCH_ASSOC);

//devolvemos un JSON con los resultados para recogerlos en la app
echo json_encode([
    "status" => "success",
    "data" => $preguntas
]);

//en caso de que no funcione correctamente
} catch (PDOException $e) {
    echo json_encode([
        "status" => "error",
        "message" => $e->getMessage()
    ]);
}

?>