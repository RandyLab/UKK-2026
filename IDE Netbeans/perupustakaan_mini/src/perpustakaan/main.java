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
        if(fieldISBN.getText().isEmpty()) return;
        try {
            int kodeISBN = Integer.parseInt(fieldISBN.getText());
            boolean ditemukan = false;
            for(Object obj : data_buku){
                Object[] dataBuku = (Object[]) obj;
                int ISBN = (int) dataBuku[0];
                if(kodeISBN == ISBN){
                    fieldJudul.setText((String) dataBuku[1]);
                    fieldPenerbit.setText((String) dataBuku[2]);
                    fieldTahunTerbit.setText((String) dataBuku[3]);
                    updateCover((String) dataBuku[4]);
                    ditemukan = true;
                    break;
                }
            }
            if(!ditemukan){
                fieldJudul.setText("Buku Tidak Ditemukan");
                fieldPenerbit.setText("");
                fieldTahunTerbit.setText("");
                updateCover("");
            }
        } catch(NumberFormatException e) {}
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
    
    Object[] data_buku ={
    new Object[] {123456, "Fumetsu no Anata", "Stone", "2026-10-13", "src/perpustakaan/covers/fumetsu.jpg"}
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
        jLabel1 = new javax.swing.JLabel();
        fieldKodePinjam = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        cekKodeButton = new javax.swing.JButton();
        panelPinjam = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        fieldNamaAnggota = new javax.swing.JTextField();
        fieldJumlah = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        datePinjam = new com.toedter.calendar.JDateChooser();
        pinjamButton = new javax.swing.JButton();
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
        panelPengembalian = new javax.swing.JPanel();
        jLabel13 = new javax.swing.JLabel();
        kembalikanButton = new javax.swing.JButton();
        dateKembalikan = new com.toedter.calendar.JDateChooser();
        jScrollPane1 = new javax.swing.JScrollPane();
        transaksiTable = new javax.swing.JTable();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel1.setBackground(new java.awt.Color(102, 255, 102));

        jLabel1.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N
        jLabel1.setText("Perpustakaan Mini");

        jLabel2.setText("Kode Pinjam");

        cekKodeButton.setText("Cek");
        cekKodeButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cekKodeButtonActionPerformed(evt);
            }
        });

        jLabel3.setText("Tanggal Pinjam");

        jLabel4.setText("Nama Anggota");

        jLabel5.setText("Jumlah Buku");

        pinjamButton.setText("Pinjam");
        pinjamButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                pinjamButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout panelPinjamLayout = new javax.swing.GroupLayout(panelPinjam);
        panelPinjam.setLayout(panelPinjamLayout);
        panelPinjamLayout.setHorizontalGroup(
            panelPinjamLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelPinjamLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelPinjamLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelPinjamLayout.createSequentialGroup()
                        .addGroup(panelPinjamLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel3)
                            .addComponent(jLabel4)
                            .addComponent(jLabel5))
                        .addGap(56, 56, 56)
                        .addGroup(panelPinjamLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(fieldJumlah)
                            .addComponent(fieldNamaAnggota)
                            .addComponent(datePinjam, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelPinjamLayout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(pinjamButton)))
                .addContainerGap())
        );
        panelPinjamLayout.setVerticalGroup(
            panelPinjamLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelPinjamLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelPinjamLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel3)
                    .addComponent(datePinjam, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panelPinjamLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(fieldNamaAnggota, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panelPinjamLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(fieldJumlah, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 11, Short.MAX_VALUE)
                .addComponent(pinjamButton)
                .addContainerGap())
        );

        jLabel6.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel6.setText("Pilih Buku");

        jLabel7.setText("ISBN");

        jLabel8.setText("Judul");

        jLabel9.setText("Penerbit");

        jLabel10.setText("Tahun Terbit");

        jLabel11.setText("Cover");

        iconCover.setIcon(new javax.swing.ImageIcon(getClass().getResource("/perpustakaan/covers/no-image.jpg"))); // NOI18N

        pilihBukuButton.setText("Pilih Buku");
        pilihBukuButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                pilihBukuButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout panelPilihBukuLayout = new javax.swing.GroupLayout(panelPilihBuku);
        panelPilihBuku.setLayout(panelPilihBukuLayout);
        panelPilihBukuLayout.setHorizontalGroup(
            panelPilihBukuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelPilihBukuLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelPilihBukuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(panelPilihBukuLayout.createSequentialGroup()
                        .addComponent(jLabel6)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(panelPilihBukuLayout.createSequentialGroup()
                        .addGroup(panelPilihBukuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel10)
                            .addComponent(jLabel9)
                            .addComponent(jLabel8)
                            .addComponent(jLabel7)
                            .addComponent(jLabel11)
                            .addComponent(pilihBukuButton))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(panelPilihBukuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelPilihBukuLayout.createSequentialGroup()
                                .addGroup(panelPilihBukuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(fieldISBN, javax.swing.GroupLayout.PREFERRED_SIZE, 157, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(fieldJudul, javax.swing.GroupLayout.PREFERRED_SIZE, 157, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(fieldPenerbit, javax.swing.GroupLayout.PREFERRED_SIZE, 157, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(fieldTahunTerbit, javax.swing.GroupLayout.PREFERRED_SIZE, 157, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(36, 36, 36))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelPilihBukuLayout.createSequentialGroup()
                                .addComponent(iconCover, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(63, 63, 63))))))
        );
        panelPilihBukuLayout.setVerticalGroup(
            panelPilihBukuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelPilihBukuLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel6)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panelPilihBukuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(panelPilihBukuLayout.createSequentialGroup()
                        .addComponent(jLabel9)
                        .addGap(32, 32, 32))
                    .addGroup(panelPilihBukuLayout.createSequentialGroup()
                        .addGroup(panelPilihBukuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(fieldISBN, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel7))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(panelPilihBukuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(fieldJudul, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel8))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(fieldPenerbit, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(panelPilihBukuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(fieldTahunTerbit, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel10))))
                .addGroup(panelPilihBukuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelPilihBukuLayout.createSequentialGroup()
                        .addComponent(jLabel11)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 104, Short.MAX_VALUE)
                        .addComponent(pilihBukuButton))
                    .addGroup(panelPilihBukuLayout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(iconCover, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );

        jLabel13.setText("Tanggal pengembalian");

        kembalikanButton.setText("Kembalikan");
        kembalikanButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                kembalikanButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout panelPengembalianLayout = new javax.swing.GroupLayout(panelPengembalian);
        panelPengembalian.setLayout(panelPengembalianLayout);
        panelPengembalianLayout.setHorizontalGroup(
            panelPengembalianLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelPengembalianLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelPengembalianLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelPengembalianLayout.createSequentialGroup()
                        .addComponent(jLabel13)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(dateKembalikan, javax.swing.GroupLayout.DEFAULT_SIZE, 155, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelPengembalianLayout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(kembalikanButton)))
                .addContainerGap())
        );
        panelPengembalianLayout.setVerticalGroup(
            panelPengembalianLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelPengembalianLayout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addGroup(panelPengembalianLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel13)
                    .addComponent(dateKembalikan, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(20, 20, 20)
                .addComponent(kembalikanButton)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

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

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel1)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(10, 10, 10)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(panelPilihBuku, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(panelPinjam, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabel2)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(fieldKodePinjam, javax.swing.GroupLayout.PREFERRED_SIZE, 152, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 36, Short.MAX_VALUE)
                                .addComponent(cekKodeButton)))
                        .addGap(26, 26, 26)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(panelPengembalian, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 490, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addGap(16, 16, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(37, 37, 37)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 216, Short.MAX_VALUE)
                .addGap(18, 18, 18)
                .addComponent(panelPengembalian, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(214, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addComponent(jLabel1)
                .addGap(8, 8, 8)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(fieldKodePinjam, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cekKodeButton))
                .addGap(18, 18, 18)
                .addComponent(panelPinjam, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(panelPilihBuku, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(54, 54, 54))
        );

        getContentPane().add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 850, 570));

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
        }
        
    } catch(NumberFormatException e) {
        JOptionPane.showMessageDialog(this, "Kode Pinjam harus angka!", 
            "Error", JOptionPane.ERROR_MESSAGE);
    }

    }//GEN-LAST:event_cekKodeButtonActionPerformed

    private void pilihBukuButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_pilihBukuButtonActionPerformed
if(!statusMeminjam) {
    JOptionPane.showMessageDialog(this, "Tekan tombol Pinjam terlebih dahulu!", 
        "Peringatan", JOptionPane.WARNING_MESSAGE);
    return;
}

if(counterInputBuku >= jumlahBukuPinjam) {
    JOptionPane.showMessageDialog(this, "Semua buku sudah diinput!", 
        "Informasi", JOptionPane.INFORMATION_MESSAGE);
    return;
}

String isbnStr = fieldISBN.getText().trim();
if(isbnStr.isEmpty()) {
    JOptionPane.showMessageDialog(this, "ISBN tidak boleh kosong!", 
        "Peringatan", JOptionPane.WARNING_MESSAGE);
    return;
}

try {
    int isbn = Integer.parseInt(isbnStr);
    boolean ditemukan = false;
    
    for(Object obj : data_buku) {
        Object[] dataBuku = (Object[]) obj;
        int ISBN = (int) dataBuku[0];
        
        if(isbn == ISBN) {
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
            
            if(counterInputBuku == jumlahBukuPinjam) {
                simpanTransaksi();
                updateBuku();
                
            }
            
            fieldISBN.setText("");
            
            break;
        }
    }
    
    if(!ditemukan) {
        JOptionPane.showMessageDialog(this, "Buku dengan ISBN " + isbn + " tidak ditemukan!", 
            "Error", JOptionPane.ERROR_MESSAGE);
    }
    
} catch(NumberFormatException e) {
    JOptionPane.showMessageDialog(this, "ISBN harus angka!", 
        "Error", JOptionPane.ERROR_MESSAGE);
}
    }//GEN-LAST:event_pilihBukuButtonActionPerformed

    private void pinjamButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_pinjamButtonActionPerformed

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
            new SimpleDateFormat("yyyy-MM-dd").format(new Date()),
            fieldNamaAnggota.getText().trim(),
            0, 0, null, 0
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
        
    } catch(NumberFormatException e) {
        JOptionPane.showMessageDialog(this, "Kode Pinjam dan Jumlah buku harus angka!", 
            "Error", JOptionPane.ERROR_MESSAGE);
    }

    }//GEN-LAST:event_pinjamButtonActionPerformed

    private void kembalikanButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_kembalikanButtonActionPerformed
     
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
            dataTransaksi[5] = tanggalKembali;
            
            try {
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                Date tglPinjam = format.parse((String) dataTransaksi[1]);
                Date tglKembali = format.parse(tanggalKembali);
                
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
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JButton kembalikanButton;
    private javax.swing.JPanel panelPengembalian;
    private javax.swing.JPanel panelPilihBuku;
    private javax.swing.JPanel panelPinjam;
    private javax.swing.JButton pilihBukuButton;
    private javax.swing.JButton pinjamButton;
    private javax.swing.JTable transaksiTable;
    // End of variables declaration//GEN-END:variables
}
