//
//  CheckListScreen.swift
//  iosApp
//
//  Created by USER on 13/04/2023.
//  Copyright © 2023 orgName. All rights reserved.
//

import Foundation
import shared
import SwiftUI

struct CheckListScreen: View {
    
    @StateObject private var viewModel = CheckListsViewModel()
    @State private var tabBar: UITabBar! = nil
    
    public var body: some View {
//        we could make a specific navigation view that itself set the tabbar instance and show content when this is done
        NavigationView(){
            if (tabBar != nil){
                VStack{
                    HStack{
                        Image(systemName: "xmark").onTapGesture {
                            
                        }
                        Spacer()
                        LocalizedText(key: "menu_travel_checklists")
                        Spacer()
                    }.padding(.top, 12)
                    
                    ScrollView(.horizontal){
                        HStack{
                            ForEach(viewModel.tags, id: \.0){ tag in
                                HStack{
                                    Text("#")
                                        .fontWeight(.medium)
                                        .font(.system(size: 22))
                                        .foregroundColor(Color("Tertiary"))
                                    Text(tag.0)
                                        .fontWeight(.medium)
                                        .font(.system(size: 14))
                                        .foregroundColor(Color("Tertiary"))
                                }.background(Color.white)
                                    .cornerRadius(15)
                                    .shadow(radius: 5, x:-1, y: 1)
                                    .padding(5)
                                    .onTapGesture {
                                        viewModel.selectTag(name: tag.0)
                                    }
                            }
                        }
                    }.padding(.top, 24)
                    
                    ScrollView{
                        VStack{
                            ForEach($viewModel.checklistsShowed, id: \.self){ checklist in
                                NavigationLink(destination: CheckListDetailsScreen(checklist: checklist)
                                    .navigationBarBackButtonHidden(true)
                                    .disableBottomBar(bottomBar: tabBar)
                                ) {
                                    HStack{
                                        UrlImage(url: "")
                                        VStack{
                                            Text("checklist.name").padding(.top, 16)
                                            Text("checklist.tags").padding(.top, 8)
                                        }.padding(.leading, 6)
                                    }.background(Color.white)
                                        .cornerRadius(15)
                                        .shadow(radius: 5, x:-1, y: 1)
                                        .padding(5)
                                }
                            }
                        }
                    }
                }
            }
        }.background(TabBarAccessor { tabbar in
            self.tabBar = tabbar
        })
    }
}

