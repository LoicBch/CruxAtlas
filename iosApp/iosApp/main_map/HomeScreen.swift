//
//  HomeScreen.swift
//  iosApp
//
//  Created by USER on 19/01/2023.
//  Copyright © 2023 orgName. All rights reserved.
//

import Foundation
import shared
import SwiftUI


struct HomeScreen: View {
    
    @ObservedObject var viewModel = BottomSheetViewModel()
    
    public var body: some View {
        
        VStack{
            TabView() {
                BottomBar(viewModel: viewModel)
            }.edgesIgnoringSafeArea(.bottom)
                .sheet(isPresented: $viewModel.bottomSheetIsOpen) {
                    BottomSheetController(option: viewModel.bottomOption)
            }
        }
    }
}

struct BottomBar: View {
    
    var viewModel : BottomSheetViewModel
    
    var body: some View {
            NavigationView {
                MainMapScreen(bottomSheetViewModel: viewModel)
            }.tabItem {
                Image(systemName: "heart.fill")
                Text("Around me")
            }
            
            NavigationView {
                AroundLocationScreen()
            }.tabItem {
                Image(systemName: "person.fill")
                Text("Around a place")
            }
            
            NavigationView {
                NavigationLink {
                    PartnersScreen()
                } label: {
                    Text("Aller à la vue de détail")
                }
            }.tabItem {
                Image(systemName: "person.fill")
                Text("Partners")
            }
            
            NavigationView {
                NavigationLink {
                    MoreScreen()
                } label: {
                    Text("Aller à la vue de détail")
                }
            }.tabItem {
                Image(systemName: "person.fill")
                Text("More")
            }
        }
}
