//
//  CheckListDetailsViewModel.swift
//  iosApp
//
//  Created by USER on 14/04/2023.
//  Copyright © 2023 orgName. All rights reserved.
//

import Foundation
import shared

extension CheckListDetailsScreen {
    @MainActor class CheckListDetailsViewModel : ObservableObject {
 
        @Published private(set) var taskDoneIds = [String]()
        
        func setup(checkList: CheckList){
         
            let tasksSaved = KMMPreference(context: NSObject()).getString(key: "tasks_of_\(checkList.id)")
            if(tasksSaved != nil){
                taskDoneIds = (tasksSaved?.components(separatedBy: ","))!
            }
        }
        
        func unCheckTask(taskId: String, checkList: CheckList){
            taskDoneIds.removeAll(where: {$0 == taskId})
            KMMPreference(context: NSObject()).put(key: "tasks_of_\(checkList.id)", value___: taskDoneIds.joined(separator: ","))
        }
        
        func checkTask(taskId: String, checkList: CheckList){
            taskDoneIds.append(taskId)
            KMMPreference(context: NSObject()).put(key: "tasks_of_\(checkList.id)", value___: taskDoneIds.joined(separator: ","))
        }
        
        func clearAll(checkList: CheckList){
            taskDoneIds.removeAll()
            KMMPreference(context: NSObject()).remove(key: "tasks_of_\(checkList.id)")
        }
    }
}
