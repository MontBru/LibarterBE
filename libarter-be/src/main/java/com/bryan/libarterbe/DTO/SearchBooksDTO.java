package com.bryan.libarterbe.DTO;

public class SearchBooksDTO {
    boolean isRequest;
    String searchTerm;

    int pageNum;

    double minPrice;

    double maxPrice;

    public SearchBooksDTO(boolean isRequest,String searchTerm, int pageNum, double minPrice, double maxPrice) {
        this.isRequest = isRequest;
        this.searchTerm = searchTerm;
        this.pageNum = pageNum;
        this.minPrice = minPrice;
        this.maxPrice = maxPrice;
    }


    public boolean isRequest() {
        return isRequest;
    }

    public void setRequest(boolean request) {
        isRequest = request;
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

    public double getMinPrice() {
        return minPrice;
    }

    public void setMinPrice(double minPrice) {
        this.minPrice = minPrice;
    }

    public double getMaxPrice() {
        return maxPrice;
    }

    public void setMaxPrice(double maxPrice) {
        this.maxPrice = maxPrice;
    }
}
