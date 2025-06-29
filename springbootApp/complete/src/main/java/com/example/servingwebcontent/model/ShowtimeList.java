package com.example.servingwebcontent.model;
import java.util.ArrayList;

public class ShowtimeList {
    public ArrayList<Showtime> showtimes = new ArrayList<>();

    public ArrayList<Showtime> addShowtime(Showtime showtime) {
        showtimes.add(showtime);
        return showtimes;
    }

    public ArrayList<Showtime> getEditShowtimeById(String movieId, String roomId) {
        for (int i = 0; i < showtimes.size(); i++) {
            if (showtimes.get(i).getMovieId().equals(movieId)) {
                showtimes.get(i).setRoomId(roomId);
                break;
            }
        }
        return showtimes;
    }

    public ArrayList<Showtime> getDeleteShowtime(String movieId) {
        for (int i = 0; i < showtimes.size(); i++) {
            if (showtimes.get(i).getMovieId().equals(movieId)) {
                showtimes.remove(i);
                break;
            }
        }
        return showtimes;
    }

    public void printShowtimeList() {
        for (int i = 0; i < showtimes.size(); i++) {
            System.out.println(showtimes.get(i));
        }
    }
}
