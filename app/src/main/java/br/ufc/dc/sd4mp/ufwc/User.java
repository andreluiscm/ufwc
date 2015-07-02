package br.ufc.dc.sd4mp.ufwc;

import java.io.Serializable;

/**
 * Created by André on 11/06/2015.
 */
public class User implements Serializable {

    private int id;
    private String email;
    private String password;
    private String gender;

    public User() {

    }

    public User(int id, String email, String password, String gender) {

        this.id = id;
        this.email = email;
        this.password = password;
        this.gender = gender;

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

}
