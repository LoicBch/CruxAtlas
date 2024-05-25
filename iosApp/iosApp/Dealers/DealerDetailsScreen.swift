//
//  DealerDetailsScreen.swift
//  iosApp
//
//  Created by USER on 13/04/2023.
//  Copyright © 2023 orgName. All rights reserved.
//

import Foundation
import shared
import SwiftUI
import MapKit

struct DealerDetailsScreen: View {
    
    var dealer : Dealer
    @State private var selection = 0
    
    @State private var offset: CGFloat = 0
    @State private var overViewTabColor  = "Primary"
    @State private var infosTabColor = "Tertiary70"
    @State private var contactsTabColor = "Tertiary70"
    
    @State private var tabViewHeight: CGFloat = 1300
    @State var sizeArray: [CGFloat] = [.zero, 300, 300]
    @State var initialHeight: CGFloat = 0
    @State var contactTabHeight: CGFloat = 0
    @State var infosTabHeight: CGFloat = 0
    @State var currentImage = "" 
    
    @Environment(\.presentationMode) var presentationMode
    
    var underLineSize = (UIScreen.main.bounds.width / 3) - 18
    
    var body: some View {
        ScrollView(.vertical, showsIndicators: false){
            VStack {
                ZStack{
                    let headerSize = getHeaderSize(dealer: dealer)
                    
                    if (dealer.isPremium && !dealer.photos.isEmpty){
                        ImageCarouselView(images: dealer.photos)
                    }
                    
                    VStack {
                        TopDealerButtons(onClose: { self.presentationMode.wrappedValue.dismiss() }, onShare: {
                            share(shared: dealer.stringToSend())
                        })
                        .padding(.top, 12)
                        
                        BadgeDealer(dealer: dealer).padding(.bottom, 10).padding(.top, headerSize)
                    }
                    .padding(EdgeInsets(top: 0, leading: 16, bottom: 0, trailing: 16))
                }
                
                HStack{
                    Text("#" + dealer.id)
                        .fontWeight(.medium)
                        .font(.custom("CircularStd-Medium", size: 11))
                        .foregroundColor(Color("Neutral20"))
                        .padding(.top, 24)
                        .frame(alignment: .leading)
                    Spacer()
                }.padding(.horizontal, 18)
                
                
                HStack{
                    Text(dealer.name)
                        .fontWeight(.bold)
                        .font(.custom("CircularStd-Medium", size: 22))
                        .foregroundColor(Color.black)
                        .padding(.top, 12)
                        .frame(alignment: .leading)
                    Spacer()
                }.padding(.horizontal, 18)
                
                
                VStack{
                    HStack{
                        Text("overview")
                            .fontWeight(.medium).frame(alignment: .leading)
                            .onTapGesture {
                            selection = 0
                        }
                        .font(.custom("CircularStd-Medium", size: 14))
                        .foregroundColor(Color(overViewTabColor))
                        
                        Spacer()
                        Text("details")
                            .fontWeight(.medium).frame(alignment: .leading)
                            .onTapGesture {
                            selection = 1
                        }
                        .foregroundColor(Color(infosTabColor))
                        .font(.custom("CircularStd-Medium", size: 14))
                        
                        Spacer()
                        Text("contact_info").fontWeight(.medium).onTapGesture {
                            selection = 2
                        }
                        .foregroundColor(Color(contactsTabColor))
                        .font(.custom("CircularStd-Medium", size: 14))
                        
                    }.padding(.horizontal, 18)
                    
                    ZStack{
                        Rectangle()
                            .frame(width: .infinity, height: 1)
                            .foregroundColor(Color("Tertiary70"))
                        
                        HStack{
                            Rectangle()
                                .frame(width: underLineSize, height: 3)
                                .foregroundColor(Color("Primary"))
                                .offset(x: offset)
                            Spacer()
                        }
                        
                    }
                }.padding(EdgeInsets(top: 12, leading: 18, bottom: 0, trailing: 18))
                    .frame(width: .infinity)
                
                TabView(selection: $selection) {
                    OverviewTab(dealer: dealer, onViewAll: { selection = 1 })
                        .fixedSize(horizontal: false, vertical: true)
                        .readSize(onChange: { size in
                            print("read overview")
                            sizeArray[0] = size.height
                            if initialHeight == 0 {
                                initialHeight = size.height
                            }
                        })
                        .tag(0)
                    
                    DetailsTab(dealer: dealer)
                        .fixedSize(horizontal: false, vertical: true)
                        .readSize(onChange: { size in
                            print("read details")
                            sizeArray[1] = size.height
                            infosTabHeight = size.height
                        })
                        .tag(1)
                    
                    ContactTab(dealer: dealer)
                        .fixedSize(horizontal: false, vertical: true)
                        .readSize(onChange: { size in
                            print("read COtnact")
                            sizeArray[2] = size.height
                            contactTabHeight = size.height
                        })
                        .tag(2)
                }
                .tabViewStyle(PageTabViewStyle(indexDisplayMode: .never))
                .onChange(of: selection) { selection in
                    withAnimation {
                        tabViewHeight = sizeArray[selection]
                    }
                }.onChange(of: sizeArray){ heightArray in
//                    we have to onChange the size array because of tabview reseting to 
                    switch (selection){
                    case 0:
                        if (selection == 1){
                            tabViewHeight = heightArray[0]
                        }
                    case 1 :
                        if (selection == 1){
                            tabViewHeight = heightArray[1]
                        }
                    case 2:
                        if (selection == 2){
                            tabViewHeight = heightArray[2]
                        }
                    default: break
                    }
                }
                .onChange(of: initialHeight) { newValue in
                    tabViewHeight = newValue
                }
                .frame(width: UIScreen.main.bounds.width, height: tabViewHeight)
                
            }.edgesIgnoringSafeArea(.all)
                .onChange(of: selection, perform: { selection in
                    switch(selection){
                    case 0:
                        withAnimation { offset = 0 }
                        overViewTabColor = "Primary"
                        contactsTabColor = "Tertiary70"
                        infosTabColor = "Tertiary70"
                    case 1:
                        withAnimation { offset = underLineSize }
                        infosTabColor = "Primary"
                        contactsTabColor = "Tertiary70"
                        overViewTabColor = "Tertiary70"
                    case 2:
                        withAnimation { offset = (underLineSize * 2) }
                        contactsTabColor = "Primary"
                        infosTabColor = "Tertiary70"
                        overViewTabColor = "Tertiary70"
                    default:
                        overViewTabColor = "Primary"
                    }
                })
        }
        AppButton(action: {
            let coordinate = CLLocationCoordinate2DMake(dealer.latitude, dealer.longitude)
            let mapItem = MKMapItem(placemark: MKPlacemark(coordinate: coordinate, addressDictionary:nil))
            mapItem.name = "Target location"
            mapItem.openInMaps(launchOptions: [MKLaunchOptionsDirectionsModeKey : MKLaunchOptionsDirectionsModeDriving])
            
        }, title: "navigate", isEnable: true)
        .padding(.top, 24).padding(.bottom, 25)
    }
}

func getHeaderSize(dealer: Dealer) -> CGFloat{
    if (dealer.isPremium && !dealer.photos.isEmpty){
        return 80
    }else{
        return 40
    }
}

struct BadgeDealer: View {
    
    var dealer: Dealer
    
    public var body: some View{
        HStack{
            if (!dealer.services.isEmpty){
                Image("repair")
                    .frame(width: 16, height: 16)
                    .padding(8)
                    .background(Color.white)
                    .cornerRadius(8)
                    .shadow(color: Color.black.opacity(0.2), radius: 2, x: 0, y: 2)
            }
            
            if (!dealer.brands.isEmpty){
                Image("dealers")
                    .frame(width: 16, height: 16)
                    .padding(8)
                    .background(Color.white)
                    .cornerRadius(8)
                    .shadow(color: Color.black.opacity(0.2), radius: 2, x: 0, y: 2)
            }
            
            Spacer()
            
            if (dealer.isPremium) {
                HStack(){
                    Image("premium_badge")
                        .frame(height: 30)
                        .padding(.leading, 5)
                    
                    Text("verified")
                        .font(.custom("CircularStd-Medium", size: 12))
                        .foregroundColor(Color("greenVerified"))
                        .padding(.trailing, 5)
                }.background(Color.white)
                    .cornerRadius(8)
                    .shadow(color: Color.black.opacity(0.2), radius: 2, x: 0, y: 2)
            }
        }
    }
}

struct TopDealerButtons: View {
    
    var onClose: () -> Void
    var onShare: () -> Void
    
    public var body: some View{
        HStack{
            Button(action: {
                onClose()
            }){
                Image("cross_round")
            }
//            .buttonStyle(AppButtonStyle())
            
            Spacer()
            
            Button(action: {
                onShare()
            }){
                Image("share_round")
            }
//            .buttonStyle(AppButtonStyle())
            .padding(.trailing, 8)
            
            NavigationLink(destination: HelpScreen()
                .navigationBarBackButtonHidden(true)
            ) {
                    Image("help_round")
            }
            
//            .padding(EdgeInsets(top: 16, leading: 18, bottom: 16, trailing: 18))
//                .background(Color.white)
//                .foregroundColor(.white)
//                .cornerRadius(50)
//                .shadow(color: Color.black.opacity(0.2), radius: 2, x: 0, y: 2)
        }
    }
}

struct OverviewTab: View {
    
    var dealer: Dealer
    var onViewAll: () -> Void
    
    public var body: some View {
        VStack {
            if (dealer.isPremium){
                ContactRowPremium(dealer: dealer)
                    .padding(.top, 20)
            }else {
                ContactRow(dealer: dealer)
                    .padding(.top, 20)
            }
            
            HStack{
                Image("distance").colorMultiply(Color("Tertiary30"))
                Text(Location(latitude: dealer.latitude, longitude: dealer.longitude).distanceFromUserLocationText())
                    .fontWeight(.medium)
                    .font(.custom("CircularStd-Medium", size: 12))
                    .foregroundColor(Color.black)
                    .padding(.trailing, 12)
                Spacer()
            }.padding(.top, 25)
            
            HStack{
                Image("pin_here").colorMultiply(Color("Tertiary30"))
                Text(dealer.fullLocation())
                    .fontWeight(.medium)
                    .font(.custom("CircularStd-Medium", size: 12))
                    .foregroundColor(Color.black)
                    .padding(.trailing, 12)
                Spacer()
            }.padding(.top, 12)
            
            HStack{
                Image("my_location").colorMultiply(Color("Tertiary30"))
                Text(dealer.fullGeolocalisation)
                    .fontWeight(.medium)
                    .font(.custom("CircularStd-Medium", size: 12))
                    .foregroundColor(Color.black)
                    .padding(.trailing, 12)
                Spacer()
            }.padding(.top, 12)
            ServicesBlock(services: Array(dealer.services.prefix(3)), onViewAll: { onViewAll() }, fullList: dealer.services.count < 3)
                .padding(.top, 42)
            BrandsBlock(brands: Array(dealer.brands.prefix(6)), onViewAll: { onViewAll() }, fullList: dealer.brands.count < 6)
                .padding(.top, 42)
        }.padding(.horizontal, 18)
    }
}

struct ServicesBlock: View {
    
    var services: [String]
    var onViewAll: () -> Void
    var fullList: Bool
    
    public var body: some View{
        VStack{
            HStack {
                Text("services")
                    .font(.custom("CircularStd-Medium", size: 16))
                    .foregroundColor(Color.black)
                Spacer()
            }.padding(.bottom, 8)
            
            HStack {
                Text("This is additional information we can add.")
                    .fontWeight(.medium)
                    .font(.custom("CircularStd-Medium", size: 12))
                    .foregroundColor(Color.black)
                Spacer()
            }
            Divider()
            
            ForEach(services, id: \.self){ service in
                 
                var service: String = Globals.filters().services.first(where: {$0.first?.lowercased == service})?.second as! String
                
                HStack{
                    Text(service)
                        .fontWeight(.medium)
                        .font(.custom("CircularStd-Medium", size: 14))
                        .foregroundColor(Color("Tertiary40"))
                        .padding(.top, 6)
                    Spacer()
                }
                
            }
            if(!fullList){
                HStack {
                    Text("view_all_services")
                        .font(.custom("CircularStd-Medium", size: 14))
                        .foregroundColor(Color("Secondary"))
                    Spacer()
                    Image(systemName: "arrow.right")
                        .foregroundColor(Color("Secondary"))
                }.onTapGesture {
                    onViewAll()
                }.padding(.top, 18)
            }
        }
    }
}

struct BrandsBlock: View{
    
    var brands: [String]
    var onViewAll: () -> Void
    var fullList: Bool
    
    public var body: some View{
        VStack{
            HStack {
                Text("official_dealers")
                    .font(.custom("CircularStd-Medium", size: 16))
                    .foregroundColor(Color.black)
                Spacer()
            }.padding(.bottom, 8)
            HStack {
                Text("This is additional information we can add.")
                    .fontWeight(.medium)
                    .font(.custom("CircularStd-Medium", size: 12))
                    .foregroundColor(Color.black)
                Spacer()
            }
            Divider()
            
            if (brands.count > 3){
                var dealerChunks = brands.chunked(into: 3)
                ForEach(dealerChunks, id: \.self){ rowElements in
                    HStack{
                        ForEach(rowElements, id: \.self){ rowElements in
                            
                            var brand = Globals.filters().brands.first(where: { $0.first?.lowercased == rowElements })?.second as! String
                            
                            Text(brand)
                                .fontWeight(.medium)
                                .padding(.horizontal, 10)
                                .padding(.vertical, 5)
                                .font(.custom("CircularStd-Medium", size: 14))
                                .foregroundColor(Color("Tertiary40"))
                                .padding(.trailing, 6)
                                .overlay(
                                    RoundedRectangle(cornerRadius: 5)
                                        .stroke(Color("unSelectedFilter"), lineWidth: 1)
                                )
                        }
                        Spacer()
                    }.frame(width: .infinity)
                }
                
            }else {
                HStack{
                    ForEach(brands, id: \.self){ brandIndex in
                        
                        var brand = Globals.filters().brands.first(where: { $0.first?.lowercased == brandIndex })?.second as! String
                        
                        Text(brand)
                            .fontWeight(.medium)
                            .padding(.horizontal, 10)
                            .padding(.vertical, 5)
                            .font(.custom("CircularStd-Medium", size: 14))
                            .foregroundColor(Color("Tertiary40"))
                            .padding(.trailing, 6)
                            .overlay(
                                RoundedRectangle(cornerRadius: 5).stroke(Color("unSelectedFilter"), lineWidth: 1)
                            )
                    }
                    Spacer()
                }
            }
            
            if (!fullList){
                HStack {
                    Text("view_all_brands")
                        .font(.custom("CircularStd-Medium", size: 14))
                        .foregroundColor(Color("Secondary"))
                    Spacer()
                    Image(systemName: "arrow.right")
                        .foregroundColor(Color("Secondary"))
                }.onTapGesture {
                    onViewAll()
                }.padding(.top, 18)
            }
        }
    }
}


struct ContactRow: View {
    
    var dealer: Dealer
    @Environment(\.openURL) var openURL
    
    public var body: some View{
        HStack{
            if (!dealer.website.isEmpty) {
                Button(action: {
                    openURL(URL(string: dealer.website)!)
                }){
                    HStack{
                        Image("website")
                        Text(dealer.website)
                            .fontWeight(.medium).frame(alignment: .leading)
                            .padding(.leading, 4)
                            .font(.custom("CircularStd-Medium", size: 12))
                            .foregroundColor(Color("Secondary"))
                    }
                    .padding(.horizontal, 10)
                    .padding(.vertical, 5)
                    .overlay(
                        RoundedRectangle(cornerRadius: 25).stroke(Color("Secondary"), lineWidth: 1)
                    )
                }.padding(.trailing, 10)
            }
            
            if (!dealer.phone.isEmpty) {
                Button(action: {
                    let phone = "tel://"
                    let phoneNumberformatted = phone + dealer.phone
                    guard let url = URL(string: phoneNumberformatted) else { return }
                    UIApplication.shared.open(url)
                }){
                    HStack{
                        Image("phone")
                        Text(dealer.phone)
                            .fontWeight(.medium)
                            .padding(.leading, 4)
                            .font(.custom("CircularStd-Medium", size: 12))
                            .foregroundColor(Color("Secondary"))
                    }
                    .padding(.horizontal, 10)
                    .padding(.vertical, 5)
                    .overlay(
                        RoundedRectangle(cornerRadius: 25).stroke(Color("Secondary"), lineWidth: 1)
                    )
                }
            }
            Spacer()
        }
    }
}

func share(shared: String) {
    let activityViewController = UIActivityViewController(activityItems: [shared], applicationActivities: nil)
    if let rootViewController = UIApplication.shared.windows.first?.rootViewController {
        rootViewController.present(activityViewController, animated: true, completion: nil)
    }
}

struct ContactRowPremium: View {
    
    var dealer: Dealer
    @Environment(\.openURL) var openURL
    
    public var body: some View{
        HStack{
            
            if (!dealer.website.isEmpty) {
                Button(action: {
                    openURL(URL(string: dealer.website)!)
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
            
            if (!dealer.phone.isEmpty) {
                Button(action: {
                    let phone = "tel://"
                    let phoneNumberformatted = phone + dealer.phone
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
            
            
            
            if (!dealer.email.isEmpty) {
                Button(action: {
                    EmailController.shared.sendEmail(subject: dealer.name, body: dealer.name + dealer.name, to: dealer.email)
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
            
            
            
            if (!dealer.facebook.isEmpty) {
                Button(action: {
                    openURL(URL(string: dealer.facebook)!)
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
            
            
            
            if (!dealer.twitter.isEmpty) {
                Button(action: {
                    openURL(URL(string: dealer.twitter)!)
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
            
            
            if (!dealer.youtube.isEmpty) {
                Button(action: {
                    openURL(URL(string: dealer.youtube)!)
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

struct DetailsTab: View {
    
    var dealer: Dealer
    
    public var body: some View{
        VStack{
            ServicesBlock(services: dealer.services, onViewAll: {}, fullList: true).padding(.top, 42)
            BrandsBlock(brands: dealer.brands, onViewAll: {}, fullList: true).padding(.top, 42)
        }.padding(.horizontal, 18)
    }
}

struct ContactTab: View {
    
    var dealer: Dealer
    @Environment(\.openURL) var openURL
    
    public var body: some View{
        VStack{
            
            HStack{
                Image("pin_here_square")
                Text(dealer.fullLocation)
                    .fontWeight(.medium).frame(alignment: .leading)
                    .padding(.leading, 14)
                    .font(.custom("CircularStd-Medium", size: 12))
                    .foregroundColor(Color.black)
                Spacer()
            }.padding(.top, 24).padding(.bottom, 16)
            Divider()
            
            HStack{
                Image("my_location_square")
                    .renderingMode(.template)
                    .foregroundColor(Color("Secondary"))
                Text(dealer.fullLocation)
                    .fontWeight(.medium).frame(alignment: .leading)
                    .font(.custom("CircularStd-Medium", size: 12))
                    .foregroundColor(Color.black)
                    .padding(.leading, 14)
                
                Spacer()
            }.padding(.vertical, 16)
            Divider()
            
            if (!dealer.website.isEmpty){
                HStack{
                    Image("website_square")
                    Text(dealer.website)
                        .fontWeight(.medium).frame(alignment: .leading)
                        .font(.custom("CircularStd-Medium", size: 12))
                        .foregroundColor(Color.black)
                        .padding(.leading, 14)
                    Spacer()
                }.padding(.vertical, 16).onTapGesture {
                    openURL(URL(string: dealer.website)!)
                }
                Divider()
            }
            
            if (!dealer.email.isEmpty){
                HStack{
                    Image("mail_square")
                        .renderingMode(.template)
                        .foregroundColor(Color("Secondary"))
                    Text(dealer.email)
                        .fontWeight(.medium).frame(alignment: .leading)
                        .font(.custom("CircularStd-Medium", size: 12))
                        .foregroundColor(Color.black)
                        .padding(.leading, 14)
                    Spacer()
                }.padding(.vertical, 16).onTapGesture {
                    EmailController.shared.sendEmail(subject: dealer.name, body: dealer.name + dealer.name, to: dealer.email)
                }
                Divider()
            }
            
            if (!dealer.phone.isEmpty){
                HStack{
                    Image("phone_square")
                        .renderingMode(.template)
                        .foregroundColor(Color("Secondary"))
                    Text(dealer.phone)
                        .fontWeight(.medium).frame(alignment: .leading)
                        .font(.custom("CircularStd-Medium", size: 12))
                        .foregroundColor(Color.black)
                        .padding(.leading, 14)
                    Spacer()
                }.padding(.vertical, 16).onTapGesture {
                    let phone = "tel://"
                    let phoneNumberformatted = phone + dealer.phone
                    guard let url = URL(string: phoneNumberformatted) else { return }
                    UIApplication.shared.open(url)
                }
                Divider()
            }
            
            if(dealer.isPremium){
                if (!dealer.facebook.isEmpty){
                    HStack{
                        Image("facebook_square")
                            .renderingMode(.template)
                            .foregroundColor(Color("Secondary"))
                        Text(dealer.facebook)
                            .fontWeight(.medium).frame(alignment: .leading)
                            .font(.custom("CircularStd-Medium", size: 12))
                            .foregroundColor(Color.black)
                            .padding(.leading, 14)
                        Spacer()
                    }.padding(.vertical, 16).onTapGesture {
                        openURL(URL(string: dealer.facebook)!)
                    }
                    Divider()
                }
                
                if (!dealer.twitter.isEmpty){
                    HStack{
                        Image("twitter_square")
                            .renderingMode(.template)
                            .foregroundColor(Color("Secondary"))
                        Text(dealer.twitter)
                            .fontWeight(.medium).frame(alignment: .leading)
                            .font(.custom("CircularStd-Medium", size: 12))
                            .foregroundColor(Color.black)
                            .padding(.leading, 14)
                        Spacer()
                    }.padding(.vertical, 16).onTapGesture {
                        openURL(URL(string: dealer.twitter)!)
                    }
                    Divider()
                }
                
                if (!dealer.youtube.isEmpty){
                    HStack{
                        Image("youtube_square")
                            .renderingMode(.template)
                            .foregroundColor(Color("Secondary"))
                        Text(dealer.youtube)
                            .fontWeight(.medium).frame(alignment: .leading)
                            .font(.custom("CircularStd-Medium", size: 12))
                            .foregroundColor(Color.black)
                            .padding(.leading, 14)
                        Spacer()
                    }.padding(.vertical, 16).onTapGesture {
                        openURL(URL(string: dealer.youtube)!)
                    }
                    Divider()
                }
            }
        }.padding(.horizontal, 18)
    }
}
