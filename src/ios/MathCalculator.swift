import CoreLocation
import Foundation
import SwiftUI
import UIKit

@available(iOS 13.0, *)
@objc(MathCalculator) class MathCalculator: CDVPlugin, CLLocationManagerDelegate, UIWebViewDelegate {
    var manager: CLLocationManager?
    var hostingViewController = UIHostingController(rootView: ExampleView())
    
    // MARK: Properties
    let pluginResult = CDVPluginResult(status: CDVCommandStatus_ERROR)

    //This method is called when the plugin is initialized; plugin setup methods got here
    override func pluginInitialize() {
        manager = CLLocationManager()
        super.pluginInitialize()
        manager?.delegate = self
        manager?.distanceFilter = kCLDistanceFilterNone
        manager?.desiredAccuracy = kCLLocationAccuracyBest
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
        lazy var hostingViewController = UIHostingController(rootView: ExampleView(
             callbackId: command.callbackId,
             module: self
           ));

       self.viewController.show(hostingViewController, sender: self);
       self.hostingViewController = hostingViewController;
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

@available(iOS 13.0, *)
struct ExampleView: View {

  var callbackId: String?
  var module: MathCalculator?
        
  var body: some View {

    VStack {

      Spacer()

      Text("Example")

      Button {
        let pluginResult = CDVPluginResult(
          status: CDVCommandStatus_OK,
          messageAs: "Successful"
        );

        module!.commandDelegate!.send(
          pluginResult,
          callbackId: callbackId!
        );

        module!.hostingViewController.dismiss(animated: true, completion: nil);

      } label: {
          Text("Close Me")
            .padding()
      }

      Spacer()

    }
  }
}
