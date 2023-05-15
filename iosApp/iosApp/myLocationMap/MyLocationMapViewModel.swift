//
//  MyLocationMapViewModel.swift
//  iosApp
//
//  Created by USER on 13/04/2023.
//  Copyright © 2023 orgName. All rights reserved.
//

import Foundation
import shared
import CoreLocation
import MapKit

extension MyLocationMapScreen {
    @MainActor class MylocationMapViewModel : ObservableObject {
        
        @Published var mapRegion =  MKCoordinateRegion(center: CLLocationCoordinate2D(latitude: 45.7, longitude: 4.8), span: MKCoordinateSpan(latitudeDelta: 0.2, longitudeDelta: 0.2))
        @Published var activeLocation = LocationInfos(lat: 0.0, lon: 0.0, country: "", iso: "", address: "", gpsDeciTxt: "", gpsDmsTxt: "")
        @Published var parkingLocation = MarkerAnnotation(coordinate: CLLocationCoordinate2D(), idPlaceLinked: "parking_location", selected: false)
        @Published var currentLocation = MarkerAnnotation(coordinate: CLLocationCoordinate2D(), idPlaceLinked: "current_location", selected: false)
        @Published var popupIsActive = false
        @Published private(set) var isLoading = false
        
        init(){
            initCurrentLocation()
            initParkingLocation()
        }
        
        func initCurrentLocation(){
            currentLocation = MarkerAnnotation(coordinate: CLLocationCoordinate2D(latitude: Globals.geoLoc().lastKnownLocation.latitude, longitude: Globals.geoLoc().lastKnownLocation.longitude), idPlaceLinked: "current_location", selected: false)
            mapRegion = MKCoordinateRegion(center: CLLocationCoordinate2D(latitude: Globals.geoLoc().lastKnownLocation.latitude, longitude: Globals.geoLoc().lastKnownLocation.longitude), span: MKCoordinateSpan(latitudeDelta: 0.2, longitudeDelta: 0.2))
            selectCurrentLocation()
        }
        
        func initParkingLocation(){
            
            let parkingLocationSaved = KMMPreference(context: NSObject()).getString(key: "parking_location")
            if (parkingLocationSaved != nil){
                let latLng = parkingLocationSaved?.components(separatedBy: ",")
                let latitude = latLng![0]
                let longitude = latLng![1]
                print(latitude)
                print(longitude)
                parkingLocation = MarkerAnnotation(coordinate: CLLocationCoordinate2D(latitude: Double(latitude)!, longitude: Double(longitude)!), idPlaceLinked: "parking_location", selected: false)
            }
        }
        
        func selectCurrentLocation(){
            isLoading = true
            Task.init {
                do {
                    activeLocation = try await RGetLocationInfos().execute(location: Location(latitude: currentLocation.coordinate.latitude, longitude: currentLocation.coordinate.longitude))!
                    parkingLocation.selected = false
                    currentLocation = MarkerAnnotation(coordinate: currentLocation.coordinate, idPlaceLinked: "current_location", selected: true)
                    isLoading = false
                } catch {
                    // handle error
                }
            }
        }
        
        func selectParkingLocation(location: Location){
            isLoading = true
            Task.init {
                do {
                    activeLocation = try await RGetLocationInfos().execute(location: location)!
                    parkingLocation.selected = true
                    currentLocation.selected = false
                    isLoading = false
                } catch {
                    // handle error
                }
            }
        }
        func deleteParkingLocation(){
            KMMPreference(context: NSObject()).remove(key: "parking_location")
            parkingLocation = MarkerAnnotation(coordinate: CLLocationCoordinate2D(latitude: 0.0, longitude: 0.0), idPlaceLinked: "parking_location", selected: false)
            selectCurrentLocation()
            popupIsActive = false
        }
        
        func saveParkingLocation(latitude: Double, longitude: Double){
            KMMPreference(context: NSObject()).put(key: "parking_location", value___: "\(latitude),\(longitude)")
            selectParkingLocation(location: Location(latitude: latitude, longitude: longitude))
        }
        
    }
}

