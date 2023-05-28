////
////  CustomScrollViewTest.swift
////  iosApp
////
////  Created by USER on 26/05/2023.
////  Copyright © 2023 orgName. All rights reserved.
////
//
//import Foundation
//import SwiftUI
//import MapKit
//import shared
//
//struct CustomScrollViewTest : View {
//
//    @StateObject var vm: ScrollViewVm = ScrollViewVm()
//    @State var didEndDragging: Bool = false
//    @State var didStopScroll: Bool = false
//    @State var itemToScrollTo: String = ""
//
//    public var body: some View{
//        VStack{
//            NavigationView{
//                MainList(dealers: $vm.dealers, itemToScrollTo: $itemToScrollTo)
//            }
//            Text("tap me !").onTapGesture {
//                itemToScrollTo = vm.dealers[35].id
//            }
//        }
//    }
//}
//
//class ScrollViewVm: ObservableObject{
//    @Published var dealers: [Dealer] = []
//
//    init(){
//        for i in 0..<50 {
//            dealers.append(Dealer(id: "\(i)", name: "item \(i)", brands: [], services: [], address: "", postalCode: "", countryIso: "", phone: "", email: "", website: "", facebook: "", youtube: "", instagram: "", twitter: "", isPremium: false, city: "", latitude: 0.0, longitude: 0.0, photos: []))
//        }
//    }
//}
//
//struct MainList: View {
//
//    @Binding var dealers: [Dealer]
//    @Binding var itemToScrollTo: String
//
//    init(dealers: Binding<[Dealer]>, itemToScrollTo: Binding<String>){
//        self._dealers = dealers
//        self._itemToScrollTo = itemToScrollTo
//    }
//
//    public var body: some View{
////        MapScrollView(dealers: $dealers, itemIdToScrollTo: $itemToScrollTo) {
////            LazyHStack(spacing: 10) {
////                ForEach(dealers, id: \.name) { marker in
////                    NavigationLink(destination: Text("OPENED")){
////                        Text(marker.name)
////                            .font(.title)
////                            .frame(width: UIScreen.main.bounds.width * 0.85)
////                            .background(Color.blue)
////                            .foregroundColor(.white)
////                            .cornerRadius(10)
////                    }
////                }
////            }
////        }
////    }
//}
