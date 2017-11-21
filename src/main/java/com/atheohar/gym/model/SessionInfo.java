package com.atheohar.gym.model;

public class SessionInfo {
  private String date;
  private String title;

  public SessionInfo(String date, String title) {
    super();
    this.date = date;
    this.title = title;
  }

  public String getDate() {
    return date;
  }

  public void setDate(String date) {
    this.date = date;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  @Override
  public String toString() {
    return "SessionInfo [date=" + date + ", title=" + title + "]";
  }

}
