//
//  EventDetailsScreen.swift
//  iosApp
//
//  Created by USER on 13/04/2023.
//  Copyright © 2023 orgName. All rights reserved.
//

import Foundation
import shared
import SwiftUI
import MapKit

struct EventDetailsScreen: View {
    
    var event: Event
    @Environment(\.presentationMode) var presentationMode
    
    public var body: some View {
        ScrollView{
            VStack{
                ZStack{
                    if (event.photos.count > 0){
                        ImageCarouselView(images: event.photos)
                    }
                    
                    VStack{
                        TopDealerButtons(onClose: { self.presentationMode.wrappedValue.dismiss() }, onShare: { share(shared: event.stringToSend())})
                    }.padding(EdgeInsets(top: 12, leading: 17, bottom: 0, trailing: 17))
                }
                
                HStack{
                    Text(event.name)
                        .fontWeight(.bold)
                        .font(.custom("CircularStd-Medium", size: 22))
                        .foregroundColor(Color.black)
                        .padding(.top, 24)
                        .frame(alignment: .leading)
                    Spacer()
                }
                
                
                HStack{
                    Text(event.descriptionEn)
                        .fontWeight(.medium)
                        .font(.custom("CircularStd-Medium", size: 14))
                        .foregroundColor(Color("Neutral20"))
                        .padding(.top, 12)
                        .frame(alignment: .leading)
                    Spacer()
                }
                
                Divider()
                
                InfosList(event: event)
                 
                Spacer()
                
                AppButton(action: {
                    
                    let coordinate = CLLocationCoordinate2DMake(event.latitude, event.longitude)
                    let mapItem = MKMapItem(placemark: MKPlacemark(coordinate: coordinate, addressDictionary:nil))
                    mapItem.name = "Target location"
                    mapItem.openInMaps(launchOptions: [MKLaunchOptionsDirectionsModeKey : MKLaunchOptionsDirectionsModeDriving])
                    
                }, title: "navigate", isEnable: true)
                
            }
            
        }.padding(.horizontal, 18)
    }
}  

struct InfosList: View {
    
    var event: Event
    
    var body: some View {
        HStack{
            Image("events").renderingMode(.template).resizable().frame(width: 20, height: 20)
                .foregroundColor(Color("Secondary"))
            Text("\(event.dateBegin) - \(event.dateEnd)")
                .fontWeight(.medium)
                .font(.custom("CircularStd-Medium", size: 12))
                .foregroundColor(Color.black)
                .padding(.leading, 14)
            Spacer()
        }.padding(.vertical, 16)
        Divider()
        
        HStack{
            Image("website_square").renderingMode(.template)
                .foregroundColor(Color("Secondary"))
            Text(event.website)
                .fontWeight(.medium)
                .font(.custom("CircularStd-Medium", size: 12))
                .foregroundColor(Color.black)
                .padding(.leading, 14)
            Spacer()
        }.padding(.vertical, 16)
        Divider()
        
        HStack{
            Image("pin_here_square").renderingMode(.template)
                .foregroundColor(Color("Secondary"))
            Text(event.fullLocation)
                .fontWeight(.medium)
                .font(.custom("CircularStd-Medium", size: 12))
                .foregroundColor(Color.black)
                .padding(.leading, 14)
            Spacer()
        }.padding(.vertical, 16)
        Divider()
        
        HStack{
            Image("my_location_square").renderingMode(.template)
                .foregroundColor(Color("Secondary"))
            Text(event.fullGeolocation())
                .fontWeight(.medium)
                .font(.custom("CircularStd-Medium", size: 12))
                .foregroundColor(Color.black)
                .padding(.leading, 14)
            Spacer()
        }.padding(.vertical, 16)
        Divider()
        
        HStack{
            Image("distance_square").renderingMode(.template)
                .foregroundColor(Color("Secondary"))
            Text("\(Location(latitude: event.latitude, longitude: event.longitude).distanceFromUserLocationText()) from you")
                .fontWeight(.medium)
                .font(.custom("CircularStd-Medium", size: 12))
                .foregroundColor(Color.black)
                .padding(.leading, 14)
            Spacer()
        }.padding(.vertical, 16)
        Divider()
    }
}
 
