//
//  tabViewAccessor.swift
//  iosApp
//
//  Created by USER on 07/05/2023.
//  Copyright © 2023 orgName. All rights reserved.
//

import SwiftUI
import Foundation

//struct TabBarAccessor: UIViewControllerRepresentable {
//    var callback: (UITabBar) -> Void
//    private let proxyController = ViewController()
//
//    func makeUIViewController(context: UIViewControllerRepresentableContext<TabBarAccessor>) ->
//                              UIViewController {
//        proxyController.callback = callback
//                                  
//        return proxyController
//    }
//    
//    func updateUIViewController(_ uiViewController: UIViewController, context: UIViewControllerRepresentableContext<TabBarAccessor>) {
//    }
//    
//    typealias UIViewControllerType = UIViewController
//
//    private class ViewController: UIViewController {
//        var callback: (UITabBar) -> Void = { _ in }
//
//        override func viewWillAppear(_ animated: Bool) {
//            super.viewWillAppear(animated)
//            if let tabBar = self.tabBarController {
//                tabBar.hidesBottomBarWhenPushed = true
//                tabBar.tabBar.frame.size = CGSize(width: 0, height: 0)
//                print(tabBar.toolbarItems?.count)
//                self.callback(tabBar.tabBar)
//            }
//        }
//    }
//}


// Might be possible to make tabbar sort of global struct object to avoid passing in the struct that need to display a screen without tabbar, maybe i'ts not possible tho

struct DisableBottomBar: ViewModifier {
    
    var bottomBar : UITabBar! = nil
    func body(content: Content) -> some View {
//        content.onAppear{
//            bottomBar.isHidden = true
//        }
//        .onDisappear{
//            bottomBar.isHidden = false
//        }
    }
}

struct InitBottomBar: ViewModifier {
    
    var bottomBar: UITabBar! = nil
    
    func body(content: Content) -> some View {
        content.onAppear{
            bottomBar.backgroundColor = UIColor(Color.white)
            bottomBar.barTintColor = .blue
        }
    }
}

extension View {
    func initBar(bottomBar: UITabBar) -> some View {
        modifier(InitBottomBar(bottomBar: bottomBar))
    }
}

extension View {
    func disableBottomBar(bottomBar: UITabBar) -> some View {
        modifier(DisableBottomBar(bottomBar: bottomBar))
    }
}
