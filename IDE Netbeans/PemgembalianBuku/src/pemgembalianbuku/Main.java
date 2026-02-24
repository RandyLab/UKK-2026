/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pemgembalianbuku;

import java.awt.Component;
import java.awt.Image;
import java.util.ArrayList;
import java.util.Date;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

/**
 *
 * @author Administrator
 */
public class Main extends javax.swing.JFrame {

    
    public class Buku {
        String ISBN, judul, penerbit, tahun_terbit, cover_path;
        
        public Buku(String ISBN, String judul, String penerbit, String tahun_terbit, String cover_path){
            this.ISBN = ISBN;
            this.judul = judul;
            this.penerbit = penerbit;
            this.tahun_terbit = tahun_terbit;
            this.cover_path = cover_path;
        }
    }
    ArrayList<Buku> daftarBuku = new ArrayList<>();
    
    public class Transaksi{
        String kode_pinjam, tanggal_pinjam,  nama_anggota, ISBN1, ISBN2, tanggal_kembali, denda;
        
        public Transaksi(String kode_pinjam, String tanggal_pinjam, String nama_anggota, String ISBN1, String ISBN2, String tanggal_kembali, String denda){
            this.kode_pinjam = kode_pinjam;
            this.tanggal_pinjam = tanggal_pinjam;
            this.nama_anggota = nama_anggota;
            this.ISBN1 = ISBN1;
            this.ISBN2 = ISBN2;
            this.tanggal_kembali = tanggal_kembali;
            this.denda = denda;
        }
    }
    ArrayList<Transaksi> daftarTransaksi = new ArrayList<>();
    
    private void loadTransaksi() {
        DefaultTableModel model =
            (DefaultTableModel) transaksiTable.getModel();
        model.setRowCount(0);

        for (Transaksi t : daftarTransaksi) {
            ImageIcon cover1 = null;
            ImageIcon cover2 = null;

            if (!t.ISBN1.isEmpty()) {
                cover1 = getCoverByISBN(t.ISBN1);
            }

            if (t.ISBN2 != null && !t.ISBN2.isEmpty()) {
                cover2 = getCoverByISBN(t.ISBN2);
            }

            model.addRow(new Object[]{
                t.kode_pinjam,
                t.tanggal_pinjam,
                t.nama_anggota,
                cover1,
                cover2,
                t.tanggal_kembali,
                t.denda
            });
        }
    }
    
    private ImageIcon getCoverByISBN(String isbn) {
        for (Buku b : daftarBuku) {
            if (b.ISBN.equalsIgnoreCase(isbn)) {
                Image img = new ImageIcon(
                    getClass().getResource(b.cover_path)
                ).getImage().getScaledInstance(60, 80, Image.SCALE_SMOOTH);

                return new ImageIcon(img);
            }
        }

        // fallback jika tidak ditemukan
        Image img = no_image.getScaledInstance(60, 80, Image.SCALE_SMOOTH);
        return new ImageIcon(img);
    }
    
    private void bersihkanForm(){
        
        jumlahPilih = 0;
        kodePinjamField.setText("");
        namaAnggotaField.setText("");
        lamaPinjamField.setText("");
        dendaField.setText("");
        jumlahBukuSpinner.setValue(0);
        tanggalPinjamChooser.setDate(null);
        tanggalKembaliChooser.setDate(null);
        
        ISBNField.setText("");
        judulField.setText("");
        penerbitField.setText("");
        tahunTerbitField.setText("");
        coverLabel.setIcon(new ImageIcon(no_image));
        
        DefaultTableModel model = (DefaultTableModel) bukuTable.getModel();
        model.setRowCount(0);

        jumlahBukuSpinner.setEnabled(true);
    }
    
    private void hitungDenda(){
        Date tanggalPinjam = tanggalPinjamChooser.getDate();
        Date tanggalKembali = tanggalKembaliChooser.getDate();
        
        if (tanggalPinjam == null || tanggalKembali == null) {
            dendaField.setText("0");
            lamaPinjamField.setText("0");
            return;
        }
        
        Long selisihMil = tanggalKembali.getTime() - tanggalPinjam.getTime() ;
        Long lamaPinjam = selisihMil / (1000 * 60 * 60 * 24);
        
        
        if(lamaPinjam < 0){
            JOptionPane.showMessageDialog(null, "Tanggal kembali tidak boleh lebih kecil daripada tanggal pinjam!" + lamaPinjam);
            dendaField.setText("0");
            lamaPinjamField.setText("0");
            return;
        }
        
        lamaPinjamField.setText(String.valueOf(lamaPinjam));
        
        if(lamaPinjam > 5){
            long denda = (lamaPinjam - 5) * 5000;
            dendaField.setText(String.valueOf(denda));
        }else{
            dendaField.setText("0");
        }
    }
    
    private boolean isKodePinjamDuplikat(String kode) {
        for (Transaksi t : daftarTransaksi) {
            if (t.kode_pinjam.equalsIgnoreCase(kode)) {
                return true;
            }
        }
        return false;
    }
    
    private void cariBuku() {

    String ISBN = ISBNField.getText().trim();
    boolean ditemukan = false;

    for (Buku buku : daftarBuku) {
        if (ISBN.equals(buku.ISBN)) {

            Image cover = new ImageIcon(
                    getClass().getResource(buku.cover_path))
                    .getImage()
                    .getScaledInstance(130, 170, Image.SCALE_SMOOTH);

            judulField.setText(buku.judul);
            penerbitField.setText(buku.penerbit);
            tahunTerbitField.setText(buku.tahun_terbit);
            coverLabel.setIcon(new ImageIcon(cover));

            ditemukan = true;
            break;
        }
    }

    if (!ditemukan) {
        judulField.setText("Tidak Ditemukan");
        penerbitField.setText("");
        tahunTerbitField.setText("");
        coverLabel.setIcon(new ImageIcon(no_image));
    }
}
    
    private void tambahTableBuku() {

    DefaultTableModel model =
        (DefaultTableModel) bukuTable.getModel();

    String ISBN = ISBNField.getText().trim();
    String judul = judulField.getText().trim();
    String penerbit = penerbitField.getText().trim();
    String tahun_terbit = tahunTerbitField.getText().trim();

    if (ISBN.isEmpty() || judul.isEmpty()) {
        JOptionPane.showMessageDialog(null, "Data belum lengkap!");
        return;
    }

    if (penerbit.isEmpty()) {
        JOptionPane.showMessageDialog(null, "Buku tidak ditemukan!");
        return;
    }

    if (jumlahPilih >= maxPilih) {
        JOptionPane.showMessageDialog(null, "Batas pinjam tercapai!");
        return;
    }

    // Cegah ISBN duplikat
    for (int i = 0; i < model.getRowCount(); i++) {
        if (model.getValueAt(i, 0).toString().equalsIgnoreCase(ISBN)) {
            JOptionPane.showMessageDialog(
                null, "Buku sudah ditambahkan!");
            return;
        }
    }

    // cover
    ImageIcon coverIcon;
    Icon icon = coverLabel.getIcon();

    if (icon instanceof ImageIcon) {
        coverIcon = (ImageIcon) icon;
    } else {
        coverIcon = new ImageIcon(
            getClass().getResource("/img/no_image.png")
        );
    }

    Image img = coverIcon.getImage()
        .getScaledInstance(60, 80, Image.SCALE_SMOOTH);

    ImageIcon resizeIcon = new ImageIcon(img);

    model.addRow(new Object[]{
        ISBN,
        judul,
        penerbit,
        tahun_terbit,
        resizeIcon
    });

    jumlahPilih++;
}
    
    Image no_image = new ImageIcon(getClass().getResource("/covers/no-image.jpg")).getImage().getScaledInstance(130, 170, Image.SCALE_SMOOTH);
    int maxPilih = 2;
    int jumlahPilih = 0;
    String[] pilihISBN = new String[0];
    
    
    public Main() {
        initComponents();
//        ==========================================================================================================
//        MAIN FUNCTION
//        ==========================================================================================================
        coverLabel.setIcon(new ImageIcon(no_image));
        loadTransaksi();
        
        daftarBuku.add(new Buku("9786231809223", "PKK Kelas 12", "Stone", "2006", "/covers/pkk.jpg"));
        daftarBuku.add(new Buku("9786022446576", "PPKN", "PEAK", "2007", "/covers/pkn.jpg"));
        daftarBuku.add(new Buku("6941798464992", "Buku Catatan", "Elitis", "2008", "/covers/buku_catatan.jpg"));
        daftarBuku.add(new Buku("208386213", "Buku Tulis Samuel", "Elitis", "2008", "/covers/buku_tulis.jpg"));
        daftarBuku.add(new Buku("9786029053265", "MPR", "Elitis", "2008", "/covers/mpr.jpg"));
        daftarBuku.add(new Buku("9786025305740", "Filsafat Dalam Berbagai Perspektif", "Elitis", "2008", "/covers/filsafat.jpg"));
        daftarBuku.add(new Buku("9786232422971", "Realm Breaker", "Elitis", "2008", "/covers/realm_breaker.jpg"));
        
        daftarTransaksi.add(new Transaksi("1234", "12-2-2026", "Raja", "9786029053265", "208386213", "14-2-2026", "0"));
        
        ISBNField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {cariBuku();}

            @Override
            public void removeUpdate(DocumentEvent e) {cariBuku();}

            @Override
            public void changedUpdate(DocumentEvent e) {cariBuku();}
        
        });
        
        bukuTable.setRowHeight(90);
        bukuTable.getColumnModel().getColumn(4).setCellRenderer(new DefaultTableCellRenderer() {
           @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
            boolean isSelected, boolean hasFocus, int row, int column) {

                JLabel label = new JLabel();
                label.setHorizontalAlignment(JLabel.CENTER);

                if (value instanceof ImageIcon) {
                    label.setIcon((ImageIcon) value);
                }

                return label;
            }
        });
        
        transaksiTable.setRowHeight(90);

        DefaultTableCellRenderer coverRenderer =
            new DefaultTableCellRenderer() {

                @Override
                public Component getTableCellRendererComponent(
                    JTable table, Object value,
                    boolean isSelected, boolean hasFocus,
                    int row, int column) {

                    JLabel label = new JLabel();
                    label.setHorizontalAlignment(JLabel.CENTER);

                    if (value instanceof ImageIcon) {
                        label.setIcon((ImageIcon) value);
                    }

                    return label;
                }
        };

        // Kolom ISBN 1 & ISBN 2
        transaksiTable.getColumnModel().getColumn(3)
                .setCellRenderer(coverRenderer);
        transaksiTable.getColumnModel().getColumn(4)
                .setCellRenderer(coverRenderer);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        kodePinjamField = new javax.swing.JTextField();
        namaAnggotaField = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jumlahBukuSpinner = new javax.swing.JSpinner();
        jLabel5 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        dendaField = new javax.swing.JTextField();
        jLabel13 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        lamaPinjamField = new javax.swing.JTextField();
        jLabel15 = new javax.swing.JLabel();
        jSeparator2 = new javax.swing.JSeparator();
        tanggalKembaliChooser = new com.toedter.calendar.JDateChooser();
        tanggalPinjamChooser = new com.toedter.calendar.JDateChooser();
        jPanel2 = new javax.swing.JPanel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        ISBNField = new javax.swing.JTextField();
        judulField = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();
        penerbitField = new javax.swing.JTextField();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        tahunTerbitField = new javax.swing.JTextField();
        coverLabel = new javax.swing.JLabel();
        tambahButton = new javax.swing.JButton();
        jSeparator1 = new javax.swing.JSeparator();
        jPanel3 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        bukuTable = new javax.swing.JTable();
        jPanel4 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        transaksiTable = new javax.swing.JTable();
        jPanel5 = new javax.swing.JPanel();
        keluarButton = new javax.swing.JButton();
        simpanButton = new javax.swing.JButton();
        bersihkanButton = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setUndecorated(true);
        setResizable(false);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel1.setBackground(new java.awt.Color(255, 102, 0));
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabel1.setText("Data Peminjaman");
        jPanel1.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 10, -1, -1));

        jLabel2.setText("Kode Peminjaman");
        jPanel1.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 50, -1, -1));
        jPanel1.add(kodePinjamField, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 70, 180, 30));
        jPanel1.add(namaAnggotaField, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 190, 180, 30));

        jLabel3.setText("Tanggal Pinjam");
        jPanel1.add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 110, -1, -1));

        jLabel4.setText("Jumlah Buku");
        jPanel1.add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 230, -1, -1));
        jPanel1.add(jumlahBukuSpinner, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 250, 70, 30));

        jLabel5.setText("Nama Anggota");
        jPanel1.add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 170, -1, -1));

        jLabel12.setText("Tanggal Pengembalian");
        jPanel1.add(jLabel12, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 290, -1, -1));

        dendaField.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        dendaField.setFocusable(false);
        jPanel1.add(dendaField, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 460, 180, 30));

        jLabel13.setText("Denda (Rp. )");
        jPanel1.add(jLabel13, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 440, -1, -1));

        jLabel14.setText("Hari");
        jPanel1.add(jLabel14, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 410, -1, -1));

        lamaPinjamField.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        lamaPinjamField.setFocusable(false);
        jPanel1.add(lamaPinjamField, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 400, 80, 30));

        jLabel15.setText("Lama Peminjaman");
        jPanel1.add(jLabel15, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 380, -1, -1));
        jPanel1.add(jSeparator2, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 360, 180, 10));

        tanggalKembaliChooser.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                tanggalKembaliChooserPropertyChange(evt);
            }
        });
        jPanel1.add(tanggalKembaliChooser, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 310, 180, 30));
        jPanel1.add(tanggalPinjamChooser, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 132, 180, 30));

        getContentPane().add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 230, 530));

        jPanel2.setBackground(new java.awt.Color(204, 102, 255));
        jPanel2.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel6.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabel6.setText("Pilih Buku");
        jPanel2.add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 10, -1, -1));

        jLabel7.setText("ISBN");
        jPanel2.add(jLabel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 50, -1, -1));

        ISBNField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ISBNFieldActionPerformed(evt);
            }
        });
        jPanel2.add(ISBNField, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 70, 190, 30));

        judulField.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        judulField.setFocusable(false);
        jPanel2.add(judulField, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 120, 190, 30));

        jLabel8.setText("Judul");
        jPanel2.add(jLabel8, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 100, -1, -1));

        penerbitField.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        penerbitField.setFocusable(false);
        jPanel2.add(penerbitField, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 170, 190, 30));

        jLabel9.setText("Penerbit");
        jPanel2.add(jLabel9, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 150, -1, -1));

        jLabel10.setText("Tahun Terbit");
        jPanel2.add(jLabel10, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 200, -1, -1));

        tahunTerbitField.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        tahunTerbitField.setFocusable(false);
        jPanel2.add(tahunTerbitField, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 220, 190, 30));

        coverLabel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/covers/no-image.jpg"))); // NOI18N
        coverLabel.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jPanel2.add(coverLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(230, 30, 130, 170));

        tambahButton.setBackground(new java.awt.Color(0, 40, 112));
        tambahButton.setFont(new java.awt.Font("Tahoma", 1, 15)); // NOI18N
        tambahButton.setForeground(new java.awt.Color(255, 255, 255));
        tambahButton.setText("Tambah");
        tambahButton.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        tambahButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tambahButtonActionPerformed(evt);
            }
        });
        jPanel2.add(tambahButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(230, 220, 130, 30));

        jSeparator1.setOrientation(javax.swing.SwingConstants.VERTICAL);
        jPanel2.add(jSeparator1, new org.netbeans.lib.awtextra.AbsoluteConstraints(210, 30, -1, 220));

        getContentPane().add(jPanel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(230, 0, 390, 290));

        jPanel3.setBackground(new java.awt.Color(102, 255, 102));
        jPanel3.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        bukuTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "ISBN", "Judul", "Penerbit", "Tahun Terbit", "Cover"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane2.setViewportView(bukuTable);

        jPanel3.add(jScrollPane2, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 11, 390, 270));

        getContentPane().add(jPanel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(620, 0, 410, 290));

        jPanel4.setBackground(new java.awt.Color(0, 255, 255));
        jPanel4.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        transaksiTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Kode Pinjam", "Tanggal Pinjam", "Nama Anggota", "ISBN 1", "ISBN 2", "Tanggal Kembali", "Denda"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane1.setViewportView(transaksiTable);

        jPanel4.add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 20, 780, 210));

        getContentPane().add(jPanel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(230, 290, 800, 240));

        jPanel5.setBackground(new java.awt.Color(255, 255, 153));
        jPanel5.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        keluarButton.setBackground(new java.awt.Color(122, 0, 0));
        keluarButton.setFont(new java.awt.Font("Tahoma", 1, 15)); // NOI18N
        keluarButton.setForeground(new java.awt.Color(255, 255, 255));
        keluarButton.setText("Keluar");
        keluarButton.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        keluarButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                keluarButtonActionPerformed(evt);
            }
        });
        jPanel5.add(keluarButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(920, 10, -1, 30));

        simpanButton.setBackground(new java.awt.Color(0, 40, 112));
        simpanButton.setFont(new java.awt.Font("Tahoma", 1, 15)); // NOI18N
        simpanButton.setForeground(new java.awt.Color(255, 255, 255));
        simpanButton.setText("Simpan");
        simpanButton.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        simpanButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                simpanButtonActionPerformed(evt);
            }
        });
        jPanel5.add(simpanButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 10, -1, 30));

        bersihkanButton.setBackground(new java.awt.Color(0, 40, 112));
        bersihkanButton.setFont(new java.awt.Font("Tahoma", 1, 15)); // NOI18N
        bersihkanButton.setForeground(new java.awt.Color(255, 255, 255));
        bersihkanButton.setText("Bersihkan");
        bersihkanButton.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        bersihkanButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bersihkanButtonActionPerformed(evt);
            }
        });
        jPanel5.add(bersihkanButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 10, -1, 30));

        getContentPane().add(jPanel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 530, 1030, 50));

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void keluarButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_keluarButtonActionPerformed
        // TODO add your handling code here:
        int pilih = JOptionPane.showConfirmDialog(null , "Apakah anda yakin ingin keluar? ","Keluar", JOptionPane.OK_CANCEL_OPTION);
        
        if(pilih == JOptionPane.OK_OPTION){
            System.exit(0);
        }
    }//GEN-LAST:event_keluarButtonActionPerformed

    private void tambahButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tambahButtonActionPerformed
                                           
    int jumlah = (int) jumlahBukuSpinner.getValue();

    if (jumlah <= 0) {
        JOptionPane.showMessageDialog(
            null,
            "Jumlah buku tidak valid",
            "Invalid",
            JOptionPane.ERROR_MESSAGE
        );
        return;
    }

    if (jumlah > maxPilih) {
        JOptionPane.showMessageDialog(
            null,
            "Maksimal buku yang bisa dipinjam 2",
            "MAX",
            JOptionPane.INFORMATION_MESSAGE
        );
        jumlahBukuSpinner.setValue(maxPilih);
        return;
    }

    if (jumlahPilih >= jumlah) {
        JOptionPane.showMessageDialog(
            null,
            "Jumlah buku sudah sesuai spinner!",
            "Validasi",
            JOptionPane.WARNING_MESSAGE
        );
        return;
    }

    tambahTableBuku();

    // 🔒 kunci spinner setelah buku pertama ditambah
    if (jumlahPilih > 0) {
        jumlahBukuSpinner.setEnabled(false);
    }

    }//GEN-LAST:event_tambahButtonActionPerformed

    private void bersihkanButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bersihkanButtonActionPerformed
        // TODO add your handling code here:
        bersihkanForm();
    }//GEN-LAST:event_bersihkanButtonActionPerformed

    private void tanggalKembaliChooserPropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_tanggalKembaliChooserPropertyChange
        // TODO add your handling code here:
        hitungDenda();
    }//GEN-LAST:event_tanggalKembaliChooserPropertyChange

    private void ISBNFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ISBNFieldActionPerformed
        // TODO add your handling code here:
        cariBuku();
    }//GEN-LAST:event_ISBNFieldActionPerformed

    private void simpanButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_simpanButtonActionPerformed

    // 1. Ambil kode pinjam
    String kodePinjam = kodePinjamField.getText().trim();

    // 2. Validasi kode kosong
    if (kodePinjam.isEmpty()) {
        JOptionPane.showMessageDialog(
            null,
            "Kode pinjam tidak boleh kosong!",
            "Validasi",
            JOptionPane.WARNING_MESSAGE
        );
        return;
    }

    // 3. Validasi duplikat
    if (isKodePinjamDuplikat(kodePinjam)) {
        JOptionPane.showMessageDialog(
            null,
            "Kode pinjam sudah digunakan!\nGunakan kode lain.",
            "Duplikat",
            JOptionPane.WARNING_MESSAGE
        );
        return;
    }

    // 4. Validasi spinner vs buku
    int jumlahSpinner = (int) jumlahBukuSpinner.getValue();
    if (jumlahPilih < jumlahSpinner) {
        JOptionPane.showMessageDialog(
            null,
            "Jumlah buku belum sesuai spinner,\nsilakan tambah buku!",
            "Validasi",
            JOptionPane.WARNING_MESSAGE
        );
        return;
    }

    // 5. Validasi form
    if (namaAnggotaField.getText().trim().isEmpty()
        || tanggalPinjamChooser.getDate() == null
        || tanggalKembaliChooser.getDate() == null) {

        JOptionPane.showMessageDialog(
            null,
            "Data peminjaman belum lengkap!",
            "Validasi",
            JOptionPane.WARNING_MESSAGE
        );
        return;
    }

    if (jumlahPilih <= 0) {
        JOptionPane.showMessageDialog(
            null,
            "Belum ada buku yang dipilih!",
            "Validasi",
            JOptionPane.WARNING_MESSAGE
        );
        return;
    }

    // 6. Ambil ISBN dari tabel buku (table TEMP)
    DefaultTableModel modelBuku =
        (DefaultTableModel) bukuTable.getModel();

    String ISBN1 = modelBuku.getValueAt(0, 0).toString();
    String ISBN2 = "";

    if (jumlahPilih > 1 && modelBuku.getRowCount() > 1) {
        ISBN2 = modelBuku.getValueAt(1, 0).toString();
    }

    // 7. Format tanggal
    String tanggalPinjam = new java.text.SimpleDateFormat("dd-MM-yyyy")
            .format(tanggalPinjamChooser.getDate());

    String tanggalKembali = new java.text.SimpleDateFormat("dd-MM-yyyy")
            .format(tanggalKembaliChooser.getDate());

    String namaAnggota = namaAnggotaField.getText().trim();
    String denda = dendaField.getText().trim();

    // 8. Simpan ke ArrayList
    daftarTransaksi.add(new Transaksi(
        kodePinjam,
        tanggalPinjam,
        namaAnggota,
        ISBN1,
        ISBN2,
        tanggalKembali,
        denda
    ));

    // 9. Refresh & reset (sesuai konfirmasi kamu)
    loadTransaksi();
    bersihkanForm(); // reset semua seperti baru buka aplikasi

    JOptionPane.showMessageDialog(
        null,
        "Transaksi berhasil disimpan!",
        "Sukses",
        JOptionPane.INFORMATION_MESSAGE
    );

    }//GEN-LAST:event_simpanButtonActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(Main.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Main.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Main.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Main.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Main().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextField ISBNField;
    private javax.swing.JButton bersihkanButton;
    private javax.swing.JTable bukuTable;
    private javax.swing.JLabel coverLabel;
    private javax.swing.JTextField dendaField;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JTextField judulField;
    private javax.swing.JSpinner jumlahBukuSpinner;
    private javax.swing.JButton keluarButton;
    private javax.swing.JTextField kodePinjamField;
    private javax.swing.JTextField lamaPinjamField;
    private javax.swing.JTextField namaAnggotaField;
    private javax.swing.JTextField penerbitField;
    private javax.swing.JButton simpanButton;
    private javax.swing.JTextField tahunTerbitField;
    private javax.swing.JButton tambahButton;
    private com.toedter.calendar.JDateChooser tanggalKembaliChooser;
    private com.toedter.calendar.JDateChooser tanggalPinjamChooser;
    private javax.swing.JTable transaksiTable;
    // End of variables declaration//GEN-END:variables
}
