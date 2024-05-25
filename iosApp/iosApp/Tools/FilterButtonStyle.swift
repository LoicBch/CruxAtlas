//
//  FilterButton.swift
//  iosApp
//
//  Created by USER on 19/05/2023.
//  Copyright © 2023 orgName. All rights reserved.
//

import Foundation
import SwiftUI

struct FilterButtonStyle: ButtonStyle {
    
    let filterActive: Bool
    
    func makeBody(configuration: Configuration) -> some View {
        
        if (filterActive) {
            configuration.label
                .padding(16)
                .background(Color.white)
                .foregroundColor(.white)
                .cornerRadius(50)
                .overlay(
                        RoundedRectangle(cornerRadius: 50)
                            .stroke(Color("Primary"), lineWidth: 2)
                )
                .overlay(
                    Image("active_filter").offset(x: 20,y: 20)
                )
                .shadow(color: Color.black.opacity(0.2), radius: 2, x: 0, y: 2)
        }else{
            configuration.label
                .padding(16)
                .background(Color.white)
                .foregroundColor(.white)
                .cornerRadius(50)
                .shadow(color: Color.black.opacity(0.2), radius: 2, x: 0, y: 2)
        }
    }
}

struct AppButtonStyle: ButtonStyle {
    func makeBody(configuration: Configuration) -> some View {
         
            configuration.label
                .padding(16)
                .background(Color.white)
                .foregroundColor(.white)
                .cornerRadius(50)
                .shadow(color: Color.black.opacity(0.2), radius: 2, x: 0, y: 2)
    }
}
