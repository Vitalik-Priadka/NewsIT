package com.priadka.newsit_project.DTO;
// Шаблон информации о новости
public class NewsDTO {
    private int id;
    private String title;
    private String text;
    private String date;
    private Integer rating;
    private Integer number_comment;
    private String image;
    // Конструктор для статьи
    public NewsDTO(int id, String image, String title, String text, String date, Integer rating , Integer number_comment){
        this.id = id;
        this.image = image;
        this.title = title;
        this.text = text;
        this.date = date;
        this.rating = rating;
        this.number_comment = number_comment;
    }
    // Getter and Setter
    public int getId() {return id;}
    public void setId(int id) {this.id = id;}

    public String getImage() {return image;}
    public void setImage(String image) {this.image = image;}

    public String getTitle() {return title;}
    public void setTitle(String title) {this.title = title;}

    public String getText() {return text;}
    public void setText(String text) {this.text = text;}

    public Integer getNumberComment() {return number_comment;}
    public void setNumberComment(Integer number_comment) {this.number_comment = number_comment;}

    public Integer getRating() {return rating;}
    public void setRating(Integer rating) {this.rating = rating;}

    public String getDate() {return date;}
    public void setDate(String date) {this.date = date;}
}
