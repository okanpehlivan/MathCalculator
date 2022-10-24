package cordova.plugin

import android.Manifest
import android.app.FragmentManager
import android.app.FragmentTransaction
import android.util.Log
import android.util.TypedValue
import android.view.View
import android.view.ViewGroup
import android.view.ViewParent
import android.widget.FrameLayout
import org.apache.cordova.*
import org.json.JSONArray
import org.json.JSONException
import org.apache.cordova.CallbackContext


class MathCalculator : CordovaPlugin(), CameraActivity.CameraPreviewListener {
  lateinit var context: CallbackContext
  private var execCallback: CallbackContext? = null
  private var execArgs: JSONArray? = null
  private val CAM_REQ_CODE = 0
  private var fragment: CameraActivity? = null
  private var startCameraCallbackContext: CallbackContext? = null
  private val containerViewId = 20 //<- set to random number to prevent conflict with other plugins
  private var webViewParent: ViewParent? = null


  private val permissions = arrayOf(
    Manifest.permission.CAMERA
  )

    @Throws(JSONException::class)
    override fun execute(action: String, data: JSONArray, callbackContext: CallbackContext): Boolean {
        context = callbackContext
        var result = true
        try {
            if (action == "hello") {
                hello("Selammmm")
            } else if (action == "start") {
              if (cordova.hasPermission(permissions[0])) {
                return start(data.getInt(0), data.getInt(1), data.getInt(2), data.getInt(3), data.getString(4), data.getBoolean(5), data.getBoolean(6), data.getBoolean(7), data.getString(8), data.getBoolean(9), data.getBoolean(10), data.getBoolean(11), callbackContext);
              } else {
                this.execCallback = callbackContext;
                this.execArgs = data;
                cordova.requestPermissions(this, CAM_REQ_CODE, permissions);
                return true;
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

  private fun start(
    x: Int,
    y: Int,
    width: Int,
    height: Int,
    defaultCamera: String,
    tapToTakePicture: Boolean,
    dragEnabled: Boolean,
    toBack: Boolean,
    alpha: String,
    tapFocus: Boolean,
    disableExifHeaderStripping: Boolean,
    storeToFile: Boolean,
    callbackContext: CallbackContext
  ): Boolean {
    Log.d(TAG, "start camera action")
    if (fragment != null) {
      callbackContext.error("Camera already started")
      return true
    }
    val opacity = alpha.toFloat()
    fragment = CameraActivity()
    fragment.setEventListener(this)
    fragment.defaultCamera = defaultCamera
    fragment.tapToTakePicture = tapToTakePicture
    fragment.dragEnabled = dragEnabled
    fragment.tapToFocus = tapFocus
    fragment.disableExifHeaderStripping = disableExifHeaderStripping
    fragment.storeToFile = storeToFile
    fragment.toBack = toBack
    val metrics = cordova.activity.resources.displayMetrics

    // offset
    val computedX =
      TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, x.toFloat(), metrics).toInt()
    val computedY =
      TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, y.toFloat(), metrics).toInt()

    // size
    val computedWidth =
      TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, width.toFloat(), metrics).toInt()
    val computedHeight =
      TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, height.toFloat(), metrics).toInt()
    fragment.setRect(computedX, computedY, computedWidth, computedHeight)
    startCameraCallbackContext = callbackContext
    cordova.activity.runOnUiThread { //create or update the layout params for the container view
      var containerView =
        cordova.activity.findViewById<View>(containerViewId) as FrameLayout
      if (containerView == null) {
        containerView = FrameLayout(cordova.activity.applicationContext)
        containerView.id = containerViewId
        val containerLayoutParams = FrameLayout.LayoutParams(
          FrameLayout.LayoutParams.MATCH_PARENT,
          FrameLayout.LayoutParams.MATCH_PARENT
        )
        cordova.activity.addContentView(containerView, containerLayoutParams)
      }

      //display camera below the webview
      if (toBack) {
        val view: View = webView.view
        val rootParent = containerView.parent
        var curParent: ViewParent = view.getParent()
        view.setBackgroundColor(0x00000000)

        // If parents do not match look for.
        if (curParent.parent !== rootParent) {
          while (curParent != null && curParent.parent !== rootParent) {
            curParent = curParent.parent
          }
          if (curParent != null) {
            (curParent as ViewGroup).setBackgroundColor(0x00000000)
            curParent.bringToFront()
          } else {
            // Do default...
            curParent = view.getParent()
            webViewParent = curParent
            (view as ViewGroup).bringToFront()
          }
        } else {
          // Default
          webViewParent = curParent
          rootParent.bringChildToFront(webViewParent as View?)
        }
      } else {
        //set camera back to front
        containerView.alpha = opacity
        containerView.bringToFront()
      }

      //add the fragment to the container
      val fragmentManager: FragmentManager = cordova.activity.fragmentManager
      val fragmentTransaction: FragmentTransaction = fragmentManager.beginTransaction()
      fragmentTransaction.add(containerView.id, fragment)
      fragmentTransaction.commit()
    }
    return true
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
