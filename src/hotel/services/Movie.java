package hotel.services;

public class Movie {
    private int movieID;
    private String movieName;
    private String movieDescription;
    private int moviePrice;
    private int movieSeen;

    public Movie(int movieID, String movieName, String movieDescription, int moviePrice, int movieSeen) {
        this.movieID = movieID;
        this.movieName = movieName;
        this.movieDescription = movieDescription;
        this.moviePrice = moviePrice;
        this.movieSeen = movieSeen;
    }

    @Override
    public String toString() {
        double tmpPrice = Double.parseDouble(String.valueOf(moviePrice));
        return  /*movieID+" "+*/movieName + " -- " + String.format("%1.2f €", tmpPrice/100);
    }

    public int getMovieID() {
        return movieID;
    }

    public void setMovieID(int movieID) {
        this.movieID = movieID;
    }

    public String getMovieName() {return movieName;}

    public void setMovieName(String movieName) {
        this.movieName = movieName;
    }

    public String getMovieDescription() {
        return movieDescription;
    }

    public int getMoviePrice() {
        return moviePrice;
    }

}
