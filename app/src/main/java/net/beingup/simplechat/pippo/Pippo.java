package net.beingup.simplechat.pippo;

import android.content.Context;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Pippo extends process{


    private final JSONArray ar = new JSONArray();
    private final JSONObject ob = new JSONObject();


    /**
     * This is primary constructor.
     *
     * @param context Context
     *
     * If a user uses this, the keep, store and pool database will be active for action and every time when the user updates data, the binary database files will be updated in real-time.
     *
     * #Advantage
     * Do more, write less code.
     */
    public Pippo(Context context) {super(context);}

    /**
     * This is closeable constructor.
     *
     * @param context Context
     * @param isClose true for active the close() method.
     *
     * If a user uses this as <mark>isClose == true</mark>, the keep, store and pool database will be active for action but the binary database files will not be updated in real time.
     * In this case, the user needs to invoke the <mark>close()</mark> method to save all updated data.
     *
     * #Advantage
     * Work super fast!
     * -> If a user needs to store a lot number of data again and again (like a loop) then we suggest using this constructor.
     * -> After the looping work the user just invoke the <mark>close()</mark> method. It will work super fast and CPU will be minimum busy.
     */
    public Pippo(Context context, boolean isClose) {
        super(context);
        c = isClose;
    }

    /**
     * This is RAM friendly constructor.
     *
     * @param context Context
     * @param keep true for active keep database.
     * @param store true for active store database.
     * @param pool true for active pool database.
     *
     * Using this, the user can select a specific database to work with it.
     * -> Think, a user wants to work with only <b>store database</b>. Then not need to activate the <b>keep and pool database</b>.
     * -> In this case, user can create object like <mark>new Pippo(this, false, true, false);</mark>.
     *
     * #Advantage
     * Work using minimum RAM.
     */
    public Pippo(Context context, boolean keep, boolean store, boolean pool){
        super(context);
        k = keep;
        s = store;
        p = pool;
    }

    /**
     * This is RAM friendly closeable constructor.
     *
     * @param context Context
     * @param keep true for active keep database.
     * @param store true for active store database.
     * @param pool true for active pool database.
     * @param isClose true for active the close() method.
     *
     * Using this, the user can select a specific database to work with it.
     * -> Think, a user wants to work with only <b>store database</b>. Then not need to activate the <b>keep and pool database</b>.
     * -> In this case, a user can create an object like,
     * ---> new Pippo(this, false, true, false, false); [for real-time update data]
     * ---> new Pippo(this, false, true, false, true); [for super fast insert a lot number of data]
     *
     * #Advantage
     * Work using minimum RAM and less presser to CPU.
     */
    public Pippo(Context context, boolean keep, boolean store, boolean pool, boolean isClose){
        super(context);
        k = keep;
        s = store;
        p = pool;
        c = isClose;
    }

    /**
     * These methods are used for store data into keep database.
     *
     * @param-> group data group name
     * @param-> name data name
     * @param-> boo boolean data
     * @param-> num integer data
     * @param-> double_num double data
     * @param-> data string data
     * @param-> array JSONArray data
     * @param-> object JSONObject data
     *
     * @return if success true, else false.
     */
    // boolean
    public boolean keep(String group, String name, boolean boo){
        return kpd(group, name, boo, 0, 0.0, "", ar, ob, 0);
    }
    public boolean keep(String name, boolean boo){
        return kpd(ug, name, boo, 0, 0.0, "", ar, ob, 0);
    }
    public boolean keep(boolean boo){
        return kpd(ug, udn, boo, 0, 0.0, "", ar, ob, 0);
    }
    // integer
    public boolean keep(String group, String name, int num){
        return kpd(group, name, false, num, 0.0, "", ar, ob, 1);
    }
    public boolean keep(String name, int num){
        return kpd(ug, name, false, num, 0.0, "", ar, ob, 1);
    }
    public boolean keep(int num){
        return kpd(ug, udn, false, num, 0.0, "", ar, ob, 1);
    }
    // double
    public boolean keep(String group, String name, double double_num){
        return kpd(group, name, false, 0, double_num, "", ar, ob, 2);
    }
    public boolean keep(String name, double double_num){
        return kpd(ug, name, false, 0, double_num, "", ar, ob, 2);
    }
    public boolean keep(double double_num){
        return kpd(ug, udn, false, 0, double_num, "", ar, ob, 2);
    }
    // String
    public boolean keep(String group, String name, String data){
        return kpd(group, name, false, 0, 0.0, data, ar, ob, 3);
    }
    public boolean keep(String name, String data){
        return kpd(ug, name, false, 0, 0.0, data, ar, ob, 3);
    }
    public boolean keep(String data){
        return kpd(ug, udn, false, 0, 0.0, data, ar, ob, 3);
    }
    // JSONArray
    public boolean keep(String group, String name, JSONArray array){
        return kpd(group, name, false, 0, 0.0, "", array, ob, 4);
    }
    public boolean keep(String name, JSONArray array){
        return kpd(ug, name, false, 0, 0.0, "", array, ob, 4);
    }
    public boolean keep(JSONArray array){
        return kpd(ug, udn, false, 0, 0.0, "", array, ob, 4);
    }
    // JSONObject
    public boolean keep(String group, String name, JSONObject object){
        return kpd(group, name, false, 0, 0.0, "", ar, object, 5);
    }
    public boolean keep(String name, JSONObject object){
        return kpd(ug, name, false, 0, 0.0, "", ar, object, 5);
    }
    public boolean keep(JSONObject object){
        return kpd(ug, udn, false, 0, 0.0, "", ar, object, 5);
    }


    /**
     * These methods used to getting data form <i>keep database<i> when user store data without group name and data name.
     */
    public boolean getBoolean(){
        try{return kd.getJSONObject(ug).getBoolean(udn);}
        catch(JSONException e){return false;}
    }
    public int getInt(){
        try{return kd.getJSONObject(ug).getInt(udn);}
        catch(JSONException e){return di;}
    }
    public double getDouble(){
        try{return kd.getJSONObject(ug).getDouble(udn);}
        catch(JSONException e){return dd;}
    }
    public String getString(){
        try{return kd.getJSONObject(ug).getString(udn); }
        catch(JSONException e){return ds;}
    }
    public JSONArray getJSONArray(){
        try{return kd.getJSONObject(ug).getJSONArray(udn);}
        catch(JSONException e){return dja;}
    }
    public JSONObject getJSONObject(){
        try{return kd.getJSONObject(ug).getJSONObject(udn);}
        catch(JSONException e){return dj;}
    }


    /**
     * These methods used to getting data form <i>keep database</i> when user store data without group name.
     * 
     * @param name data name
     *
     * @return data from keep database.
     */
    public boolean getBoolean(String name){
        try{return kd.getJSONObject(ug).getBoolean(name);}
        catch(JSONException e){return false;}
    }
    public int getInt(String name){
        try{return kd.getJSONObject(ug).getInt(name);}
        catch(JSONException e){return di;}
    }
    public double getDouble(String name){
        try{return kd.getJSONObject(ug).getDouble(name);}
        catch(JSONException e){return dd;}
    }
    public String getString(String name){
        try{return kd.getJSONObject(ug).getString(name); }
        catch(JSONException e){return ds;}
    }
    public JSONArray getJSONArray(String name){
        try{return kd.getJSONObject(ug).getJSONArray(name);}
        catch(JSONException e){return dja;}
    }
    public JSONObject getJSONObject(String name){
        try{return kd.getJSONObject(ug).getJSONObject(name);}
        catch(JSONException e){return dj;}
    }

    /**
     * This method return all data from <i>keep database</i> as JSONObject.
     */
    public JSONObject getKeep(){return kd;}


    /**
     * These methods are used for store data into store database.
     *
     * @param-> address data storage address.
     * @param-> boo boolean data
     * @param-> num integer data
     * @param-> double_num double data
     * @param-> data string data
     * @param-> array JSONArray data
     * @param-> object JSONObject data
     * @param-> override true for override old data
     *
     * @return if success true, else false.
     */
    // boolean
    public boolean store(String address, boolean boo){
        return spd(address, boo, 0, 0.0, "", ar, ob, false, 0);
    }
    public boolean store(String address, boolean boo, boolean override){
        return spd(address, boo, 0, 0.0, "", ar, ob, override, 0);
    }
    // int
    public boolean store(String address, int num){
        return spd(address, false, num, 0.0, "", ar, ob, false, 1);
    }
    public boolean store(String address, int num, boolean override){
        return spd(address, false, num, 0.0, "", ar, ob, override, 1);
    }
    // double
    public boolean store(String address, double double_num){
        return spd(address, false, 0, double_num, "", ar, ob, false, 2);
    }
    public boolean store(String address, double double_num, boolean override){
        return spd(address, false, 0, double_num, "", ar, ob, override, 2);
    }
    // String
    public boolean store(String address, String text){
        return spd(address, false, 0, 0.0, text, ar, ob, false, 3);
    }
    public boolean store(String address, String text, boolean override){
        return spd(address, false, 0, 0.0, text, ar, ob, override, 3);
    }
    // JSONArray
    public boolean store(String address, JSONArray array){
        return spd(address, false, 0, 0.0, "", array, ob, false, 4);
    }
    public boolean store(String address, JSONArray array, boolean override){
        return spd(address, false, 0, 0.0, "", array, ob, override, 4);
    }
    // JSONObject
    public boolean store(String address, JSONObject object){
        return spd(address, false, 0, 0.0, "", ar, object, false, 5);
    }
    public boolean store(String address, JSONObject object, boolean override){
        return spd(address, false, 0, 0.0, "", ar, object, override, 5);
    }


    /**
     * These methods used to store data into pool database.
     *
     * @param-> name data name
     * @param-> boo boolean data
     * @param-> num integer data
     * @param-> double_num double data
     * @param-> data string data
     * @param-> array JSONArray data
     * @param-> object JSONObject data
     * @param-> isStabel true for <b>pool stable database</b>, false for <b>pool non stable database</b>.
     *
     * @return if success true, else false.
     */
    public boolean pool(String name, boolean boo, boolean isStable){
        if(isStable) return ips(name, boo, 0, 0.0, "", ar, ob, 0);
        return ipns(name, boo, 0, 0.0, "", ar, ob, 0);
    }
    public boolean pool(String name, int num, boolean isStable){
        if(isStable) return ips(name, false, num, 0.0, "", ar, ob, 1);
        return ipns(name, false, num, 0.0, "", ar, ob, 1);
    }
    public boolean pool(String name, double double_num, boolean isStable){
        if(isStable) return ips(name, false, 0, double_num, "", ar, ob, 2);
        return ipns(name, false, 0, double_num, "", ar, ob, 2);
    }
    public boolean pool(String name, String data, boolean isStable){
        if(isStable) return ips(name, false, 0, 0.0, data, ar, ob, 3);
        return ipns(name, false, 0, 0.0, data, ar, ob, 3);
    }
    public boolean pool(String name, JSONArray array, boolean isStable){
        if(isStable) return ips(name, false, 0, 0.0, "", array, ob, 4);
        return ipns(name, false, 0, 0.0, "", array, ob, 4);
    }
    public boolean pool(String name, JSONObject object, boolean isStable){
        if(isStable) return ips(name, false, 0, 0.0, "", ar, object, 5);
        return ipns(name, false, 0, 0.0, "", ar, object, 5);
    }


    /**
     * These methods used to update data into pool database.
     *
     * @param-> name data name
     * @param-> boo boolean data
     * @param-> num integer data
     * @param-> double_num double data
     * @param-> data string data
     * @param-> array JSONArray data
     * @param-> object JSONObject data
     * @param-> index pool data index number
     * @param-> isStabel true for <b>pool stable database</b>, false for <b>pool non stable database</b>.
     *
     * @return if success true, else false.
     */
    public boolean updatePool(String name, boolean boo, int index, boolean isStable){
        return up(boo, 0, 0.0, "", ar, ob, name, index, isStable, 0);
    }
    public boolean updatePool(String name, int num, int index, boolean isStable){
        return up(false, num, 0.0, "", ar, ob, name, index, isStable, 1);
    }
    public boolean updatePool(String name, double double_num, int index, boolean isStable){
        return up(false, 0, double_num, "", ar, ob, name, index, isStable, 2);
    }
    public boolean updatePool(String name, String data, int index, boolean isStable){
        return up(false, 0, 0.0, data, ar, ob, name, index, isStable, 3);
    }
    public boolean updatePool(String name, JSONArray array, int index, boolean isStable){
        return up(false, 0, 0.0, "", array, ob, name, index, isStable, 4);
    }
    public boolean updatePool(String name, JSONObject object, int index, boolean isStable){
        return up(false, 0, 0.0, "", ar, object, name, index, isStable, 5);
    }
}

