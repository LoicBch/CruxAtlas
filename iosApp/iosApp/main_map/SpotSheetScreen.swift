//
//  SpotSheetScreen.swift
//  iosApp
//
//  Created by USER on 19/11/2022.
//  Copyright © 2022 orgName. All rights reserved.
//

import Foundation
import shared
import SwiftUI

struct SpotSheetScreen: View {

    var spot : Spot
    
    var body: some View {
        VStack {
            Text(spot.name)
        }
    }
}
