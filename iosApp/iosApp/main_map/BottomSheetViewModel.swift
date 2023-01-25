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
    
    func openSheet(){
        bottomSheetIsOpen = true
    }
    
    func closeSheet(){
        bottomSheetIsOpen = false
    }
    
}

