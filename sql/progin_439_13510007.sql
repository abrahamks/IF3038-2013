-- phpMyAdmin SQL Dump
-- version 3.5.2.2
-- http://www.phpmyadmin.net
--
-- Host: 127.0.0.1
-- Generation Time: May 23, 2013 at 02:57 PM
-- Server version: 5.5.27
-- PHP Version: 5.4.7

SET SQL_MODE="NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;

--
-- Database: `progin_439_13510007`
--

-- --------------------------------------------------------

--
-- Table structure for table `assign`
--

CREATE TABLE IF NOT EXISTS `assign` (
  `id_user` int(10) NOT NULL,
  `id_task` int(10) NOT NULL,
  PRIMARY KEY (`id_user`,`id_task`),
  KEY `id_task` (`id_task`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `assign`
--

INSERT INTO `assign` (`id_user`, `id_task`) VALUES
(3, 5),
(7, 6),
(3, 7),
(7, 7);

-- --------------------------------------------------------

--
-- Table structure for table `comment`
--

CREATE TABLE IF NOT EXISTS `comment` (
  `id_komentar` int(10) NOT NULL AUTO_INCREMENT,
  `timestamp` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `komentar` text NOT NULL,
  `id_user` int(10) NOT NULL,
  `id_task` int(10) NOT NULL,
  PRIMARY KEY (`id_komentar`),
  KEY `id_user` (`id_user`),
  KEY `id_task` (`id_task`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=23 ;

--
-- Dumping data for table `comment`
--

INSERT INTO `comment` (`id_komentar`, `timestamp`, `komentar`, `id_user`, `id_task`) VALUES
(19, '2013-03-23 04:15:30', 'adasda', 4, 5),
(20, '2013-03-23 04:15:36', 'asdsad', 4, 5),
(21, '2013-03-23 04:15:52', 'sdad', 4, 5),
(22, '2013-03-23 04:16:07', 'asdasda', 4, 5);

-- --------------------------------------------------------

--
-- Table structure for table `edit_kategori`
--

CREATE TABLE IF NOT EXISTS `edit_kategori` (
  `id_user` int(11) NOT NULL,
  `id_kategori` int(11) NOT NULL,
  PRIMARY KEY (`id_user`,`id_kategori`),
  KEY `id_katego` (`id_kategori`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `edit_kategori`
--

INSERT INTO `edit_kategori` (`id_user`, `id_kategori`) VALUES
(3, 5);

-- --------------------------------------------------------

--
-- Table structure for table `have_tags`
--

CREATE TABLE IF NOT EXISTS `have_tags` (
  `id_task` int(10) NOT NULL,
  `id_tag` int(10) NOT NULL,
  PRIMARY KEY (`id_task`,`id_tag`),
  KEY `id_tag` (`id_tag`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `have_tags`
--

INSERT INTO `have_tags` (`id_task`, `id_tag`) VALUES
(1, 1),
(7, 5),
(1, 11),
(5, 14);

-- --------------------------------------------------------

--
-- Table structure for table `kategori`
--

CREATE TABLE IF NOT EXISTS `kategori` (
  `id_kategori` int(10) NOT NULL AUTO_INCREMENT,
  `nama_kategori` varchar(100) NOT NULL,
  `id_user` int(10) NOT NULL,
  PRIMARY KEY (`id_kategori`),
  KEY `id_user` (`id_user`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=11 ;

--
-- Dumping data for table `kategori`
--

INSERT INTO `kategori` (`id_kategori`, `nama_kategori`, `id_user`) VALUES
(1, 'IF 2034', 2),
(5, 'tesgaya', 3),
(9, 'tes', 4),
(10, 'Tugas_Terbaru', 7);

-- --------------------------------------------------------

--
-- Table structure for table `tag`
--

CREATE TABLE IF NOT EXISTS `tag` (
  `id_tag` int(10) NOT NULL AUTO_INCREMENT,
  `tag_name` varchar(50) NOT NULL,
  PRIMARY KEY (`id_tag`),
  UNIQUE KEY `tag_name` (`tag_name`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=15 ;

--
-- Dumping data for table `tag`
--

INSERT INTO `tag` (`id_tag`, `tag_name`) VALUES
(3, 'adf'),
(1, 'basdat'),
(4, 'fd'),
(5, 'intelegensia buatan'),
(12, 'lol'),
(11, 'progiiin'),
(2, 'rpl'),
(13, 'tes'),
(14, 'tesdoang');

-- --------------------------------------------------------

--
-- Table structure for table `task`
--

CREATE TABLE IF NOT EXISTS `task` (
  `id_task` int(10) NOT NULL AUTO_INCREMENT,
  `nama_task` varchar(50) NOT NULL,
  `status` tinyint(1) NOT NULL DEFAULT '0',
  `deadline` date NOT NULL,
  `id_kategori` int(10) NOT NULL,
  `id_user` int(10) NOT NULL,
  `last_edit` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id_task`),
  KEY `id_kategori` (`id_kategori`),
  KEY `id_user` (`id_user`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=8 ;

--
-- Dumping data for table `task`
--

INSERT INTO `task` (`id_task`, `nama_task`, `status`, `deadline`, `id_kategori`, `id_user`, `last_edit`) VALUES
(1, 'Tugas 2', 1, '2013-03-14', 5, 2, '2013-05-24 20:21:20'),
(5, 'Tugas 4', 1, '2013-03-14', 5, 4, '2013-05-11 19:31:12'),
(6, 'Tugas Paling Baru Loh', 1, '2013-05-31', 10, 7, '2013-05-18 11:12:38'),
(7, 'Tugas Paling Baru v2', 0, '2013-05-31', 10, 7, '2013-05-18 11:12:38');

-- --------------------------------------------------------

--
-- Table structure for table `task_attachment`
--

CREATE TABLE IF NOT EXISTS `task_attachment` (
  `id_task` int(10) NOT NULL,
  `attachment` varchar(100) NOT NULL,
  PRIMARY KEY (`id_task`,`attachment`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `task_attachment`
--

INSERT INTO `task_attachment` (`id_task`, `attachment`) VALUES
(5, '0D066EB83BD8B5061F0AFF78406BB6F0.database'),
(5, '0DADBA7F7391E9619F1B7B8A6A465DAA.jpg'),
(5, '0FF26FC4841D3FAE1C5B8C7E5A7F3435.jpg'),
(5, '2A7DE832A4723D90F1D5F8E9ED0A079B.jpg'),
(5, '33E6A5300F990DB60344E67E55820303.jpg'),
(5, '56D73C946A69E1E59EAACB62E03AEC3F.png'),
(5, '968EDA0E3C919CD8822F1C4090B63E92.jpg'),
(5, '9BB53B9CC6008EEE1CE3312F3DD542B2.jpg'),
(5, 'AA8D192272436A431ABDE3E91D65722B.jpg'),
(5, 'B6DDAC5000A38E844726876EDAA27E06.jpg'),
(5, 'D39FCC28C9599AE8792BE9AFB257043C.jpg'),
(5, 'EBEC68A8B9BDB3325CF3905FE8531771.jpg'),
(5, 'ED863257E65D0F4E4AA535A2D458F67C.jpg'),
(6, '4D5FC8D9E92E1006D5031A41D6FE824E.'),
(7, '8C1B19F3961E4E76E63DD37D500A0A8A.');

-- --------------------------------------------------------

--
-- Table structure for table `user`
--

CREATE TABLE IF NOT EXISTS `user` (
  `id_user` int(10) NOT NULL AUTO_INCREMENT,
  `username` varchar(50) NOT NULL,
  `email` varchar(50) NOT NULL,
  `fullname` varchar(50) NOT NULL,
  `avatar` varchar(100) NOT NULL,
  `birthdate` date NOT NULL,
  `password` varchar(50) NOT NULL,
  PRIMARY KEY (`id_user`),
  UNIQUE KEY `username` (`username`),
  KEY `email` (`email`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=8 ;

--
-- Dumping data for table `user`
--

INSERT INTO `user` (`id_user`, `username`, `email`, `fullname`, `avatar`, `birthdate`, `password`) VALUES
(2, 'asdfghjkl', 'adsf@yahoo.com', 'asdf ghjkl', '51BF9CCEC6439E231D13043486B6C50A.jpg', '2013-03-15', '25d55ad283aa400af464c76d713c07ad'),
(3, 'abrahamks', 'abrahamkrisnanda@outlook.com', 'abraham krisnanda', '4897178F18808820BA169AC2DFC3568A.JPG', '2013-03-15', '1441f19754335ca4638bfdf1aea00c6d'),
(4, 'admin', 'fernandojordan.92@gmail.com', 'Jordan Fernando', 'B18D024454540A7B459FEA497EFB9D05.jpg', '2013-03-20', '2f029a1250c44708d7865338918648af'),
(5, 'sijordan', 'sijordan@gmail.com', 'si jordan', '598BF544CE9A2FA278B626B6C8FF0DD6.jpg', '2013-04-18', 'sijordans'),
(6, 'sijordan2', 'sijordan2@gmail.com', 'si jordan', 'ACFEE7A634F86D7D8BD84A548FC770C1.jpg', '1992-12-12', 'sijordans'),
(7, 'admin2', 'adadeh@yahoo.com', 'ada deh', '00B18C21BCFA996D5BB674B0FFAD5688.jpg', '2013-05-18', '5f4dcc3b5aa765d61d8327deb882cf99');

--
-- Constraints for dumped tables
--

--
-- Constraints for table `assign`
--
ALTER TABLE `assign`
  ADD CONSTRAINT `assign_ibfk_1` FOREIGN KEY (`id_user`) REFERENCES `user` (`id_user`),
  ADD CONSTRAINT `assign_ibfk_3` FOREIGN KEY (`id_task`) REFERENCES `task` (`id_task`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Constraints for table `comment`
--
ALTER TABLE `comment`
  ADD CONSTRAINT `comment_ibfk_1` FOREIGN KEY (`id_user`) REFERENCES `user` (`id_user`),
  ADD CONSTRAINT `comment_ibfk_3` FOREIGN KEY (`id_task`) REFERENCES `task` (`id_task`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Constraints for table `edit_kategori`
--
ALTER TABLE `edit_kategori`
  ADD CONSTRAINT `edit_kategori_ibfk_1` FOREIGN KEY (`id_user`) REFERENCES `user` (`id_user`),
  ADD CONSTRAINT `edit_kategori_ibfk_2` FOREIGN KEY (`id_kategori`) REFERENCES `kategori` (`id_kategori`);

--
-- Constraints for table `have_tags`
--
ALTER TABLE `have_tags`
  ADD CONSTRAINT `have_tags_ibfk_3` FOREIGN KEY (`id_task`) REFERENCES `task` (`id_task`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `have_tags_ibfk_4` FOREIGN KEY (`id_tag`) REFERENCES `tag` (`id_tag`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Constraints for table `kategori`
--
ALTER TABLE `kategori`
  ADD CONSTRAINT `kategori_ibfk_1` FOREIGN KEY (`id_user`) REFERENCES `user` (`id_user`);

--
-- Constraints for table `task`
--
ALTER TABLE `task`
  ADD CONSTRAINT `task_ibfk_2` FOREIGN KEY (`id_user`) REFERENCES `user` (`id_user`),
  ADD CONSTRAINT `task_ibfk_3` FOREIGN KEY (`id_kategori`) REFERENCES `kategori` (`id_kategori`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Constraints for table `task_attachment`
--
ALTER TABLE `task_attachment`
  ADD CONSTRAINT `task_attachment_ibfk_2` FOREIGN KEY (`id_task`) REFERENCES `task` (`id_task`) ON DELETE CASCADE ON UPDATE CASCADE;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
