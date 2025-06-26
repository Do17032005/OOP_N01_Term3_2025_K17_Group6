package com.example.servingwebcontent.model;
 
public class Movie extends ObjectGeneral {     

    private String title;
    private String showTime; // Ngày phát hành
    private String dateTime; // Ngày giờ chiếu
    private int duration; // Thời lượng của phim
    private String genre; // Thể loại phim
    private int age; // Độ tuổi
    private String description = ""; // Mô tả phim

    public Movie() {
    super("", ""); // hoặc giá trị mặc định phù hợp với ObjectGeneral
    this.title = "";
    this.showTime = "";
    this.dateTime = ""; // Hoặc giá trị mặc định khác
    this.duration = 0;
    this.genre = "";
    this.age = 0;
}



    public Movie(String id, String name, String title, String showTime, String dateTime ,int duration, String genre, int age) {

        super(id, name);
        this.title = title;
        this.showTime = showTime;
        this.dateTime = dateTime; // Hoặc giá trị mặc định khác
        this.duration = duration;
        this.genre = genre;
        this.age = age;
    }
    public void setId(String id) {
        super.setId(id);
    }

    public String getName() {
        return super.getName();
    }
    public void setName(String name) {
        super.setName(name);
    }


    public String getTitle() {
        return title;
    }

    public String getShowTime() {
        return showTime;
    }

    public String getDateTime() {
        return dateTime;
    }

    public int getDuration() {
        return duration;
    }

    public String getGenre() {
        return genre;
    }

    public int getAge() {
        return age;
    }
    public boolean isSuitableForAge(int viewerAge) {
    return viewerAge >= age;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public void setShowTime(String showTime) {
        this.showTime = showTime;
    }
    public void setDuration(int duration) {
        this.duration = duration;
    }
    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }
    public void setGenre(String genre) {
        this.genre = genre;
    }
    public void setAge(int age) {
        this.age = age;
    }
    public String getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }

    public void display() {
        System.out.println("_____________________________");
        System.out.println("ID         : " + getId());
        System.out.println("Name       : " + getName());
        System.out.println("Movie      : " + title);
        System.out.println("ShowTime   : " + showTime);
        System.out.println("ShowDateTime   : " + dateTime);
        System.out.println("Duration   : " + duration + " minutes");
        System.out.println("Genre      : " + genre);
        System.out.println("Age Rating : " + age + "+");
        System.out.println("_____________________________");
    }


}