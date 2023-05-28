//
//  CheckListDetails.swift
//  iosApp
//
//  Created by USER on 14/04/2023.
//  Copyright © 2023 orgName. All rights reserved.
//

import Foundation
import shared
import SwiftUI

struct CheckListDetailsScreen: View {
    
    @StateObject private var viewModel = CheckListDetailsViewModel()
    @State private var tasksSelected: [(String, Bool)] = []
    var checklist: CheckList
    @Environment(\.presentationMode) var presentationMode
    
    public var body: some View {
        VStack {
            ScrollView(.vertical, showsIndicators: false){
                VStack {
                    ZStack(alignment: .top){
                        UrlImage(url: checklist.imageLink).frame(height: 100)
                        HStack{
                            Button(action: {
                                
                            }){
                                Image(systemName: "arrow.left").onTapGesture {
                                    presentationMode.wrappedValue.dismiss()
                                }
                            }
                            .frame(width: 50, height: 50)
                            .background(Color(red: 136, green: 175, blue: 255))
                            .cornerRadius(25)
                            .shadow(color: Color.black.opacity(0.2), radius: 2, x: 0, y: 2)
                            .buttonStyle(.plain)
                            
                            Spacer()
                            Button(action: {
                            }){
                                Image("help")
                            }
                            .frame(width: 50, height: 50)
                            .background(Color(red: 136, green: 175, blue: 255))
                            .cornerRadius(25)
                            .shadow(color: Color.black.opacity(0.2), radius: 2, x: 0, y: 2)
                            .buttonStyle(.plain)
                        }.padding(.top, 12)
                    }
                    
                    HStack{
                        Text(checklist.name)
                            .font(.system(size: 22))
                            .fontWeight(.bold)
                            .foregroundColor(Color("Tertiary"))
                            .padding(.top, 24)
                        Spacer()
                    }
                    
                    HStack{
                        Text(getTagsStrings(tags: checklist.tags))
                            .font(.system(size: 12))
                            .fontWeight(.medium)
                            .foregroundColor(Color("Primary"))
                            .padding(.top, 12)
                        Spacer()
                    }
                    
                    HStack{
                        Text(checklist.description_)
                            .font(.system(size: 14))
                            .fontWeight(.medium)
                            .foregroundColor(Color("Tertiary"))
                            .padding(.top, 12)
                        Spacer()
                    }
                    
                    
                    HStack{
                        Spacer()
                        Text("\(viewModel.taskDoneIds.count) / \(checklist.todos.count)")
                    }
                    if (!tasksSelected.isEmpty){
                        ForEach(Array(checklist.todos), id: \.self){ todo in
                            TodoItem(todo: todo, selectedTasks: $tasksSelected).onTapGesture {
                                if (!viewModel.taskDoneIds.contains(todo.id)){
                                    viewModel.checkTask(taskId: todo.id, checkList: checklist)
                                }else{
                                    viewModel.unCheckTask(taskId: todo.id, checkList: checklist)
                                }
                            }
                        }
                    }
                }.onAppear {
                    tasksSelected = checklist.todos.map({
                        return ($0.id, false)
                    })
                }.padding(.horizontal, 17)
                
                
                Spacer()
                
                AppButton(action: {
                    viewModel.clearAll(checkList: checklist)
                }, title: "clear", isEnable: true)
                
            }.frame(height: .infinity)
        }
    }
}

struct TodoItem: View {
    
    var todo: Todo
    @Binding var selectedTasks: [(String, Bool)]
    
    public var body: some View{
        VStack{
            Divider()
            HStack{
                Text(todo.name)
                Spacer()
                Toggle("", isOn: $selectedTasks.first(where: {
                    $0.0.wrappedValue == todo.id
                })!.1)
                .toggleStyle(CheckBoxAppStyle())
            }
        }
    }
}

func getTagsStrings(tags: [String]) -> String {
    return tags.map({ "#\($0)"}).joined(separator: " ")
}
