package com.example.coderchang.onedaily.doman;

import java.io.Serializable;
import java.util.List;

/**
 * Created by coderchang on 16/8/23.
 */
public class Story implements Serializable{

    private String title;
    private String ga_prefix;
    private List<String> images;
    private int type;
    private int id;
    private boolean multipic = false;

    public Story() {
    }

    public Story(String ga_prefix, int id, List<String> images, String title, int type) {
        this.ga_prefix = ga_prefix;
        this.id = id;
        this.images = images;
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

    public List<String> getImages() {
        return images;
    }

    public void setImages(List<String> images) {
        this.images = images;
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

    @Override
    public String toString() {
        return "Story{" +
                "ga_prefix='" + ga_prefix + '\'' +
                ", title='" + title + '\'' +
                ", images=" + images +
                ", type=" + type +
                ", id=" + id +
                ", multipic=" + multipic +
                '}';
    }
}
