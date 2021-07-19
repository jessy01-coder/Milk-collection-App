-- phpMyAdmin SQL Dump
-- version 4.9.5
-- https://www.phpmyadmin.net/
--
-- Host: localhost:3306
-- Generation Time: Jul 18, 2021 at 08:20 AM
-- Server version: 10.3.16-MariaDB
-- PHP Version: 7.3.23

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET AUTOCOMMIT = 0;
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `id16788618_nyaladb`
--

-- --------------------------------------------------------

--
-- Table structure for table `nyala_agents`
--

CREATE TABLE `nyala_agents` (
  `id` varchar(255) NOT NULL,
  `name` varchar(255) NOT NULL,
  `nid` varchar(255) NOT NULL,
  `phone` varchar(255) NOT NULL,
  `route` varchar(255) NOT NULL,
  `password` varchar(255) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `nyala_agents`
--

INSERT INTO `nyala_agents` (`id`, `name`, `nid`, `phone`, `route`, `password`) VALUES
('60abb37190bfe', 'Malanga K', '123456', '00000', 'Leshau', '$2y$10$WQoxUrHyDeRG1PnrioudZupvElt4tS7S74s3Wu8zYobdVbB48tOni'),
('60ac95f6d10f1', 'Joseph Njenga', '32141411', '0798766922', 'Shamata', '$2y$10$gjr9Z03ttYTKXpY6z7mdvejeTHh25wavTA.yX5sBeBnn1qzGBExWq'),
('60add17b18cf1', 'Mary Muthee', '38437453', '0112848631', 'Shamata', '$2y$10$2WCT2DpkbkKcMqC2WLzvc..uqNw1bMDbTpmN6C4HvVFiqXfVj/sKq'),
('60add4f6beb11', 'Devic Mutugi', '39279914', '0797065298', 'Withare', '$2y$10$iLuC/KmTz30qrAGhZHTXpu/Js4LmT71XwKYAgySrtm1sI2kMn73FK'),
('60b601b06dc4d', 'Johnny johnson', '123', '123', 'Kasuku', '$2y$10$NisZYh64ok7rMlJ6zDY9.uN8PKcqmPakAPfB5KaGovKR/BvRzV0TW'),
('60b601fdb6c70', 'john johnson', '1234', '1234', 'Kiamariga', '$2y$10$YpRhO3jjZU9RblQDqfuWPO/89D6sLdXqWzfyAdRIbgkFx79ocprMC'),
('60c0bd7d32cbb', 'joy sab', '6666', '6666', '', '$2y$10$Rt4ViaQ.LwpeCxHrfC58xOhvLObYnct4dv.r/csbRHz/bkW8Ectsy'),
('60c1a4e0bcb26', 'Alex Kungu', '5464656', '07564646', 'Kiamariga', '$2y$10$2Js3To5888pp.rKZmx0XMe20EtpLKQ7Fsq7G27xyXSFc4PaFyVzYi'),
('60c23d01ac928', 'Jesse Maina', '39021123', '0750739338', 'Withare', '$2y$10$R74AGfoByVQR8EiGmL/eluPigjGK0hUmq52Fbe95ZEGes.jpvoQIe'),
('60c26949e3b0d', 'Stella M Wairimu', '34422538', '0716310012', 'Leshau', '$2y$10$m3tjOSep1rTqB92VrzOnA.UaVkFh/tuwKq1aC4dfvwKN4jfwStMaO'),
('60c26b3e0d99d', 'Stella Macharia', '25364523', '0735897623', 'Leshau', '$2y$10$FF4tlQzFgdx/Lubzs07wGe.Yvh8uR1VikZH6vtvkBINgurbfWBhZq'),
('60c26b964dc5f', 'Stella M', '29257852', '0735428569', 'Leshau', '$2y$10$mZzZkk1IUoHY0vdGYI6C5OY56Yw3KDvYrajCye4fP2eiLSGtEdMme'),
('60c9d359b339d', 'kiptoo john', '1235', '1235', 'Kasuku', '$2y$10$xGfWl/q4.GRAPIGmvFcXvuYntHJ6hLrUr7jockO/CCscd5doCVwY6'),
('60cc5fbe624af', 'Malanga K', '202020', '0791205435', 'Withare', '$2y$10$2Z.uF7cElxYaf2xTkMH3I.zQkDl6piUwlNkhZ47pnMHfQanB/NO8y'),
('60ceeb8f4645c', 'Peter Mwaura', '37201680', '0745312254', 'Withare', '$2y$10$YubwvP4xUUKEYCW2HM96b.tpBcc8UfnGaGGye77PQ9jjc4Lq9WHZy'),
('60cf112b31490', 'agent 001', '001', '001', 'Kiamariga', '$2y$10$lm/7/.yWPxVJ4E61ShHNV.azuLcYfvNOJv6jiS02zF/MeST08HWlu'),
('60cf13ccb52e9', 'agent 005', '005', '005', 'Kasuku', '$2y$10$rtpagdFQtIvj.bDR1VEd0O84nia2y2Dgvb8VUlsSaOK3NpHxWOxOe'),
('60cf13e6377ae', 'agent 005', '006', '006', 'Kasuku', '$2y$10$7MDU4FCSxW7NCFtIsJxXOu0yS6nsDedN3NU57zHSC9PJM.kNzlcS6');

-- --------------------------------------------------------

--
-- Table structure for table `nyala_collection`
--

CREATE TABLE `nyala_collection` (
  `id` varchar(255) NOT NULL,
  `farmer_nid` varchar(255) NOT NULL,
  `agent_nid` varchar(255) NOT NULL,
  `capacity` varchar(255) NOT NULL,
  `cash` varchar(255) NOT NULL,
  `can_number` varchar(255) NOT NULL,
  `shift` varchar(255) NOT NULL,
  `day` varchar(255) NOT NULL,
  `month` varchar(255) NOT NULL,
  `year` varchar(255) NOT NULL,
  `time` varchar(255) NOT NULL,
  `payment_status` varchar(255) NOT NULL DEFAULT '0'
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `nyala_collection`
--

INSERT INTO `nyala_collection` (`id`, `farmer_nid`, `agent_nid`, `capacity`, `cash`, `can_number`, `shift`, `day`, `month`, `year`, `time`, `payment_status`) VALUES
('BQW2BKY0ASB2B', '37058859', '37058859', '100', '5000', 'C123', 'Morning', '27', 'May', '2021', '1126', '1'),
('BQW2EPU3ASU9A', '37058859', '37058859', '250', '2500', 'C001', 'Morning', '31', 'May', '2021', '1138', '0'),
('BQW2FOU9HNU2F', '100', '1234', '2', '100', '02', 'Afternoon', '1', 'Jun', '2021', '1249', '1'),
('BQW2FOV0JMV2B', '200', '1234', '200', '10000', '008', 'Afternoon', '1', 'Jun', '2021', '1251', '1'),
('BQW2FOV1ANX6G', '200', '1234', '200', '10000', '008', 'Afternoon', '1', 'Jun', '2021', '1251', '1'),
('BQW2FOV7DRA1F', '002', '001', '50', '2500', '009', 'Afternoon', '1', 'Jun', '2021', '132', '1'),
('BQW2HLC4ITC8D', '236', '1234', '236', '11800', '02', 'Afternoon', '3', 'Jun', '2021', '148', '1'),
('BQW2JLV0DOZ8E', '37058859', '37058859', '500', '25000', 'F776', 'Evening', '5', 'Jun', '2021', '1937', '1'),
('BQW3BPZ2JKY6G', '00', '1234', '2', '100', '09', 'Afternoon', '8', 'Jun', '2021', '1528', '0'),
('BQW3CNU3CKY0F', '00', '1234', '2', '100', 'po', 'Afternoon', '9', 'Jun', '2021', '1218', '0'),
('BQW3DKW5ESX2H', '37058859', '37058859', '200', '10000', 'C56', 'Morning', '10', 'Jun', '2021', '822', '1'),
('BQW3DKX7JSW1E', '1234567', '5464656', '100', '5000', 'aCdt', 'Morning', '10', 'Jun', '2021', '843', '0'),
('BQW3DOW4JTU7G', '39021123', '39021123', '20', '1000', '2676', 'Evening', '10', 'Jun', '2021', '1928', '1'),
('BQW3GKX4IOB9C', '100', '1234', '3', '90', '009', 'Evening', '13', 'Jun', '2021', '1958', '1'),
('BQW3GKY2GKB5D', '00', '1234', '2', '60', '09', 'Evening', '13', 'Jun', '2021', '2011', '0'),
('BQW3GKY2IQY4E', '200', '1234', '6', '180', '8', 'Evening', '13', 'Jun', '2021', '2011', '1'),
('BQW3GPB0EOC0J', '888', '1234', '2.5', '75.0', '09', 'Morning', '14', 'Jun', '2021', '1050', '1'),
('BQW3HND7BRY4I', '888', '1234', '2.8', '84.0', '5', 'Morning', '15', 'Jun', '2021', '948', '1'),
('BQW3IND4EMC6H', '200', '1234', '2.8', '84.0', '09', 'Afternoon', '16', 'Jun', '2021', '1330', '1'),
('BQW4AKB2ENU5A', '212121', '202020', '50', '1500.0', '2', 'Afternoon', '18', 'Jun', '2021', '127', '1'),
('BQW4AKB4AOB8D', '11223355', '202020', '20', '600.0', '6', 'Afternoon', '18', 'Jun', '2021', '1210', '0'),
('BQW4AKY9DOU9D', '236', '1234', '2.3', '69.0', '12', 'Morning', '18', 'Jun', '2021', '1128', '1'),
('BQW4ALX4ITC2G', '888', '202020', '2', '60.0', '3', 'Afternoon', '18', 'Jun', '2021', '1351', '1'),
('BQW4ATW8GLZ1F', '131313', '1234', '2.6', '78.0', '08', 'Morning', '19', 'Jun', '2021', '1154', '0'),
('BQW4BRY3JQB5H', '37201690', '37201680', '20', '600.0', '4', 'Morning', '20', 'Jun', '2021', '1033', '0'),
('BQW4BSU5IKZ8A', '235', '1234', '2.5', '75.0', '009', 'Afternoon', '20', 'Jun', '2021', '1216', '0'),
('BQW4BSW5CTD4C', '363', '1234', '20', '600.0', '009', 'Afternoon', '20', 'Jun', '2021', '1248', '0'),
('BQW4BSY7DKA0I', '8005', '1234', '9.5', '285.0', '009', 'Afternoon', '20', 'Jun', '2021', '1325', '0'),
('BQW4BSZ3HRA0A', '2288', '1234', '20', '600.0', '08', 'Afternoon', '20', 'Jun', '2021', '1336', '0'),
('BQW4DOX4JMW9D', '235', '1234', '2.5', '75.0', '9', 'Morning', '22', 'Jun', '2021', '931', '0'),
('BQW4IRA9GLD6I', '131313', '1234', '2.0', '60.0', '12', 'Afternoon', '28', 'Jun', '2021', '1342', '0'),
('BQW4IRC0DTW0C', '363', '1234', '20', '600.0', '5', 'Afternoon', '28', 'Jun', '2021', '140', '0'),
('BQW4IRD1AMX0G', '235', '1234', '1', '30.0', '7', 'Afternoon', '28', 'Jun', '2021', '1418', '0'),
('BQW5BNZ6GKW0C', '131313', '1234', '5', '150.0', '5', 'Afternoon', '1', 'Jul', '2021', '1334', '0'),
('BQW5CLD9IMZ0C', '212121', '202020', '200', '7000.0', '34', 'Afternoon', '2', 'Jul', '2021', '1259', '1'),
('BQW5ETZ1AMC2H', '363', '1234', '12.5', '437.5', '09', 'Evening', '5', 'Jul', '2021', '1725', '0'),
('BQW5ETZ1GKA3F', '131313', '1234', '10', '350.0', '12', 'Evening', '5', 'Jul', '2021', '1726', '0'),
('BQW6DNV6ATY6G', '141414', '1234', '12', '420.0', '5', 'Morning', '15', 'Jul', '2021', '946', '0');

-- --------------------------------------------------------

--
-- Table structure for table `nyala_farmer`
--

CREATE TABLE `nyala_farmer` (
  `id` varchar(255) NOT NULL,
  `name` varchar(255) NOT NULL,
  `nid` varchar(255) NOT NULL,
  `agent_nid` varchar(255) NOT NULL,
  `phone` varchar(255) NOT NULL,
  `route` varchar(255) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `nyala_farmer`
--

INSERT INTO `nyala_farmer` (`id`, `name`, `nid`, `agent_nid`, `phone`, `route`) VALUES
('609bc4a736685', 'Bantos Ben', '37058859', '37058859', '0748355080', 'Kiamariga'),
('609bd71b3d2c8', 'Antony mwandiki', '12345678', '98765432', '0724436324', 'Leshau'),
('609bfa55439c7', 'liza nkatha', '31234567', '98765432', '+254791605296', 'Shamata'),
('609bfcb6caaaa', 'liz nka', '12121212', '22222222', '0741082170', 'Kiamariga'),
('609cf057b1ed3', 'kelvin kim', '99887766', '98765432', '0724569672', 'Leshau'),
('609cf37785407', 'mary muthee', '44556677', '98765432', '+254112848631', 'Shamata'),
('60ab837133308', 'Christune rose', '22334455', '98765432', '+254704038052', 'Kasuku'),
('60ab89be1bb41', 'person john', '33998877', '33445566', '0726345382', 'Leshau'),
('60ab8aa88db78', 'john otieno', '44556678', '33445566', '0712345679', 'Kiamariga'),
('60aba2d3baf90', 'rose maina', '12345677', '98765432', '0757346722', 'Withare'),
('60aba7b00f89a', 'Alex john', '1234566', '98765432', '0722334455', 'Kasuku'),
('60aba9b58ee2a', 'edwin otwori', '11223355', '98765432', '0713307285', 'Kasuku'),
('60abb3b87dc3d', 'Migwi Ngobit', '999', '123456', '000', 'Leshau'),
('60ad0bd6c884c', 'brandie atieno', '37071193', '98765432', '0707996674', 'Kasuku'),
('60ad554070a22', 'john ndungu', '99887755', '98765432', '0798765432', 'Kiamariga'),
('60ae23edcb8fb', 'james ndege', '12345', '34123456', '072', 'Kiamariga'),
('60b101b0418db', 'cynthia lwaya', '1234567', '98765433', '07122', 'Leshau'),
('60b6025d9b7ad', 'tyson tyson', '100', '1234', '100', 'Kasuku'),
('60b602754ced4', 'moniq moniw', '200', '1234', '200', 'Leshau'),
('60b6058944909', 'alice Alicia', '002', '001', '002', 'Kasuku'),
('60b8b8255952e', 'johanna ng\'eno', '236', '1234', '236', 'Kiamariga'),
('60bf5e2d30c40', 'denno denno', '00', '1234', '00', 'Leshau'),
('60c086f35d51e', 'steven mutiso', '888', '1234', '888', 'Kasuku'),
('60c23d66e393d', 'Jesse Maina', '39021123', '39021123', '0700827215', 'Withare'),
('60cc60c403ef9', 'Wachepele D', '212121', '202020', '0791205435', 'Withare'),
('60cdb0a5efee8', 'bonnie ochieng', '131313', '1234', '0723374934', 'Kasuku'),
('60ceef1a6586c', 'peter m', '37201690', '37201680', '0745312254', 'Kiamariga'),
('60cf07483fc0b', 'migwi jesse', '235', '1234', '0754402228', 'Leshau'),
('60cf12a5d249d', 'john bosco', '00000001', '001', '0741182483', 'Kasuku'),
('60cf1437b00a1', 'farmer 01', '007', '006', '0754402223', 'Kasuku'),
('60cf17874cba8', 'mercy muso', '8005', '1234', '+254706104663', 'Kasuku'),
('60d97ce76e09b', 'farmer 05', '8080', '1234', '80801', 'Withare'),
('60e315e7876ba', 'edwin nyaundi', '0123', '1234', '0744', 'Kasuku'),
('60efd9c373b89', 'edwin akumu', '141414', '1234', '0748304503', 'Kasuku');

-- --------------------------------------------------------

--
-- Table structure for table `nyala_pricing`
--

CREATE TABLE `nyala_pricing` (
  `id` int(11) NOT NULL,
  `price` varchar(255) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `nyala_pricing`
--

INSERT INTO `nyala_pricing` (`id`, `price`) VALUES
(1, '35');

-- --------------------------------------------------------

--
-- Table structure for table `nyala_report`
--

CREATE TABLE `nyala_report` (
  `id` varchar(255) NOT NULL,
  `year` varchar(255) NOT NULL,
  `Jan` varchar(255) NOT NULL DEFAULT '0',
  `Feb` varchar(255) NOT NULL DEFAULT '0',
  `Mar` varchar(255) NOT NULL DEFAULT '0',
  `Apr` varchar(255) NOT NULL DEFAULT '0',
  `May` varchar(255) NOT NULL DEFAULT '0',
  `Jun` varchar(255) NOT NULL DEFAULT '0',
  `Jul` varchar(255) NOT NULL DEFAULT '0',
  `Aug` varchar(255) NOT NULL DEFAULT '0',
  `Sep` varchar(255) NOT NULL DEFAULT '0',
  `Oct` varchar(255) NOT NULL DEFAULT '0',
  `Nov` varchar(255) NOT NULL DEFAULT '0',
  `Dece` varchar(255) NOT NULL DEFAULT '0'
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `nyala_report`
--

INSERT INTO `nyala_report` (`id`, `year`, `Jan`, `Feb`, `Mar`, `Apr`, `May`, `Jun`, `Jul`, `Aug`, `Sep`, `Oct`, `Nov`, `Dece`) VALUES
('60af57d185234', '2021', '0', '0', '0', '0', '150', '2605', '351.5', '0', '0', '0', '0', '0');

--
-- Indexes for dumped tables
--

--
-- Indexes for table `nyala_agents`
--
ALTER TABLE `nyala_agents`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `nid` (`nid`,`phone`);

--
-- Indexes for table `nyala_collection`
--
ALTER TABLE `nyala_collection`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `nyala_farmer`
--
ALTER TABLE `nyala_farmer`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `nid` (`nid`),
  ADD UNIQUE KEY `phone` (`phone`);

--
-- Indexes for table `nyala_pricing`
--
ALTER TABLE `nyala_pricing`
  ADD PRIMARY KEY (`id`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `nyala_pricing`
--
ALTER TABLE `nyala_pricing`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=2;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
