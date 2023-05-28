//
//  Extension.swift
//  iosApp
//
//  Created by USER on 14/04/2023.
//  Copyright © 2023 orgName. All rights reserved.
//

import Foundation
import shared
import _MapKit_SwiftUI
import SwiftUI

extension Location {
    func distanceFrom(location: Location) -> Double {
        let earthRadius = 6371.0
        
        let lat1 = (self.latitude * Double.pi) / 180
        let lon1 = (self.longitude * Double.pi) / 180
        let lat2 = (location.latitude * Double.pi) / 180
        let lon2 = (location.longitude * Double.pi) / 180
        
        let deltaLat = lat2 - lat1
        let deltaLon = lon2 - lon1
        
        let a = sin(deltaLat / 2) * sin(deltaLat / 2) + cos(lat1) * cos(lat2) * sin(deltaLon / 2) * sin(deltaLon / 2)
        
        let c = 2 * atan2(sqrt(a), sqrt(1 - a))
        let toKm = earthRadius * c
        return toKm
    }
    
    func distanceFromUserLocationText() -> String {
        switch KMMPreference(context: NSObject()).getInt(key: Constants.PreferencesKey().METRIC, default: 0) {
        case 0 :
            return "\(round(self.distanceFrom(location: Globals.geoLoc().lastKnownLocation) * 10) / 10) km"
        case 1:
            return "\((round(self.distanceFrom(location: Globals.geoLoc().lastKnownLocation) * 10) / 10).fromKmToMiles()) mi"
        default:
            return "\(round(self.distanceFrom(location: Globals.geoLoc().lastKnownLocation) * 10) / 10) km"
        }
    }
    
    func isAroundLastSearchedLocation() -> Bool { 
        return self.distanceFrom(location: Globals.geoLoc().lastSearchedLocation) < Double(Globals.geoLoc().RADIUS_AROUND_LIMIT)
    }
    
    func distanceFromLastSearch() -> Double {
        self.distanceFrom(location: Globals.geoLoc().lastSearchedLocation)
    }
    
    func distanceFromUserLocation() -> Double {
        self.distanceFrom(location: Globals.geoLoc().lastKnownLocation)
    }
}

struct WithNetworkOnlyKey: EnvironmentKey {
    static var defaultValue: (() -> Void) -> Void = { _ in }
}


struct WithGpsOnlyKey: EnvironmentKey {
    static var defaultValue: (() -> Void) -> Void = { _ in }
}

struct ShowOnlyWithGps: EnvironmentKey {
    static var defaultValue: () -> Bool = { false }
}

struct ShowOnlyWithNetwork: EnvironmentKey {
    static var defaultValue: () -> Bool = { false }
}

struct OnLanguageUpdate: EnvironmentKey {
    static var defaultValue: (String) -> Void = { _ in }
}

struct OnAppLoading: EnvironmentKey {
    static var defaultValue: (Bool) -> Void = { _ in }
}

extension EnvironmentValues {
    var withNetworkOnly: (() -> Void) -> Void {
        get { self[WithNetworkOnlyKey.self] }
        set { self[WithNetworkOnlyKey.self] = newValue }
    }
    
    var withGpsOnly: (() -> Void) -> Void {
        get { self[WithGpsOnlyKey.self] }
        set { self[WithGpsOnlyKey.self] = newValue }
    }
    
    var showOnlyWithGps: () -> Bool {
        get { self[ShowOnlyWithGps.self] }
        set { self[ShowOnlyWithGps.self] = newValue }
    }
    
    var showOnlyWithNetwork: () -> Bool {
        get { self[ShowOnlyWithNetwork.self] }
        set { self[ShowOnlyWithNetwork.self] = newValue }
    }
    
    var onLanguageUpdate: (String) -> Void {
        get { self[OnLanguageUpdate.self] }
        set { self[OnLanguageUpdate.self] = newValue }
    }
    
    var onLoading: (Bool) -> Void{
        get { self[OnAppLoading.self] }
        set { self[OnAppLoading.self] = newValue }
    }
}

extension Double {
    func fromKmToMiles() -> Double {
        (self * 0.621371 * 10.0).rounded() / 10
    }
    
    func toDMS() -> String {
        let degrees = Int(floor(self))
        let minutes = Int(floor((self - Double(degrees)) * 60.0))
        let secondsInDegrees = (self - Double(degrees)) * 3600.0
        let secondsInMinutes = secondsInDegrees - Double(minutes) * 60.0
        let secondsInDecimal = secondsInMinutes / 1000.0
        let seconds = secondsInDecimal.rounded() * 1000.0
        
        return "\(degrees)° \(minutes)' \(seconds)''"
    }
}

extension Map {
    func setMapStyle(mapType: MKMapType) -> some View {
        MKMapView.appearance().mapType = mapType
        return self
    }
}

extension Binding {
    func onChange(_ handler: @escaping (Value) -> Void) -> Binding<Value> {
        Binding(
            get: { self.wrappedValue },
            set: { newValue in
                self.wrappedValue = newValue
                handler(newValue)
            }
        )
    }
}

extension Array where Element: Equatable {
    
    // Remove first collection element that is equal to the given `object`:
    mutating func remove(object: Element) {
        guard let index = firstIndex(of: object) else {return}
        remove(at: index)
    }
}

extension Dealer {
    func fullLocation() -> String {
        return "\(self.address), \(self.city), \(self.postalCode)"
    }
    
    func fullGeolocation() -> String {
        return "\(self.latitude), \(self.longitude) (lat, lng)\nN \(self.latitude.toDMS()), E \(self.longitude.toDMS())"
    }
    
    func stringToSend() -> String {
        return "\(self.name)\n\n\(self.fullLocation)\n\(self.fullGeolocation())"
    }
}

extension Event {
    func fullLocation() -> String {
        return "\(self.address), \(self.city), \(self.postalCode)"
    }
    
    func fullGeolocation() -> String {
        return "\(self.latitude), \(self.longitude) (lat, lng)\nN \(self.latitude.toDMS()), E \(self.longitude.toDMS())"
    }
    
    func stringToSend() -> String {
        return "\(self.name)\n\n\(self.fullLocation)\n\(self.fullGeolocation())"
    }
}

extension Array {
    func chunked(into size: Int) -> [[Element]] {
        return stride(from: 0, to: count, by: size).map {
            Array(self[$0 ..< Swift.min($0 + size, count)])
        }
    }
}

extension View {
    func readSize(onChange: @escaping (CGSize) -> Void) -> some View {
        background(
            GeometryReader { geometryProxy in
                Color.clear
                    .preference(key: SizePreferenceKey.self, value: geometryProxy.size)
            }
        )
        .onPreferenceChange(SizePreferenceKey.self, perform: onChange)
    }
}

extension Filter{
    func filterName() -> String {
        var filterName = ""
        switch(category){
        case FilterType.service:
            filterName = (Globals.filters().services.first(where: {$0.first! as String == filterId})?.second! ?? "") as String
        case FilterType.brand:
            filterName = (Globals.filters().brands.first(where: {$0.first! as String == filterId})?.second! ?? "") as String
        default:
            filterName = ""
        }
        return filterName
    }
    
    func getIdFromFilterName(name: String) -> String {
        var filterId = ""
        switch (category){
        case FilterType.service:
            filterId = (Globals.filters().services.first(where: {$0.second! as String == name})?.first! ?? "") as String
        case FilterType.brand:
            filterId = (Globals.filters().brands.first(where: {$0.second! as String == name})?.first! ?? "") as String
        default:
            filterId = ""
        }
        return filterId
    }
}

//Needed because iosMap treat the blue user location dot as a normal annotation so we just unselect it from annotations
extension MKMapView{
    func markerAnnotation() -> [MarkerAnnotation]{
        return (self.annotations.filter({$0 is MarkerAnnotation})) as! [MarkerAnnotation]
    }
}

extension URL {
    func ping(completion: @escaping (Int?, Error?) -> Void) {
        var request = URLRequest(url: self)
        request.httpMethod = "HEAD"

        let task = URLSession.shared.dataTask(with: request) { (_, response, error) in
            if let error = error {
                completion(nil, error)
            } else if let httpResponse = response as? HTTPURLResponse {
                completion(httpResponse.statusCode, nil)
            }
        }

        task.resume()
    }
}

extension [MarkerAnnotation] {
    func getRegion() -> MKCoordinateRegion {
        var latitudeMin = Double.greatestFiniteMagnitude
        var latitudeMax = -Double.greatestFiniteMagnitude
        var longitudeMin = Double.greatestFiniteMagnitude
        var longitudeMax = -Double.greatestFiniteMagnitude

        // Calcul des valeurs minimales et maximales des coordonnées
        for annotation in self {
            let coordinate = annotation.coordinate
            latitudeMin = Swift.min(latitudeMin, coordinate.latitude)
            latitudeMax = Swift.max(latitudeMax, coordinate.latitude)
            longitudeMin = Swift.min(longitudeMin, coordinate.longitude)
            longitudeMax = Swift.max(longitudeMax, coordinate.longitude)
        }

        // Calcul des coordonnées centrales
        let centerLatitude = (latitudeMin + latitudeMax) / 2
        let centerLongitude = (longitudeMin + longitudeMax) / 2

        // Calcul des écarts de latitude et de longitude
        let latitudeDelta = abs(latitudeMax - latitudeMin) * 1.3
        let longitudeDelta = abs(longitudeMax - longitudeMin) * 1.3

        // Création de la région
        return MKCoordinateRegion(center: CLLocationCoordinate2D(latitude: centerLatitude, longitude: centerLongitude),
                                  span: MKCoordinateSpan(latitudeDelta: latitudeDelta, longitudeDelta: longitudeDelta))
    }
}
