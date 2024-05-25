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
        @Published private(set) var tagSelected: [(String, Bool)] = []
        @Published private(set) var checklistsShowed: Array<CheckList> = []
        @Published private(set) var loading = false
        @Published private(set) var ads = [Ad]()
        
        init(){
            getChecklists()
            getAds()
        }
        
        func selectTag(name: String){
            let index = tags.firstIndex(where: { $0.0 == name })!
            if (tags[index].1){
                tags[index].1 = false
            }else{
                tags[index].1 = true
            }
            let tagsFilter = tags.filter({ $0.1 }).map({$0.0})
            
            let selectedChecklists = checklistsGlobal.filter { checklist in
                var containsAllTags = true
                tagsFilter.forEach { tagName in
                    if !checklist.tags.contains(where: { $0 == tagName }) {
                        containsAllTags = false
                    }
                }
                return containsAllTags
            }
            checklistsShowed = selectedChecklists
        }
        
        func getChecklists(){
            loading = true
            Task.init {
                do {
                    let res = try await RFetchCheckLists().execute()!
                    print("res.count")
                    print(res.count)
                    checklistsGlobal = res
                    checklistsShowed = res
                    tags = Array(Set(res.flatMap({$0.tags}))).map({($0, false)})
                    loading = false
                } catch {
                    // handle error
                }
            }
        }
        
        func getAds(){
            Task.init {
                do {
                    ads = try await RFetchAds().execute()!
                } catch { 
                    // handle error
                }
            }
        }
    
    }
}
