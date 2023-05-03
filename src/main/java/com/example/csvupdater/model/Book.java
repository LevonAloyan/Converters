package com.example.csvupdater.model;

public class Book {

    private int id;
    private Long ISBN;
    private String PrijsNieuw;

    public Long getISBN() {
        return ISBN;
    }

    public void setISBN(Long ISBN) {
        this.ISBN = ISBN;
    }

    public String getPrijsNieuw() {
        return PrijsNieuw;
    }

    public void setPrijsNieuw(String prijsNieuw) {
        PrijsNieuw = prijsNieuw;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
