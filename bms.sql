-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Host: localhost:3309
-- Generation Time: May 28, 2024 at 06:27 AM
-- Server version: 10.4.32-MariaDB
-- PHP Version: 8.0.30

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `bms`
--

-- --------------------------------------------------------

--
-- Table structure for table `bordercrossings`
--

CREATE TABLE `bordercrossings` (
  `CrossingID` int(11) NOT NULL,
  `DriverName` varchar(100) NOT NULL,
  `VehiclePlateNo` varchar(20) NOT NULL,
  `DriverLicenseNo` varchar(20) NOT NULL,
  `CrossingDateTime` datetime NOT NULL,
  `BorderCrossingPoint` varchar(100) NOT NULL,
  `PurposeOfVisit` varchar(255) DEFAULT NULL,
  `AdditionalNotes` text DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `bordercrossings`
--

INSERT INTO `bordercrossings` (`CrossingID`, `DriverName`, `VehiclePlateNo`, `DriverLicenseNo`, `CrossingDateTime`, `BorderCrossingPoint`, `PurposeOfVisit`, `AdditionalNotes`) VALUES
(1, 'Arun', 'KL 35 B 7132', 'KL 000145386', '2024-05-27 23:15:22', 'Karnataka - Thalappuzha Checkpost', 'Home Visit', ''),
(2, 'Jos', 'KL 35 G 5123', 'KL 000031257', '2024-05-27 23:28:36', 'Tamil Nadu - Walayar Checkpost', 'Job', ''),
(3, 'Jeel', 'KL 12 4869', 'Kl 00045678345', '2024-05-28 07:52:45', 'Karnataka - Thalappuzha Checkpost', 'Job', '');

-- --------------------------------------------------------

--
-- Table structure for table `border_officers`
--

CREATE TABLE `border_officers` (
  `officer_id` int(11) NOT NULL,
  `first_name` varchar(50) NOT NULL,
  `last_name` varchar(50) NOT NULL,
  `username` varchar(50) NOT NULL,
  `password` varchar(255) NOT NULL,
  `email` varchar(100) NOT NULL,
  `phone_number` varchar(20) NOT NULL,
  `station` varchar(100) NOT NULL,
  `hire_date` date NOT NULL,
  `status` varchar(20) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `border_officers`
--

INSERT INTO `border_officers` (`officer_id`, `first_name`, `last_name`, `username`, `password`, `email`, `phone_number`, `station`, `hire_date`, `status`) VALUES
(1, 'Amalda', 'Patrick', 'amalda', 'Amalda@123', 'amaldapatrick@gmail.com', '9562290332', 'Karnataka - Thalappuzha Checkpost', '2024-05-27', 'active'),
(2, 'Augusto', 'Simon', 'augusto', 'augusto@123', 'augusto@gmail.com', '7536542369', 'Tamil Nadu - Walayar Checkpost', '2024-05-27', 'active'),
(3, 'Rohan', 'Sujith', 'rohan', '75395', 'xxx@ggmail.com', '9512365478', 'Karnataka - Muthanga Checkpost', '2024-05-28', 'active'),
(4, 'Anoop', 'Thomas', 'anoop', 'anoop', 'anoop@123', '7891236547', 'Tamil Nadu - Kaliyakkavilai Checkpost', '2024-05-28', 'active');

--
-- Indexes for dumped tables
--

--
-- Indexes for table `bordercrossings`
--
ALTER TABLE `bordercrossings`
  ADD PRIMARY KEY (`CrossingID`);

--
-- Indexes for table `border_officers`
--
ALTER TABLE `border_officers`
  ADD PRIMARY KEY (`officer_id`),
  ADD UNIQUE KEY `username` (`username`),
  ADD UNIQUE KEY `email` (`email`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `bordercrossings`
--
ALTER TABLE `bordercrossings`
  MODIFY `CrossingID` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=4;

--
-- AUTO_INCREMENT for table `border_officers`
--
ALTER TABLE `border_officers`
  MODIFY `officer_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=5;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
