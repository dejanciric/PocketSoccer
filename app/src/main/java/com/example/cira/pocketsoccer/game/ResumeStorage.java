package com.example.cira.pocketsoccer.game;

public class ResumeStorage {
    public float[] x, y, dx, dy;

    public int state1, state2;

    public String player1Name, player2Name;
    public int score1, score2;

    public String rule;
    public String goals;
    public String time;

    public int timeLeftForMove;
    public int field;
    public int gameSpeed;
    public String turn;
    public int computer;
    public boolean goal;

    public ResumeStorage(float[] x, float[] y, float[] dx, float[] dy, int state1, int state2, String player1Name, String player2Name, int score1, int score2, String rule, String goals, String time, int timeLeftForMove, int field, int gameSpeed, String turn, int computer, boolean goal) {
        this.x = x;
        this.y = y;
        this.dx = dx;
        this.dy = dy;
        this.state1 = state1;
        this.state2 = state2;
        this.player1Name = player1Name;
        this.player2Name = player2Name;
        this.score1 = score1;
        this.score2 = score2;
        this.rule = rule;
        this.goals = goals;
        this.time = time;
        this.timeLeftForMove = timeLeftForMove;
        this.field = field;
        this.gameSpeed = gameSpeed;
        this.turn = turn;
        this.computer = computer;
        this.goal = goal;
    }
}
