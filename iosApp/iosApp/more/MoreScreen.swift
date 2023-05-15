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
    
    @Binding var isEventsMapOpening: Bool
    @Binding var tabSelected: Int
    
    @State private var tabBar: UITabBar! = nil
    @State private var myLocationActive = false
    
    @Environment(\.showOnlyWithGps) var showOnlyWithGps
    
    public var body: some View {
            VStack{
                HStack(){
                    Image("menu_selected")
                    LocalizedText(key: "appbar_menu")
                        .font(.system(size: 22, weight: .bold))
                        .padding(.leading, 16)
                }.frame(maxWidth: .infinity, alignment: .leading).padding(.leading, 15)
                NavigationView(){
                    if(tabBar != nil){
                    VStack{
                        ForEach(MenuItems.filter({!$0.isSubMenu}), id: \.id) { menuItem in
                            VStack{
                                if (menuItem.label == "menu_events"){
                                    MenuItemRow(menuItem: menuItem).onTapGesture {
                                        isEventsMapOpening = true
                                        tabSelected = 0
                                    }
                                    Divider().padding(.horizontal, 15)
                                    Dropdown(content: {
                                        VStack{
                                            NavigationLink (destination: CheckListScreen()
                                                .navigationBarBackButtonHidden(true)
                                                .disableBottomBar(bottomBar: tabBar)){
                                                    MenuItemRow(menuItem: MenuItems.first(where: {$0.drawable == "checklist"})!)
                                            }
                                             
                                            NavigationLink (destination:   MyLocationMapScreen().navigationBarBackButtonHidden(true)
                                                .disableBottomBar(bottomBar: tabBar),
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
                                            .disableBottomBar(bottomBar: tabBar)
                                    } label: {
                                        MenuItemRow(menuItem: menuItem)
                                    }
                                }
                                Divider().padding(.horizontal, 15)
                            }
                        }
                        //            activate when SplashScreen is done
                        //            ForEach(menuPubItem) { pubMenu in
                        //                PubMenuContainer(menuAd: pubMenu)
                        //                Divider()
                        //            }
                        Spacer()
                    }
                }
                }.background(TabBarAccessor { tabbar in
                    self.tabBar = tabbar
                })
            }
    }
    
    struct MenuItemRow : View{
        
        var menuItem: MenuItem<AnyView>
        
        public var body: some View{
            
            HStack{
                Image(menuItem.drawable)
                LocalizedText(key: menuItem.label)
                    .font(.system(size: 16, weight: .medium))
                    .foregroundColor(Color("Tertiary"))
                    .padding(.leading, 20)
                Spacer()
                Image(systemName: "chevron.right").foregroundColor(Color("Secondary"))
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
        
        var menuAd: MenuLinkIdentifiable
        
        public var body: some View{
            HStack{
                UrlImage(url: menuAd.urlImage)
                    .frame(width: 30, height: 30)
                    .scaledToFit()
                VStack{
                    
                    Text(menuAd.title)
                        .font(.system(size: 16, weight: .medium))
                        .foregroundColor(Color("Tertiary"))
                    
                    Text(menuAd.subtitle)
                        .font(.system(size: 11, weight: .medium))
                        .foregroundColor(Color("Tertiary"))
                    
                }.padding(.leading, 20)
                Spacer()
                Image(systemName: "chevron.right").foregroundColor(Color("Secondary"))
            }
            .padding(.horizontal, 15)
            .onTapGesture {
                guard let url = URL(string: menuAd.click) else { return }
                UIApplication.shared.open(url)
            }
        }
    }
    
    struct MenuLinkIdentifiable: Identifiable{
        var id: UUID
        var title: String
        var subtitle: String
        var urlImage: String
        var click: String
    }
    
    func toIdentifiable(menuLinks: [MenuLink]) -> [MenuLinkIdentifiable]{
        
        var identifiables: [MenuLinkIdentifiable] = []
        
        menuLinks.forEach({
            identifiables.append(MenuLinkIdentifiable(id: UUID(), title: $0.name, subtitle: $0.subtitle, urlImage: $0.icon, click: $0.urlstat))
        })
        return identifiables
    }
}
