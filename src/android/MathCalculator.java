package cordova.plugin.mathcalculator;

import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CallbackContext;
import java.util.Locale;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MathCalculator extends CordovaPlugin {

    @Override
    public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {
        if (action.equals("add")) {
            this.add(args, callbackContext);

            return true;
        } else if (action.equals("substract")) {
            this.substract(args, callbackContext);

            return true;
        } else if (action.equals("getLocationPermission")) {
            this.getLocationPermission(args, callbackContext);

            return true;
        }
        return false;
    }

    private void getLocationPermission(String action, CallbackContext callback) {
        try {
            String result = Locale.getDefault().getCountry();
            callback.success(""+ result );
        } catch (Exception ex) {
            callback.error("Location permission error");
        }

        // try {
        //     ActivityResultLauncher<String[]> locationPermissionRequest =
        //             registerForActivityResult(new ActivityResultContracts
        //                             .RequestMultiplePermissions(), result -> {
        //                         Boolean fineLocationGranted = result.getOrDefault(
        //                                 Manifest.permission.ACCESS_FINE_LOCATION, false);
        //                         Boolean coarseLocationGranted = result.getOrDefault(
        //                                 Manifest.permission.ACCESS_COARSE_LOCATION,false);

        //                         if (fineLocationGranted != null && fineLocationGranted) {
        //                             // Precise location access granted.
        //                             callback.success(true);
        //                         } else if (coarseLocationGranted != null && coarseLocationGranted) {
        //                             // Only approximate location access granted.
        //                             callback.success(true);
        //                         } else {
        //                             // No location access granted.
        //                             callback.success(false);
        //                         }
        //                     }
        //             );

        //     locationPermissionRequest.launch(new String[] {
        //             Manifest.permission.ACCESS_FINE_LOCATION,
        //             Manifest.permission.ACCESS_COARSE_LOCATION
        //     });
        // } catch (Exception ex) {
        //     callback.error("Konum servisleri çalışırken bir hata verdi");
        // }
    }

    private void add(JSONArray args, CallbackContext callback) {
        if (args != null) {
            try {
                int p1 = Integer.parseInt(args.getJSONObject(0).getString("param1"));
                int p2 = Integer.parseInt(args.getJSONObject(0).getString("param2"));

                callback.success(""+ (p1+p2) );

            } catch (Exception ex) {
                callback.error("Something went wrong " + ex);
            }
        } else {
            callback.error("Please dont pass null value");
        }
    }

    private void substract(JSONArray args, CallbackContext callback) {
        if (args != null) {
            try {
                int p1 = Integer.parseInt(args.getJSONObject(0).getString("param1"));
                int p2 = Integer.parseInt(args.getJSONObject(0).getString("param2"));

                callback.success(""+ (p1-p2) );

            } catch (Exception ex) {
                callback.error("Something went wrong " + ex);
            }
        } else {
            callback.error("Please dont pass null value");
        }
    }
}