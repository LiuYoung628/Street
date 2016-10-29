package com.example.mypersonalfile.randian1.StreetClass;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by Administrator on 2016/3/10 0010.
 */
public class BookClass {

    private String id,title;

    private ImageClass  images ;

    public ImageClass getImages() {
        return images;
    }

    public void setImages(ImageClass images) {
        this.images = images;
    }

    private List<String> author =new ArrayList<>();



    public List<String> getAuthor() {
        return author;
    }

    public void setAuthor(List<String> author) {
        this.author = author;
    }


    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }


    public void setId(String id) {
        this.id = id;
    }


    public void setTitle(String title) {
        this.title = title;
    }
}
