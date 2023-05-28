import SwiftUI

extension View {
    /* modify navigationView if you need navigation bar for all navigation view stack
     or put it on exact page if you need navigation bar only for that page
     - Parameter enabled: makes bottom nav visible
     - Returns: modified view with bottom nav
      */
    func bottomNav(enabled: Binding<Bool>) -> some View {
        self.modifier(BottomNavModifier(enabled: enabled))
    }
}

struct BottomNavModifier: ViewModifier {
    @Binding var enabled: Bool
    @Environment(\.safeAreaEdgeInsets) private var safeArea

    func body(content: Content) -> some View {
        VStack(spacing: 0) {
            content 
                Spacer(minLength: 0)
                VStack(spacing: 0) {
                    BottomNavContainer()
                }
        }
    }
}
