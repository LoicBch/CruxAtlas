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
                if isExpanded {
                    Image("tools_selected").padding(.horizontal, 5)
                }else {
                    Image("tools").padding(.horizontal, 5)
                }
                
                Text(LocalizedStringKey(title)).fontWeight(.bold).padding(.leading, 20).foregroundColor(isExpanded ? Color("Primary") : Color("Tertiary"))
                Spacer()
                Image("arrow_dropdown")
                    .rotationEffect(isExpanded ? .degrees(180) : .degrees(0))
            }
            
            .frame(width: .infinity, height: 70)
            .padding(.horizontal, 15)
            
            if isExpanded {
                content
                    .padding(.leading, 40)
                    .animation(.easeInOut)
            }
        }.background(Color.white).onTapGesture {
            withAnimation(){
                isExpanded.toggle()
            }
        }
    }
}
