package com.bryan.libarterbe.DTO;

import java.util.ArrayList;

public class BookAPIResponseDTO {
    private String url;
    private String key;
    private String title;
    ArrayList<Object> authors = new ArrayList<Object>();
    private float number_of_pages;
    Identifiers IdentifiersObject;
    ArrayList<Object> publishers = new ArrayList<Object>();
    private String publish_date;
    ArrayList<Object> subjects = new ArrayList<Object>();
    ArrayList<Object> subject_places = new ArrayList<Object>();
    ArrayList<Object> subject_people = new ArrayList<Object>();
    ArrayList<Object> subject_times = new ArrayList<Object>();
    ArrayList<Object> excerpts = new ArrayList<Object>();
    ArrayList<Object> ebooks = new ArrayList<Object>();
    Cover CoverObject;


    // Getter Methods


    public ArrayList<Object> getAuthors() {
        return authors;
    }

    public void setAuthors(ArrayList<Object> authors) {
        this.authors = authors;
    }

    public Identifiers getIdentifiersObject() {
        return IdentifiersObject;
    }

    public void setIdentifiersObject(Identifiers identifiersObject) {
        IdentifiersObject = identifiersObject;
    }

    public ArrayList<Object> getPublishers() {
        return publishers;
    }

    public void setPublishers(ArrayList<Object> publishers) {
        this.publishers = publishers;
    }

    public ArrayList<Object> getSubjects() {
        return subjects;
    }

    public void setSubjects(ArrayList<Object> subjects) {
        this.subjects = subjects;
    }

    public ArrayList<Object> getSubject_places() {
        return subject_places;
    }

    public void setSubject_places(ArrayList<Object> subject_places) {
        this.subject_places = subject_places;
    }

    public ArrayList<Object> getSubject_people() {
        return subject_people;
    }

    public void setSubject_people(ArrayList<Object> subject_people) {
        this.subject_people = subject_people;
    }

    public ArrayList<Object> getSubject_times() {
        return subject_times;
    }

    public void setSubject_times(ArrayList<Object> subject_times) {
        this.subject_times = subject_times;
    }

    public ArrayList<Object> getExcerpts() {
        return excerpts;
    }

    public void setExcerpts(ArrayList<Object> excerpts) {
        this.excerpts = excerpts;
    }

    public ArrayList<Object> getEbooks() {
        return ebooks;
    }

    public void setEbooks(ArrayList<Object> ebooks) {
        this.ebooks = ebooks;
    }

    public Cover getCoverObject() {
        return CoverObject;
    }

    public void setCoverObject(Cover coverObject) {
        CoverObject = coverObject;
    }

    public String getUrl() {
        return url;
    }

    public String getKey() {
        return key;
    }

    public String getTitle() {
        return title;
    }

    public float getNumber_of_pages() {
        return number_of_pages;
    }

    public Identifiers getIdentifiers() {
        return IdentifiersObject;
    }

    public String getPublish_date() {
        return publish_date;
    }

    public Cover getCover() {
        return CoverObject;
    }

    // Setter Methods

    public void setUrl( String url ) {
        this.url = url;
    }

    public void setKey( String key ) {
        this.key = key;
    }

    public void setTitle( String title ) {
        this.title = title;
    }

    public void setNumber_of_pages( float number_of_pages ) {
        this.number_of_pages = number_of_pages;
    }

    public void setIdentifiers( Identifiers identifiersObject ) {
        this.IdentifiersObject = identifiersObject;
    }

    public void setPublish_date( String publish_date ) {
        this.publish_date = publish_date;
    }

    public void setCover( Cover coverObject ) {
        this.CoverObject = coverObject;
    }
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


    // Getter Methods



    // Setter Methods


}