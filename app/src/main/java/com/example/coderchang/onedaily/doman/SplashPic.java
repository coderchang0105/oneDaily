package com.example.coderchang.onedaily.doman;

/**
 * Created by coderchang on 16/8/21.
 */
public class SplashPic {
    private String text;
    private String img;

    public SplashPic(String img, String text) {
        this.img = img;
        this.text = text;
    }

    public SplashPic() {
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
