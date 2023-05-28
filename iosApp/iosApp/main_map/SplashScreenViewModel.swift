//
//  SplashScreenViewModel.swift
//  iosApp
//
//  Created by USER on 27/04/2023.
//  Copyright © 2023 orgName. All rights reserved.
//

import Foundation
import shared
import Combine
import Network
import CoreLocation

extension HomeScreen {
    @MainActor class SplashScreenViewModel: ObservableObject{
        
        @Published var globalsVarsAreSet = false
        @Published var locationPermissionIsAsked = false
        @Published var locationIsObserved = false
        @Published var networkIsObserved = false
        @Published var globalSliderState: [SliderState] = []
        @Published var globalPopupState = GlobalPopupState.Hid
        @Published var appIsLoading = false
        
        private var locationIsEnabled = false
        private var networkIsEnabled = false
        
        func startSetup() { 
         setupApp()
        }
        
        func startObservingLocation(location: CLLocation){
            Globals.geoLoc().lastKnownLocation = Location(latitude: location.coordinate.latitude, longitude: location.coordinate.longitude)
            
            if (Globals.geoLoc().lastSearchedLocation == Constants().DEFAULT_LOCATION){
                Globals.geoLoc().lastSearchedLocation = Location(latitude: location.coordinate.latitude, longitude: location.coordinate.longitude)
            }
             
            if (!self.locationIsObserved){
                self.locationIsObserved = true
            }
        }
        
        func setupApp(){
            
            if (LocationManager.Companion().isPermissionAllowed()){
                locationPermissionIsAsked = true
            }else{
                LocationManager.Companion().requestPermission()
                locationPermissionIsAsked = true
            }
                        
            Task.init {
                do {
                    let res = try await RSetupApp().execute()
                    setDeviceConstants()
                    globalsVarsAreSet = true
                }
            }
        }
        
        func setLocationEnabled(enable: Bool){
            locationIsEnabled = enable
        }
        
        func setNetworkEnable(enable: Bool){
            networkIsEnabled = enable
        }
        
         func setDeviceConstants() {
               Globals.geoLoc().appLanguage = "FR"
               Globals.geoLoc().deviceLanguage = ""
               Globals.geoLoc().deviceCountry = ""
           }
        
        func showGLobalPopup(state: GlobalPopupState){
            globalPopupState = state
        }
        
        func showSliderState(state: SliderState){
            if (!globalSliderState.contains(where: {
                $0 == SliderState.GpsMissing
            })){
                globalSliderState.append(state)
            }
        }
        
        func withNetworkOnly(block : () -> Void){
            if (networkIsEnabled){
                block()
            }else {
                showGLobalPopup(state: GlobalPopupState.NetworkMissing)
            }
        }
        
        func withGpsOnly(block : () -> Void){
            if (locationIsEnabled){
                block()
            }else{
                showGLobalPopup(state: GlobalPopupState.GpsMissing)
            }
        }
        
        func showOnlyWithGps() -> Bool {
            if (locationIsEnabled){
                return true
            }else{
                showGLobalPopup(state: GlobalPopupState.GpsMissing)
                return false
            }
        }
        
        func showOnlyWithNetwork() -> Bool {
            if (networkIsEnabled){
                
                return true
            }else{
                showGLobalPopup(state: GlobalPopupState.NetworkMissing)
                return false
            }
        }
        
        func hideGlobalPopup(){
            globalPopupState = .Hid
        }
        
        func removeGpMissingsNotification(){
            globalSliderState.remove(object: SliderState.GpsMissing)
        }
        
        func removeNetworkMissingNotification(){
            globalSliderState.remove(object: SliderState.NetworkMissing)
        }
    }
}
