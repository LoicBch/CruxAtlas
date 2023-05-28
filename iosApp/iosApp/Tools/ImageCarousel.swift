//
//  ImageCarousel.swift
//  iosApp
//
//  Created by USER on 07/05/2023.
//  Copyright © 2023 orgName. All rights reserved.
//

import SwiftUI
import shared

struct ImageCarouselView: View {
    var images: [Photo]

    @State private var currentImage: String = ""

    var body: some View {
        TabView(selection: $currentImage){
            ForEach(images, id: \.url){ photo in
                GeometryReader{ proxy in
                    let size = proxy.size
                    UrlImageGallery(url: photo.url)
                }.tabItem{
                    Text("")
                }
            }
        }
        .tabViewStyle(.page(indexDisplayMode: .automatic))
        .ignoresSafeArea()
        .onTapGesture {
            //                            Full screen
        }
        }
    }
