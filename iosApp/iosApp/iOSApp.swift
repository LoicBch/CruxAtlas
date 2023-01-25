import SwiftUI
import UIKit
import shared

@main
struct iOSApp: App {
    
    init() {
        HelperDI().doInitKoin()
    }
    
	var body: some Scene {
		WindowGroup {
			HomeScreen()
		}
	}
}
