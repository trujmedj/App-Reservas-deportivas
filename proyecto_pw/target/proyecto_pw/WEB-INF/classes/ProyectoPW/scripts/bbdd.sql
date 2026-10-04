-- phpMyAdmin SQL Dump
-- version 2.7.0-pl2
-- http://www.phpmyadmin.net
-- 
-- Servidor: oraclepr.uco.es
-- Version del servidor: 5.1.73
-- Version de PHP: 5.3.3
-- 
-- Base de datos: `i22pagag`
-- 
-- --------------------------------------------------------


-- 
-- Estructura de tabla para la tabla `Jugador`
-- 

CREATE TABLE Jugador (
    email VARCHAR(50) PRIMARY KEY,          
    nombre VARCHAR(50) NOT NULL,            
    apellidos VARCHAR(50) NOT NULL,         
    fechaNacimiento DATE NOT NULL,          
    fechaInscripcion DATE NOT NULL,         
    contrasena VARCHAR(255) NOT NULL,         
    rol ENUM('jugador', 'admin') NOT NULL DEFAULT 'jugador' 
);


-- 
-- Estructura de tabla para la tabla `Material`
-- 

CREATE TABLE Material (
    idMaterial INT PRIMARY KEY AUTO_INCREMENT,
    tipo ENUM('PELOTAS', 'CANASTAS', 'CONOS') NOT NULL,
    usoExterior BOOLEAN NOT NULL,
    estado ENUM('DISPONIBLE', 'RESERVADO', 'MAL_ESTADO') NOT NULL
);

-- 
-- Estructura de tabla para la tabla `Pista`
-- 

CREATE TABLE Pista (
    nombre VARCHAR(50) PRIMARY KEY,
    disponible BOOLEAN NOT NULL,
    esExterior BOOLEAN NOT NULL,
    tamano ENUM('MINIBASKET', 'ADULTOS', 'TRES_VS_TRES') NOT NULL,
    maxJugadores INT NOT NULL
);

-- 
-- Estructura de tabla para la relación entre `Pista` y `Material`
-- 

CREATE TABLE Pista_Material (
    idMaterial INT NOT NULL,
    nombrePista VARCHAR(50) NOT NULL,
    PRIMARY KEY (idMaterial, nombrePista),
    FOREIGN KEY (idMaterial) REFERENCES Material(idMaterial) ON DELETE CASCADE ON UPDATE CASCADE,
    FOREIGN KEY (nombrePista) REFERENCES Pista(nombre) ON DELETE CASCADE ON UPDATE CASCADE
);

-- 
-- Estructura de tabla para la tabla `Reserva`
-- 

CREATE TABLE Reserva (
    emailJugador VARCHAR(50) NOT NULL,
    fechaHoraReserva DATETIME NOT NULL,
    duracionMinutos INT NOT NULL,
    nombrePista VARCHAR(50) NOT NULL,
    precio FLOAT NOT NULL,
    descuento FLOAT,
    numAdultos INT,
    numNinos INT,
    FOREIGN KEY (emailJugador) REFERENCES Jugador(email) ON DELETE CASCADE,
    FOREIGN KEY (nombrePista) REFERENCES Pista(nombre) ON DELETE CASCADE
    bono BOOLEAN NOT NULL 
);

-- 
-- Estructura de tabla para la tabla `Bono`
-- 

CREATE TABLE Bono (
    idBono INT PRIMARY KEY AUTO_INCREMENT,
    emailJugador VARCHAR(50) NOT NULL,
    fechaCreacion DATE,
    fechaCaducidad DATE,
    sesionesRestantes INT DEFAULT 5, 
    tipoBono INT NOT NULL,           
    FOREIGN KEY (emailJugador) REFERENCES Jugador(email) ON DELETE CASCADE
);
