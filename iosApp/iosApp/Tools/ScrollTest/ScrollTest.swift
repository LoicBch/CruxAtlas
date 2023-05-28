import SwiftUI
import UIKit
import shared

struct ScrollTest<Content: View>: UIViewRepresentable {
    
    let content: () -> Content
    
    init(@ViewBuilder content: @escaping () -> Content) {
        self.content = content
    }
    
    func makeUIView(context: Context) -> UIScrollViewTest {
        let scrollView = UIScrollViewTest()
        let contentView = UIHostingController(rootView: content())
        contentView.view.translatesAutoresizingMaskIntoConstraints = false
        scrollView.addSubview(contentView.view)
        
        NSLayoutConstraint.activate([
            contentView.view.leadingAnchor.constraint(equalTo: scrollView.contentLayoutGuide.leadingAnchor),
            contentView.view.trailingAnchor.constraint(equalTo: scrollView.contentLayoutGuide.trailingAnchor),
            contentView.view.topAnchor.constraint(equalTo: scrollView.contentLayoutGuide.topAnchor),
            contentView.view.bottomAnchor.constraint(equalTo: scrollView.contentLayoutGuide.bottomAnchor),
            contentView.view.heightAnchor.constraint(equalTo: scrollView.frameLayoutGuide.heightAnchor)
        ])
        contentView.view?.backgroundColor = UIColor(.clear)
        return scrollView
    }
    
    func updateUIView(_ uiView: UIScrollViewTest, context: Context) {
        let contentView = content()
        
        guard let hosting = uiView.contentViewController as? UIHostingController<Content> else {
            uiView.contentViewController = UIHostingController(rootView: contentView)
            return
        }
                
        hosting.rootView = contentView
        uiView.updateView()
    }
}
 
    
//    func makeCoordinator() -> MapScrollviewCoordinator {
//        MapScrollviewCoordinator(self)
//    }
    
     // MARK: - COORDINATOR
//    class MapScrollviewCoordinator: NSObject, UIScrollViewDelegate {
//        var parent: MapScrollView
//        var lastVelocityXSign = 0
//        var currentDirection: ScrollingDirection = .right
//
//        init(_ parent: MapScrollView) {
//            self.parent = parent
//            super.init()
//        }
//    }

