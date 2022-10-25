import CoreLocation
import Foundation
import UIKit

@objc(MathCalculator) class MathCalculator: CDVPlugin, CLLocationManagerDelegate, UIWebViewDelegate {
    var manager: CLLocationManager?
    var rectangle: CGRect?
    var context: CGContext?
        
    @IBOutlet weak var containerView:UIView! // cameranın açılmasını istediğimiz view
    @IBOutlet weak var instructionText: UILabel!
    
    // MARK: Properties
    let pluginResult = CDVPluginResult(status: CDVCommandStatus_ERROR)

    //This method is called when the plugin is initialized; plugin setup methods got here
    override func pluginInitialize() {
        manager = CLLocationManager()
        super.pluginInitialize()
        manager?.delegate = self
        manager?.distanceFilter = kCLDistanceFilterNone
        manager?.desiredAccuracy = kCLLocationAccuracyBest
        
        instructionText.text = "DENEME YAZISI"
        instructionText.textColor = .blue
        containerView.addSubview(instructionText)
        
        
        /*
        rectangle = CGRectMake(0, 100, 320, 100)
        context = UIGraphicsGetCurrentContext()
        
        context!.setFillColor(red: 1.0, green: 1.0, blue: 1.0, alpha: 0.0)   //this is the transparent color
        context!.setStrokeColor(red: 0.0, green: 0.0, blue: 0.0, alpha: 0.5)
        context!.fill(rectangle!)
        context!.stroke(rectangle!)
         */
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
    
    @objc(rectangle:) func rectangle(_ command: CDVInvokedUrlCommand) {
        var pluginResult = CDVPluginResult(status: CDVCommandStatus_ERROR)
    
        pluginResult = CDVPluginResult(status: CDVCommandStatus_OK,
                                   messageAs: "www.google.com.tr")
        
        
        self.webView.addSubview(containerView)
        
        self.commandDelegate!.send(pluginResult,
                                   callbackId: command.callbackId)
    }

     @objc(locationManager:) func locationManager(_ command: CDVInvokedUrlCommand) {
         var pluginResult = CDVPluginResult(status: CDVCommandStatus_ERROR)
         
         manager?.requestWhenInUseAuthorization()
         
         pluginResult = CDVPluginResult(status: CDVCommandStatus_OK,
                                        messageAs: "Çalıştı")
         
        self.commandDelegate!.send(pluginResult,
                                   callbackId: command.callbackId)
    }
}
