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

// ==================== DATA ====================
let daftarBuku = [
  new Buku("9786231809223", "PKK Kelas 12", "Stone", "2006", "covers/pkk.jpg"),
  new Buku("9786022446576", "PPKN", "PEAK", "2007", "covers/pkn.jpg"),
  new Buku(
    "6941798464992",
    "Buku Catatan",
    "Elitis",
    "2008",
    "covers/buku_Catatan.jpg"
  ),
  new Buku(
    "208386213",
    "Buku Tulis Samuel",
    "Elitis",
    "2008",
    "covers/buku_tulis.jpg"
  ),
  new Buku("9786029053265", "MPR", "Elitis", "2008", "covers/mpr.jpg"),
  new Buku(
    "9786025305740",
    "Filsafat Dalam Berbagai Perspektif",
    "Elitis",
    "2008",
    "covers/filsafat.jpg"
  ),
  new Buku(
    "9786232422971",
    "Realm Breaker",
    "Elitis",
    "2008",
    "covers/realm_breaker.jpg"
  ),
  new Buku("6941256125632", "Wireless Rat", "Elitis", "2008", "covers/AM.jpg"),
  new Buku(
    "8991389260036",
    "MISS YOU KING",
    "Elitis",
    "2008",
    "covers/metamorphosis.jpg"
  ),
  new Buku(
    "119480002479",
    "RAJIN PANDAI",
    "Elitis",
    "2008",
    "covers/death_note.jpg"
  ),
];

let peminjaman = [];
let bukuDipilih = [];

// ==================== ELEMENT ====================
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

const tbodyBukuDipilih = document.getElementById("tbodyBukuDipilih");
const tbodyPinjam = document.getElementById("tbodyPinjam");

const simpanBtn = document.getElementById("simpan");
const bersihkanBtn = document.getElementById("bersihkan");
const keluarBtn = document.getElementById("keluar");

// ==================== FUNGSI ====================
function hitungLamaPinjam(tglPinjam, tglKembali) {
  if (!tglPinjam || !tglKembali) return 0;
  const start = new Date(tglPinjam);
  const end = new Date(tglKembali);
  const diffTime = Math.abs(end - start);
  return Math.ceil(diffTime / (1000 * 60 * 60 * 24));
}

function hitungDenda(tglPinjam, tglKembali) {
  const lama = hitungLamaPinjam(tglPinjam, tglKembali);
  if (lama <= 5) return 0;
  const start = new Date(tglPinjam);
  start.setDate(start.getDate() + 5);
  const end = new Date(tglKembali);
  let hariKerjaTelat = 0;
  let current = new Date(start);
  while (current <= end) {
    const hari = current.getDay();
    if (hari >= 1 && hari <= 5) hariKerjaTelat++;
    current.setDate(current.getDate() + 1);
  }
  return hariKerjaTelat * 5000;
}

function updateLamaDanDenda() {
  lamaPinjamInput.value =
    hitungLamaPinjam(tglPinjamInput.value, tglKembaliInput.value) + " Hari";
  dendaInput.value = hitungDenda(tglPinjamInput.value, tglKembaliInput.value);
}

function renderTabelBukuDipilih() {
  let html = "";
  bukuDipilih.forEach((buku, index) => {
    html += `<tr>
        <td>${buku.isbn}</td>
        <td>${buku.judul}</td>
        <td>${buku.penerbit}</td>
        <td>${buku.tahun}</td>
        <td><img class="cover-table" src="${buku.cover}" alt="cover" onerror="this.src='covers/no-image.jpg'"></td>
        <td><button class="hapus-buku" data-index="${index}">Hapus</button></td>
      </tr>`;
  });
  tbodyBukuDipilih.innerHTML = html;

  document.querySelectorAll(".hapus-buku").forEach((btn) => {
    btn.addEventListener("click", function () {
      bukuDipilih.splice(parseInt(this.dataset.index), 1);
      renderTabelBukuDipilih();
    });
  });
}

function renderTabelPinjam() {
  let html = "";
  peminjaman.forEach((p, index) => {
    const buku1 = daftarBuku.find((b) => b.isbn === p.isbn1);
    const buku2 = daftarBuku.find((b) => b.isbn === p.isbn2);
    const buku3 = daftarBuku.find((b) => b.isbn === p.isbn3);
    const buku4 = daftarBuku.find((b) => b.isbn === p.isbn4);
    const buku5 = daftarBuku.find((b) => b.isbn === p.isbn5);

    html += `<tr>
        <td>${p.kode}</td>
        <td>${p.tglPinjam}</td>
        <td>${p.nama}</td>
        <td><img class="cover-table" src="${
          buku1 ? buku1.cover : "covers/no-image.jpg"
        }" alt="cover"></td>
        <td><img class="cover-table" src="${
          buku2 ? buku2.cover : "covers/no-image.jpg"
        }" alt="cover"></td>
        <td><img class="cover-table" src="${
          buku3 ? buku3.cover : "covers/no-image.jpg"
        }" alt="cover"></td>
        <td><img class="cover-table" src="${
          buku4 ? buku4.cover : "covers/no-image.jpg"
        }" alt="cover"></td>
        <td><img class="cover-table" src="${
          buku5 ? buku5.cover : "covers/no-image.jpg"
        }" alt="cover"></td>
        <td>${p.tglKembali}</td>
        <td>Rp ${p.denda.toLocaleString("id-ID")}</td>
        <td>
          <button class="hapus-transaksi" data-index="${index}">
            Hapus
          </button>
        </td>
      </tr>`;
  });

  tbodyPinjam.innerHTML = html;

  // EVENT HAPUS
  document.querySelectorAll(".hapus-transaksi").forEach((btn) => {
    btn.addEventListener("click", function () {
      const index = parseInt(this.dataset.index);

      if (confirm("Yakin ingin menghapus transaksi ini?")) {
        peminjaman.splice(index, 1);
        renderTabelPinjam();
        alert("Transaksi berhasil dihapus!");
      }
    });
  });
}

function resetFormBuku() {
  isbnInput.value = "";
  judulInput.value = "";
  penerbitInput.value = "";
  tahunInput.value = "";
  coverImage.src = "covers/no-image.jpg";
}

function resetSemuaForm() {
  kodePinjamInput.value = "";
  tglPinjamInput.value = "";
  namaAnggotaInput.value = "";
  jumlahBukuInput.value = "1";
  tglKembaliInput.value = "";
  lamaPinjamInput.value = "0 Hari";
  dendaInput.value = "0";
  resetFormBuku();
  bukuDipilih = [];
  renderTabelBukuDipilih();
}

function isKodePinjamUnique(kode) {
  return !peminjaman.some((p) => p.kode === kode);
}

function validateJumlahBuku() {
  const jumlah = parseInt(jumlahBukuInput.value);
  if (isNaN(jumlah) || jumlah < 1 || jumlah > 5) {
    alert("Jumlah buku harus antara 1 - 5");
    return false;
  }
  return true;
}

// ==================== EVENT ====================
// jumlahBukuInput.addEventListener("input", function () {
//   let val = parseInt(this.value);
//   if (isNaN(val) || val < 1) this.value = 1;
//   if (val > 2) this.value = 2;
// });

isbnInput.addEventListener("input", function () {
  const buku = daftarBuku.find((b) => b.isbn === this.value.trim());
  if (buku) {
    judulInput.value = buku.judul;
    penerbitInput.value = buku.penerbit;
    tahunInput.value = buku.tahun;
    coverImage.src = buku.cover;
    coverImage.onerror = function () {
      this.src = "covers/no-image.jpg";
    };
  } else resetFormBuku();
});

tambahBukuBtn.addEventListener("click", function () {
  const isbn = isbnInput.value.trim();
  const jumlah = jumlahBukuInput.value;
  if (!isbn) return alert("Masukkan ISBN terlebih dahulu!");
  if (jumlah <= 0) {
    return alert("Jumlah Buku tidak Valid!");
  }
  if (jumlah > 5) {
    return alert("Jumlah Buku maksimal 5!");
  }
  const buku = daftarBuku.find((b) => b.isbn === isbn);
  if (!buku) return alert("Buku dengan ISBN tidak ditemukan!");
  const maxBuku = parseInt(jumlahBukuInput.value);
  if (bukuDipilih.length >= maxBuku) return alert(`Maksimal ${maxBuku} buku!`);
  if (bukuDipilih.some((b) => b.isbn === isbn))
    return alert("Buku sudah ditambahkan!");
  bukuDipilih.push(buku);
  alert(`Buku "${buku.judul}" ditambahkan (${bukuDipilih.length}/${maxBuku})`);
  renderTabelBukuDipilih();
  resetFormBuku();
});

tglPinjamInput.addEventListener("change", updateLamaDanDenda);
tglKembaliInput.addEventListener("change", updateLamaDanDenda);

simpanBtn.addEventListener("click", function () {
  const kode = kodePinjamInput.value.trim();
  if (!kode) return alert("Kode pinjam harus diisi!");
  if (!isKodePinjamUnique(kode)) return alert("Kode pinjam sudah digunakan!");
  if (!validateJumlahBuku()) return;
  if (!tglPinjamInput.value) return alert("Tanggal pinjam harus diisi!");
  if (!namaAnggotaInput.value) return alert("Nama anggota harus diisi!");
  const maxBuku = parseInt(jumlahBukuInput.value);
  if (bukuDipilih.length !== maxBuku)
    return alert(`Anda harus memilih ${maxBuku} buku!`);
  if (!tglKembaliInput.value) return alert("Tanggal kembali harus diisi!");

  const denda = hitungDenda(tglPinjamInput.value, tglKembaliInput.value);

  const newPinjam = {
    kode: kode,
    tglPinjam: tglPinjamInput.value,
    nama: namaAnggotaInput.value,
    isbn1: bukuDipilih[0] ? bukuDipilih[0].isbn : "",
    isbn2: bukuDipilih[1] ? bukuDipilih[1].isbn : "",
    isbn3: bukuDipilih[2] ? bukuDipilih[2].isbn : "",
    isbn4: bukuDipilih[3] ? bukuDipilih[3].isbn : "",
    isbn5: bukuDipilih[4] ? bukuDipilih[4].isbn : "",
    tglKembali: tglKembaliInput.value,
    denda: denda,
  };

  peminjaman.push(newPinjam);
  renderTabelPinjam();
  resetSemuaForm();
  alert("Data peminjaman tersimpan!");
});

bersihkanBtn.addEventListener("click", function () {
  if (confirm("Bersihkan form input?")) resetSemuaForm();
});

keluarBtn.addEventListener("click", function () {
  if (confirm("Yakin ingin keluar?")) {
    window.close();
    window.location.href = "about:blank";
  }
});

// Inisialisasi
resetSemuaForm();
