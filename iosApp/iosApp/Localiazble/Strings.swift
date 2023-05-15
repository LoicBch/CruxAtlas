//
//  Strings.swift
//  iosApp
//
//  Created by USER on 14/04/2023.
//  Copyright © 2023 orgName. All rights reserved.
//

import Foundation

enum StringKeeper: String {
    case labelTitle
    var localized : String {
        NSLocalizedString(String(describing: Self.self) + "_\(rawValue)", comment: "")
    }
}
