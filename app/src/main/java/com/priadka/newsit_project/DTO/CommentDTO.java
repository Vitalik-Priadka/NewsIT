package com.priadka.newsit_project.DTO;


public class CommentDTO {
    private int image;
    private String author, id;
    private String text, date;

    public CommentDTO(String id , int image,String date, String author, String text) {
        this.id = id;
        this.image = image;
        this.date = date;
        this.author = author;
        this.text = text;
    }

    public String getCommId() {return id;}
    public void setCommId(String id) {this.id = id;}

    public int getCommImage() {return image;}
    public void setCommImage(int image) {this.image = image;}

    public String getCommDate() {return date;}
    public void setCommDate(String date) {this.date = date;}

    public String getCommAuthor() {return author;}
    public void setCommAuthor(String author) {this.author = author;}

    public String getCommText() {return text;}
    public void setCommText(String text) {this.text = text;}
}
