/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package view;

import java.util.*;
import Model.*;

/**
 *
 * @author muham
 */
public class ViewConsole {

    private Aplikasi model;
    private Scanner sInt, sStr;
    private String uSession;
    boolean ceklo = false;
    ArrayList<Integer> tmp = new ArrayList<>();

    public ViewConsole(Aplikasi model) {
        this.model = model;
        sInt = new Scanner(System.in);
        sStr = new Scanner(System.in);
    }

    public int inputInt() {
        try {
            return sInt.nextInt();
        } catch (InputMismatchException ie) {
            throw new InputMismatchException("tolong masukkan angka");
        } finally {
            sInt = new Scanner(System.in);
        }
    }

    public void loginApp() {
        String pass;
        boolean sudahLog = false;
        do {
            System.out.println("Login App Media Sharing");
            System.out.print("Username : ");
            uSession = sStr.nextLine();
            System.out.print("Password : ");
            pass = sStr.nextLine();
            if (model.login(uSession, pass)) {
                sudahLog = true;
                ceklo = true;
            } else {
                System.out.println("Username / password salah !");
                System.out.println("");
            }
        } while (sudahLog == false);
    }

    public void menuUtama() {
        System.out.println("Aplikasi Media Sharing");
        System.out.println("1. Profile");
        System.out.println("2. Media");
        System.out.println("3. Friend");
        System.out.println("4. Tag Media (in development)");
        System.out.println("0. Exit");
        //System.out.println("");
    }

    public void lihatMedia() {
        model.refreshListMedia(model.getAkun(uSession).getIdAkun());
        System.out.println("Foto");
        for (int i = 0; i < model.getsizeMedia(); i++) {
            if (model.getMedia(i) instanceof Foto) {
                System.out.println("Id : " + model.getMedia(i).getIdMedia());
                System.out.println("Nama : " + model.getMedia(i).getNama());
                System.out.println("Size : " + model.getMedia(i).getSize());
            }
        }
        System.out.println("=====================================");
        System.out.println("Video");
        for (int i = 0; i < model.getsizeMedia(); i++) {
            if (model.getMedia(i) instanceof Video) {
                System.out.println("Id : " + model.getMedia(i).getIdMedia());
                System.out.println("Nama : " + model.getMedia(i).getNama());
                System.out.println("Size : " + model.getMedia(i).getSize());
            }
        }
        System.out.println("");
    }

    public void menuMedia() {
        int pil, pil1, size;
        String nama;
        System.out.println("1. Add Media");
        System.out.println("2. Delete Media");
        System.out.print("Pilihan : ");
        pil = inputInt();
        switch (pil) {
            case 1:
                System.out.println("1. Foto");
                System.out.println("2. Video");
                System.out.print("Pilihan : ");
                pil1 = inputInt();
                switch (pil1) {
                    case 1:
                        System.out.print("Nama : ");
                        nama = sStr.nextLine();
                        System.out.print("Size : ");
                        size = inputInt();
                        model.addMedia(new Foto(nama, size), model.getAkun(uSession));
                        System.out.println("Photo Added");
                        break;
                    case 2:
                        System.out.print("Nama : ");
                        nama = sStr.nextLine();
                        System.out.print("Size : ");
                        size = inputInt();
                        model.addMedia(new Video(nama, size), model.getAkun(uSession));
                        System.out.println("Video Added");
                        break;
                    default:
                        System.out.println("tidak ada di pilihan");
                }
                break;
            case 2:
                lihatMedia();
                System.out.print("Pilih ID Media yang ingin di hapus : ");
                pil1 = inputInt();
                if (model.cekMedia(pil1)) {
                    model.delMedia(pil1);
                } else {
                    System.out.println("ID Media tidak valid");
                }
                break;
            default:
                System.out.println("tidak ada dipilihan");
        }
    }

    public void lihatteman() {
        model.refreshlistFriend(model.getAkun(uSession));
        if (!model.cekFriend()) {
            for (int i = 0; i < model.getsizeFriend(); i++) {
                System.out.println("Id : " + model.getFriend(i).getIdAkun());
                System.out.println("username : " + model.getFriend(i).getUsername());
            }
        } else {
            System.out.println("anda belum mempunyai teman");
        }
    }

    public void refreshList() {
        for (int i = 0; i < tmp.size(); i++) {
            tmp.remove(i);
        }
    }

    public void lihatakunygblmjaditeman() {
        boolean cekf = false;
        refreshList();
        model.refreshlistFriend(model.getAkun(uSession));
        if (!model.cekFriend()) {
            for (int i = 0; i < model.getsizeAkun(); i++) {
                for (int x = 0; x < model.getsizeFriend(); x++) {
                    if (model.getFriend(x).getIdAkun() == model.getAkun(i).getIdAkun()) {
                        cekf = true;
                    }
                }
                //System.out.println(aplikasi.getAkun(i).getUsername());
                if (cekf == false) {
                    if (!model.getAkun(i).getUsername().equals(uSession)) {
                        tmp.add(model.getAkun(i).getIdAkun());
                        System.out.println("Id : " + model.getAkun(i).getIdAkun());
                        System.out.println("username : " + model.getAkun(i).getUsername());
                    }
                }
                cekf = false;
            }
        } else {
            for (int i = 0; i < model.getsizeAkun(); i++) {
                if (!model.getAkun(i).getUsername().equals(uSession)) {
                    tmp.add(model.getAkun(i).getIdAkun());
                    System.out.println("Id : " + model.getAkun(uSession).getIdAkun());
                    System.out.println("username : " + model.getAkun(uSession).getUsername());
                }
            }
        }
        System.out.println("======================");
    }

    public boolean cekFriend(int x) {
        if (!tmp.isEmpty()) {
            for (int t : tmp) {
                if (t == x) {
                    return true;
                }
            }
            return false;
        } else {
            return false;
        }
    }

    public void menuFriend() {
        int pil, pil1;
        System.out.println("1. Add Friend");
        System.out.println("2. Delete Friend");
        System.out.print("Pilihan : ");
        pil = inputInt();
        switch (pil) {
            case 1:
                System.out.println("Akun yang bisa di tambahkan menjadi teman");
                lihatakunygblmjaditeman();
                System.out.print("Masukkan ID yang ingin di tambahkan menjadi teman anda : ");
                pil1 = inputInt();
                if (cekFriend(pil1)) {
                    model.addFriend(model.getAkun1(pil1), model.getAkun(uSession));
                    System.out.println("Friend Added!");
                } else {
                    System.out.println("ID Tidak ada dalam daftar !");
                }
                break;
            case 2:
                System.out.println("Friend list");
                lihatteman();
                System.out.print("Masukkan ID yang ingin dihapus dari friend list : ");
                pil1 = inputInt();
                if (model.cekFriend(pil1)) {
                    model.delFriend(model.getAkun1(pil1), model.getAkun(uSession));
                    System.out.println("Friend Deleted!");
                } else {
                    System.out.println("ID Tidak ada dalam daftar !");
                }
                break;
            default :
                System.out.println("tidak ada dipilhan !");
        }
    }

    public void createUser() {
        Akun as = new Akun();
        String np, nb, user, pass, tmptlr, lahir, blan;
        int tgl, bln, thn;
        boolean cekttl = false;
        System.out.print("Nama Depan : ");
        np = sStr.nextLine();
        System.out.print("Nama Belakang : ");
        nb = sStr.nextLine();
        System.out.print("Username : ");
        user = sStr.nextLine();
        System.out.print("Password : ");
        pass = sStr.nextLine();
        System.out.print("Tempat Lahir : ");
        tmptlr = sStr.nextLine();
        do {
            System.out.print("Masukkan Tanggal Lahir : ");
            tgl = inputInt();
            if (tgl > 0 && tgl < 32) {
                cekttl = true;
            } else {
                System.out.println("cek input tanggal anda !");
            }
        } while (cekttl != true);
        cekttl = false;
        do {
            System.out.print("Masukkan Bulan Lahir : ");
            bln = inputInt();
            if (bln > 0 && bln < 13) {
                cekttl = true;
            } else {
                System.out.println("cek input bulan anda !");
            }
        } while (cekttl != true);
        blan = model.convertBulan(bln);
        cekttl = false;
        do {
            System.out.print("Masukkan Tahun lahir : ");
            thn = inputInt();
            if (thn > 999 && thn < 10000) {
                cekttl = true;
            } else {
                System.out.println("tahun harus 4 digit !");
            }
        } while (cekttl != true);
        lahir = Integer.toString(tgl) + "-" + blan + "-" + Integer.toString(thn);
        as.setNamaDepan(np);
        as.setNamaBelakang(nb);
        as.setUsername(user);
        as.setPassword(pass);
        as.setTempatLahir(tmptlr);
        as.setTanggalLahir(lahir);
        model.addAkun(as);
        System.out.println("Account created !");
    }

    public void lihatProfile() {
        String pil1;
        Akun as = new Akun();
        as = model.getAkun(uSession);
        System.out.println("Nama Depan : " + as.getNamaDepan());
        System.out.println("Nama Belakang : " + as.getNamaBelakang());
        System.out.println("Username : " + as.getUsername());
        System.out.println("Password : " + as.getPassword());
        System.out.println("Tempat Lahir : " + as.getTempatLahir());
        System.out.println("Tanggal Lahir : " + as.getTanggalLahir());
        System.out.println("=======================================");
        System.out.print("Ingin update profile ? (y/n) : ");
        pil1 = sStr.nextLine();
        if (pil1.equals("y") || pil1.equals("Y")) {
            String np, nb, user, pass, tmptlr, lahir, blan;
            int tgl, bln, thn;
            boolean cekttl = false;
            System.out.print("Nama Depan : ");
            np = sStr.nextLine();
            System.out.print("Nama Belakang : ");
            nb = sStr.nextLine();
            System.out.print("Username : ");
            user = sStr.nextLine();
            System.out.print("Password : ");
            pass = sStr.nextLine();
            System.out.print("Tempat Lahir : ");
            tmptlr = sStr.nextLine();
            do {
                System.out.print("Masukkan Tanggal Lahir : ");
                tgl = inputInt();
                if (tgl > 0 && tgl < 32) {
                    cekttl = true;
                } else {
                    System.out.println("cek input tanggal anda !");
                }
            } while (cekttl != true);
            cekttl = false;
            do {
                System.out.print("Masukkan Bulan Lahir : ");
                bln = inputInt();
                if (bln > 0 && bln < 13) {
                    cekttl = true;
                } else {
                    System.out.println("cek input bulan anda !");
                }
            } while (cekttl != true);
            blan = model.convertBulan(bln);
            cekttl = false;
            do {
                System.out.print("Masukkan Tahun lahir : ");
                thn = inputInt();
                if (thn > 999 && thn < 10000) {
                    cekttl = true;
                } else {
                    System.out.println("tahun harus 4 digit !");
                }
            } while (cekttl != true);
            lahir = Integer.toString(tgl) + "-" + blan + "-" + Integer.toString(thn);
            as.setNamaDepan(np);
            as.setNamaBelakang(nb);
            as.setUsername(user);
            uSession = user;
            as.setPassword(pass);
            as.setTempatLahir(tmptlr);
            as.setTanggalLahir(lahir);
            model.updateAkun(as);
            System.out.println("data updated !");
        }
    }

    public void yangTampil() {
        int pil = 0;
        do {
            System.out.println("1. Login");
            System.out.println("2. Create Account");
            System.out.print("Pilihan : ");
            pil = inputInt();
            switch (pil) {
                case 1:
                    loginApp();
                    break;
                case 2:
                    createUser();
                    break;
                default:
                    System.out.println("tidak ada dipilihan");
            }
        } while (ceklo != true);
        pil = 0;
        do {
            try {
                menuUtama();
                System.out.print("Pilihan : ");
                pil = inputInt();
                switch (pil) {
                    case 1:
                        lihatProfile();
                        break;
                    case 2:
                        menuMedia();
                        break;
                    case 3:
                        menuFriend();
                        break;
                }
            } catch (Exception e) {
                System.out.println("terdapat error : " + e.getMessage());
            } finally {
                sInt = new Scanner(System.in);
                sStr = new Scanner(System.in);
            }
        } while (pil != 0);
    }

}
