package com.bryan.libarterbe.DTO;

import java.util.ArrayList;

public record BookAPIResponseDTO(String url,
                                 String key,
                                 String title,
                                 ArrayList<Object> authors,
                                 float number_of_pages,
                                 Identifiers IdentifiersObject,
                                 ArrayList<Object> publishers,
                                 String publish_date,
                                 ArrayList<Object> subjects,
                                 ArrayList<Object> subject_places,
                                 ArrayList<Object> subject_people,
                                 ArrayList<Object> subject_times,
                                 ArrayList<Object> excerpts,
                                 ArrayList<Object> ebooks,
                                 Cover CoverObject) {
}
class Cover {
    private String small;
    private String medium;
    private String large;


    // Getter Methods

    public String getSmall() {
        return small;
    }

    public String getMedium() {
        return medium;
    }

    public String getLarge() {
        return large;
    }

    // Setter Methods

    public void setSmall( String small ) {
        this.small = small;
    }

    public void setMedium( String medium ) {
        this.medium = medium;
    }

    public void setLarge( String large ) {
        this.large = large;
    }
}
class Identifiers {
    ArrayList<Object> goodreads = new ArrayList<Object>();
    ArrayList<Object> librarything = new ArrayList<Object>();
    ArrayList<Object> isbn_10 = new ArrayList<Object>();
    ArrayList<Object> isbn_13 = new ArrayList<Object>();
    ArrayList<Object> openlibrary = new ArrayList<Object>();

}