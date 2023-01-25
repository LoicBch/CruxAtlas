//
//  MainMapScreen.swift
//  iosApp
//
//  Created by USER on 02/11/2022.
//  Copyright © 2022 orgName. All rights reserved.
//

import SwiftUI
import shared
import MapKit

struct MainMapScreen: View {
    
    @ObservedObject var bottomSheetViewModel: BottomSheetViewModel
    @StateObject private var viewModel = MainMapViewModel()
    
    public var body: some View {
        
        ZStack(alignment: .top){
            
            if(viewModel.verticalListIsShowing){
                VerticalSpotsList(spots : viewModel.spots)
            }
            
            Map(coordinateRegion: .constant(viewModel.mapRegion), annotationItems: viewModel.markers){ marker in
                MapAnnotation(coordinate: marker.coordinate) {
                    MarkerView(title: marker.name).onTapGesture {
                        
                    }
                }
            }
            
            VStack(){
                TopButtons(showVerticalList: { viewModel.permuteVerticalList() }, ShowSheetView: { bottomSheetViewModel.openSheet()})
                Spacer()
                HorizontalSpotsList(spots: viewModel.spots)
                if (!viewModel.ads.isEmpty){
                    MainMapAdContainer()
                }
            }
        }
    }
}

struct HorizontalSpotsList: View {
    var spots: [Spot]
    
    @State private var scrollViewContentSize: CGSize = .zero
    @GestureState private var dragOffset: CGFloat = 0
    @State private var selectedIndex: Int = 0
    
    var body: some View {
        ScrollView(.horizontal){
            LazyHStack(alignment: .center, spacing: 20){
                ForEach(spots , id: \.self) { spot in
                        HorizontalListItem(spot: spot)
                        .frame(width: scrollViewContentSize.width, height: 130)
                        .background(RoundedRectangle(cornerRadius: 25)
                        .fill(Color.white))
                }
            }.frame(minWidth: 0, maxWidth: .infinity, minHeight: 0, maxHeight: .infinity,alignment: .leading).border(Color.red)
        }.gesture(
            DragGesture().updating($dragOffset){value, state, _ in
                state = value.translation.width
            }.onEnded{ value in
                let offset = value.translation.width + self.dragOffset
                if offset > 0 {
                    // scroll to the right
                    self.selectedIndex += 1
                } else {
                    // scroll to the left
                    self.selectedIndex -= 1
                }
            }
        ).background(
            GeometryReader { geo -> Color in
                DispatchQueue.main.async {
                    scrollViewContentSize = geo.size
                }
                return Color.clear
            }
        ).frame(width: .infinity, height: 130)
    }
}


struct HorizontalListItem: View {
    
    var spot: Spot

    var body: some View{
        
        HStack(){
            if (spot.isPremium && !spot.photos.isEmpty) {
                ZStack(){
                    Image("").frame(width: 130, height: 130)
                    Image("")
                }
            }
    
            VStack(){
                Text("test")
                Text("test")
                Spacer()
                HStack(){
                    if (!spot.services.isEmpty){
                        Image("")
                    }
    
                    if (!spot.services.isEmpty){
                        Image("")
                    }
    
                    if (!spot.services.isEmpty){
                        Image("")
                    }
    
                    Spacer()
    
                    if (spot.photos.isEmpty) {
                        HStack(){
                            Image("")
                            Text("test")
                        }
                    }
    
                    if (spot.isPremium && !spot.photos.isEmpty) {
                        HStack(){
                            Image("")
                            Text("test")
                        }
                    }
    
                }
            }
        }
    }
}

struct MainMapAdContainer: View{
    var body: some View{
        Image("")
            .frame(height: 80)
    }
}

struct TopButtons: View {
    
    var showVerticalList: () -> Void
    var ShowSheetView: () -> Void
    
    var body: some View{
        HStack(alignment: .top){
            Button(action: {
                showVerticalList()
            }){
                Image("reload")
            }
            .frame(width: 50, height: 50)
            .background(Color(red: 136, green: 175, blue: 255))
            .cornerRadius(25)
            .buttonStyle(.plain)
            
            Spacer()
            
            Button(action: {
                
            }){
                Image("reload")
            }
            .frame(width: 50, height: 50)
            .background(Color(red: 136, green: 175, blue: 255))
            .cornerRadius(25)
            .buttonStyle(.plain)
            
            
            Button(action: {
                ShowSheetView()
            }){
                Image("reload")
            }
            .frame(width: 50, height: 50)
            .background(Color(red: 136, green: 175, blue: 255))
            .cornerRadius(25)
            .buttonStyle(.plain)
        }
        .padding(EdgeInsets(top: 8, leading: 16, bottom: 0, trailing: 16))
    }
}


struct LocationSearchContainer: View {
    
    var label: String
    
    var body: some View{
        HStack(alignment: .center, spacing: 0) {
            Image(systemName: "pin.fill")
                .foregroundColor(Color.primary)
                .frame(width: 44, height: 44)
                .padding(.leading, 14)
                .background(Color.white)
            
            Text(label)
                .font(.system(size: 14, weight: .bold))
                .foregroundColor(Color.black)
                .padding(.leading, 22)
            
            Spacer().frame(width: 1, height: 44)
            
            Image(systemName: "xmark.circle.fill")
                .frame(width: 44, height: 44)
                .padding(.trailing, 15)
                .background(Color.white)
        }
        .frame(height: 44)
        .padding(.horizontal, 16)
        .padding(.bottom, 15)    }
}

struct SearchHereButton: View {
    
    var body: some View{
        HStack(alignment: .center, spacing: 0) {
            Spacer().frame(width: 1, height: 44)
            
            Button(action: {
            }) {
                Text("Search this area")
                    .font(.system(size: 14, weight: .bold))
                    .foregroundColor(Color.white)
            }
            .frame(height: 40)
            .padding(.vertical, 15)
            .background(Color.secondary)
            
            Spacer().frame(width: 1, height: 44)
        }
        .background(Color.white)
        
    }
}


struct VerticalSpotsList: View {
    
    var spots: [Spot]
    @State private var scrollViewContentSize: CGSize = .zero
    
    var body: some View{
        ScrollView(){
            LazyVStack{
                ForEach(spots , id: \.self) { spot in
                    VerticalListItem(spot: spot)
                        .frame(width: scrollViewContentSize.width, height: 130)
                        .background(RoundedRectangle(cornerRadius: 25)
                            .fill(Color.white))
                }
            }
        }.background(
            GeometryReader { geo -> Color in
                DispatchQueue.main.async {
                    scrollViewContentSize = geo.size
                }
                return Color.clear
            }
        ).frame(width: .infinity, height: 130)
    }
    
    
    
    struct VerticalListItem: View {
        
        var spot: Spot
        
        var body: some View{
            
            if (spot.isPremium){
                ZStack{
                    Image(systemName: "checkmark.circle.fill")
                        .foregroundColor(.blue)
                        .padding(.trailing, 5)
                        .padding(.bottom, 5)
                        .shadow(radius: 2)
                        .background(Color.white)
                        .clipShape(RoundedRectangle(cornerRadius: 15))
                        .padding(5)
                        .zIndex(1)
                }
            }
            
            VStack{
                Spacer()
                HStack{
                    if (spot.isPremium && !spot.photos.isEmpty) {
                        
                    }
                    
                    if (!spot.services.isEmpty) {
                        
                    }
                    
                    if (!spot.brands.isEmpty) {
                        
                    }
                    
                    if (!spot.brands.isEmpty) {
                        
                    }
                    
                    if (!spot.photos.isEmpty) {
                        
                    }
                    
                }
                
            }.padding(8)
        }
    }
    
    struct MainMapScreen_Previews: PreviewProvider {
        static var previews: some View {
            EmptyView()
        }
    }
}
