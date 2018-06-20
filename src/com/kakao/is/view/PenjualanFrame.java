/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kakao.is.view;

import com.kakao.is.controller.Database;
import com.kakao.is.controller.LoginController;
import com.kakao.is.model.MenuTrolly;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JInternalFrame;
import javax.swing.JOptionPane;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author gokil
 */
public class PenjualanFrame extends javax.swing.JFrame {

    /**
     * Creates new form PenjualanFrame
     */
    static PenjualanFrame instance;
    public int current_list;
    public static final int MODE_TAMBAH = 1;
    public static final int MODE_EDIT = 2;
    public static final int MODE_VIEW = 3;
    public static final int LIST_MAKANAN = 1;
    public static final int LIST_MINUMAN = 2;
    public static final int LIST_ALL = 3;
    //DefaultTableModel new_tbl_order = new DefaultTableModel();


    public ArrayList<MenuTrolly> keranjang;
    public HashMap<String, MenuTrolly> dict_keranjang;
    String namaPemesan = "-";
    private int total_subtotal = 0;

    public PenjualanFrame() {
       
        initComponents();
        keranjang = new ArrayList<>();
        dict_keranjang = new HashMap<>();
        idNota = getIdNota();
        current_list = LIST_ALL;
        //new_tbl_order = (DefaultTableModel)tblOrder.getModel();
        tampilkanMenu();

        tblOrder.getModel().addTableModelListener(new TableModelListener() {
            public void tableChanged(TableModelEvent e) {
                if (!keranjang.isEmpty()) {
                    System.out.println(e.getType());
                    if (e.getType() == 0) {
                        System.out.println(tblOrder.getValueAt(e.getLastRow(), 1) + "");
                        //tblOrder.setValueAt(tblOrder.getValueAt(e.getLastRow(), 3), e.getLastRow(), 4);
                        //tblOrder.editingStopped();
                        //tblOrder.setValueAt(e.getLastRow(), 1, 1);

                        //Update data pesanan yang ada di keranjang
                        MenuTrolly update = dict_keranjang.get(tblOrder.getValueAt(e.getLastRow(), 1));
                        update.setQty(to_int(tblOrder.getValueAt(e.getLastRow(), 3).toString()));
                        update.setSubtotal(update.getHarga() * update.getQty());
                        tampilkanKeranjang();

                        System.out.println(e.getLastRow());
                    }

                }
            }
        });

        txtDiskon.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent de) {
                try {
                    diskon = to_int(txtDiskon.getText());
                    txtTotal.setText(((total_subtotal) - diskon) + "");
                } catch (NumberFormatException e) {

                }
            }

            @Override
            public void removeUpdate(DocumentEvent de) {
                try {
                    diskon = to_int(txtDiskon.getText());
                    txtTotal.setText(((total_subtotal) - diskon) + "");
                } catch (NumberFormatException e) {

                }
            }

            @Override
            public void changedUpdate(DocumentEvent de) {
                try {
                    diskon = to_int(txtDiskon.getText());
                    txtTotal.setText(((total_subtotal) - diskon) + "");
                } catch (NumberFormatException e) {

                }
            }
        });
        txtTunai.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent de) {
                try {
                    tunai = to_int(txtTunai.getText());
                    txtKembali.setText((tunai - (total_subtotal - diskon)) + "");
                } catch (NumberFormatException e) {

                }
            }

            @Override
            public void removeUpdate(DocumentEvent de) {
                try {
                    tunai = to_int(txtTunai.getText());
                    txtKembali.setText((tunai - (total_subtotal - diskon)) + "");
                } catch (NumberFormatException e) {

                }

            }

            @Override
            public void changedUpdate(DocumentEvent de) {
                try {
                    tunai = to_int(txtTunai.getText());
                    txtKembali.setText((tunai - (total_subtotal - diskon)) + "");
                } catch (NumberFormatException e) {

                }
            }
        });
        //tblOrder.getModel().addTableModelListener(tblMenu);
        tampilkanKeranjang();
    }

    int to_int(String a) {
        return Integer.parseInt(a);
    }

    public static PenjualanFrame getInstance() {
        if (instance == null) {
            instance = new PenjualanFrame();
        }
        return instance;
    }

    public void tampilkanMenu() {
        //String kolom[] = {"ID","MenuTrolly"};
        DefaultTableModel dtm = (DefaultTableModel) tblMenu.getModel();
        //Hapus data
        //tblMenu.getCellEditor();
        for (int i = 0; i < dtm.getRowCount(); i++) {
            dtm.removeRow(i);
        }
        String sql = "";
        switch (current_list) {
            case LIST_ALL: {
                sql = "SELECT idProduk,namaProduk FROM tb_produk";
                break;
            }
            case LIST_MAKANAN: {
                sql = "SELECT idProduk,namaProduk FROM tb_produk "
                        + "WHERE jenis=" + LIST_MAKANAN;
                break;
            }
            case LIST_MINUMAN: {
                sql = "SELECT idProduk,namaProduk FROM tb_produk "
                        + "WHERE jenis=" + LIST_MINUMAN;
                break;
            }
        }
        ResultSet hasil = Database.query(sql);
        int id;
        String nama;
        try {
            while (hasil.next()) {
                id = hasil.getInt("IdProduk");
                nama = hasil.getString("namaProduk");
                String coloumn[] = {id + "", nama};

                dtm.addRow(coloumn);
            }
        } catch (SQLException ex) {
            Logger.getLogger(PenjualanFrame.class.getName()).log(Level.SEVERE, null, ex);
        }
        tblMenu.setModel(dtm);

    }

    private void clear_tbl_order() {
        DefaultTableModel dtm = (DefaultTableModel) tblOrder.getModel();
        dtm.getDataVector().removeAllElements();
    }

    public void tampilkanKeranjang() {
        //String kolom[] = {"No","MenuTrolly","Harga","Banyak","Subtotal"};
        //tblOrder.setModel(new DefaultTableModel(,kolom));
        //tblOrder.setModel(new_tbl_order);
        //tblOrder.removeAll();
        total_subtotal = 0;
        DefaultTableModel dtm = (DefaultTableModel) tblOrder.getModel();
        //tblOrder.setModel(dtm);
        //Hapus data
        dtm.getDataVector().removeAllElements();
        tblOrder.setModel(dtm);
        System.out.println(dtm.getRowCount() + "==");
        //tblOrder.setModel(dtm);

        if (!keranjang.isEmpty()) {

            for (int i = 0; i < keranjang.size(); i++) {
                MenuTrolly current = keranjang.get(i);
                //int id = current.getId();
                String nama = current.getNama();
                int harga = current.getHarga();
                int qty = current.getQty();
                int subtotal = current.getSubtotal();
                total_subtotal += subtotal;
                String coloumn[] = {(i + 1) + "", nama, harga + "", qty + "", subtotal + ""};
                dtm.addRow(coloumn);

            }
            //dtm.set
            /* 
                Hitung keseluruhan
             */
            calculate_biaya();
        } else {
            String coloumn[] = {"", "", "", "", ""};
            dtm.addRow(coloumn);
        }
        tblOrder.setModel(dtm);

        //tblOrder.setEditingColumn(3);
    }
    int diskon;
    int tunai;
    int total;
    int kembalian;

    void calculate_biaya() {
        txtHargaJual.setText(total_subtotal + "");
        diskon = to_int(txtDiskon.getText());
        tunai = to_int(txtTunai.getText());
        //total = ;
        //kembalian = tunai - total ;
        txtTotal.setText("" + (total_subtotal - diskon));
        txtKembali.setText((tunai - total) + "");
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        PenjualanInternalFrame = new javax.swing.JInternalFrame();
        jLabel5 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        tblMenu = new javax.swing.JTable();
        btnTambah = new javax.swing.JButton();
        jPanel3 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblOrder = new javax.swing.JTable();
        btnHapus = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        txtHargaJual = new javax.swing.JLabel();
        txtDiskon = new javax.swing.JTextField();
        txtTotal = new javax.swing.JLabel();
        txtTunai = new javax.swing.JTextField();
        txtKembali = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jPanel4 = new javax.swing.JPanel();
        btnBatal = new javax.swing.JButton();
        btnCetak = new javax.swing.JButton();
        btnPreview = new javax.swing.JButton();
        txtNamaPemesan = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        btnClose = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setFocusTraversalPolicyProvider(true);
        setUndecorated(true);
        setResizable(false);

        PenjualanInternalFrame.setVisible(true);

        jLabel5.setFont(new java.awt.Font("Century Schoolbook L", 3, 18)); // NOI18N
        jLabel5.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel5.setText("Sale Order");
        jLabel5.setToolTipText("");

        tblMenu.setAutoCreateRowSorter(true);
        tblMenu.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "ID", "Produk"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tblMenu.setShowHorizontalLines(false);
        tblMenu.setShowVerticalLines(false);
        tblMenu.getTableHeader().setReorderingAllowed(false);
        tblMenu.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblMenuMouseClicked(evt);
            }
        });
        jScrollPane2.setViewportView(tblMenu);
        if (tblMenu.getColumnModel().getColumnCount() > 0) {
            tblMenu.getColumnModel().getColumn(0).setMinWidth(30);
            tblMenu.getColumnModel().getColumn(0).setPreferredWidth(30);
            tblMenu.getColumnModel().getColumn(0).setMaxWidth(30);
        }

        btnTambah.setFont(new java.awt.Font("Ubuntu", 0, 14)); // NOI18N
        btnTambah.setText("+");
        btnTambah.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnTambahActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 146, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnTambah, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(43, 43, 43)
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 317, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(123, 123, 123)
                        .addComponent(btnTambah, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(55, Short.MAX_VALUE))
        );

        tblOrder.setAutoCreateRowSorter(true);
        tblOrder.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "No", "Menu", "Harga", "Banyak", "Subtotal"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Integer.class, java.lang.String.class, java.lang.Integer.class, java.lang.Integer.class, java.lang.Integer.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, true, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tblOrder.setShowHorizontalLines(false);
        tblOrder.setShowVerticalLines(false);
        tblOrder.getTableHeader().setReorderingAllowed(false);
        jScrollPane1.setViewportView(tblOrder);
        if (tblOrder.getColumnModel().getColumnCount() > 0) {
            tblOrder.getColumnModel().getColumn(0).setMinWidth(30);
            tblOrder.getColumnModel().getColumn(0).setPreferredWidth(30);
            tblOrder.getColumnModel().getColumn(0).setMaxWidth(30);
            tblOrder.getColumnModel().getColumn(1).setMinWidth(150);
            tblOrder.getColumnModel().getColumn(1).setPreferredWidth(150);
            tblOrder.getColumnModel().getColumn(1).setMaxWidth(150);
        }

        btnHapus.setFont(new java.awt.Font("Ubuntu", 1, 10)); // NOI18N
        btnHapus.setText("--");
        btnHapus.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnHapusActionPerformed(evt);
            }
        });

        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel1.setText("Harga Jual :");

        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel2.setText("Diskon :");

        jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel3.setText("Tunai :");

        txtHargaJual.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        txtHargaJual.setText("10000");

        txtDiskon.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        txtDiskon.setText("0");
        txtDiskon.setCursor(new java.awt.Cursor(java.awt.Cursor.TEXT_CURSOR));
        txtDiskon.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtDiskonFocusLost(evt);
            }
        });
        txtDiskon.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtDiskonActionPerformed(evt);
            }
        });

        txtTotal.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        txtTotal.setText("8000");

        txtTunai.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        txtTunai.setText("0");
        txtTunai.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtTunaiFocusLost(evt);
            }
        });
        txtTunai.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtTunaiActionPerformed(evt);
            }
        });

        txtKembali.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        txtKembali.setText("2000");

        jLabel4.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel4.setText("Total :");

        jLabel7.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel7.setText("Kembali :");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 93, Short.MAX_VALUE)
                    .addComponent(jLabel2, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel7, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(txtHargaJual, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(txtDiskon)
                        .addComponent(txtTotal, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(txtTunai, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(txtKembali))
                .addGap(53, 53, 53))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(txtHargaJual))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(txtDiskon, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtTotal)
                    .addComponent(jLabel4))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(txtTunai, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtKembali)
                    .addComponent(jLabel7))
                .addGap(9, 9, 9))
        );

        btnBatal.setText("Batal");
        btnBatal.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBatalActionPerformed(evt);
            }
        });

        btnCetak.setText("Cetak");
        btnCetak.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCetakActionPerformed(evt);
            }
        });

        btnPreview.setText("Preview");
        btnPreview.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPreviewActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(98, 98, 98)
                .addComponent(btnBatal, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(btnPreview, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(btnCetak, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(btnBatal)
                .addComponent(btnCetak)
                .addComponent(btnPreview))
        );

        txtNamaPemesan.setText("-");
        txtNamaPemesan.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtNamaPemesanFocusLost(evt);
            }
        });
        txtNamaPemesan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtNamaPemesanActionPerformed(evt);
            }
        });

        jLabel6.setText("Pemesan :");

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addGap(0, 17, Short.MAX_VALUE)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 78, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(txtNamaPemesan, javax.swing.GroupLayout.PREFERRED_SIZE, 137, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 495, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(5, 5, 5)
                .addComponent(btnHapus)
                .addGap(1, 1, 1))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtNamaPemesan, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel6))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 237, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGap(119, 119, 119)
                        .addComponent(btnHapus, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        btnClose.setFont(new java.awt.Font("OCR A Extended", 1, 11)); // NOI18N
        btnClose.setForeground(new java.awt.Color(255, 0, 0));
        btnClose.setText("X");
        btnClose.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCloseActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout PenjualanInternalFrameLayout = new javax.swing.GroupLayout(PenjualanInternalFrame.getContentPane());
        PenjualanInternalFrame.getContentPane().setLayout(PenjualanInternalFrameLayout);
        PenjualanInternalFrameLayout.setHorizontalGroup(
            PenjualanInternalFrameLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(PenjualanInternalFrameLayout.createSequentialGroup()
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
            .addGroup(PenjualanInternalFrameLayout.createSequentialGroup()
                .addGap(59, 59, 59)
                .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 660, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 10, Short.MAX_VALUE)
                .addComponent(btnClose)
                .addContainerGap())
        );
        PenjualanInternalFrameLayout.setVerticalGroup(
            PenjualanInternalFrameLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(PenjualanInternalFrameLayout.createSequentialGroup()
                .addGroup(PenjualanInternalFrameLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(btnClose))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 8, Short.MAX_VALUE)
                .addGroup(PenjualanInternalFrameLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, PenjualanInternalFrameLayout.createSequentialGroup()
                        .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap())
                    .addComponent(jPanel3, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(PenjualanInternalFrame, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(PenjualanInternalFrame)
        );

        PenjualanInternalFrame.getAccessibleContext().setAccessibleName("jualInternalFrame");
        PenjualanInternalFrame.getAccessibleContext().setAccessibleParent(null);

        getAccessibleContext().setAccessibleParent(this);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnTambahActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTambahActionPerformed
        // TODO add your handling code here:
        int posisi = -1;
        posisi = tblMenu.getSelectedRow();
        if (posisi == -1) {
            JOptionPane.showMessageDialog(this, "Silahkan pilih menu terlebih dahulu", "Warning",
                    JOptionPane.WARNING_MESSAGE);
        } else {
            //get the id
            String id = tblMenu.getValueAt(posisi, 0).toString();
            String sql = sql = "SELECT namaProduk,hargaJual FROM tb_produk"
                    + " WHERE idProduk=" + id;
            ResultSet hasil = Database.query(sql);
            String nama = "";
            int harga = 0;
            try {
                hasil.next();
                nama = hasil.getString("namaProduk");
                harga = hasil.getInt("hargaJual");

            } catch (SQLException ex) {
                Logger.getLogger(PenjualanFrame.class.getName()).log(Level.SEVERE, null, ex);
            }

            //insert to basket and set default for qty, subtotal
            int idProduk = Integer.parseInt(id);
            MenuTrolly tmb_barang = new MenuTrolly(idProduk, nama, harga, 1, harga);
            keranjang.add(tmb_barang);
            dict_keranjang.put(tmb_barang.getNama(), tmb_barang);
            tampilkanKeranjang();
        }
    }//GEN-LAST:event_btnTambahActionPerformed

    private void btnHapusActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnHapusActionPerformed
        // TODO add your handling code here:
        int posisi = -1;
        posisi = tblOrder.getSelectedRow();

        if (posisi == -1) {
            JOptionPane.showMessageDialog(this, "Silahkan pilih order yang akan dihapus", "Warning",
                    JOptionPane.WARNING_MESSAGE);
        } else {
            String nama = tblOrder.getValueAt(posisi, 1).toString();

            MenuTrolly curr;
            //find MenuTrolly that want to remove
            for (int pos = 0; pos < keranjang.size(); pos++) {
                curr = keranjang.get(pos);
                if (curr.getNama().equals(nama)) {
                    keranjang.remove(curr);
                    break;
                }
            }
            tampilkanKeranjang();
        }
    }//GEN-LAST:event_btnHapusActionPerformed

    private void btnBatalActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBatalActionPerformed
        // TODO add your handling code here:
        //Clear the trolly
        keranjang.clear();
        tampilkanKeranjang();
    }//GEN-LAST:event_btnBatalActionPerformed

    private void tblMenuMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblMenuMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_tblMenuMouseClicked

    private void txtDiskonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtDiskonActionPerformed
        // TODO add your handling code here:


    }//GEN-LAST:event_txtDiskonActionPerformed

    private void txtTunaiActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtTunaiActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtTunaiActionPerformed

    private void txtDiskonFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtDiskonFocusLost
        // TODO add your handling code here:
        diskon = to_int(txtDiskon.getText());
        txtTotal.setText(((total_subtotal) - diskon) + "");
    }//GEN-LAST:event_txtDiskonFocusLost

    private void txtNamaPemesanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtNamaPemesanActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtNamaPemesanActionPerformed

    private void txtTunaiFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtTunaiFocusLost
        // TODO add your handling code here:
        tunai = to_int(txtTunai.getText());
        txtKembali.setText((tunai - (total_subtotal - diskon)) + "");
    }//GEN-LAST:event_txtTunaiFocusLost

    private void btnCetakActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCetakActionPerformed
        // TODO add your handling code here:
        if (!keranjang.isEmpty()) {
            simpan_tr_ke_db();

            /* 
                Hapus seluruh data keranjang
                
             */
            //Clear the trolly
            keranjang.clear();
            tampilkanKeranjang();

            //Kembalikan nilai diskon dan tunai
            txtTunai.setText("0");
            txtDiskon.setText("0");
            JOptionPane.showMessageDialog(this, "Nota berhasil tercetak", "Sukses", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(this, "Keranjang masih kosong", "Warning", JOptionPane.WARNING_MESSAGE);
        }
    }//GEN-LAST:event_btnCetakActionPerformed

    private void txtNamaPemesanFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtNamaPemesanFocusLost
        // TODO add your handling code here:
        namaPemesan = txtNamaPemesan.getText();
    }//GEN-LAST:event_txtNamaPemesanFocusLost

    private void btnPreviewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPreviewActionPerformed
        // TODO add your handling code here:
        if (!keranjang.isEmpty()) {
            ReceiptDetailFrame.MODE = 1; //MODE ORDER 
            //ReceiptDetailFrame.keranjang = keranjang;
            String kasir = LoginController.getInstance().getCurrentUser().getUsername();
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            LocalDateTime now = LocalDateTime.now();
            String datetime = dtf.format(now);

            ReceiptDetailFrame.set_keterangan(idNota, datetime, kasir, txtNamaPemesan.getText(), to_int(txtHargaJual.getText()),
                    to_int(txtDiskon.getText()), to_int(txtTotal.getText()), to_int(txtTunai.getText()), to_int(txtKembali.getText()),keranjang);
            ReceiptDetailFrame receipt = ReceiptDetailFrame.getInstance();

            receipt.setVisible(true);
            receipt.setLocationRelativeTo(null);
        } else {
            JOptionPane.showMessageDialog(this, "Keranjang masih kosong", "Warning", JOptionPane.WARNING_MESSAGE);
        }


    }//GEN-LAST:event_btnPreviewActionPerformed

    private void btnCloseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCloseActionPerformed
        // TODO add your handling code here:
        this.dispose();
        //instance = null;
    }//GEN-LAST:event_btnCloseActionPerformed
    int idNota;

    private void simpan_tr_ke_db() {
        /* Langkah
            1. dapatkan hargaJual/total_subtotal,diskon,total,tunai,datetime,iduser
            2. buat record tb_nota di tb_nota terlebih dahulu
            3. dapatkan id tb_nota yang baru dibuat
            4. lakukan perulangan (dapatkan idproduk,qty,subtotal) untuk membuat record detail ke tb_dt_nota
         */
        namaPemesan = txtNamaPemesan.getText();
        int hargaJual = total_subtotal;
        int total = hargaJual - diskon;
        int idUser = LoginController.getInstance().getCurrentUser().getIdUser();

        //Get Date time
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();
        String datetime = dtf.format(now);
        //System.out.println(dtf.format(now));

        String sql = "INSERT INTO tb_nota(idNota,hargaJual,diskon,total,tunai,tglWaktu,idUser,namaPemesan) "
                + " VALUES(" + idNota + "," + hargaJual + "," + diskon + "," + total + "," + tunai + ",'" + datetime + "'," + idUser + ",\"" + namaPemesan + "\")";
        System.out.println(sql);
        Database.update(sql);

        //jika berhasil mendapatkan idNota
        if (idNota != -1) {
            simpan_tr_dt_nota(idNota);
        }

    }

    private void simpan_tr_dt_nota(int idNota) {
        /* Langkah 
            1. Lakukan perulangan (dapatkan idproduk,qty,subtotal) untuk membuat record detail ke tb_dt_nota
         */
        int i, idProduk, banyak, subTotal;
        String sql = "";
        for (i = 0; i < keranjang.size(); i++) {
            idProduk = keranjang.get(i).getId();
            banyak = keranjang.get(i).getQty();
            subTotal = keranjang.get(i).getSubtotal();
            sql = "INSERT INTO tb_dt_nota(idNota,idProduk,banyak,subTotal) \n"
                    + "VALUES (" + idNota + "," + idProduk + "," + banyak + "," + subTotal + ")";
            Database.update(sql);
        }
    }

    int getIdNota() {
        String sql = "SELECT idNota FROM tb_nota ORDER BY idNota  DESC LIMIT 1 ";
        ResultSet hasil = Database.query(sql);
        try {
//            while(hasil.next()){
//                System.out.println("kamvret");
//            }
            //jika tidak null
//            hasil.next();
//            if(hasil.wasNull()){
//                return hasil.getInt("idNota")+1;
//            }else{
//                return 1;
//            }

            if(!hasil.next()){
                return 1;
            }else{
                
                return hasil.getInt("idNota")+1;
            }
//            if(){
//                
//                
//            }
            
        } catch (SQLException ex) {
            Logger.getLogger(PenjualanFrame.class.getName()).log(Level.SEVERE, null, ex);
        }
        return 1;
    }
    public JInternalFrame getInternalFrame(){
        return PenjualanInternalFrame;
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JInternalFrame PenjualanInternalFrame;
    private javax.swing.JButton btnBatal;
    private javax.swing.JButton btnCetak;
    private javax.swing.JButton btnClose;
    private javax.swing.JButton btnHapus;
    private javax.swing.JButton btnPreview;
    private javax.swing.JButton btnTambah;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTable tblMenu;
    private javax.swing.JTable tblOrder;
    private javax.swing.JTextField txtDiskon;
    private javax.swing.JLabel txtHargaJual;
    private javax.swing.JLabel txtKembali;
    private javax.swing.JTextField txtNamaPemesan;
    private javax.swing.JLabel txtTotal;
    private javax.swing.JTextField txtTunai;
    // End of variables declaration//GEN-END:variables
}
