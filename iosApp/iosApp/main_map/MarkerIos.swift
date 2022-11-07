//
//  Location.swift
//  Bucketlist
//
//  Created by Paul Hudson on 09/12/2021.
//

import Foundation
import CoreLocation

struct MarkerIos: Identifiable, Codable, Equatable {
    var id: UUID
    var name: String
    var description: String
    let latitude: Double
    let longitude: Double

    var coordinate: CLLocationCoordinate2D {
        CLLocationCoordinate2D(latitude: latitude, longitude: longitude)
    }

    static let example = MarkerIos(id: UUID(), name: "Buckingham Palace", description: "Where Queen Elizabeth lives with her dorgis", latitude: 51.501, longitude: -0.141)

    static func ==(lhs: MarkerIos, rhs: MarkerIos) -> Bool {
        lhs.id == rhs.id
    }
}
