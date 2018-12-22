package beans;

import java.sql.Date;

public class Score {
    private int id;
    private String name1, name2, score;
    private int host;
    private String date;

    public Score(int id, String name1, String name2, String score, int host, String date) {
        this.id = id;
        this.name1 = name1;
        this.name2 = name2;
        this.score = score;
        this.host = host;
        this.date = date;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName1(String name1) {
        this.name1 = name1;
    }

    public void setName2(String name2) {
        this.name2 = name2;
    }

    public void setScore(String score) {
        this.score = score;
    }

    public void setHost(int host) {
        this.host = host;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getId() {
        return id;
    }

    public String getName1() {
        return name1;
    }

    public String getName2() {
        return name2;
    }

    public String getScore() {
        return score;
    }

    public int getHost() {
        return host;
    }

    public String getDate() {
        return date;
    }
}
