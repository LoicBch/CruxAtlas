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
import SwiftUI

extension MainMapScreen {
    @MainActor class MainMapViewModel : ObservableObject {
        
        init(){
            showDealers(location: Globals.geoLoc().lastKnownLocation)
            getAds()
        }
        
        @Published var mapRegion = MKCoordinateRegion(center: CLLocationCoordinate2D(latitude: Globals.geoLoc().lastKnownLocation.latitude, longitude: Globals.geoLoc().lastKnownLocation.longitude), span: MKCoordinateSpan(latitudeDelta: 0.2, longitudeDelta: 0.2))
        
        @Published var markers = [MarkerAnnotation]()
        @Published var events = [Event]()
        @Published var dealers = [Dealer]()
        @Published private(set) var updateSource = UpdateSource.default_
        @Published private(set) var ads = [Ad]()
        @Published private(set) var cameraIsOutOfRadiusLimit = false
        @Published private(set) var verticalListIsShowing = false
        @Published private(set) var isLoading = false
        @Published private(set) var placeSearched = ""
        @Published var mapType = MKMapType.standard
        @Published private(set) var dealersSorted = [Dealer]()
        @Published private(set) var eventsSorted = [Event]()
        @Published var placeIdToScroll = ""
        @Published var searchHereButtonIsShowing = false
        @Published var currentPlaceSelectedId = ""
        @Published var mapMovedByCode = false
        
        
        func showDealers(location: Location){
            isLoading = true
            searchHereButtonIsShowing = false
            updateSource = UpdateSource.aroundMe
            Task.init {
                do {
                    let res = try await RFetchDealersAtLocationUseCase().execute(location : location)!
                    dealers = res
                    dealersSorted = res
                    placeSearched = ""
                    self.markers = dealers.map({ (dealer:Dealer) -> MarkerAnnotation in
                        MarkerAnnotation(coordinate: CLLocationCoordinate2D(latitude: dealer.latitude, longitude: dealer.longitude), idPlaceLinked: dealer.id, selected: false)
                    })
                    updateMapRegion(latitude: location.latitude, longitude: location.longitude, zoom: mapRegion.span)
                    isLoading = false
                } catch {
                    isLoading = false
                    // handle error
                }
            }
        }
        
        func showEvents(){
            isLoading = true
            Task.init {
                do {
                    let res = try await RFetchEvents().execute()!
                    events = res
                    eventsSorted = res
                    searchHereButtonIsShowing = false
                    self.markers = events.map({ (event:Event) -> MarkerAnnotation in
                        MarkerAnnotation(coordinate: CLLocationCoordinate2D(latitude: event.latitude, longitude: event.longitude), idPlaceLinked: event.id, selected: false)
                    })
                    
                    updateSource = UpdateSource.events
                    isLoading = false
                    mapRegion = self.markers.getRegion()
                    updateMapRegion(latitude: mapRegion.center.latitude, longitude: mapRegion.center.longitude, zoom: mapRegion.span)
                } catch {
                    isLoading = false
                    // handle error
                }
            }
        }
        
        func showSpotsAroundPlace(place: Place){
            isLoading = true
            Task.init {
                do {
                    let result = try await RFetchDealersAtLocationUseCase().execute(location : place.location)!
                    dealers = result
                    dealersSorted = result
                    updateSource = UpdateSource.aroundPlace
                    placeSearched = place.name
                    searchHereButtonIsShowing = false
                    updateMapRegion(latitude: place.location.latitude, longitude: place.location.longitude, zoom: mapRegion.span)
                    self.markers = dealers.map({ (dealer:Dealer) -> MarkerAnnotation in
                        MarkerAnnotation(coordinate: CLLocationCoordinate2D(latitude: dealer.latitude, longitude: dealer.longitude), idPlaceLinked: dealer.id, selected: false)
                    })
                    isLoading = false
                } catch {
                    isLoading = false
                    // handle error
                }
            }
        }
        
        func getAds(){
            isLoading = true
            Task.init {
                do {
                    ads = try await RFetchAds().execute()!
                    isLoading = false
                } catch {
                    isLoading = false
                    // handle error
                }
            }
        }
        
        func onSortingOptionSelected(sortingOption : SortOption){
            isLoading = true
            Task.init {
                do {
                    if (updateSource == UpdateSource.events){
                        eventsSorted = try await RSortEvents().execute(sortOption: sortingOption, eventsToSort: events)!
                    } else {
                        dealersSorted = try await RSortDealer().execute(sortOption: sortingOption, dealersToSort: dealers)!
                    }
                } catch {
                    // handle error
                }
            }
        }
        
        func onMapStopMoving(location: Location){
            self.searchHereButtonIsShowing = false
            self.mapMovedByCode = false
            if (updateSource == UpdateSource.aroundMe || updateSource == UpdateSource.default_){
                if (!Location(latitude: location.latitude, longitude: location.longitude).isAroundLastSearchedLocation){
                    self.searchHereButtonIsShowing = true
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
        
        func switchMapStyle(){
            if (mapType == MKMapType.standard){
                mapType = MKMapType.satellite
            }else{
                mapType = MKMapType.standard
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
        
        func selectMarker(placeLinkedId: String, zoom: MKCoordinateSpan){
            unSelectMarker(placeLinkedId: currentPlaceSelectedId)
            currentPlaceSelectedId = placeLinkedId
            
            var markerSelected = markers.first(where: {
                $0.idPlaceLinked == placeLinkedId
            })
            if let i = markers.firstIndex(of: markerSelected!) {
                markers[i] = MarkerAnnotation(coordinate: CLLocationCoordinate2D(latitude: (markerSelected?.coordinate.latitude)!, longitude: (markerSelected?.coordinate.longitude)!), idPlaceLinked: markerSelected!.idPlaceLinked, selected: true)
            }
            
            updateMapRegion(latitude: CGFloat(markerSelected!.coordinate.latitude), longitude: CGFloat(markerSelected!.coordinate.longitude), zoom: zoom)
            
        }
        
        private func unSelectMarker(placeLinkedId: String){
            if(placeLinkedId != ""){
                var markerSelected = markers.first(where: {
                    $0.idPlaceLinked == placeLinkedId
                })
                if let i = markers.firstIndex(of: markerSelected!) {
                    markers[i] = MarkerAnnotation(coordinate: markerSelected!.coordinate, idPlaceLinked: markerSelected!.idPlaceLinked, selected: false )
                }
            }
        }
        
        
        func updateMapRegion(latitude: CGFloat, longitude: CGFloat, zoom: MKCoordinateSpan ){
            mapMovedByCode = true
            mapRegion = MKCoordinateRegion(center: CLLocationCoordinate2D(latitude: latitude, longitude: longitude), span: zoom)
//            mapRegion = MKCoordinateRegion(center: CLLocationCoordinate2D(latitude: 50.6, longitude: 3.06), span: zoom)
//            mapRegion = MKCoordinateRegion(center: CLLocationCoordinate2D(latitude: 44.8, longitude: -0.5), span: zoom)
        }
    }
}
