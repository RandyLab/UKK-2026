-- Struktur database yang sudah ada
CREATE DATABASE perpustakaan_db;
USE perpustakaan_db;

-- Tabel buku
CREATE TABLE buku (
    id_buku INT PRIMARY KEY AUTO_INCREMENT,
    judul VARCHAR(200) NOT NULL,
    penulis VARCHAR(100) NOT NULL,
    stok INT DEFAULT 1
);

-- Tabel anggota  
CREATE TABLE anggota (
    id_anggota INT PRIMARY KEY AUTO_INCREMENT,
    nama VARCHAR(100) NOT NULL,
    no_hp VARCHAR(15)
);

-- Tabel transaksi
CREATE TABLE transaksi (
    id_transaksi INT PRIMARY KEY AUTO_INCREMENT,
    id_anggota INT NOT NULL,
    id_buku INT NOT NULL,
    tanggal_pinjam DATE NOT NULL,
    tanggal_kembali DATE,
    status ENUM('dipinjam', 'dikembalikan', 'telat') DEFAULT 'dipinjam',
    denda INT DEFAULT 0,
    FOREIGN KEY (id_anggota) REFERENCES anggota(id_anggota),
    FOREIGN KEY (id_buku) REFERENCES buku(id_buku)
);

-- Insert data awal via CMD/Manual
INSERT INTO buku (judul, penulis, stok) VALUES 
('Pemrograman Java', 'Budi Santoso', 5),
('Database MySQL', 'Sari Dewi', 3),
('Struktur Data', 'Agus Wijaya', 2);

INSERT INTO anggota (nama, no_hp) VALUES
('Andi Pratama', '08123456789'),
('Rina Wijaya', '08129876543');