//
//  AroundLocationScreen.swift
//  iosApp
//
//  Created by USER on 19/01/2023.
//  Copyright © 2023 orgName. All rights reserved.
//

import Foundation
import shared
import SwiftUI


struct AroundLocationScreen: View {
    
    @StateObject private var viewModel = AroundLocationViewModel()
    @EnvironmentObject var appState: AppState
    @EnvironmentObject var navState: NavigationViewState
    
    public var body: some View {
        VStack(){
            HStack(){
                Image("around_location_selected")
                Text("appbar_around_location")
                    .fontWeight(.bold)
                .font(.custom("CircularStd-Medium", size: 22))
                .padding(.leading, 12)
            }.frame(maxWidth: .infinity, alignment: .leading).padding(.top, 16)
            
            Text("around_location_subtitle")
                .font(.custom("CircularStd-Medium", size: 12))
                .fontWeight(.medium)
                .foregroundColor(Color("Neutral50"))
                .frame(maxWidth: .infinity, alignment: .leading)
                .padding(.top, 16)
            
            AppTextField(onTextChange: {
                viewModel.onUserSearch(search: $0)
            }, onBackPressed: {
                navState.bottomNavSelectedTab = .mainMap
            })
            
            if (viewModel.suggestionList.isEmpty){
                Text("last_searched")
                    .font(.custom("CircularStd-Medium", size: 14))
                    .fontWeight(.medium)
                    .foregroundColor(Color.black)
                    .frame(maxWidth: .infinity, alignment: .leading)
                    .padding(.top, 54)
                
                Divider()
                
                HistoricSearch(searchs: viewModel.placeHistoric, onSearchDelete: {
                    viewModel.onDeleteSearch(search: $0)
                }, onSelectSearch: {
                    viewModel.onSelectPlace(searchSelected: $0)
                })
                
            }else{ 
                SuggestionList(places: viewModel.suggestionList, onItemClick: {
                    let currentTimeMillis = Int64(Date().timeIntervalSince1970 * 1000)
                    let search = Search(
                        categoryKey: "LOCATION",
                        searchLabel: $0.name,
                        timeStamp: currentTimeMillis,
                        lat: KotlinDouble(value: $0.location.latitude),
                        lon: KotlinDouble(value: $0.location.longitude))
                    viewModel.onSelectPlace(searchSelected: search)
                })
            }
            Spacer()

        }.padding(.horizontal, 16)
            .onReceive(viewModel.$placeSelected) { place in
            if place.name != "" {
                appState.placeSelected = place
                navState.bottomNavSelectedTab = .mainMap
            }
            }.onAppear{
                viewModel.resetSuggestionsList()
            }
    }
}  

struct HistoricSearch: View {
    
    var searchs: [Search]
    var onSearchDelete: (Search) -> Void
    var onSelectSearch: (Search) -> Void
    
    public var body: some View {
        ScrollView(){
            LazyVStack{
                ForEach(searchs , id: \.self) { search in
                    HistoricItem(
                        search: search.searchLabel,
                        onSearchDelete: { onSearchDelete(search) },
                        onSelectSearch: {
                            labelSelected in onSelectSearch(searchs[searchs.firstIndex(where: { $0.searchLabel == labelSelected })!])
                    })
                }
            }
        }
    }
}

struct HistoricItem: View {
    
    var search: String
    var onSearchDelete: () -> Void
    var onSelectSearch: (String) -> Void
    
    public var body: some View{
        
        HStack(){
            Image("historic")
                .renderingMode(.template)
                .foregroundColor(Color("Primary70"))
            Text(search).font(.custom("CircularStd-Medium", size: 16))
                .fontWeight(.medium)
                .foregroundColor(Color("Primary30"))
            Spacer()
            Image(systemName: "xmark.circle")
                .renderingMode(.template)
                .foregroundColor(Color("Secondary"))
                .onTapGesture {
                    onSearchDelete()
                }
        }
        .frame(width: .infinity, height: 48)
        .onTapGesture {
            onSelectSearch(search)
        }
    }
}

struct SuggestionList: View {
    
    var places: [Place]
    var onItemClick: (Place) -> Void
    
    public var body: some View{
        ScrollView(){
            LazyVStack{
                ForEach(places , id: \.self) { place in
                    HStack(){
                        Text(place.name).font(.custom("CircularStd-Medium", size: 16))
                            .fontWeight(.medium)
                            .foregroundColor(Color("Primary30"))
                        Spacer()
                    }
                    .frame(width: .infinity, height: 48)
                    .onTapGesture {
                        onItemClick(place)
                    }
                }
            }
        }
    }
}
