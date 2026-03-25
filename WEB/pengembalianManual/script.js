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
	new Buku(
		"9786231809223",
		"PKK Kelas 12",
		"Stone",
		"2006",
		"covers/pkk.jpg"
	),
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
	new Buku(
		"6941256125632",
		"Wireless Rat",
		"Elitis",
		"2008",
		"covers/AM.jpg"
	),
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
	)
];

let bukuDipilih = [];
let peminjaman = [];

const maxPilih = 2;

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
	const buku = daftarBuku.find(b => b.isbn == inputISBN.value.trim());
	const jumlahPilih = parseInt(inputJumlahPinjam.value);

	if (!isbn) {
		alert("ISBN tidak boleh kosong");
		return;
	}
	if (!buku) {
		alert("Buku tidak ditemukan");
		return;
	}
	if (!jumlahPilih) {
		alert("Masukkan Jumlah Buku yang dipilih");
		return;
	}
	if (jumlahPilih > maxPilih) {
		alert("Jumlah buku lebih besar dari pada maksimal pinjam");
		return;
	}
	if (jumlahPilih <= 0) {
		alert("Jumlah buku tidak valid");
		return;
	}
	if (bukuDipilih.find(b => b.isbn === isbn)) {
		alert("Buku yang dipilih tidak boleh sama");
		return;
	}
	if (bukuDipilih.length >= jumlahPilih) {
		alert("Jumlah buku terpenuhi!");
		return;
	}

	bukuDipilih.push(buku);
	renderTableBuku();
	resetFormBuku();
	inputISBN.value = "";
	alert("Buku berhasil dipilih!");
}

function pinjamBuku() {
	const kodePinjam = inputKodePinjam.value.trim();
	const nama = inputNama.value.trim();
	const tanggalPinjam = inputTanggalPinjam.value;
	const jumlahPilih = parseInt(inputJumlahPinjam.value);

	if (!kodePinjam) {
		return alert("Kode pinjam harus diisi");
	}
	if (!nama) {
		return alert("Nama harus diisi");
	}
	if (!tanggalPinjam) {
		return alert("Tanggal pinjam harus diisi");
	}
	if (!jumlahPilih) {
		return alert("Jumlah pinjam harus diisi");
	}

	if (bukuDipilih.length == 0) {
		return alert("Belum ada buku yang dipilih!");
	}
	if (bukuDipilih.length < jumlahPilih) {
		return alert(`Pilih ${jumlahPilih} buku terlebih dahulu!`);
	}

	if (peminjaman.find(p => p.kode === kodePinjam)) {
		return alert("Kode pinjam sudah ada");
	}

	const transaksi = {
		kode: kodePinjam,
		tanggalPinjam: tanggalPinjam,
		nama: nama,
		cover1: bukuDipilih[0]
			? bukuDipilih[0].path_cover
			: "covers/no-image.jpg",
		cover2: bukuDipilih[1]
			? bukuDipilih[1].path_cover
			: "covers/no-image.jpg",
		status: true,
		tanggalKembali: null,
		denda: null
	};

	peminjaman.push(transaksi);
	renderTablePinjam();
	resetFormPinjam();
	alert("Data berhasil disimpan");
}

function renderTablePinjam() {
	tBodyTransaksi.innerHTML = "";

	peminjaman.forEach((transaksi, index) => {
		const tr = document.createElement("tr");
		tr.innerHTML = `
    <td>${transaksi.kode}</td>
    <td>${transaksi.tanggalPinjam}</td>
    <td>${transaksi.nama}</td>
    <td><img src="${transaksi.cover1}"/></td>
    <td><img src="${transaksi.cover2}"/></td>
    <td>${transaksi.status ? "dipinjam" : "dikembalikan"}</td>
    <td>${transaksi.tanggalKembali}</td>
    <td>${transaksi.denda}</td>
    <td><button onclick="hapusTransaksi(${index})">Hapus</button></td>
    `;
		tBodyTransaksi.appendChild(tr);
	});
}

function renderTableBuku() {
	tBodyBuku.innerHTML = "";

	bukuDipilih.forEach((buku, index) => {
		const tr = document.createElement("tr");
		tr.innerHTML = `
		<td>${buku.isbn}</td>
		<td>${buku.judul}</td>
		<td>${buku.penerbit}</td>
		<td>${buku.tahun}</td>
		<td><img src="${buku.path_cover}"/></td>
		<td><button onclick="hapusBuku(${index})">Hapus</button></td>
		`;
		tBodyBuku.appendChild(tr);
	});
}

function hapusBuku(index) {
	bukuDipilih.splice(index, 1);
	renderTableBuku();
}

function resetFormPinjam() {
	inputKodePinjam.value = "";
	inputTanggalPinjam.value = "";
	inputNama.value = "";
	inputJumlahPinjam.value = "";
	inputISBN.value = "";
	bukuDipilih = [];
	resetFormBuku();
	renderTableBuku();
	renderTablePinjam();
}

function resetFormBuku() {
	inputJudul.value = "Buku Tidak Ditemukan";
	inputPenerbit.value = "";
	inputTahunTerbit.value = "";
	imgCover.src = "covers/no-image.jpg";
}

// listener input ISBN
inputISBN.addEventListener("input", function () {
	const buku = daftarBuku.find(b => b.isbn == this.value.trim());
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
