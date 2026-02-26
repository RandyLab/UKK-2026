// script.js

// ==================== CLASS BUKU ====================
class Buku {
  constructor(isbn, judul, penerbit, tahun, cover) {
    this.isbn = isbn;
    this.judul = judul;
    this.penerbit = penerbit;
    this.tahun = tahun;
    this.cover = cover;
  }
}

// ==================== ARRAY DATA ====================
let daftarBuku = [];
daftarBuku.push(
  new Buku("9786231809223", "PKK Kelas 12", "Stone", "2006", "covers/pkk.jpg")
);
daftarBuku.push(
  new Buku("9786022446576", "PPKN", "PEAK", "2007", "covers/pkn.jpg")
);
daftarBuku.push(
  new Buku(
    "6941798464992",
    "Buku Catatan",
    "Elitis",
    "2008",
    "covers/buku_Catatan.jpg"
  )
);
daftarBuku.push(
  new Buku(
    "208386213",
    "Buku Tulis Samuel",
    "Elitis",
    "2008",
    "covers/buku_tulis.jpg"
  )
);
daftarBuku.push(
  new Buku("9786029053265", "MPR", "Elitis", "2008", "covers/mpr.jpg")
);
daftarBuku.push(
  new Buku(
    "9786025305740",
    "Filsafat Dalam Berbagai Perspektif",
    "Elitis",
    "2008",
    "covers/filsafat.jpg"
  )
);
daftarBuku.push(
  new Buku(
    "9786232422971",
    "Realm Breaker",
    "Elitis",
    "2008",
    "covers/realm_breaker.jpg"
  )
);
daftarBuku.push(
  new Buku("6941256125632", "Wireless Rat", "Elitis", "2008", "covers/AM.jpg")
);
daftarBuku.push(
  new Buku(
    "8991389260036",
    "MISS YOU KING",
    "Elitis",
    "2008",
    "covers/metamorphosis.jpg"
  )
);
daftarBuku.push(
  new Buku(
    "119480002479",
    "RAJIN PANDAI",
    "Elitis",
    "2008",
    "covers/death_note.jpg"
  )
);

// Data Peminjaman (array untuk tabel)
let peminjaman = [];

// Variabel untuk menyimpan buku yang dipilih sementara
let bukuDipilih = [];

// ==================== ELEMEN DOM ====================
const kodePinjamInput = document.getElementById("kodePinjam");
const tglPinjamInput = document.getElementById("tglPinjam");
const namaAnggotaInput = document.getElementById("namaAnggota");
const jumlahBukuInput = document.getElementById("jumlahBuku");
const tglKembaliInput = document.getElementById("tglKembali");
const lamaPinjamInput = document.getElementById("lamaPinjam");
const dendaInput = document.getElementById("denda");
const isbnInput = document.getElementById("isbn");
const judulInput = document.getElementById("judul");
const penerbitInput = document.getElementById("penerbit");
const tahunInput = document.getElementById("tahun");
const coverImage = document.getElementById("coverImage");
const tambahBukuBtn = document.getElementById("tambahBuku");
const tbody = document.getElementById("tbodyPinjam");
const simpanBtn = document.getElementById("simpan");
const bersihkanBtn = document.getElementById("bersihkan");
const keluarBtn = document.getElementById("keluar");

// ==================== FUNGSI BANTU ====================
// Fungsi untuk menghitung hari kerja (Senin-Jumat)
function hitungHariKerja(tglPinjam, tglKembali) {
  if (!tglPinjam || !tglKembali) return 0;

  const start = new Date(tglPinjam);
  const end = new Date(tglKembali);

  // Reset time to avoid DST issues
  start.setHours(0, 0, 0, 0);
  end.setHours(0, 0, 0, 0);

  let kerjaCount = 0;
  let current = new Date(start);

  while (current <= end) {
    const hari = current.getDay(); // 0 = Minggu, 1 = Senin, ..., 6 = Sabtu
    if (hari >= 1 && hari <= 5) {
      // Senin - Jumat
      kerjaCount++;
    }
    current.setDate(current.getDate() + 1);
  }

  return kerjaCount;
}

function hitungLamaPinjam(tglPinjam, tglKembali) {
  if (!tglPinjam || !tglKembali) return 0;
  const start = new Date(tglPinjam);
  const end = new Date(tglKembali);
  const diffTime = Math.abs(end - start);
  const diffDays = Math.ceil(diffTime / (1000 * 60 * 60 * 24));
  return diffDays;
}

function hitungDenda(tglPinjam, tglKembali) {
  const lama = hitungLamaPinjam(tglPinjam, tglKembali);

  // Denda setelah lewat 5 hari (hari kerja)
  if (lama <= 5) return 0;

  // Hitung hari kerja setelah hari ke-5
  const start = new Date(tglPinjam);
  start.setDate(start.getDate() + 5); // Mulai hari ke-6

  const end = new Date(tglKembali);

  let hariKerjaTelat = 0;
  let current = new Date(start);

  while (current <= end) {
    const hari = current.getDay();
    if (hari >= 1 && hari <= 5) {
      // Senin - Jumat
      hariKerjaTelat++;
    }
    current.setDate(current.getDate() + 1);
  }

  return hariKerjaTelat * 5000;
}

// Update field lama pinjam dan denda berdasarkan tanggal
function updateLamaDanDenda() {
  const lama = hitungLamaPinjam(tglPinjamInput.value, tglKembaliInput.value);
  lamaPinjamInput.value = lama + " Hari";

  const denda = hitungDenda(tglPinjamInput.value, tglKembaliInput.value);
  dendaInput.value = denda;
}

// Merender tabel dari array peminjaman (dengan gambar cover)
function renderTabel() {
  let html = "";
  peminjaman.forEach((p) => {
    // Cari buku berdasarkan ISBN yang tersimpan
    const buku1 = daftarBuku.find((b) => b.isbn === p.isbn1);
    const buku2 = daftarBuku.find((b) => b.isbn === p.isbn2);

    html += `<tr>
            <td>${p.kode}</td>
            <td>${p.tglPinjam}</td>
            <td>${p.nama}</td>
            <td><img class="cover-table" src="${
              buku1 ? buku1.cover : "covers/no-image.jpg"
            }" alt="cover" onerror="this.src='covers/no-image.jpg'"></td>
            <td><img class="cover-table" src="${
              buku2 ? buku2.cover : "covers/no-image.jpg"
            }" alt="cover" onerror="this.src='covers/no-image.jpg'"></td>
            <td>${p.tglKembali}</td>
            <td>Rp ${p.denda.toLocaleString("id-ID")}</td>
        </tr>`;
  });
  tbody.innerHTML = html;
}

// Reset form buku (bawah)
function resetFormBuku() {
  isbnInput.value = "";
  judulInput.value = "";
  penerbitInput.value = "";
  tahunInput.value = "";
  coverImage.src = "covers/no-image.jpg";
}

// Reset semua input (kecuali tabel)
function resetSemuaForm() {
  kodePinjamInput.value = "";
  tglPinjamInput.value = "";
  namaAnggotaInput.value = "";
  jumlahBukuInput.value = "1";
  tglKembaliInput.value = "";
  lamaPinjamInput.value = "0 Hari";
  dendaInput.value = "0";
  resetFormBuku();
  bukuDipilih = []; // kosongkan buku yang dipilih
}

// Validasi kode pinjam tidak duplikat
function isKodePinjamUnique(kode) {
  return !peminjaman.some((p) => p.kode === kode);
}

// Validasi jumlah buku sesuai input
function validateJumlahBuku() {
  const jumlah = parseInt(jumlahBukuInput.value);
  if (isNaN(jumlah) || jumlah < 1 || jumlah > 2) {
    alert("Jumlah buku harus antara 1 - 2");
    return false;
  }
  return true;
}

// ==================== EVENT LISTENER ====================
// Validasi jumlah buku saat diinput
jumlahBukuInput.addEventListener("input", function () {
  let val = parseInt(this.value);
  if (isNaN(val) || val < 1) this.value = 1;
  if (val > 2) this.value = 2;
});

// Saat ISBN diinput, cari di daftarBuku dan tampilkan cover
isbnInput.addEventListener("input", function (e) {
  const isbn = e.target.value.trim();
  const buku = daftarBuku.find((b) => b.isbn === isbn);
  if (buku) {
    judulInput.value = buku.judul;
    penerbitInput.value = buku.penerbit;
    tahunInput.value = buku.tahun;
    coverImage.src = buku.cover;
    // Jika gambar error, ganti dengan no-image.jpg
    coverImage.onerror = function () {
      this.src = "covers/no-image.jpg";
    };
  } else {
    judulInput.value = "";
    penerbitInput.value = "";
    tahunInput.value = "";
    coverImage.src = "covers/no-image.jpg";
  }
});

// Tombol Tambah (menambah buku ke daftar sementara)
tambahBukuBtn.addEventListener("click", function () {
  const isbn = isbnInput.value.trim();
  if (!isbn) {
    alert("Masukkan ISBN terlebih dahulu!");
    return;
  }

  const buku = daftarBuku.find((b) => b.isbn === isbn);
  if (!buku) {
    alert("Buku dengan ISBN tersebut tidak ditemukan!");
    return;
  }

  // Validasi jumlah maksimal buku yang dipilih
  const maxBuku = parseInt(jumlahBukuInput.value);
  if (bukuDipilih.length >= maxBuku) {
    alert(`Maksimal hanya bisa memilih ${maxBuku} buku!`);
    return;
  }

  // Cek duplikasi ISBN
  if (bukuDipilih.some((b) => b.isbn === isbn)) {
    alert("Buku dengan ISBN ini sudah ditambahkan!");
    return;
  }

  bukuDipilih.push(buku);
  alert(`Buku "${buku.judul}" ditambahkan. (${bukuDipilih.length}/${maxBuku})`);

  // Kosongkan input ISBN setelah berhasil tambah
  resetFormBuku();
});

// Saat tanggal berubah, hitung lama dan denda
tglPinjamInput.addEventListener("change", updateLamaDanDenda);
tglKembaliInput.addEventListener("change", updateLamaDanDenda);

// Tombol SIMPAN
simpanBtn.addEventListener("click", function () {
  // Validasi kode pinjam
  const kodePinjam = kodePinjamInput.value.trim();
  if (!kodePinjam) {
    alert("Kode pinjam harus diisi!");
    return;
  }

  if (!isKodePinjamUnique(kodePinjam)) {
    alert("Kode pinjam sudah digunakan!");
    return;
  }

  // Validasi jumlah buku
  if (!validateJumlahBuku()) return;

  // Validasi field lainnya
  if (!tglPinjamInput.value) {
    alert("Tanggal pinjam harus diisi!");
    return;
  }
  if (!namaAnggotaInput.value) {
    alert("Nama anggota harus diisi!");
    return;
  }

  const maxBuku = parseInt(jumlahBukuInput.value);
  if (bukuDipilih.length !== maxBuku) {
    alert(`Anda harus memilih ${maxBuku} buku!`);
    return;
  }

  if (!tglKembaliInput.value) {
    alert("Tanggal kembali harus diisi!");
    return;
  }

  const denda = hitungDenda(tglPinjamInput.value, tglKembaliInput.value);

  // Siapkan objek peminjaman
  const newPinjam = {
    kode: kodePinjam,
    tglPinjam: tglPinjamInput.value,
    nama: namaAnggotaInput.value,
    isbn1: bukuDipilih[0] ? bukuDipilih[0].isbn : "",
    isbn2: bukuDipilih[1] ? bukuDipilih[1].isbn : "",
    tglKembali: tglKembaliInput.value,
    denda: denda,
  };

  // Simpan ke array
  peminjaman.push(newPinjam);
  renderTabel();

  // Reset form setelah simpan
  resetSemuaForm();
  alert("Data peminjaman tersimpan!");
});

// Tombol BERSIHKAN (hanya form input, tidak hapus tabel)
bersihkanBtn.addEventListener("click", function () {
  if (confirm("Bersihkan form input?")) {
    resetSemuaForm();
  }
});

// Tombol KELUAR (keluar dari aplikasi)
keluarBtn.addEventListener("click", function () {
  if (confirm("Yakin ingin keluar?")) {
    window.close();
    window.location.href = "about:blank";
  }
});

// Inisialisasi
resetSemuaForm();
