CREATE TABLE Kategori (
    KategoriID SERIAL PRIMARY KEY,
    Turu VARCHAR(255) NOT NULL
);

CREATE TABLE Yazar (
    YazarID SERIAL PRIMARY KEY,
    YazarAdi VARCHAR(255) NOT NULL,
    YazarSoyad VARCHAR(255) NOT NULL
);

CREATE TABLE Kitap (
    KitapID SERIAL PRIMARY KEY,
    KitapAdi VARCHAR(255) NOT NULL,
    ISBN VARCHAR(13) UNIQUE NOT NULL,
    BasimYili DATE NOT NULL,
    Yayinevi VARCHAR(255),
    Adet INT NOT NULL,
    KategoriID INT REFERENCES Kategori(KategoriID) ON DELETE SET NULL,
    YazarID INT REFERENCES Yazar(YazarID) ON DELETE SET NULL,
    RafNo VARCHAR(50) REFERENCES Raf(RafNo) ON DELETE SET NULL
);

CREATE TABLE Raf (
    RafNo VARCHAR(50) PRIMARY KEY,
    Bolum VARCHAR(255) NOT NULL
);

CREATE TABLE Uye (
    UyeID SERIAL PRIMARY KEY,
    UyeAdi VARCHAR(255) NOT NULL,
    UyeSoyad VARCHAR(255) NOT NULL,
    Email VARCHAR(255) UNIQUE,
    TelNo VARCHAR(15),
    KayitTarih DATE DEFAULT CURRENT_DATE,
    Adres TEXT
);

CREATE TABLE Personel (
    PersonelID SERIAL PRIMARY KEY,
    PerAdi VARCHAR(255) NOT NULL,
    PerSoyad VARCHAR(255) NOT NULL,
    TelNo VARCHAR(15),
    Email VARCHAR(255) UNIQUE,
    IsBaslamaTarih DATE DEFAULT CURRENT_DATE
);

CREATE TABLE Odunc (
    OduncID SERIAL PRIMARY KEY,
    UyeID INT REFERENCES Uye(UyeID) ON DELETE CASCADE,
    KitapID INT REFERENCES Kitap(KitapID) ON DELETE CASCADE,
    OduncAlmaTarih DATE NOT NULL DEFAULT CURRENT_DATE,
    GercekTeslimTarih DATE
);

CREATE TABLE Ceza (
    CezaID SERIAL PRIMARY KEY,
    OduncID INT REFERENCES Odunc(OduncID) ON DELETE CASCADE,
    CezaMiktar DECIMAL(10, 2) NOT NULL,
    CezaTarih DATE NOT NULL DEFAULT CURRENT_DATE
);

CREATE TABLE Rezervasyon (
    RezervasyonID SERIAL PRIMARY KEY,
    UyeID INT REFERENCES Uye(UyeID) ON DELETE CASCADE,
    KitapID INT REFERENCES Kitap(KitapID) ON DELETE CASCADE,
    RezervasyonTarih DATE NOT NULL DEFAULT CURRENT_DATE
);

CREATE TABLE Bagis (
    BagisID SERIAL PRIMARY KEY,
    KitapID INT REFERENCES Kitap(KitapID) ON DELETE CASCADE,
    BagisTarih DATE NOT NULL DEFAULT CURRENT_DATE
);

INSERT INTO Kategori (Turu) VALUES
('Roman'),
('Bilim'),
('Sanat'),
('Tarih'),
('Felsefe'),
('Teknoloji'),
('Psikoloji'),
('Edebiyat'),
('Çocuk'),
('Fantastik');

INSERT INTO Yazar (YazarAdi, YazarSoyad) VALUES
('Orhan', 'Pamuk'),
('Elif', 'Şafak'),
('J.K.', 'Rowling'),
('Fyodor', 'Dostoyevski'),
('Albert', 'Camus'),
('Yuval Noah', 'Harari'),
('George', 'Orwell'),
('Virginia', 'Woolf'),
('Stefan', 'Zweig'),
('J.R.R.', 'Tolkien');

INSERT INTO Raf (RafNo, Bolum) VALUES
('R1', 'Roman Bölümü'),
('R2', 'Fantastik Bölümü'),
('R3', 'Bilim Kurgu Bölümü'),
('R4', 'Tarih Bölümü'),
('R5', 'Sanat Bölümü'),
('R6', 'Teknoloji Bölümü'),
('R7', 'Çocuk Kitapları Bölümü'),
('R8', 'Edebiyat Bölümü'),
('R9', 'Felsefe Bölümü'),
('R10', 'Psikoloji Bölümü');

INSERT INTO Kitap (KitapAdi, ISBN, BasimYili, Yayinevi, Adet, KategoriID, YazarID, RafNo) VALUES
('Kar', '9789750800000', '2002-01-01', 'YKY', 5, 1, 1, 'R1'),
('Harry Potter ve Felsefe Taşı', '9780545582889', '1997-06-26', 'Scholastic', 10, 10, 3, 'R2'),
('1984', '9780141036144', '1949-06-08', 'Penguin', 7, 7, 7, 'R3'),
('Sefiller', '9780140444308', '1862-01-01', 'Penguin', 6, 4, 2, 'R4'),
('İnsanlığın Geleceği', '9780062316097', '2015-01-01', 'Harper', 3, 6, 6, 'R5'),
('Dönüşüm', '9780156628709', '1915-01-01', 'Schocken', 4, 5, 5, 'R6'),
('Körlük', '9780156007757', '1995-10-01', 'Harvest', 8, 8, 8, 'R7'),
('Monte Cristo Kontu', '9780140449266', '1844-01-01', 'Penguin', 9, 1, 9, 'R8'),
('Silmarillion', '9780618391110', '1977-01-01', 'Houghton', 6, 10, 10, 'R9'),
('Kumral Ada Mavi Tuna', '9789750809012', '1997-06-01', 'YKY', 2, 1, 1, 'R10');

INSERT INTO Uye (UyeAdi, UyeSoyad, Email, TelNo, Adres) VALUES
('Ahmet', 'Demir', 'ahmet@gmail.com', '05551112233', 'Ankara'),
('Ayşe', 'Yıldız', 'ayse@gmail.com', '05556667788', 'İstanbul'),
('Mehmet', 'Kaya', 'mehmet@gmail.com', '05337779911', 'İzmir'),
('Fatma', 'Çelik', 'fatma@gmail.com', '05336668899', 'Bursa'),
('Ali', 'Yılmaz', 'ali@gmail.com', '05335557722', 'Antalya'),
('Zeynep', 'Şahin', 'zeynep@gmail.com', '05324446677', 'Adana'),
('Emre', 'Turan', 'emre@gmail.com', '05443332255', 'Gaziantep'),
('Ece', 'Aydın', 'ece@gmail.com', '05443332266', 'Trabzon'),
('Hüseyin', 'Yıldırım', 'huseyin@gmail.com', '05447778899', 'Mersin'),
('Ceren', 'Demirtaş', 'ceren@gmail.com', '05321112233', 'Kocaeli');

INSERT INTO Personel (PerAdi, PerSoyad, TelNo, Email) VALUES
('Mehmet', 'Kaya', '05336668899', 'mehmet.kaya@kutuphane.com'),
('Ayşe', 'Demir', '05336668877', 'ayse.demir@kutuphane.com'),
('Fatma', 'Turan', '05331112233', 'fatma.turan@kutuphane.com'),
('Ali', 'Yıldız', '05332224455', 'ali.yildiz@kutuphane.com'),
('Ahmet', 'Şahin', '05337778899', 'ahmet.sahin@kutuphane.com'),
('Zeynep', 'Kara', '05333334455', 'zeynep.kara@kutuphane.com'),
('Emre', 'Aydın', '05336663377', 'emre.aydin@kutuphane.com'),
('Ece', 'Çelik', '05331119955', 'ece.celik@kutuphane.com'),
('Hüseyin', 'Demirtaş', '05335557766', 'huseyin.demirtas@kutuphane.com'),
('Ceren', 'Kılıç', '05333335588', 'ceren.kilic@kutuphane.com');

INSERT INTO Odunc (UyeID, KitapID, OduncAlmaTarih) VALUES
(1, 1, '2024-11-01'),
(2, 2, '2024-11-05'),
(3, 3, '2024-11-07'),
(4, 4, '2024-11-10'),
(5, 5, '2024-11-12'),
(6, 6, '2024-11-13'),
(7, 7, '2024-11-14'),
(8, 8, '2024-11-15'),
(9, 9, '2024-11-16'),
(10, 10, '2024-11-17');

INSERT INTO Ceza (OduncID, CezaMiktar) VALUES
(1, 5.00),
(2, 10.00),
(3, 3.00),
(4, 0.00),
(5, 0.00),
(6, 7.00),
(7, 2.50),
(8, 1.00),
(9, 0.00),
(10, 0.00);

INSERT INTO Rezervasyon (UyeID, KitapID, RezervasyonTarih) VALUES
(1, 5, '2024-11-01'),
(2, 6, '2024-11-02'),
(3, 7, '2024-11-03'),
(4, 8, '2024-11-04'),
(5, 9, '2024-11-05'),
(6, 10, '2024-11-06'),
(7, 1, '2024-11-07'),
(8, 2, '2024-11-08'),
(9, 3, '2024-11-09'),
(10, 4, '2024-11-10');

INSERT INTO Bagis (KitapID, BagisTarih) VALUES
(1, '2024-10-01'),
(2, '2024-10-05'),
(3, '2024-10-07'),
(4, '2024-10-10'),
(5, '2024-10-12'),
(6, '2024-10-13'),
(7, '2024-10-14'),
(8, '2024-10-15'),
(9, '2024-10-16'),
(10, '2024-10-17');