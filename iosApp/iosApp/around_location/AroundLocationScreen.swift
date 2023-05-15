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
    @Binding var tabSelected: Int
    @Binding var placeSelected: Place
    
    public var body: some View {
        VStack(){
            HStack(){
                Image("around_location_selected")
                LocalizedText(key: "appbar_around_location")
                .font(.system(size: 22, weight: .bold))
                .padding(.leading, 16)
            }.frame(maxWidth: .infinity, alignment: .leading)
            
            LocalizedText(key: "around_location_subtitle")
                .font(.system(size: 12, weight: .medium))
                .foregroundColor(Color("NeutralText"))
                .frame(maxWidth: .infinity, alignment: .leading)
                .padding(.top, 16)
            
            AppTextField(onTextChange: {
                viewModel.onUserSearch(search: $0)
                placeSelected = Place(name: "TEST", location: Location(latitude: 0.0, longitude: 0.0))
            }).padding(.top, 12)
            
            if (viewModel.suggestionList.isEmpty){
                LocalizedText(key: "last_searched")
                    .font(.system(size: 14, weight: .medium))
                    .foregroundColor(Color("Tertiary"))
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
            placeSelected = place
            tabSelected = 0
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
                .foregroundColor(Color("Primary"))
            Text(search).font(.system(size: 16, weight: .medium)).foregroundColor(Color.gray)
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
                        Text(place.name).font(.system(size: 16, weight: .medium)).foregroundColor(Color.gray)
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
