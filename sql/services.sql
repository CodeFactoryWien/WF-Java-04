-- phpMyAdmin SQL Dump
-- version 4.9.2
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Erstellungszeit: 20. Jan 2020 um 09:14
-- Server-Version: 10.4.10-MariaDB
-- PHP-Version: 7.3.12

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET AUTOCOMMIT = 0;
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Datenbank: `wf_java04_hotel`
--

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `services`
--

CREATE TABLE `services` (
  `servicesID` tinyint(4) NOT NULL,
  `fk_bookingID` tinyint(4) NOT NULL,
  `serviceType` char(14) NOT NULL,
  `serviceDate` date NOT NULL,
  `fk_serviceID` tinyint(4) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Daten für Tabelle `services`
--

INSERT INTO `services` (`servicesID`, `fk_bookingID`, `serviceType`, `serviceDate`, `fk_serviceID`) VALUES
(4, 3, 'movie', '2020-01-17', 1),
(5, 2, 'movie', '2020-01-18', 3),
(13, 3, 'movie', '2020-01-19', 2),
(18, 3, 'movie', '2020-01-19', 6),
(39, 3, 'movie', '2020-01-19', 4),
(40, 1, 'movie', '2020-01-19', 2),
(41, 1, 'wellness', '2020-01-19', 2),
(42, 1, 'minibar', '2020-01-19', 2),
(43, 1, 'minibar', '2020-01-19', 2),
(44, 1, 'minibar', '2020-01-19', 2),
(45, 1, 'minibar', '2020-01-19', 2),
(46, 1, 'wellness', '2020-01-19', 5),
(63, 3, 'movie', '2020-01-19', 4),
(65, 3, 'wellness', '2020-01-20', 3),
(66, 3, 'minibar', '2020-01-20', 2);

--
-- Indizes der exportierten Tabellen
--

--
-- Indizes für die Tabelle `services`
--
ALTER TABLE `services`
  ADD PRIMARY KEY (`servicesID`);

--
-- AUTO_INCREMENT für exportierte Tabellen
--

--
-- AUTO_INCREMENT für Tabelle `services`
--
ALTER TABLE `services`
  MODIFY `servicesID` tinyint(4) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=67;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
