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
    var onBackPressed: () -> Void
    
    public var body: some View {
        
        TextField("Tap to start typing", text: $text.onChange({text in onTextChange(text)}))
            .textFieldStyle(AppTextFieldStyle(onFocused: { isFocused = true }))
            .overlay(
            HStack {
                if (isFocused){
                    Image(systemName: "arrow.left").foregroundColor(.black).padding(.leading, 12).onTapGesture {
    //                    self.text = ""
    //                    onTextChange("")
    //                    UIApplication.shared.sendAction(#selector(UIResponder.resignFirstResponder), to:nil, from:nil, for:nil)
                        onBackPressed()
                    }
                }else {
                    Image("magnifying_glass").foregroundColor(.black).padding(.leading, 12).onTapGesture {
    //                    self.text = ""
    //                    onTextChange("")
    //                    UIApplication.shared.sendAction(#selector(UIResponder.resignFirstResponder), to:nil, from:nil, for:nil)
                        onBackPressed()
                    }
                }
                Spacer()
                Image(systemName: "x.circle")
                    .padding(.trailing, 12)
                              .foregroundColor(Color("Secondary"))
                              .opacity(text.isEmpty ? 0.0 : 1.0)
                              .onTapGesture {
                                  self.text = ""
                                  onTextChange("")
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
                .frame(width: .infinity, height: 48)).onAppear{
                    text = ""
                }
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
            .frame(maxWidth: .infinity, alignment: .leading)
            .font(.custom("CircularStd-Medium", size: 14))
            .onTapGesture {
                onFocused()
            }
    }
}
