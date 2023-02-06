//
//  AroundLocationScreen.swift
//  iosApp
//
//  Created by USER on 19/01/2023.
//  Copyright © 2023 orgName. All rights reserved.
//

import Foundation
import shared
import SwiftUI


struct AroundLocationScreen: View {
    public var body: some View {
        VStack(){
            HStack(){
                Image("")
                Text("Around a place")
                    .font(.system(size: 22, weight: .bold))
            }
            Text("Search for location: address, city, region, country.").font(.system(size: 12, weight: .medium))
            AppTextField()
        }
    }
}

struct AppTextField: View {
    
    @State var text: String = ""
    
    public var body: some View{
        
        HStack {
            Image(systemName: "magnifyingglass")
            TextField("Search...", text: $text)
        }
        .textFieldStyle(AppTextFieldStyle())
    }
}

struct AppTextFieldStyle: TextFieldStyle {
    func _body(configuration: TextField<Self._Label>) -> some View {
        configuration
            .padding(10)
            .background(LinearGradient(gradient: Gradient(colors: [Color.orange, Color.orange]), startPoint: .topLeading, endPoint: .bottomTrailing))
            .cornerRadius(20)
            .shadow(color: .gray, radius: 10)
    }
}
