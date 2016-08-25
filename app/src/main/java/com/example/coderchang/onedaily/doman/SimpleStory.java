package com.example.coderchang.onedaily.doman;

import java.io.Serializable;

/**
 * Created by coderchang on 16/8/25.
 */
public class SimpleStory implements Serializable{
    private String title;
    private String image;
    private String storyId;

    public SimpleStory(String image, String storyId, String title) {
        this.image = image;
        this.storyId = storyId;
        this.title = title;
    }

    public SimpleStory() {
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getStoryId() {
        return storyId;
    }

    public void setStoryId(String storyId) {
        this.storyId = storyId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
