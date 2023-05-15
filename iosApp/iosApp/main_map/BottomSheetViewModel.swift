//
//  HomeViewModel.swift
//  iosApp
//
//  Created by USER on 20/01/2023.
//  Copyright © 2023 orgName. All rights reserved.
//

import Foundation
import shared
import CoreLocation
import MapKit

@MainActor class BottomSheetViewModel : ObservableObject {
    
    init(){
        
    }
    
    @Published var bottomSheetIsOpen = false
    @Published var bottomOption = BottomSheetOption.FILTER
    
    func openSheet(option: BottomSheetOption){
        setOption(option: option)
        bottomSheetIsOpen = true
        print(bottomSheetIsOpen)
    }
    
    func closeSheet(){
        bottomSheetIsOpen = false
    }
    
    private func setOption(option: BottomSheetOption){
        bottomOption = option
    }
    
}

public enum BottomSheetOption {
    case FILTER, FILTER_EVENT, SORT, SORT_AROUND_PLACE, SORT_EVENT, MAP_LAYER
}

