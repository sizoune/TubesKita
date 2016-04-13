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
import javax.swing.*;

public class LoginPanelController extends MouseAdapter implements ActionListener {

    private Aplikasi aplikasi;
    private View view;

    public LoginPanelController(Aplikasi e) {
        this.aplikasi = e;
        LoginUser log = new LoginUser();
        log.setVisible(true);
        log.addListener((ActionListener) this);
        view = log;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object src = e.getSource();
        LoginUser log = (LoginUser) view;
        if (src.equals(log.getLoginButton())) {
            if (aplikasi.login(log.getUsername(), log.getPassword())) {
                JOptionPane.showMessageDialog(null, "Login sukses");
            } else {
                JOptionPane.showMessageDialog(null, "Username / password incorrect");
            }
        } else if (src.equals(log.getCreateAccountButton())) {
            Account acc = new Account();
            acc.setVisible(true);
            acc.addListener(this);
            log.dispose();
            view = acc;
        } else if (view instanceof Account) {
            Account acc = (Account) view;
            Model.Akun as = new Model.Akun();
            if (src.equals(acc.getAccountButton())) {
                as.setNamaDepan(acc.getFirstname());
                as.setNamaBelakang(acc.getLastname());
                as.setUsername(acc.getUsername());
                as.setPassword(acc.getPassword());
                as.setTempatLahir(acc.getBirthplace());
                as.setTanggalLahir(acc.getBirthdate());
                as.setEmail(acc.getEmail());
                aplikasi.addAkun(as);
            }
        }
    }
}
