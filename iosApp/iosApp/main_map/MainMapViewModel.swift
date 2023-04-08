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
        @Published private(set) var mapcenter = Location(latitude: 51.5, longitude: -0.12)
        
        @Published private(set) var markers = [MarkerIos]()
        @Published private(set) var events = [Event]()
        @Published private(set) var dealers = [Dealer]()
        @Published private(set) var updateSource = UpdateSource.default_
        @Published private(set) var ads = [Ads]()
        @Published private(set) var cameraIsOutOfRadiusLimit = false
        @Published private(set) var verticalListIsShowing = false
        @Published private(set) var isLoading = false
        @Published private(set) var placeSearched = ""
        
        @Published private(set) var dealersSorted = [Dealer]()
        @Published private(set) var eventsSorted = [Event]()
        
        
        func showDealers(location: Location){
            
            Task.init {
                do {
                    let dealerss = try await RFetchSpotAtLocationUseCase().execute(location : mapcenter)
                    dealers = dealerss
                    self.markers = dealerss.map({ (dealer:Dealer) -> MarkerIos in
                        MarkerIos(id : UUID(), placeLinkedId : dealer.id, selected : false, latitude: dealer.latitude, longitude: dealer.longitude)
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
            verticalListIsShowing = false
        }
        
        func permuteVerticalList(){
            verticalListIsShowing = !verticalListIsShowing
        }
        
        func showEvents(){
            Task.init {
                do {
                    let eventss = try await RFetchSpotAtLocationUseCase().execute(location : mapcenter)
                    events = eventss
                    self.events = eventss.map({ (event:Event) -> MarkerIos in
                        MarkerIos(id : UUID(), placeLinkedId: event.id, selected: false, latitude: event.latitude, longitude: event.longitude)
                    })
                } catch {
                    // handle error
                }
            }
        }
        
        func getAds(){
            Task.init {
                
            }
        }
        
        func onSortingOptionSelected(sortOption: SortOption){
            
        }
        
    }
}
