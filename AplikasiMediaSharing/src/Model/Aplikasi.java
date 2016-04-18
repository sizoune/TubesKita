/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;

import java.util.ArrayList;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.*;
import java.sql.ResultSet;
import java.util.Iterator;
import Database.Database;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

/**
 *
 * @author muham
 */
public class Aplikasi {

    private ArrayList<Media> listMedia = new ArrayList<Media>();
    private ArrayList<Akun> listAkun = new ArrayList<Akun>();
    private ArrayList<Akun> listFriend = new ArrayList<Akun>();
    boolean cek = false;
    public String userr;
    public int cF = 0;
    private ResultSet rs = null;
    private Database db = new Database();
    private boolean validLogin = false;

    public Aplikasi() {
        //loadSemuaAkun();
    }

    public void loadSemuaAkun() {
        rs = db.loadSemuaAkunDB();
        try {
            while (rs.next()) {
                Akun akun = new Akun(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5), rs.getString(6), rs.getString(7), rs.getString(8));
                listAkun.add(akun);
            }
            rs.close();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, " " + ex.getMessage(), "Can't Get Data", JOptionPane.WARNING_MESSAGE);
        }
    }

    public void loadSemuaFriendUser(Akun a) {
        rs = db.loadSemuaFriendDB(a);
        try {
            while (rs.next()) {
                Akun akun = new Akun(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4));
                listFriend.add(akun);
            }
            rs.close();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, " " + ex.getMessage(), "Can't Get Data", JOptionPane.WARNING_MESSAGE);
        }
    }

    public void refreshTag(int idmedia, Akun kita) {
        rs = db.loadTag(idmedia, kita);
        try {
            loadSemuaMediaUser(kita.getIdAkun());
            while (rs.next()) {
                for (int i=0; i<listMedia.size();i++) {
                    if (listMedia.get(i).getIdMedia()==idmedia) {
                        listMedia.get(i).tagPerson(new Akun(rs.getInt(1),rs.getString(2)));
                    }
                }
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, " " + ex.getMessage(), "Can't Get Data", JOptionPane.WARNING_MESSAGE);
        }
    }

    public void refreshlistFriend(Akun a) {
        listFriend = new ArrayList<Akun>();
        rs = db.loadSemuaFriendDB(a);
        try {
            while (rs.next()) {
                Akun akun = new Akun(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4));
                listFriend.add(akun);
            }
            rs.close();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, " " + ex.getMessage(), "Can't Get Data", JOptionPane.WARNING_MESSAGE);
        }
    }

    public void refreshListMedia(int id) {
        listMedia = new ArrayList<Media>();
        rs = db.loadSemuaMediaDB(id);
        try {
            while (rs.next()) {
                if (rs.getString(3).equals("foto")) {
                    Media f = new Foto(rs.getInt(1), rs.getString(4), rs.getDouble(5));
                    listMedia.add(f);
                } else if (rs.getString(3).equals("video")) {
                    Media v = new Video(rs.getInt(1), rs.getString(4), rs.getDouble(5));
                    listMedia.add(v);
                }
            }
            rs.close();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, " " + ex.getMessage(), "Can't Get Data", JOptionPane.WARNING_MESSAGE);
        }
    }

    public void loadSemuaMediaUser(int id) {
        rs = db.loadSemuaMediaDB(id);
        try {
            while (rs.next()) {
                if (rs.getString(3).equals("foto")) {
                    Media f = new Foto(rs.getInt(1), rs.getString(4), rs.getDouble(5));
                    listMedia.add(f);
                } else if (rs.getString(3).equals("video")) {
                    Media v = new Video(rs.getInt(1), rs.getString(4), rs.getDouble(5));
                    listMedia.add(v);
                }
            }
            rs.close();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, " " + ex.getMessage(), "Can't Get Data", JOptionPane.WARNING_MESSAGE);
        }
    }

    public boolean login(String username, String password) {
        loadSemuaAkun();
        validLogin = false;
        Akun temp = getAkun(username);
        if (temp != null) {
            if (temp.getPassword().equals(password)) {
                loadSemuaMediaUser(getAkun(username).getIdAkun());
                loadSemuaFriendUser(getAkun(username));
                validLogin = true;
            }
        }
        return validLogin;
    }

    public Foto getMediaFoto(String name) {
        Foto result = new Foto();
        for (Media temp : listMedia) {
            if (temp instanceof Foto) {
                Foto cek = (Foto) temp;
                if (cek.getNama().equals(name)) {
                    result = cek;
                    return result;
                }
            }
        }
        return null;
    }

    public Video getMediaVideo(String name) {
        Video result = new Video();
        for (Media temp : listMedia) {
            if (temp instanceof Video) {
                Video cek = (Video) temp;
                if (cek.getNama().equals(name)) {
                    result = cek;
                    return result;
                }
            }
        }
        return null;
    }
    
    public boolean cekMedia(int i) {
        for (Media tmp : listMedia) {
            if (tmp.getIdMedia()==i) {
                return true;
            }
        }
        return false;
    }
    
    public boolean cariAkun(int i) {
        for (Akun tmp : listAkun) {
            if (tmp.getIdAkun()==i) {
                return true;
            }
        }
        return false;
    }
    
    public boolean cekFriend(int i) {
         for (Akun tmp : listFriend) {
            if (tmp.getIdAkun()==i) {
                return true;
            }
        }
        return false;
    }

    public Akun getAkun(String username) {
        Akun result = new Akun();
        for (Akun temp : listAkun) {
            if (temp.getUsername().equals(username)) {
                result = temp;
                return result;
            }
        }
        return null;
    }
    
    public Akun getAkun1(int id) {
        Akun result = new Akun();
        for (Akun temp : listAkun) {
            if (temp.getIdAkun()==id) {
                result = temp;
                return result;
            }
        }
        return null;
    }

    public String displayMedia(Media a) {
        return "Name : " + a.getNama() + "\n"
                + "Size : " + a.getSize();
    }

    public int getsizeMedia() {
        return listMedia.size();
    }

    public int getsizeFriend() {
        return listFriend.size();
    }

    public int getsizeAkun() {
        return listAkun.size();
    }

    public Media getMedia(int i) {
        return listMedia.get(i);
    }

    public ArrayList<Media> getlistMedia() {
        return listMedia;
    }

    public Akun getAkun(int id) {
        return listAkun.get(id);
    }

    public ArrayList<Akun> getlistAkun() {
        return listAkun;
    }

    public Akun getFriend(int id) {
        return listFriend.get(id);
    }

    public ArrayList<Akun> getlistFriend() {
        return listFriend;
    }

    public boolean cekFriend() {
        return listFriend.isEmpty();
    }

    public void addAkun(Akun a) {
        db.insertAkun(a);
    }

    public void addMedia(Media m, Akun a) {
        db.insertMedia(m, a);
    }

    public void addFriend(Akun friend, Akun kita) {
        db.insertFriend(friend, kita);
    }

    public void addTag(int a, Akun f, Akun k) {
        db.insertTag(a, f, k);
    }

    public void updateAkun(Akun a) {
        db.updateAkun(a);
    }

    public void delMedia(int i) {
        db.deleteMedia(i);
    }

    public void delFriend(Akun friend, Akun kita) {
        db.deleteFriend(friend, kita);
    }
    
    public String convertBulan(int i) {
        String o = "";
        switch (i) {
            case 1:
                o = "Jan";
                break;
            case 2:
                o = "Feb";
                break;
            case 3:
                o = "Mar";
                break;
            case 4:
                o = "Apr";
                break;
            case 5:
                o = "May";
                break;
            case 6:
                o = "Jun";
                break;
            case 7:
                o = "Jul";
                break;
            case 8:
                o = "Aug";
                break;
            case 9:
                o = "Sep";
                break;
            case 10:
                o = "Oct";
                break;
            case 11:
                o = "Nov";
                break;
            case 12:
                o = "Dec";
                break;
        }
        return o;
    }

    /*
     public void loadfile() {
     String workingDir = System.getProperty("user.dir");
     Path path = Paths.get(workingDir+"/akun.ser");//ganti dengan folder project
     if (Files.exists(path)) {
     //System.out.println("data loaded");
     cek = true;
     ArrayList<Akun> obj = loaddata("akun.ser");
     akun =  obj;
     }
     }
    
    
     public void addAkun (String nama, String pass, String username, String tempattanggallahir) {
     boolean ceka = false;
     if (cek) {
     for (Akun p : akun) {
     if (username.equals(p.getUsername())) {
     ceka=true;
     break;
     }
     }
     if (ceka) {
     System.out.println("Username sudah ada di database tolong coba dengan yang lain");
     }
     else {
     akun.add(new Akun(nama,pass,username,tempattanggallahir));
     savedata(akun, "akun.ser");
     }
     }
     else {
     akun.add(new Akun(nama,pass,username,tempattanggallahir));
     savedata(akun, "akun.ser");
     }
     }
    
     public boolean cekAkun (String username) {
     boolean cekk = false;
     for (Akun p : akun) {
     if (username.equals(p.getUsername())) {
     cekk= true;    
     }
     }
     return cekk;
     }
    
     public int sizeAkun () {
     return akun.size();
     }
    
     public Akun getAkun (String user) {
     Akun t = new Akun();
     for (Akun p : akun) {
     if (p.getUsername().equals(user)) {
     t=p;
     break;
     }
     }
     return t;
     }
    
     public Akun getAkun (int i) {
     return akun.get(i);
     }
    
     public void deleteAkun (int i) {
     akun.remove(i);
     }
    
     public void savedata (ArrayList<Akun> a,String name) {
     try {
     FileOutputStream fout = new FileOutputStream(name);
     ObjectOutputStream oout = new ObjectOutputStream(fout);
     oout.writeObject(a);
     oout.close();
     fout.close();
     } catch (Exception e) {
     System.out.println("terjadi exception");
     }
     }
    
     public ArrayList<Akun> loaddata (String nama) {
     ArrayList<Akun> obj = new ArrayList();
     try {
     FileInputStream fin = new FileInputStream(nama);
     ObjectInputStream oin = new ObjectInputStream(fin);
     obj= (ArrayList<Akun>)oin.readObject();
     oin.close();
     fin.close();
     } catch (Exception e) {
     System.out.println("terjadi exception");
     }
     return obj;
     }
    
     public boolean doLogin (String user,String pass) {
     Akun s = getAkun(user);
     boolean cekk = false;
     if (s.getPass().equals(pass)) {
     //System.out.println("masuk");
     cekk = true;
     }
     return cekk;
     }
    
     public void edProfile (String nama, String pass, String username, String tempattanggallahir) {
     getAkun(userr).setNama(nama);
     getAkun(userr).setTempattanggallahir(tempattanggallahir);
     getAkun(userr).setUsername(username);
     userr=username;
     getAkun(userr).setPass(pass);
     savedata(akun, "akun.ser");
     }
    
     public void addFriend(int s) {
     getAkun(userr).followFriend(getAkun(s-1));
     }
    
     public void removeFriend (int s) {
     getAkun(userr).removePerson(s-1);
     }
    
     public void lihatTeman() {
     System.out.println("==========teman anda sekarang============");
     //getAkun(userr).loadFriend();
     if (!getAkun(userr).cekFriend()) {
     for (int i=0;i<getAkun(userr).sizeFriend();i++) {
     System.out.println((i+1)+" . "+getAkun(userr).getPerson(i).getNama());
     cF++;
     }
     }
     }
    
     public void lihatFriend() {
     boolean cekf;
     System.out.println("==========teman anda sekarang============");
     //getAkun(userr).loadFriend();
     if (!getAkun(userr).cekFriend()) {
     for (int i=0;i<getAkun(userr).sizeFriend();i++) {
     System.out.println((i+1)+" . "+getAkun(userr).getPerson(i).getNama());
     }
     }
     else {
     System.out.println("belum ada teman");
     }
     System.out.println("==== akun yang bisa di tambahkan menjadi teman");
     for (int y = 0;y<akun.size();y++) {
     cekf = false;
     if (!getAkun(userr).cekFriend()) {
     for (int i=0;i<getAkun(userr).sizeFriend();i++) {
     if (getAkun(y).getUsername().equals(getAkun(userr).getPerson(i).getUsername())) {
     cekf=true;
     cF++;
     }
     }
     if (cekf==false) {
     if (getAkun(userr).getUsername() != getAkun(y).getUsername()) {
     System.out.println((y+1)+" . "+getAkun(y).getNama());
     }
     }
     }
     else {
     if (getAkun(userr).getUsername() != getAkun(y).getUsername()) {
     System.out.println((y+1)+" . "+getAkun(y).getNama());
     }
     }
     }
     }
    
     public void removeTag (int s,int y) {
     getAkun(userr).getMedia(s-1).removeTag(y-1);
     }
    
     public void lihattagFoto(int i) {
     if (!getAkun(userr).cekMedia()) {
     System.out.println("Foto");
     if (getAkun(userr).getMedia(i-1) instanceof Foto) {
     if (!getAkun(userr).getMedia(i-1).cekTag()) {
     for (int x=0;x<getAkun(userr).getMedia(i-1).sizeTag();x++) {
     System.out.println((x+1)+". "+getAkun(userr).getMedia(i-1).getPersonTag(x).getNama());
     }
     }
     else {
     System.out.println("-");
     }
     System.out.println("");
     }
     }
     }
    
     public void taggedMedia(int i,int y) {
     getAkun(userr).getMedia(i-1).tagPerson(getAkun(userr).getPerson(y-1));
     }
    
     public void timeline() {
     System.out.println("Nama : "+getAkun(userr).getNama());
     if (!getAkun(userr).cekMedia()) {
     System.out.println("Foto");
     for (int i=0;i<getAkun(userr).sizeMedia();i++) {
     if (getAkun(userr).getMedia(i) instanceof Foto) {
     Foto f = (Foto) getAkun(userr).getMedia(i);
     System.out.println("Nama Foto : "+f.getNama());
     System.out.println("Size Foto : "+getAkun(userr).getMedia(i).getSize());
     System.out.print("tagged person : ");
     if (!getAkun(userr).getMedia(i).cekTag()) {
     for (int x=0;x<getAkun(userr).getMedia(i).sizeTag();x++) {
     System.out.print(getAkun(userr).getMedia(i).getPersonTag(x).getNama()+" ");
     }
     }
     else {
     System.out.print("-");
     }
     System.out.println("");
     }
     }
     System.out.println("Video");
     for (int i=0;i<getAkun(userr).sizeMedia();i++) {
     if (getAkun(userr).getMedia(i) instanceof Video) {
     Video f = (Video) getAkun(userr).getMedia(i);
     System.out.println("Nama Video : "+f.getNama());
     System.out.println("Size Video : "+getAkun(userr).getMedia(i).getSize());
     System.out.print("tagged person : ");
     if (!getAkun(userr).getMedia(i).cekTag()) {
     for (int x=0;x<getAkun(userr).getMedia(i).sizeTag();x++) {
     System.out.print(getAkun(userr).getMedia(i).getPersonTag(x).getNama()+" ");
     }
     }
     else {
     System.out.print("-");
     }
     System.out.println("");
     }
     }
     }
     System.out.println("");
     //getAkun(userr).loadFriend();
     if (!getAkun(userr).cekFriend()) {
     for (int i=0;i<getAkun(userr).sizeFriend();i++) {
     System.out.println("Nama : "+getAkun(userr).getPerson(i).getNama());
     if (!getAkun(userr).getPerson(i).cekMedia()) {
     System.out.println("Foto");
     for (int y=0;y<getAkun(userr).getPerson(i).sizeMedia();i++) {
     if (getAkun(userr).getPerson(i).getMedia(y) instanceof Foto) {
     Foto f = (Foto) getAkun(userr).getPerson(i).getMedia(y);
     System.out.println("Nama Foto : "+f.getNama());
     System.out.println("Size Foto : "+getAkun(userr).getPerson(i).getMedia(y).getSize());
     System.out.print("tagged person : ");
     if (!getAkun(userr).getMedia(i).cekTag()) {
     for (int x=0;x<getAkun(userr).getPerson(i).getMedia(y).sizeTag();x++) {
     System.out.print(getAkun(userr).getPerson(i).getMedia(y).getPersonTag(x).getNama()+" ");
     }
     }
     else {
     System.out.print("-");
     }
     System.out.println("");
     }
     }
     System.out.println("Video");
     for (int y=0;y<getAkun(userr).getPerson(i).sizeMedia();i++) {
     if (getAkun(userr).getPerson(i).getMedia(y) instanceof Video) {
     Video f = (Video) getAkun(userr).getPerson(i).getMedia(y);
     System.out.println("Nama Video : "+f.getNama());
     System.out.println("Size Video : "+getAkun(userr).getPerson(i).getMedia(y).getSize());
     System.out.print("tagged person : ");
     if (!getAkun(userr).getMedia(i).cekTag()) {
     for (int x=0;x<getAkun(userr).getPerson(i).getMedia(y).sizeTag();x++) {
     System.out.print(getAkun(userr).getPerson(i).getMedia(y).getPersonTag(x).getNama()+" ");
     }
     }
     else {
     System.out.print("-");
     }
     }
     }
     }
     }
     }
        
     }
    
     public void menuDua() {
     System.out.println("Nama : "+getAkun(userr).getNama());
     System.out.println("Tempat,Tanggal Lahir : "+getAkun(userr).getTempattanggallahir());
     System.out.println("Username : "+getAkun(userr).getUsername());
     System.out.println("Password : "+getAkun(userr).getPass());
     }
    
     public void addFoto (String nama, double size) {
     getAkun(userr).createMediaFoto(size, nama);
     }
    
     public void addVideo (String nama,double size) {
     getAkun(userr).createMediaVideo(size, nama);
     }
    
     public void lihatFoto () {
     if (!getAkun(userr).cekMedia()) {
     for (int i=0;i<getAkun(userr).sizeMedia();i++) {
     if (getAkun(userr).getMedia(i) instanceof Foto) {
     Foto f = (Foto) getAkun(userr).getMedia(i);
     System.out.println((i+1)+". Nama : "+f.getNama());
     System.out.println("   Size : "+getAkun(userr).getMedia(i).getSize());
     }
     }
     }
     }
    
     public void lihatVideo () {
     if (!getAkun(userr).cekMedia()) {
     for (int i=0;i<getAkun(userr).sizeMedia();i++) {
     if (getAkun(userr).getMedia(i) instanceof Video) {
     Video f = (Video) getAkun(userr).getMedia(i);
     System.out.println((i+1)+". Nama : "+f.getNama());
     System.out.println("   Size : "+getAkun(userr).getMedia(i).getSize());
     }
     }
     }
     }
    
     public void edFoto(int i,String nama,double size) {
     if (!getAkun(userr).cekMedia()) {
     getAkun(userr).editFoto(i-1, nama, size); 
     }        
     }
    
     public void edVideo(int i,String nama,double size) {
     if (!getAkun(userr).cekMedia()) {
     getAkun(userr).editVideo(i-1, nama, size); 
     }        
     }
    
     public void deleteFotodanVideo (int i) {
     if (!getAkun(userr).cekMedia()) {
     getAkun(userr).removeMedia(i-1);
     }  
     }
     */
}
