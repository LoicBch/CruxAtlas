//
//  BottomNavContainer.swift
//  iosApp
//
//  Created by USER on 24/05/2023.
//  Copyright © 2023 orgName. All rights reserved.
//

import SwiftUI

struct BottomNavContainer: View {
    @EnvironmentObject var navState: NavigationViewState
    @EnvironmentObject var appState: AppState
    
    var body: some View {
        CustomTabView(
                props: .init(
                        selectedNavBarTab: navState.bottomNavSelectedTab,
                        items: buildNavigationItems(),
                        onTap: { item in
                            if (item == .mainMap){
                                appState.aroundMeClicked = true
                            }
                            navState.bottomNavSelectedTab = item
                        },
                        onLongTap: { item in
                            navState.bottomNavSelectedTab = item
                        }
                )
        )
    }

    private func buildNavigationItems() -> [BottomNavTabItem] {
        [
            BottomNavTabItem(type: .mainMap),
            BottomNavTabItem(type: .aroundLocation),
            BottomNavTabItem(type: .partners),
            BottomNavTabItem(type: .more)
        ]
    }
}
