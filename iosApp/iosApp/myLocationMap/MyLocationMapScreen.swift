//
//  MyLocationScreen.swift
//  iosApp
//
//  Created by USER on 13/04/2023.
//  Copyright © 2023 orgName. All rights reserved.
//

import Foundation
import shared
import SwiftUI
import MapKit


struct MyLocationMapScreen: View {
    
    @StateObject private var viewModel = MylocationMapViewModel()
    
    public var body: some View {
        VStack{
            HStack{
                Image(systemName: "xmark").onTapGesture {
                    
                }
                Spacer()
                LocalizedText(key: "location_map")
                Spacer()
            }.padding(.top, 12)
            ZStack{
                MyLocationMap(currentLocation: $viewModel.currentLocation, parkingLocation: $viewModel.parkingLocation,
                              region: $viewModel.mapRegion,
                              onMapStopMoving: {_ in
                }, onSelectMarker: { markerSelected in
                    if(markerSelected.idPlaceLinked == "current_location"){
                        viewModel.selectCurrentLocation()
                    }else{
                        viewModel.selectParkingLocation(location: Location(latitude: viewModel.parkingLocation.coordinate.latitude, longitude: viewModel.parkingLocation.coordinate.longitude) )
                    }
                })
                VStack{
                    Spacer()
                    VStack{
                        if (viewModel.parkingLocation.selected){
                            parkingMarkerBox(parkingLocation: $viewModel.activeLocation, onDeleteParkingLocation: {viewModel.popupIsActive = true}, onNavigate: {
                                let coordinate = CLLocationCoordinate2DMake(viewModel.parkingLocation.coordinate.latitude, viewModel.parkingLocation.coordinate.longitude)
                                let mapItem = MKMapItem(placemark: MKPlacemark(coordinate: coordinate, addressDictionary:nil))
                                mapItem.name = "Parking location"
                                mapItem.openInMaps(launchOptions: [MKLaunchOptionsDirectionsModeKey : MKLaunchOptionsDirectionsModeDriving])
                            })
                        }else{
                            currentLocationMarkerBox(currentPosInfos: $viewModel.activeLocation, onParkHere: {
                                viewModel.saveParkingLocation(latitude: viewModel.currentLocation.coordinate.latitude, longitude: viewModel.currentLocation.coordinate.longitude)
                            })
                        }
                    }
                }
                if (viewModel.popupIsActive){
                    VStack{
                        Spacer()
                        popupConfirm(onExit: {viewModel.popupIsActive = false}, onDelete: { viewModel.deleteParkingLocation()})
                        Spacer()
                    }.background(Color("ClearGrey"))
                }
            }
        }
    }
}

struct popupConfirm: View {
    
    var onExit: () -> Void
    var onDelete: () -> Void
    
    public var body: some View {
        VStack{
            HStack{
                Image(systemName: "trash.fill").onTapGesture {
                    onExit()
                }
                Spacer()
                LocalizedText(key: "are_you_sure")
                    .font(.system(size: 16))
                    .foregroundColor(Color.black)
                Spacer()
            }
            LocalizedText(key: "delete_location_popup")
                .padding(.top, 14)
            
            Button(action: {
                onDelete()
            }){
                Spacer()
                LocalizedText(key: "delete")
                    .font(.system(size: 16))
                    .foregroundColor(Color.white)
                    .padding(.vertical, 16)
                Spacer()
            }
            .frame(height: 50)
            .background(Color.red)
            .cornerRadius(25)
            .padding(.top, 20)
            .padding(.horizontal, 16)
            .buttonStyle(.plain)
        }.padding(12)
            .background(Color.white)
            .cornerRadius(15)
            .shadow(radius: 5, x:-1, y: 1)
            .padding(16)
    }
}

struct parkingMarkerBox: View {
    
    @Binding var parkingLocation: LocationInfos
    var onDeleteParkingLocation: () -> Void
    var onNavigate: () -> Void
    
    public var body: some View {
        VStack{
            HStack{
                LocalizedText(key: "my_parking")
//                    .fontWeight(.medium)
                    .font(.system(size: 16))
                    .foregroundColor(Color("Tertiary"))
                Spacer()
                Image(systemName: "trash.fill").onTapGesture {
                    onDeleteParkingLocation()
                }
                Image("share")
            }
            
            HStack{
                Image("distance")
                Text(Location(latitude: parkingLocation.lat, longitude: parkingLocation.lon).distanceFromUserLocationText())
                    .fontWeight(.medium)
                    .font(.system(size: 12))
                    .foregroundColor(Color("Tertiary"))
                Spacer()
            }.padding(.top, 10)
            
            HStack{
                Image("pin_here")
                Text(parkingLocation.address)
                    .fontWeight(.medium)
                    .font(.system(size: 12))
                    .foregroundColor(Color("Tertiary"))
                Spacer()
            }.padding(.top, 8)
            
            HStack{
                Image("my_location")
                Text("\(parkingLocation.gpsDeciTxt) (lat, lng) \n \(parkingLocation.gpsDmsTxt) (lat, lng)")
                    .fontWeight(.medium)
                    .font(.system(size: 12))
                    .foregroundColor(Color("Tertiary"))
                Spacer()
            }.padding(.top, 8)
            
            AppButton(action: {
                onNavigate()
            }, title: "navigate", isEnable: true).padding(.top, 16)
        }
        .padding(12)
        .background(Color.white)
        .cornerRadius(15)
        .shadow(radius: 5, x:-1, y: 1)
        .padding(16)
    }
}

struct currentLocationMarkerBox: View {
    
    @Binding var currentPosInfos: LocationInfos
    var onParkHere : () -> Void
    
    public var body: some View {
        VStack{
            HStack{
                LocalizedText(key: "my_location")
//                    .fontWeight(.medium)
                    .font(.system(size: 16))
                    .foregroundColor(Color("Tertiary"))
                Spacer()
                Image("share")
            }
            
            HStack{
                Image("pin_here")
                Text(currentPosInfos.address)
                    .fontWeight(.medium)
                    .font(.system(size: 12))
                    .foregroundColor(Color("Tertiary"))
                Spacer()
            }.padding(.top, 10)
            
            HStack{
                Image("my_location")
                Text("\(currentPosInfos.gpsDeciTxt) (lat, lng) \n \(currentPosInfos.gpsDmsTxt) (lat, lng)")
                    .fontWeight(.medium)
                    .font(.system(size: 12))
                    .foregroundColor(Color("Tertiary"))
                Spacer()
            }.padding(.top, 8)
            
            AppButton(action: {
                onParkHere()
            }, title: "park_here", isEnable: true).padding(.top, 16)
        }
        .padding(12)
        .background(Color.white)
        .cornerRadius(15)
        .shadow(radius: 5, x:-1, y: 1)
        .padding(16)
    }
}


