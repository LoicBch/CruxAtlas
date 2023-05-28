import Foundation

struct BottomNavTabItem: Identifiable {
    var id: Int {  type.id }
    let type: BottomNavTab

    init(
            type: BottomNavTab,
            badge: Int = 0
    ) {
        self.type = type
    }
}
