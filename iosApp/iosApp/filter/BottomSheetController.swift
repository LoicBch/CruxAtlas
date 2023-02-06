//
//  BottomSheetController.swift
//  iosApp
//
//  Created by USER on 26/01/2023.
//  Copyright © 2023 orgName. All rights reserved.
//

import Foundation
import SwiftUI

struct BottomSheetController: View {
    
    var option: BottomSheetOption
    
    public var body: some View{
        switch option {
        case .FILTER: FilterScreen()
        case .FILTER_EVENT: FilterScreen()
        case .SORT: FilterScreen()
        case .SORT_AROUND_PLACE: FilterScreen()
        case .SORT_EVENT: FilterScreen()
        case .MAP_LAYER: FilterScreen()
        }
    }
}
