//
//  AppTextField.swift
//  iosApp
//
//  Created by USER on 28/04/2023.
//  Copyright © 2023 orgName. All rights reserved.
//

import Foundation
import SwiftUI

struct AppTextField: View {
    @State var text = ""
    @State var isFocused = false
    var onTextChange: (String) -> Void
    
    public var body: some View {
        
        TextField("Search...", text: $text.onChange({text in onTextChange(text)}))
            .textFieldStyle(AppTextFieldStyle(onFocused: { isFocused = true }))
        .overlay(
            HStack {
                Image(systemName: isFocused ? "chevron.left" : "magnifyingglass").foregroundColor(.black).padding(.leading, 12)
                Spacer()
                Image(systemName: "xmark.circle.fill")
                    .padding(.trailing, 12)
                              .foregroundColor(.gray)
                              .opacity(text.isEmpty ? 0.0 : 1.0)
                              .onTapGesture {
                                  self.text = ""
                              }
            }.background(
                RoundedRectangle(cornerRadius: 4)
                   .stroke(isFocused ? Color("Primary") : Color.white, lineWidth: 2)
                   .frame(width: .infinity, height: 48)
            )
        )
        .background(
            RoundedRectangle(cornerRadius: 4)
                .fill(isFocused ? Color.white : Color("unFocusedTextField"))
               .frame(width: .infinity, height: 48))
    }
}

struct AppTextFieldStyle: TextFieldStyle {
    
    @State private var isFocused = false
    var onFocused: () -> Void
  
    func _body(configuration: TextField<Self._Label>) -> some View {
        configuration
            .padding(.horizontal, 40)
            .frame(height: 48)
            .cornerRadius(4)
            .shadow(color: .gray, radius: 1)
            .frame(maxWidth: .infinity, alignment: .leading)
            .onTapGesture {
                onFocused()
            }
    }
}
