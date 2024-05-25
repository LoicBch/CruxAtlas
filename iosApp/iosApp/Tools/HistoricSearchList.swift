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
            ForEach(filterSearchs, id: \.filterId){ filter in
                if (filter.filterName != ""){
                    LastSearchItem(filter: filter, onDeleteSearch: {onDeleteSearch(filter)}).onTapGesture {
                        onSelectSearch(filter.filterName)
                    }
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
                .font(.custom("CircularStd-Medium", size: 16))
                .foregroundColor(Color("Primary30"))
            Spacer()
            Image(systemName: "xmark.circle").onTapGesture {
                onDeleteSearch()
            }.foregroundColor(Color("Secondary"))
        }.frame(height: 40)
    }
}
