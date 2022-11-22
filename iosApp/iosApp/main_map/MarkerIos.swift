//
//  Location.swift
//  Bucketlist
//
//  Created by Paul Hudson on 09/12/2021.
//

import Foundation
import CoreLocation
import shared

struct MarkerIos: Identifiable, Equatable {
    var id: UUID
    var name: String
    var description: String
    let latitude: Double
    let longitude: Double
    var spot : Spot

    var coordinate: CLLocationCoordinate2D {
        CLLocationCoordinate2D(latitude: latitude, longitude: longitude)
    }

    
    static func ==(lhs: MarkerIos, rhs: MarkerIos) -> Bool {
        lhs.id == rhs.id
    }
}
