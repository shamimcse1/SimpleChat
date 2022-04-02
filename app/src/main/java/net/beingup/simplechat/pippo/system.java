package net.beingup.simplechat.pippo;

import android.content.Context;
import android.content.ContextWrapper;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;



class system implements exceptions{
    Context context;

    /**
     * kj -> keep json file name.
     * kd -> keep binary file name.
     * sj -> store json file name.
     * sd -> store binary file name.
     * pj -> pool json file name.
     * pd -> pool binary file name.
     *
     * These variables store file name for database binary files and assert folder JSON files.
     */
    private final String kj = "pippo_keep.json", kd = "keep.bin";
    private final String sj = "pippo_store.json", sd = "store.bin";
    private final String pj = "pippo_pool.json", pd = "pool.bin";


    /**
     * st1 -> default json data for keep and store database.
     * st2 -> default json data for pool database.
     *
     * At the first time when user will create Pippo object, the system will be created <b>kd</b>, <b>sd</b> and <b>pd</b> files with these default data.
     */
    private final String st1 = "{}", st2 = "{\"stable\":{},\"non_stable\":{}}";


    /**
     * This variable store data as a close request.
     * It will be updated when the user invokes <mark>close(int)</mark> method.
     * The system will return this value with exceptions.
     */
    private int cn = 0;


    // constructor
    system(Context context){this.context = context;}


    /**
     * This is a setter of <b>cn</b> variable.
     */
    void scn(int cn){this.cn = cn;}


    /**
     * This method used for reading database binary files from the .android folder.
     *
     * @param i database define number
     *
     * @return target database as JSONObject
     */
    public JSONObject rdf(int i){
        ContextWrapper cw = new ContextWrapper(context);
        File dc = cw.getDir("pippo", Context.MODE_PRIVATE);
        File f;
        switch (i){
            case 0: f = new File(dc, kd); break;
            case 1: f = new File(dc, sd); break;
            default: f = new File(dc, pd);
        }
        int l = (int) f.length();
        byte[] bytes = new byte[l];
        try {
            FileInputStream fns = new FileInputStream(f);
            try {
                fns.read(bytes);
                try{return new JSONObject(new String(bytes));}
                catch (JSONException e){
                    JSONException(cn, e, 301, ms301);
                    return new JSONObject();
                }
            } finally {
                fns.close();
            }
        }catch (FileNotFoundException e){
            cf(i);
            // create default pool structure
            if(i==2){
                try{return new JSONObject(st2);}
                catch (JSONException e1){JSONException(cn, e1, 303, ms303);}
            }
            return new JSONObject();
        }
        catch (IOException e){
            IOException(cn, e, 202, ms202);
            return new JSONObject();
        }
    }


    /**
     * This method used for write updated data into the target database binary file.
     *
     * @param i database define number
     * @param ob updated data for target database
     *
     * @return if success returns true, else return false and invoke exceptions.
     */
    boolean wdf(int i, JSONObject ob){
        ContextWrapper cw = new ContextWrapper(context);
        File dc = cw.getDir("pippo", Context.MODE_PRIVATE);
        File f;
        switch (i){
            case 0: f = new File(dc, kd); break;
            case 1: f = new File(dc, sd); break;
            default: f = new File(dc, pd);
        }
        try {
            FileOutputStream s = new FileOutputStream(f);
            s.write(ob.toString().getBytes());
            s.close();
            return true;
        }
        catch (IOException e) {
            IOException(cn, e, 203, ms203);
            return false;
        }
    }


    /**
     * This method used to creating binary files for the databases.
     *
     * @param i database json file define number
     *
     * @return if success returns true, else return false and invoke exception.
     */
    private boolean cf(int i){
        ContextWrapper cw = new ContextWrapper(context);
        File dc = cw.getDir("pippo", Context.MODE_PRIVATE);
        File f;
        switch(i){
            case 0: f = new File(dc, kd); break;
            case 1: f = new File(dc, sd); break;
            default: f = new File(dc, pd);
        }
        try {
            FileOutputStream s = new FileOutputStream(f);
            switch(i){
                case 0:
                case 1:
                    s.write(st1.getBytes());
                    break;
                case 2: s.write(st2.getBytes());
            }
            s.close();
            return true;
        }
        catch (IOException e) {
            IOException(cn, e, 201, ms201);
            return false;
        }
    }

    @Override
    public void FileNotFoundException(int i, FileNotFoundException e, int i1, String s) {}

    @Override
    public void IOException(int i, IOException e, int i1, String s) {}

    @Override
    public void JSONException(int i, JSONException e, int i1, String s) {}
}

