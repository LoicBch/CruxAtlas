//
//  HelpScreen.swift
//  iosApp
//
//  Created by USER on 31/08/2023.
//  Copyright © 2023 orgName. All rights reserved.
//

import Foundation
import SwiftUI
import shared

struct HelpScreen: View {
    
    @Environment(\.presentationMode) var presentationMode
 
    public var body: some View{
        VStack(alignment: .leading){
            
            Button(action: {
                self.presentationMode.wrappedValue.dismiss()
            }){
                Image("arrow_left")
            }
            
            
            Text(LocalizedStringKey("need_help"))
                .fontWeight(.bold)
                .font(.custom("CircularStd-Medium", size: 22))
                .padding(.top, 41)
            
            Text(LocalizedStringKey("help_subtitle"))
                .fontWeight(.medium)
                .font(.custom("CircularStd-Medium", size: 16))
                .padding(.top, 12)
            
            HStack{
                Image("world")
                Text(LocalizedStringKey("faq_link"))
                    .fontWeight(.medium)
                    .font(.custom("CircularStd-Medium", size: 16))
                    .padding(.leading, 12)
            }.padding(.top, 12).onTapGesture {
                guard let url = URL(string: Constants().HELP_URL) else { return }
                UIApplication.shared.open(url)
            }
            
            Text(LocalizedStringKey("bug_report"))
                .fontWeight(.bold)
                .font(.custom("CircularStd-Medium", size: 16))
                .padding(.top, 80)
            
            Text(LocalizedStringKey("bug_text"))
                .fontWeight(.medium)
                .font(.custom("CircularStd-Medium", size: 12))
                .padding(.top, 12)
            Spacer()
            AppButton(action: {}, title: "navigate", isEnable: true)
        }.padding(16)
    }
}

