/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Admin;

import Data.DBConnection;
import Data.UserAccount;
import User.FClient;
import User.FLogin;
import java.awt.Color;
import java.awt.FlowLayout;
import java.io.BufferedReader;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URL;
import java.sql.Connection;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.Vector;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

/**
 *
 * @author Lenovo
 */
public class FServer extends javax.swing.JFrame {
    private UserAccount loggedInUser;
    private ServerSocket serverSocket;
    
    String ip = "127.0.0.1";
    int port = Integer.parseInt("8080");
    
    SimpleDateFormat sdf = new SimpleDateFormat("hh:mm:ss a");
    Thread t;
    ServerThread serverThread;
    /** Chat List  **/
    public Vector socketList = new Vector();
    public Vector clientList = new Vector();
    /** File Sharing List **/
    public Vector clientFileSharingUsername = new Vector();
    public Vector clientFileSharingSocket = new Vector();
    /** Server **/
    ServerSocket server;
    
    Connection connection = DBConnection.getConnection();
  
    public FServer(UserAccount loggedInUser) {
        initComponents();  
        setLocationRelativeTo(null);
        this.loggedInUser = loggedInUser;
        labelXinChao.setText("Xin chào " + loggedInUser.HOTEN);    
    }

    
    private FServer() {
        
    }
    
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
    
    public void appendMessage(String msg){
        Date date = new Date();
        txtServer.append(sdf.format(date) +": "+ msg +"\n");
        txtServer.setCaretPosition(txtServer.getText().length() - 1);
    }
    
    public void setSocketList(Socket socket){
        try {
            socketList.add(socket);
            appendMessage("[setSocketList]: Được thêm");
        } catch (Exception e) { appendMessage("[setSocketList]: "+ e.getMessage()); }
    }
    
    public void setClientList(String client){
        try {
            clientList.add(client);
            appendMessage("[setClientList]: Được thêm");
        } catch (Exception e) { appendMessage("[setClientList]: "+ e.getMessage()); }
    }
    
    public void setClientFileSharingUsername(String user){
        try {
            clientFileSharingUsername.add(user);
        } catch (Exception e) { }
    }
    
    public void setClientFileSharingSocket(Socket soc){
        try {
            clientFileSharingSocket.add(soc);
        } catch (Exception e) { }
    }
    
    public Socket getClientList(String client){
        Socket tsoc = null;
        for(int x=0; x < clientList.size(); x++){
            if(clientList.get(x).equals(client)){
                tsoc = (Socket) socketList.get(x);
                break;
            }
        }
        return tsoc;
    }
    public void removeFromTheList(String client){
        try {
            for(int x=0; x < clientList.size(); x++){
                if(clientList.elementAt(x).equals(client)){
                    clientList.removeElementAt(x);
                    socketList.removeElementAt(x);
                    appendMessage("[Removed]: "+ client);
                    break;
                }
            }
        } catch (Exception e) {
            appendMessage("[RemovedException]: "+ e.getMessage());
        }
    }
    
    public Socket getClientFileSharingSocket(String username){
        Socket tsoc = null;
        for(int x=0; x < clientFileSharingUsername.size(); x++){
            if(clientFileSharingUsername.elementAt(x).equals(username)){
                tsoc = (Socket) clientFileSharingSocket.elementAt(x);
                break;
            }
        }
        return tsoc;
    }
    
    public void removeClientFileSharing(String username){
        for(int x=0; x < clientFileSharingUsername.size(); x++){
            if(clientFileSharingUsername.elementAt(x).equals(username)){
                try {
                    Socket rSock = getClientFileSharingSocket(username);
                    if(rSock != null){
                        rSock.close();
                    }
                    clientFileSharingUsername.removeElementAt(x);
                    clientFileSharingSocket.removeElementAt(x);
                    appendMessage("[FileSharing]: Hủy bỏ "+ username);
                } catch (IOException e) {
                    appendMessage("[FileSharing]: "+ e.getMessage());
                    appendMessage("[FileSharing]: Không thể hủy bỏ "+ username);
                }
                break;
            }
        }
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
        labelXinChao = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        btKhoiDong = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        btBatDau1 = new javax.swing.JButton();
        btBatDau2 = new javax.swing.JButton();
        btDong = new javax.swing.JButton();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jScrollPane3 = new javax.swing.JScrollPane();
        txtServer = new javax.swing.JTextArea();
        jScrollPane4 = new javax.swing.JScrollPane();
        txtOnline = new javax.swing.JTextPane();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu2 = new javax.swing.JMenu();

        jMenu1.setText("jMenu1");

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        labelXinChao.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        labelXinChao.setText("Xin chào");

        jPanel1.setForeground(new java.awt.Color(102, 102, 255));

        btKhoiDong.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        btKhoiDong.setText("Khởi động server");
        btKhoiDong.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btKhoiDongActionPerformed(evt);
            }
        });

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel1.setText("Chức năng");

        btBatDau1.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        btBatDau1.setText("Chặn người dùng");
        btBatDau1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btBatDau1ActionPerformed(evt);
            }
        });

        btBatDau2.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        btBatDau2.setText("Thông báo người dùng");
        btBatDau2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btBatDau2ActionPerformed(evt);
            }
        });

        btDong.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        btDong.setText("Đóng server");
        btDong.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btDongActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btKhoiDong, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addComponent(btBatDau1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btBatDau2, javax.swing.GroupLayout.DEFAULT_SIZE, 227, Short.MAX_VALUE)
                    .addComponent(btDong, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addGap(18, 18, 18)
                .addComponent(btKhoiDong, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btDong, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 15, Short.MAX_VALUE)
                .addComponent(btBatDau1, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(btBatDau2, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(29, 29, 29))
        );

        jLabel3.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel3.setText("Server");

        jLabel4.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel4.setText("Danh sách người dùng");

        txtServer.setColumns(20);
        txtServer.setFont(new java.awt.Font("Times New Roman", 0, 18)); // NOI18N
        txtServer.setRows(5);
        jScrollPane3.setViewportView(txtServer);

        txtOnline.setFont(new java.awt.Font("Tahoma", 1, 9)); // NOI18N
        txtOnline.setForeground(new java.awt.Color(120, 14, 3));
        txtOnline.setAutoscrolls(false);
        txtOnline.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jScrollPane4.setViewportView(txtOnline);

        jMenuBar1.setForeground(new java.awt.Color(153, 153, 255));

        jMenu2.setText("Đăng xuất");
        jMenu2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jMenu2MouseClicked(evt);
            }
        });
        jMenuBar1.add(jMenu2);

        setJMenuBar(jMenuBar1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(labelXinChao)
                    .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel3)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 335, Short.MAX_VALUE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel4)
                    .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 339, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(21, 21, 21)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(labelXinChao)
                    .addComponent(jLabel3)
                    .addComponent(jLabel4))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jScrollPane3)
                    .addComponent(jScrollPane4))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    
    private void btKhoiDongActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btKhoiDongActionPerformed
        serverThread = new ServerThread(port, this);
        t = new Thread(serverThread);
        t.start();

        new Thread(new OnlineListThread(this)).start();

        btKhoiDong.setEnabled(false);
        btDong.setEnabled(true);
    }//GEN-LAST:event_btKhoiDongActionPerformed

    
    
    private void btBatDau1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btBatDau1ActionPerformed
        // TODO add your handling code here:
        FChan f = new FChan(loggedInUser);
        f.setVisible(true);
    }//GEN-LAST:event_btBatDau1ActionPerformed

    private void btBatDau2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btBatDau2ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btBatDau2ActionPerformed

    private void btDongActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btDongActionPerformed
        // TODO add your handling code here:
        int confirm = JOptionPane.showConfirmDialog(null, "Đóng Máy Chủ.?");
        if(confirm == 0){
            serverThread.stop();
        }
    }//GEN-LAST:event_btDongActionPerformed

    private void jMenu2MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jMenu2MouseClicked
        // TODO add your handling code here:
        this.dispose();
        FLogin loginFrame = new FLogin();
        loginFrame.setVisible(true);
        DBConnection.closeConnection(connection);
    }//GEN-LAST:event_jMenu2MouseClicked

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
            java.util.logging.Logger.getLogger(FServer.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(FServer.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(FServer.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(FServer.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new FServer().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btBatDau1;
    private javax.swing.JButton btBatDau2;
    private javax.swing.JButton btDong;
    private javax.swing.JButton btKhoiDong;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenu jMenu2;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JLabel labelXinChao;
    private javax.swing.JTextPane txtOnline;
    private javax.swing.JTextArea txtServer;
    // End of variables declaration//GEN-END:variables

}

