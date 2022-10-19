package cordova.plugin

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
        callbackContext.success(output)
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