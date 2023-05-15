//
//  File.swift
//  iosApp
//
//  Created by USER on 15/04/2023.
//  Copyright © 2023 orgName. All rights reserved.
//

import Foundation
import SwiftUI

struct UrlImage: View {
    @State private var image: UIImage? = nil
    
    let url: String
    
    var body: some View {
        if let image = image {
            Image(uiImage: image)
                .resizable()
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
