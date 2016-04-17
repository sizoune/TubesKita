    /*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Database;

/**
 *
 * @author Fiqih
 */
import Model.*;
import java.sql.Connection;
import java.sql.DriverAction;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

public class Database {

    private Statement stmt = null;
    private Connection con = null;
    private ResultSet rs = null;
    private String dbUser = "root";
    private String password = "";
    //private ArrayList<Akun> listAkun = new ArrayList<Akun>();
    
    public Database() {
        try {
            con = DriverManager.getConnection("jdbc:mysql://localhost:3306/media_sharing", dbUser, password);
            stmt = con.createStatement();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, " " + e.getMessage(), "Connection Error", JOptionPane.WARNING_MESSAGE);
        }
    }
    
    public ResultSet getData(String query) {
        try {
            rs = stmt.executeQuery(query);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, " " + ex.getMessage(), "Can't Get Data", JOptionPane.WARNING_MESSAGE);
        }
        return rs;
    }
    
    public void execute(String query) {
        try {
            stmt.execute(query);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, " " + ex.getMessage(), "Can't Execute Query", JOptionPane.WARNING_MESSAGE);
        }
    }
    
    public ResultSet loadSemuaAkunDB() {
        String query = "Select * from user";
        rs = getData(query);
        return rs;
    }    
    
    public ResultSet loadSemuaMediaDB(int id) {
        String query = "Select * from media where id_user='" + id + "'";
        rs = getData(query);
        return rs;
    }
    
    public ResultSet loadSemuaFriendDB(Akun a) {
        String query = "select id_user,username,first_name,last_name from user where id_user in "
                + "(select id_user from friend where id_kita = "+a.getIdAkun()+")";
        System.out.println(query);
        rs = getData(query);
        return rs;
    }
    
    public ResultSet loadTag (int i,Akun a) {
        String query = "select id_user,username from tag join user using (id_user) where id_media = "+i;
        rs = getData(query);
        return rs;
    }
    
    public void insertFriend (Akun friend, Akun kita) {
        String query = "insert into friend (id_user,id_kita) values ("
                +friend.getIdAkun()+","
                +kita.getIdAkun()+")";
        execute(query);
    }
    
    public void deleteFriend (Akun friend, Akun kita) {
        String query = "delete from friend where id_user = "+friend.getIdAkun()+" and id_kita = "+kita.getIdAkun();
        execute(query);
    }
    
    public void insertAkun(Akun a) {
        String query = "insert into user(username,first_name,last_name,tempat_lahir,tanggal_lahir,email,password) values("
                + "'" + a.getUsername() + "'"
                + ",'" + a.getNamaDepan() + "'"
                + ",'" + a.getNamaBelakang() + "'"
                + ",'" + a.getTempatLahir() + "'"
                + ",'" + a.getTanggalLahir() + "'"
                + ",'" + a.getEmail() + "'"
                + ",'" + a.getPassword() + "')";
        execute(query);
    }
    
    public void insertMedia(Media m, Akun a) {
        String cek;
        if (m instanceof Foto) {
            cek = "foto";
        } else {
            cek = "video";
        }
        String query = "insert into media (id_user,type,name,size) values ("
                + a.getIdAkun() + ",'"
                + cek + "','"
                + m.getNama() + "',"
                + m.getSize() + ")";
        execute(query);
    }
    
    public void insertTag (int a, Akun friend, Akun kita) {
        String query = "insert into tag (id_media,id_user,id_kita) values ("
                +a+","
                +friend.getIdAkun()+","
                +kita.getIdAkun()+")";
        execute(query);
    }
    
    public void deleteMedia (int id) {
        String query = "delete from media where id_media = "+id;
        execute(query);
    }
    
    
    public void updateAkun(Akun a) {
        String query = "update user set "
                + "username ='" + a.getUsername() + "',"
                + "first_name ='" + a.getNamaDepan() + "',"
                + "last_name ='" + a.getNamaBelakang() + "',"
                + "tempat_lahir ='" + a.getTempatLahir() + "',"
                + "tanggal_lahir ='" + a.getTanggalLahir() + "',"
                + "email ='" + a.getEmail() + "',"
                + "password ='" + a.getPassword() + "' "
                + "where id_user =" + a.getIdAkun();
        execute(query);
    }
    
}
