//
//  Dropdown.swift
//  iosApp
//
//  Created by USER on 20/04/2023.
//  Copyright © 2023 orgName. All rights reserved.
//

import Foundation
import SwiftUI

struct Dropdown<Content: View>: View{
    
    @State var isExpanded = false
    @ViewBuilder var content: Content
    var title: String
    
    public var body: some View{
        VStack {
            HStack{
                Image("tools")
                LocalizedText(key: title).padding(.leading, 20)
                Spacer()
                Image("arrow_dropdown")
                    .rotationEffect(isExpanded ? .degrees(180) : .degrees(0))
                    .animation(.easeInOut(duration: 0.5))
            }
            .onTapGesture {
                withAnimation(){
                    isExpanded.toggle()
                }
            }
            .frame(width: .infinity, height: 70)
            .padding(.horizontal, 15)
            
            
            if isExpanded {
                content
                    .padding(.leading, 40)
                    .animation(.linear)
            }
        }
    }
}
