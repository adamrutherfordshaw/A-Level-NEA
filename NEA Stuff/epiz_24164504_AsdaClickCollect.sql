-- phpMyAdmin SQL Dump
-- version 3.5.8.2
-- http://www.phpmyadmin.net
--
-- Host: sql200.epizy.com
-- Generation Time: Aug 14, 2019 at 06:12 AM
-- Server version: 5.6.41-84.1
-- PHP Version: 5.3.3

SET SQL_MODE="NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;

--
-- Database: `epiz_24164504_AsdaClickCollect`
--

-- --------------------------------------------------------

--
-- Table structure for table `OrdersItems_tbl`
--

CREATE TABLE IF NOT EXISTS `OrdersItems_tbl` (
  `OrdersItemsID` int(11) NOT NULL AUTO_INCREMENT,
  `OrderID` int(11) NOT NULL,
  `ProductID` int(11) NOT NULL,
  `Quantity` int(11) NOT NULL,
  PRIMARY KEY (`OrdersItemsID`),
  KEY `ProductID` (`ProductID`),
  KEY `OrderID` (`OrderID`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1 AUTO_INCREMENT=1 ;

-- --------------------------------------------------------

--
-- Table structure for table `Orders_tbl`
--

CREATE TABLE IF NOT EXISTS `Orders_tbl` (
  `OrderID` int(11) NOT NULL AUTO_INCREMENT,
  `OrderDate` date NOT NULL,
  `CustomerID` int(11) NOT NULL,
  `ColleagueID` int(11) NOT NULL,
  `TimeTaken` double NOT NULL,
  `Completed` tinyint(1) NOT NULL,
  PRIMARY KEY (`OrderID`),
  KEY `CustomerID` (`CustomerID`),
  KEY `ColleagueID` (`ColleagueID`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1 AUTO_INCREMENT=1 ;

-- --------------------------------------------------------

--
-- Table structure for table `Products_tbl`
--

CREATE TABLE IF NOT EXISTS `Products_tbl` (
  `ProductID` int(3) NOT NULL AUTO_INCREMENT,
  `UPC` bigint(10) NOT NULL,
  `ProductName` varchar(18) DEFAULT NULL,
  `Aisle` int(2) DEFAULT NULL,
  `Location` int(4) DEFAULT NULL,
  PRIMARY KEY (`ProductID`),
  UNIQUE KEY `ProductID` (`ProductID`),
  UNIQUE KEY `UPC` (`UPC`)
) ENGINE=MyISAM  DEFAULT CHARSET=utf8 AUTO_INCREMENT=107 ;

--
-- Dumping data for table `Products_tbl`
--

INSERT INTO `Products_tbl` (`ProductID`, `UPC`, `ProductName`, `Aisle`, `Location`) VALUES
(1, 7148163001, 'Beans', 12, 1001),
(2, 5610093001, 'Eggs', 20, 2001),
(3, 4680057001, 'Chocolate Bars', 21, 1216),
(4, 7523109001, 'Bananas', 4, 1022),
(5, 9030988001, 'Apples', 4, 1029),
(6, 885595101, 'Milk', 26, 2009),
(7, 7130280001, 'Bread', 19, 1080),
(8, 4311444001, 'Lightbulbs', 8, 2077),
(9, 2752955001, 'Car Oil', 8, 2088),
(10, 8025874001, 'Spatulas', 34, 2058),
(11, 4175992001, 'Spoons', 34, 1089),
(12, 2771684001, 'Party Hats', 2, 1018),
(13, 4334571001, 'Pans', 34, 2012),
(14, 1215500001, 'Microwaves', 35, 1057),
(15, 3478327001, 'Toothbrushes', 6, 2011),
(16, 1854025001, 'Chicken', 31, 2058),
(17, 4327595001, 'Ham', 30, 1074),
(18, 5420293001, 'Cakes', 18, 2098),
(19, 9729667001, 'Jaffa Cakes', 17, 1061),
(20, 3789912001, 'Pringles', 16, 1077),
(21, 2815034001, 'Coca Cola', 9, 1099),
(22, 1387323001, 'Oven Trays', 34, 2137),
(23, 9653645001, 'Spaghetti', 12, 2093),
(24, 9092041001, 'Pasta', 12, 2117),
(25, 4411147001, 'Toaster', 35, 1014),
(26, 644911501, 'Kettle', 35, 1006),
(27, 7005143001, 'Garden Hose', 1, 2054),
(28, 1554357001, 'Fanta', 9, 2076),
(29, 3261440001, 'Pens', 7, 1190),
(30, 502628101, 'Notepad', 7, 1153),
(31, 7570295001, 'PS4 Game', 10, 1012),
(32, 3252054001, 'Xbox Controller', 10, 1052),
(33, 8042672001, 'Umbrella', 10, 2011),
(34, 9051293001, 'DVD Player', 10, 2062),
(35, 4166484001, 'Printer Ink', 10, 2096),
(36, 9541579001, 'Wrapping Paper', 2, 1079),
(37, 6962850001, 'Paper Plates', 2, 1044),
(38, 162977001, 'RC Car', 3, 2039),
(39, 1642342001, 'Car Mat', 8, 2017),
(40, 4060627001, 'Baby Seat', 11, 1010),
(41, 2761233001, 'Ravioli', 12, 1076),
(42, 8491000001, 'Soup', 12, 1066),
(43, 1877751001, 'Frozen Vegetables', 13, 1013),
(44, 3728143001, 'Fish', 13, 2046),
(45, 7708328001, 'Beef', 25, 1013),
(46, 4469932001, 'Whipped Cream', 26, 2028),
(47, 8783846001, 'Milk Tray', 17, 1008),
(48, 3744322001, 'Slow Cooker', 35, 1089),
(49, 8605893001, 'Biscuit Tin', 34, 2149),
(50, 9315594001, 'Tea Towels', 34, 2140),
(51, 3303008001, 'Detergent', 14, 1114),
(52, 2590610001, 'Cookie Cutter', 34, 2098),
(53, 1786935001, 'Knife Block', 34, 2058),
(54, 25056601, 'Storage Tubs', 34, 1164),
(55, 8408871001, 'Travel Mugs', 34, 1152),
(56, 7029701001, 'Placemats', 34, 1141),
(57, 8115714001, 'Dinner Plate', 34, 1103),
(58, 4376639001, 'Mug', 34, 1006),
(59, 4106317001, 'Ice Cube Tray', 34, 2107),
(60, 7105542001, 'Gift Bag', 2, 1092),
(61, 7987735001, 'Cocktail Sticks', 2, 1035),
(62, 5929912001, 'Nerf Gun', 3, 2109),
(63, 2431479001, 'Playing Cards', 3, 2008),
(64, 1143328001, 'Peppa Pig Doll Set', 3, 2160),
(65, 6329758001, 'Candles', 33, 2086),
(66, 9025697001, 'Tealight Holder', 33, 2108),
(67, 2451739001, 'King Size Bedset', 33, 1008),
(68, 749503401, 'Pink Pillowcase', 33, 1019),
(69, 7599790001, 'Yellow Throw', 33, 1105),
(70, 3786062001, 'Hot Water Bottles', 33, 1170),
(71, 5265732001, 'Cushion', 33, 2107),
(72, 5811764001, 'Hooded Baby Towel', 33, 2198),
(73, 787088901, 'Pillow', 33, 1062),
(74, 1114329001, 'Duvet', 33, 1057),
(75, 9003339001, 'Broom', 32, 2097),
(76, 9499055001, 'Dustpan & Brush', 32, 2084),
(77, 4412162001, 'Washing Line', 32, 2053),
(78, 7695068001, 'Clothes Hangers', 32, 2067),
(79, 3825693001, 'Henry Hoover', 32, 2015),
(80, 3261572001, 'Ironing Board', 32, 2027),
(81, 1526768001, 'Iron', 32, 2020),
(82, 994099401, 'Soap Dispenser', 32, 1017),
(83, 6925178001, 'Bath Mat', 32, 1048),
(84, 1830760001, 'Face Towel', 32, 1050),
(85, 4713914001, 'Bath Towel', 32, 1049),
(86, 4258493001, 'Bath Sheet', 32, 1051),
(87, 7525425001, 'Shaver', 32, 1117),
(88, 8219315001, 'Hair Dryer', 32, 1106),
(89, 1087859001, 'Lamb Steaks', 29, 2081),
(90, 1516570001, 'Margarine', 28, 1165),
(91, 1881318001, 'Butter', 28, 1219),
(92, 8721181001, 'Carrots', 5, 2015),
(93, 7294109001, 'Onions', 5, 2018),
(94, 2577900001, 'Biscuits', 15, 2098),
(95, 8278990001, 'Headphones', 10, 2045),
(96, 7911555001, 'Razor Blades', 6, 2131),
(97, 4509423001, 'Facial Wipes', 6, 1087),
(98, 8033303001, 'Screwdriver', 8, 2176),
(99, 2035823001, 'Rice', 22, 2007),
(100, 3646880001, 'Curry Powder', 22, 2086),
(101, 3441808001, 'Sausages', 23, 1075),
(102, 4254850001, 'Popcorn', 24, 2044),
(103, 500450101, 'Fruit Pastilles', 24, 2089),
(104, 2064854001, 'Sugar', 27, 1090),
(105, 9330523001, 'Flour', 27, 1080),
(106, 8395861001, 'Whisk', 34, 2183);

-- --------------------------------------------------------

--
-- Table structure for table `Users_tbl`
--

CREATE TABLE IF NOT EXISTS `Users_tbl` (
  `UserID` int(5) NOT NULL AUTO_INCREMENT,
  `EmployeeID` int(10) NOT NULL,
  `FullName` text NOT NULL,
  `EmailAddress` varchar(254) NOT NULL,
  `DateOfBirth` date NOT NULL,
  `Username` varchar(20) NOT NULL,
  `Password` varchar(32) NOT NULL,
  `Customer` tinyint(1) NOT NULL,
  `Administrator` tinyint(1) NOT NULL,
  PRIMARY KEY (`UserID`),
  UNIQUE KEY `EmailAddress` (`Username`)
) ENGINE=MyISAM  DEFAULT CHARSET=latin1 AUTO_INCREMENT=2 ;

--
-- Dumping data for table `Users_tbl`
--

INSERT INTO `Users_tbl` (`UserID`, `EmployeeID`, `FullName`, `EmailAddress`, `DateOfBirth`, `Username`, `Password`, `Customer`, `Administrator`) VALUES
(1, 1000000001, 'Adam Rutherford-Shaw', '', '2002-01-18', 'adamrutherfordshaw', 'cb173e0ee6fe752d1cc389f727fd9577', 0, 1);

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
