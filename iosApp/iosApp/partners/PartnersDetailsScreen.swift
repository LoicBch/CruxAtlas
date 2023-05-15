//
//  PartnersDetailsScreen.swift
//  iosApp
//
//  Created by USER on 13/04/2023.
//  Copyright © 2023 orgName. All rights reserved.
//

import Foundation
import shared
import SwiftUI
import MapKit

struct PartnersDetailsScreen: View {
    
    var partner: Partner
    @State private var selection = 0
    
    @State private var offset: CGFloat = 0
    @State private var overViewTabColor  = "Primary"
    @State private var contactsTabColor = "Tertiary"
    
    @State private var tabViewHeight: CGFloat = 1300
    @State var sizeArray: [CGFloat] = [.zero, 300]
    @State var initialHeight: CGFloat = 0
    @State var contactTabHeight: CGFloat = 0
    @Environment(\.presentationMode) var presentationMode
    
    var underLineSize = UIScreen.main.bounds.width / 2
    
    public var body: some View {
        ScrollView(.vertical, showsIndicators: false){
            VStack {
                ZStack{
                    let headerSize = getHeaderSize(partner: partner)
                    
                    if (!partner.photos.isEmpty){
                        ImageCarouselView(images: partner.photos)
                    }
                    
                    VStack{
                        TopDealerButtons(onClose: { self.presentationMode.wrappedValue.dismiss() })
                        BadgePartner(partner: partner).padding(.top, headerSize)
                    }.padding(EdgeInsets(top: 12, leading: 17, bottom: 0, trailing: 17))
                }
                
                HStack{
                    Text("#" + partner.id)
                        .fontWeight(.light)
                        .font(.system(size: 11))
                        .foregroundColor(Color("Tertiary"))
                        .padding(.top, 24)
                        .frame(alignment: .leading)
                    Spacer()
                }.padding(.horizontal, 18)
                
                
                HStack{
                    Text(partner.name)
                        .fontWeight(.black)
                        .font(.system(size: 22))
                        .foregroundColor(Color.black)
                        .padding(.top, 12)
                        .frame(alignment: .leading)
                    Spacer()
                }.padding(.horizontal, 18)
                
                HStack{
                    Text(partner.description_)
                        .fontWeight(.medium)
                        .font(.system(size: 14))
                        .foregroundColor(Color.black)
                        .padding(.top, 12)
                        .frame(alignment: .leading)
                    Spacer()
                }.padding(.horizontal, 18)
                
                VStack{
                    HStack{
                        LocalizedText(key: "overview").onTapGesture {
                            withAnimation { selection = 0 }
                        }
                        .foregroundColor(Color(overViewTabColor))
                        Spacer()
                      
                        LocalizedText(key: "contact_info").onTapGesture {
                            withAnimation { selection = 1 }
                        }
                        .foregroundColor(Color(contactsTabColor))
                    }.padding(.horizontal, 18)
                    
                    ZStack{
                        Rectangle()
                            .frame(width: .infinity, height: 2)
                            .foregroundColor(.gray)
                        
                        HStack{
                            Rectangle()
                                .frame(width: underLineSize, height: 2)
                                .foregroundColor(Color("Primary"))
                                .offset(x: offset)
                            Spacer()
                        }
                        
                    }
                }.padding(EdgeInsets(top: 24, leading: 18, bottom: 0, trailing: 18))
                    .frame(width: .infinity)
                
                TabView(selection: $selection) {
                    OverviewTabPartner(partner: partner)
                        .fixedSize(horizontal: false, vertical: true)
                        .readSize(onChange: { size in
                            sizeArray[0] = size.height
                            if initialHeight == 0 {
                                initialHeight = size.height
                            }
                        })
                        .tag(0)
                    
                    ContactTabPartner(partner: partner)
                        .fixedSize(horizontal: false, vertical: true)
                        .readSize(onChange: { size in
                            sizeArray[1] = size.height
                            contactTabHeight = size.height
                        })
                        .tag(1)
                }
                .tabViewStyle(PageTabViewStyle(indexDisplayMode: .never))
                .frame(width: UIScreen.main.bounds.width, height: tabViewHeight)
                .onChange(of: selection) { newValue in
                    withAnimation {
                        tabViewHeight = sizeArray[newValue]
                    }
                }
                .onChange(of: sizeArray){ heightArray in
//                    setting tabview on first appearance
                    switch (selection){
                    case 0:
                        if (selection == 1){
                            tabViewHeight = heightArray[0]
                        }
                    case 1 :
                        if (selection == 1){
                            tabViewHeight = heightArray[1]
                        }
                    default: break
                    }
                }
                .onChange(of: initialHeight) { newValue in
                    tabViewHeight = newValue
                }
                
            }.edgesIgnoringSafeArea(.all)
                .onChange(of: selection, perform: { selection in
                    switch(selection){
                    case 0:
                        withAnimation { offset = 0 }
                        overViewTabColor = "Primary"
                        contactsTabColor = "Tertiary"
                    case 1:
                        withAnimation { offset = underLineSize }
                        contactsTabColor = "Tertiary"
                        overViewTabColor = "Tertiary"
                    default:
                        overViewTabColor = "Primary"
                    }
                })
            
            AppButton(action: {
                
            }, title: "get_offer", isEnable: true)
                .padding(.top, 42)
        }
    }
}

func getHeaderSize(partner: Partner) -> CGFloat{
    if (!partner.photos.isEmpty){
        return 100
    }else{
        return 50
    }
}

struct OverviewTabPartner: View {
    
    var partner: Partner
    
    public var body: some View {
        VStack {
            ContactRowPremiumPartner(partner: partner).padding(.top, 16)
            Text("Missing content")
                .padding(.vertical, 50)
        }.padding(.horizontal, 18)
    }
}

struct ContactTabPartner: View {
    
    var partner: Partner
    @Environment(\.openURL) var openURL
    
    public var body: some View{
        VStack{
            
            if (!partner.website.isEmpty){
                HStack{
                    Image("website")
                    Text(partner.website)
                        .fontWeight(.medium)
                        .font(.system(size: 12))
                        .foregroundColor(Color.black)
                        .padding(.leading, 14)
                    Spacer()
                }.padding(.top, 24).onTapGesture {
                    openURL(URL(string: partner.website)!)
                }
                Divider()
            }
            
            if (!partner.email.isEmpty){
                HStack{
                    Image("mail")
                    Text(partner.email)
                        .fontWeight(.medium)
                        .font(.system(size: 12))
                        .foregroundColor(Color.black)
                        .padding(.leading, 14)
                    Spacer()
                }.padding(.top, 24).onTapGesture {
                    EmailController.shared.sendEmail(subject: partner.name, body: partner.name + partner.name, to: partner.email)
                }
                Divider()
            }
            
            if (!partner.phone.isEmpty){
                HStack{
                    Image("phone")
                    Text(partner.phone)
                        .fontWeight(.medium)
                        .font(.system(size: 12))
                        .foregroundColor(Color.black)
                        .padding(.leading, 14)
                    Spacer()
                }.padding(.top, 24).onTapGesture {
                    let phone = "tel://"
                    let phoneNumberformatted = phone + partner.phone
                    guard let url = URL(string: phoneNumberformatted) else { return }
                    UIApplication.shared.open(url)
                }
                Divider()
            }
            
                if (!partner.facebook.isEmpty){
                    HStack{
                        Image("facebook")
                        Text(partner.facebook)
                            .fontWeight(.medium)
                            .font(.system(size: 12))
                            .foregroundColor(Color.black)
                            .padding(.leading, 14)
                        Spacer()
                    }.padding(.top, 24).onTapGesture {
                        openURL(URL(string: partner.facebook)!)
                    }
                    Divider()
                }
                
                if (!partner.twitter.isEmpty){
                    HStack{
                        Image("twitter")
                        Text(partner.twitter)
                            .fontWeight(.medium)
                            .font(.system(size: 12))
                            .foregroundColor(Color.black)
                            .padding(.leading, 14)
                        Spacer()
                    }.padding(.top, 24).onTapGesture {
                        openURL(URL(string: partner.twitter)!)
                    }
                    Divider()
                }
                
                if (!partner.youtube.isEmpty){
                    HStack{
                        Image("youtube")
                        Text(partner.youtube)
                            .fontWeight(.medium)
                            .font(.system(size: 12))
                            .foregroundColor(Color.black)
                            .padding(.leading, 14)
                        Spacer()
                    }.padding(.top, 24).onTapGesture {
                        openURL(URL(string: partner.youtube)!)
                    }
                    Divider()
            }
        }.padding(.horizontal, 18)
    }
}

struct ContactRowPremiumPartner: View {
    
    var partner: Partner
    @Environment(\.openURL) var openURL
    
    public var body: some View{
        HStack{
            
            if (!partner.website.isEmpty) {
                Button(action: {
                    openURL(URL(string: partner.website)!)
                }){
                    Image("website")
                }
                .frame(width: 50, height: 50)
                .background(Color.white)
                .cornerRadius(25)
                .buttonStyle(.plain)
                .overlay(
                    RoundedRectangle(cornerRadius: 25).stroke(Color("Secondary"), lineWidth: 1)
                )
            }
            
                        if (!partner.phone.isEmpty) {
            Button(action: {
                let phone = "tel://"
                let phoneNumberformatted = phone + partner.phone
                guard let url = URL(string: phoneNumberformatted) else { return }
                UIApplication.shared.open(url)
            }){
                Image("phone")
            }
            .frame(width: 50, height: 50)
            .background(Color.white)
            .cornerRadius(25)
            .buttonStyle(.plain)
            .overlay(
                RoundedRectangle(cornerRadius: 25).stroke(Color("Secondary"), lineWidth: 1)
            )
                        }
            
            
            
                        if (!partner.email.isEmpty) {
            Button(action: {
                EmailController.shared.sendEmail(subject: partner.name, body: partner.name + partner.name, to: partner.email)
            }){
                Image("mail")
            }
            .frame(width: 50, height: 50)
            .background(Color.white)
            .cornerRadius(25)
            .buttonStyle(.plain)
            .overlay(
                RoundedRectangle(cornerRadius: 25).stroke(Color("Secondary"), lineWidth: 1)
            )
                        }
            
            
            
                        if (!partner.facebook.isEmpty) {
            Button(action: {
                openURL(URL(string: partner.facebook)!)
            }){
                Image("facebook")
            }
            .frame(width: 50, height: 50)
            .background(Color.white)
            .cornerRadius(25)
            .buttonStyle(.plain)
            .overlay(
                RoundedRectangle(cornerRadius: 25).stroke(Color("Secondary"), lineWidth: 1)
            )
                        }
            
            
            
                        if (!partner.twitter.isEmpty) {
            Button(action: {
                openURL(URL(string: partner.twitter)!)
            }){
                Image("twitter")
            }
            .frame(width: 50, height: 50)
            .background(Color.white)
            .cornerRadius(25)
            .buttonStyle(.plain)
            .overlay(
                RoundedRectangle(cornerRadius: 25).stroke(Color("Secondary"), lineWidth: 1)
            )
                        }
            
            
            if (!partner.youtube.isEmpty) {
                Button(action: {
                    openURL(URL(string: partner.youtube)!)
                }){
                    Image("youtube")
                }
                .frame(width: 50, height: 50)
                .background(Color.white)
                .cornerRadius(25)
                .buttonStyle(.plain)
                .overlay(
                    RoundedRectangle(cornerRadius: 25).stroke(Color("Secondary"), lineWidth: 1)
                )
            }
            Spacer()
        }.frame(width: .infinity)
    }
}

struct BadgePartner: View {
    
    var partner: Partner
    
    public var body: some View{
        HStack{
            Image("shield")
                .frame(width: 16, height: 16)
                .padding(5)
                .background(Color.white)
                .cornerRadius(5)
                .shadow(radius: 5, x:-1, y: 1)
            
            Spacer()
            
            HStack(){
                Image("premium_badge")
                    .frame(height: 30)
                    .padding(.leading, 5)
                
                LocalizedText(key: "verified")
//                    .fontWeight(.light)
                    .font(.system(size: 12))
                    .foregroundColor(Color("Primary"))
                    .padding(.trailing, 5)
            }.background(Color.white)
                .cornerRadius(15)
                .shadow(radius: 5, x:-1, y: 1)
        }
    }
}
