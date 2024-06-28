package Barang;

import java.awt.event.*;
import java.sql.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class FormBarang extends JFrame {
    private String[] judul = {"Kode Barang", "Nama Barang", "Harga Barang", "Stok Barang"};
    DefaultTableModel df;
    JTable tab = new JTable();
    JScrollPane scp = new JScrollPane(tab);
    JPanel pnl = new JPanel();
    JLabel lblnama_barang = new JLabel("Nama Barang");
    JTextField txnama_barang = new JTextField(20);
    JLabel lblkode_barang = new JLabel("Kode Barang");
    JTextField txkode_barang = new JTextField(10);
    JLabel lblharga_barang = new JLabel("Harga Barang");
    JTextField txharga_barang = new JTextField(10);
    JLabel lblstok_barang = new JLabel("Stok Barang");
    JTextField txstok_barang = new JTextField(10);

    JButton btadd = new JButton("Simpan");
    JButton btnew = new JButton("Baru");
    JButton btdel = new JButton("Hapus");
    JButton btedit = new JButton("Ubah");

    FormBarang() {
        super("Data Barang");
        setSize(460, 300);
        pnl.setLayout(null);

        pnl.add(lblkode_barang);
        lblkode_barang.setBounds(20, 10, 80, 20);
        pnl.add(txkode_barang);
        txkode_barang.setBounds(105, 10, 100, 20);

        pnl.add(lblnama_barang);
        lblnama_barang.setBounds(20, 33, 80, 20);
        pnl.add(txnama_barang);
        txnama_barang.setBounds(105, 33, 175, 20);

        pnl.add(lblharga_barang);
        lblharga_barang.setBounds(20, 56, 80, 20);
        pnl.add(txharga_barang);
        txharga_barang.setBounds(105, 56, 175, 20);

        pnl.add(lblstok_barang);
        lblstok_barang.setBounds(20, 79, 80, 20);
        pnl.add(txstok_barang);
        txstok_barang.setBounds(105, 79, 175, 20);

        pnl.add(btnew);
        btnew.setBounds(300, 10, 125, 20);
        btnew.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                btnewAksi(e);
            }
        });

        pnl.add(btadd);
        btadd.setBounds(300, 33, 125, 20);
        btadd.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                btaddAksi(e);
            }
        });

        pnl.add(btedit);
        btedit.setBounds(300, 56, 125, 20);
        btedit.setEnabled(false);
        btedit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                bteditAksi(e);
            }
        });

        pnl.add(btdel);
        btdel.setBounds(300, 79, 125, 20);
        btdel.setEnabled(false);
        btdel.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                btdelAksi(e);
            }
        });

        df = new DefaultTableModel(null, judul);
        tab.setModel(df);
        scp.setBounds(20, 110, 405, 150);
        pnl.add(scp);

        tab.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent evt) {
                tabMouseClicked(evt);
            }
        });

        getContentPane().add(pnl);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
        loadData();
    }

    void loadData() {
        try {
            Connection cn = new ConnectDB().getConnect();
            Statement st = cn.createStatement();
            String sql = "SELECT * FROM tbl_barang";
            ResultSet rs = st.executeQuery(sql);
            clearTable();
            while (rs.next()) {
                String kode_barang = rs.getString("kode_barang");
                String nama_barang = rs.getString("nama_barang");
                String harga_barang = rs.getString("harga_barang");
                String stok_barang = rs.getString("stok_barang");
                String[] data = {kode_barang, nama_barang, harga_barang, stok_barang};
                df.addRow(data);
            }
            rs.close();
            cn.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    void clearTable() {
        int numRow = df.getRowCount();
        for (int i = 0; i < numRow; i++) {
            df.removeRow(0);
        }
    }

    void clearTextField() {
        txkode_barang.setText(null);
        txnama_barang.setText(null);
        txharga_barang.setText(null);
        txstok_barang.setText(null);
    }

    void simpanData(Barang m) {
        try {
            Connection cn = new ConnectDB().getConnect();
            String sql = "INSERT INTO tbl_barang (kode_barang, nama_barang, harga_barang, stok_barang) " +
                    "VALUES (?, ?, ?, ?)";
            PreparedStatement pst = cn.prepareStatement(sql);
            pst.setString(1, m.getkode_barang());
            pst.setString(2, m.getnama_barang());
            pst.setString(3, m.getharga_barang());
            pst.setString(4, m.getstok_barang());
            pst.executeUpdate();
            cn.close();
            JOptionPane.showMessageDialog(null, "Data Berhasil Disimpan", "Info Proses", JOptionPane.INFORMATION_MESSAGE);
            loadData();
            clearTextField();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    void hapusData(String kode_barang) {
        try {
            Connection cn = new ConnectDB().getConnect();
            String sql = "DELETE FROM tbl_barang WHERE kode_barang = ?";
            PreparedStatement pst = cn.prepareStatement(sql);
            pst.setString(1, kode_barang);
            pst.executeUpdate();
            cn.close();
            JOptionPane.showMessageDialog(null, "Data Berhasil Dihapus", "Info Proses", JOptionPane.INFORMATION_MESSAGE);
            loadData();
            clearTextField();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    void ubahData(Barang m, String kode_barang) {
        try {
            Connection cn = new ConnectDB().getConnect();
            String sql = "UPDATE tbl_barang SET nama_barang = ?, harga_barang = ?, stok_barang = ? WHERE kode_barang = ?";
            PreparedStatement pst = cn.prepareStatement(sql);
            pst.setString(1, m.getnama_barang());
            pst.setString(2, m.getharga_barang());
            pst.setString(3, m.getstok_barang());
            pst.setString(4, kode_barang);
            pst.executeUpdate();
            cn.close();
            JOptionPane.showMessageDialog(null, "Data Berhasil Diubah", "Info Proses", JOptionPane.INFORMATION_MESSAGE);
            loadData();
            clearTextField();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    private void btnewAksi(ActionEvent evt) {
        clearTextField();
        btedit.setEnabled(false);
        btdel.setEnabled(false);
        btadd.setEnabled(true);
    }

    private void btaddAksi(ActionEvent evt) {
        Barang m = new Barang();
        m.setkode_barang(txkode_barang.getText());
        m.setnama_barang(txnama_barang.getText());
        m.setharga_barang(txharga_barang.getText());
        m.setstok_barang(txstok_barang.getText());
        simpanData(m);
    }

    private void btdelAksi(ActionEvent evt) {
        int status = JOptionPane.showConfirmDialog(null, "Yakin data akan dihapus?", "Konfirmasi", JOptionPane.OK_CANCEL_OPTION);
        if (status == JOptionPane.OK_OPTION) {
            hapusData(txkode_barang.getText());
        }
    }

    private void bteditAksi(ActionEvent evt) {
        Barang m = new Barang();
        m.setkode_barang(txkode_barang.getText());
        m.setnama_barang(txnama_barang.getText());
        m.setharga_barang(txharga_barang.getText());
        m.setstok_barang(txstok_barang.getText());
        ubahData(m, txkode_barang.getText());
    }

    private void tabMouseClicked(MouseEvent evt) {
        int row = tab.getSelectedRow();
        if (row != -1) {
            Object kodeBarang = df.getValueAt(row, 0);
            Object namaBarang = df.getValueAt(row, 1);
            Object hargaBarang = df.getValueAt(row, 2);
            Object stokBarang = df.getValueAt(row, 3);

            txkode_barang.setText(kodeBarang != null ? kodeBarang.toString() : "");
            txnama_barang.setText(namaBarang != null ? namaBarang.toString() : "");
            txharga_barang.setText(hargaBarang != null ? hargaBarang.toString() : "");
            txstok_barang.setText(stokBarang != null ? stokBarang.toString() : "");

            btadd.setEnabled(false);
            btedit.setEnabled(true);
            btdel.setEnabled(true);
        }
    }

    public static void main(String[] args) {
        new FormBarang();
    }
}

class Barang {
    private String kode_barang;
    private String nama_barang;
    private String harga_barang;
    private String stok_barang;

    public String getkode_barang() {
        return kode_barang;
    }

    public void setkode_barang(String kode_barang) {
        this.kode_barang = kode_barang;
    }

    public String getnama_barang() {
        return nama_barang;
    }

    public void setnama_barang(String nama_barang) {
        this.nama_barang = nama_barang;
    }

    public String getharga_barang() {
        return harga_barang;
    }

    public void setharga_barang(String harga_barang) {
        this.harga_barang = harga_barang;
    }

    public String getstok_barang() {
        return stok_barang;
    }

    public void setstok_barang(String stok_barang) {
        this.stok_barang = stok_barang;
    }
}

class ConnectDB {
    public Connection getConnect() {
        try {
            String url = "jdbc:mysql://localhost:3306/toko";
            String user = "root";
            String password = "";
            return DriverManager.getConnection(url, user, password);
        } catch (SQLException ex) {
            ex.printStackTrace();
            return null;
        }
    }
}