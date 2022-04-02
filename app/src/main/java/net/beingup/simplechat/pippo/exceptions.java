package net.beingup.simplechat.pippo;

import org.json.JSONException;

import java.io.FileNotFoundException;
import java.io.IOException;

public interface exceptions {
    /**
     * This varibales store exceptions message text data.
     */
    // message for FileNotFound
    String ms101 = " not found on assert folder.";
    // message for IO
    String ms201 = "Android OS can not create binary files for the database.";
    String ms202 = "Java IO system do not work properly when the system tries to read database binary files.";
    String ms203 = "Java IO system do not work properly when the system tries to write database binary files.";
    String ms204 = "Java IO system do not work properly when the system tries to read assert .json files.";
    String ms205 = "Java IO system do not work properly when the system tries to export .json files.";
    // message for JSON
    String ms301 = "Unreadable database binary files. It may be corrupted.";
    String ms302 = " is not a pure json file.";
    String ms303 = "Pool default structure can not create right now.";


    /**
     * @param i close number
     * @param e exception
     * @param i1 message number
     * @param s text message
     *
     * These methods give exceptions and other info.
     */
    void FileNotFoundException(int i, FileNotFoundException e, int i1, String s);
    void IOException(int i, IOException e, int i1, String s);
    void JSONException(int i, JSONException e, int i1, String s);
}
