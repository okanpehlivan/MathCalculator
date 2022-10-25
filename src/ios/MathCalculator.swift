import CoreLocation
import Foundation
import SwiftUI
import UIKit
import AVFoundation

public let deviceBounds = UIScreen.main.bounds
public let deviceWidth = deviceBounds.size.width
public let deviceHeight = deviceBounds.size.height

@available(iOS 14.0, *)
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

@available(iOS 14.0, *)
struct ExampleView: View {
    
    var callbackId: String?
    var module: MathCalculator?
    
    var body: some View {
        VStack {
            Button {
                let pluginResult = CDVPluginResult(
                      status: CDVCommandStatus_OK,
                      messageAs: "Successful"
                );

                module!.commandDelegate!.send(
                  pluginResult,
                  callbackId: callbackId!
                );
            } label: {
                Text("Swift UI Content")
                    .frame(width: deviceWidth, height: 100)
                    .background(Color.blue)
            }
           
            Spacer()
        }
        .background(Color.orange)
        .gesture(DragGesture(minimumDistance: 0, coordinateSpace: .local)
                    .onEnded({ value in
                        if value.translation.height > 0 {
                            module!.hostingViewController.dismiss(animated: true)
                        }
                    }))
        .ignoresSafeArea(.all, edges: .all)
    }
}
