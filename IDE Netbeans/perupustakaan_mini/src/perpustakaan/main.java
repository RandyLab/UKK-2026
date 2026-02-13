/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package perpustakaan;

import java.awt.Image;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Administrator
 */
public class main extends javax.swing.JFrame {

    private void updateCover(String path){
        if(!path.isEmpty()){
            ImageIcon coverIcon = new ImageIcon(path);
            Image image = coverIcon.getImage().getScaledInstance(100, 130, Image.SCALE_SMOOTH);
            iconCover.setIcon(new ImageIcon(image));
        }else{
            URL imgURL = getClass().getResource("/perpustakaan/covers/no-image.jpg");
            if(imgURL != null){
                ImageIcon defaultIcon = new ImageIcon(imgURL);
                Image defaultImg = defaultIcon.getImage().getScaledInstance(100, 130, Image.SCALE_SMOOTH);
                iconCover.setIcon(new ImageIcon(defaultImg));
            }
        }
    }
    
    private boolean isKodePinjamExist(int kode) {
    for(Object obj : transaksi) {
        Object[] dataTransaksi = (Object[]) obj;
        int kodeData = (int) dataTransaksi[0];
        if(kodeData == kode) {
            return true;
        }
    }
    return false;
}
    
    private void updateBuku(){
    String kodeISBN = fieldISBN.getText().trim();
    boolean ditemukan = false;

    for(Object obj : data_buku){
        Object[] dataBuku = (Object[]) obj;
        String ISBN = (String) dataBuku[0];

        if(kodeISBN.equals(ISBN)){
            fieldJudul.setText((String) dataBuku[1]);
            fieldPenerbit.setText((String) dataBuku[2]);
            fieldTahunTerbit.setText((String) dataBuku[3]);
            updateCover((String) dataBuku[4]);
            ditemukan = true;
            break;
        }
    }

    if(!ditemukan){
        fieldJudul.setText("");
        fieldPenerbit.setText("");
        fieldTahunTerbit.setText("");
        updateCover("");
    }
}

    
    private void loadTable(){
        DefaultTableModel model = (DefaultTableModel) transaksiTable.getModel();
        model.setRowCount(0);
        for(Object obj : transaksi){
            Object[] row = (Object[]) obj;
            model.addRow(new Object[]{
                row[0], row[1], row[2], row[3], row[4], row[5]
            });
        }
    }
    
    private void clearForm(){
    fieldKodePinjam.setText("");
    fieldNamaAnggota.setText("");
    fieldJumlah.setText("");
    fieldISBN.setText("");
    fieldJudul.setText("");
    fieldPenerbit.setText("");
    fieldTahunTerbit.setText("");
    datePinjam.setDate(null);
    dateKembalikan.setDate(null);
    updateCover("");
    fieldKodePinjam.setEnabled(true);
}
    
    private void simpanTransaksi() {
    Object[] transaksiLama = transaksi;
    transaksi = new Object[transaksiLama.length + 1];
    System.arraycopy(transaksiLama, 0, transaksi, 0, transaksiLama.length);
    transaksi[transaksiLama.length] = transaksiSementara;
    
    loadTable();
    statusMeminjam = false;
    jumlahBukuPinjam = 0;
    counterInputBuku = 0;
    transaksiSementara = null;
    clearForm();
    fieldKodePinjam.setEnabled(true);
    
    JOptionPane.showMessageDialog(this, 
        "Transaksi berhasil disimpan!",
        "Sukses", JOptionPane.INFORMATION_MESSAGE);
}
    
    private void tambahBukuKeTemp(int isbn, String judul, String penerbit, String tahun, String cover) {
    if(tempPilihBuku.size() >= 2) {
        JOptionPane.showMessageDialog(this, "Maksimal 2 buku per peminjaman!", "Peringatan", JOptionPane.WARNING_MESSAGE);
        return;
    }
    
    Object[] buku = new Object[]{
        isbn, judul, penerbit, tahun, cover
    };
    tempPilihBuku.add(buku);
    
    String daftarBuku = "";
    for(int i = 0; i < tempPilihBuku.size(); i++) {
        Object[] b = tempPilihBuku.get(i);
        daftarBuku += (i+1) + ". " + b[1] + " (ISBN: " + b[0] + ")\n";
    }
    
    JOptionPane.showMessageDialog(this, "Buku ditambahkan!\n\nDaftar Buku Dipilih:\n" + daftarBuku, 
        "Keranjang Buku", JOptionPane.INFORMATION_MESSAGE);
    
    fieldJumlah.setText(String.valueOf(tempPilihBuku.size()));
}
    
    private void resetFBuku(){
        fieldISBN.setText("");
        fieldJudul.setText("");
        fieldPenerbit.setText("");
        fieldTahunTerbit.setText("");
        updateCover("");
    }
    
    Object[] data_buku ={
    new Object[] {"9786231809223", "PKK Kelas 12", "Stone", "2006-10-13", "src/perpustakaan/covers/pkk.jpg"},
    new Object[] {"9786022446576", "PPKN", "PEAK", "2007-10-13", "src/perpustakaan/covers/pkn.jpg"},
    new Object[] {"6941798464992", "Buku Catatan", "Elitis", "2008-10-13", "src/perpustakaan/covers/buku_catatan.jpg"},
    new Object[] {"208386213", "Buku Tulis Samuel", "Elitis", "2008-10-13", "src/perpustakaan/covers/buku_tulis.jpg"},
    new Object[] {"9786029053265", "MPR", "Elitis", "2008-10-13", "src/perpustakaan/covers/mpr.jpg"},
    new Object[] {"9786025305740", "Filsafat Dalam Berbagai Perspektif", "Elitis", "2008-10-13", "src/perpustakaan/covers/filsafat.jpg"},
    new Object[] {"9786232422971", "Realm Breaker", "Elitis", "2008-10-13", "src/perpustakaan/covers/realm_breaker.jpg"},
};

Object[] transaksi ={
    new Object[] {1234, "2026-08-11", "Andal Bangun", 123456, 123457, "2026-08-11", 5000}
};

private int jumlahBukuPinjam = 0;
private int counterInputBuku = 0;
private boolean statusMeminjam = false;
private Object[] transaksiSementara = null;

private java.util.ArrayList<Object[]> tempPilihBuku = new java.util.ArrayList<>();

        
    public main() {
        initComponents();
        loadTable();
        
        fieldKodePinjam.requestFocus();
        
        fieldISBN.getDocument().addDocumentListener(new DocumentListener(){
            public void insertUpdate(DocumentEvent e) { updateBuku(); }
            public void removeUpdate(DocumentEvent e) { updateBuku(); }
            public void changedUpdate(DocumentEvent e) { updateBuku(); }
        });
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
        fieldKodePinjam = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        cekKodeButton = new javax.swing.JButton();
        panelPilihBuku = new javax.swing.JPanel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        fieldISBN = new javax.swing.JTextField();
        fieldJudul = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        fieldPenerbit = new javax.swing.JTextField();
        jLabel10 = new javax.swing.JLabel();
        fieldTahunTerbit = new javax.swing.JTextField();
        jLabel11 = new javax.swing.JLabel();
        iconCover = new javax.swing.JLabel();
        pilihBukuButton = new javax.swing.JButton();
        jLabel3 = new javax.swing.JLabel();
        fieldNamaAnggota = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        fieldJumlah = new javax.swing.JTextField();
        pinjamButton = new javax.swing.JButton();
        jLabel12 = new javax.swing.JLabel();
        datePinjam = new com.toedter.calendar.JDateChooser();
        panelPengembalian = new javax.swing.JPanel();
        jLabel13 = new javax.swing.JLabel();
        kembalikanButton = new javax.swing.JButton();
        jLabel14 = new javax.swing.JLabel();
        dateKembalikan = new com.toedter.calendar.JDateChooser();
        jScrollPane1 = new javax.swing.JScrollPane();
        transaksiTable = new javax.swing.JTable();
        keluarTombol = new javax.swing.JButton();
        jLabel15 = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setUndecorated(true);
        setResizable(false);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel1.setBackground(new java.awt.Color(204, 255, 204));
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
        jPanel1.add(fieldKodePinjam, new org.netbeans.lib.awtextra.AbsoluteConstraints(151, 70, 116, -1));

        jLabel2.setFont(new java.awt.Font("SansSerif", 1, 14)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(51, 51, 51));
        jLabel2.setText("Kode Pinjam");
        jPanel1.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 70, -1, -1));

        cekKodeButton.setBackground(new java.awt.Color(0, 153, 0));
        cekKodeButton.setFont(new java.awt.Font("Microsoft Tai Le", 1, 14)); // NOI18N
        cekKodeButton.setForeground(new java.awt.Color(255, 255, 255));
        cekKodeButton.setText("Cek");
        cekKodeButton.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        cekKodeButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cekKodeButtonActionPerformed(evt);
            }
        });
        jPanel1.add(cekKodeButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(285, 67, -1, -1));

        panelPilihBuku.setBackground(new java.awt.Color(255, 255, 204));
        panelPilihBuku.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel6.setFont(new java.awt.Font("Microsoft Tai Le", 3, 18)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(51, 51, 51));
        jLabel6.setText("Isi Data Pinjam");
        panelPilihBuku.add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(13, 14, 138, -1));

        jLabel7.setFont(new java.awt.Font("SansSerif", 1, 14)); // NOI18N
        jLabel7.setForeground(new java.awt.Color(51, 51, 51));
        jLabel7.setText("ISBN");
        panelPilihBuku.add(jLabel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(13, 203, -1, -1));
        panelPilihBuku.add(fieldISBN, new org.netbeans.lib.awtextra.AbsoluteConstraints(146, 204, 147, -1));
        panelPilihBuku.add(fieldJudul, new org.netbeans.lib.awtextra.AbsoluteConstraints(146, 236, 147, -1));

        jLabel8.setFont(new java.awt.Font("SansSerif", 1, 14)); // NOI18N
        jLabel8.setForeground(new java.awt.Color(51, 51, 51));
        jLabel8.setText("Judul");
        panelPilihBuku.add(jLabel8, new org.netbeans.lib.awtextra.AbsoluteConstraints(13, 235, -1, -1));

        jLabel9.setFont(new java.awt.Font("SansSerif", 1, 14)); // NOI18N
        jLabel9.setForeground(new java.awt.Color(51, 51, 51));
        jLabel9.setText("Penerbit");
        panelPilihBuku.add(jLabel9, new org.netbeans.lib.awtextra.AbsoluteConstraints(13, 268, -1, -1));
        panelPilihBuku.add(fieldPenerbit, new org.netbeans.lib.awtextra.AbsoluteConstraints(146, 267, 147, -1));

        jLabel10.setFont(new java.awt.Font("SansSerif", 1, 14)); // NOI18N
        jLabel10.setForeground(new java.awt.Color(51, 51, 51));
        jLabel10.setText("Tahun Terbit");
        panelPilihBuku.add(jLabel10, new org.netbeans.lib.awtextra.AbsoluteConstraints(13, 298, -1, -1));
        panelPilihBuku.add(fieldTahunTerbit, new org.netbeans.lib.awtextra.AbsoluteConstraints(146, 299, 147, -1));

        jLabel11.setFont(new java.awt.Font("SansSerif", 1, 14)); // NOI18N
        jLabel11.setForeground(new java.awt.Color(51, 51, 51));
        jLabel11.setText("Cover");
        panelPilihBuku.add(jLabel11, new org.netbeans.lib.awtextra.AbsoluteConstraints(13, 403, -1, -1));

        iconCover.setIcon(new javax.swing.ImageIcon(getClass().getResource("/perpustakaan/covers/no-image.jpg"))); // NOI18N
        panelPilihBuku.add(iconCover, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 330, -1, -1));

        pilihBukuButton.setBackground(new java.awt.Color(0, 153, 0));
        pilihBukuButton.setFont(new java.awt.Font("Microsoft Tai Le", 1, 14)); // NOI18N
        pilihBukuButton.setForeground(new java.awt.Color(255, 255, 255));
        pilihBukuButton.setText("Pilih Buku");
        pilihBukuButton.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        pilihBukuButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                pilihBukuButtonActionPerformed(evt);
            }
        });
        panelPilihBuku.add(pilihBukuButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 480, -1, -1));

        jLabel3.setFont(new java.awt.Font("SansSerif", 1, 14)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(51, 51, 51));
        jLabel3.setText("Tanggal Pinjam");
        panelPilihBuku.add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(13, 48, -1, -1));

        fieldNamaAnggota.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                fieldNamaAnggotaActionPerformed(evt);
            }
        });
        panelPilihBuku.add(fieldNamaAnggota, new org.netbeans.lib.awtextra.AbsoluteConstraints(128, 75, 165, -1));

        jLabel4.setFont(new java.awt.Font("SansSerif", 1, 14)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(51, 51, 51));
        jLabel4.setText("Nama Anggota");
        panelPilihBuku.add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(13, 74, -1, -1));

        jLabel5.setFont(new java.awt.Font("SansSerif", 1, 14)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(51, 51, 51));
        jLabel5.setText("Jumlah Buku");
        panelPilihBuku.add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(13, 101, -1, -1));
        panelPilihBuku.add(fieldJumlah, new org.netbeans.lib.awtextra.AbsoluteConstraints(128, 102, 165, -1));

        pinjamButton.setBackground(new java.awt.Color(0, 153, 0));
        pinjamButton.setFont(new java.awt.Font("Microsoft Tai Le", 1, 14)); // NOI18N
        pinjamButton.setForeground(new java.awt.Color(255, 255, 255));
        pinjamButton.setText("Pinjam");
        pinjamButton.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        pinjamButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                pinjamButtonActionPerformed(evt);
            }
        });
        panelPilihBuku.add(pinjamButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(210, 130, -1, -1));

        jLabel12.setFont(new java.awt.Font("Microsoft Tai Le", 3, 18)); // NOI18N
        jLabel12.setForeground(new java.awt.Color(51, 51, 51));
        jLabel12.setText("Pilih Buku");
        panelPilihBuku.add(jLabel12, new org.netbeans.lib.awtextra.AbsoluteConstraints(13, 173, 90, -1));

        datePinjam.addContainerListener(new java.awt.event.ContainerAdapter() {
            public void componentAdded(java.awt.event.ContainerEvent evt) {
                datePinjamComponentAdded(evt);
            }
        });
        panelPilihBuku.add(datePinjam, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 50, 160, -1));

        jPanel1.add(panelPilihBuku, new org.netbeans.lib.awtextra.AbsoluteConstraints(33, 105, 300, 519));

        panelPengembalian.setBackground(new java.awt.Color(255, 255, 204));

        jLabel13.setFont(new java.awt.Font("SansSerif", 1, 14)); // NOI18N
        jLabel13.setForeground(new java.awt.Color(51, 51, 51));
        jLabel13.setText("Tanggal pengembalian");

        kembalikanButton.setBackground(new java.awt.Color(0, 153, 0));
        kembalikanButton.setFont(new java.awt.Font("Microsoft Tai Le", 1, 14)); // NOI18N
        kembalikanButton.setForeground(new java.awt.Color(255, 255, 255));
        kembalikanButton.setText("Kembalikan");
        kembalikanButton.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        kembalikanButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                kembalikanButtonActionPerformed(evt);
            }
        });

        jLabel14.setFont(new java.awt.Font("Microsoft Tai Le", 3, 18)); // NOI18N
        jLabel14.setForeground(new java.awt.Color(51, 51, 51));
        jLabel14.setText("Kembalikan Buku");

        javax.swing.GroupLayout panelPengembalianLayout = new javax.swing.GroupLayout(panelPengembalian);
        panelPengembalian.setLayout(panelPengembalianLayout);
        panelPengembalianLayout.setHorizontalGroup(
            panelPengembalianLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelPengembalianLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelPengembalianLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelPengembalianLayout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(kembalikanButton))
                    .addGroup(panelPengembalianLayout.createSequentialGroup()
                        .addComponent(jLabel14, javax.swing.GroupLayout.PREFERRED_SIZE, 158, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(panelPengembalianLayout.createSequentialGroup()
                        .addComponent(jLabel13)
                        .addGap(18, 18, 18)
                        .addComponent(dateKembalikan, javax.swing.GroupLayout.DEFAULT_SIZE, 194, Short.MAX_VALUE)))
                .addContainerGap())
        );
        panelPengembalianLayout.setVerticalGroup(
            panelPengembalianLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelPengembalianLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel14)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(panelPengembalianLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel13, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(dateKembalikan, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(kembalikanButton)
                .addGap(23, 23, 23))
        );

        jPanel1.add(panelPengembalian, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 470, 390, 110));

        transaksiTable.setBackground(new java.awt.Color(251, 255, 251));
        transaksiTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Kode Transaksi", "Tanggal Pinjam", "Nama Anggota", "ISBN 1", "ISBN 2", "Tanggal Kembali"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane1.setViewportView(transaksiTable);

        jPanel1.add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(367, 67, 390, 130));

        keluarTombol.setBackground(new java.awt.Color(255, 102, 102));
        keluarTombol.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        keluarTombol.setForeground(new java.awt.Color(255, 255, 255));
        keluarTombol.setText("Keluar");
        keluarTombol.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        keluarTombol.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                keluarTombolActionPerformed(evt);
            }
        });
        jPanel1.add(keluarTombol, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 590, -1, -1));

        jLabel15.setForeground(new java.awt.Color(51, 51, 51));
        jLabel15.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel15.setText("Versi 1.0.0");
        jPanel1.add(jLabel15, new org.netbeans.lib.awtextra.AbsoluteConstraints(690, 590, -1, -1));

        jLabel16.setForeground(new java.awt.Color(51, 51, 51));
        jLabel16.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel16.setText("Copyright © RandyBgn 2026 - All rights reserved");
        jLabel16.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        jPanel1.add(jLabel16, new org.netbeans.lib.awtextra.AbsoluteConstraints(490, 610, -1, -1));

        jPanel2.setBackground(new java.awt.Color(153, 255, 0));

        jLabel1.setFont(new java.awt.Font("Comic Sans MS", 1, 24)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("Perpustakaan Mini");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(265, 265, 265)
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 219, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(306, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel1)
                .addContainerGap())
        );

        jPanel1.add(jPanel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 790, 50));

        getContentPane().add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 790, 650));

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void cekKodeButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cekKodeButtonActionPerformed

    String kodeStr = fieldKodePinjam.getText().trim();
    if(kodeStr.isEmpty()) {
        JOptionPane.showMessageDialog(this, "Masukkan kode pinjam!", 
            "Peringatan", JOptionPane.WARNING_MESSAGE);
        return;
    }
    
    try {
        int kode = Integer.parseInt(kodeStr);
        
        if(isKodePinjamExist(kode)) {
            JOptionPane.showMessageDialog(this, "Kode Pinjam SUDAH digunakan!", 
                "Hasil Cek", JOptionPane.WARNING_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(this, "Kode Pinjam TERSEDIA!", 
                "Hasil Cek", JOptionPane.INFORMATION_MESSAGE);
            datePinjam.requestFocus();
        }
        
    } catch(NumberFormatException e) {
        JOptionPane.showMessageDialog(this, "Kode Pinjam harus angka!", 
            "Error", JOptionPane.ERROR_MESSAGE);
    }

    }//GEN-LAST:event_cekKodeButtonActionPerformed

    private void pilihBukuButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_pilihBukuButtonActionPerformed

        String isbn = fieldISBN.getText().trim();
        boolean ditemukan = false;

        for(Object obj : data_buku) {
            Object[] dataBuku = (Object[]) obj;
            String ISBN = (String) dataBuku[0];

            if(isbn.equals(ISBN)) {

                if(counterInputBuku == 0) {
                    transaksiSementara[3] = ISBN;
                } else {
                    transaksiSementara[4] = ISBN;
                }

                fieldJudul.setText((String) dataBuku[1]);
                fieldPenerbit.setText((String) dataBuku[2]);
                fieldTahunTerbit.setText((String) dataBuku[3]);
                updateCover((String) dataBuku[4]);

                counterInputBuku++;
                ditemukan = true;

                JOptionPane.showMessageDialog(this, 
                    "Buku ke-" + counterInputBuku + " ditambahkan!\n" +
                    "Sisa input: " + (jumlahBukuPinjam - counterInputBuku),
                    "Sukses", JOptionPane.INFORMATION_MESSAGE);
                resetFBuku();
                if(counterInputBuku == jumlahBukuPinjam) {
                    simpanTransaksi();
                }

                break;
            }
        }

        if(!ditemukan) {
            JOptionPane.showMessageDialog(this, 
                "Buku dengan ISBN " + isbn + " tidak ditemukan!", 
                "Error", JOptionPane.ERROR_MESSAGE);
            resetFBuku();
        }

    }//GEN-LAST:event_pilihBukuButtonActionPerformed

    private void pinjamButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_pinjamButtonActionPerformed

    if(datePinjam.getDate() == null) {
        JOptionPane.showMessageDialog(this, "Tanggal harus di isi!", 
            "Peringatan", JOptionPane.WARNING_MESSAGE);
        return;
    }
    if(fieldNamaAnggota.getText().trim().isEmpty()) {
        JOptionPane.showMessageDialog(this, "Nama anggota harus diisi!", 
            "Peringatan", JOptionPane.WARNING_MESSAGE);
        return;
    }
    
    String kodeStr = fieldKodePinjam.getText().trim();
    if(kodeStr.isEmpty()) {
        JOptionPane.showMessageDialog(this, "Kode Pinjam harus diisi!", 
            "Peringatan", JOptionPane.WARNING_MESSAGE);
        return;
    }
    
    try {
        int kodePinjam = Integer.parseInt(kodeStr);
        Date tanggalPinjam = datePinjam.getDate();
        
        if(isKodePinjamExist(kodePinjam)) {
            JOptionPane.showMessageDialog(this, "Kode Pinjam sudah digunakan!", 
                "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        String jumlahStr = fieldJumlah.getText().trim();
        if(jumlahStr.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Jumlah buku harus diisi!", 
                "Peringatan", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        int jumlahBuku = Integer.parseInt(jumlahStr);
        if(jumlahBuku < 1 || jumlahBuku > 2) {
            JOptionPane.showMessageDialog(this, "Jumlah buku minimal 1 dan maksimal 2!", 
                "Peringatan", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        jumlahBukuPinjam = jumlahBuku;
        statusMeminjam = true;
        counterInputBuku = 0;
        
        transaksiSementara = new Object[]{
        kodePinjam,
        new SimpleDateFormat("yyyy-MM-dd").format(tanggalPinjam),
        fieldNamaAnggota.getText().trim(),
        "", "", null, 0
        };

        
        fieldISBN.setText("");
        fieldJudul.setText("");
        fieldPenerbit.setText("");
        fieldTahunTerbit.setText("");
        updateCover("");
        fieldKodePinjam.setEnabled(false);
        
        JOptionPane.showMessageDialog(this, 
            "Masukkan " + jumlahBukuPinjam + " buku satu per satu melalui tombol 'Pilih Buku'",
            "Informasi", JOptionPane.INFORMATION_MESSAGE);
        fieldISBN.requestFocus();
        
    } catch(NumberFormatException e) {
        JOptionPane.showMessageDialog(this, "Kode Pinjam dan Jumlah buku harus angka!", 
            "Error", JOptionPane.ERROR_MESSAGE);
    }

    }//GEN-LAST:event_pinjamButtonActionPerformed

    private void kembalikanButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_kembalikanButtonActionPerformed
 if(dateKembalikan.getDate() == null){
     JOptionPane.showMessageDialog(this, "Tanggal pengembalian harus di isi!", 
                    "Peringatan", JOptionPane.INFORMATION_MESSAGE);
                return;
 }
 String kodeStr = JOptionPane.showInputDialog(this, "Masukkan Kode Pinjam:");
if(kodeStr == null || kodeStr.trim().isEmpty()) return;

try {
    int kode = Integer.parseInt(kodeStr);
    boolean ditemukan = false;
    
    for(int i = 0; i < transaksi.length; i++) {
        Object[] dataTransaksi = (Object[]) transaksi[i];
        int kodeData = (int) dataTransaksi[0];
        
        if(kodeData == kode) {
            ditemukan = true;
            
            if(dataTransaksi[5] != null) {
                JOptionPane.showMessageDialog(this, "Buku sudah dikembalikan!", 
                    "Informasi", JOptionPane.INFORMATION_MESSAGE);
                return;
            }
            
            if(dateKembalikan.getDate() == null) {
                JOptionPane.showMessageDialog(this, "Pilih tanggal kembali!", 
                    "Peringatan", JOptionPane.WARNING_MESSAGE);
                return;
            }
            
            String tanggalKembali = new SimpleDateFormat("yyyy-MM-dd").format(dateKembalikan.getDate());
            
            try {
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                Date tglPinjam = format.parse((String) dataTransaksi[1]);
                Date tglKembali = format.parse(tanggalKembali);
                
                // PENAMBAHAN: Validasi tanggal pinjam tidak boleh lebih besar dari tanggal kembali
                if(tglKembali.before(tglPinjam)) {
                    JOptionPane.showMessageDialog(this, 
                        "Tanggal kembali tidak boleh lebih kecil dari tanggal pinjam!\n" +
                        "Tanggal Pinjam: " + dataTransaksi[1] + "\n" +
                        "Tanggal Kembali: " + tanggalKembali,
                        "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                
                // PENAMBAHAN: Validasi tanggal pinjam tidak boleh sama dengan tanggal kembali
                if(tglKembali.equals(tglPinjam)) {
                    JOptionPane.showMessageDialog(this, 
                        "Tanggal kembali tidak boleh sama dengan tanggal pinjam!\n" +
                        "Minimal pengembalian adalah 1 hari setelah tanggal pinjam.",
                        "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                
                dataTransaksi[5] = tanggalKembali;
                
                long selisih = tglKembali.getTime() - tglPinjam.getTime();
                long selisihHari = selisih / (1000 * 60 * 60 * 24);
                
                int denda = 0;
                if(selisihHari > 5) {
                    denda = (int) ((selisihHari - 5) * 5000);
                    dataTransaksi[6] = denda;
                }
                
                JOptionPane.showMessageDialog(this, 
                    "Pengembalian berhasil!\n" +
                    "Kode Pinjam: " + kodeData + "\n" +
                    "Nama Anggota: " + dataTransaksi[2] + "\n" +
                    "Tanggal Pinjam: " + dataTransaksi[1] + "\n" +
                    "Tanggal Kembali: " + tanggalKembali + "\n" +
                    "Total Hari: " + selisihHari + " hari\n" +
                    (denda > 0 ? "Denda: Rp " + denda : "Tidak ada denda"),
                    "Sukses", JOptionPane.INFORMATION_MESSAGE);
                
            } catch(ParseException e) {
                e.printStackTrace();
            }
            
            loadTable();
            clearForm();
            break;
        }
    }
    
    if(!ditemukan) {
        JOptionPane.showMessageDialog(this, "Kode Pinjam tidak ditemukan!", 
            "Error", JOptionPane.ERROR_MESSAGE);
    }
    
} catch(NumberFormatException e) {
    JOptionPane.showMessageDialog(this, "Kode Pinjam harus angka!", 
        "Error", JOptionPane.ERROR_MESSAGE);
}
    }//GEN-LAST:event_kembalikanButtonActionPerformed

    private void keluarTombolActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_keluarTombolActionPerformed
        // TODO add your handling code here:
        int pilih = JOptionPane.showConfirmDialog(null, "Apakah anda yakin ingin keluar", "Keluar",JOptionPane.YES_NO_OPTION);
        
        if(pilih == JOptionPane.YES_OPTION){
            System.exit(0);
        }
    }//GEN-LAST:event_keluarTombolActionPerformed

    private void fieldNamaAnggotaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_fieldNamaAnggotaActionPerformed
        // TODO add your handling code here:
        fieldJumlah.requestFocus();
    }//GEN-LAST:event_fieldNamaAnggotaActionPerformed

    private void datePinjamComponentAdded(java.awt.event.ContainerEvent evt) {//GEN-FIRST:event_datePinjamComponentAdded
        // TODO add your handling code here:
        fieldNamaAnggota.requestFocus();
    }//GEN-LAST:event_datePinjamComponentAdded

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
            java.util.logging.Logger.getLogger(main.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(main.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(main.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(main.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new main().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton cekKodeButton;
    private com.toedter.calendar.JDateChooser dateKembalikan;
    private com.toedter.calendar.JDateChooser datePinjam;
    private javax.swing.JTextField fieldISBN;
    private javax.swing.JTextField fieldJudul;
    private javax.swing.JTextField fieldJumlah;
    private javax.swing.JTextField fieldKodePinjam;
    private javax.swing.JTextField fieldNamaAnggota;
    private javax.swing.JTextField fieldPenerbit;
    private javax.swing.JTextField fieldTahunTerbit;
    private javax.swing.JLabel iconCover;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
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
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JButton keluarTombol;
    private javax.swing.JButton kembalikanButton;
    private javax.swing.JPanel panelPengembalian;
    private javax.swing.JPanel panelPilihBuku;
    private javax.swing.JButton pilihBukuButton;
    private javax.swing.JButton pinjamButton;
    private javax.swing.JTable transaksiTable;
    // End of variables declaration//GEN-END:variables
}
