-- phpMyAdmin SQL Dump
-- version 4.9.2
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Erstellungszeit: 23. Jan 2020 um 14:07
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
  `bookingID` int(4) NOT NULL,
  `fk_roomID` tinyint(4) NOT NULL,
  `fk_guestID` tinyint(4) NOT NULL,
  `fk_customerID` tinyint(4) NOT NULL,
  `openAmount` int(11) NOT NULL,
  `bookingFrom` date NOT NULL,
  `bookingUntil` date NOT NULL,
  `bookingCanceled` date DEFAULT NULL,
  `checkedIn` date DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Daten für Tabelle `bookings`
--

INSERT INTO `bookings` (`bookingID`, `fk_roomID`, `fk_guestID`, `fk_customerID`, `openAmount`, `bookingFrom`, `bookingUntil`, `bookingCanceled`, `checkedIn`) VALUES
(3, 3, 5, 0, 0, '2020-01-22', '2020-01-24', NULL, '2020-01-22'),
(4, 11, 1, 5, 899, '2020-01-22', '2020-02-02', '2017-05-22', '2020-01-22'),
(5, 12, 7, 5, 99, '2020-01-21', '2020-01-21', '2020-01-21', NULL);

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `customers`
--

CREATE TABLE `customers` (
  `customerID` int(4) NOT NULL,
  `firstName` varchar(55) NOT NULL,
  `lastName` varchar(55) NOT NULL,
  `companyName` varchar(155) NOT NULL,
  `birthDate` date NOT NULL,
  `address` varchar(55) NOT NULL,
  `zipCode` smallint(6) NOT NULL,
  `country` varchar(55) NOT NULL,
  `phoneNumber` varchar(55) NOT NULL,
  `email` varchar(55) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Daten für Tabelle `customers`
--

INSERT INTO `customers` (`customerID`, `firstName`, `lastName`, `companyName`, `birthDate`, `address`, `zipCode`, `country`, `phoneNumber`, `email`) VALUES
(1, 'Dummy', 'Account', 'Dummy GmbH', '1900-01-01', 'Dummystreet 5', 1234, 'Dummyhausen', '123', 'dummy@domain.com');

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `guests`
--

CREATE TABLE `guests` (
  `guestID` int(6) NOT NULL,
  `firstName` varchar(55) NOT NULL,
  `lastName` varchar(55) NOT NULL,
  `birthDate` date NOT NULL,
  `address` varchar(155) NOT NULL,
  `zipCode` smallint(6) NOT NULL,
  `country` varchar(55) NOT NULL,
  `phoneNumber` varchar(55) NOT NULL,
  `email` varchar(55) NOT NULL,
  `passportNr` varchar(55) NOT NULL,
  `fk_customerID` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Daten für Tabelle `guests`
--

INSERT INTO `guests` (`guestID`, `firstName`, `lastName`, `birthDate`, `address`, `zipCode`, `country`, `phoneNumber`, `email`, `passportNr`, `fk_customerID`) VALUES
(0, 'Dummy', 'Account', '1900-01-01', 'Dummystreet 5', 1234, 'Dummyhausen', '+436761234567', 'dummy@domain.com', '147258369', 0),
(1, 'John', 'Asteriod', '1965-01-23', 'Fluidgasse 12', 12245, 'Deutschland', '+448556942541', 'j.asteriod@domain.com', '923351234', 0),
(2, 'Haris', 'Ascobar', '1978-01-20', 'Ottakringer Straße 77', 1160, 'Austria', '+436997445898', 'harisascobar@domain.com', 'P122424562', 0),
(3, 'Roman', 'Beltrovic', '1984-04-20', 'Faktorstraße 12', 1240, 'Austria', '+4368125477584', 'bigboibeltrovic@gmail.com', 'P54156166165', 0),
(4, 'Milenda', 'Carolyn', '1989-08-08', '91 Shawan Falls Dr', 13017, 'USA', '+485562626262', 'mc@gmail.com', 'P4481553514', 0),
(5, 'Markus', 'Kaspar', '1888-01-01', 'Waifustreet 69', 14201, 'Deutschland', '+436994206988', 'igoogledanimutiddies@gmail.com', 'P456255613', 0),
(6, 'Moritz', 'Egger', '2020-01-02', 'Kettenbrückengasse 23', 1050, 'Austria', '456789', 'm@welt.all', '7er8re8f87f87fd87', 7),
(7, 'Moritz', 'Egger', '2002-01-17', 'Kettenbrückengasse 23', 1050, 'Austria', '3456789', 'm@welt.all', 'h78fd87fd87d', 7);

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `invoices`
--

CREATE TABLE `invoices` (
  `invoiceID` int(4) NOT NULL,
  `fk_bookingID` int(4) NOT NULL,
  `fk_guestID` int(11) NOT NULL,
  `fk_customerID` int(11) DEFAULT NULL,
  `nights` int(11) NOT NULL,
  `priceNight` int(11) NOT NULL,
  `arrival` date NOT NULL,
  `depature` date NOT NULL,
  `services` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `rooms`
--

CREATE TABLE `rooms` (
  `roomID` int(11) NOT NULL,
  `fk_roomTypeID` int(4) NOT NULL,
  `roomSize` int(4) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Daten für Tabelle `rooms`
--

INSERT INTO `rooms` (`roomID`, `fk_roomTypeID`, `roomSize`) VALUES
(1, 1, 38),
(2, 1, 38),
(3, 1, 39),
(4, 1, 40),
(5, 1, 40),
(6, 2, 42),
(7, 2, 42),
(8, 2, 43),
(9, 2, 44),
(10, 2, 44),
(11, 3, 48),
(12, 3, 48),
(13, 3, 49),
(14, 3, 49),
(15, 3, 50),
(16, 4, 52),
(17, 4, 53),
(18, 4, 54),
(19, 4, 54),
(20, 4, 55),
(21, 5, 100),
(22, 5, 101),
(23, 5, 101),
(24, 5, 103),
(25, 5, 104),
(26, 6, 107),
(27, 6, 107),
(28, 6, 108),
(29, 6, 109),
(30, 6, 110),
(31, 7, 117),
(32, 7, 118),
(33, 7, 118),
(34, 7, 119),
(35, 7, 120),
(36, 8, 123),
(37, 8, 124),
(38, 8, 125),
(39, 8, 125),
(40, 8, 126),
(42, 1, 69);

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `roomtype`
--

CREATE TABLE `roomtype` (
  `roomTypeID` int(4) NOT NULL,
  `roomTypeName` varchar(155) NOT NULL,
  `roomTypeCapacity` int(4) NOT NULL,
  `roomTypeFacilities` varchar(555) NOT NULL,
  `roomTypePrice` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Daten für Tabelle `roomtype`
--

INSERT INTO `roomtype` (`roomTypeID`, `roomTypeName`, `roomTypeCapacity`, `roomTypeFacilities`, `roomTypePrice`) VALUES
(1, 'Single Room', 1, 'WLAN, Shower, TV, Safe, ', 5990),
(2, 'Single Room with Balcony', 1, 'WLAN, Shower, TV, Safe, Balcony', 6990),
(3, 'Double Room', 2, 'WLAN, Shower, TV, Safe', 9990),
(4, 'Double Room with Balcony', 2, 'WLAN, Shower, TV, Safe, Balcony', 10990),
(5, 'Suite', 4, 'WLAN, Bathtub, TV, Safe,', 19990),
(6, 'Suite with Balcony', 4, 'WLAN, Bathtub, TV, Safe, Balcony', 22990),
(7, 'Superior Double Room', 4, 'WLAN, Bathtub, TV, Safe, Coffeemachine,', 29990),
(8, 'Superior Double Room with Balcony', 4, 'WLAN, Bathtub, TV, Safe, Coffeemachine, Balcony', 33990);

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `services`
--

CREATE TABLE `services` (
  `servicesID` int(4) NOT NULL,
  `fk_bookingID` tinyint(4) NOT NULL,
  `serviceType` char(14) NOT NULL,
  `serviceDate` date NOT NULL,
  `fk_serviceID` tinyint(4) NOT NULL,
  `fixPrice` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Daten für Tabelle `services`
--

INSERT INTO `services` (`servicesID`, `fk_bookingID`, `serviceType`, `serviceDate`, `fk_serviceID`, `fixPrice`) VALUES
(128, 3, 'wellness', '2020-01-21', 4, 1200);

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `serv_minibar`
--

CREATE TABLE `serv_minibar` (
  `mbID` int(11) NOT NULL,
  `mbItem` varchar(128) DEFAULT NULL,
  `mbItemDescription` varchar(1024) DEFAULT NULL,
  `mbPrice` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Daten für Tabelle `serv_minibar`
--

INSERT INTO `serv_minibar` (`mbID`, `mbItem`, `mbItemDescription`, `mbPrice`) VALUES
(1, 'Snickers', 'Snickers ist ein Schokoriegel mit Karamell, Erdnüssen und einer weichen, weißen Nougat-ähnlichen Masse.', 350),
(2, 'Coke 0,2l', 'Flasche Coca Cola', 700),
(3, 'Burgunder 0,2l', 'Flasche Burgunder', 1290),
(4, 'Valser 0,2l', 'Bottle Valser Water', 300),
(5, 'Pringels Mini 40g', 'Mit 40 Gramm superleckeren Pringles hast du immer einen Snack parat', 420);

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `serv_movies`
--

CREATE TABLE `serv_movies` (
  `movieID` int(11) NOT NULL,
  `movieName` varchar(128) DEFAULT NULL,
  `movieDescription` varchar(1024) DEFAULT NULL,
  `moviePrice` int(11) DEFAULT NULL,
  `movieSeen` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Daten für Tabelle `serv_movies`
--

INSERT INTO `serv_movies` (`movieID`, `movieName`, `movieDescription`, `moviePrice`, `movieSeen`) VALUES
(1, '1917', 'Two young British soldiers during the First World War are given an impossible mission: deliver a message deep in enemy territory that will stop 1,600 men, and one of the soldiers\' brothers, from walking straight into a deadly trap.', 299, 4),
(2, 'Star Wars: Episode IX - Der Aufstieg Skywalkers', 'The surviving members of the resistance face the First Order once again, and the legendary conflict between the Jedi and the Sith reaches its peak bringing the Skywalker saga to its end.', 399, NULL),
(3, 'Once Upon a Time in Hollywood', 'A faded television actor and his stunt double strive to achieve fame and success in the film industry during the final years of Hollywood\'s Golden Age in 1969 Los Angeles.', 299, NULL),
(4, 'Joker', 'In Gotham City, mentally troubled comedian Arthur Fleck is disregarded and mistreated by society. He then embarks on a downward spiral of revolution and bloody crime. This path brings him face-to-face with his alter-ego: the Joker.', 299, NULL),
(5, 'Cats', 'A tribe of cats called the Jellicles must decide yearly which one will ascend to the Heaviside Layer and come back to a new Jellicle life.', 399, NULL),
(6, 'The Irishman', 'A mob hitman recalls his possible involvement with the slaying of Jimmy Hoffa.', 299, NULL);

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `serv_wellness`
--

CREATE TABLE `serv_wellness` (
  `wellnessID` int(11) NOT NULL,
  `wellnessName` varchar(128) DEFAULT NULL,
  `wellnessDescription` varchar(1024) DEFAULT NULL,
  `wellnessPrice` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Daten für Tabelle `serv_wellness`
--

INSERT INTO `serv_wellness` (`wellnessID`, `wellnessName`, `wellnessDescription`, `wellnessPrice`) VALUES
(1, 'Sauna', NULL, 500),
(2, 'Classic Massage', 'The classic massage is based on targeted massage treatments that relieve tension and have a very positive effect on the muscles', 900),
(3, 'Thai Massage', NULL, 1000),
(4, 'Hot Stone – Massage', 'The hot stone massage gently processes and massages the acupuncture points and meridians of the body with heated, smooth lava stones and warm aromatic oil. The hot stone foot, hand and face massages are also particularly pleasant types of massage with hot stones.', 1200),
(5, 'Shiatsu Massage', 'The Shiatsu massage originally comes from Japan and is a special finger pressure massage. Touch, gentle pressure, leaning and massage of certain parts of the body are used. Basically, it is about balancing the body, relieving tension and releasing the energy flows.', 1200);

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `users`
--

CREATE TABLE `users` (
  `userID` int(4) NOT NULL,
  `userIsAdmin` tinyint(1) NOT NULL,
  `userName` varchar(55) NOT NULL,
  `userPassword` varchar(155) NOT NULL,
  `userFirstName` varchar(55) NOT NULL,
  `userLastName` varchar(55) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Daten für Tabelle `users`
--

INSERT INTO `users` (`userID`, `userIsAdmin`, `userName`, `userPassword`, `userFirstName`, `userLastName`) VALUES
(1, 1, 'admin', '8xa4dfmfDjDoBobFcW8VjJj77cwU0853vuuFi4PYPwbDKPIqym+XQWyTheKQScGDqXYpcfwUItZySQLYu5XvJQ==', 'System', 'Administrator'),
(2, 0, 'user', 'yFOJLrz6BLYIJt0ol2wbbNH1NSjjeYlHiaAprj3e0UDVaOPqDcidV349E397yYIyk4NvraiqRtMXV6T5f0n3CA==', 'Reception', 'Desk 1');

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
  ADD PRIMARY KEY (`servicesID`);

--
-- Indizes für die Tabelle `serv_minibar`
--
ALTER TABLE `serv_minibar`
  ADD PRIMARY KEY (`mbID`);

--
-- Indizes für die Tabelle `serv_movies`
--
ALTER TABLE `serv_movies`
  ADD PRIMARY KEY (`movieID`);

--
-- Indizes für die Tabelle `serv_wellness`
--
ALTER TABLE `serv_wellness`
  ADD PRIMARY KEY (`wellnessID`);

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
  MODIFY `bookingID` int(4) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=6;

--
-- AUTO_INCREMENT für Tabelle `customers`
--
ALTER TABLE `customers`
  MODIFY `customerID` int(4) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=2;

--
-- AUTO_INCREMENT für Tabelle `guests`
--
ALTER TABLE `guests`
  MODIFY `guestID` int(6) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=8;

--
-- AUTO_INCREMENT für Tabelle `invoices`
--
ALTER TABLE `invoices`
  MODIFY `invoiceID` int(4) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT für Tabelle `rooms`
--
ALTER TABLE `rooms`
  MODIFY `roomID` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=43;

--
-- AUTO_INCREMENT für Tabelle `services`
--
ALTER TABLE `services`
  MODIFY `servicesID` int(4) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=129;

--
-- AUTO_INCREMENT für Tabelle `serv_minibar`
--
ALTER TABLE `serv_minibar`
  MODIFY `mbID` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=6;

--
-- AUTO_INCREMENT für Tabelle `serv_movies`
--
ALTER TABLE `serv_movies`
  MODIFY `movieID` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=7;

--
-- AUTO_INCREMENT für Tabelle `serv_wellness`
--
ALTER TABLE `serv_wellness`
  MODIFY `wellnessID` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=6;

--
-- AUTO_INCREMENT für Tabelle `users`
--
ALTER TABLE `users`
  MODIFY `userID` int(4) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=3;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
