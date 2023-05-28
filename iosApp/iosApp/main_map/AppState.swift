//
//  AppState.swift
//  iosApp
//
//  Created by USER on 24/05/2023.
//  Copyright © 2023 orgName. All rights reserved.
//

import SwiftUI
import shared

class AppState: ObservableObject {
    @Published  var aroundMeClicked: Bool = false
    @Published  var placeSelected: Place = Place(name: "", location: Location(latitude: 0.0, longitude: 0.0))
    @Published  var isEventsMapOpening = false
    @Published  var isAroundMeClicked = false
    @Published  var filterApplied = Filter(category: FilterType.unselectedDealer, filterId: "", isSelected: false)
    @Published  var verticalSortingApplied = SortingOption.none
    @Published  var lastlySelectedTab = BottomNavTab.mainMap
}
