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
    
    @State(initialValue: getCurrentLang()) var lang: String
    
    @EnvironmentObject private var navState: NavigationViewState
    @State private var tabBarHeight: CGFloat = 0
    
    public var body: some View {
        if(viewModel.globalsVarsAreSet && viewModel.locationPermissionIsAsked && viewModel.locationIsObserved && viewModel.networkIsObserved){
            ZStack{
                VStack {
                    BottomBar()
                        .onReceive(locationManager.$locationStatus){ status in
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
                            viewModel.setNetworkEnable(enable: connected)
                        }
                        .environment(\.locale, .init(identifier: lang))
                    //                    .environment(\.currentMetric, .init(identifier: lang))
                        .environment(\.onLanguageUpdate, {newLanguage in lang = newLanguage})
                        .environment(\.withGpsOnly, { block in viewModel.withGpsOnly(block: block)})
                        .environment(\.withNetworkOnly, {block in viewModel.withNetworkOnly(block: block) })
                        .environment(\.showOnlyWithGps, viewModel.showOnlyWithGps)
                        .environment(\.showOnlyWithNetwork, viewModel.showOnlyWithNetwork)
                        .environment(\.onLoading, { viewModel.appIsLoading = $0 })
                }
                
                if (viewModel.globalPopupState != GlobalPopupState.Hid){
                    GlobalPopup(state: $viewModel.globalPopupState, onExit: {
                        viewModel.hideGlobalPopup()
                    })
                }
                
                if (viewModel.appIsLoading){
                    LoadingBox(onExit: { viewModel.appIsLoading = false })
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
    
    @State var lastlySelectedTab = BottomNavTab.mainMap
    @State private var tabBarHeight: CGFloat = 0
    
    @Environment(\.withGpsOnly) var withGpsOnly
    @Environment(\.showOnlyWithNetwork) var showOnlyWithNetwork
    
    @EnvironmentObject private var navState: NavigationViewState
    @EnvironmentObject private var appState: AppState
     
    var body: some View {
        
        TabView(selection: $navState.bottomNavSelectedTab) {
            NavigationView {
                MainMapScreen(bottomSheetViewModel: viewModel).bottomNav(enabled: .constant(false))
            }
            .tag(BottomNavTab.mainMap)
            .keepTabViewHeight(in: $tabBarHeight)
            
            NavigationView {
                AroundLocationScreen()
                    .bottomNav(enabled: .constant(false))
            }
            .tag(BottomNavTab.aroundLocation)
            .keepTabViewHeight(in: $tabBarHeight)
            
            NavigationView {
                PartnersScreen().bottomNav(enabled: .constant(false))
            }
            .tag(BottomNavTab.partners)
            .keepTabViewHeight(in: $tabBarHeight) 
            
            NavigationView {
                MoreScreen()
                    .bottomNav(enabled: .constant(false))
            }
            .tag(BottomNavTab.more)
            .keepTabViewHeight(in: $tabBarHeight)
            
        } 
        .hideTabViewBar
            .environment(\.tabBarHeight, tabBarHeight)
            .onReceive(navState.$bottomNavSelectedTab){ selection in
                if (selection == lastlySelectedTab && selection != .mainMap){
                    navState.bottomNavSelectedTab = .mainMap
                    lastlySelectedTab = .mainMap
                }else{
                    lastlySelectedTab = selection
                }
            }
            .sheet(isPresented: $viewModel.bottomSheetIsOpen) {
                BottomSheetController(
                    option: viewModel.bottomOption,
                    onClose: {
                        viewModel.bottomSheetIsOpen = false
                    },
                    onActiveFilterUpdate: {
                        appState.filterApplied = $0
                    },
                    onActiveSortingUpdate: {
                        appState.verticalSortingApplied = $0 })
            }
    }
}

func getCurrentLang() -> String {
    let language = KMMPreference(context: NSObject()).getString(key: "language")
    if (language != nil){
        return language!
    }else{
        return UserDefaults.standard.string(forKey: "language") ?? "en"
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




extension View {
    var hideTabViewBar: some View {
        modifier(HideTabViewBarModifier())
    }
}

struct HideTabViewBarModifier: ViewModifier {
    @Environment(\.safeAreaEdgeInsets) private var safeAreaEdgeInsets
    @Environment(\.tabBarHeight) private var tabBarHeight
    
    func body(content: Content) -> some View {
        content
            .padding(.bottom, -safeAreaEdgeInsets.bottom - tabBarHeight)
    }
}

struct SafeAreaEdgeInsetsKey: EnvironmentKey {
    static var defaultValue: EdgeInsets {
        let keyWindows = UIApplication.shared.windows.filter { $0.isKeyWindow }.first ??
        UIApplication.shared.windows[0]
        
        return keyWindows.safeAreaEdgeInsets
    }
}

extension EnvironmentValues {
    ///Should only be used once UIApplication is instantiated by the system
    var safeAreaEdgeInsets: EdgeInsets {
        self[SafeAreaEdgeInsetsKey.self]
    }
}

extension UIEdgeInsets {
    var insets: EdgeInsets {
        EdgeInsets(top: top, leading: left, bottom: bottom, trailing: right)
    }
}

extension UIWindow {
    var safeAreaEdgeInsets: EdgeInsets {
        safeAreaInsets.insets
    }
}


extension EnvironmentValues {
    ///TabView raw height; does not include bottom safe area.
    var tabBarHeight: CGFloat {
        get { self[TabBarHeightEnvironmentKey.self] }
        set { self[TabBarHeightEnvironmentKey.self] = newValue }
    }
}

struct TabBarHeightEnvironmentKey: EnvironmentKey {
    static var defaultValue: CGFloat = 0
}



extension View {
    /// Read TabView height from underlying  UITabBarController  and keep it in property passed by binding.
    ///
    /// # Usage
    /// ```
    /// @State private var tabViewHeight: CGFloat = 0
    /// TabItem().keepTabViewHeight(in: $tabViewHeight)
    /// ```
    func keepTabViewHeight(
        in storage: Binding<CGFloat>,
        includingSeparator tabViewHeightShouldIncludeSeparator: Bool = true
    ) -> some View {
        background(TabBarAccessor { tabBar in
            let onePixel: CGFloat = 1/3
            let separatorHeight: CGFloat = tabViewHeightShouldIncludeSeparator ? onePixel : 0
            DispatchQueue.main.async {
                storage.wrappedValue = tabBar.bounds.height + separatorHeight
            }
        })
    }
}

// Helper bridge to UIViewController to access enclosing UITabBarController
// and thus its UITabBar
struct TabBarAccessor: UIViewControllerRepresentable {
    var callback: (UITabBar) -> Void
    private let proxyController = ViewController()
    
    func makeUIViewController(context: UIViewControllerRepresentableContext<TabBarAccessor>) ->
    UIViewController {
        proxyController.callback = callback
        return proxyController
    }
    
    func updateUIViewController(_ uiViewController: UIViewController, context: UIViewControllerRepresentableContext<TabBarAccessor>) {
    }
    
    typealias UIViewControllerType = UIViewController
    
    private class ViewController: UIViewController {
        var callback: (UITabBar) -> Void = { _ in }
        
        override func viewWillAppear(_ animated: Bool) {
            super.viewWillAppear(animated)
            if let tabBar = self.tabBarController {
                self.callback(tabBar.tabBar)
            }
        }
    }
}
