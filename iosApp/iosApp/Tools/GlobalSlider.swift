//
//  GlobalSlider.swift
//  iosApp
//
//  Created by USER on 09/05/2023.
//  Copyright © 2023 orgName. All rights reserved.
//

import Foundation
import shared
import SwiftUI

struct GlobalSlider: View {

    @Binding var state: [SliderState]
    var onRemoveGpsNotification: () -> Void
    var onRemoveNetworkNotification: () -> Void

    var body: some View {
        VStack{
            if (state.count > 0){
                withAnimation {
                    VStack{
                        HStack{
                            LocalizedText(key: getTitle(popupState: state[0]))
                            Spacer()
                            Image(systemName: "trash").onTapGesture {
                                switch(state[0]){
                                case .GpsMissing: return onRemoveGpsNotification()
                                case .NetworkMissing: return onRemoveNetworkNotification()
                                case .Hid: break;
                                }
                            }
                        }
                        HStack{
                            LocalizedText(key: getContent(popupState: state[0]))
                            Spacer()
                        }
                    }.padding(12)
                        .background(Color("PrimaryContainer"))
                        .cornerRadius(10)
                        .shadow(radius: 5, x:-1, y: 1)
                }
            }

            if (state.count > 1){
                VStack{
                    HStack{
                        LocalizedText(key: getTitle(popupState: state[1]))
                        Spacer()
                        Image(systemName: "x.cross").onTapGesture {
                            switch(state[1]){
                            case .GpsMissing: return onRemoveGpsNotification()
                            case .NetworkMissing: return onRemoveNetworkNotification()
                            case .Hid: break;
                            }
                        }
                    }
                    HStack{
                        LocalizedText(key: getContent(popupState: state[1]))
                        Spacer()
                    }
                }.padding(12)
                    .background(Color("PrimaryContainer"))
                    .cornerRadius(10)
                    .shadow(radius: 5, x:-1, y: 1)
            }
            Spacer()
        }
    }


    func getTitle(popupState: SliderState) -> String{
        switch(popupState){
        case .GpsMissing: return "slider_missing_gps_title"
        case .NetworkMissing: return "slider_missing_network_title"
        case .Hid: return ""
        }
    }


    func getContent(popupState: SliderState) -> String{
        switch(popupState){
        case .GpsMissing: return "slider_missing_gps_text"
        case .NetworkMissing: return "slider_missing_network_text"
        case .Hid: return ""
        }
    }

}
enum SliderState{
    case Hid
    case GpsMissing
    case NetworkMissing
}
