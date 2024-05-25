//
//  CheckListDetailsViewModel.swift
//  iosApp
//
//  Created by USER on 14/04/2023.
//  Copyright © 2023 orgName. All rights reserved.
//

import Foundation
import shared
 
    @MainActor class CheckListDetailsViewModel : ObservableObject {
 
        @Published var taskElements = [CheckboxItem]()
        private var taskDoneIds = [String]()
        
        func setup(checkList: CheckList){
         
            let tasksSaved = KMMPreference(context: NSObject()).getString(key: "tasks_of_\(checkList.id)")
            if(tasksSaved != nil){
                taskDoneIds = (tasksSaved?.components(separatedBy: ","))!
                self.taskElements = checkList.todos.map({CheckboxItem(taskId: $0.id,title: $0.name, isChecked: taskDoneIds.contains($0.id))})
//                taskElements.forEach({print("\($0.taskId) is \($0.isChecked)")}) 
            }else{
                self.taskElements = checkList.todos.map({CheckboxItem(taskId: $0.id,title: $0.name, isChecked: false)})
//                taskElements.forEach({print("\($0.taskId) is \($0.isChecked)")})
            }
        }
        
        func unCheckTask(taskId: String, checkList: CheckList){
            
            if let index = taskElements.firstIndex(where: { $0.taskId == taskId }) {
                        // Créez un nouvel objet CheckboxItem avec isChecked à true
                let newItem = CheckboxItem(taskId: taskElements[index].taskId,title: taskElements[index].title, isChecked: false)
                        taskElements[index] = newItem
                KMMPreference(context: NSObject()).put(key: "tasks_of_\(checkList.id)", value___:taskElements.map({$0.taskId}).joined(separator: ","))
            }
            
            KMMPreference(context: NSObject()).put(key: "tasks_of_\(checkList.id)", value___: taskElements.filter({$0.isChecked}).map({$0.taskId}).joined(separator: ","))
        }
        
        func checkTask(taskId: String, checkList: CheckList){
            if let index = taskElements.firstIndex(where: { $0.taskId == taskId }) {
                let newItem = CheckboxItem(taskId: taskElements[index].taskId,title: taskElements[index].title, isChecked: true)
                        taskElements[index] = newItem
                KMMPreference(context: NSObject()).put(key: "tasks_of_\(checkList.id)", value___: taskElements.filter({$0.isChecked}).map({$0.taskId}).joined(separator: ","))
            }
        }
        
        func isChecked(todoId: String) -> Bool{
            print("task number \(todoId) is \(taskDoneIds.contains(todoId))")
            return taskDoneIds.contains(todoId)
        }
        
        func clearAll(checkList: CheckList){
            
            for index in taskElements.indices {
                let newItem = CheckboxItem(taskId: taskElements[index].taskId,title: taskElements[index].title, isChecked: false)
                taskElements[index] = newItem
            }
            
            KMMPreference(context: NSObject()).remove(key: "tasks_of_\(checkList.id)")
        }
    }

struct CheckboxItem: Hashable {
    let id = UUID()
    var taskId: String
    var title: String
    var isChecked = false
    
    func hash(into hasher: inout Hasher) {
           hasher.combine(id)
       }
}
