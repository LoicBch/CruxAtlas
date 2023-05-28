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
import Combine
import Introspect

struct MainMapScreen: View {
    
    @ObservedObject var bottomSheetViewModel: BottomSheetViewModel
    @StateObject private var viewModel = MainMapViewModel()
    
    @State var mapIsMoving: Bool = false
    @State var dragFromCode: Bool = false
    
    @Environment(\.onLoading) var onLoading
    @EnvironmentObject var appState: AppState
    
    var scrollFromDevice = false
    
    public var body: some View {
        ZStack(alignment: .top){
            // MARK: - MAP Z-INDEX 0
            MainMap(
                region: $viewModel.mapRegion,
                mapType: $viewModel.mapType,
                markers: $viewModel.markers,
                selectedMarkerId: $viewModel.currentPlaceSelectedId,
                mapIsMoving: $mapIsMoving,
                onMapStopMoving: { location in
                    withAnimation {
                        print("STOPED")
                        mapIsMoving = false
                    }
                    viewModel.onMapStopMoving(location: Location(latitude: location.latitude, longitude: location.longitude))
                }, onMapStartMoving: {
                    withAnimation {
                        print("STARTMOVING")
                        mapIsMoving = true
                    }
                }
            ){ marker in
                if (marker.selected){
                    viewModel.placeIdToScroll = marker.idPlaceLinked
                }else{
                    viewModel.selectMarker(placeLinkedId: marker.idPlaceLinked, zoom: viewModel.mapRegion.span)
                    viewModel.placeIdToScroll = marker.idPlaceLinked
                }
            }.onReceive(appState.$aroundMeClicked){ isClicked in
                if isClicked {
                    viewModel.showDealers(location: Globals.geoLoc().lastKnownLocation)
                    appState.aroundMeClicked = false
                }
            }.onReceive(appState.$placeSelected){ placeSelected in
                if (placeSelected.name != ""){
                    viewModel.showSpotsAroundPlace(place: placeSelected)
                }
            }.onReceive(appState.$isEventsMapOpening){ isOpening in
                if (isOpening){
                    viewModel.showEvents()
                }
            }.onReceive(appState.$filterApplied){ filter in
                if (filter.isSelected){
                    if (filter.category == FilterType.countries){
                        viewModel.showEvents()
                    }else{
                        viewModel.showDealers(location: Location(latitude: viewModel.mapRegion.center.latitude, longitude: viewModel.mapRegion.center.longitude))
                    }
                }
            }.onReceive(appState.$verticalSortingApplied){ sortingOption in
                viewModel.onSortingOptionSelected(sortingOption: sortingOption.getSortDomain())
            }
            // MARK: - BOTTOM CONTROLLER Z-INDEX 1
                VStack(){
                    Spacer()
                    
                    if (viewModel.searchHereButtonIsShowing){
                        SearchHereBox().padding(.bottom, 15).onTapGesture {
                            viewModel.showDealers(location: Location(latitude: viewModel.mapRegion.center.latitude, longitude: viewModel.mapRegion.center.longitude))
                        }.opacity(mapIsMoving && viewModel.searchHereButtonIsShowing && !viewModel.mapMovedByCode ? 0 : 1.0)
                    }
                    
                    if(viewModel.updateSource == UpdateSource.events){
                        HorizontalEventList(events: $viewModel.events, placeIdToScroll: $viewModel.placeIdToScroll, onScrollStopped: { placeId in
                            withAnimation {
                                viewModel.selectMarker(placeLinkedId: placeId, zoom: viewModel.mapRegion.span)
                            }
                        })
                        
                        .opacity(mapIsMoving && !viewModel.mapMovedByCode ? 0 : 1.0)
                    } else {
                        HorizontalDealersList(
                            dealers: $viewModel.dealers,
                            placeIdToScroll: $viewModel.placeIdToScroll,
                            onScrollStoped: { placeId in
                                withAnimation {
                                    viewModel.selectMarker(placeLinkedId: placeId, zoom: viewModel.mapRegion.span)
                                }
                            })
                        .opacity(mapIsMoving && !viewModel.mapMovedByCode ? 0 : 1.0)
                    }
                    
                    if (!viewModel.ads.isEmpty){
                        MainMapAdContainer(ad: viewModel.ads.first!)
                            .opacity(mapIsMoving && !viewModel.mapMovedByCode ? 0 : 1.0)
                    }
                }
            
            // MARK: - VERTICAL LIST OVERLAY Z-INDEX 2
            
            if(viewModel.verticalListIsShowing){
                    if (viewModel.updateSource == UpdateSource.events){
                        VerticalEventsList(events: viewModel.events)
                    }else{
                        VerticaldealersList(dealers: viewModel.dealers)
                    }
            }
            
            // MARK: - TOP CONTROLLER Z-INDEX 3
            
            TopButtons(bottomSheetViewModel: bottomSheetViewModel, showVerticalList: { viewModel.permuteVerticalList() }, onMaptypeChange: { viewModel.switchMapStyle() }, isVerticalListopen: viewModel.verticalListIsShowing, currentSource: viewModel.updateSource, filterIsActive: appState.filterApplied.filterId != "")
                .opacity(mapIsMoving && !viewModel.mapMovedByCode ? 0 : 1.0)
            
            }.onChange(of: viewModel.isLoading){
            onLoading($0)
        }
    }
}

struct SearchHereBox: View{
    public var body: some View{
        HStack{
            Text("search_this_area")
                .padding(.horizontal, 20)
                .padding(.vertical, 10)
                .foregroundColor(Color.white)
        }
        .background(RoundedRectangle(cornerRadius: 25)
            .fill(Color("Secondary")).shadow(color: Color.black.opacity(0.2), radius: 2, x: 0, y: 2))
    }
}

struct VerticaldealersList: View {
    
    var dealers: [Dealer]
    
    var body: some View{
        ScrollView(){
            LazyVStack{
                ForEach(dealers , id: \.self) { dealer in
                    NavigationLink(destination: DealerDetailsScreen(dealer: dealer)
                        .navigationBarBackButtonHidden(true)
                    ) {
                        VerticalDealerItem(dealer: dealer)
                            .frame(width: .infinity, height: 130)
                            .padding(8)
                            .background(RoundedRectangle(cornerRadius: 8)
                                .fill(Color.white).shadow(color: Color.black.opacity(0.2), radius: 2, x: 0, y: 2))
                    }
                }
            }.padding(.top, 100).padding(.horizontal, 8)
        }.background(
            GeometryReader { geo -> Color in
                return Color.white
            }
        )
        .frame(width: .infinity, height: .infinity)
    }
}


struct VerticalDealerItem: View {
    
    var dealer: Dealer
    
    var body: some View{
        
        HStack(){
            if (dealer.isPremium && !dealer.photos.isEmpty) {
                ZStack(){
                    UrlImageList(url: dealer.photos[0].url)
                }
            }
            
            VStack(){
                Text(dealer.name)
                    .fontWeight(.bold)
                    .frame(maxWidth: .infinity, alignment: .leading)
                    .font(.system(size: 14))
                    .foregroundColor(.black)
                    .padding(.top, 8)
                    .padding(.leading, 8)
                
                Text(dealer.fullLocation)
                    .fontWeight(.bold)
                    .font(.system(size: 12))
                    .foregroundColor(Color("Tertiary"))
                    .frame(maxWidth: .infinity, alignment: .leading)
                    .padding(.top, 5)
                    .padding(.leading, 8)
                
                Spacer()
                HStack(){
                    if (!dealer.services.isEmpty){
                        Image("repair")
                            .frame(width: 16, height: 16)
                            .padding(5)
                            .background(Color.white)
                            .cornerRadius(5)
                            .shadow(color: Color.black.opacity(0.2), radius: 2, x: 0, y: 2)
                    }
                    
                    if (!dealer.brands.isEmpty){
                        Image("dealers")
                            .frame(width: 16, height: 16)
                            .padding(5)
                            .background(Color.white)
                            .cornerRadius(5)
                            .shadow(color: Color.black.opacity(0.2), radius: 2, x: 0, y: 2)
                    }
                    
                    Spacer()
                    
                    if (dealer.photos.isEmpty) {
                        HStack(){
                            Image("distance")
                                .frame(height: 30)
                                .padding(.leading, 5)
                            
                            Text(Location(latitude: dealer.latitude, longitude: dealer.longitude).distanceFromUserLocationText())
                                .fontWeight(.light)
                                .font(.system(size: 12))
                                .foregroundColor(Color("Primary"))
                                .padding(.trailing, 5)
                        }.background(Color.white)
                            .cornerRadius(5)
                            .shadow(color: Color.black.opacity(0.2), radius: 2, x: 0, y: 2)
                    }
                    
                    if (dealer.isPremium && !dealer.photos.isEmpty) {
                        HStack(){
                            Image("distance")
                                .frame(height: 30)
                                .padding(.leading, 5)
                            
                            Text(Location(latitude: dealer.latitude, longitude: dealer.longitude).distanceFromUserLocationText())
                                .fontWeight(.light)
                                .font(.system(size: 12))
                                .foregroundColor(Color("Primary"))
                                .padding(.trailing, 5)
                        }.background(Color.white)
                            .cornerRadius(15)
                            .shadow(color: Color.black.opacity(0.2), radius: 2, x: 0, y: 2)
                    }
                }.padding(8)
            }
        }
    }
}


extension View {
    func onFrameChange(_ frameHandler: @escaping (CGRect)->(),
                       enabled isEnabled: Bool = true) -> some View {
        guard isEnabled else { return AnyView(self) }
        return AnyView(self.background(GeometryReader { (geometry: GeometryProxy) in
            Color.clear.beforeReturn {
                frameHandler(geometry.frame(in: .global))
            }
        }))
    }
    
    private func beforeReturn(_ onBeforeReturn: ()->()) -> Self {
        onBeforeReturn()
        return self
    }
}

struct HorizontalDealersList: View {
    @Binding  var dealers: [Dealer]
    @Binding  var placeIdToScroll: String
    var onScrollStoped: (String) -> Void
    
    var body: some View {
        MapDealersScrollView(dealers: $dealers, itemIdToScrollTo: $placeIdToScroll){
            LazyHStack(spacing: 10){
                ForEach(dealers , id: \.self.id) { dealer in
                    NavigationLink(destination: DealerDetailsScreen(dealer: dealer)
                        .navigationBarBackButtonHidden(true)
                    ) {
                        HorizontalListDealerItem(dealer: dealer)
                            .background(RoundedRectangle(cornerRadius: 5)
                                .fill(Color.white))
                            .frame(width: UIScreen.main.bounds.width * 0.85)
                            .padding(.leading, dealers.first == dealer ? 10 : 0)
                    }
                }
            }
        } onDraggingStoped: { itemId in
            onScrollStoped(itemId)
        } onDecelerating: { itemId in
            onScrollStoped(itemId)
        }
        .frame(width: .infinity, height: 130)
    }
}

struct ViewOffsetKey: PreferenceKey {
    typealias Value = CGFloat
    static var defaultValue = CGFloat.zero
    static func reduce(value: inout Value, nextValue: () -> Value) {
        value += nextValue()
    }
}

struct HorizontalListDealerItem: View {
    
    var dealer: Dealer
    
    var body: some View{
        
        HStack(){
            if (dealer.isPremium && !dealer.photos.isEmpty) {
                UrlImageList(url: dealer.photos[0].url)
            }
            
            VStack(){
                Text(dealer.name)
                    .fontWeight(.bold)
                    .frame(maxWidth: .infinity, alignment: .leading)
                    .font(.system(size: 14))
                    .foregroundColor(.black)
                    .padding(.top, 8)
                    .padding(.leading, 8)
                
                Text(dealer.fullLocation)
                    .fontWeight(.bold)
                    .font(.system(size: 12))
                    .foregroundColor(Color("Tertiary"))
                    .frame(maxWidth: .infinity, alignment: .leading)
                    .padding(.top, 5)
                    .padding(.leading, 8)
                
                Spacer()
                HStack(){
                    if (!dealer.services.isEmpty){
                        Image("repair")
                            .frame(width: 16, height: 16)
                            .padding(5)
                            .background(Color.white)
                            .cornerRadius(5)
                            .shadow(color: Color.black.opacity(0.2), radius: 2, x: 0, y: 2)
                    }
                    
                    if (!dealer.brands.isEmpty){
                        Image("dealers")
                            .frame(width: 16, height: 16)
                            .padding(5)
                            .background(Color.white)
                            .cornerRadius(5)
                            .shadow(color: Color.black.opacity(0.2), radius: 2, x: 0, y: 2)
                    }
                    
                    Spacer()
                    
                    if (dealer.photos.isEmpty) {
                        HStack(){
                            Image("distance")
                                .frame(height: 30)
                                .padding(.leading, 5)
                            
                            Text(Location(latitude: dealer.latitude, longitude: dealer.longitude).distanceFromUserLocationText())
                                .fontWeight(.bold)
                                .font(.system(size: 12))
                                .foregroundColor(Color("Primary"))
                                .padding(.trailing, 5)
                        }.background(Color.white)
                            .cornerRadius(5)
                            .shadow(color: Color.black.opacity(0.2), radius: 2, x: 0, y: 2)
                    }
                    
                    if (dealer.isPremium && !dealer.photos.isEmpty) {
                        HStack(){
                            Image("distance")
                                .frame(height: 30)
                                .padding(.leading, 5)
                            
                            Text(Location(latitude: dealer.latitude, longitude: dealer.longitude).distanceFromUserLocationText())
                                .fontWeight(.light)
                                .font(.system(size: 12))
                                .foregroundColor(Color("Primary"))
                                .padding(.trailing, 5)
                        }.background(Color.white)
                            .cornerRadius(15)
                            .shadow(color: Color.black.opacity(0.2), radius: 2, x: 0, y: 2)
                    }
                }.padding(8)
            }
        }
    }
}

struct VerticalEventsList: View {
    var events: [Event]
    var body: some View{
        ScrollView(){
            LazyVStack{
                ForEach(events , id: \.self) { event in
                    NavigationLink(destination: EventDetailsScreen(event: event)
                        .navigationBarBackButtonHidden(true)
                    ) {
                        VerticalEventItem(event: event)
                            .frame(width: .infinity, height: 130)
                            .padding(8)
                            .background(RoundedRectangle(cornerRadius: 8)
                                .fill(Color.white).shadow(color: Color.black.opacity(0.2), radius: 2, x: 0, y: 2))
                    }
                }
            }.padding(.top, 100).padding(.horizontal, 8)
        }.background(
            GeometryReader { geo -> Color in
                return Color.white
            }
        )
        .frame(width: .infinity, height: .infinity)
    }
}

struct VerticalEventItem: View {
    var event: Event
    
    var body: some View{
        
        HStack(){
            if (event.photos.isEmpty) {
                ZStack(){
                    UrlImageList(url: event.photos[0].url)
                }
            }
            
            VStack(){
                Text(event.name)
                    .fontWeight(.bold)
                    .frame(maxWidth: .infinity, alignment: .leading)
                    .font(.system(size: 14))
                    .foregroundColor(.black)
                    .padding(.top, 8)
                    .padding(.leading, 8)
                
                Text("\(event.dateBegin) - \(event.dateEnd)")
                    .fontWeight(.bold)
                    .font(.system(size: 12))
                    .foregroundColor(Color("Tertiary"))
                    .frame(maxWidth: .infinity, alignment: .leading)
                    .padding(.top, 5)
                    .padding(.leading, 8)
                
                Spacer()
                HStack(){
                    HStack(){
                        Text(event.country)
                            .fontWeight(.bold)
                            .font(.system(size: 12))
                            .foregroundColor(Color("Primary"))
                            .padding(5)
                    }.background(Color.white)
                        .cornerRadius(5)
                        .shadow(color: Color.black.opacity(0.2), radius: 2, x: 0, y: 2)
                    
                    Spacer()
                    
                    HStack(){
                        Image("distance")
                            .frame(height: 30)
                            .padding(.leading, 5)
                        
                        Text(Location(latitude: event.latitude, longitude: event.longitude).distanceFromUserLocationText())
                            .fontWeight(.bold)
                            .font(.system(size: 12))
                            .foregroundColor(Color("Primary"))
                            .padding(5)
                    }.background(Color.white)
                        .cornerRadius(5)
                        .shadow(color: Color.black.opacity(0.2), radius: 2, x: 0, y: 2)
                }.padding(.leading, 8)
            }.padding(8)
        }
    }
}

struct HorizontalEventList: View {
    @Binding var events: [Event]
    @Binding var placeIdToScroll: String
    var onScrollStopped: (String) -> Void
    
    var body: some View {
        MapEventsScrollView(events: $events, itemIdToScrollTo: $placeIdToScroll){
            LazyHStack(spacing: 10){
                ForEach(events , id: \.self.id) { event in
                    NavigationLink(destination: EventDetailsScreen(event: event)
                        .navigationBarBackButtonHidden(true)
                    ) {
                        HorizontalEventItem(event: event)
                            .background(RoundedRectangle(cornerRadius: 5)
                                .fill(Color.white))
                            .frame(width: UIScreen.main.bounds.width * 0.85)
                            .padding(.leading, events.first == event ? 10 : 0)
                    }
                }
            }
        } onDraggingStoped: { itemId in
            onScrollStopped(itemId)
        } onDecelerating: { itemId in
            onScrollStopped(itemId)
        }
        .frame(width: .infinity, height: 130)
    }
}

struct HorizontalEventItem: View {
    
    var event: Event
    
    var body: some View{
        HStack(){
            if (event.photos.isEmpty) {
                ZStack(){
                    UrlImageList(url: event.photos[0].url)
                }
            }
            
            VStack(){
                Text(event.name)
                    .fontWeight(.bold)
                    .frame(maxWidth: .infinity, alignment: .leading)
                    .font(.system(size: 14))
                    .foregroundColor(.black)
                    .padding(.top, 8)
                    .padding(.leading, 8)
                
                Text("\(event.dateBegin) - \(event.dateEnd)")
                    .fontWeight(.bold)
                    .font(.system(size: 12))
                    .foregroundColor(Color("Tertiary"))
                    .frame(maxWidth: .infinity, alignment: .leading)
                    .padding(.top, 5)
                    .padding(.leading, 8)
                
                Spacer()
                HStack(){
                    
                    HStack(){
                        Text(event.country)
                            .fontWeight(.bold)
                            .font(.system(size: 12))
                            .foregroundColor(Color("Primary"))
                            .padding(5)
                    }.background(Color.white)
                        .cornerRadius(5)
                        .shadow(color: Color.black.opacity(0.2), radius: 2, x: 0, y: 2)
                    
                    Spacer()
                    
                    HStack(){
                        Image("distance")
                            .frame(height: 30)
                            .padding(.leading, 5)
                        
                        Text(Location(latitude: event.latitude, longitude: event.longitude).distanceFromUserLocationText())
                            .fontWeight(.light)
                            .font(.system(size: 12))
                            .foregroundColor(Color("Primary"))
                            .padding(.trailing, 5)
                    }.background(Color.white)
                        .cornerRadius(5)
                        .shadow(color: Color.black.opacity(0.2), radius: 2, x: 0, y: 2)
                }
            }.padding(8)
        }
    }
}


struct MainMapAdContainer: View{
    
    let ad : Ad
    
    var body: some View{
        UrlImagePub(url: ad.url)
            .onTapGesture {
                guard let url = URL(string: ad.url) else { return }
                guard let urlStat = URL(string: ad.click) else { return }
                urlStat.ping(completion: {_, _ in})
                UIApplication.shared.open(url)
            }
    }
}

struct TopButtons: View {
    
    @ObservedObject var bottomSheetViewModel: BottomSheetViewModel
    @Environment(\.withNetworkOnly) var withNetworkOnly
    
    var showVerticalList: () -> Void
    var onMaptypeChange: () -> Void
    var isVerticalListopen: Bool
    var currentSource: UpdateSource
    var filterIsActive: Bool
    
    var body: some View {
        HStack(alignment: .top){
            Button(action: {
                showVerticalList()
            }){
                if(isVerticalListopen){
                    Image("map")
                }else{
                    Image("list")
                }
            }
            .buttonStyle(FilterButtonStyle(filterActive: false))
            
            Spacer()
            
            Button(action: {
                
                if(isVerticalListopen){
                    withNetworkOnly {
                        if(currentSource == UpdateSource.events){
                            bottomSheetViewModel.openSheet(option: BottomSheetOption.SORT_EVENT)
                        }else if (currentSource == UpdateSource.aroundPlace){
                            bottomSheetViewModel.openSheet(option: BottomSheetOption.SORT_AROUND_PLACE)
                        }else{
                            bottomSheetViewModel.openSheet(option: BottomSheetOption.SORT)
                        }
                    }
                }else{
                    onMaptypeChange()
                }
            }){
                if(isVerticalListopen){
                    Image("sort").resizable().frame(width: 24, height: 24)
                }else{
                    Image("map_layer")
                }
            }
            .buttonStyle(FilterButtonStyle(filterActive: false))
            
            
            Button(action: {
                withNetworkOnly {
                    if(currentSource == UpdateSource.events){
                        bottomSheetViewModel.openSheet(option: BottomSheetOption.FILTER_EVENT)
                    }else {
                        bottomSheetViewModel.openSheet(option: BottomSheetOption.FILTER)
                    }
                }
            }){
                Image("filter")
            }
            .buttonStyle(FilterButtonStyle(filterActive: !Globals.filters().brands.isEmpty || !Globals.filters().services.isEmpty))
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

struct MainMapScreen_Previews: PreviewProvider {
    static var previews: some View {
        EmptyView()
    }
}

enum ScrollOrigin {
    case user
    case programmatic
}

struct OffsetPreferenceKey: PreferenceKey {
    static var defaultValue: CGPoint = .zero
    
    static func reduce(value: inout CGPoint, nextValue: () -> CGPoint) {
        value = nextValue()
    }
}
