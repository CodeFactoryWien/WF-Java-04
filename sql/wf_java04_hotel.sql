-- phpMyAdmin SQL Dump
-- version 4.9.2
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Erstellungszeit: 15. Jan 2020 um 12:24
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
-- Tabellenstruktur für Tabelle `bookings`
--

CREATE TABLE `bookings` (
  `bookingID` tinyint(4) NOT NULL,
  `fk_roomID` tinyint(4) NOT NULL,
  `fk_guestID` tinyint(4) NOT NULL,
  `fk_customerID` tinyint(4) NOT NULL,
  `openAmount` int(11) NOT NULL,
  `bookingFrom` date NOT NULL,
  `bookingUntil` date NOT NULL,
  `bookingCanceled` date NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `customers`
--

CREATE TABLE `customers` (
  `customerID` tinyint(4) NOT NULL,
  `firstName` varchar(55) NOT NULL,
  `lastName` varchar(55) NOT NULL,
  `companyName` int(11) NOT NULL,
  `birthDate` date NOT NULL,
  `address` varchar(55) NOT NULL,
  `zipCode` smallint(6) NOT NULL,
  `country` varchar(55) NOT NULL,
  `phoneNumber` int(11) NOT NULL,
  `email` varchar(55) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `guests`
--

CREATE TABLE `guests` (
  `guestID` smallint(6) NOT NULL,
  `firstName` varchar(55) NOT NULL,
  `lastName` varchar(55) NOT NULL,
  `birthDate` date NOT NULL,
  `address` varchar(155) NOT NULL,
  `zipCode` smallint(6) NOT NULL,
  `country` varchar(55) NOT NULL,
  `phoneNumber` varchar(55) NOT NULL,
  `email` varchar(55) NOT NULL,
  `passportNr` int(11) NOT NULL,
  `fk_customerID` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Daten für Tabelle `guests`
--

INSERT INTO `guests` (`guestID`, `firstName`, `lastName`, `birthDate`, `address`, `zipCode`, `country`, `phoneNumber`, `email`, `passportNr`, `fk_customerID`) VALUES
(0, 'Dummy', 'Account', '1900-01-01', 'Dummystreet 5', 1234, 'Dummyhausen', '+436761234567', 'dummy@domain.com', 147258369, 0);

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `invoices`
--

CREATE TABLE `invoices` (
  `invoiceID` tinyint(4) NOT NULL,
  `fk_bookingID` tinyint(4) NOT NULL,
  `fk_serviceID` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `rooms`
--

CREATE TABLE `rooms` (
  `roomID` int(11) NOT NULL,
  `fk_roomTypeID` tinyint(4) NOT NULL,
  `roomSize` tinyint(4) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `roomtype`
--

CREATE TABLE `roomtype` (
  `roomTypeID` tinyint(4) NOT NULL,
  `roomTypeName` varchar(155) NOT NULL,
  `roomTypeCapacity` tinyint(4) NOT NULL,
  `roomTypeFacilites` varchar(555) NOT NULL,
  `roomTypePrice` double NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Daten für Tabelle `roomtype`
--

INSERT INTO `roomtype` (`roomTypeID`, `roomTypeName`, `roomTypeCapacity`, `roomTypeFacilites`, `roomTypePrice`) VALUES
(1, 'Single Room', 1, 'WLAN, Shower, TV, Safe, ', 59.9),
(2, 'Single Room with Balcony', 1, 'WLAN, Shower, TV, Safe, Balcony', 69.9),
(3, 'Double Room', 2, 'WLAN, Shower, TV, Safe', 99.9),
(4, 'Double Room with Balcony', 2, 'WLAN, Shower, TV, Safe, Balcony', 109.9),
(5, 'Suite', 4, 'WLAN, Bathtub, TV, Safe,', 199.9),
(6, 'Suite with Balcony', 4, 'WLAN, Bathtub, TV, Safe,', 229.9),
(7, 'Superior Double Room', 4, 'WLAN, Bathtub, TV, Safe, Coffeemachine,', 299.9),
(8, 'Superior Double Room', 4, 'WLAN, Bathtub, TV, Safe, Coffeemachine, Balcony', 339.9);

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `services`
--

CREATE TABLE `services` (
  `serviceID` tinyint(4) NOT NULL,
  `fk_bookingID` tinyint(4) NOT NULL,
  `movieID` tinyint(4) NOT NULL,
  `movieDate` date NOT NULL,
  `wellnessID` date NOT NULL,
  `wellnessDate` int(11) NOT NULL,
  `minibarID` int(11) NOT NULL,
  `minibarDate` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `users`
--

CREATE TABLE `users` (
  `userID` tinyint(4) NOT NULL,
  `userIsAdmin` tinyint(1) NOT NULL,
  `userName` varchar(55) NOT NULL,
  `userPassword` varchar(55) NOT NULL,
  `userFirstName` varchar(55) NOT NULL,
  `userLastName` varchar(55) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Indizes der exportierten Tabellen
--

--
-- Indizes für die Tabelle `bookings`
--
ALTER TABLE `bookings`
  ADD PRIMARY KEY (`bookingID`);

--
-- Indizes für die Tabelle `customers`
--
ALTER TABLE `customers`
  ADD PRIMARY KEY (`customerID`);

--
-- Indizes für die Tabelle `guests`
--
ALTER TABLE `guests`
  ADD PRIMARY KEY (`guestID`);

--
-- Indizes für die Tabelle `invoices`
--
ALTER TABLE `invoices`
  ADD PRIMARY KEY (`invoiceID`);

--
-- Indizes für die Tabelle `rooms`
--
ALTER TABLE `rooms`
  ADD PRIMARY KEY (`roomID`);

--
-- Indizes für die Tabelle `roomtype`
--
ALTER TABLE `roomtype`
  ADD PRIMARY KEY (`roomTypeID`);

--
-- Indizes für die Tabelle `services`
--
ALTER TABLE `services`
  ADD PRIMARY KEY (`serviceID`);

--
-- Indizes für die Tabelle `users`
--
ALTER TABLE `users`
  ADD PRIMARY KEY (`userID`);

--
-- AUTO_INCREMENT für exportierte Tabellen
--

--
-- AUTO_INCREMENT für Tabelle `bookings`
--
ALTER TABLE `bookings`
  MODIFY `bookingID` tinyint(4) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT für Tabelle `customers`
--
ALTER TABLE `customers`
  MODIFY `customerID` tinyint(4) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT für Tabelle `guests`
--
ALTER TABLE `guests`
  MODIFY `guestID` smallint(6) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=3;

--
-- AUTO_INCREMENT für Tabelle `invoices`
--
ALTER TABLE `invoices`
  MODIFY `invoiceID` tinyint(4) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT für Tabelle `rooms`
--
ALTER TABLE `rooms`
  MODIFY `roomID` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT für Tabelle `services`
--
ALTER TABLE `services`
  MODIFY `serviceID` tinyint(4) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT für Tabelle `users`
--
ALTER TABLE `users`
  MODIFY `userID` tinyint(4) NOT NULL AUTO_INCREMENT;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
