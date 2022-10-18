import CoreLocation

@objc(MathCalculator) class MathCalculator: CDVPlugin, CLLocationManagerDelegate {
    var manager: CLLocationManager?
    
    // MARK: Properties
    let pluginResult = CDVPluginResult(status: CDVCommandStatus_ERROR)

    //This method is called when the plugin is initialized; plugin setup methods got here
    override func pluginInitialize() {
        
        manager = CLLocationManager()
        super.pluginInitialize()
        manager?.delegate = self
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
         let pluginResult = CDVPluginResult(status: CDVCommandStatus_ERROR)
         
         manager?.requestWhenInUseAuthorization()
         
         pluginResult = CDVPluginResult(status: CDVCommandStatus_OK,
                                        messageAs: "Çalıştı")
         
        self.commandDelegate!.send(pluginResult,
                                   callbackId: command.callbackId)
    }
}
