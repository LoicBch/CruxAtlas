//
//  MarkerIos.swift
//  iosApp
//
//  Created by USER on 04/11/2022.
//  Copyright © 2022 orgName. All rights reserved.
//

import Foundation
import CoreLocation
import MapKit


struct MarkerIosf: Identifiable {
    let id = UUID()
    let name: String
    let coordinate: CLLocationCoordinate2D
    
}

 
