/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package User;

import User.FLogin;
import Data.DBConnection;
import Data.UserAccount1;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.swing.JOptionPane;


public class FXacNhan extends javax.swing.JFrame {
     
    private UserAccount1 useraccount;
    private Date expirationTime;
    private String sentCode;
    
    public FXacNhan(UserAccount1 useraccount) {
        initComponents();
        setLocationRelativeTo(null);
        this.useraccount = useraccount;
        
        expirationTime = new Date(System.currentTimeMillis() + 10 * 60 * 1000);
        sentCode = generateRandomCode();
        String email = useraccount.getgmail();
        String messageContent = "Mã xác nhận của bạn: " + sentCode;
        sendEmail(email, messageContent);
    }

    
    public static String generateRandomCode() {
        int min = 1000; 
        int max = 9999; 

        Random rand = new Random();
        int randomCode = rand.nextInt((max - min) + 1) + min;

        return String.valueOf(randomCode);
    }

    private FXacNhan() {
        
    }

    
    private boolean isCodeExpired() {
        java.util.Date currentTime = new java.util.Date();
        java.sql.Date currentSqlDate = new java.sql.Date(currentTime.getTime());
        return currentSqlDate.after(expirationTime);
    }
    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        btSubmit = new javax.swing.JButton();
        txtCode = new javax.swing.JTextField();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jLabel2.setFont(new java.awt.Font("Segoe UI", 1, 36)); // NOI18N

        jLabel3.setFont(new java.awt.Font("Segoe UI", 1, 36)); // NOI18N
        jLabel3.setText("CONFIRM");

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabel1.setText("Nhập 4 chữ số gửi về gmail của bạn để xác nhận");

        btSubmit.setBackground(new java.awt.Color(51, 102, 255));
        btSubmit.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        btSubmit.setForeground(new java.awt.Color(255, 255, 255));
        btSubmit.setText("Submit");
        btSubmit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btSubmitActionPerformed(evt);
            }
        });

        txtCode.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        txtCode.setHorizontalAlignment(javax.swing.JTextField.CENTER);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                            .addGap(165, 165, 165)
                            .addComponent(jLabel3)
                            .addGap(176, 176, 176)
                            .addComponent(jLabel2))
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                            .addContainerGap()
                            .addComponent(jLabel1)
                            .addGap(24, 24, 24)))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(195, 195, 195)
                        .addComponent(btSubmit, javax.swing.GroupLayout.PREFERRED_SIZE, 97, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(177, 177, 177)
                        .addComponent(txtCode, javax.swing.GroupLayout.PREFERRED_SIZE, 136, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(24, 24, 24)
                        .addComponent(jLabel2))
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabel3)))
                .addGap(23, 23, 23)
                .addComponent(jLabel1)
                .addGap(18, 18, 18)
                .addComponent(txtCode, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btSubmit, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btSubmitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btSubmitActionPerformed
        // TODO add your handling code here:
        String enterCode = txtCode.getText();
        if (isCodeExpired()) {
            JOptionPane.showMessageDialog(this, "Mã xác nhận đã hết hạn. Vui lòng yêu cầu mã mới.", "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
        else{
            if(enterCode.equals(sentCode)){
                String TaiKhoan = useraccount.getTaiKhoan();
                String MatKhau = useraccount.getMatKhau();
                String Hoten = useraccount.getHoTen();
                String Gmail = useraccount.getgmail();
                boolean XacNhan = false;
                if (insertTaiKhoanToDatabase(TaiKhoan, MatKhau, Hoten, Gmail,XacNhan)) {
                    JOptionPane.showMessageDialog(this, "Đăng ký thành công.");
                    FLogin f = new FLogin();
                    f.setVisible(true);
                    this.dispose();
                }
            }
            else{
                JOptionPane.showMessageDialog(this, "Mã xác nhận sai hãy thử lại !");
            }
        }
    }//GEN-LAST:event_btSubmitActionPerformed

    private boolean insertTaiKhoanToDatabase(String TaiKhoan, String MatKhau, String Hoten, String Gmail,boolean XacNhan) {
        try {
            Connection connection = DBConnection.getConnection();
            String hashedPassword = hashPassword(MatKhau);
            String insertQuery = "INSERT INTO TaiKhoan (TAIKHOAN, MATKHAU, HOTEN, GMAIL,XACNHAN) VALUES (?, ?, ?, ?,1)";
            try (PreparedStatement insertStatement = connection.prepareStatement(insertQuery)) {
                insertStatement.setString(1, TaiKhoan);
                insertStatement.setString(2, hashedPassword);
                insertStatement.setString(3, Hoten);
                insertStatement.setString(4, Gmail);
                int rowsInserted = insertStatement.executeUpdate();
                return rowsInserted > 0; 
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false; 
        }
    }
    
    // Hàm để mã hóa mật khẩu bằng SHA-256
    private String hashPassword(String password) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] encodedHash = digest.digest(password.getBytes());
            StringBuilder hexString = new StringBuilder(2 * encodedHash.length);
            for (byte b : encodedHash) {
                String hex = Integer.toHexString(0xFF & b);
                if (hex.length() == 1) {
                    hexString.append('0');
                }
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException ex) {
            throw new RuntimeException(ex);
        }
    }
    
    public void sendEmail(String recipient, String messageContent) {
        String host = "smtp.gmail.com"; 
        String port = "587"; 
        String username = "toinguyen24102000@gmail.com"; 
        String password = "ciqf iiqu zyhp pfdy";

        Properties properties = new Properties();
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");
        properties.put("mail.smtp.host", host);
        properties.put("mail.smtp.port", port);

        // Tạo một phiên làm việc
        Session session = Session.getInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });

        try {
            // Tạo email
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(username));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipient));
            message.setSubject("Mã xác nhận tài khoản:");
            message.setText(messageContent);

            // Gửi email
            Transport.send(message);
            System.out.println("Đã gửi mail !!");
        } catch (MessagingException e) {
            e.printStackTrace();
            System.out.println("Lỗi khi gửi email: " + e.getMessage());
        }
    }
    
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
            java.util.logging.Logger.getLogger(FXacNhan.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(FXacNhan.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(FXacNhan.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(FXacNhan.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new FXacNhan().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btSubmit;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JTextField txtCode;
    // End of variables declaration//GEN-END:variables
}
