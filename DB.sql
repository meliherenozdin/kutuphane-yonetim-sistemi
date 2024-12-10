-- Kategori tablosunu oluşturuyor: Kitapların türlerini tutar (örneğin Roman, Bilim).
CREATE TABLE Kategori (
    KategoriID SERIAL PRIMARY KEY, -- Otomatik artan birincil anahtar
    Turu VARCHAR(255) NOT NULL    -- Kategorinin adı (zorunlu alan)
);

-- Yazar tablosunu oluşturuyor: Kitap yazarlarının bilgilerini tutar.
CREATE TABLE Yazar (
    YazarID SERIAL PRIMARY KEY,   -- Otomatik artan birincil anahtar
    YazarAdi VARCHAR(255) NOT NULL,  -- Yazarın adı (zorunlu alan)
    YazarSoyad VARCHAR(255) NOT NULL -- Yazarın soyadı (zorunlu alan)
);

-- Raf tablosunu oluşturuyor: Kütüphane raflarının bilgilerini tutar.
CREATE TABLE Raf (
    RafNo VARCHAR(50) PRIMARY KEY, -- Raf numarası (birincil anahtar)
    Bolum VARCHAR(255) NOT NULL    -- Rafın bulunduğu bölümün adı (zorunlu alan)
);

-- Kitap tablosunu oluşturuyor: Kütüphanedeki kitapların bilgilerini tutar.
CREATE TABLE Kitap (
    KitapID SERIAL PRIMARY KEY,             -- Otomatik artan birincil anahtar
    KitapAdi VARCHAR(255) NOT NULL,         -- Kitap adı (zorunlu alan)
    ISBN VARCHAR(13) UNIQUE NOT NULL,       -- Kitabın benzersiz ISBN numarası
    BasimYili DATE NOT NULL,                -- Kitabın basım yılı
    Yayinevi VARCHAR(255),                  -- Kitabı yayınlayan yayınevi
    Adet INT NOT NULL CHECK ("adet" >= 0),                      -- Kitabın mevcut adedi
    KategoriID INT REFERENCES Kategori(KategoriID) ON DELETE SET NULL, -- Kitabın kategorisi
    YazarID INT REFERENCES Yazar(YazarID) ON DELETE SET NULL,           -- Kitabın yazarı
    RafNo VARCHAR(50) REFERENCES Raf(RafNo) ON DELETE SET NULL          -- Kitabın bulunduğu raf
);

-- Üye tablosunu oluşturuyor: Kütüphaneye kayıtlı üyelerin bilgilerini tutar.
CREATE TABLE Uye (
    UyeID SERIAL PRIMARY KEY,         -- Otomatik artan birincil anahtar
    UyeAdi VARCHAR(255) NOT NULL,     -- Üyenin adı (zorunlu alan)
    UyeSoyad VARCHAR(255) NOT NULL,   -- Üyenin soyadı (zorunlu alan)
    Email VARCHAR(255) UNIQUE,        -- Üyenin benzersiz e-posta adresi
    TelNo VARCHAR(15) UNIQUE,         -- Üyenin benzersiz telefon numarası
    Adres TEXT                        -- Üyenin adres bilgisi
);

-- Personel tablosunu oluşturuyor: Kütüphane çalışanlarının bilgilerini tutar.
CREATE TABLE Personel (
    PersonelID SERIAL PRIMARY KEY,     -- Otomatik artan birincil anahtar
    PerAdi VARCHAR(255) NOT NULL,      -- Personelin adı (zorunlu alan)
    PerSoyad VARCHAR(255) NOT NULL,    -- Personelin soyadı (zorunlu alan)
    TelNo VARCHAR(15) UNIQUE,          -- Personelin telefon numarası
    Email VARCHAR(255) UNIQUE,         -- Personelin e-posta adresi
    IsBaslamaTarih DATE DEFAULT CURRENT_DATE -- Personelin işe başlama tarihi
);

-- Ödünç tablosunu oluşturuyor: Kitap ödünç alma işlemlerini tutar.
CREATE TABLE Odunc (
    OduncID SERIAL PRIMARY KEY,                -- Otomatik artan birincil anahtar
    UyeID INT REFERENCES Uye(UyeID) ON DELETE CASCADE, -- Ödünç alan üye
    KitapID INT REFERENCES Kitap(KitapID) ON DELETE CASCADE, -- Ödünç alınan kitap
    OduncAlmaTarih DATE NOT NULL DEFAULT CURRENT_DATE,       -- Ödünç alma tarihi
    GercekTeslimTarih DATE                                   -- Kitabın teslim tarihi
);

-- Ceza tablosunu oluşturuyor: Ödünç alma işlemlerinden kaynaklanan cezaları tutar.
CREATE TABLE Ceza (
    CezaID SERIAL PRIMARY KEY,                 -- Otomatik artan birincil anahtar
    OduncID INT REFERENCES Odunc(OduncID) ON DELETE CASCADE, -- Ceza ile ilişkili ödünç işlemi
    UyeID INT REFERENCES Uye(UyeID) ON DELETE CASCADE,       -- Ceza alan üye
    CezaMiktar DECIMAL(10, 2) NOT NULL,         -- Ceza miktarı (örneğin, 5.00 TL)
    CezaTarih DATE NOT NULL DEFAULT CURRENT_DATE -- Ceza tarihi
);

-- Rezervasyon tablosunu oluşturuyor: Üyelerin kitap rezervasyonlarını tutar.
CREATE TABLE Rezervasyon (
    RezervasyonID SERIAL PRIMARY KEY,                   -- Otomatik artan birincil anahtar
    UyeID INT REFERENCES Uye(UyeID) ON DELETE CASCADE,  -- Rezervasyonu yapan üye
    KitapID INT REFERENCES Kitap(KitapID) ON DELETE CASCADE, -- Rezerve edilen kitap
    RezervasyonTarih DATE NOT NULL DEFAULT CURRENT_DATE, -- Rezervasyon tarihi
    Durum VARCHAR(50) DEFAULT 'Boşta',                  -- Rezervasyon durumu
    UNIQUE (KitapID, Durum)                             -- Kitap için yalnızca bir aktif rezervasyon
);

-- Bağış tablosunu oluşturuyor: Üyelerin kitap bağışlarını tutar.
CREATE TABLE Bagis (
    BagisID SERIAL PRIMARY KEY,              -- Otomatik artan birincil anahtar
    UyeID INT REFERENCES Uye(UyeID) ON DELETE CASCADE, -- Bağış yapan üye
    KitapID INT REFERENCES Kitap(KitapID) ON DELETE CASCADE, -- Bağışlanan kitap
    BagisTarih DATE NOT NULL DEFAULT CURRENT_DATE -- Bağış tarihi
);


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

INSERT INTO Ceza (OduncID, UyeID, CezaMiktar) VALUES
(1, 10, 5.00),
(2, 9, 10.00),
(3, 5, 3.00),
(4, 4, 0.00),
(5, 3, 0.00),
(6, 8, 7.00),
(7, 7, 2.50),
(8, 6, 1.00),
(9, 2, 0.00),
(10, 1, 0.00);

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

