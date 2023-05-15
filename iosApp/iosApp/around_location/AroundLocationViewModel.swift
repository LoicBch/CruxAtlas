//
//  AroundLocationViewModel.swift
//  iosApp
//
//  Created by USER on 14/04/2023.
//  Copyright © 2023 orgName. All rights reserved.
//

import Foundation
import shared

extension AroundLocationScreen {
    @MainActor class AroundLocationViewModel : ObservableObject {
        
        @Published private(set) var suggestionList = [Place]()
        @Published private(set) var placeHistoric = [Search]()
        @Published private(set) var placeSelected = Place(name: "", location: Location(latitude: 0.0, longitude: 0.0))
        
        init(){
           loadPlaceHistoric()
        }
        
        func onUserSearch(search: String){
            if(search.count > Constants().SUGGESTION_START_LENGTH){
                Task.init {
                    do {
                        suggestionList = try await RFetchSuggestionsFromInput().execute(input: search)!
                    } catch {
                        
                    }
                }
            } else{
                suggestionList = []
            }
        }
            
            func onSelectPlace(searchSelected: Search){
                Task.init {
                    do {
                        try await RAddSearch().execute(search: searchSelected)
                        placeSelected = Place(name: searchSelected.searchLabel, location: Location(latitude: searchSelected.lat as! Double, longitude: searchSelected.lon as! Double))
                    } catch {
                        
                    }
                }
            }
            
            func onDeleteSearch(search: Search){
                Task.init {
                    do {
                        try await RDeleteSearch().execute(search: search)
                        placeHistoric.removeAll(where: {$0.searchLabel == search.searchLabel})
                    } catch {
                        
                    }
                }
            }
            
            func loadPlaceHistoric(){
                Task.init {
                    do {
                        let allSearch = try await RGetAllSearchForACategory().execute(searchCategoryKey: Constants.Persistence().SEARCH_CATEGORY_LOCATION)!
                        let uniqueSearch = Dictionary(grouping: allSearch, by: { $0.searchLabel })
                            .compactMap { $0.value.first }
                        placeHistoric = Array(uniqueSearch.prefix(3))
                    } catch {
                        
                    }
                }
            }
        }
    }
