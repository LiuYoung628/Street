package com.example.mypersonalfile.randian1.StreetClass;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2016/3/31 0031.
 */
public class MovieClass {

    private String id,title,year;

    private ImageClass  images ;

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public ImageClass getImages() {
        return images;
    }

    public void setImages(ImageClass images) {
        this.images = images;
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
