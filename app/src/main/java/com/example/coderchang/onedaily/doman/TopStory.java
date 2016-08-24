package com.example.coderchang.onedaily.doman;

/**
 * Created by coderchang on 16/8/23.
 */
public class TopStory {
    private String image;
    private int type;
    private int id;
    private String ga_prefix;
    private String title;

    public TopStory() {
    }

    public TopStory(String ga_prefix, int id, String image, String title, int type) {
        this.ga_prefix = ga_prefix;
        this.id = id;
        this.image = image;
        this.title = title;
        this.type = type;
    }

    public String getGa_prefix() {
        return ga_prefix;
    }

    public void setGa_prefix(String ga_prefix) {
        this.ga_prefix = ga_prefix;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
