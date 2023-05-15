//
//  CheckListsViewModel.swift
//  iosApp
//
//  Created by USER on 13/04/2023.
//  Copyright © 2023 orgName. All rights reserved.
//

import Foundation
import shared

extension CheckListScreen {
    @MainActor class CheckListsViewModel : ObservableObject {
        
        private(set) var checklistsGlobal = [CheckList]()
        @Published private(set) var tags: [(String, Bool)]  = []
        @Published private(set) var tagSelected = [Event]()
        @Published var checklistsShowed = [CheckList]()
        @Published private(set) var loading = false
        
        func selectTag(name: String){
            
        }
        
        func getChecklists(){
            loading = true
            Task.init {
                do {
                    let res = try await RFetchCheckLists().execute()!
                    checklistsGlobal = res
                    checklistsShowed = res
                    tags = Array(Set(res.flatMap({$0.tags}))).map({($0, false)})
                    loading = false
                } catch {
                    // handle error
                }
            }
        }
    
    }
}
