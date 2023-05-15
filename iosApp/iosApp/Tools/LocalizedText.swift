//
//  LocalizedText.swift
//  iosApp
//
//  Created by USER on 15/05/2023.
//  Copyright © 2023 orgName. All rights reserved.
//

import SwiftUI

struct LocalizedText: View {
    let key: String
    let comment: String = "default"

    var body: some View {
        Text(NSLocalizedString(key, comment: comment))
    }
}
