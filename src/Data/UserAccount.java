/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Data;

public class UserAccount {
    public int ID;
    public String TAIKHOAN;
    public String MATKHAU;
    public String HOTEN;
    public String GMAIL;
    public boolean XACNHAN;
    public boolean QUYEN;

    public UserAccount(String ID, String TAIKHOAN, String MATKHAU, String HOTEN, String GMAIL, boolean XACNHAN, boolean QUYEN) {
       this.ID = Integer.parseInt(ID);
       this.TAIKHOAN = TAIKHOAN;
       this.MATKHAU = MATKHAU;
       this.HOTEN = HOTEN;
       this.GMAIL = GMAIL;
       this.XACNHAN = XACNHAN;
       this.QUYEN = QUYEN;
   }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getTAIKHOAN() {
        return TAIKHOAN;
    }

    public void setTAIKHOAN(String TAIKHOAN) {
        this.TAIKHOAN = TAIKHOAN;
    }

    public String getMATKHAU() {
        return MATKHAU;
    }

    public void setMATKHAU(String MATKHAU) {
        this.MATKHAU = MATKHAU;
    }

    public String getHOTEN() {
        return HOTEN;
    }

    public void setHOTEN(String HOTEN) {
        this.HOTEN = HOTEN;
    }

    public String getGMAIL() {
        return GMAIL;
    }

    public void setGMAIL(String GMAIL) {
        this.GMAIL = GMAIL;
    }

    public boolean isXACNHAN() {
        return XACNHAN;
    }

    public void setXACNHAN(boolean XACNHAN) {
        this.XACNHAN = XACNHAN;
    }

    public boolean isQUYEN() {
        return QUYEN;
    }

    public void setQUYEN(boolean QUYEN) {
        this.QUYEN = QUYEN;
    }
}

