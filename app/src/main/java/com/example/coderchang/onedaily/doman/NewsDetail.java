package com.example.coderchang.onedaily.doman;

import java.util.List;

/**
 * Created by coderchang on 16/8/24.
 */
public class NewsDetail {
    private String body;
    private String image_source;
    private String title;
    private String share_url;
    private List<String> js;
    private String ga_prefix;
    private List<String> images;
    private int type;
    private int id;
    private List<String> css;

    public NewsDetail() {
    }

    public NewsDetail(String body, List<String> css, String ga_prefix, int id, String image_source,
                      List<String> images, List<String> js,
                      String share_url, String title, int type) {
        this.body = body;
        this.css = css;
        this.ga_prefix = ga_prefix;
        this.id = id;
        this.image_source = image_source;
        this.images = images;
        this.js = js;
        this.share_url = share_url;
        this.title = title;
        this.type = type;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public List<String> getCss() {
        return css;
    }

    public void setCss(List<String> css) {
        this.css = css;
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

    public String getImage_source() {
        return image_source;
    }

    public void setImage_source(String image_source) {
        this.image_source = image_source;
    }

    public List<String> getImages() {
        return images;
    }

    public void setImages(List<String> images) {
        this.images = images;
    }

    public List<String> getJs() {
        return js;
    }

    public void setJs(List<String> js) {
        this.js = js;
    }

    public String getShare_url() {
        return share_url;
    }

    public void setShare_url(String share_url) {
        this.share_url = share_url;
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
