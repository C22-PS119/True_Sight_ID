package com.truesightid.utils.extension

import android.app.Activity
import android.app.AlertDialog
import android.view.View
import android.view.ViewGroup

class MyLoading {
    companion object {
        private var alertDialog: AlertDialog? = null
        fun newInstance(activity: Activity): AlertDialog {
            if (alertDialog == null) {
                alertDialog = AlertDialog.Builder(activity).create()
            }
            return alertDialog!!
        }

        private var views = ArrayList<View>()
        fun addView(view: View){
            views.add(view)
        }
        fun clearAllView(activity: Activity){
            for(view in views){
                try {
                    activity.windowManager.removeView(view)
                }catch (ex: Exception){}
                views.remove(view)
            }
        }
    }
}