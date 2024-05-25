//
//  MaterialTextField.swift
//  iosApp
//
//  Created by USER on 01/05/2023.
//  Copyright © 2023 orgName. All rights reserved.
//

import Foundation
import SwiftUI

struct MaterialTextField: View{
    
    @Binding var buttonLabel: String
    var onTap: () -> Void
    
    public var body: some View{
         
            HStack{
                Text(buttonLabel)
                    .fontWeight(.medium)
                    .font(.custom("CircularStd-Medium", size: 16))
                    .padding(.leading, 10)
                    .foregroundColor(Color("Primary"))
                Spacer()
                Image(systemName: "arrow.up.and.down")
                    .padding(.trailing, 10)
                    .foregroundColor(Color("Primary"))
            }
            .padding(.vertical, 15)
            .overlay(
                RoundedRectangle(cornerRadius: 5)
                    .stroke(Color("Primary"), lineWidth: 2)
            ).onTapGesture {
                onTap()
            }
    }
}
