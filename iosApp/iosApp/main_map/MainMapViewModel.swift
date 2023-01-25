//
//  MainMapViewModel.swift
//  iosApp
//
//  Created by USER on 02/11/2022.
//  Copyright © 2022 orgName. All rights reserved.
//

import Foundation
import shared
import CoreLocation
import MapKit

extension MainMapScreen {
    @MainActor class MainMapViewModel : ObservableObject {
        
        init(){
            LocationManager.Companion().requiredPermission = .authorizedalways
            LocationManager.Companion().onAlwaysAllowsPermissionRequired(target: self) {
                print("onAlwaysAllowsPermissionRequired")
            }
            getSpotAroundPos(location: Location(latitude: 45.7640, longitude: 4.8357))
        }
        
        @Published var mapRegion = MKCoordinateRegion(center: CLLocationCoordinate2D(latitude: 45.7, longitude: 4.8), span: MKCoordinateSpan(latitudeDelta: 0.2, longitudeDelta: 0.2))
        @Published private(set) var markers = [MarkerIos]()
        @Published private(set) var mapcenter = Location(latitude: 51.5, longitude: -0.12)
        @Published private(set) var spots = [Spot]()
        
        @Published private(set) var spotQuery = SpotPresentation(spots: Array<Spot>(), source: SpotSource.default_)
        @Published private(set) var ads = [Ads]()
        @Published private(set) var cameraIsOutOfRadiusLimit = false
        @Published private(set) var verticalListIsShowing = false
        @Published private(set) var isLoading = false
        @Published private(set) var placeSearched = ""
        
        func getSpotAroundPos(location: Location){
            
            Task.init {
                do {
                    let spotss = try await RFetchSpotAtLocationUseCase().execute(location : mapcenter)
                    //let spots : ResultWrapper<[Spot]> = try await FetchSpotAtLocation(location: location).execute() as! ResultWrapper<[Spot]>
                    spots = spotss
                    self.markers = spotss.map({ (spot:Spot) -> MarkerIos in
                        MarkerIos(id : UUID(), name : spot.name, description: spot.name, latitude: spot.latitude, longitude: spot.longitude, spot : spot)
                    })
                } catch {
                    // handle error
                }
            }
        }
        
        func getLocation(){
             
                LocationManager.Companion()
                    .onLocationUnavailable(target: "SingleRequest") {
                        print("onLocationUnavailable")
                    }
                    .onPermissionUpdated(target: self, block: {
                        print("onPermissionUpdated. Granted:", $0)
                    })
                    .currentLocation { data in
                        print("location coordinates", Date(), data.coordinates)
                    }
        }
        
        func showVerticalList(){
            verticalListIsShowing = true
        }
        
        func hideVerticalList(){
            verticalListIsShowing = true
        }
        
        func permuteVerticalList(){
            verticalListIsShowing = !verticalListIsShowing
        }
    }
}
