import CoreLocation

@objc(MathCalculator) class MathCalculator : CDVPlugin, NSObject, CLLocationManagerDelegate {

    let manager: CLLocationManager

    // MARK: Properties
    var pluginResult = CDVPluginResult(status: CDVCommandStatus_ERROR)

    //This method is called when the plugin is initialized; plugin setup methods got here
    override func pluginInitialize() {
        manager = CLLocationManager
        super.pluginInitialize()
        manager.delegate = self
        manager.distanceFilter = kCLDistanceFilterNone
        manager.desiredAccuracy = kCLLocationAccuracyBest
    }

    @objc(add:) func add(_ command: CDVInvokedUrlCommand) {
        var pluginResult = CDVPluginResult(status: CDVCommandStatus_ERROR)
        let param1 = (command.arguments[0] as? NSObject)?.value(forKey: "param1") as? Int
        let param2 = (command.arguments[0] as? NSObject)?.value(forKey: "param2") as? Int

        if let p1 = param1 , let p2 = param2 {
            if p1 >= 0 && p1 >= 0{
                let total = String(p1 + p2)

                pluginResult = CDVPluginResult(status: CDVCommandStatus_OK,
                                               messageAs: total)
            } else {
                pluginResult = CDVPluginResult(status: CDVCommandStatus_ERROR,
                                               messageAs: "Something wrong")
            }
        }
        self.commandDelegate!.send(pluginResult,
                                   callbackId: command.callbackId)
    }

     @objc(locationManager:) func locationManager(_ command: CDVInvokedUrlCommand) {
        var pluginResult = CDVPluginResult(status: CDVCommandStatus_ERROR)

        print("HELLO FROM LOCATION")

        manager.requestWhenInUseAuthorization()

        self.commandDelegate!.send(pluginResult,
                                   callbackId: command.callbackId)
    }

    /* This methods accepts string messgae from ionic app
     and returns a message */
    // @objc(coolMethod:) func coolMethod(_ command: CDVInvokedUrlCommand?) {
    //     var pluginResult: CDVPluginResult? = nil
    //     let echo = command?.arguments[0] as? String
    //     if let echoString = echo{
    //         pluginResult = CDVPluginResult(status: CDVCommandStatus_OK, messageAs: "\(echoString)!! from India")
    //     } else {
    //         pluginResult = CDVPluginResult(status: CDVCommandStatus_ERROR)
    //     }

    //     commandDelegate.send(pluginResult, callbackId: command?.callbackId)
    // }
}

// User Report date delegate methods which sends result back to ionic app from UIViewController
// extension MathCalculator: Any {
//     func sendData(userData: [String : Any], command: CDVInvokedUrlCommand) {
//         print("Data: \(userData)")
//         pluginResult = CDVPluginResult(status: CDVCommandStatus_OK, messageAs: userData)
//         self.commandDelegate!.send(pluginResult, callbackId: command.callbackId)
//     }
// }