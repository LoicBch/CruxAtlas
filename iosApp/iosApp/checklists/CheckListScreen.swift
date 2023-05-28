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
    @Environment(\.presentationMode) var presentationMode
    
    public var body: some View {
//        we could make a specific navigation view that itself set the tabbar instance and show content when this is done
        NavigationView(){
            if (tabBar != nil){
                VStack{
                    HStack{
                        Image(systemName: "arrow.left")
                            .padding(.leading, 16)
                            .onTapGesture {
                            presentationMode.wrappedValue.dismiss()
                        }
                        Spacer()
                        Text("menu_travel_checklists")
                        Spacer()
                    }.padding(.top, 12)

                    ScrollView(.horizontal){
                        HStack{
                            ForEach(viewModel.tags, id: \.0){ tag in
                                HStack{
                                    Text("#")
                                        .fontWeight(.medium)
                                        .font(.system(size: 22))
                                        .foregroundColor(tag.1 ? Color.white : Color("Tertiary"))
                                    Text(tag.0)
                                        .fontWeight(.medium)
                                        .font(.system(size: 14))
                                        .foregroundColor(tag.1 ? Color.white : Color("Tertiary"))
                                }.padding(EdgeInsets(top: 8, leading: 12, bottom: 8, trailing: 12))
                                    .background(tag.1 ? Color("Primary") : Color("GreyTags"))
                                    .cornerRadius(15)
                                    .shadow(color: Color.black.opacity(0.2), radius: 2, x: 0, y: 2)
                                    .padding(5)
                                    .onTapGesture {
                                        viewModel.selectTag(name: tag.0)
                                    }
                            }
                        }
                    }.padding(.top, 24).padding(.horizontal, 16)

                    ScrollView{
                        VStack{
                            ForEach(Array(viewModel.checklistsShowed), id: \.self){ checklist in
                                NavigationLink(destination: CheckListDetailsScreen(checklist: checklist)
                                    .navigationBarBackButtonHidden(true)
                                    .disableBottomBar(bottomBar: tabBar)
                                ) {
                                    HStack{
                                        UrlImage(url: checklist.imageLink)
                                        VStack{
                                            HStack{
                                                Text(checklist.name)
                                                    .foregroundColor(Color.black)
                                                    .fontWeight(.bold)
                                                    .font(.system(size: 16))
                                                    .padding(.top, 16)
                                                Spacer()
                                            }
                                            HStack{
                                                Text(getTagsString(tags: checklist.tags))
                                                    .foregroundColor(Color("Primary"))
                                                    .fontWeight(.medium)
                                                    .font(.system(size: 11))
                                                    .padding(.top, 8)
                                                    
                                                Spacer()
                                            }
                                            Spacer()
                                        }.padding(.leading, 8)
                                    }.background(Color.white)
                                        .cornerRadius(15)
                                        .shadow(color: Color.black.opacity(0.2), radius: 2, x: 0, y: 2)
                                        .padding(5)
                                }
                            }
                        }
                    }.padding(.horizontal, 16)
                }
            }
        }.background(TabBarAccessor { tabbar in
            self.tabBar = tabbar
        })
    }
}

func getTagsString(tags: [String]) -> String {
    return tags.map({ "#\($0)"}).joined(separator: " ") 
}
