package cordova.plugin

import org.apache.cordova.*
import org.json.JSONArray
import org.json.JSONException
import android.util.Log

class MathCalculator : CordovaPlugin() {
    lateinit var context: CallbackContext

    @Throws(JSONException::class)
    override fun execute(action: String, data: JSONArray, callbackContext: CallbackContext): Boolean {
        context = callbackContext
        var result = true
        try {
            if (action == "hello") {
                val input = data.getString(0)
                val output = "Kotlin says \"$input\""
                callbackContext.success(output)
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

    private fun handleError(errorMsg: String) {
        try {
            Log.e(TAG, errorMsg)
            context.error(errorMsg)
        } catch (e: Exception) {
            Log.e(TAG, e.toString())
        }

    }

    private fun handleException(exception: Exception) {
        handleError(exception.toString())
    }

    companion object {

        protected val TAG = "MathCalculator"
    }
}