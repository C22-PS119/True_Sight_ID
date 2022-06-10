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
    }
}