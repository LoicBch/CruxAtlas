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
            
        }
        
        @Published var mapRegion = MKCoordinateRegion(center: CLLocationCoordinate2D(latitude: 45.7, longitude: 4.8), span: MKCoordinateSpan(latitudeDelta: 0.2, longitudeDelta: 0.2))
        @Published private(set) var markers = [MarkerIos]()
        @Published private(set) var mapcenter = Location(latitude: 51.5, longitude: -0.12)
        @Published var spotSelected : Spot?
        
        func getSpotAroundPos(location: Location){
            
            Task.init {
                        do {
                            let spots = try await RFetchSpotAtLocationUseCase().execute(location : location)
                            //let spots : ResultWrapper<[Spot]> = try await FetchSpotAtLocation(location: location).execute() as! ResultWrapper<[Spot]>

                            self.markers = spots.map({ (spot:Spot) -> MarkerIos in
                                MarkerIos(id : UUID(), name : spot.name, description: spot.name, latitude: spot.lat, longitude: spot.long_, spot : spot)
                            })
                        } catch {
                            // handle error
                        }
                    }
        }
    }
}
