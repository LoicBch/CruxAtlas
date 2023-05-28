//
//  HistoricSearchList.swift
//  iosApp
//
//  Created by USER on 26/04/2023.
//  Copyright © 2023 orgName. All rights reserved.
//

import Foundation
import SwiftUI
import shared

struct HistoricSearchList: View{
    
    var filterSearchs: [Filter]
    var onSelectSearch: (String) -> Void
    var onDeleteSearch: (Filter) -> Void
    
    public var body: some View{
        VStack{
        HStack{
            Text("last_searched")
//                .fontWeight(.medium)
                .font(.system(size: 12))
                .foregroundColor(Color.gray)
                .padding(.top, 20)
            Spacer()
        }
        Divider()
            ForEach(filterSearchs, id: \.filterId){ filter in
                LastSearchItem(filter: filter, onDeleteSearch: {onDeleteSearch(filter)}).onTapGesture {
                    onSelectSearch(filter.filterName)
                }
            }
        }
    }
}

struct LastSearchItem: View{
    
    var filter: Filter
    var onDeleteSearch: () -> Void
    
    public var body: some View{
        HStack{
            Image("historic")
            Text(filter.filterName)
                .fontWeight(.medium)
                .font(.system(size: 16))
                .foregroundColor(Color("Tertiary"))
            Spacer()
            Image(systemName: "xmark.circle").onTapGesture {
                onDeleteSearch()
            }
        }.frame(height: 40)
    }
}
