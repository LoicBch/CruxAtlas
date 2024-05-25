//
//  GlobalPopup.swift
//  iosApp
//
//  Created by USER on 09/05/2023.
//  Copyright © 2023 orgName. All rights reserved.
//

import Foundation
import shared
import SwiftUI

struct GlobalPopup: View{
    
    @Binding var state : GlobalPopupState
    var onExit: () -> Void
    
    var body: some View{
        VStack {
            Spacer()
            VStack{
                HStack{
                    Text(LocalizedStringKey(getTitle(popupState: state)))
                        .fontWeight(.bold)
                        .font(.custom("CircularStd-Medium", size: 22))
                        .foregroundColor(Color.black)
                        .padding(.top, 24)
                        .frame(alignment: .leading)
                    Spacer()
                    Image(systemName: "trash").onTapGesture {
                        onExit()
                    }
                }
                Text(LocalizedStringKey(getContent(popupState:state)))
                    .font(.custom("CircularStd-Medium", size: 16))
                    .fontWeight(.medium)
                    .foregroundColor(Color("Tertiary30"))
                    .padding(.top, 24)
                    .frame(alignment: .leading)
            }.padding(12)
            .background(RoundedRectangle(cornerRadius: 5)
                .fill(Color.white).shadow(radius: 2, x:-1, y: 2))
            .padding(16)
            Spacer()
        }.background(Color("ClearGrey"))
    }
}


func getTitle(popupState: GlobalPopupState) -> String{
    switch(popupState){
    case .GpsMissing: return "popup_missing_gps_title"
    case .NetworkMissing: return "popup_missing_network_title"
    case .Hid: return ""
    }
}


func getContent(popupState: GlobalPopupState) -> String{
    switch(popupState){
    case .GpsMissing: return "popup_missing_gps_text"
    case .NetworkMissing: return "popup_missing_network_text"
    case .Hid: return ""
    }
}

enum GlobalPopupState{
    case Hid
    case GpsMissing
    case NetworkMissing
}
