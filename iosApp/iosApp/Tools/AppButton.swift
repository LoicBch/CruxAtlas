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
                    .fontWeight(.bold)
                    .font(.custom("CircularStd-Medium", size: 16))
                    .foregroundColor(Color.white)
                    .padding(.vertical, 16)
                Spacer()
            }
            .background(Color("Secondary"))
            .cornerRadius(25)
            .frame(width: .infinity, height: 50)
            .padding(.horizontal, 16)
            .buttonStyle(.plain).onTapGesture {
                action()
            }
        }else{
            Button(action: {}){
                Spacer()
                Text(LocalizedStringKey(title))
                    .fontWeight(.bold)
                    .font(.custom("CircularStd-Medium", size: 16))
                    .foregroundColor(Color.white)
                    .padding(.vertical, 16)
                Spacer()
            }
            .frame(height: 50)
            .background(Color("NeutralText"))
            .cornerRadius(25)
            .padding(.horizontal, 16)
            .buttonStyle(.plain)
        }
    }
}

struct AppIconButton: View {
    
    var action: () -> Void
    var title: String
    var icon: String
    var isEnable: Bool
    
    public var body: some View{
        
        if(isEnable){
            Button(action: {
                action()
            }){
                Spacer()
                Image(icon).renderingMode(.template).foregroundColor(Color.white)
                Text(LocalizedStringKey(title))
                    .fontWeight(.bold)
                    .font(.custom("CircularStd-Medium", size: 16))
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
                Image(icon).renderingMode(.template).foregroundColor(Color.white)
                Text(LocalizedStringKey(title))
                    .fontWeight(.bold)
                    .font(.custom("CircularStd-Medium", size: 16))
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
