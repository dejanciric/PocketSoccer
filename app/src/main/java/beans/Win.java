package beans;

public class Win {

    private String name1, name2;
    private int win1, win2;

    public Win(String name1, String name2, int win1, int win2) {
        this.name1 = name1;
        this.name2 = name2;
        this.win1 = win1;
        this.win2 = win2;
    }

    public void setName1(String name1) {
        this.name1 = name1;
    }

    public void setName2(String name2) {
        this.name2 = name2;
    }

    public void setWin1(int win1) {
        this.win1 = win1;
    }

    public void setWin2(int win2) {
        this.win2 = win2;
    }

    public String getName1() {

        return name1;
    }

    public String getName2() {
        return name2;
    }

    public int getWin1() {
        return win1;
    }

    public int getWin2() {
        return win2;
    }
}
