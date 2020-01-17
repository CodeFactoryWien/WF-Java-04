package hotel.services;

public class Movie {
    private int movieID;
    private String movieName;
    private String movieDescription;
    private double moviePrice;
    private int movieSeen;

    public Movie(int movieID, String movieName, String movieDescription, double moviePrice, int movieSeen) {
        this.movieID = movieID;
        this.movieName = movieName;
        this.movieDescription = movieDescription;
        this.moviePrice = moviePrice;
        this.movieSeen = movieSeen;
    }

    @Override
    public String toString() {
        return  movieName + " -- " + moviePrice + "€";
    }

    public int getMovieID() {
        return movieID;
    }

    public void setMovieID(int movieID) {
        this.movieID = movieID;
    }

    public String getMovieName() {
        return movieName;
    }

    public void setMovieName(String movieName) {
        this.movieName = movieName;
    }

    public String getMovieDescription() {
        return movieDescription;
    }

    public void setMovieDescription(String movieDescription) {
        this.movieDescription = movieDescription;
    }

    public double getMoviePrice() {
        return moviePrice;
    }

    public void setMoviePrice(double moviePrice) {
        this.moviePrice = moviePrice;
    }

    public int getMovieSeen() {
        return movieSeen;
    }

    public void setMovieSeen(int movieSeen) {
        this.movieSeen = movieSeen;
    }
}
