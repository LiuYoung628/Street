package com.example.mypersonalfile.randian1.StreetClass;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/3/10 0010.
 */
public class Books {

    private int count,start,total;
    private List<BookClass> books = new ArrayList<>();

    private List<MusciClass> musics = new ArrayList<>();

    private List<MovieClass> subjects =new ArrayList<>();

    public List<MovieClass> getSubjects() {
        return subjects;
    }

    public void setSubjects(List<MovieClass> subjects) {
        this.subjects = subjects;
    }
    public void addSubject(MovieClass movieClass){
        subjects.add(movieClass);
    }

    public List<MusciClass> getMusics() {

        return musics;
    }

    public void setMusics(List<MusciClass> musics) {
        this.musics = musics;
    }

    public void addMusic(MusciClass musciClass){
        musics.add(musciClass);
    }

    public int getCount() {
        return count;
    }

    public int getStart() {
        return start;
    }

    public int getTotal() {
        return total;
    }

    public List<BookClass> getBooks() {
        return books;
    }

    public void setBooks(List<BookClass> books) {
        this.books = books;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public void setStart(int start) {
        this.start = start;
    }

    public void setTotal(int total) {
        this.total = total;
    }



    public void addBook(BookClass bookClass){
        books.add(bookClass);
    }
}
