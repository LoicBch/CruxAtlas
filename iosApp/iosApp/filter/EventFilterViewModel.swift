//
//  EventFilterViewModel.swift
//  iosApp
//
//  Created by USER on 01/05/2023.
//  Copyright © 2023 orgName. All rights reserved.
//

import Foundation
import shared

extension EventFilterScreen{
    @MainActor class EventFilterViewModel : ObservableObject{
        
        @Published private(set) var eventFiltersUsed = [Filter]()
        @Published var eventFilterSelected: Filter = Filter(category: FilterType.unselectedEvent, filterId: "", isSelected: false)
        @Published private(set) var isLoading = false
        
        init(){
            getHistoricFilters()
        }
        
        func getHistoricFilters(){
            isLoading = true
            Task.init {
                do {
                    
                    let completeFilterHistoric = try await RGetFiltersSaved().execute()
                    let eventFilterHistoric = completeFilterHistoric?.filter({$0.category == FilterType.countries}) ?? []
                    let uniqueEventFilterHistoric = Dictionary(grouping: eventFilterHistoric, by: { $0.filterName })
                        .compactMap { $0.value.first }
                    eventFilterSelected = eventFilterHistoric.first(where: {$0.isSelected})!
                    eventFiltersUsed = Array(uniqueEventFilterHistoric.prefix(3))
                    
                    isLoading = false
                } catch {
                    // handle error
                }
            }
        }
        
        func applyFilter(){
            isLoading = true
            
            if (eventFilterSelected.category == FilterType.countries){
                eventFiltersUsed.append(eventFilterSelected)
            }
            
            Task.init {
                do {
                    try await RApplyPlacesFilters().execute(filter: Filter(category: eventFilterSelected.category, filterId: eventFilterSelected.filterId, isSelected: true))
                    isLoading = false
                } catch {
                    // handle error
                }
            }
        }
        
        func deleteFilter(filter: Filter){
            isLoading = true
            Task.init {
                do {
                    try await RDeleteFilter().execute(filter: filter)
                    eventFiltersUsed.remove(object: filter)
                    isLoading = false
                } catch {
                    // handle error
                }
            }
        }
        
        func onFilterCategorySelected(category: FilterType) {
            eventFilterSelected = Filter(category: category, filterId: "", isSelected: false)
        }
        
        func onFilterOptionSelected(filterName: String) {
            eventFilterSelected = Filter( category: eventFilterSelected.category, filterId: eventFilterSelected.getIdFromFilterName(name: filterName), isSelected: false)
        }
    }
}
