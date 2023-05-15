//
//  PartnersViewModel.swift
//  iosApp
//
//  Created by USER on 14/04/2023.
//  Copyright © 2023 orgName. All rights reserved.
//

import Foundation
import shared

extension PartnersScreen {
    @MainActor class PartnersViewModel : ObservableObject {
        
        @Published private(set) var partners = [Partner]()
        @Published private(set) var isLoading = false
        
        init(){
            getPartners()
        }
        
        func getPartners(){
            isLoading = true
            Task.init {
                do {
                    partners = try await RFetchPartners().execute()!
                    isLoading = false
                } catch {
                    // handle error
                }
            }
        }
    }
}
