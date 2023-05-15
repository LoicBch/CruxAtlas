//
//  EventFilterScreen.swift
//  iosApp
//
//  Created by USER on 01/05/2023.
//  Copyright © 2023 orgName. All rights reserved.
//

import Foundation
import SwiftUI
import shared

struct EventFilterScreen: View{
    
    @StateObject var viewModel: EventFilterViewModel = EventFilterViewModel()
    @State var isSelectingCountry = false
    var onClose: () -> Void
    
    public var body: some View{
        
        
        if(isSelectingCountry){
            FilterChosenScreen(
                onBoxTap: { isSelectingCountry = true },
                countrySelected: viewModel.eventFilterSelected,
                countriesHistoric: viewModel.eventFiltersUsed,
                onApply: {
                    onClose()
                    viewModel.applyFilter()
                    
                }
            )
        }else{
//            FilterSelectionScreen()
        }
    }
}

struct FilterChosenScreen: View {
    
    var onBoxTap: () -> Void
    var countrySelected: Filter
    var countriesHistoric: [Filter]
    var onApply: () -> Void
    
    public var body: some View{
        VStack{
            HStack{
                Image(systemName: "xmark").onTapGesture {
                    
                }
                Spacer()
                LocalizedText(key: "filters_title")
                Spacer()
            }.padding(.top, 12)
            
            HStack{
                LocalizedText(key: "where_do_you_want_to_go")
//                    .fontWeight(.black)
                    .font(.system(size: 22))
                    .foregroundColor(Color.black)
                Spacer()
            }.padding(.top, 60)
            Divider()
            
//            if (countrySelected.filterId == ""){
//                FilterSelectionBox(buttonLabel: countrySelected.filterName, onTap: {
//                    onBoxTap()
//                })
//            }else{
//                MaterialTextField(buttonLabel: countrySelected.filterName, onTap: {
//                    onBoxTap()
//                })
//            }
//
//            ResultList(results: countriesHistoric, onItemClick: {
//                onApply()
//            })
            
            AppButton(action: { onApply() }, title: "apply_filters", isEnable: true)
        }
    }
}

struct FilterSelectionScreen: View{
    
    var onSelectionConfirm: ()->Void
    var checkBoxStates: [Bool]
    
    public var body: some View{
        VStack{
            HStack{
                Image(systemName: "xmark").onTapGesture {
                    
                }
                Spacer()
                LocalizedText(key: "choose_country")
                Spacer()
            }.padding(.top, 12)
            
            AppTextField(onTextChange: { input in
                
            }).padding(.top, 22)
            
            HStack{
                LocalizedText(key: "last_searched")
//                    .fontWeight(.black)
                    .font(.system(size: 14))
                    .foregroundColor(Color.black)
                Spacer()
            }.padding(.top, 35)
            
            ResultList(results: [], onItemClick: {_ in })
            
            HStack{
                LocalizedText(key: "all_countries")
//                    .fontWeight(.black)
                    .font(.system(size: 14))
                    .foregroundColor(Color.black)
                Spacer()
            }.padding(.top, 35)
            
//            CheckboxsGroup()
            
            AppButton(action: { onSelectionConfirm() }, title: "apply_filters", isEnable: true)
            
        }
    }
}


struct CheckboxsGroup: View{
    
    @Binding var checkboxStates: [Bool]
    
    public var body: some View{
        HStack{
            Image("repair")
            LocalizedText(key: "filter_step1_option1")
//                .fontWeight(.medium)
                .font(.system(size: 14))
                .foregroundColor(Color("Tertiary"))
                .padding(.leading, 10)
            Spacer()
            Toggle("", isOn: $checkboxStates[0])
                .toggleStyle(CheckboxToggleStyle())
            
        }
        
        HStack{
            Image("dealers")
            LocalizedText(key: "filter_step1_option2")
//                .fontWeight(.medium)
                .font(.system(size: 14))
                .foregroundColor(Color("Tertiary"))
                .padding(.leading, 10)
            Spacer()
            Toggle("", isOn: $checkboxStates[1])
                .toggleStyle(CheckboxToggleStyle())
        }
    }
}
