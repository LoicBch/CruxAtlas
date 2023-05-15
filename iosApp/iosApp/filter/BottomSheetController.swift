//
//  BottomSheetController.swift
//  iosApp
//
//  Created by USER on 26/01/2023.
//  Copyright © 2023 orgName. All rights reserved.
//

import Foundation
import SwiftUI
import shared

struct BottomSheetController: View {
    
    var option: BottomSheetOption
    var onClose: () -> Void
    var onActiveFilterUpdate: (Filter) -> Void
    var onActiveSortingUpdate: (SortingOption) -> Void
    var updateSource: UpdateSource = UpdateSource.aroundMe
    
    public var body: some View{
        switch option {
        case .FILTER: DealerFilterScreen(onClose: { onClose() }, onFilterApplied: { onActiveFilterUpdate($0) })
        case .FILTER_EVENT: EventFilterScreen(onClose: { onClose() })
        case .SORT: SortingSheet(updateSource: updateSource, onSortingApplied: { onActiveSortingUpdate($0) }, onClose: { onClose() })
        case .SORT_AROUND_PLACE: Text("");
        case .SORT_EVENT: Text("");
        case .MAP_LAYER: Text("");
        }
    }
}
