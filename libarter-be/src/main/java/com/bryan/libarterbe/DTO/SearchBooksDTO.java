package com.bryan.libarterbe.DTO;

public class SearchBooksDTO {
    String searchTerm;

    int pageNum;

    public SearchBooksDTO(String searchTerm, int pageNum) {
        this.searchTerm = searchTerm;
        this.pageNum = pageNum;
    }

    public String getSearchTerm() {
        return searchTerm;
    }

    public void setSearchTerm(String searchTerm) {
        this.searchTerm = searchTerm;
    }

    public int getPageNum() {
        return pageNum;
    }

    public void setPageNum(int pageNum) {
        this.pageNum = pageNum;
    }
}
