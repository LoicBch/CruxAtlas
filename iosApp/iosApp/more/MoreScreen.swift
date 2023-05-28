//
//  MoreScreen.swift
//  iosApp
//
//  Created by USER on 19/01/2023.
//  Copyright © 2023 orgName. All rights reserved.
//

import Foundation
import shared
import SwiftUI


struct MoreScreen: View {
    
    let MenuItems = [
        MenuItem(id: UUID(), label: "menu_events", destination: { AnyView(AppSettingScreen()) }, drawable: "events", isSubMenu: false),
        MenuItem(id: UUID(), label: "menu_travel_checklists", destination: { AnyView(CheckListScreen()) }, drawable: "checklist", isSubMenu: true),
        MenuItem(id: UUID(), label: "menu_my_location", destination: { AnyView(MyLocationMapScreen()) }, drawable: "my_location", isSubMenu: true),
        MenuItem(id: UUID(), label: "menu_app_settings", destination: { AnyView(AppSettingScreen()) }, drawable: "settings", isSubMenu: false)
    ]
      
    @State private var myLocationActive = false
    @Environment(\.showOnlyWithGps) var showOnlyWithGps
    @EnvironmentObject var appState: AppState
    @EnvironmentObject var navState: NavigationViewState
    
    
    public var body: some View {
        ScrollView{
            VStack{
                HStack(){
                    Image("menu_selected")
                    Text("appbar_menu")
                        .font(.system(size: 22, weight: .bold))
                        .padding(.leading, 16)
                }.frame(maxWidth: .infinity, alignment: .leading).padding(.leading, 15).padding(.top, 16)
                
                ForEach(MenuItems.filter({!$0.isSubMenu}), id: \.id) { menuItem in
                    VStack{
                        if (menuItem.label == "menu_events"){
                            Divider().padding(.horizontal, 15)
                            MenuItemRow(menuItem: menuItem).background(Color.white).onTapGesture {
                                appState.isEventsMapOpening = true
                                navState.bottomNavSelectedTab = .mainMap
                            }
                            Divider().padding(.horizontal, 15)
                            Dropdown(content: {
                                VStack{
                                    NavigationLink (destination: CheckListScreen()
                                        .navigationBarBackButtonHidden(true)
                                    ){
                                        MenuItemRow(menuItem: MenuItems.first(where: {$0.drawable == "checklist"})!)
                                    }
                                    
                                    NavigationLink (destination:   MyLocationMapScreen().navigationBarBackButtonHidden(true)
                                                    ,
                                                    isActive: $myLocationActive,label: {
                                        MenuItemRow(menuItem: MenuItems.first(where: {$0.drawable == "my_location"})!).onTapGesture{
                                            if (showOnlyWithGps()){
                                                myLocationActive = true
                                            }else{
                                                myLocationActive = false
                                            }
                                        }
                                    })
                                }
                            }, title: "menu_travel_tools")
                        }else{
                            NavigationLink{
                                menuItem.destination()
                                    .navigationBarBackButtonHidden(true)
                            } label: {
                                MenuItemRow(menuItem: menuItem)
                            }
                        }
                        Divider().padding(.horizontal, 15)
                    }
                }
                
                ForEach(Array(Globals().menuLinks), id:\.self) { pubMenu in
                    PubMenuContainer(menuAd: pubMenu)
                    Divider().padding(.horizontal, 15)
                }
                Spacer()
            }
        }
    }
}

struct MenuItemRow : View{
    
    var menuItem: MenuItem<AnyView>
    
    public var body: some View{
        
        HStack{
            Image(menuItem.drawable)
            Text(LocalizedStringKey(menuItem.label))
                .font(.system(size: 16, weight: .bold))
                .foregroundColor(Color("Tertiary"))
                .padding(.leading, 20)
            Spacer()
            Image(systemName: "arrow.right").foregroundColor(Color("Secondary"))
        }
        .frame(width: .infinity, height: 70)
        .padding(.horizontal, 15)
    }
}

struct MenuItem<Content: View>: Identifiable {
    var id: UUID
    var label: String
    var destination: () -> Content
    let drawable: String
    let isSubMenu: Bool
}

struct PubMenuContainer: View {
    
    var menuAd: MenuLink
    
    public var body: some View{
        HStack{
            UrlImage(url: menuAd.icon)
                .frame(width: 30, height: 30)
                .scaledToFit()
            VStack{
                HStack{
                    Text(menuAd.name)
                        .font(.system(size: 16, weight: .medium))
                        .foregroundColor(Color("Tertiary"))
                    Spacer()
                }
                
                HStack{
                    Text(menuAd.subtitle)
                        .font(.system(size: 11, weight: .medium))
                        .foregroundColor(Color("Tertiary"))
                    Spacer()
                }
            }.padding(.leading, 20)
            Spacer()
            Image(systemName: "arrow.right").foregroundColor(Color("Secondary"))
        }.frame(width: .infinity, height: 70)
            .padding(.horizontal, 15)
            .onTapGesture {
                guard let url = URL(string: menuAd.url) else { return }
                guard let urlStat = URL(string: menuAd.urlstat) else { return }
                urlStat.ping(completion: {_ , _ in})
                UIApplication.shared.open(url)
            }
    }
}
