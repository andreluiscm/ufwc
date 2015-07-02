package br.ufc.dc.sd4mp.ufwc;

/**
 * Created by André on 10/06/2015.
 */
public class Comment {

    private int userId;
    private int bathroomId;
    private String text;

    public Comment() {

    }

    public Comment(int userId, int bathroomId, String text) {

        this.userId = userId;
        this.bathroomId = bathroomId;
        this.text = text;

    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getBathroomId() {
        return bathroomId;
    }

    public void setBathroomId(int bathroomId) {
        this.bathroomId = bathroomId;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

}
