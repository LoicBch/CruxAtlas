//
//  filterScreen.swift
//  iosApp
//
//  Created by USER on 26/01/2023.
//  Copyright © 2023 orgName. All rights reserved.
//

import Foundation
import shared
import SwiftUI

struct DealerFilterScreen: View {
    
    @State var isSelectingOption = false
    @StateObject var viewModel: DealerFilterViewModel = DealerFilterViewModel()
    var onClose: () -> Void
    var onFilterApplied: (Filter) -> Void
    
    public var body: some View {
        
        if (isSelectingOption){
            filtersOptions(
                categorySelected: viewModel.dealerFilterSelected.category,
                onItemTap: {
                    viewModel.onFilterOptionSelected(filterName: $0)
                    isSelectingOption = false
                },
                onFilterOptionSelected: {viewModel.onFilterOptionSelected(filterName: $0)},
                dealerFilterUsed: viewModel.dealerFiltersUsed,
                onSearchDelete: {viewModel.deleteFilter(filter: $0)}
            )
        }else{
            filtersCategories(
                onChooseOptionsTap: { isSelectingOption = true },
                onCategorySelected: { category in
                    viewModel.onFilterCategorySelected(category: category)
                },
                dealerFiltersUsed: viewModel.dealerFiltersUsed,
                onDealerOptionSelected: { viewModel.onFilterOptionSelected(filterName: $0) },
                onFilterDelete: { viewModel.deleteFilter(filter: $0) },
                onApplyFilter: {
                    onClose()
                    viewModel.applyFilter()
                },
                filterDealerSelected: $viewModel.dealerFilterSelected
            ).onReceive(viewModel.$filterApplied){
                onFilterApplied($0)
            }
        }
    }
}

struct filtersOptions: View{
    
    var categorySelected: FilterType
    var onItemTap: (String)->Void
    var onFilterOptionSelected: (String)->Void
    var dealerFilterUsed: [Filter]
    var onSearchDelete: (Filter)->Void
    
    @State var input = ""
    
    public var body: some View{
        VStack{
            HStack{
                Image(systemName: "xmark").onTapGesture {
                    
                }
                Spacer()
                Text(getTitleLabel(for:categorySelected))
                Spacer()
            }.padding(.top, 12)
            
            AppTextField(onTextChange: { input in
                self.input = input
            }).padding(.top, 22)
            
            
            if (input.isEmpty){
                HistoricSearchList(filterSearchs: dealerFilterUsed, onSelectSearch: { filterName in
                    onFilterOptionSelected(filterName)
                }, onDeleteSearch: { filter in
                    onSearchDelete(filter)
                })
            }
            
            HStack{
                Text(getHistoricLabel(for:categorySelected))
                    .fontWeight(.black)
                    .font(.system(size: 14))
                    .foregroundColor(Color.black)
                Spacer()
            }.padding(.top, 35)
            Divider()
            ResultList(results: getItemsFromFilterCategory(category: categorySelected, input: input), onItemClick: { result in
                onItemTap(result)
            })
            Spacer()
            
        }.padding(.horizontal, 18)
    }
    
    func getItemsFromFilterCategory(category: FilterType, input: String) -> [String]{
        var list: [String] = []
        if (input.isEmpty){
            switch(categorySelected){
            case .service:
                list = Globals.filters.shared.services.map({$0.second! as String})
            case .brand:
                list = Globals.filters.shared.brands.map({$0.second! as String})
            default:
                list = []
            }
        }else{
            list =  makeListFromInput(input: input, categorySelected: categorySelected)
        }
        
        return list
    }
    
    func makeListFromInput(input: String, categorySelected: FilterType) -> [String] {
         
        var lisToCompare: [String] = []
        switch(categorySelected){
        case .service:
            lisToCompare = Globals.filters().services.map({$0.second! as String})
        case .brand:
            lisToCompare = Globals.filters().brands.map({$0.second! as String})
        default:
            lisToCompare = []
        }
        
        return lisToCompare.filter {
            $0.lowercased().hasPrefix(input.lowercased())
        }
    }
    
    func getTitleLabel(for category: FilterType) -> String {
        print(category)
        switch category {
        case .service:
            return "service"
        case .brand:
            return "brand"
        default:
            return ""
        }
    }
    
    func getHistoricLabel(for category: FilterType) -> String {
        if (!input.isEmpty){
            return "resultList"
        }else{
            switch category {
            case .service:
                return "All motorhome services"
            case .brand:
                return "All motorhome brands"
            default:
                return ""
            }
        }
    }
}

struct ResultList: View {
    
    var results: [String]
    var onItemClick: (String)->Void
    
    public var body: some View{
        ScrollView{
            VStack{
                ForEach(results, id: \.self){ result in
                    HStack(alignment: .center){
                        Image(systemName: "magnifyingglass")
                        Text(result)
                        Spacer()
                    }.frame(width: .infinity, height: 48).onTapGesture {
                        onItemClick(result)
                    }
                }
            }
        }
    }
}
 

struct FilterSelectionBox: View{
    
    @Binding var buttonLabel: String
    
    var onTap: () -> Void
    
    public var body: some View{
        
            HStack{
                Text(buttonLabel)
                    .fontWeight(.medium)
                    .font(.system(size: 16))
                    .foregroundColor(Color("Tertiary"))
                    .padding(.leading, 10)
                Spacer()
                Image(systemName: "arrow.up.and.down")
                    .padding(.trailing, 10)
            }
            .padding(.vertical, 15)
            .overlay(
                RoundedRectangle(cornerRadius: 5)
                    .stroke(Color("Tertiary"), lineWidth: 2)
            ).onTapGesture {
                onTap()
            }
    }
}

func radiobuttonToFilterType(radiobuttonId: String) -> FilterType{
    var filterType = FilterType.unselectedDealer
    switch(radiobuttonId){
    case "Repair, upgrade or buy motorhome":
        filterType = FilterType.service
    case "Find official motorhome dealers":
        filterType = FilterType.brand
    default:
        filterType = FilterType.unselectedDealer
    }
    return filterType
}

func filterTypeToRadioButton(filter: Filter) -> String {
    switch(filter.category){
    case .service:
        return "Repair, upgrade or buy motorhome"
    case .brand:
        return "Find official motorhome dealers"
    default:
        return ""
    }
}

struct filtersCategories: View{
    
    @State var buttonLabel = ""
    @State var categorySelectedId = ""
    var onChooseOptionsTap: ()->Void
    var onCategorySelected: (FilterType)->Void
    var dealerFiltersUsed: [Filter]
    var onDealerOptionSelected: (String)->Void
    var onFilterDelete: (Filter)->Void
    var onApplyFilter: ()->Void
    @Binding var filterDealerSelected: Filter
     
    
    public var body: some View{
        VStack{
            HStack{
                Image(systemName: "xmark").onTapGesture {
                    
                }
                Spacer()
                LocalizedText(key: "filters_title")
                Spacer()
                Image("reload").onTapGesture {
                    onCategorySelected(FilterType.unselectedDealer)
                }
            }.padding(.top, 12)
            
            HStack{
                LocalizedText(key: "step_1")
//                    .fontWeight(.medium)
                    .font(.system(size: 12))
                    .foregroundColor(Color.gray)
                    .padding(.top, 60)
                Spacer()
            }
            
            HStack{
                LocalizedText(key: "filter_step1_title")
//                    .fontWeight(.black)
                    .font(.system(size: 22))
                    .foregroundColor(Color.black)
                Spacer()
            }.padding(.top, 8)
            
            Divider()
            
            RadioButtonGroup(items:[("Repair, upgrade or buy motorhome","repair"),("Find official motorhome dealers","dealers")], selectedId: $categorySelectedId) { selected in
                onCategorySelected(radiobuttonToFilterType(radiobuttonId: selected))
            }
            
            if(filterDealerSelected.category != FilterType.unselectedDealer){
                
                HStack{
                    LocalizedText(key: "step_2")
//                        .fontWeight(.medium)
                        .font(.system(size: 12))
                        .foregroundColor(Color.gray)
                        .padding(.top, 60)
                    Spacer()
                }
                
                HStack{
                    LocalizedText(key: "filter_step2_title")
//                        .fontWeight(.black)
                        .font(.system(size: 22))
                        .foregroundColor(Color.black)
                    Spacer()
                }.padding(.top, 8)
                Divider()
                
                if(filterDealerSelected.filterId != ""){

                    MaterialTextField(buttonLabel: $buttonLabel, onTap: {
                        onChooseOptionsTap()
                    }).padding(.top, 15)
                } else {
                    FilterSelectionBox(buttonLabel: $buttonLabel, onTap: {
                        onChooseOptionsTap()
                    }).padding(.top, 15)
                }
                 
                HistoricSearchList(filterSearchs: dealerFiltersUsed, onSelectSearch: { filterName in
                    onDealerOptionSelected(filterName)
                }, onDeleteSearch: { filter in
                    onFilterDelete(filter)
                })
                
            }
            
            Spacer()
            AppButton(action: {onApplyFilter()}, title: "apply_filters", isEnable: true)
            
        }.padding(.horizontal, 18)
            .onAppear(){
                if (filterDealerSelected.filterId != ""){
                    buttonLabel = filterDealerSelected.filterName
                }
            }
            .onChange(of: filterDealerSelected.category) { categorySelected in
                if (filterDealerSelected.filterId != ""){
                    buttonLabel = filterDealerSelected.filterName
                    categorySelectedId = filterTypeToRadioButton(filter: filterDealerSelected)
                }else{
                    switch(categorySelected){
                    case FilterType.service:
                        buttonLabel = "services"
                        categorySelectedId = "Repair, upgrade or buy motorhome"
                    case FilterType.brand:
                        buttonLabel = "brands"
                        categorySelectedId = "Find official motorhome dealers"
                    default:
                        buttonLabel = ""
                        categorySelectedId = ""
                    }
                }
            }
        Spacer()
    }
}
