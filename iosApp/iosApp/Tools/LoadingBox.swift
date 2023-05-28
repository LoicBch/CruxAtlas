//
//  LoadingBox.swift
//  iosApp
//
//  Created by USER on 17/05/2023.
//  Copyright © 2023 orgName. All rights reserved.
//

import Foundation
import SwiftUI

struct LoadingBox: View {
    
    var onExit: () -> Void
    
    public var body: some View{
        VStack {
//            Spacer()
//            HStack{
                Spacer()
                VStack{
                    Image("logo")
                        .resizable()
                        .frame(width: 160, height: 160)
                    HStack{
                        Text("loading")
                            .fontWeight(.medium)
                            .font(.system(size: 16))
                            .foregroundColor(Color("Tertiary"))
                            .frame(alignment: .leading)
                        ProgressView().padding(.leading, 16)
                            .progressViewStyle(CircularProgressViewStyle())
                    }
                    .padding(.top, 20)
                }.padding(24)
                    .background(RoundedRectangle(cornerRadius: 25)
                        .fill(Color.white)).shadow(color: Color.black.opacity(0.2), radius: 2, x: 0, y: 2)
                    .padding(16)
                Spacer()
            }
//            Spacer()
//        }.background(Color("ClearGrey")).padding(.bottom, 72)
    }
}
