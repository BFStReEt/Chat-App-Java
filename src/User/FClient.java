/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package User;

import Admin.ClientThread;
import Admin.FServer;
import Admin.MessageStyle;
import Admin.SendFile;
import Data.DBConnection;
import Data.UserAccount;
import java.awt.Color;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.HeadlessException;
import java.awt.event.KeyEvent;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;
import java.net.URL;
import java.sql.Connection;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.SwingWorker;

/**
 *
 * @author Lenovo
 */
public class FClient extends javax.swing.JFrame {

    private UserAccount loggedInUser;
    
    
    Socket socket;
    DataOutputStream dos;
    public boolean attachmentOpen = false;
    private boolean isConnected = false;
    private String mydownloadfolder = "D:\\";
    Container con;
    JComboBox colorList = new JComboBox();
    
    String host = "127.0.0.1";
    int port = Integer.parseInt("8080");
    

    
    public FClient(UserAccount loggedInUser) {
        initComponents();
        Connection connection = DBConnection.getConnection();  
        setLocationRelativeTo(null);
        this.loggedInUser = loggedInUser;
        connect();
    }
    
    public void connect(){
        appendMessage(" Đang kết nối...", "Trạng thái",Color.BLACK, Color.BLACK);
        try {
            socket = new Socket(host, port);
            dos = new DataOutputStream(socket.getOutputStream());
            // gửi username đang kết nối
            dos.writeUTF("CMD_JOIN "+ loggedInUser.getTAIKHOAN());
            appendMessage(" Đã kết nối", "Trạng thái",Color.BLACK, Color.BLACK);
            appendMessage(" Gửi tin nhắn bây giờ!", "Trạng thái",Color.BLACK, Color.BLACK);
            
            // Khởi động Client Thread 
            new Thread(new ClientThread(socket, this)).start();
            btGui.setEnabled(true);
            // đã được kết nối
            isConnected = true;
            
        }
        catch(IOException e) {
            isConnected = false;
            JOptionPane.showMessageDialog(this, "Không thể kết nối đến máy chủ, vui lòng thử lại sau.!","Kết nối thất bại",JOptionPane.ERROR_MESSAGE);
            appendMessage("[IOException]: "+ e.getMessage(), "Lỗi",Color.RED, Color.RED);
        }
    }
    public boolean isConnected(){
        return this.isConnected;
    }

    /*
        Hiển thị Message
    */
    public void appendMessage(String msg, String header, Color headerColor, Color contentColor){
        txtTinNhan.setEditable(true);
        getMsgHeader(header, headerColor);
        getMsgContent(msg, contentColor);
        txtTinNhan.setEditable(false);
    }
    
    /*
        Tin nhắn chat
    */
    public void appendMyMessage(String msg, String header){
        txtTinNhan.setEditable(true);
        getMsgHeader(header, Color.BLACK);
        getMsgContent(msg, Color.BLACK);
        txtTinNhan.setEditable(false);
    }
    
    public void openFolder(){
        JFileChooser chooser = new JFileChooser();
        chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        int open = chooser.showDialog(this, "Mở Thư Mục");
        if(open == chooser.APPROVE_OPTION){
            mydownloadfolder = chooser.getSelectedFile().toString()+"\\";
        } else {
            mydownloadfolder = "D:\\";
        }
    }
    //////////////////////////////////////////////////
    /*
        Tiêu đề tin nhắn
    */
    public void getMsgHeader(String header, Color color){
        int len = txtTinNhan.getDocument().getLength();
        txtTinNhan.setCaretPosition(len);
        //
        txtTinNhan.replaceSelection(header+":");
    }
     
    /*
        Nội dung tin nhắn
    */
    public void getMsgContent(String msg, Color color){
        int len = txtTinNhan.getDocument().getLength();
        txtTinNhan.setCaretPosition(len);
        txtTinNhan.replaceSelection(msg +"\n");
    }

    public void appendOnlineList(Vector list){
        sampleOnlineList(list); 
    }
    
    /*
    /*
        Hiển thị danh sách đang online
    */
    public void showOnLineList(Vector list){
        try {
            txtOnline.setEditable(true);
            txtOnline.setContentType("text/html");
            StringBuilder sb = new StringBuilder();
            Iterator it = list.iterator();
            sb.append("<html><table>");
            
            while(it.hasNext()){
                Object e = it.next();
                URL url = getImageFile();
                Icon icon = new ImageIcon(this.getClass().getResource("/images/online.png"));
                sb.append("<tr><td><b>></b></td><td>").append(e).append("</td></tr>");
                System.out.println("Online: "+ e);
            }
            sb.append("</table></body></html>");
            txtOnline.removeAll();
            txtOnline.setText(sb.toString());
            txtOnline.setEditable(false);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
    
    /*
      ************************************  Hiển thị danh sách online  *********************************************
    */
    private void sampleOnlineList(Vector list){
        txtOnline.setEditable(true);
        txtOnline.removeAll();
        txtOnline.setText("");
        Iterator i = list.iterator();
        while(i.hasNext()){
            Object e = i.next();
            /*  Hiển thị Username Online   */
            JPanel panel = new JPanel();
            panel.setLayout(new FlowLayout(FlowLayout.LEFT));
            panel.setBackground(Color.white);
            
            Icon icon = new ImageIcon(this.getClass().getResource("/Icon/user.png"));
            JLabel label = new JLabel(icon);
            label.setText(" "+ e);
            panel.add(label);
            int len = txtOnline.getDocument().getLength();
            txtOnline.setCaretPosition(len);
            txtOnline.insertComponent(panel);
            /*  Append Next Line   */
            sampleAppend();
        }
        txtOnline.setEditable(false);
    }
    
    private void sampleAppend(){
        int len = txtOnline.getDocument().getLength();
        txtOnline.setCaretPosition(len);
        txtOnline.replaceSelection("\n");
    }
    
    /*
        Lấy file ảnh
    */
    public URL getImageFile(){
        URL url = this.getClass().getResource("/Icon/user.png");
        return url;
    }
    
    public void setMyTitle(String s){
        setTitle(s);
    }
    
    /*
        Phương thức get host
    */
    public String getMyHost(){
        return this.host;
    }
    
    /*
        Phương thức get Port
    */
    public int getMyPort(){
        return this.port;
    }
    
     /*
        Phương thức tải get download
    */
    public String getMyDownloadFolder(){
        return this.mydownloadfolder;
    }
    
    public String getMyUsername(){
        return this.loggedInUser.TAIKHOAN;
    }
    
    /*
        Cập nhật Attachment 
    */
    public void updateAttachment(boolean b){
        this.attachmentOpen = b;
    }
    
    private FClient() {
       
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jMenu1 = new javax.swing.JMenu();
        jMenu2 = new javax.swing.JMenu();
        jMenuItem1 = new javax.swing.JMenuItem();
        jPopupMenu1 = new javax.swing.JPopupMenu();
        jMenuItem2 = new javax.swing.JMenuItem();
        jLabel1 = new javax.swing.JLabel();
        jScrollPane3 = new javax.swing.JScrollPane();
        txtTinNhan = new javax.swing.JTextArea();
        jLabel2 = new javax.swing.JLabel();
        txtTimKiem = new javax.swing.JTextField();
        btDK = new javax.swing.JButton();
        btNhan = new javax.swing.JButton();
        btGui = new javax.swing.JButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        txtNhap = new javax.swing.JTextArea();
        jScrollPane4 = new javax.swing.JScrollPane();
        txtOnline = new javax.swing.JTextPane();
        btGui1 = new javax.swing.JButton();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu6 = new javax.swing.JMenu();
        jMenuItem3 = new javax.swing.JMenuItem();
        jMenuItem4 = new javax.swing.JMenuItem();

        jMenu1.setText("jMenu1");

        jMenu2.setText("jMenu2");

        jMenuItem1.setText("jMenuItem1");

        jMenuItem2.setText("jMenuItem2");

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosed(java.awt.event.WindowEvent evt) {
                formWindowClosed(evt);
            }
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
        });

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        jLabel1.setText("Chat");

        txtTinNhan.setColumns(20);
        txtTinNhan.setFont(new java.awt.Font("Times New Roman", 0, 18)); // NOI18N
        txtTinNhan.setRows(5);
        jScrollPane3.setViewportView(txtTinNhan);

        jLabel2.setFont(new java.awt.Font("Tahoma", 1, 15)); // NOI18N
        jLabel2.setText("Tên tài khoản");

        txtTimKiem.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        txtTimKiem.setText("Tìm kiếm trên chat");
        txtTimKiem.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                txtTimKiemMouseClicked(evt);
            }
        });

        btDK.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icon/attachment.png"))); // NOI18N
        btDK.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btDKActionPerformed(evt);
            }
        });

        btNhan.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icon/downloads (1).png"))); // NOI18N
        btNhan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btNhanActionPerformed(evt);
            }
        });

        btGui.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icon/send (1).png"))); // NOI18N
        btGui.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btGuiActionPerformed(evt);
            }
        });

        txtNhap.setColumns(20);
        txtNhap.setFont(new java.awt.Font("Times New Roman", 0, 18)); // NOI18N
        txtNhap.setRows(5);
        txtNhap.setText("Nhập tin nhắn");
        txtNhap.setMargin(new java.awt.Insets(4, 2, 2, 2));
        txtNhap.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                txtNhapMouseClicked(evt);
            }
        });
        jScrollPane2.setViewportView(txtNhap);

        txtOnline.setFont(new java.awt.Font("Tahoma", 1, 9)); // NOI18N
        txtOnline.setForeground(new java.awt.Color(120, 14, 3));
        txtOnline.setAutoscrolls(false);
        txtOnline.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jScrollPane4.setViewportView(txtOnline);

        btGui1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icon/search (1).png"))); // NOI18N

        jMenuBar1.setForeground(new java.awt.Color(102, 153, 255));

        jMenu6.setText("Chức năng");

        jMenuItem3.setText("Thay đổi thông tin");
        jMenuItem3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem3ActionPerformed(evt);
            }
        });
        jMenu6.add(jMenuItem3);

        jMenuItem4.setText("Đăng xuất");
        jMenuItem4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem4ActionPerformed(evt);
            }
        });
        jMenu6.add(jMenuItem4);

        jMenuBar1.add(jMenu6);

        setJMenuBar(jMenuBar1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel1)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(txtTimKiem, javax.swing.GroupLayout.PREFERRED_SIZE, 168, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btGui1, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jScrollPane4))
                .addGap(7, 7, 7)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(jLabel2)
                        .addGap(209, 209, 209))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 502, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                                .addComponent(btDK, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btNhan, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 341, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(btGui, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(0, 0, Short.MAX_VALUE))))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(38, 38, 38)
                        .addComponent(jLabel2))
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabel1)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(btGui1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(txtTimKiem))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 357, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 348, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(btGui, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(btNhan, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(btDK, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jScrollPane2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void txtTimKiemMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtTimKiemMouseClicked
        // TODO add your handling code here:
        txtTimKiem.setText("");
    }//GEN-LAST:event_txtTimKiemMouseClicked

    private void formWindowClosed(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosed
        // TODO add your handling code here:
        
        FLogin f = new FLogin();
        f.setVisible(true);
        this.dispose();
    }//GEN-LAST:event_formWindowClosed

    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
        // TODO add your handling code here:
        
        FLogin f = new FLogin();
        f.setVisible(true);
        this.dispose();
    }//GEN-LAST:event_formWindowClosing
    
    
    private void btGuiActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btGuiActionPerformed
        // TODO add your handling code here:
        try {
            String content = loggedInUser.TAIKHOAN+" "+ txtNhap.getText();
            dos.writeUTF("CMD_CHATALL "+ content);
            appendMyMessage(" "+txtNhap.getText(), loggedInUser.TAIKHOAN);
            txtNhap.setText("");
        } catch (IOException e) {
            appendMessage(" Không thể gửi tin nhắn đi bây giờ, không thể kết nối đến Máy Chủ tại thời điểm này, xin vui lòng thử lại sau hoặc khởi động lại ứng dụng này.!", "Lỗi", Color.RED, Color.RED);
        }
    }//GEN-LAST:event_btGuiActionPerformed

    private void txtNhapMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtNhapMouseClicked
        // TODO add your handling code here:
        txtNhap.setText("");
    }//GEN-LAST:event_txtNhapMouseClicked

    private void jMenuItem3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem3ActionPerformed
        // TODO add your handling code here:
        FThongTin f = new FThongTin(loggedInUser);
        f.setVisible(true);
    }//GEN-LAST:event_jMenuItem3ActionPerformed

    private void jMenuItem4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem4ActionPerformed
        // TODO add your handling code here:
        FLogin f = new FLogin();
        f.setVisible(true);
        this.dispose();
    }//GEN-LAST:event_jMenuItem4ActionPerformed

    private void btDKActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btDKActionPerformed
        // TODO add your handling code here:
        if(!attachmentOpen){
            SendFile s = new SendFile();
            if(s.prepare(loggedInUser.TAIKHOAN, host, port, this)){
                s.setLocationRelativeTo(null);
                s.setVisible(true);
                attachmentOpen = true;
            } else {
                JOptionPane.showMessageDialog(this, "Không thể thiết lập Chia sẻ File tại thời điểm này, xin vui lòng thử lại sau.!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        }
    }//GEN-LAST:event_btDKActionPerformed

    private void btNhanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btNhanActionPerformed
        // TODO add your handling code here:
        try {
            JFileChooser chooser = new JFileChooser();
            chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
            int browse = chooser.showOpenDialog(this);
            if(browse == chooser.APPROVE_OPTION){
                this.mydownloadfolder = chooser.getSelectedFile().toString() +"\\";
            }
        } catch (HeadlessException e) {
        }
    }//GEN-LAST:event_btNhanActionPerformed

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
            java.util.logging.Logger.getLogger(FClient.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(FClient.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(FClient.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(FClient.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new FClient().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btDK;
    private javax.swing.JButton btGui;
    private javax.swing.JButton btGui1;
    private javax.swing.JButton btNhan;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenu jMenu2;
    private javax.swing.JMenu jMenu6;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JMenuItem jMenuItem2;
    private javax.swing.JMenuItem jMenuItem3;
    private javax.swing.JMenuItem jMenuItem4;
    private javax.swing.JPopupMenu jPopupMenu1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JTextArea txtNhap;
    private javax.swing.JTextPane txtOnline;
    private javax.swing.JTextField txtTimKiem;
    private javax.swing.JTextArea txtTinNhan;
    // End of variables declaration//GEN-END:variables


}
