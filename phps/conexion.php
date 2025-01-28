<?php

//FICHERO DE CONEXIÓN DE LA BASE DE DATOS

$servidor = "localhost";
$usuario = "root";
$password = "";
$database = "juegos";

//realizamos la conexión con los atributos

try {
    $conexion = new PDO("mysql:host=$servidor; dbname=$database", $usuario, $password);
    $conexion->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);
    echo "Conexion realizada";
} catch (PDOException $th) {
    //throw $th;
}

?>