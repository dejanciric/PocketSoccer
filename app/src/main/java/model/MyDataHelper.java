package model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import java.util.ArrayList;

import beans.Score;
import beans.Win;

public class MyDataHelper extends SQLiteOpenHelper {
    private static final String DB_NAME = "pocketsoccer.db";
    private static final String TABLE_NAME_WINS = "wins";
    private static final String TABLE_NAME_SCORES = "scores";

    public MyDataHelper(Context context) {
        super(context, DB_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table if not exists "+TABLE_NAME_WINS+" (name1 text not null, name2 text not null, win1 integer, win2 integer, primary key (name1, name2))");
        db.execSQL("create table if not exists "+TABLE_NAME_SCORES+" (id integer primary key autoincrement, name1 text, name2 text, score text, host integer, date text, FOREIGN KEY(name1,name2) REFERENCES "+TABLE_NAME_WINS +" (name1, name2) on update no action on delete cascade)");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists "+ TABLE_NAME_WINS);
        db.execSQL("drop table if exists "+ TABLE_NAME_SCORES);
        onCreate(db);
    }
    // update win for one row or insert new if not exists, and return which player is first in primary key
    // for add Score to be consistent
    public int addWin(String player1, String player2, int player){
        if (doesExists(player1, player2)){
            updateWin(player1, player2, player);
            return 1;
        }else if (doesExists(player2, player1)){
            if (player == 1){
                updateWin(player2, player1, 2);
            }else if (player == 2){
                updateWin(player2, player1, 1);
            }
            return 2;
        }else{
            SQLiteDatabase db = getWritableDatabase();
            ContentValues cv = new ContentValues();
            cv.put("name1", player1);
            cv.put("name2", player2);
            if (player == 1){
                cv.put("win1", 1);
                cv.put("win2", 0);
            }else if (player == 2){
                cv.put("win1", 0);
                cv.put("win2", 1);
            }else{
                cv.put("win1", 0);
                cv.put("win2", 0);
            }

            db.insert(TABLE_NAME_WINS, null, cv);
            return 1;
        }
    }

    public void updateWin(String player1, String player2, int player){
        if (player == 0 ) return;
        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = new ContentValues();
        int val = getNumOfWin(player1, player2, player);
        val++;
        if (player == 1){
            cv.put("win1", val);
        }else if (player == 2){
            cv.put("win2", val);
        }

        db.update(TABLE_NAME_WINS, cv, " name1 = ? and name2 = ?", new String[]{player1, player2});
    }

    public boolean doesExists(String player1, String player2){
        SQLiteDatabase db = getWritableDatabase();
        Cursor c = db.rawQuery("select * from "+TABLE_NAME_WINS + " where name1 = '"+player1+ "' and name2= '"+player2+"'", null);
        if (c.moveToNext()) {
            return  true;
        }
        return false;
    }

    public int getNumOfWin(String player1, String player2, int player){
        SQLiteDatabase db = getWritableDatabase();
        Cursor c = db.rawQuery("select * from "+TABLE_NAME_WINS + " where name1 = '"+player1+ "' and name2= '"+player2+"'", null);
        if (c.moveToNext()) {
            if (player == 1){
                return c.getInt(c.getColumnIndex("win1"));
            }
            else{
                return c.getInt(c.getColumnIndex("win2"));
            }
        }
        return -1;
    }

    public void addScore(String player1, String player2, String score, int winner){
        int first = addWin(player1, player2, winner);
        if (first == 1){
            insertScore(player1, player2, score, 1);
        }else if (first == 2){
            insertScore(player2, player1, score, 2);
        }
    }

    public void insertScore(String player1, String player2, String score, int host) {
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("insert into "+TABLE_NAME_SCORES+" (name1, name2, score, host, date) values ( '"+player1 +"','"+player2 +"','"+score +"',"+host+", datetime('now', 'localtime'))");
        /*ContentValues cv = new ContentValues();
        cv.put("name1", player1);
        cv.put("name2", player2);
        cv.put("score", score);
        cv.put("host", host);
        cv.put("date", datetime('now', 'localtime'));
        db.insert(TABLE_NAME_SCORES, null, cv);*/
    }

    public ArrayList<Win> getAllWins(){
        ArrayList<Win> wins = new ArrayList<Win>();
        SQLiteDatabase db = getWritableDatabase();
        Cursor c = db.rawQuery("select * from "+TABLE_NAME_WINS, null);
        while (c.moveToNext()) {
            String name1 = c.getString(c.getColumnIndex("name1"));
            String name2 = c.getString(c.getColumnIndex("name2"));
            int win1 = c.getInt(c.getColumnIndex("win1"));
            int win2 = c.getInt(c.getColumnIndex("win2"));

            wins.add(new Win(name1, name2, win1, win2));
        }
        return wins;
    }

    public ArrayList<Score> getAllScores(String player1, String player2){
        ArrayList<Score> scores = new ArrayList<Score>();
        SQLiteDatabase db = getWritableDatabase();
        Cursor c = null;
        if (doesExists(player1, player2)){
            c = db.rawQuery("select * from "+TABLE_NAME_SCORES+" where name1 = '"+player1+"' and name2 ='"+player2+"'", null);
        }else if (doesExists(player2, player1)){
            c = db.rawQuery("select * from "+TABLE_NAME_SCORES+" where name1 = '"+player2+"' and name2 ='"+player1+"'", null);
        }

        while (c.moveToNext()) {
            int id = c.getInt(c.getColumnIndex("id"));
            String name1 = c.getString(c.getColumnIndex("name1"));
            String name2 = c.getString(c.getColumnIndex("name2"));
            String score = c.getString(c.getColumnIndex("score"));
            int host = c.getInt(c.getColumnIndex("host"));
            String date = c.getString(c.getColumnIndex("date"));
            scores.add(new Score(id,name1, name2, score,host,date));

        }
        return scores;
    }

    public int[] getNumWinOfPlayers(String player1, String player2){
        SQLiteDatabase db = getWritableDatabase();
        int[] nums = new int[2];
        Cursor c = db.rawQuery("select * from "+TABLE_NAME_WINS + " where name1 = '"+player1+ "' and name2= '"+player2+"'", null);
        if (c.moveToNext()) {
            nums[0] = c.getInt(c.getColumnIndex("win1"));
            nums[1] = c.getInt(c.getColumnIndex("win2"));
        }else{
            c = db.rawQuery("select * from "+TABLE_NAME_WINS + " where name1 = '"+player2+ "' and name2= '"+player1+"'", null);
            if (c.moveToNext()){
                nums[0] = c.getInt(c.getColumnIndex("win2"));
                nums[1] = c.getInt(c.getColumnIndex("win1"));
            }
        }
        return nums;
    }

    public void deleteAllWins(){
        SQLiteDatabase db = getWritableDatabase();
        db.delete(TABLE_NAME_WINS,null, null);
    }

    public void deleteAllScores(String player1, String player2){
        SQLiteDatabase db = getWritableDatabase();
        db.delete(TABLE_NAME_SCORES,"(name1 = ? and name2 = ?) or (name1 = ? and name2 = ?)", new String[]{ player1, player2, player2, player1});
    }

    public void deleteAllWinsForPlayers(String player1, String player2){
        SQLiteDatabase db = getWritableDatabase();
        db.delete(TABLE_NAME_WINS,"(name1 = ? and name2 = ?) or (name1 = ? and name2 = ?)", new String[]{ player1, player2, player2, player1});
    }

    public void deleteAllScoresForReal(){
        SQLiteDatabase db = getWritableDatabase();
        db.delete(TABLE_NAME_SCORES,null,null);

    }


}
