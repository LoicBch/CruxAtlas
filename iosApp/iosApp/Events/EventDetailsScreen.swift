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
                    
//                    let headerSize = getHeaderSize(event: event)
                    
//                    if (!event.photos.isEmpty){
//                        ImageCarouselView(images: event.photos)
//                    }
                    
                    VStack{
                        TopDealerButtons(onClose: { self.presentationMode.wrappedValue.dismiss() })
                    }.padding(EdgeInsets(top: 12, leading: 17, bottom: 0, trailing: 17))
                }
                
                HStack{
                    Text(event.name)
                        .fontWeight(.black)
                        .font(.system(size: 22))
                        .foregroundColor(Color.black)
                        .padding(.top, 24)
                        .frame(alignment: .leading)
                    Spacer()
                }
                
                
                HStack{
                    Text(event.descriptionEn)
                        .fontWeight(.medium)
                        .font(.system(size: 14))
                        .foregroundColor(Color("Tertiary"))
                        .padding(.top, 12)
                        .frame(alignment: .leading)
                    Spacer()
                }
                
                Divider()
                
                InfosList(event: event)
                 
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

//func getHeaderSize(event: Event) -> CGFloat{
//    if (!event.photos.isEmpty){
//        return 100
//    }else{
//        return 50
//    }
//}

struct InfosList: View {
    
    var event: Event
    
    var body: some View {
        HStack{
            Image("events")
            Text("\(event.dateBegin) - \(event.dateEnd)")
                .fontWeight(.medium)
                .font(.system(size: 12))
                .foregroundColor(Color.black)
                .padding(.leading, 14)
            Spacer()
        }.padding(.top, 24)
        Divider()
        
        HStack{
            Image("website")
            Text(event.website)
                .fontWeight(.medium)
                .font(.system(size: 12))
                .foregroundColor(Color.black)
                .padding(.leading, 14)
            Spacer()
        }.padding(.top, 24)
        Divider()
        
        HStack{
            Image("pin_here")
            Text(event.fullLocation)
                .fontWeight(.medium)
                .font(.system(size: 12))
                .foregroundColor(Color.black)
                .padding(.leading, 14)
            Spacer()
        }.padding(.top, 24)
        Divider()
        
        HStack{
            Image("my_location")
            Text(event.fullGeolocation())
                .fontWeight(.medium)
                .font(.system(size: 12))
                .foregroundColor(Color.black)
                .padding(.leading, 14)
            Spacer()
        }.padding(.top, 24)
        Divider()
        
        HStack{
            Image("distance")
            Text("\(Location(latitude: event.latitude, longitude: event.longitude).distanceFromUserLocationText()) from you")
                .fontWeight(.medium)
                .font(.system(size: 12))
                .foregroundColor(Color.black)
                .padding(.leading, 14)
            Spacer()
        }.padding(.top, 24)
        Divider()
    }
}
 
