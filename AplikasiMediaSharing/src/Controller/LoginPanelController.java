/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controller;

/**
 *
 * @author Fiqih
 */
import Model.Aplikasi;
import view.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class LoginPanelController extends MouseAdapter implements ActionListener {

    private Aplikasi aplikasi;
    private View view;
    private String uSession;
    private int ff = 0;
    private Model.Akun akun = null;

    public LoginPanelController(Aplikasi e) {
        this.aplikasi = e;
        aplikasi.loadSemuaAkun();
        LoginUser log = new LoginUser();
        log.setVisible(true);
        log.setLocationRelativeTo(null);
        log.addListener((ActionListener) this);
        view = log;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String dt, mn, yr, i1, i2, i3;
        Object src = e.getSource();
        if (view instanceof LoginUser) {
            LoginUser log = (LoginUser) view;
            if (src.equals(log.getLoginButton())) {
                if (aplikasi.login(log.getUsername(), log.getPassword())) {
                    String tmp = "";
                    uSession = log.getUsername();
                    Profile pro = new Profile();
                    pro.setLocationRelativeTo(null);
                    Model.Akun as = aplikasi.getAkun(uSession);
                    pro.setFirstName(as.getNamaDepan());
                    pro.setLastName(as.getNamaBelakang());
                    pro.setBirthPlace(as.getTempatLahir());
                    pro.setUsername(as.getUsername());
                    pro.setPassword(as.getPassword());
                    if ((as.getTanggalLahir()).length() == 10) {
                        dt = as.getTanggalLahir().substring(0, 1);
                        mn = as.getTanggalLahir().substring(2, 5);
                        yr = as.getTanggalLahir().substring(6, 10);
                    } else {
                        dt = as.getTanggalLahir().substring(0, 2);
                        mn = as.getTanggalLahir().substring(3, 6);
                        yr = as.getTanggalLahir().substring(7, 11);
                    }
                    pro.setBirthDate(dt, mn, yr);
                    pro.resetMedia();
                    for (int i = 0; i < aplikasi.getsizeMedia(); i++) {
                        if (aplikasi.getMedia(i) instanceof Model.Foto) {
                            pro.getTextFoto().append(aplikasi.displayMedia(aplikasi.getMedia(i)));
                            pro.getTextFoto().append("\n Friend Tagged : ");
                            //pro.getTextFoto().append("\n===========================\n");
                        } else {
                            pro.getTextVideo().append(aplikasi.displayMedia(aplikasi.getMedia(i)));
                            pro.getTextVideo().append("\n Friend Tagged : ");
                            //pro.getTextVideo().append("\n===========================\n");
                        }
                        aplikasi.refreshTag(aplikasi.getMedia(i).getIdMedia(), aplikasi.getAkun(uSession));
                        if (!aplikasi.getMedia(i).cekTag()) {
                            //System.out.print("Friend tagged : ");
                            for (int s = 0; s < aplikasi.getMedia(i).sizeTag(); s++) {
                                tmp = tmp + aplikasi.getMedia(i).getPersonTag(s).getUsername() + ", ";
                            }
                            if (aplikasi.getMedia(i) instanceof Model.Foto) {
                                pro.getTextFoto().append(tmp);
                                pro.getTextFoto().append("\n===========================\n");
                            } else {
                                pro.getTextVideo().append(tmp);
                                pro.getTextVideo().append("\n===========================\n");
                            }
                            tmp = "";
                        } else {
                            if (aplikasi.getMedia(i) instanceof Model.Foto) {
                                //pro.getTextFoto().append(aplikasi.displayMedia(aplikasi.getMedia(i)));
                                pro.getTextFoto().append("\n===========================\n");
                            } else {
                                //pro.getTextVideo().append(aplikasi.displayMedia(aplikasi.getMedia(i)));
                                pro.getTextVideo().append("\n===========================\n");
                            }
                        }
                    }
                    if (!aplikasi.cekFriend()) {
                        DefaultListModel df = new DefaultListModel();
                        for (int i = 0; i < aplikasi.getsizeFriend(); i++) {
                            df.addElement(aplikasi.getFriend(i).getUsername());
                        }
                        pro.getlistFriend().setModel(df);
                    }
                    pro.setVisible(true);
                    pro.addListener(this);
                    log.dispose();
                    view = pro;
                } else {
                    JOptionPane.showMessageDialog(null, "Username / password incorrect");
                    log.reset();
                }
            } else if (src.equals(log.getCreateAccountButton())) {
                Account acc = new Account();
                acc.setLocationRelativeTo(null);
                acc.setVisible(true);
                acc.addListener(this);
                log.dispose();
                view = acc;
            }
        } else if (view instanceof Account) {
            Account acc = (Account) view;
            Model.Akun as = new Model.Akun();
            if (src.equals(acc.getAccountButton())) {
                i1 = (String) acc.getcbDate().getSelectedItem();
                i2 = (String) acc.getcbMonth().getSelectedItem();
                i3 = (String) acc.getcbYear().getSelectedItem();
                if (acc.getFirstname().equals("") || acc.getLastname().equals("")
                        || acc.getUsername().equals("") || acc.getPassword().equals("")
                        || acc.getBirthplace().equals("") || i1.equals("-")
                        || i2.equals("-")
                        || i3.equals("-")) {
                    JOptionPane.showMessageDialog(null, "Data anda belum lengkap");
                } else {
                    if (aplikasi.getAkun(acc.getUsername()) == null) {
                        as.setNamaDepan(acc.getFirstname());
                        as.setNamaBelakang(acc.getLastname());
                        as.setUsername(acc.getUsername());
                        as.setPassword(acc.getPassword());
                        as.setTempatLahir(acc.getBirthplace());
                        as.setTanggalLahir(acc.getBirthdate());
                        as.setEmail(acc.getEmail());
                        aplikasi.addAkun(as);
                        JOptionPane.showMessageDialog(null, "Data Saved !");
                    } else {
                        JOptionPane.showMessageDialog(null, "Username sudah terdaftar tolong isi dengan yang lain !");
                    }
                    acc.reset();
                }
            } else if (src.equals(acc.getBackbutton())) {
                LoginUser log = new LoginUser();
                log.setLocationRelativeTo(null);
                log.setVisible(true);
                log.addListener(this);
                acc.dispose();
                view = log;
            }
        } else if (view instanceof Profile) {
            Profile pro = (Profile) view;
            if (src.equals(pro.getEditButton())) {
                i1 = (String) pro.getcbDate().getSelectedItem();
                i2 = (String) pro.getcbMonth().getSelectedItem();
                i3 = (String) pro.getcbYear().getSelectedItem();
                if (pro.getFirstName().equals("") || pro.getLastName().equals("")
                        || pro.getUsername().equals("") || pro.getPassword().equals("")
                        || pro.getBirthPlace().equals("") || i1.equals("-")
                        || i2.equals("-")
                        || i3.equals("-")) {
                    JOptionPane.showMessageDialog(null, "Data anda belum lengkap");
                } else {
                    if (aplikasi.getAkun(pro.getUsername()) == null) {
                        Model.Akun as = aplikasi.getAkun(uSession);
                        as.setNamaDepan(pro.getFirstName());
                        as.setNamaBelakang(pro.getLastName());
                        as.setUsername(pro.getUsername());
                        as.setPassword(pro.getPassword());
                        as.setTempatLahir(pro.getBirthPlace());
                        as.setTanggalLahir(pro.getBirthDate());
                        //as.setEmail(.getEmail()); belum ada email nya wkwkwk
                        aplikasi.updateAkun(as);
                        JOptionPane.showMessageDialog(null, "Data Updated !");
                    } else {
                        JOptionPane.showMessageDialog(null, "Username sudah terdaftar tolong isi dengan yang lain !");
                        pro.setUsername("");
                    }
                }
            } else if (src.equals(pro.getMediaButton())) {
                boolean cekf = false;
                Media med = new Media();
                DefaultListModel df = new DefaultListModel();
                DefaultListModel df1 = new DefaultListModel();
                med.setLocationRelativeTo(null);
                aplikasi.refreshListMedia(aplikasi.getAkun(uSession).getIdAkun());
                for (int i = 0; i < aplikasi.getsizeMedia(); i++) {
                    if (aplikasi.getMedia(i) instanceof Model.Foto) {
                        df.addElement(aplikasi.getMedia(i).getNama());
                    } else {
                        df1.addElement(aplikasi.getMedia(i).getNama());
                    }
                }
                med.getlistFoto().setModel(df);
                med.getlistVideo().setModel(df1);
                if (!aplikasi.cekFriend()) {
                    DefaultListModel df2 = new DefaultListModel();
                    for (int i = 0; i < aplikasi.getsizeFriend(); i++) {
                        df2.addElement(aplikasi.getFriend(i).getUsername());
                    }
                    med.getlistFriend().setModel(df2);
                }
                med.setVisible(true);
                med.addAdapter(this);
                med.addListener(this);
                med.gettabTag().getModel().addChangeListener((ChangeEvent e1) -> {
                    if (med.gettabTag().getTitleAt(med.gettabTag().getSelectedIndex()).equals("Tag")) {
                        ff = 0;
                        akun = null;
                        med.getlistFoto().clearSelection();
                        med.getlistVideo().clearSelection();
                        //JOptionPane.showMessageDialog(null, "tag");
                    }
                    if (med.gettabTag().getTitleAt(med.gettabTag().getSelectedIndex()).equals("- Media")) {
                        ff = 0;
                        akun = null;
                        med.getlistFriend().clearSelection();
                        med.getlistMedia().clearSelection();
                        //JOptionPane.showMessageDialog(null, "tag");
                    }
                });
                pro.dispose();
                view = med;
            } else if (src.equals(pro.getFriendButton())) {
                boolean cekf = false;
                AddFriend ad = new AddFriend();
                ad.setLocationRelativeTo(null);
                DefaultListModel df = new DefaultListModel();
                DefaultListModel df1 = new DefaultListModel();
                aplikasi.refreshlistFriend(aplikasi.getAkun(uSession));
                if (!aplikasi.cekFriend()) {
                    for (int i = 0; i < aplikasi.getsizeAkun(); i++) {
                        for (int x = 0; x < aplikasi.getsizeFriend(); x++) {
                            if (aplikasi.getFriend(x).getIdAkun() == aplikasi.getAkun(i).getIdAkun()) {
                                cekf = true;
                            }
                        }
                        //System.out.println(aplikasi.getAkun(i).getUsername());
                        if (cekf == false) {
                            if (!aplikasi.getAkun(i).getUsername().equals(uSession)) {
                                df.addElement(aplikasi.getAkun(i).getUsername());
                            }
                        }
                        cekf = false;
                    }
                    //ad.getlistAkun().setModel(df);
                    for (int i = 0; i < aplikasi.getsizeFriend(); i++) {
                        df1.addElement(aplikasi.getFriend(i).getUsername());
                    }
                    ad.getlistAkun1().setModel(df1);
                } else {
                    for (int i = 0; i < aplikasi.getsizeAkun(); i++) {
                        if (!aplikasi.getAkun(i).getUsername().equals(uSession)) {
                            df.addElement(aplikasi.getAkun(i).getUsername());
                        }
                    }
                }
                ad.getlistAkun().setModel(df);
                ad.setVisible(true);
                ad.addListener(this);
                ad.addAdapter(this);
                pro.dispose();
                view = ad;
            }
        } else if (view instanceof Media) {
            String tmp = "";
            Media med = (Media) view;
            if (src.equals(med.getBack())) {
                Profile pro = new Profile();
                pro.setLocationRelativeTo(null);
                Model.Akun as = aplikasi.getAkun(uSession);
                pro.setFirstName(as.getNamaDepan());
                pro.setLastName(as.getNamaBelakang());
                pro.setBirthPlace(as.getTempatLahir());
                pro.setUsername(as.getUsername());
                pro.setPassword(as.getPassword());
                if ((as.getTanggalLahir()).length() == 10) {
                    dt = as.getTanggalLahir().substring(0, 1);
                    mn = as.getTanggalLahir().substring(2, 5);
                    yr = as.getTanggalLahir().substring(6, 10);
                } else {
                    dt = as.getTanggalLahir().substring(0, 2);
                    mn = as.getTanggalLahir().substring(3, 6);
                    yr = as.getTanggalLahir().substring(7, 11);
                }
                pro.setBirthDate(dt, mn, yr);
                aplikasi.refreshListMedia(aplikasi.getAkun(uSession).getIdAkun());
                for (int i = 0; i < aplikasi.getsizeMedia(); i++) {
                    if (aplikasi.getMedia(i) instanceof Model.Foto) {
                        pro.getTextFoto().append(aplikasi.displayMedia(aplikasi.getMedia(i)));
                        pro.getTextFoto().append("\n Friend Tagged : ");
                        //pro.getTextFoto().append("\n===========================\n");
                    } else {
                        pro.getTextVideo().append(aplikasi.displayMedia(aplikasi.getMedia(i)));
                        pro.getTextVideo().append("\n Friend Tagged : ");
                        //pro.getTextVideo().append("\n===========================\n");
                    }
                    aplikasi.refreshTag(aplikasi.getMedia(i).getIdMedia(), aplikasi.getAkun(uSession));
                    //System.out.println(aplikasi.getMedia(i).getNama()+""+aplikasi.getMedia(i).sizeTag());
                    if (!aplikasi.getMedia(i).cekTag()) {
                        //System.out.print("Friend tagged : ");
                        for (int s = 0; s < aplikasi.getMedia(i).sizeTag(); s++) {
                            tmp = tmp + aplikasi.getMedia(i).getPersonTag(s).getUsername() + ", ";
                        }
                        if (aplikasi.getMedia(i) instanceof Model.Foto) {
                            pro.getTextFoto().append(tmp);
                            pro.getTextFoto().append("\n===========================\n");
                        } else {
                            pro.getTextVideo().append(tmp);
                            pro.getTextVideo().append("\n===========================\n");
                        }
                        tmp = "";
                    } else {
                        if (aplikasi.getMedia(i) instanceof Model.Foto) {
                            //pro.getTextFoto().append(aplikasi.displayMedia(aplikasi.getMedia(i)));
                            pro.getTextFoto().append("\n===========================\n");
                        } else {
                            //pro.getTextVideo().append(aplikasi.displayMedia(aplikasi.getMedia(i)));
                            pro.getTextVideo().append("\n===========================\n");
                        }
                    }
                }
                if (!aplikasi.cekFriend()) {
                    DefaultListModel df = new DefaultListModel();
                    for (int i = 0; i < aplikasi.getsizeFriend(); i++) {
                        df.addElement(aplikasi.getFriend(i).getUsername());
                    }
                    pro.getlistFriend().setModel(df);
                }
                pro.setVisible(true);
                pro.addListener(this);
                med.dispose();
                view = pro;
            } else if (src.equals(med.getaddFoto())) {
                Double ab;
                if (med.getnameFoto().equals("") || med.getsizeFoto().equals("")) {
                    JOptionPane.showMessageDialog(null, "Data belum lengkap !");
                } else {
                    try {
                        ab = Double.parseDouble(med.getsizeFoto());
                        Model.Media mft = new Model.Foto(med.getnameFoto(), ab);
                        aplikasi.addMedia(mft, aplikasi.getAkun(uSession));
                        JOptionPane.showMessageDialog(null, "Media Added !");
                        med.reset();
                        DefaultListModel df = new DefaultListModel();
                        DefaultListModel df1 = new DefaultListModel();
                        med.setLocationRelativeTo(null);
                        aplikasi.refreshListMedia(aplikasi.getAkun(uSession).getIdAkun());
                        for (int i = 0; i < aplikasi.getsizeMedia(); i++) {
                            if (aplikasi.getMedia(i) instanceof Model.Foto) {
                                df.addElement(aplikasi.getMedia(i).getNama());
                            } else {
                                df1.addElement(aplikasi.getMedia(i).getNama());
                            }
                        }
                        med.getlistFoto().setModel(df);
                        med.getlistVideo().setModel(df1);
                    } catch (NumberFormatException x) {
                        JOptionPane.showMessageDialog(null, "tolong isi size dengan angka !");
                        med.reset();
                    }
                }
            } else if (src.equals(med.getaddVideo())) {
                Double ab;
                if (med.getnameVideo().equals("") || med.getsizeVideo().equals("")) {
                    JOptionPane.showMessageDialog(null, "Data belum lengkap !");
                } else {
                    try {
                        ab = Double.parseDouble(med.getsizeVideo());
                        Model.Media mft = new Model.Video(med.getnameVideo(), ab);
                        aplikasi.addMedia(mft, aplikasi.getAkun(uSession));
                        JOptionPane.showMessageDialog(null, "Media Added !");
                        med.reset();
                        DefaultListModel df = new DefaultListModel();
                        DefaultListModel df1 = new DefaultListModel();
                        med.setLocationRelativeTo(null);
                        aplikasi.refreshListMedia(aplikasi.getAkun(uSession).getIdAkun());
                        for (int i = 0; i < aplikasi.getsizeMedia(); i++) {
                            if (aplikasi.getMedia(i) instanceof Model.Foto) {
                                df.addElement(aplikasi.getMedia(i).getNama());
                            } else {
                                df1.addElement(aplikasi.getMedia(i).getNama());
                            }
                        }
                        med.getlistFoto().setModel(df);
                        med.getlistVideo().setModel(df1);
                    } catch (NumberFormatException x) {
                        JOptionPane.showMessageDialog(null, "tolong isi size dengan angka !");
                        med.reset();
                    }
                }
            } else if (src.equals(med.getDelPho())) {
                if (ff != 0) {
                    aplikasi.delMedia(ff);
                    JOptionPane.showMessageDialog(null, "Data Delete !");
                    DefaultListModel df = new DefaultListModel();
                    DefaultListModel df1 = new DefaultListModel();
                    med.setLocationRelativeTo(null);
                    aplikasi.refreshListMedia(aplikasi.getAkun(uSession).getIdAkun());
                    for (int i = 0; i < aplikasi.getsizeMedia(); i++) {
                        if (aplikasi.getMedia(i) instanceof Model.Foto) {
                            df.addElement(aplikasi.getMedia(i).getNama());
                        } else {
                            df1.addElement(aplikasi.getMedia(i).getNama());
                        }
                    }
                    med.getlistFoto().setModel(df);
                    med.getlistVideo().setModel(df1);
                    ff = 0;
                } else {
                    JOptionPane.showMessageDialog(null, "Tidak ada foto yang dipilih !");
                }
            } else if (src.equals(med.getDelVid())) {
                if (ff != 0) {
                    aplikasi.delMedia(ff);
                    JOptionPane.showMessageDialog(null, "Data Delete !");
                    DefaultListModel df = new DefaultListModel();
                    DefaultListModel df1 = new DefaultListModel();
                    med.setLocationRelativeTo(null);
                    aplikasi.refreshListMedia(aplikasi.getAkun(uSession).getIdAkun());
                    for (int i = 0; i < aplikasi.getsizeMedia(); i++) {
                        if (aplikasi.getMedia(i) instanceof Model.Foto) {
                            df.addElement(aplikasi.getMedia(i).getNama());
                        } else {
                            df1.addElement(aplikasi.getMedia(i).getNama());
                        }
                    }
                    med.getlistFoto().setModel(df);
                    med.getlistVideo().setModel(df1);
                    ff = 0;
                } else {
                    JOptionPane.showMessageDialog(null, "Tidak ada video yang dipilih !");
                }
            } else if (src.equals(med.getcomboMedia())) {
                if (med.getselectedCBMdia().equals("Photo")) {
                    ff = 0;
                    med.getPanelMedia().setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.BLACK), "Photo"));
                    DefaultListModel df = new DefaultListModel();
                    aplikasi.refreshListMedia(aplikasi.getAkun(uSession).getIdAkun());
                    for (int i = 0; i < aplikasi.getsizeMedia(); i++) {
                        if (aplikasi.getMedia(i) instanceof Model.Foto) {
                            df.addElement(aplikasi.getMedia(i).getNama());
                        }
                        med.getlistMedia().setModel(df);
                    }
                } else if (med.getselectedCBMdia().equals("Video")) {
                    ff = 0;
                    med.getPanelMedia().setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.BLACK), "Video"));
                    DefaultListModel df = new DefaultListModel();
                    aplikasi.refreshListMedia(aplikasi.getAkun(uSession).getIdAkun());
                    for (int i = 0; i < aplikasi.getsizeMedia(); i++) {
                        if (aplikasi.getMedia(i) instanceof Model.Video) {
                            df.addElement(aplikasi.getMedia(i).getNama());
                        }
                        med.getlistMedia().setModel(df);
                    }
                } else {
                    med.getPanelMedia().setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.BLACK), ""));
                    DefaultListModel df = new DefaultListModel();
                    med.getlistMedia().setModel(df);
                    ff = 0;
                }
            } else if (src.equals(med.getTag())) {
                if (akun != null) {
                    if (ff != 0) {
                        if (!aplikasi.cekTagged(ff, aplikasi.getAkun(akun.getUsername()), aplikasi.getAkun(uSession))) {
                            aplikasi.addTag(ff, aplikasi.getAkun(akun.getUsername()), aplikasi.getAkun(uSession));
                            JOptionPane.showMessageDialog(null, "Tagged !");
                            aplikasi.refreshTag(ff, akun);
                            ff = 0;
                            akun = null;
                        } else {
                            JOptionPane.showMessageDialog(null, "Akun " + akun.getUsername() + " sudah di tag ke media dengan id " + ff);
                        }
                    } else {
                        JOptionPane.showMessageDialog(null, "belum ada foto / video yang dipilih !");
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "belum ada teman yang dipilih !");
                }
            } else if (src.equals(med.getcomboMedia1())) {
                if (med.getselectedCBMdia1().equals("Photo")) {
                    ff = 0;
                    med.getPanelMedia1().setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.BLACK), "Photo"));
                    DefaultListModel df = new DefaultListModel();
                    aplikasi.refreshListMedia(aplikasi.getAkun(uSession).getIdAkun());
                    for (int i = 0; i < aplikasi.getsizeMedia(); i++) {
                        if (aplikasi.getMedia(i) instanceof Model.Foto) {
                            df.addElement(aplikasi.getMedia(i).getNama());
                        }
                        med.getlistMedia1().setModel(df);
                    }
                } else if (med.getselectedCBMdia1().equals("Video")) {
                    ff = 0;
                    med.getPanelMedia1().setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.BLACK), "Video"));
                    DefaultListModel df = new DefaultListModel();
                    aplikasi.refreshListMedia(aplikasi.getAkun(uSession).getIdAkun());
                    for (int i = 0; i < aplikasi.getsizeMedia(); i++) {
                        if (aplikasi.getMedia(i) instanceof Model.Video) {
                            df.addElement(aplikasi.getMedia(i).getNama());
                        }
                        med.getlistMedia1().setModel(df);
                    }
                } else {
                    med.getPanelMedia1().setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.BLACK), ""));
                    DefaultListModel df = new DefaultListModel();
                    med.getlistMedia1().setModel(df);
                    ff = 0;
                }
            } else if (src.equals(med.getremoveTag())) {
                if (ff != 0) {
                    if (akun != null) {
                        aplikasi.deleteTag(ff, akun);
                        JOptionPane.showMessageDialog(null, "Tag Removed !");
                        DefaultListModel df = new DefaultListModel();
                        med.getlistFriend1().setModel(df);
                        med.getlistMedia1().clearSelection();
                        ff = 0;
                        akun = null;
                    } else {
                        JOptionPane.showMessageDialog(null, "belum ada teman yang dipilih !");
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "belum ada photo / video yang dipilih");
                }
            } else if (src.equals(med.getseeTagged())) {
                if (ff != 0) {
                    DefaultListModel df3 = new DefaultListModel();
                    aplikasi.refreshTag(aplikasi.getMediaTag(ff).getIdMedia(), aplikasi.getAkun(uSession));
                    if (!aplikasi.cekTag(ff)) {
                        for (int i = 0; i < aplikasi.getMediaTag(ff).sizeTag(); i++) {
                            df3.addElement(aplikasi.getMediaTag(ff).getPersonTag(i).getUsername());
                        }
                        med.getlistFriend1().setModel(df3);
                        //JOptionPane.showMessageDialog(null, aplikasi.getMediaTag(ff).sizeTag());
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "belum ada photo / video yang dipilih");
                }
            }
        } else if (view instanceof AddFriend) {
            AddFriend ad = (AddFriend) view;
            if (src.equals(ad.getBack())) {
                Profile pro = new Profile();
                pro.setLocationRelativeTo(null);
                Model.Akun as = aplikasi.getAkun(uSession);
                pro.setFirstName(as.getNamaDepan());
                pro.setLastName(as.getNamaBelakang());
                pro.setBirthPlace(as.getTempatLahir());
                pro.setUsername(as.getUsername());
                pro.setPassword(as.getPassword());
                if ((as.getTanggalLahir()).length() == 10) {
                    dt = as.getTanggalLahir().substring(0, 1);
                    mn = as.getTanggalLahir().substring(2, 5);
                    yr = as.getTanggalLahir().substring(6, 10);
                } else {
                    dt = as.getTanggalLahir().substring(0, 2);
                    mn = as.getTanggalLahir().substring(3, 6);
                    yr = as.getTanggalLahir().substring(7, 11);
                }
                aplikasi.refreshListMedia(aplikasi.getAkun(uSession).getIdAkun());
                pro.setBirthDate(dt, mn, yr);
                pro.resetMedia();
                for (int i = 0; i < aplikasi.getsizeMedia(); i++) {
                    if (aplikasi.getMedia(i) instanceof Model.Foto) {
                        pro.getTextFoto().append(aplikasi.displayMedia(aplikasi.getMedia(i)));
                        pro.getTextFoto().append("\n===========================\n");
                    } else {
                        pro.getTextVideo().append(aplikasi.displayMedia(aplikasi.getMedia(i)));
                        pro.getTextVideo().append("\n===========================\n");
                    }
                }
                if (!aplikasi.cekFriend()) {
                    DefaultListModel df = new DefaultListModel();
                    for (int i = 0; i < aplikasi.getsizeFriend(); i++) {
                        df.addElement(aplikasi.getFriend(i).getUsername());
                    }
                    pro.getlistFriend().setModel(df);
                }
                pro.setVisible(true);
                pro.addListener(this);
                ad.dispose();
                view = pro;
            } else if (src.equals(ad.getAdd())) {
                if (akun != null) {
                    boolean cekf = false;
                    aplikasi.addFriend(akun, aplikasi.getAkun(uSession));
                    JOptionPane.showMessageDialog(null, "Friend Added !");
                    DefaultListModel df = new DefaultListModel();
                    DefaultListModel df1 = new DefaultListModel();
                    aplikasi.refreshlistFriend(aplikasi.getAkun(uSession));
                    if (!aplikasi.cekFriend()) {
                        for (int i = 0; i < aplikasi.getsizeAkun(); i++) {
                            for (int x = 0; x < aplikasi.getsizeFriend(); x++) {
                                if (aplikasi.getFriend(x).getIdAkun() == aplikasi.getAkun(i).getIdAkun()) {
                                    cekf = true;
                                }
                            }
                            if (cekf == false) {
                                df.addElement(aplikasi.getAkun(i).getUsername());
                            }
                            cekf = false;
                        }
                        ad.getlistAkun().setModel(df);
                        for (int i = 0; i < aplikasi.getsizeFriend(); i++) {
                            df1.addElement(aplikasi.getFriend(i).getUsername());
                        }
                        ad.getlistAkun1().setModel(df1);
                    } else {
                        for (int i = 0; i < aplikasi.getsizeAkun(); i++) {
                            df.addElement(aplikasi.getAkun(i).getUsername());
                        }
                        ad.getlistAkun().setModel(df);
                    }
                    akun = null;
                } else {
                    JOptionPane.showMessageDialog(null, "Tidak ada Akun yang dipilih !");
                }
            } else if (src.equals(ad.getDel())) {
                if (akun != null) {
                    boolean cekf = false;
                    aplikasi.delFriend(akun, aplikasi.getAkun(uSession));
                    JOptionPane.showMessageDialog(null, "Friend Deleted !");
                    DefaultListModel df = new DefaultListModel();
                    DefaultListModel df1 = new DefaultListModel();
                    aplikasi.refreshlistFriend(aplikasi.getAkun(uSession));
                    if (!aplikasi.cekFriend()) {
                        for (int i = 0; i < aplikasi.getsizeAkun(); i++) {
                            for (int x = 0; x < aplikasi.getsizeFriend(); x++) {
                                if (aplikasi.getFriend(x).getIdAkun() == aplikasi.getAkun(i).getIdAkun()) {
                                    cekf = true;
                                }
                            }
                            if (cekf == false) {
                                df.addElement(aplikasi.getAkun(i).getUsername());
                            }
                            cekf = false;
                        }
                        ad.getlistAkun().setModel(df);
                        for (int i = 0; i < aplikasi.getsizeFriend(); i++) {
                            df1.addElement(aplikasi.getFriend(i).getUsername());
                        }
                        ad.getlistAkun1().setModel(df1);
                    } else {
                        for (int i = 0; i < aplikasi.getsizeAkun(); i++) {
                            df.addElement(aplikasi.getAkun(i).getUsername());
                        }
                        ad.getlistAkun().setModel(df);
                        for (int i = 0; i < aplikasi.getsizeFriend(); i++) {
                            df1.addElement(aplikasi.getFriend(i).getUsername());
                        }
                        ad.getlistAkun1().setModel(df1);
                    }
                    akun = null;
                } else {
                    JOptionPane.showMessageDialog(null, "Tidak ada Akun yang dipilih !");
                }
            }
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {
        if (view instanceof Media) {
            Media m = (Media) view;

            if (m.gettabPabe().getSelectedComponent() == m.getPanel3()) {
                ff = 0;
                m.getlistVideo().clearSelection();
                // m.getlistFriend().clearSelection();
                // m.getlistMedia().clearSelection();
                if (aplikasi.getMediaFoto(m.getselectedFoto()) != null) {
                    ff = aplikasi.getMediaFoto(m.getselectedFoto()).getIdMedia();
                }
            }
            if (m.gettabPabe().getSelectedComponent() == m.getPanel4()) {
                ff = 0;
                m.getlistFoto().clearSelection();
                // m.getlistFriend().clearSelection();
                //m.getlistMedia().clearSelection();
                if (aplikasi.getMediaVideo(m.getselectedVideo()) != null) {
                    ff = aplikasi.getMediaVideo(m.getselectedVideo()).getIdMedia();
                }
            }
            if (m.gettabPane().getSelectedComponent() == m.getPanel9()) {
                ff = 0;
                m.getlistFriend1().clearSelection();
                m.getlistMedia1().clearSelection();
                if (m.getcbMedia().getSelectedItem().equals("Photo")) {
                    if (aplikasi.getMediaFoto(m.getselectedMedia()) != null) {
                        ff = aplikasi.getMediaFoto(m.getselectedMedia()).getIdMedia();
                        //JOptionPane.showMessageDialog(null, aplikasi.getMediaFoto(m.getselectedMedia()).getIdMedia());
                    }
                } else if (m.getcbMedia().getSelectedItem().equals("Video")) {
                    if (aplikasi.getMediaVideo(m.getselectedMedia()) != null) {
                        ff = aplikasi.getMediaVideo(m.getselectedMedia()).getIdMedia();
                        //JOptionPane.showMessageDialog(null, aplikasi.getMediaVideo(m.getselectedMedia()).getIdMedia());
                    }
                }

                akun = null;
                if (aplikasi.getAkun(m.getselectedFriend()) != null) {
                    akun = aplikasi.getAkun(m.getselectedFriend());
                    //JOptionPane.showMessageDialog(null, akun.getUsername());
                }

            }
            if (m.gettabPane().getSelectedComponent() == m.getPanel10()) {
                //JOptionPane.showMessageDialog(null, "tes");
                //m.getlistFriend1().setModel(
                ff = 0;
                akun = null;
                m.getlistFriend().clearSelection();
                m.getlistMedia().clearSelection();
                if (m.getcbMedia1().getSelectedItem().equals("Photo")) {
                    //JOptionPane.showMessageDialog(null, m.getselectedMedia1());
                    if (aplikasi.getMediaFoto(m.getselectedMedia1()) != null) {
                        ff = aplikasi.getMediaFoto(m.getselectedMedia1()).getIdMedia();
                        /*DefaultListModel df3 = new DefaultListModel();
                         aplikasi.refreshTag(aplikasi.getMediaTag(ff).getIdMedia(), aplikasi.getAkun(uSession));
                         if (!aplikasi.cekTag(ff)) {
                         for (int i = 0; i < aplikasi.getMediaTag(ff).sizeTag(); i++) {
                         df3.addElement(aplikasi.getMediaTag(ff).getPersonTag(i).getUsername());
                         }
                         m.getlistFriend1().setModel(df3);
                         //JOptionPane.showMessageDialog(null, aplikasi.getMediaTag(ff).sizeTag());
                         }*/
                    }

                    //if (aplikasi.getAkun(m.getselectedFriend1()) != null) {
                    //akun = aplikasi.getAkun(m.getselectedFriend1());
                    //}
                } else if (m.getcbMedia1().getSelectedItem().equals("Video")) {
                    if (aplikasi.getMediaVideo(m.getselectedMedia1()) != null) {
                        ff = aplikasi.getMediaVideo(m.getselectedMedia1()).getIdMedia();
                        DefaultListModel df3 = new DefaultListModel();
                        //JOptionPane.showMessageDialog(null, aplikasi.getMediaVideo(m.getselectedMedia()).getIdMedia());
                        /*aplikasi.refreshTag(aplikasi.getMediaTag(ff).getIdMedia(), aplikasi.getAkun(uSession));
                         if (!aplikasi.cekTag(ff)) {
                         for (int i = 0; i < aplikasi.getMediaTag(ff).sizeTag(); i++) {
                         df3.addElement(aplikasi.getMediaTag(ff).getPersonTag(i).getUsername());
                         }
                         m.getlistFriend1().setModel(df3);
                         //JOptionPane.showMessageDialog(null, aplikasi.getMediaTag(ff).sizeTag());
                         }*/
                    }
                }
                if (aplikasi.getAkun(m.getselectedFriend1()) != null) {
                    akun = aplikasi.getAkun(m.getselectedFriend1());
                    //JOptionPane.showMessageDialog(null, akun.getUsername());
                }

            }

        } else if (view instanceof AddFriend) {
            AddFriend ad = (AddFriend) view;
            if (ad.gettabPane().getSelectedComponent() == ad.getPanelAdd()) {
                akun = null;
                ad.getlistAkun1().clearSelection();
                if (aplikasi.getAkun(ad.getselectedAdd()) != null) {
                    akun = aplikasi.getAkun(ad.getselectedAdd());
                    //JOptionPane.showMessageDialog(null, akun.getNamaDepan());
                }
            }
            if (ad.gettabPane().getSelectedComponent() == ad.getPanelDel()) {
                akun = null;
                ad.getlistAkun().clearSelection();
                if (aplikasi.getAkun(ad.getselectedDel()) != null) {
                    akun = aplikasi.getAkun(ad.getselectedDel());
                }
            }
        }
    }
}
