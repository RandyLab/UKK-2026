// ====================Data==========================
class Buku {
  constructor(isbn, judul, penerbit, tahun, path_cover) {
    this.isbn = isbn;
    this.judul = judul;
    this.penerbit = penerbit;
    this.tahun = tahun;
    this.path_cover = path_cover;
  }
}

let daftarBuku = [
  new Buku("9786022446576", "PPKN", "PEAK", "2007", "../covers/pkn.jpg"),
];

let bukuDipilih = [];
let peminjaman = [];

// ================Objek===================
const inputKodePinjam = document.getElementById("kodePinjam");
const inputTanggalPinjam = document.getElementById("tanggalPinjam");
const inputNama = document.getElementById("namaAnggota");
const inputJumlahPinjam = document.getElementById("jumlahPinjam");

const inputISBN = document.getElementById("ISBN");
const inputJudul = document.getElementById("judulBuku");
const inputPenerbit = document.getElementById("penerbit");
const inputTahunTerbit = document.getElementById("tahunTerbit");
const imgCover = document.getElementById("previewCover");

const inputTanggalKembali = document.getElementById("tanggalKembali");
const inputLamaPinjam = document.getElementById("lamaPinjam");
const inputDenda = document.getElementById("denda");

const tBodyBuku = document.getElementById("tBodyBuku");
const tBodyTransaksi = document.getElementById("tBodyTransaksi");

// ===================functions=======================
function pilihBuku() {
  const isbn = inputISBN.value.trim();
  const buku = daftarBuku.find((b) => b.isbn == inputISBN.value.trim());
  const maxBuku = parseInt(inputJumlahPinjam.value);

  if (!isbn) {
    alert("ISBN tidak boleh kosong");
    return;
  }
  if (!buku) {
    alert("Buku tidak ditemukan");
    return;
  }
  if (!maxBuku) {
    alert("Masukkan Jumlah Buku yang dipilih");
    return;
  }
  if (maxBuku) console.log("pilih buku");
}

function resetFormBuku() {
  inputJudul.value = "Buku Tidak Ditemukan";
  inputPenerbit.value = "";
  inputTahunTerbit.value = "";
  imgCover.src = "../covers/no-image.jpg";
}

// listener input ISBN
inputISBN.addEventListener("input", function () {
  const buku = daftarBuku.find((b) => b.isbn == this.value.trim());
  if (buku) {
    inputJudul.value = buku.judul;
    inputPenerbit.value = buku.penerbit;
    inputTahunTerbit.value = buku.tahun;
    imgCover.src = buku.path_cover;
    imgCover.onerror = function () {
      imgCover.src = "covers/no-image.jpg";
    };
  } else {
    resetFormBuku();
  }
});
