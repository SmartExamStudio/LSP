-- phpMyAdmin SQL Dump
-- version 5.2.0
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: Jan 08, 2026 at 06:47 AM
-- Server version: 10.4.27-MariaDB
-- PHP Version: 7.4.33

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `distrozone_db`
--

-- --------------------------------------------------------

--
-- Table structure for table `tb_admin`
--

CREATE TABLE `tb_admin` (
  `id_admin` int(11) NOT NULL,
  `nama` varchar(100) DEFAULT NULL,
  `username` varchar(50) DEFAULT NULL,
  `password` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `tb_admin`
--

INSERT INTO `tb_admin` (`id_admin`, `nama`, `username`, `password`) VALUES
(1, 'Administrator', 'admin', 'a665a45920422f9d417e4867efdc4fb8a04a1f3fff1fa07e998e86f7f7a27ae3'),
(2, NULL, 'admin', 'a665a45920422f9d417e4867efdc4fb8a04a1f3fff1fa07e998e86f7f7a27ae3');

-- --------------------------------------------------------

--
-- Table structure for table `tb_customer`
--

CREATE TABLE `tb_customer` (
  `id_customer` int(11) NOT NULL,
  `nama` varchar(100) DEFAULT NULL,
  `alamat` text DEFAULT NULL,
  `kota` varchar(100) DEFAULT NULL,
  `no_hp` varchar(20) DEFAULT NULL,
  `password` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Table structure for table `tb_detail_order`
--

CREATE TABLE `tb_detail_order` (
  `id_detail` int(11) NOT NULL,
  `id_order` int(11) DEFAULT NULL,
  `id_kaos` int(11) DEFAULT NULL,
  `jumlah` int(11) DEFAULT NULL,
  `subtotal` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Table structure for table `tb_detail_transaksi`
--

CREATE TABLE `tb_detail_transaksi` (
  `id_detail` int(11) NOT NULL,
  `id_transaksi` varchar(30) NOT NULL,
  `id_kaos` int(11) DEFAULT NULL,
  `nama_kaos` varchar(255) DEFAULT NULL,
  `jumlah` int(11) DEFAULT NULL,
  `harga_saat_transaksi` int(11) NOT NULL DEFAULT 0,
  `subtotal` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `tb_detail_transaksi`
--

INSERT INTO `tb_detail_transaksi` (`id_detail`, `id_transaksi`, `id_kaos`, `nama_kaos`, `jumlah`, `harga_saat_transaksi`, `subtotal`) VALUES
(5, 'TRX-20260107-001', 7, 'lois Lengan Panjang - hijau muda (M)', 1, 120000, 120000);

-- --------------------------------------------------------

--
-- Table structure for table `tb_kaos`
--

CREATE TABLE `tb_kaos` (
  `id_kaos` int(11) NOT NULL,
  `merek_kaos` varchar(100) DEFAULT NULL,
  `type_kaos` enum('Lengan Panjang','lengan Pendek') DEFAULT NULL,
  `warna_kaos` varchar(50) DEFAULT NULL,
  `size` enum('XS','S','M','L','XL','2XL','3XL','4XL','5XL') DEFAULT NULL,
  `harga_jual` int(11) DEFAULT NULL,
  `harga_pokok` int(11) DEFAULT NULL,
  `stok_kaos` int(11) DEFAULT NULL,
  `foto_kaos` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `tb_kaos`
--

INSERT INTO `tb_kaos` (`id_kaos`, `merek_kaos`, `type_kaos`, `warna_kaos`, `size`, `harga_jual`, `harga_pokok`, `stok_kaos`, `foto_kaos`) VALUES
(2, 'adidas', 'Lengan Panjang', 'ijo', 'XL', 120000, 100000, 50, NULL),
(3, 'batik', 'Lengan Panjang', 'coklat', 'M', 250000, 200000, 50, NULL),
(5, 'adidas', 'Lengan Panjang', 'black', 'XS', 200000, 190000, 100, 'RobloxScreenShot20251209_195504881.png'),
(6, 'nike', 'lengan Pendek', 'hitam', 'L', 150000, 120000, 16, NULL),
(7, 'lois', 'Lengan Panjang', 'hijau muda', 'M', 120000, 100000, 14, NULL),
(8, 'bata', 'Lengan Panjang', 'biru laut', 'XL', 240000, 200000, 5, NULL),
(11, 'nike', 'Lengan Panjang', 'biru laut', 'XL', 250000, 220000, 20, 'RobloxScreenShot20251227_100154453.png'),
(12, 'zara', 'lengan Pendek', 'hijau musa', 'XS', 100000, 80000, 15, '1767813250_RobloxScreenShot20251218_010112498.png');

-- --------------------------------------------------------

--
-- Table structure for table `tb_karyawan`
--

CREATE TABLE `tb_karyawan` (
  `id_karyawan` int(11) NOT NULL,
  `nama` varchar(100) DEFAULT NULL,
  `alamat` text DEFAULT NULL,
  `no_telepon` varchar(20) DEFAULT NULL,
  `NIK` varchar(20) DEFAULT NULL,
  `foto_karyawan` varchar(255) DEFAULT NULL,
  `username` varchar(50) DEFAULT NULL,
  `password` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `tb_karyawan`
--

INSERT INTO `tb_karyawan` (`id_karyawan`, `nama`, `alamat`, `no_telepon`, `NIK`, `foto_karyawan`, `username`, `password`) VALUES
(1, 'Budi', 'Jakarta', '08123456789', '123123123', '', 'kasir1', 'a665a45920422f9d417e4867efdc4fb8a04a1f3fff1fa07e998e86f7f7a27ae3'),
(2, 'Bahlil', 'Jakarta Utara', '081255674890', '12332468398', 'E:\\WhatsApp Image 2025-04-12 at 08.16.19.jpeg', 'Kasir2', 'etanol10%');

-- --------------------------------------------------------

--
-- Table structure for table `tb_order_online`
--

CREATE TABLE `tb_order_online` (
  `id_order` int(11) NOT NULL,
  `id_customer` int(11) DEFAULT NULL,
  `id_karyawan_verifikasi` int(11) DEFAULT NULL,
  `tanggal` datetime DEFAULT NULL,
  `status` enum('PEDING','ACC','DIKIRIM') DEFAULT 'PEDING',
  `total_barang` int(11) DEFAULT NULL,
  `total_harga_barang` int(11) DEFAULT NULL,
  `ongkir` int(11) DEFAULT NULL,
  `total_final` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Table structure for table `tb_transaksi`
--

CREATE TABLE `tb_transaksi` (
  `id_transaksi` varchar(30) NOT NULL,
  `id_karyawan` int(11) DEFAULT NULL,
  `tanggal` datetime DEFAULT NULL,
  `total_harga` int(11) DEFAULT NULL,
  `total_item` int(11) NOT NULL DEFAULT 0,
  `metode_pembayaran` enum('CASH','QRIS','TRANSFER') DEFAULT NULL,
  `status_transaksi` enum('SELESAI','DIBATALKAN') NOT NULL DEFAULT 'SELESAI'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `tb_transaksi`
--

INSERT INTO `tb_transaksi` (`id_transaksi`, `id_karyawan`, `tanggal`, `total_harga`, `total_item`, `metode_pembayaran`, `status_transaksi`) VALUES
('1', 1, '2025-12-29 17:43:55', 120000, 1, 'CASH', 'SELESAI'),
('10', 1, '2026-01-02 00:45:19', 120000, 1, 'CASH', 'SELESAI'),
('11', 1, '2026-01-02 00:47:15', 840000, 4, 'TRANSFER', 'SELESAI'),
('12', 1, '2026-01-05 01:53:22', 360000, 3, 'TRANSFER', 'SELESAI'),
('13', 1, '2026-01-05 08:50:03', 240000, 1, 'CASH', 'SELESAI'),
('14', 1, '2026-01-06 10:50:34', 480000, 4, 'QRIS', 'SELESAI'),
('15', 1, '2026-01-07 18:35:09', 120000, 1, 'CASH', 'SELESAI'),
('16', NULL, '2026-01-07 18:53:44', 10000, 0, NULL, 'SELESAI'),
('2', 1, '2025-12-29 17:44:32', 240000, 2, 'CASH', 'SELESAI'),
('3', 1, '2025-12-29 17:45:16', 4500000, 90, 'CASH', 'SELESAI'),
('4', 1, '2025-12-29 18:25:44', 120000, 1, 'QRIS', 'SELESAI'),
('5', 1, '2025-12-29 18:26:21', 450000, 9, 'TRANSFER', 'SELESAI'),
('6', 1, '2025-12-30 22:25:17', 150000, 1, 'CASH', 'SELESAI'),
('7', 1, '2025-12-30 23:11:59', 990000, 7, 'CASH', 'SELESAI'),
('8', 1, '2025-12-31 19:04:48', 150000, 1, 'CASH', 'SELESAI'),
('9', 1, '2026-01-02 00:43:42', 150000, 1, 'CASH', 'SELESAI'),
('TRX-20260107-001', 1, '2026-01-07 22:46:15', 120000, 1, 'CASH', 'SELESAI');

-- --------------------------------------------------------

--
-- Table structure for table `users`
--

CREATE TABLE `users` (
  `id_user` int(11) NOT NULL,
  `nama` varchar(100) NOT NULL,
  `username` varchar(50) NOT NULL,
  `password` varchar(255) NOT NULL,
  `role` enum('admin','kasir','customer') NOT NULL,
  `alamat` text DEFAULT NULL,
  `no_telepon` varchar(20) DEFAULT NULL,
  `created_at` timestamp NOT NULL DEFAULT current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Indexes for dumped tables
--

--
-- Indexes for table `tb_admin`
--
ALTER TABLE `tb_admin`
  ADD PRIMARY KEY (`id_admin`);

--
-- Indexes for table `tb_customer`
--
ALTER TABLE `tb_customer`
  ADD PRIMARY KEY (`id_customer`);

--
-- Indexes for table `tb_detail_order`
--
ALTER TABLE `tb_detail_order`
  ADD PRIMARY KEY (`id_detail`),
  ADD KEY `id_order` (`id_order`),
  ADD KEY `id_kaos` (`id_kaos`);

--
-- Indexes for table `tb_detail_transaksi`
--
ALTER TABLE `tb_detail_transaksi`
  ADD PRIMARY KEY (`id_detail`),
  ADD KEY `id_transaksi` (`id_transaksi`),
  ADD KEY `id_kaos` (`id_kaos`);

--
-- Indexes for table `tb_kaos`
--
ALTER TABLE `tb_kaos`
  ADD PRIMARY KEY (`id_kaos`);

--
-- Indexes for table `tb_karyawan`
--
ALTER TABLE `tb_karyawan`
  ADD PRIMARY KEY (`id_karyawan`);

--
-- Indexes for table `tb_order_online`
--
ALTER TABLE `tb_order_online`
  ADD PRIMARY KEY (`id_order`),
  ADD KEY `id_customer` (`id_customer`),
  ADD KEY `id_karyawan_verifikasi` (`id_karyawan_verifikasi`);

--
-- Indexes for table `tb_transaksi`
--
ALTER TABLE `tb_transaksi`
  ADD PRIMARY KEY (`id_transaksi`),
  ADD KEY `id_karyawan` (`id_karyawan`);

--
-- Indexes for table `users`
--
ALTER TABLE `users`
  ADD PRIMARY KEY (`id_user`),
  ADD UNIQUE KEY `username` (`username`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `tb_admin`
--
ALTER TABLE `tb_admin`
  MODIFY `id_admin` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=3;

--
-- AUTO_INCREMENT for table `tb_customer`
--
ALTER TABLE `tb_customer`
  MODIFY `id_customer` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `tb_detail_order`
--
ALTER TABLE `tb_detail_order`
  MODIFY `id_detail` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `tb_detail_transaksi`
--
ALTER TABLE `tb_detail_transaksi`
  MODIFY `id_detail` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=6;

--
-- AUTO_INCREMENT for table `tb_kaos`
--
ALTER TABLE `tb_kaos`
  MODIFY `id_kaos` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=13;

--
-- AUTO_INCREMENT for table `tb_karyawan`
--
ALTER TABLE `tb_karyawan`
  MODIFY `id_karyawan` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=3;

--
-- AUTO_INCREMENT for table `tb_order_online`
--
ALTER TABLE `tb_order_online`
  MODIFY `id_order` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `users`
--
ALTER TABLE `users`
  MODIFY `id_user` int(11) NOT NULL AUTO_INCREMENT;

--
-- Constraints for dumped tables
--

--
-- Constraints for table `tb_detail_order`
--
ALTER TABLE `tb_detail_order`
  ADD CONSTRAINT `tb_detail_order_ibfk_1` FOREIGN KEY (`id_order`) REFERENCES `tb_order_online` (`id_order`),
  ADD CONSTRAINT `tb_detail_order_ibfk_2` FOREIGN KEY (`id_kaos`) REFERENCES `tb_kaos` (`id_kaos`);

--
-- Constraints for table `tb_detail_transaksi`
--
ALTER TABLE `tb_detail_transaksi`
  ADD CONSTRAINT `fk_detail_transaksi` FOREIGN KEY (`id_transaksi`) REFERENCES `tb_transaksi` (`id_transaksi`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `tb_detail_transaksi_ibfk_2` FOREIGN KEY (`id_kaos`) REFERENCES `tb_kaos` (`id_kaos`) ON UPDATE CASCADE;

--
-- Constraints for table `tb_order_online`
--
ALTER TABLE `tb_order_online`
  ADD CONSTRAINT `tb_order_online_ibfk_1` FOREIGN KEY (`id_customer`) REFERENCES `tb_customer` (`id_customer`),
  ADD CONSTRAINT `tb_order_online_ibfk_2` FOREIGN KEY (`id_karyawan_verifikasi`) REFERENCES `tb_karyawan` (`id_karyawan`);

--
-- Constraints for table `tb_transaksi`
--
ALTER TABLE `tb_transaksi`
  ADD CONSTRAINT `tb_transaksi_ibfk_1` FOREIGN KEY (`id_karyawan`) REFERENCES `tb_karyawan` (`id_karyawan`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
