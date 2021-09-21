package com.eorlov.weatherapplication.base

import android.util.Log
import android.view.WindowManager
import androidx.fragment.app.Fragment

abstract class BaseFragment : Fragment() {
    protected fun lockUI() {
        activity?.window?.addFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
    }

    protected fun unlockUI() {
        activity?.window?.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
    }


    /**
     * Prints action logs.
     *
     * @param action name of the action to log
     */
    protected fun printLog(action: String) {
        Log.d("BaseFragment", "${javaClass.simpleName} $action")
    }
}