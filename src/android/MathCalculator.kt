package cordova.plugin

import org.apache.cordova.*
import org.json.JSONArray
import org.json.JSONException
import android.view.ViewGroup

import android.view.ViewParent

import android.widget.FrameLayout

import android.util.TypedValue

import android.util.DisplayMetrics
import androidx.core.app.ActivityCompat.requestPermissions
import androidx.fragment.app.FragmentManager


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
              if (cordova.hasPermission(permissions[0])) {
                return startCamera(args.getInt(0), args.getInt(1), args.getInt(2), args.getInt(3), args.getString(4), args.getBoolean(5), args.getBoolean(6), args.getBoolean(7), args.getString(8), args.getBoolean(9), args.getBoolean(10), args.getBoolean(11), callbackContext);
              } else {
                this.execCallback = callbackContext;
                this.execArgs = args;
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

  private fun startCamera(
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
    val metrics: DisplayMetrics = cordova.getActivity().getResources().getDisplayMetrics()

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
    cordova.getActivity()
      .runOnUiThread(Runnable { //create or update the layout params for the container view
        var containerView = cordova.getActivity().findViewById(containerViewId) as FrameLayout
        if (containerView == null) {
          containerView = FrameLayout(cordova.getActivity().getApplicationContext())
          containerView.id = containerViewId
          val containerLayoutParams = FrameLayout.LayoutParams(
            FrameLayout.LayoutParams.MATCH_PARENT,
            FrameLayout.LayoutParams.MATCH_PARENT
          )
          cordova.getActivity().addContentView(containerView, containerLayoutParams)
        }

        //display camera below the webview
        if (toBack) {
          val view: View = webView.getView()
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
        val fragmentManager: FragmentManager = cordova.getActivity().getFragmentManager()
        val fragmentTransaction: FragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.add(containerView.id, fragment)
        fragmentTransaction.commit()
      })
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
