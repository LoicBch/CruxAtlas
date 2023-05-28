//
//  AppButton.swift
//  iosApp
//
//  Created by USER on 24/04/2023.
//  Copyright © 2023 orgName. All rights reserved.
// 
import Foundation
import SwiftUI

struct AppButton: View {
    
    var action: () -> Void
    var title: String
    var isEnable: Bool
    
    public var body: some View{
        
        if(isEnable){
            Button(action: {
                action()
            }){
                Spacer()
                Text(LocalizedStringKey(title))
//                    .fontWeight(.bold)
                    .font(.system(size: 16))
                    .foregroundColor(Color.white)
                    .padding(.vertical, 16)
                Spacer()
            }
            .frame(height: 50)
            .background(Color("Secondary"))
            .cornerRadius(25)
            .padding(.horizontal, 16)
            .buttonStyle(.plain)
        }else{
            Button(action: {}){
                Spacer()
                Text(LocalizedStringKey(title))
//                    .fontWeight(.bold)
                    .font(.system(size: 16))
                    .foregroundColor(Color.white)
                    .padding(.vertical, 16)
                Spacer()
            }
            .frame(height: 50)
            .background(Color("Grey"))
            .cornerRadius(25)
            .padding(.horizontal, 16)
            .buttonStyle(.plain)
        }
    }
}
