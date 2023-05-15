//
//  HomeScreen.swift
//  iosApp
//
//  Created by USER on 19/01/2023.
//  Copyright © 2023 orgName. All rights reserved.
//

import Foundation
import shared
import SwiftUI
import CoreLocation

struct HomeScreen: View {
    
    @StateObject private var viewModel = SplashScreenViewModel()
    @ObservedObject private var locationManager = LocationInitializer()
    @ObservedObject private var networkMonitor = NetworkMonitor()
    
    public var body: some View {
        if(viewModel.globalsVarsAreSet && viewModel.locationPermissionIsAsked && viewModel.locationIsObserved && viewModel.networkIsObserved){
            ZStack{
                VStack {
                    BottomBar().onReceive(locationManager.$locationStatus){ status in
                        switch (status){
                        case .notDetermined: break;
                        case .restricted :
                            viewModel.setLocationEnabled(enable: false)
                            viewModel.showSliderState(state: SliderState.GpsMissing)
                        case .denied :
                            viewModel.setLocationEnabled(enable: false)
                            viewModel.showSliderState(state: SliderState.GpsMissing)
                        case.authorizedAlways:
                            viewModel.setLocationEnabled(enable: true)
                            viewModel.removeGpMissingsNotification()
                            break;
                        case.authorizedWhenInUse:
                            viewModel.setLocationEnabled(enable: true)
                            viewModel.removeGpMissingsNotification()
                            break;
                        case .none:
                            break
                        case .some(_):
                            break
                        }
                    }.onReceive(networkMonitor.$isConnected){ connected in
                        print("NETWORK \(connected)")
                        viewModel.setNetworkEnable(enable: connected)
                    }
                    .environment(\.withGpsOnly, { block in viewModel.withGpsOnly(block: block)})
                    .environment(\.withNetworkOnly, {block in viewModel.withNetworkOnly(block: block) })
                    .environment(\.showOnlyWithGps, viewModel.showOnlyWithGps)
                    .environment(\.showOnlyWithNetwork, viewModel.showOnlyWithNetwork)
                }
                
                if (viewModel.globalPopupState != GlobalPopupState.Hid){
                    GlobalPopup(state: $viewModel.globalPopupState, onExit: {
                        viewModel.hideGlobalPopup()
                    })
                }
                
                GlobalSlider(
                    state: $viewModel.globalSliderState,
                    onRemoveGpsNotification: {},
                    onRemoveNetworkNotification: {}
                ).padding(EdgeInsets(top: 100, leading: 16, bottom: 0, trailing: 16))
            }
        }else{
            ZStack{
                Color("SplashScreenBackground").ignoresSafeArea()
                Image("logo")
            }.onAppear{
                viewModel.startSetup()
            }.onReceive(locationManager.$locationStatus){ status in
                switch (status){
                case .notDetermined:
                    viewModel.locationIsObserved = false
                    viewModel.setLocationEnabled(enable: false)
                    print("LOCATION NOT DERTERMINED")
                case .restricted :
                    viewModel.locationIsObserved = true
                    viewModel.setLocationEnabled(enable: false)
                    print("LOCATION RESTRICTED")
                case .denied :
                    print("LOCATION DENIED")
                    viewModel.setLocationEnabled(enable: false)
                    viewModel.locationIsObserved = true
                    print("LOCATION DENIED")
                case.authorizedAlways:
                    print("LOCATION AUTHORIZE")
                    viewModel.setLocationEnabled(enable: true)
                    break;
                case.authorizedWhenInUse:
                    viewModel.setLocationEnabled(enable: true)
                    print("LOCATION AUTHORIZE")
                    break;
                case .none:
                    break
                case .some(_):
                    break
                }
            }.onChange(of: locationManager.lastLocation){ location in
                if location != nil{
                    viewModel.startObservingLocation(location: location!)
                }
            }.onReceive(networkMonitor.$isConnected){ connected in
                viewModel.networkIsObserved = true
                viewModel.setNetworkEnable(enable: connected)
            }
        }
    }
}

struct BottomBar: View {
    
    @ObservedObject var viewModel = BottomSheetViewModel()
    
    @State private var selection = 0
    
    @State private var placeSelected = Place(name: "", location: Location(latitude: 0.0, longitude: 0.0))
    @State private var isEventsMapOpening = false
    @State private var isAroundMeClicked = false
    @State private var filterApplied = Filter(category: FilterType.unselectedDealer, filterId: "", isSelected: false)
    @State private var verticalSortingApplied = SortingOption.none
    
    @Environment(\.withGpsOnly) var withGpsOnly
    @Environment(\.showOnlyWithNetwork) var showOnlyWithNetwork
    
    
    var body: some View {
        TabView(selection: $selection.onUpdate {
            if (showOnlyWithNetwork()){
                if (selection == 0){ 
                        withGpsOnly {
                            isAroundMeClicked = true
                        }
                    }
            }else{
                selection = 0
            }
        }) {
            
            MainMapScreen(bottomSheetViewModel: viewModel,
                          placeSelected: $placeSelected,
                          isEventsMapOpening : $isEventsMapOpening,
                          isAroundMeClicked: $isAroundMeClicked,
                          activeFilter: $filterApplied,
                          verticalSorting: $verticalSortingApplied)
            .tabItem {
                Image("around_me").frame(width: 32, height: 32)
                LocalizedText(key: "appbar_around_me")
            }.tag(0)
            
            AroundLocationScreen(tabSelected: $selection, placeSelected: $placeSelected)
                .tabItem {
                    Image("around_location").frame(width: 32, height: 32)
                    LocalizedText(key: "appbar_around_location")
                }.tag(1)
            
            
            PartnersScreen()
                .tabItem {
                    Image("partners").frame(width: 32, height: 32)
                    LocalizedText(key: "appbar_partners")
                }.tag(2)
            
            
            MoreScreen(isEventsMapOpening: $isEventsMapOpening, tabSelected: $selection)
                .tabItem {
                    Image("menu").frame(width: 32, height: 32)
                    LocalizedText(key: "appbar_menu")
                }.tag(3)
            
        }.onAppear(perform: {
            selection = 0
        })
        .edgesIgnoringSafeArea(.bottom)
        .sheet(isPresented: $viewModel.bottomSheetIsOpen) {
            BottomSheetController(
                option: viewModel.bottomOption,
                onClose: { viewModel.bottomSheetIsOpen = false },
                onActiveFilterUpdate: { filterApplied = $0 },
                onActiveSortingUpdate: { verticalSortingApplied = $0 })
        }
    }
}

extension Binding {
    func onUpdate(_ closure: @escaping () -> Void) -> Binding<Value> {
        Binding(get: {
            wrappedValue
        }, set: { newValue in
            wrappedValue = newValue
            closure()
        })
    }
}
