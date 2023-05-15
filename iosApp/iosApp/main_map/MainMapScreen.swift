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

struct MainMapScreen: View {
    
    @ObservedObject var bottomSheetViewModel: BottomSheetViewModel
    @StateObject private var viewModel = MainMapViewModel()
    @Binding var placeSelected : Place
    @Binding var isEventsMapOpening : Bool
    @Binding var isAroundMeClicked : Bool
    @Binding var activeFilter : Filter
    @Binding var verticalSorting: SortingOption
    
    @State private var tabBar: UITabBar! = nil
    
    public var body: some View {
        NavigationView {
            ZStack(alignment: .top){
                MainMap(
                    region: $viewModel.mapRegion,
                    mapType: $viewModel.mapType,
                    markers: $viewModel.markers,
                    selectedMarkerId: $viewModel.currentPlaceSelectedId,
                    onMapStopMoving: { location in
                        viewModel.onMapStopMoving(location: Location(latitude: location.latitude, longitude: location.longitude))
                    }
                ){ marker in
                    if (marker.selected){
                        viewModel.placeIdToScroll = marker.idPlaceLinked
                    }else{
                        viewModel.selectMarker(placeLinkedId: marker.idPlaceLinked, zoom: viewModel.mapRegion.span)
                        viewModel.placeIdToScroll = marker.idPlaceLinked
                    }
                }
                .onChange(of: placeSelected){ selectedPlace in
                    if (selectedPlace.name != ""){
                        viewModel.showSpotsAroundPlace(place: selectedPlace)
                    }
                }.onChange(of: activeFilter){ filter in
                    if (filter.category == FilterType.countries){
                        viewModel.showEvents()
                    }else{
                        viewModel.showDealers(location: Location(latitude: viewModel.mapRegion.center.latitude, longitude: viewModel.mapRegion.center.longitude))
                    }
                }.onChange(of: verticalSorting){ sortingOption in
                    viewModel.onSortingOptionSelected(sortingOption: sortingOption.getSortDomain())
                }.onChange(of: isEventsMapOpening){ isLaunched in
                    if (isLaunched){
                        viewModel.showEvents()
                    }
                }.onChange(of: isAroundMeClicked){ isClicked in
                    if isClicked {
                        viewModel.showDealers(location: Globals.geoLoc().lastKnownLocation)
                        isAroundMeClicked = false
                    }
                }
                
                if(viewModel.verticalListIsShowing){
                    ZStack(alignment: .top){
                        if (viewModel.updateSource == UpdateSource.events){
                            VerticalEventsList(events: viewModel.events, bottomBar: self.tabBar)
                        }else{
                            VerticaldealersList(dealers: viewModel.dealers, bottomBar: self.tabBar)
                        }
                        TopButtons(bottomSheetViewModel: bottomSheetViewModel, showVerticalList: { viewModel.permuteVerticalList() }, onMaptypeChange: { viewModel.switchMapStyle() }, isVerticalListopen: true, currentSource: viewModel.updateSource)
                    }
                }else{
                    VStack(){
                        TopButtons(bottomSheetViewModel: bottomSheetViewModel, showVerticalList: { viewModel.permuteVerticalList() }, onMaptypeChange: { viewModel.switchMapStyle() }, isVerticalListopen: false, currentSource: viewModel.updateSource)
                        Spacer()
                        
                        if (viewModel.searchHereButtonIsShowing){
                            SearchHereBox().padding(.bottom, 15).onTapGesture {
                                viewModel.showDealers(location: Location(latitude: viewModel.mapRegion.center.latitude, longitude: viewModel.mapRegion.center.longitude))
                            }
                        }
                        if (tabBar != nil){
                            if(viewModel.updateSource == UpdateSource.events){
                                HorizontalEventList(events: $viewModel.events, placeIdToScroll: $viewModel.placeIdToScroll, onScrollStopped: { placeId in
                                    withAnimation {
                                        viewModel.selectMarker(placeLinkedId: placeId, zoom: viewModel.mapRegion.span)
                                    }
                                }, bottomBar: self.tabBar)
                            }else {
                                HorizontalDealersList(
                                    dealers: $viewModel.dealers,
                                    placeIdToScroll: $viewModel.placeIdToScroll,
                                    onScrollStopped: { placeId in
                                        withAnimation {
                                            viewModel.selectMarker(placeLinkedId: placeId, zoom: viewModel.mapRegion.span)
                                        }
                                    },
                                    bottomBar: self.tabBar)
                            }
                        }
                        
                        if (!viewModel.ads.isEmpty){
                            MainMapAdContainer(ad: viewModel.ads.first!)
                        }
                    }
                }
            }
            .background(TabBarAccessor { tabbar in
                self.tabBar = tabbar
            })
        }
    }
    
    struct SearchHereBox: View{
        public var body: some View{
            HStack{
                LocalizedText(key: "search_this_area")
                    .padding(.horizontal, 20)
                    .padding(.vertical, 10)
                    .foregroundColor(Color.white)
            }
            .background(RoundedRectangle(cornerRadius: 25)
            .fill(Color("Secondary")).shadow(radius: 2, x:-1, y: 2))
        }
    }
    
    struct VerticaldealersList: View {
        
        var dealers: [Dealer]
        var bottomBar : UITabBar
        
        var body: some View{
            ScrollView(){
                LazyVStack{
                    ForEach(dealers , id: \.self) { dealer in
                        NavigationLink(destination: DealerDetailsScreen(dealer: dealer)
                            .navigationBarBackButtonHidden(true)
                            .disableBottomBar(bottomBar: bottomBar)
                        ) {
                            VerticalDealerItem(dealer: dealer)
                                .frame(width: .infinity, height: 130)
                                .padding(8)
                                .background(RoundedRectangle(cornerRadius: 5)
                                    .fill(Color.white).shadow(radius: 2, x:-1, y: 2))
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
                        UrlImage(url: dealer.photos[0].url).frame(width: 130, height: 130)
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
                                .shadow(radius: 5, x:-1, y: 1)
                        }
                        
                        if (!dealer.brands.isEmpty){
                            Image("dealers")
                                .frame(width: 16, height: 16)
                                .padding(5)
                                .background(Color.white)
                                .cornerRadius(5)
                                .shadow(radius: 5, x:-1, y: 1)
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
                                .shadow(radius: 5, x:-1, y: 1)
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
                                .shadow(radius: 5, x:-1, y: 1)
                        }
                    }.padding(8)
                }
            }
        }
    }
    
    struct HorizontalDealersList: View {
        @Binding private var dealers: [Dealer]
        @Binding private var placeIdToScroll: String
        private var onScrollStopped: (String) -> Void
        private var bottomBar: UITabBar
        
        @State private var scrollViewContentSize: CGSize = .zero
        @State private var selectedIndex: Int = 0
        @State private var previousOffset: CGFloat = 0
        @State private var scrollDirection: ScrollDirection = .none
        @State private var scrollOrigin: ScrollOrigin = .user
        @State private var dealersCurrentlyShowing: String = ""
        
        
        
        let scrollEndedDetector: CurrentValueSubject<CGFloat, Never>
        let scrollEndedPublisher: AnyPublisher<CGFloat, Never>
        
        init(dealers: Binding<[Dealer]>, placeIdToScroll: Binding<String>, onScrollStopped: @escaping (String) -> Void, bottomBar: UITabBar) {
            self._dealers = dealers
            self._placeIdToScroll = placeIdToScroll
            self.onScrollStopped = onScrollStopped
            let scrollEndedDetector = CurrentValueSubject<CGFloat, Never>(0)
            self.scrollEndedPublisher = scrollEndedDetector
                .debounce(for: .seconds(0.2), scheduler: DispatchQueue.main)
                .dropFirst()
                .eraseToAnyPublisher()
            self.scrollEndedDetector = scrollEndedDetector
            self.bottomBar = bottomBar
        }
        
        var body: some View {
            ScrollViewReader { value in
                ScrollView(.horizontal){
                    LazyHStack(alignment: .center, spacing: 20){
                        ForEach(dealers , id: \.self.id) { dealer in
                            NavigationLink(destination: DealerDetailsScreen(dealer: dealer)
                                .navigationBarBackButtonHidden(true)
                                .disableBottomBar(bottomBar: bottomBar)
                            ) {
                                HorizontalListDealerItem(dealer: dealer)
                                    .background(RoundedRectangle(cornerRadius: 5)
                                        .fill(Color.white))
                                    .padding(.leading, 8)
                                    .frame(width: UIScreen.main.bounds.width * 0.85)
                                    .onAppear {
                                        //                                        print("\(dealer.name) just appeared")
                                        dealersCurrentlyShowing = dealer.id
                                    }
                                    .onDisappear(){
                                        //                                        print("\(dealer.name) just disapeared")
                                    }.background(GeometryReader {
                                        Color.clear.preference(key: ViewOffsetKey.self,
                                                               value: -$0.frame(in: .named("scroll")).origin.x)
                                    })
                                    .onPreferenceChange(ViewOffsetKey.self) { scrollEndedDetector.send($0) }
                            }
                        }
                    }
                }
                .onChange(of: placeIdToScroll, perform: { placeIdToScroll in
                    if(placeIdToScroll != ""){
                        scrollOrigin = .programmatic
                        value.scrollTo(placeIdToScroll, anchor: .leading)
                    }
                })
                .coordinateSpace(name: "scroll")
                .onReceive(scrollEndedPublisher) { offset in
                    if (scrollOrigin == .user){
                        withAnimation(){
                            scrollOrigin = .programmatic
                            value.scrollTo(dealersCurrentlyShowing, anchor: .leading)
                        }
                    }else{
                        scrollOrigin = .user
                    }
                    
                    // doesnt work as expected, we would had to calculate position of specific item on the width screen to determine if we should focus n-1 or n+1
                    //                    and have a array of dealersCurrentlyShowing to keep track
                    if (offset > 0){
                        scrollDirection = .right
                    }else{
                        scrollDirection = .left
                    }
                    
                    if (dealersCurrentlyShowing != ""){
                        print(offset)
                        onScrollStopped(dealersCurrentlyShowing)
                    }
                }
                .frame(width: .infinity, height: 130)
            }
        }
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
                ZStack(){
                    UrlImage(url: dealer.photos[0].url).frame(width: 130, height: 130)
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
                            .shadow(radius: 5, x:-1, y: 1)
                    }
                    
                    if (!dealer.brands.isEmpty){
                        Image("dealers")
                            .frame(width: 16, height: 16)
                            .padding(5)
                            .background(Color.white)
                            .cornerRadius(5)
                            .shadow(radius: 5, x:-1, y: 1)
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
                            .shadow(radius: 5, x:-1, y: 1)
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
                            .shadow(radius: 5, x:-1, y: 1)
                    }
                }.padding(8)
            }
        }
    }
}

struct VerticalEventsList: View {
    
    
    var events: [Event]
    var bottomBar : UITabBar
    
    var body: some View{
        ScrollView(){
            LazyVStack{
                ForEach(events , id: \.self) { event in
                    NavigationLink(destination: EventDetailsScreen(event: event)
                        .navigationBarBackButtonHidden(true)
                        .disableBottomBar(bottomBar: bottomBar)
                    ) {
                        VerticalEventItem(event: event)
                            .frame(width: .infinity, height: 130)
                            .padding(8)
                            .background(RoundedRectangle(cornerRadius: 5)
                                .fill(Color.white).shadow(radius: 2, x:-1, y: 2))
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
            //            if (event.photos.isEmpty) {
            //                ZStack(){
            //                    UrlImage(url: event.photos[0].url).frame(width: 130, height: 130)
            //                }
            //            }
            
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
                            .fontWeight(.light)
                            .font(.system(size: 12))
                            .foregroundColor(Color("Primary"))
                            .padding(.trailing, 5)
                    }.background(Color.white)
                        .cornerRadius(5)
                        .shadow(radius: 5, x:-1, y: 1)
                    
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
                        .shadow(radius: 5, x:-1, y: 1)
                }
            }.padding(8)
        }
    }
}

struct HorizontalEventList: View {
    @Binding var events: [Event]
    @Binding private var placeIdToScroll: String
    private var onScrollStopped: (String) -> Void
    private var bottomBar: UITabBar
    
    @State private var scrollViewContentSize: CGSize = .zero
    @State private var selectedIndex: Int = 0
    @State private var previousOffset: CGFloat = 0
    @State private var scrollDirection: ScrollDirection = .none
    @State private var scrollOrigin: ScrollOrigin = .user
    @State private var eventCurrentlyShowing: String = ""
    
    
    
    let scrollEndedDetector: CurrentValueSubject<CGFloat, Never>
    let scrollEndedPublisher: AnyPublisher<CGFloat, Never>
    
    init(events: Binding<[Event]>, placeIdToScroll: Binding<String>, onScrollStopped: @escaping (String) -> Void, bottomBar: UITabBar) {
        self._events = events
        self._placeIdToScroll = placeIdToScroll
        self.onScrollStopped = onScrollStopped
        let scrollEndedDetector = CurrentValueSubject<CGFloat, Never>(0)
        self.scrollEndedPublisher = scrollEndedDetector
            .debounce(for: .seconds(0.2), scheduler: DispatchQueue.main)
            .dropFirst()
            .eraseToAnyPublisher()
        self.scrollEndedDetector = scrollEndedDetector
        self.bottomBar = bottomBar
    }
    
    var body: some View {
        ScrollViewReader { value in
            ScrollView(.horizontal){
                LazyHStack(alignment: .center, spacing: 20){
                    ForEach(events , id: \.self.id) { event in
                        NavigationLink(destination: EventDetailsScreen(event: event)
                            .navigationBarBackButtonHidden(true)
                            .disableBottomBar(bottomBar: bottomBar)
                        ) {
                            HorizontalEventItem(event: event)
                                .background(RoundedRectangle(cornerRadius: 5)
                                    .fill(Color.white))
                                .padding(.leading, 8)
                                .frame(width: UIScreen.main.bounds.width * 0.85)
                                .onAppear {
                                    eventCurrentlyShowing = event.id
                                }
                                .onDisappear(){
                                }.background(GeometryReader {
                                    Color.clear.preference(key: ViewOffsetKey.self,
                                                           value: -$0.frame(in: .named("scroll")).origin.x)
                                })
                                .onPreferenceChange(ViewOffsetKey.self) { scrollEndedDetector.send($0) }
                        }
                    }
                }
            }
            .onChange(of: placeIdToScroll, perform: { placeIdToScroll in
                if(placeIdToScroll != ""){
                    scrollOrigin = .programmatic
                    value.scrollTo(placeIdToScroll, anchor: .leading)
                }
            })
            .coordinateSpace(name: "scroll")
            .onReceive(scrollEndedPublisher) { offset in
                if (scrollOrigin == .user){
                    withAnimation(){
                        scrollOrigin = .programmatic
                        value.scrollTo(eventCurrentlyShowing, anchor: .leading)
                    }
                }else{
                    scrollOrigin = .user
                }
                
                // doesnt work as expected, we would had to calculate position of specific item on the width screen to determine if we should focus n-1 or n+1
                //                    and have a array of dealersCurrentlyShowing to keep track
                if (offset > 0){
                    scrollDirection = .right
                }else{
                    scrollDirection = .left
                }
                
                if (eventCurrentlyShowing != ""){
                    print(offset)
                    onScrollStopped(eventCurrentlyShowing)
                }
            }
            .frame(width: .infinity, height: 130)
        }
    }
    
}

struct HorizontalEventItem: View {
    
    var event: Event
    
    var body: some View{
        HStack(){
            //            if (event.photos.isEmpty) {
            //                ZStack(){
            //                    UrlImage(url: event.photos[0].url).frame(width: 130, height: 130)
            //                }
            //            }
            
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
                            .fontWeight(.light)
                            .font(.system(size: 12))
                            .foregroundColor(Color("Primary"))
                            .padding(.trailing, 5)
                    }.background(Color.white)
                        .cornerRadius(5)
                        .shadow(radius: 5, x:-1, y: 1)
                    
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
                        .shadow(radius: 5, x:-1, y: 1)
                }
            }.padding(8)
        }
    }
}


struct MainMapAdContainer: View{
    
    let ad : Ad
    
    var body: some View{
        UrlImage(url: ad.url)
            .frame(height: 80)
            .scaledToFit()
            .onTapGesture {
                guard let url = URL(string: ad.click) else { return }
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
            .frame(width: 50, height: 50)
            .background(Color(red: 136, green: 175, blue: 255))
            .cornerRadius(25)
            .shadow(radius: 25, x: 1, y: 2)
            .buttonStyle(.plain)
            
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
                    Image("sort")
                }else{
                    Image("map_layer")
                }
            }
            .frame(width: 50, height: 50)
            .background(Color(red: 136, green: 175, blue: 255))
            .cornerRadius(25)
            .shadow(radius: 25, x: 1, y: 2)
            .buttonStyle(.plain)
            
            
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
            .frame(width: 50, height: 50)
            .background(Color(red: 136, green: 175, blue: 255))
            .cornerRadius(25)
            .shadow(radius: 25, x: 1, y: 2)
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

struct MainMapScreen_Previews: PreviewProvider {
    static var previews: some View {
        EmptyView()
    }
}


enum ScrollDirection {
    case right
    case left
    case none
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
//}
