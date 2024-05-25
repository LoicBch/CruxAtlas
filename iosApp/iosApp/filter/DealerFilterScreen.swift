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
    @State var categorySelectedId = ""
    @StateObject var viewModel: DealerFilterViewModel = DealerFilterViewModel()
    var onClose: () -> Void
    var onFilterApplied: (Filter) -> Void
    
    public var body: some View {
            if (isSelectingOption){
                ScrollView(.vertical, showsIndicators: false){
                    filtersOptions(
                        categorySelected: viewModel.dealerFilterSelected.category,
                        onItemTap: {
                            viewModel.onFilterOptionSelected(filterName: $0)
                            isSelectingOption = false
                        },
                        onFilterOptionSelected: {viewModel.onFilterOptionSelected(filterName: $0)},
                        dealerFilterUsed: viewModel.dealerFiltersUsed,
                        onSearchDelete: {viewModel.deleteFilter(filter: $0)}, onClose: { isSelectingOption = false }
                    )
                }
            }else{
                filtersCategories(
                    buttonLabel: $viewModel.unselectedButtonLabel, categorySelectedId: $viewModel.categorySelectedId, onChooseOptionsTap: { isSelectingOption = true },
                    onCategorySelected: { category in
                        viewModel.onFilterCategorySelected(category: category)
                    },
                    dealerFiltersUsed: $viewModel.dealerFiltersUsed,
                    onDealerOptionSelected: { viewModel.onFilterOptionSelected(filterName: $0) },
                    onFilterDelete: { viewModel.deleteFilter(filter: $0) },
                    onApplyFilter: {
                        onClose()
                        viewModel.applyFilter()
                    },
                    isApplyEnable: true,
                    filterDealerSelected: $viewModel.dealerFilterSelected, onCloseSheet: {
                        onClose()
                    }
                ).onReceive(viewModel.$filterApplied){
                    if ($0.filterId != "-1"){
                        onFilterApplied($0)
                    }
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
    var onClose: () -> Void
    
    @State var input = ""
    
    public var body: some View{
        VStack{
            HStack{
                Image(systemName: "xmark").onTapGesture {
                    onClose()
                }
                Spacer()
                Text(getTitleLabel(for:categorySelected))
                Spacer()
                Image(systemName: "xmark").opacity(0)
            }.padding(.top, 15)
            
            AppTextField(onTextChange: { input in
                self.input = input
            }, onBackPressed: {
                onClose()
            }).padding(.top, 22)
            
            
            if (input.isEmpty){
                HStack{
                    Text("last_searched")
                        .font(.custom("CircularStd-Medium", size: 14))
                        .fontWeight(.medium)
                        .foregroundColor(Color.black)
                        .padding(.top, 20)
                    Spacer()
                }
                Divider()

                HistoricSearchList(filterSearchs: dealerFilterUsed, onSelectSearch: { filterName in
                    onFilterOptionSelected(filterName)
                }, onDeleteSearch: { filter in
                    onSearchDelete(filter)
                })
            }
            
            HStack{
                Text(getHistoricLabel(for:categorySelected))
                    .fontWeight(.medium)
                    .font(.custom("CircularStd-Medium", size: 16))
                    .foregroundColor(Color("Primary30"))
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
            return "Services"
        case .brand:
            return "Motorhome brands"
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
                        Image(systemName: "magnifyingglass").renderingMode(.template).foregroundColor(Color("Primary70"))
                        Text(result).font(.custom("CircularStd-Medium", size: 16)).fontWeight(.medium).foregroundColor(Color("Primary30"))
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
                    .font(.custom("CircularStd-Medium", size: 16))
                    .foregroundColor(Color("Tertiary30"))
                    .padding(.leading, 10)
                Spacer()
                Image("interchange")
                    .padding(.trailing, 10)
            }
            .padding(.vertical, 15)
            .overlay(
                RoundedRectangle(cornerRadius: 5)
                    .strokeBorder(Color("Tertiary"), lineWidth: 2)
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
    
    @Binding var buttonLabel: String
    @Binding var categorySelectedId: String
    var onChooseOptionsTap: ()->Void
    var onCategorySelected: (FilterType)->Void
    @Binding var dealerFiltersUsed: [Filter]
    var onDealerOptionSelected: (String)->Void
    var onFilterDelete: (Filter)->Void
    var onApplyFilter: ()->Void
    var isApplyEnable: Bool
    @Binding var filterDealerSelected: Filter
    var onCloseSheet: ()->Void
     
    
    public var body: some View{
        VStack{
            ScrollView(.vertical, showsIndicators: false){
                HStack{
                    Image(systemName: "xmark").onTapGesture {
                        onCloseSheet()
                    }
                    Spacer()
                    Text("filters_title")
                    Spacer()
                    Image("reload").onTapGesture {
                        onCategorySelected(FilterType.unselectedDealer)
                    }
                }.padding(.top, 12)
                
                HStack{
                    Text("STEP 1")
                        .fontWeight(.medium)
                        .font(.custom("CircularStd-Medium", size: 12))
                        .foregroundColor(Color("Variant"))
                        .padding(.top, 60)
                    Spacer()
                }
                
                HStack{
                    Text("filter_step1_title")
                        .fontWeight(.bold)
                        .font(.custom("CircularStd-Medium", size: 22))
                        .foregroundColor(Color.black)
                    Spacer()
                }.padding(.top, 8)
                
                Divider()
                
                RadioButtonGroup(items:[("Repair, upgrade or buy motorhome","repair"),("Find official motorhome dealers","dealers")], selectedId:  $categorySelectedId) { selected in
                    onCategorySelected(radiobuttonToFilterType(radiobuttonId: selected))
                }
                
                if(filterDealerSelected.category != FilterType.unselectedDealer){
                    
                    HStack{
                        Text("STEP 2")
                            .fontWeight(.medium)
                            .font(.custom("CircularStd-Medium", size: 12))
                            .foregroundColor(Color("Variant"))
                            .padding(.top, 60)
                        Spacer()
                    }
                    
                    HStack{
                        Text("filter_step2_title")
                            .fontWeight(.bold)
                            .font(.custom("CircularStd-Medium", size: 22))
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
                    
                    HStack{
                        Text("last_searched")
                            .font(.custom("CircularStd-Medium", size: 14))
                            .fontWeight(.medium)
                            .foregroundColor( Color("Variant"))
                            .padding(.top, 20)
                        Spacer()
                    }
                    
                    HistoricSearchList(filterSearchs: dealerFiltersUsed, onSelectSearch: { filterName in
                        onDealerOptionSelected(filterName)
                    }, onDeleteSearch: { filter in
                        onFilterDelete(filter)
                    })
                    
                }
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
                    print("FILTER: A")
                    buttonLabel = filterDealerSelected.filterName
                    categorySelectedId = filterTypeToRadioButton(filter: filterDealerSelected)
                }else{
                    print("FILTER: B")
                    switch(categorySelected){
                    case FilterType.service:
                        buttonLabel = "Services"
                        categorySelectedId = "Repair, upgrade or buy motorhome"
                    case FilterType.brand:
                        buttonLabel = "Brands"
                        categorySelectedId = "Find official motorhome dealers"
                    default:
                        buttonLabel = ""
                        categorySelectedId = ""
                    }
                }
            }.onChange(of: filterDealerSelected.filterName){ filterName in
                if (filterName != ""){
                    buttonLabel = filterName
                }
            }
        Spacer()
    }
}
