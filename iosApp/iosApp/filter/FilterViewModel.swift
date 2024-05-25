//
//  FilterViewModel.swift
//  iosApp
//
//  Created by USER on 26/04/2023.
//  Copyright © 2023 orgName. All rights reserved.
//

import Foundation
import shared

extension DealerFilterScreen {
    @MainActor class DealerFilterViewModel : ObservableObject {
        
        @Published var dealerFiltersUsed = [Filter]()
        @Published  var dealerFilterSelected = Filter(category: FilterType.unselectedDealer, filterId: "", isSelected: false)
        @Published private(set) var isLoading = false
        @Published private(set) var filterApplied = Filter(category: FilterType.unselectedDealer, filterId: "-1", isSelected: false)
        @Published var categorySelectedId = ""
        @Published var unselectedButtonLabel = ""
        
        init(){
            getHistoricFilters()
        }
        
        func getHistoricFilters(){
            isLoading = true
            Task.init {
                do {
                    let completeFilterHistoric = try await RGetFiltersSaved().execute()
                    let dealerFilterHistoric = completeFilterHistoric?.filter({$0.category != FilterType.countries}) ?? []
                    let uniqueDealerFilterHistoric = Dictionary(grouping: dealerFilterHistoric, by: { $0.filterName })
                        .compactMap { $0.value.first }
                    if (!uniqueDealerFilterHistoric.isEmpty && !dealerFilterHistoric.isEmpty){
                        if let dealerFilterSelectedd = dealerFilterHistoric.first(where: { $0.isSelected}) {
                            dealerFilterSelected = dealerFilterSelectedd
                            
                            let categoryHistoric = uniqueDealerFilterHistoric.filter({$0.category == dealerFilterSelectedd.category })
                            dealerFiltersUsed = Array(categoryHistoric.prefix(3))
                            categorySelectedId = getCategoryIdFromCategory(type: dealerFilterSelected.category)
                        } else {
                            dealerFiltersUsed = Array(uniqueDealerFilterHistoric.prefix(3))
                            dealerFilterSelected = Filter(category: FilterType.unselectedDealer, filterId: "", isSelected: false)
                        }
                    }
                    
                    isLoading = false
                } catch {
                    // handle error
                }
            }
        }
        
        func applyFilter(){
            isLoading = true
            
            if (dealerFilterSelected.category == FilterType.countries || dealerFilterSelected.category == FilterType.brand){
                dealerFiltersUsed.append(dealerFilterSelected)
            }
            
            filterApplied = Filter(category: dealerFilterSelected.category, filterId: dealerFilterSelected.filterId, isSelected: true)
            
            Task.init {
                do {
                    try await RApplyPlacesFilters().execute(filter: Filter(category: dealerFilterSelected.category, filterId: dealerFilterSelected.filterId, isSelected: true))
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
                    dealerFiltersUsed.remove(object: filter)
                    isLoading = false
                } catch {
                    // handle error
                }
            }
        }
        
        func onFilterCategorySelected(category: FilterType) {
            dealerFilterSelected = Filter(category: category, filterId: "", isSelected: false) 
            Task.init {
                do {
                    let completeFilterHistoric = try await RGetFiltersSaved().execute()
                    let dealerFilterHistoric = completeFilterHistoric?.filter({$0.category == category}) ?? []
                    let uniqueDealerFilterHistoric = Dictionary(grouping: dealerFilterHistoric, by: { $0.filterName })
                        .compactMap { $0.value.first }
                    
                    if let dealerFilterSelectedd = dealerFilterHistoric.first(where: { $0.isSelected}) {
                        dealerFilterSelected = dealerFilterSelectedd
                    }else{
//                        dealerFilterSelected = Filter(category: FilterType.unselectedDealer, filterId: "", isSelected: false)
                    }
                    
                    if (!uniqueDealerFilterHistoric.isEmpty && !dealerFilterHistoric.isEmpty){
                        dealerFiltersUsed = Array(uniqueDealerFilterHistoric.prefix(3))
                    }else{
                        dealerFiltersUsed = []
                    }
                     
                } catch {
                    // handle error
                }
            }
            
        }
        
        func onFilterOptionSelected(filterName: String) {
            dealerFilterSelected = Filter( category: dealerFilterSelected.category, filterId: dealerFilterSelected.getIdFromFilterName(name: filterName), isSelected: false)
        }
        
        private func getCategoryIdFromCategory(type: FilterType) -> String{
            var filterId: String = ""
            switch (type){
            case FilterType.service:
                filterId = "Repair, upgrade or buy motorhome"
            case FilterType.brand:
                filterId = "Find official motorhome dealers"
            default:
                filterId = ""
            }
            return filterId
        }
    }
}
