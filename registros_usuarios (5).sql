-- phpMyAdmin SQL Dump
-- version 3.5.1
-- http://www.phpmyadmin.net
--
-- Servidor: localhost
-- Tiempo de generación: 09-06-2024 a las 21:16:00
-- Versión del servidor: 5.5.24-log
-- Versión de PHP: 5.4.3

SET SQL_MODE="NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;

--
-- Base de datos: `registros_usuarios`
--

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `batallas`
--

CREATE TABLE IF NOT EXISTS `batallas` (
  `Id` int(11) NOT NULL AUTO_INCREMENT,
  `User1` int(11) NOT NULL,
  `User2` int(11) NOT NULL,
  `log` text,
  PRIMARY KEY (`Id`),
  KEY `User1` (`User1`,`User2`),
  KEY `FK_batallas_user2` (`User2`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 AUTO_INCREMENT=1 ;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `inventarios`
--

CREATE TABLE IF NOT EXISTS `inventarios` (
  `Id` int(11) NOT NULL,
  `armaduras` text CHARACTER SET armscii8,
  `armas` text CHARACTER SET armscii8,
  `cartas` text CHARACTER SET armscii8,
  `set_actual` text,
  `Personaje` varchar(10) DEFAULT NULL,
  PRIMARY KEY (`Id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Volcado de datos para la tabla `inventarios`
--

INSERT INTO `inventarios` (`Id`, `armaduras`, `armas`, `cartas`, `set_actual`, `Personaje`) VALUES
(11, NULL, '665fac5a18f33c6523cfbc35', NULL, '665fac5a18f33c6523cfbc35|null', 'p006');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `users`
--

CREATE TABLE IF NOT EXISTS `users` (
  `Id` int(11) NOT NULL AUTO_INCREMENT,
  `Username` varchar(40) DEFAULT NULL,
  `Nombre` varchar(20) DEFAULT NULL,
  `Apellidos` varchar(40) DEFAULT NULL,
  `Fecha_De_Nacimiento` date DEFAULT NULL,
  `Confirmada` tinyint(1) DEFAULT '0',
  `pwd` varchar(255) DEFAULT NULL,
  `Nivel` int(11) DEFAULT NULL,
  `Cantidad_Misiones` int(11) DEFAULT NULL,
  `Monedas` int(11) DEFAULT NULL,
  `email` varchar(20) DEFAULT NULL,
  `tipo` varchar(10) DEFAULT NULL,
  PRIMARY KEY (`Id`),
  UNIQUE KEY `Username` (`Username`),
  KEY `Username_2` (`Username`),
  KEY `Username_3` (`Username`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=12 ;

--
-- Volcado de datos para la tabla `users`
--

INSERT INTO `users` (`Id`, `Username`, `Nombre`, `Apellidos`, `Fecha_De_Nacimiento`, `Confirmada`, `pwd`, `Nivel`, `Cantidad_Misiones`, `Monedas`, `email`, `tipo`) VALUES
(11, 'admin', 'ad', 'as', '1990-01-01', 1, 'JZfzbMbGVoyJXsnohIn1VQ==', 2000, 0, 99499, 'johndoe@example.com', 'Mago');

--
-- Disparadores `users`
--
DROP TRIGGER IF EXISTS `crear_inv`;
DELIMITER //
CREATE TRIGGER `crear_inv` AFTER INSERT ON `users`
 FOR EACH ROW BEGIN
    -- Insertar el nuevo registro en tabla_destino
    INSERT INTO inventarios (id)
    VALUES (NEW.id);
END
//
DELIMITER ;

--
-- Restricciones para tablas volcadas
--

--
-- Filtros para la tabla `batallas`
--
ALTER TABLE `batallas`
  ADD CONSTRAINT `FK_batallas_user1` FOREIGN KEY (`User1`) REFERENCES `users` (`Id`),
  ADD CONSTRAINT `FK_batallas_user2` FOREIGN KEY (`User2`) REFERENCES `users` (`Id`);

--
-- Filtros para la tabla `inventarios`
--
ALTER TABLE `inventarios`
  ADD CONSTRAINT `inventarios_ibfk_1` FOREIGN KEY (`Id`) REFERENCES `users` (`Id`);

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
