//
//  File.swift
//  iosApp
//
//  Created by USER on 16/04/2023.
//  Copyright © 2023 orgName. All rights reserved.
//

import Foundation
import SwiftUI

final class MapSettings: ObservableObject {
    @Published var mapType = 0
    @Published var showElevation = 0
    @Published var showEmphasisStyle = 0
}
