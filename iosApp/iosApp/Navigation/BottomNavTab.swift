//
//  BottomNavTab.swift
//  iosApp
//
//  Created by USER on 24/05/2023.
//  Copyright © 2023 orgName. All rights reserved.
//

import Foundation

enum BottomNavTab: Identifiable {
    case mainMap
    case aroundLocation
    case partners
    case more
    
    var id: Int {
        switch self {
        case .mainMap:
            return 0
        case .aroundLocation:
            return 1
        case .partners:
            return 2
        case .more:
            return 3
        }
    }
    
    var label: String {
        switch self {
        case .mainMap:
            return "appbar_around_me"
        case .aroundLocation:
            return "appbar_around_location"
        case .partners:
            return "appbar_partners"
        case .more:
            return "appbar_menu"
        }
    }
    
    func icon(isSelected: Bool) -> String {
        switch self {
        case .mainMap:
            return isSelected ? "around_me_selected" : "around_me"
        case .aroundLocation:
            return isSelected ? "around_location_selected" : "around_location"
        case .partners:
            return isSelected ? "partners_selected" : "partners"
        case .more:
            return isSelected ? "menu_selected" : "menu"
        }
    }
} 
