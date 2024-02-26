package com.bryan.libarterbe.model;

import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name="tags")
public class Tag {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    private String text;
    @ManyToMany(fetch = FetchType.LAZY)
    private List<Book> books;

    public Tag(Integer id, String text) {
        this.id = id;
        this.text = text;
    }

    public Tag(String text) {
        this.text = text;
    }

    public Tag() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
