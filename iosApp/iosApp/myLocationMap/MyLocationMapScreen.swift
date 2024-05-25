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
    @Environment(\.presentationMode) var presentationMode
    
    public var body: some View {
        VStack{
            HStack{
                Image(systemName: "arrow.left").onTapGesture {
                    presentationMode.wrappedValue.dismiss()
                }.padding(.leading, 20)
                Spacer()
                Text("menu_my_location").font(.custom("CircularStd-Medium", size: 16)).fontWeight(.medium).foregroundColor(Color.black)
                Spacer()
                Image(systemName: "arrow.left")
                    .padding(.leading, 20)
                    .opacity(0)
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
                Image(systemName: "arrow.left").onTapGesture {
                    onExit()
                }
                Spacer()
                Text("Are you sure")
                    .font(.custom("CircularStd-Medium", size: 16))
                    .fontWeight(.medium)
                    .foregroundColor(Color.black)
                Spacer()
                Image(systemName: "xmark").opacity(0)
            }
            Text("delete_location_popup").font(.custom("CircularStd-Medium", size: 12)).fontWeight(.medium).foregroundColor(Color("Tertiary50"))
                .padding(.top, 14)
            
            Button(action: {
                onDelete()
            }){
                Spacer()
                Image(systemName:"trash").renderingMode(.template).foregroundColor(Color.white)
                Text("delete")
                    .font(.custom("CircularStd-Medium", size: 14))
                    .fontWeight(.medium)
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
                Text("my_parking")
                    .fontWeight(.medium)
                    .font(.custom("CircularStd-Medium", size: 16))
                    .foregroundColor(Color("Tertiary10"))
                Spacer()
                Image(systemName: "trash.fill").renderingMode(.template).foregroundColor(Color("Tertiary30"))
                    .onTapGesture {
                    onDeleteParkingLocation()
                }.padding(.trailing, 10)
                Image("share").renderingMode(.template).foregroundColor(Color("Tertiary30"))
                    .onTapGesture {
                    share(shared: "\(parkingLocation.address)\n\n \(parkingLocation.gpsDeciTxt)\n\(parkingLocation.gpsDmsTxt)")
                }
            }
            
            HStack{
                Image("distance").renderingMode(.template).foregroundColor(Color("Tertiary50"))
                Text(Location(latitude: parkingLocation.lat, longitude: parkingLocation.lon).distanceFromUserLocationText())
                    .fontWeight(.medium)
                    .font(.custom("CircularStd-Medium", size: 12))
                    .foregroundColor(Color("Tertiary50"))
                Spacer()
            }.padding(.top, 10)
            
            HStack{
                Image("pin_here").renderingMode(.template).foregroundColor(Color("Tertiary50"))
                Text(parkingLocation.address)
                    .fontWeight(.medium)
                    .font(.custom("CircularStd-Medium", size: 12))
                    .foregroundColor(Color("Tertiary50"))
                Spacer()
            }.padding(.top, 8)
            
            HStack{
                Image("my_location").renderingMode(.template).foregroundColor(Color("Tertiary50"))
                Text("\(parkingLocation.gpsDeciTxt) (lat, lng) \n \(parkingLocation.gpsDmsTxt) (lat, lng)")
                    .fontWeight(.medium)
                    .font(.custom("CircularStd-Medium", size: 12))
                    .foregroundColor(Color("Tertiary50"))
                Spacer()
            }.padding(.top, 8)
            
            AppIconButton(action: {
                onNavigate()
            }, title: "navigate", icon: "my_location", isEnable: true).padding(.top, 16)
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
                Text("my_location")
                    .fontWeight(.medium)
                    .font(.custom("CircularStd-Medium", size: 16))
                    .foregroundColor(Color("Tertiary10"))
                Spacer()
                Image("share").renderingMode(.template).foregroundColor(Color("Tertiary30"))
                    .onTapGesture {
                    share(shared: "\(currentPosInfos.address)\n\n \(currentPosInfos.gpsDeciTxt)\n\(currentPosInfos.gpsDmsTxt)" )
                }
            }
            
            HStack{
                Image("pin_here").renderingMode(.template).foregroundColor(Color("Tertiary50"))
                Text(currentPosInfos.address)
                    .fontWeight(.medium)
                    .font(.custom("CircularStd-Medium", size: 12))
                    .foregroundColor(Color("Tertiary50"))
                Spacer()
            }.padding(.top, 10)
            
            HStack{
                Image("my_location").renderingMode(.template).foregroundColor(Color("Tertiary50"))
                Text("\(currentPosInfos.gpsDeciTxt) (lat, lng) \n \(currentPosInfos.gpsDmsTxt) (lat, lng)")
                    .fontWeight(.medium)
                    .font(.custom("CircularStd-Medium", size: 12))
                    .foregroundColor(Color("Tertiary50"))
                Spacer()
            }.padding(.top, 8)
            
            AppIconButton(action: {
                onParkHere()
            }, title: "park_here", icon: "parking", isEnable: true).padding(.top, 16)
        }
        .padding(12)
        .background(Color.white)
        .cornerRadius(15)
        .shadow(radius: 5, x:-1, y: 1)
        .padding(16)
    }
}


