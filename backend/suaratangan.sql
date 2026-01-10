-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Host: localhost
-- Generation Time: Jan 10, 2026 at 05:39 PM
-- Server version: 10.4.28-MariaDB
-- PHP Version: 8.2.4

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `suaratangan`
--

-- --------------------------------------------------------

--
-- Table structure for table `kotak_saran`
--

CREATE TABLE `kotak_saran` (
  `id` int(11) NOT NULL,
  `user_id` int(11) NOT NULL,
  `saran` text NOT NULL,
  `tanggal` timestamp NOT NULL DEFAULT current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `kotak_saran`
--

INSERT INTO `kotak_saran` (`id`, `user_id`, `saran`, `tanggal`) VALUES
(1, 8, 'Bismillah PAB dapet A++ :D', '2026-01-07 17:51:54'),
(2, 11, 'Puji Tuhan kelar', '2026-01-07 17:55:39'),
(3, 9, 'Tetap semangat, yaa!', '2026-01-07 17:58:44');

-- --------------------------------------------------------

--
-- Table structure for table `laporan_masalah`
--

CREATE TABLE `laporan_masalah` (
  `id` int(11) NOT NULL,
  `user_id` int(11) NOT NULL,
  `judul` varchar(255) NOT NULL,
  `deskripsi` text NOT NULL,
  `tanggal` timestamp NOT NULL DEFAULT current_timestamp(),
  `status` enum('pending','resolved') DEFAULT 'pending'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `laporan_masalah`
--

INSERT INTO `laporan_masalah` (`id`, `user_id`, `judul`, `deskripsi`, `tanggal`, `status`) VALUES
(2, 11, 'Bug dalam Header Notifikasi', 'Header notifikasi berwarna hijau. Tidak selaras dengan tema aplikasi secara keseluruhan..', '2026-01-07 17:40:51', 'pending'),
(3, 8, 'Terjemahan Kurang Akurat', 'Hasil penerjemahan tidak sesuai dengan arti bahasanya.', '2026-01-07 17:42:44', 'pending'),
(4, 9, 'Aplikasi Terlalu Bagus', ':)', '2026-01-07 17:44:03', 'pending'),
(5, 14, 'Laporan', 'Contoh Masalah', '2026-01-07 18:58:12', 'pending');

-- --------------------------------------------------------

--
-- Table structure for table `users`
--

CREATE TABLE `users` (
  `id` int(11) NOT NULL,
  `nama_lengkap` varchar(100) NOT NULL,
  `username` varchar(16) NOT NULL,
  `email` varchar(100) NOT NULL,
  `telepon` varchar(12) NOT NULL,
  `password` varchar(255) NOT NULL,
  `peran` varchar(255) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `users`
--

INSERT INTO `users` (`id`, `nama_lengkap`, `username`, `email`, `telepon`, `password`, `peran`) VALUES
(8, 'Teresa Kaena Dharmanyoto', 'kaee', 'teresa@mail.com', '081282825601', '$2y$10$TW72YWW5Eq1lRl1FxxBKYe6kdS/SssqW0NFSHUKhIE28OaqTDoTFu', 'Teman Tuli'),
(9, 'Runi Dwi Jiasta', 'runi', 'runi@mail.com', '082222222222', '$2y$10$xMtW4TGJievwGE.p5YpUZen/ZspuJQ.vhECKk4eLC0kxp1C8sE0zW', 'Teman Dengar'),
(10, 'Andrew Riza Rafhael', 'andrew', 'andrew@mail.com', '081111111111', '$2y$10$2gl6QhSaWnWUQs0TW/QMo.QuoVpnwyAv2yyuHnE2ULkl5V8.WMixm', 'Teman Tuli'),
(11, 'Caroline Evarista Den Lau', 'evaa', 'evaa@mail.com', '083333333333', '$2y$10$FVQ34ZNrmImP073qO83.DOKCMJgeomsfFwcrHj3z4MIph5gNFO4Ee', 'Teman Dengar'),
(14, 'Test', 'test', 'test@mail.com', '081282825601', '$2y$10$4GTcio6Tpr8UyjvHBiMjb.vqbu3ad8vPSFYs2CWo8LbJlqxjdKCKe', 'Teman Tuli');

--
-- Indexes for dumped tables
--

--
-- Indexes for table `kotak_saran`
--
ALTER TABLE `kotak_saran`
  ADD PRIMARY KEY (`id`),
  ADD KEY `user_id` (`user_id`);

--
-- Indexes for table `laporan_masalah`
--
ALTER TABLE `laporan_masalah`
  ADD PRIMARY KEY (`id`),
  ADD KEY `user_id` (`user_id`);

--
-- Indexes for table `users`
--
ALTER TABLE `users`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `username` (`username`),
  ADD UNIQUE KEY `email` (`email`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `kotak_saran`
--
ALTER TABLE `kotak_saran`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=4;

--
-- AUTO_INCREMENT for table `laporan_masalah`
--
ALTER TABLE `laporan_masalah`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=6;

--
-- AUTO_INCREMENT for table `users`
--
ALTER TABLE `users`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=15;

--
-- Constraints for dumped tables
--

--
-- Constraints for table `kotak_saran`
--
ALTER TABLE `kotak_saran`
  ADD CONSTRAINT `kotak_saran_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`) ON DELETE CASCADE;

--
-- Constraints for table `laporan_masalah`
--
ALTER TABLE `laporan_masalah`
  ADD CONSTRAINT `laporan_masalah_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`) ON DELETE CASCADE;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
