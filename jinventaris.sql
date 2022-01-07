-- phpMyAdmin SQL Dump
-- version 5.0.0
-- https://www.phpmyadmin.net/
--
-- Host: localhost:3306
-- Generation Time: Jan 07, 2022 at 07:45 AM
-- Server version: 5.7.24
-- PHP Version: 7.4.11

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET AUTOCOMMIT = 0;
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `jinventaris`
--

-- --------------------------------------------------------

--
-- Table structure for table `tb_barang`
--

CREATE TABLE `tb_barang` (
  `id_barang` int(11) NOT NULL,
  `nama` varchar(30) NOT NULL,
  `jumlah` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `tb_barang`
--

INSERT INTO `tb_barang` (`id_barang`, `nama`, `jumlah`) VALUES
(5, 'Meja', 120),
(6, 'Kursi', 120),
(7, 'Laptop', 90),
(8, 'Komputer', 12),
(9, 'Keyboard', 90),
(10, 'Monitor', 12),
(11, 'CPU', 100),
(12, 'Air Conditioner', 133);

-- --------------------------------------------------------

--
-- Table structure for table `tb_peminjaman`
--

CREATE TABLE `tb_peminjaman` (
  `id_peminjaman` int(11) NOT NULL,
  `nama` varchar(50) NOT NULL,
  `no_telp` varchar(20) NOT NULL,
  `id_barang` int(11) NOT NULL,
  `jumlah` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Triggers `tb_peminjaman`
--
DELIMITER $$
CREATE TRIGGER `kembali_jumlah_barang` AFTER DELETE ON `tb_peminjaman` FOR EACH ROW UPDATE tb_barang SET jumlah = jumlah + OLD.jumlah WHERE tb_barang.id_barang = OLD.id_barang
$$
DELIMITER ;
DELIMITER $$
CREATE TRIGGER `update_jumlah_barang` AFTER INSERT ON `tb_peminjaman` FOR EACH ROW UPDATE tb_barang SET jumlah = jumlah - NEW.jumlah WHERE tb_barang.id_barang = NEW.id_barang
$$
DELIMITER ;
DELIMITER $$
CREATE TRIGGER `update_perubahan_jumlah` AFTER UPDATE ON `tb_peminjaman` FOR EACH ROW UPDATE tb_barang SET jumlah = jumlah + OLD.jumlah - NEW.jumlah WHERE tb_barang.id_barang = NEW.id_barang
$$
DELIMITER ;

--
-- Indexes for dumped tables
--

--
-- Indexes for table `tb_barang`
--
ALTER TABLE `tb_barang`
  ADD PRIMARY KEY (`id_barang`);

--
-- Indexes for table `tb_peminjaman`
--
ALTER TABLE `tb_peminjaman`
  ADD PRIMARY KEY (`id_peminjaman`),
  ADD KEY `fk_peminjaman` (`id_barang`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `tb_barang`
--
ALTER TABLE `tb_barang`
  MODIFY `id_barang` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=13;

--
-- AUTO_INCREMENT for table `tb_peminjaman`
--
ALTER TABLE `tb_peminjaman`
  MODIFY `id_peminjaman` int(11) NOT NULL AUTO_INCREMENT;

--
-- Constraints for dumped tables
--

--
-- Constraints for table `tb_peminjaman`
--
ALTER TABLE `tb_peminjaman`
  ADD CONSTRAINT `fk_peminjaman` FOREIGN KEY (`id_barang`) REFERENCES `tb_barang` (`id_barang`) ON DELETE CASCADE ON UPDATE CASCADE;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;

