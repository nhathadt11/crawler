package study.nhatha.domain;

public class Movie {
  private long id;
  private String title;
  private int year;
  private String plot;
  private String director;
  private String stars;
  private float rating;

  public Movie() {
  }

  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public int getYear() {
    return year;
  }

  public void setYear(int year) {
    this.year = year;
  }

  public String getPlot() {
    return plot;
  }

  public void setPlot(String plot) {
    this.plot = plot;
  }

  public String getDirector() {
    return director;
  }

  public void setDirector(String director) {
    this.director = director;
  }

  public String getStars() {
    return stars;
  }

  public void setStars(String stars) {
    this.stars = stars;
  }

  public float getRating() {
    return rating;
  }

  public void setRating(float rating) {
    this.rating = rating;
  }
}
