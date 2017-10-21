package com.priadka.newsit_project.DTO;

public class NewsDTO {
    private String title;
    private String date;
    private Integer rating;
    private Integer number_comment;
    //private image;

    public NewsDTO(String title , String date, Integer rating , Integer number_comment){
        this.title = title;
        this.date = date;
        this.rating = rating;
        this.number_comment = number_comment;
    }
    public String getTitle() {return title;}
    public void setTitle(String title) {this.title = title;}

    public Integer getNumberComment() {return number_comment;}
    public void setNumberComment(Integer number_comment) {this.number_comment = number_comment;}

    public Integer getRating() {return rating;}
    public void setRating(Integer rating) {this.rating = rating;}

    public String getDate() {return date;}
    public void setDate(String date) {this.date = date;}
}
