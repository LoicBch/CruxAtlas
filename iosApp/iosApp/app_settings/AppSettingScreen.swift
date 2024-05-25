//
//  AppSettingScreen.swift
//  iosApp
//
//  Created by USER on 13/04/2023.
//  Copyright © 2023 orgName. All rights reserved.
//

import Foundation
import shared
import SwiftUI

struct AppSettingScreen: View {
    
    @State var languagePopupIsActive = false
    @State var metricPopupIsShowing = false
    
    @Environment(\.onLanguageUpdate) var onLanguageUpdate
    @Environment(\.presentationMode) var presentationMode
    
    var SettingItems = [
//        SettingItem(id: UUID(), label: "measuring_system"),
        SettingItem(id: UUID(), label: "language"),
        SettingItem(id: UUID(), label: "help"),
        SettingItem(id: UUID(), label: "term_of_use"),
        SettingItem(id: UUID(), label: "privacy_policy")
    ]
    
    public var body: some View {
        ZStack{
            VStack{
                HStack{
                    Image(systemName: "arrow.left").onTapGesture {
                        presentationMode.wrappedValue.dismiss()
                    }
                    Spacer()
                    Text("app_settings").font(.custom("CircularStd-Medium", size: 16)).fontWeight(.medium).foregroundColor(Color("Tertiary10"))
                    Spacer()
                    Image(systemName: "arrow.left").opacity(0)
                }.padding(.leading, 15).padding(.top, 12)
                Divider().padding(.horizontal, 15).padding(.top, 44)
                NavigationView(){
                    VStack{
                        ForEach(SettingItems, id: \.id) { settingItem in
                            VStack{
                                SettingItemRow(settingItem: settingItem)
                                Divider().padding(.horizontal, 15)
                            }.background(Color.white)
                            .onTapGesture {
                                switch(settingItem.label){
                                case "measuring_system":
                                    languagePopupIsActive = false
                                    metricPopupIsShowing = true
                                case "language":
                                    metricPopupIsShowing = false
                                    languagePopupIsActive = true
                                case "help":
                                    guard let url = URL(string: Constants().HELP_URL) else { return }
                                    UIApplication.shared.open(url)
                                case "term_of_use":
                                    guard let url = URL(string: Constants().A_PROPOS_URL) else { return }
                                    UIApplication.shared.open(url)
                                case "privacy_policy":
                                    guard let url = URL(string: Constants().PRIVACY_POLICY_URL) else { return }
                                    UIApplication.shared.open(url)
                                default:
                                    guard let url = URL(string: Constants().HELP_URL) else { return }
                                    UIApplication.shared.open(url)
                                }
                            }
                        }
                        Spacer()
                        BuildInfosRow().padding(.bottom, 15).padding(.leading, 15)
                    }
                }
            }
            if (languagePopupIsActive){
                LanguagePopup(onLanguageSelected: { languageSelected in
                    var key = "en"
                    switch (languageSelected){
                    case "french": key = "fr"
                    case "english": key = "en"
                    case "italian": key = "it"
                    case "spanish": key = "es"
                    case "german": key = "de"
                    case "dutch": key = "nl"
                    default:break;
                    }
                    languagePopupIsActive = false
                    KMMPreference(context: NSObject()).put(key: "language", value___: key)
                    onLanguageUpdate(key)
                }, onClose: {languagePopupIsActive = false})
            }
            
//            if (metricPopupIsShowing){
//                MetricPopup(onSelectMetric: {
//
//                })
//            }
        }
    }
}

struct SettingItem: Identifiable {
    var id: UUID
    var label: String
}

struct SettingItemRow: View {
    
    var settingItem: SettingItem
    
    public var body: some View{
        HStack{
            Text(LocalizedStringKey(settingItem.label)).fontWeight(.bold)
                .font(.custom("CircularStd-Medium", size: 16))
                .foregroundColor(Color("Tertiary30"))
            Spacer()
            Image(systemName: "arrow.right").foregroundColor(Color("Secondary"))
        }
        .frame(width: .infinity, height: 70)
        .padding(.horizontal, 15)
    }
}

struct BuildInfosRow: View {
    
    var version = Bundle.main.infoDictionary?["CFBundleShortVersionString"] as! String
    var buildVersion = Bundle.main.infoDictionary?["CFBundleVersion"] as! String
    
    public var body: some View{
        HStack{
                Image("logo")
                    .resizable()
                    .frame(width: 50, height: 50)
            
            VStack{
                HStack{
                    Text("technical_infos")
                        .font(.custom("CircularStd-Medium", size: 16)).fontWeight(.medium).foregroundColor(Color("Tertiary30"))
                    Spacer()
                }
                HStack{
                    let txt = NSLocalizedString("version", comment: "default")
                    Text("\(txt): \(version)").font(.custom("CircularStd-Medium", size: 16)).fontWeight(.medium).foregroundColor(Color("Tertiary30"))
                    Spacer()
                }
                HStack{
                    let txt = NSLocalizedString("build", comment: "default")
                    Text("\(txt): \(buildVersion)").font(.custom("CircularStd-Medium", size: 16)).fontWeight(.medium).foregroundColor(Color("Tertiary30"))
                    Spacer()
                }
            }
        }
    }
}

struct LanguagePopup: View {
    
    let languages = ["french", "english", "italian", "spanish", "german", "dutch"]
    var onLanguageSelected: (String) -> Void
    var onClose: () -> Void
    
    public var body: some View{
        VStack{
            Spacer()
            HStack{
                Spacer()
                VStack{
                    ForEach(languages, id: \.self){ language in
                        Text(LocalizedStringKey(language)).font(.custom("CircularStd-Medium", size: 16)).fontWeight(.medium).foregroundColor(Color("Tertiary30")).padding(.vertical, 10).onTapGesture {
                            onLanguageSelected(language)
                        }
                    }
                }.frame(width: .infinity)
                    .padding(.horizontal, 30)
                    .padding(.vertical, 20)
                    .background(Color.white)
                    .cornerRadius(5)
                    .shadow(color: Color.black.opacity(0.2), radius: 2, x: 0, y: 2)
                Spacer()
            }
            Spacer()
        }.background(Color("ClearGrey")).onTapGesture {
            onClose()
        }
    }
}
    
    struct MetricPopup: View {
        
        let metrics = ["meter", "miles"]
        var onSelectMetric: (String) -> Void
        
        public var body: some View{
            VStack{
                ForEach(metrics, id: \.self){ metric in
                    Text(LocalizedStringKey(metric)).font(.custom("CircularStd-Medium", size: 16)).fontWeight(.medium).foregroundColor(Color("Tertiary30")).onTapGesture {
                        onSelectMetric(metric)
                    }
                }.frame(width: .infinity)
                    .padding(.horizontal, 30)
                    .padding(.vertical, 20)
                    .background(Color.white)
                    .cornerRadius(5)
                    .shadow(color: Color.black.opacity(0.2), radius: 2, x: 0, y: 2)
            }
        }
    }
