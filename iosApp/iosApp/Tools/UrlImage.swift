//
//  File.swift
//  iosApp
//
//  Created by USER on 15/04/2023.
//  Copyright © 2023 orgName. All rights reserved.
//

import Foundation
import SwiftUI

struct UrlImagePub: View {
    @State private var image: UIImage? = nil
    
    let url: String
    
    var body: some View {
        if let image = image {
            Image(uiImage: image)
                .resizable()
                .scaledToFill()
                .aspectRatio(contentMode: .fill)
                .frame(width: .infinity, height: 70)
        } else {
            ProgressView()
                .onAppear(perform: loadImage)
        }
    }
    
    func loadImage() {
        
        guard let url = URL(string: url) else { return }
        URLSession.shared.dataTask(with: url) { data, response, error in
            if let data = data, let image = UIImage(data: data) {
                DispatchQueue.main.async {
                    self.image = image
                }
            }
        }.resume()
    }
}

struct UrlImageList: View {
    @State private var image: UIImage? = nil
    
    let url: String
    
    var body: some View {
        if let image = image {
            Image(uiImage: image)
                .resizable()
                    .scaledToFill()
                    .frame(width: 130, height: 130, alignment: .center)
                    .cornerRadius(8, corners: [.topLeft, .bottomLeft])
                    .clipped()
        } else {
            ProgressView()
                .onAppear(perform: loadImage)
        }
    }
    
    func loadImage() {
        
        guard let url = URL(string: url) else { return }
        URLSession.shared.dataTask(with: url) { data, response, error in
            if let data = data, let image = UIImage(data: data) {
                DispatchQueue.main.async {
                    self.image = image
                }
            }
        }.resume()
    }
}

struct UrlImageGallery: View {
    @State private var image: UIImage? = nil
    
    let url: String
    
    var body: some View {
        if let image = image {
            Image(uiImage: image)
                .resizable()
                .scaledToFill()
                .aspectRatio(contentMode: .fill)
                .frame(width: .infinity, height: 150)
        } else {
            ProgressView()
                .onAppear(perform: loadImage)
        }
    }
    
    func loadImage() {
        
        guard let url = URL(string: url) else { return }
        URLSession.shared.dataTask(with: url) { data, response, error in
            if let data = data, let image = UIImage(data: data) {
                DispatchQueue.main.async {
                    self.image = image
                }
            }
        }.resume()
    }
}

struct UrlImage: View {
    @State private var image: UIImage? = nil
    
    let url: String
    
    var body: some View {
        if let image = image {
            Image(uiImage: image)
                .resizable()
                .scaledToFit()
                .aspectRatio(contentMode: .fit)
        } else {
            ProgressView()
                .onAppear(perform: loadImage)
        }
    }
    
    func loadImage() {
        
        guard let url = URL(string: url) else { return }
        URLSession.shared.dataTask(with: url) { data, response, error in
            if let data = data, let image = UIImage(data: data) {
                DispatchQueue.main.async {
                    self.image = image
                }
            }
        }.resume()
    }
}

extension View {
    func cornerRadius(_ radius: CGFloat, corners: UIRectCorner) -> some View {
        clipShape( RoundedCorner(radius: radius, corners: corners) )
    }
}

struct RoundedCorner: Shape {

    var radius: CGFloat = .infinity
    var corners: UIRectCorner = .allCorners

    func path(in rect: CGRect) -> Path {
        let path = UIBezierPath(roundedRect: rect, byRoundingCorners: corners, cornerRadii: CGSize(width: radius, height: radius))
        return Path(path.cgPath)
    }
}
