package cs301.Soccer;

import android.util.Log;
import cs301.Soccer.soccerPlayer.SoccerPlayer;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.*;

/**
 * Soccer player database -- presently, all dummied up
 *
 * @author Alexx Blake
 * @version 3/16/23
 *
 */
public class SoccerDatabase implements SoccerDB {

    // dummied up variable; you will need to change this
    private Hashtable<String, SoccerPlayer> database = new Hashtable<>();

    /**
     * add a player
     *
     * @see SoccerDB#addPlayer(String, String, int, String)
     */
    @Override
    public boolean addPlayer(String firstName, String lastName,
                             int uniformNumber, String teamName) {

        String name = makeKey(firstName, lastName);

        if (database.containsKey(name)){
            return false;
        }
        else {
            SoccerPlayer player = new SoccerPlayer(firstName, lastName, uniformNumber, teamName);
            database.put(name, player);
            return true;
        }
    }

    public String makeKey(String firstName, String lastName){
        String fullName = firstName + " " + lastName;
        return fullName;
    }

    /**
     * remove a player
     *
     * @see SoccerDB#removePlayer(String, String)
     */
    @Override
    public boolean removePlayer(String firstName, String lastName) {

        String name = makeKey(firstName, lastName);

        if (database.containsKey(name)){
            database.remove(name);
            return true;
        }
        else {
            return false;
        }
    }

    /**
     * look up a player
     *
     * @see SoccerDB#getPlayer(String, String)
     */
    @Override
    public SoccerPlayer getPlayer(String firstName, String lastName) {

        String name = makeKey(firstName, lastName);
        if(database.containsKey(name)){
            return database.get(name);
        }
        else {
            return null;
        }
    }

    /**
     * increment a player's goals
     *
     * @see SoccerDB#bumpGoals(String, String)
     */
    @Override
    public boolean bumpGoals(String firstName, String lastName) {

        String name = makeKey(firstName, lastName);
        if (database.containsKey(name)){
            database.get(name).bumpGoals();
            return true;
        }
        else {
            return false;
        }
    }

    /**
     * increment a player's yellow cards
     *
     * @see SoccerDB#bumpYellowCards(String, String)
     */
    @Override
    public boolean bumpYellowCards(String firstName, String lastName) {

        String name = makeKey(firstName, lastName);
        if (database.containsKey(name)){
            database.get(name).bumpYellowCards();
            return true;
        }
        else {
            return false;
        }
    }

    /**
     * increment a player's red cards
     *
     * @see SoccerDB#bumpRedCards(String, String)
     */
    @Override
    public boolean bumpRedCards(String firstName, String lastName) {

        String name = makeKey(firstName, lastName);
        if (database.containsKey(name)){
            database.get(name).bumpRedCards();
            return true;
        }
        else {
            return false;
        }
    }

    /**
     * tells the number of players on a given team
     *
     * @see SoccerDB#numPlayers(String)
     */
    @Override
    // report number of players on a given team (or all players, if null)
    public int numPlayers(String teamName) {

        int count = 0;

        if(teamName == null){
            return database.size();
        }
        else {
            Set<String> setOfPlayers = database.keySet();
            Iterator<String> itr = setOfPlayers.iterator();
            while (itr.hasNext()){
                String key = itr.next();
                if (database.get(key).getTeamName().equals(teamName)){
                    count++;
                }
            }
            return count;
        }
    }

    /**
     * gives the nth player on a the given team
     *
     * @see SoccerDB#playerIndex(int, String)
     */
    // get the nTH player
    @Override
    public SoccerPlayer playerIndex(int idx, String teamName) {

        int count = 0;

        Set<String> setOfPlayers = database.keySet();
        Iterator<String> itr = setOfPlayers.iterator();
        while (itr.hasNext()){
            String key = itr.next();
            if (teamName == null || database.get(key).getTeamName().equals(teamName)){
                if (idx == count) {
                    return database.get(key);
                }
                count++;
            }
        }
        return null;
    }

    /**
     * reads database data from a file
     *
     * @see SoccerDB#readData(java.io.File)
     */
    // read data from file
    @Override
    public boolean readData(File file) {

        try {
            Scanner scanner = new Scanner(System.in);
            scanner = new Scanner(file);

            while (scanner.hasNextLine()){
                String firstName = scanner.nextLine();
                String lastName = scanner.nextLine();
                String teamName = scanner.nextLine();
                int uniformNumber = Integer.parseInt(scanner.nextLine());
                SoccerPlayer player = new SoccerPlayer(firstName, lastName, uniformNumber, teamName);
                int goals = Integer.parseInt(scanner.nextLine());
                for (int i = 0; i < goals; i++) {
                    player.bumpGoals();
                }
                int redCards = Integer.parseInt(scanner.nextLine());
                for (int i = 0; i < goals; i++) {
                    player.bumpRedCards();
                }
                int yellowCards = Integer.parseInt(scanner.nextLine());
                for (int i = 0; i < goals; i++) {
                    player.bumpYellowCards();
                }
            }
            scanner.close();
            return true;
        }
        catch (IOException e) {
            return false;
        }
    }

    /**
     * write database data to a file
     *
     * @see SoccerDB#writeData(java.io.File)
     */
    // write data to file
    @Override
    public boolean writeData(File file) {

        try {
            FileWriter fileWriter = new FileWriter(file);
            PrintWriter pw = new PrintWriter(fileWriter);
            Set<String> setOfPlayers = database.keySet();
            Iterator<String> itr = setOfPlayers.iterator();
            while (itr.hasNext()) {
                String key = itr.next();
                pw.println(logString(database.get(key).getFirstName()));
                pw.println(logString(database.get(key).getLastName()));
                pw.println(logString(database.get(key).getTeamName()));
                pw.println(logString(Integer.toString(database.get(key).getUniform())));
                pw.println(logString(Integer.toString(database.get(key).getGoals())));
                pw.println(logString(Integer.toString(database.get(key).getRedCards())));
                pw.println(logString(Integer.toString(database.get(key).getYellowCards())));
            }
            pw.close();
            return true;
        }
        catch (IOException e){
            return false;
        }
    }

    /**
     * helper method that logcat-logs a string, and then returns the string.
     * @param s the string to log
     * @return the string s, unchanged
     */
    private String logString(String s) {
        Log.i("write string", s);
        return s;
    }

    /**
     * returns the list of team names in the database
     *
     * @see cs301.Soccer.SoccerDB#getTeams()
     */
    // return list of teams
    @Override
    public HashSet<String> getTeams() {
        return new HashSet<String>();
    }

    /**
     * Helper method to empty the database and the list of teams in the spinner;
     * this is faster than restarting the app
     */
    public boolean clear() {
        if(database != null) {
            database.clear();
            return true;
        }
        return false;
    }
}
