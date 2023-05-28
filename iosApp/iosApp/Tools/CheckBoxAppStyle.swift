//
//  CheckBoxAppStyle.swift
//  iosApp
//
//  Created by USER on 19/05/2023.
//  Copyright © 2023 orgName. All rights reserved.
//

import Foundation
import SwiftUI

struct CheckBoxAppStyle: ToggleStyle {
    func makeBody(configuration: Configuration) -> some View {
        HStack {
            Image(systemName: configuration.isOn ? "checkmark.square.fill" : "square")
                .foregroundColor(configuration.isOn ? Color("Primary") : .gray)
        } 
        .padding()
        .onTapGesture {
            configuration.isOn.toggle()
        }
    }
}
