package cordova.plugin

import android.Manifest
import android.app.Activity
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.util.Preconditions.checkNotNull

import org.apache.cordova.*
import org.json.JSONArray
import org.json.JSONException


class MathCalculator : CordovaPlugin() {
    lateinit var context: CallbackContext

    @Throws(JSONException::class)
    override fun execute(action: String, data: JSONArray, callbackContext: CallbackContext): Boolean {
        context = callbackContext
        var result = true
        try {
            if (action == "hello") {
                hello("Selammmm")
            } else if (action == "start") {
              val activity: Activity? = currentActivity
              
              if (activity !== null) {
                ActivityCompat.requestPermissions(activity, arrayOf(Manifest.permission.CAMERA))
                
                // return startCamera(data.getInt(0), data.getInt(1), data.getInt(2), data.getInt(3), data.getString(4), data.getBoolean(5), data.getBoolean(6), data.getBoolean(7), data.getString(8), data.getBoolean(9), data.getBoolean(10), data.getBoolean(11), callbackContext);
              }
            } else {
                handleError("Invalid action")
                result = false
            }
        } catch (e: Exception) {
            handleException(e)
            result = false
        }

        return result
    }

    private fun hello(input: String) {
        val output = "Kotlin says \"$input\""
        context.success(output)
    }

    private fun handleError(errorMsg: String) {
        try {
            context.error(errorMsg)
        } catch (e: Exception) {
            context.error(errorMsg)
        }
    }

    private fun handleException(exception: Exception) {
        handleError(exception.toString())
    }

    companion object {

        protected val TAG = "MathCalculator"
    }
}
