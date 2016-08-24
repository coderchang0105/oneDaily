package com.example.coderchang.onedaily.doman;

import java.util.List;

/**
 * Created by coderchang on 16/8/23.
 */
public class News {
    private String date;
    private List<Story> stories;
    private List<TopStory> top_stories;

    public News() {
    }

    public News(String date, List<Story> stories, List<TopStory> top_stories) {
        this.date = date;
        this.stories = stories;
        this.top_stories = top_stories;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public List<Story> getStories() {
        return stories;
    }

    public void setStories(List<Story> stories) {
        this.stories = stories;
    }

    public List<TopStory> getTop_stories() {
        return top_stories;
    }

    public void setTop_stories(List<TopStory> top_stories) {
        this.top_stories = top_stories;
    }
}
