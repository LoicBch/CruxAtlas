import SwiftUI
import UIKit
import shared

@main
struct iOSApp: App {
    
    init() {
        HelperDI().doInitKoin()
        for family in UIFont.familyNames.sorted() {
                    let names = UIFont.fontNames(forFamilyName: family)
                    print("Family: \(family) Font names: \(names)")
                }
    }
    
	var body: some Scene {
		WindowGroup { 
            HomeScreen()
                .preferredColorScheme(.light)
                .environmentObject(NavigationViewState())
                .environmentObject(AppState())
		}
	}
}
