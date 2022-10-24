package cordova.plugin

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.pm.PackageManager;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.hardware.Camera;
import android.os.Handler;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraManager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.Size;
import android.util.SizeF;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.FrameLayout;

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
              if (cordova.hasPermission(permissions[0])) {
                return startCamera(data.getInt(0), data.getInt(1), data.getInt(2), data.getInt(3), data.getString(4), data.getBoolean(5), data.getBoolean(6), data.getBoolean(7), data.getString(8), data.getBoolean(9), data.getBoolean(10), data.getBoolean(11), callbackContext);
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
