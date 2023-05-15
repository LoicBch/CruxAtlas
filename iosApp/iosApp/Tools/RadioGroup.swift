//
//  RadioGroup.swift
//  iosApp
//
//  Created by USER on 28/04/2023.
//  Copyright © 2023 orgName. All rights reserved.
//

import Foundation
import SwiftUI

struct ColorInvert: ViewModifier {

    @Environment(\.colorScheme) var colorScheme

    func body(content: Content) -> some View {
        Group {
            if colorScheme == .dark {
                content.colorInvert()
            } else {
                content
            }
        }
    }
}

struct RadioButton: View {

    @Environment(\.colorScheme) var colorScheme

    let id: String
    let image: String
    let callback: (String)->()
    let selectedID : String
    let size: CGFloat
    let color: Color
    let textSize: CGFloat

    init(
        _ id: String,
        image: String,
        callback: @escaping (String)->(),
        selectedID: String,
        size: CGFloat = 20,
        color: Color = Color("Primary"),
        textSize: CGFloat = 14
        ) {
        self.id = id
        self.image = image
        self.size = size
        self.color = color
        self.textSize = textSize
        self.selectedID = selectedID
        self.callback = callback
    }

    var body: some View {
        Button(action:{
            self.callback(self.id)
        }) {
            HStack(alignment: .center, spacing: 10) {
                Image(image)
                LocalizedText(key: id)
                    .font(Font.system(size: textSize))
                    .padding(.leading, 8)
                Spacer()
                Image(systemName: self.selectedID == self.id ? "largecircle.fill.circle" : "circle")
                    .renderingMode(.original)
                    .resizable()
                    .aspectRatio(contentMode: .fit)
                    .frame(width: self.size, height: self.size)
                    .modifier(ColorInvert())
            }.foregroundColor(self.color)
        }
        .foregroundColor(self.color)
    }
}

struct RadioButtonGroup: View {

    let items : [(String, String)]

    @Binding var selectedId: String

    let callback: (String) -> ()

    var body: some View {
        VStack {
            ForEach(0..<items.count) { index in
                RadioButton(self.items[index].0, image: self.items[index].1, callback: self.radioGroupCallback, selectedID: self.selectedId)
            }
        }
    }

    func radioGroupCallback(id: String) {
        selectedId = id
        callback(id)
    }
}

struct SortingRadioGroup: View {

    let items : [SortingOption]

    @Binding var selectedId: String

    let callback: (String) -> ()

    var body: some View {
        VStack {
            ForEach(0..<items.count) { index in
                RadioButton(items[index].getLabel(), image: items[index].getIcon(), callback: self.radioGroupCallback, selectedID: self.selectedId)
            }
        }
    }

    func radioGroupCallback(id: String) {
        selectedId = id
        callback(id)
    }
}
