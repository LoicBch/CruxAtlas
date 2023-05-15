//
//  SizePreferenceKey.swift
//  iosApp
//
//  Created by USER on 25/04/2023.
//  Copyright © 2023 orgName. All rights reserved.
//

import Foundation
import SwiftUI

struct SizePreferenceKey: PreferenceKey {
  static var defaultValue: CGSize = .zero
  static func reduce(value: inout CGSize, nextValue: () -> CGSize) {}
}
