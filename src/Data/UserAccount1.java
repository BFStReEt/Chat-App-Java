/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Data;

/**
 *
 * @author Lenovo
 */
public class UserAccount1 {
    private String taiKhoan;
    private String matKhau;
    private String hoTen;
    private String gmail;

    public UserAccount1(String taiKhoan, String matKhau, String hoTen, String gmail) {
        this.taiKhoan = taiKhoan;
        this.matKhau = matKhau;
        this.hoTen = hoTen;
        this.gmail = gmail;
    }

    // Các phương thức getter và setter cho các thuộc tính
    public String getTaiKhoan() {
        return taiKhoan;
    }

    public void setTaiKhoan(String taiKhoan) {
        this.taiKhoan = taiKhoan;
    }

    public String getMatKhau() {
        return matKhau;
    }

    public void setMatKhau(String matKhau) {
        this.matKhau = matKhau;
    }

    public String getHoTen() {
        return hoTen;
    }

    public void setHoTen(String hoTen) {
        this.hoTen = hoTen;
    }
    
    public String getgmail() {
        return gmail;
    }

    public void setgmail(String gmail) {
        this.gmail = gmail;
    }

}
