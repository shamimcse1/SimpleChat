package net.beingup.simplechat.HelperClasses

import android.app.ActivityManager
import android.content.Context


class ServiceControler (val con: Context) {

    /**
     * This function return true if 'serviceClass' is running in background.
     */
    fun isServiceRunning(serviceClass: Class<*>): Boolean {
        val manager = con.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        for (service in manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.name == service.service.className)
                return true
        }
        return false
    }
}