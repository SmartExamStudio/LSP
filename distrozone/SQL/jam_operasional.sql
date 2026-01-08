-- =============================================
-- TABEL JAM OPERASIONAL - DistroZone
-- =============================================
-- Jalankan script ini di phpMyAdmin untuk membuat tabel

CREATE TABLE IF NOT EXISTS `tb_jam_operasional` (
    `id` INT NOT NULL AUTO_INCREMENT,
    `tipe` VARCHAR(20) NOT NULL COMMENT 'OFFLINE atau ONLINE',
    `jam_buka` VARCHAR(10) NOT NULL DEFAULT '10:00',
    `jam_tutup` VARCHAR(10) NOT NULL DEFAULT '20:00',
    `senin` BOOLEAN NOT NULL DEFAULT TRUE,
    `selasa` BOOLEAN NOT NULL DEFAULT TRUE,
    `rabu` BOOLEAN NOT NULL DEFAULT TRUE,
    `kamis` BOOLEAN NOT NULL DEFAULT TRUE,
    `jumat` BOOLEAN NOT NULL DEFAULT TRUE,
    `sabtu` BOOLEAN NOT NULL DEFAULT TRUE,
    `minggu` BOOLEAN NOT NULL DEFAULT TRUE,
    PRIMARY KEY (`id`),
    UNIQUE KEY `unique_tipe` (`tipe`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- Insert default data
-- Toko Offline: 10:00-20:00, Senin libur
INSERT INTO `tb_jam_operasional` (`tipe`, `jam_buka`, `jam_tutup`, `senin`, `selasa`, `rabu`, `kamis`, `jumat`, `sabtu`, `minggu`) 
VALUES ('OFFLINE', '10:00', '20:00', FALSE, TRUE, TRUE, TRUE, TRUE, TRUE, TRUE);

-- Toko Online: 10:00-17:00, setiap hari buka
INSERT INTO `tb_jam_operasional` (`tipe`, `jam_buka`, `jam_tutup`, `senin`, `selasa`, `rabu`, `kamis`, `jumat`, `sabtu`, `minggu`) 
VALUES ('ONLINE', '10:00', '17:00', TRUE, TRUE, TRUE, TRUE, TRUE, TRUE, TRUE);
