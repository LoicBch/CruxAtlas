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
    @State var currentAppLanguage = UserDefaults.standard.string(forKey: "language")
    
    var SettingItems = [
        SettingItem(id: UUID(), label: "measuring_system"),
        SettingItem(id: UUID(), label: "language"),
        SettingItem(id: UUID(), label: "help"),
        SettingItem(id: UUID(), label: "term_of_use"),
        SettingItem(id: UUID(), label: "privacy_policy")
    ]
    
    public var body: some View {
        ZStack{
            VStack{
                HStack{
                    Image(systemName: "chevron.left")
                    Spacer()
                    LocalizedText(key: "app_settings")
                    Spacer()
                    Image(systemName: "")
                }
                
                NavigationView(){
                    VStack{
                            ForEach(SettingItems, id: \.id) { settingItem in
                                VStack{
                                    SettingItemRow(settingItem: settingItem)
                                    Divider().padding(.horizontal, 15)
                                }
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
                        BuildInfosRow()
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
                    currentAppLanguage = key
                    languagePopupIsActive = false
                })
            }
            
            if (metricPopupIsShowing){
                MetricPopup()
            }
        }.onChange(of: currentAppLanguage){ languageSelected in
            UserDefaults.standard.set(languageSelected, forKey: "language")
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
            LocalizedText(key: settingItem.label)
                .font(.system(size: 16, weight: .medium))
                .foregroundColor(Color("Tertiary"))
            Spacer()
            Image(systemName: "chevron.right").foregroundColor(Color("Secondary"))
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
            VStack{
                Spacer()
                Image("logo")
                    .resizable()
                    .frame(width: 50, height: 50)
            }.padding(.leading, 15)
            
            VStack{
                Spacer()
                HStack{
                    LocalizedText(key: "technical_infos").font(.system(size: 16, weight: .medium))
                    Spacer()
                }
                HStack{
                    let txt = NSLocalizedString("version", comment: "default")
                    Text("\(txt): \(version)").font(.system(size: 16, weight: .medium))
                    Spacer()
                }
                HStack{
                    let txt = NSLocalizedString("build", comment: "default")
                    Text("\(txt): \(buildVersion)").font(.system(size: 16, weight: .medium))
                    Spacer()
                }
            }
        }
    }
}

struct LanguagePopup: View {
    
    let languages = ["french", "english", "italian", "spanish", "german", "dutch"]
    var onLanguageSelected: (String) -> Void
    
    public var body: some View{
        VStack{
            ForEach(languages, id: \.self){ language in
                LocalizedText(key: language).padding(.vertical, 10).onTapGesture {
                    onLanguageSelected(language)
                }
            }
        }.frame(width: .infinity)
            .padding(.horizontal, 30)
            .padding(.vertical, 20)
        .background(Color.white)
            .cornerRadius(5)
            .shadow(radius: 25, x: 1, y: 2)
    }
}

struct MetricPopup: View {
    
    let metrics = ["meter", "miles"]
    
    public var body: some View{
        VStack{
            ForEach(metrics, id: \.self){ metric in
                LocalizedText(key: metric)
            }.frame(width: .infinity)
                .padding(.horizontal, 20)
                .background(Color.white)
                .cornerRadius(5)
                .shadow(radius: 25, x: 1, y: 2)
        }
    }
}
