package net.beingup.simplechat.HelperClasses

import org.json.JSONArray

class ArrayAndList {
    /**
     * This function return true if 's' element is found into 'a' JSONArray.
     */
    fun isFound(s: String, a: JSONArray): Boolean{
        if (a.length()<1) return false
        for(x in 0..(a.length()-1)){
            if (s == a.getString(x)) return true
        }
        return false
    }
}