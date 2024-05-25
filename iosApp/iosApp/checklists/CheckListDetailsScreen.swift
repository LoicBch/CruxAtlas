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
    
    @ObservedObject private var viewModel = CheckListDetailsViewModel()
    var checklist: CheckList
    @Environment(\.presentationMode) var presentationMode
    
    public var body: some View {
        VStack {
            ScrollView(.vertical, showsIndicators: false){
                VStack {
                    ZStack(alignment: .top){
                        UrlImageChecklist(url: checklist.imageLink)
                        HStack{
                            Button(action: {
                                
                            }){
                                Image(systemName: "arrow.left").onTapGesture {
                                    presentationMode.wrappedValue.dismiss()
                                }
                            }
                            .frame(width: 32, height: 32)
                            .background(Color(red: 136, green: 175, blue: 255))
                            .cornerRadius(25)
                            .shadow(color: Color.black.opacity(0.2), radius: 2, x: 0, y: 2)
                            .buttonStyle(.plain)
                            
                            Spacer()
                            
                            NavigationLink(destination: HelpScreen()
                                .navigationBarBackButtonHidden(true)
                            ) {
                                    Image("help_round")
                            }
//                            .frame(width: 32, height: 32)
//                            .background(Color(red: 136, green: 175, blue: 255))
//                            .cornerRadius(25)
//                            .shadow(color: Color.black.opacity(0.2), radius: 2, x: 0, y: 2)
//                            .buttonStyle(.plain)
                        }.padding(.top, 12).padding(.horizontal, 16)
                    }
                    
                    VStack{
                        HStack{
                            Text(checklist.name)
                                .font(.custom("CircularStd-Medium", size: 22))
                                .fontWeight(.bold)
                                .foregroundColor(Color.black)
                                .padding(.top, 24)
                            Spacer()
                        }
                        
                        HStack{
                            Text(getTagsStrings(tags: checklist.tags))
                                .font(.custom("CircularStd-Medium", size: 12))
                                .fontWeight(.medium)
                                .foregroundColor(Color("Primary"))
                                .padding(.top, 12)
                            Spacer()
                        }
                        
                        HStack{
                            Text(checklist.description_)
                                .font(.custom("CircularStd-Medium", size: 14))
                                .fontWeight(.medium)
                                .foregroundColor(Color("Neutral20"))
                                .padding(.top, 12)
                            Spacer()
                        }
                        
                        
                        HStack{
                            Spacer()
                            Text("\(viewModel.taskElements.filter({$0.isChecked}).count) / \(checklist.todos.count)")
                                .font(.custom("CircularStd-Medium", size: 12))
                                .fontWeight(.medium)
                                .foregroundColor(Color("Primary"))
                                .padding(.top, 25).padding(.trailing, 12)
                        }
                        if (!viewModel.taskElements.isEmpty){
                            ForEach(viewModel.taskElements, id: \.self){ task in
                                TodoItem(isChecked: task.isChecked, selectedTasks: $viewModel.taskElements, task: task, onSelect: {
                                    if (task.isChecked){
                                        viewModel.unCheckTask(taskId: task.taskId, checkList: checklist)
                                    }else{
                                        viewModel.checkTask(taskId: task.taskId, checkList: checklist)
                                    }
                                })
                            }
                        }
                    }.padding(.horizontal, 17)
                }
                
                
                Spacer()
                
                AppButton(action: {
                    viewModel.clearAll(checkList: checklist)
                }, title: "clear_all", isEnable: true).padding(.top, 35)
                
            }.frame(height: .infinity)
        }.onAppear(){
            viewModel.setup(checkList: checklist)
        }
    }
}

struct TodoItem: View {
     
    
    @State var isChecked: Bool
    @Binding var selectedTasks: [CheckboxItem]
    var task: CheckboxItem
    var onSelect: () -> Void
    
    
    public var body: some View{
        VStack{
            Divider()
            HStack{
                Text(task.title)
                    .font(.custom("CircularStd-Medium", size: 14))
                    .foregroundColor(Color("Tertiary30"))
                    .fontWeight(.medium)
                Spacer()
                
                Toggle("", isOn: $isChecked).onChange(of: isChecked){ value in
                        onSelect()
                }
                .toggleStyle(CheckBoxAppStyle())
            }
        }
    }
}

func getTagsStrings(tags: [String]) -> String {
    return tags.map({ "#\($0)"}).joined(separator: " ")
}
