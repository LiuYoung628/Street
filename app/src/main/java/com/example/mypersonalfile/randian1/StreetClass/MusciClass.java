package com.example.mypersonalfile.randian1.StreetClass;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2016/3/25 0025.
 */
public class MusciClass {
    private String id,title,image;

    private List<Map<String,String>> author =new ArrayList<>();



    public List<Map<String,String>> getAuthor() {
        return author;
    }

    public void setAuthor(List<Map<String,String>> author) {
        this.author = author;
    }

    public String getId() {
        return id;
    }

    public String getImage() {
        return image;
    }

    public String getTitle() {
        return title;
    }



    public void setId(String id) {
        this.id = id;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
