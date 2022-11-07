//
//  MainMapScreen.swift
//  iosApp
//
//  Created by USER on 02/11/2022.
//  Copyright © 2022 orgName. All rights reserved.
//

import SwiftUI
import shared
import MapKit

 struct MainMapScreen: View {
    
     
    @StateObject private var viewModel = MainMapViewModel()
   
     public var body: some View {
        
        ZStack(alignment: .leading){
            Map(coordinateRegion: .constant(viewModel.mapRegion), annotationItems: viewModel.markers){ marker in
                MapAnnotation(coordinate: marker.coordinate) {
                    MarkerView(title: marker.name)
                      }
                }
            Button(action: {
                viewModel.getSpotAroundPos(location: Location(latitude: viewModel.mapRegion.center.latitude, longitude: viewModel.mapRegion.center.longitude))
            }
            ){
                Image("reload")
            }
            .frame(width: 50, height: 50)
            .background(Color.green)
            .cornerRadius(25)
            .buttonStyle(.plain)
            .offset(x: 25, y: 300)
            }
        }
    }

    struct MainMapScreen_Previews: PreviewProvider {
        static var previews: some View {
            EmptyView()
        }
    }
