import SwiftUI
import UIKit
import shared

struct MapEventsScrollView<Content: View>: UIViewRepresentable {
    
    let content: () -> Content
    @Binding var events: [Event]
    @Binding var itemIdToScrollTo: String
    
    var onDecelerating: (String) -> Void
    var onDraggingStoped: (String) -> Void
    
    
    init(events : Binding<[Event]>, itemIdToScrollTo: Binding<String> , @ViewBuilder content: @escaping () -> Content, onDraggingStoped: @escaping (String) -> Void,
         onDecelerating: @escaping (String) -> Void){
        self.content = content
        self._events = events
        self._itemIdToScrollTo = itemIdToScrollTo
        self.onDecelerating = onDecelerating
        self.onDraggingStoped = onDraggingStoped
    }
    
    func makeUIView(context: Context) -> MapUiScrollView {
        let scrollView = MapUiScrollView()
        let contentView = UIHostingController(rootView: content())
        contentView.view.translatesAutoresizingMaskIntoConstraints = false
        scrollView.addSubview(contentView.view)
        scrollView.delegate = context.coordinator
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
    
    func updateUIView(_ uiView: MapUiScrollView, context: Context) {
        if itemIdToScrollTo != "" {
            scrollToWhereId(id: itemIdToScrollTo, scrollview: uiView)
        }
        
        let contentView = content()
        
        guard let hosting = uiView.contentViewController as? UIHostingController<Content> else {
            uiView.contentViewController = UIHostingController(rootView: contentView)
            return
        }
                
        hosting.rootView = contentView
        uiView.updateView()
    }
    
    func scrollToWhereId(id: String, scrollview: UIScrollView){
        let index = events.firstIndex(where: {$0.id == id})!
        scrollToIndex(index: index, scrollview: scrollview)
    }
    
    func scrollToWhereName(name: String, scrollView: UIScrollView){
        let index = events.firstIndex(where: {$0.name == name})!
        scrollToIndex(index: index, scrollview: scrollView)
    }
    
    
    func scrollToIndex(index: Int, scrollview: UIScrollView){
        let spacingOffset = CGFloat(index * 10)
        let elementOffset = (0.85 * UIScreen.main.bounds.width) * CGFloat(index)
        let offset = spacingOffset + elementOffset
        scrollview.setContentOffset(CGPoint(x: offset, y: 0), animated: true)
    }
    
    func getVisibleItemFromOffset(offset: CGFloat, direction : ScrollingDirection) -> Event {
        let spacing = CGFloat(10)
        let elementWidth = 0.85 * UIScreen.main.bounds.width
        var directionBias = CGFloat(0)
        
        if (direction == .right){
            directionBias = 0.70 * UIScreen.main.bounds.width
        }else if (direction == .left) {
            directionBias = 0.30 * UIScreen.main.bounds.width
        }
        
        let adjustedOffset = (offset - spacing) + directionBias
        let index = Int(adjustedOffset / (elementWidth + spacing))
        
        return events[index]
    }
    
    func makeCoordinator() -> MapScrollviewCoordinator {
        MapScrollviewCoordinator(self)
    }
    
    // MARK: - COORDINATOR
    class MapScrollviewCoordinator: NSObject, UIScrollViewDelegate {
        var parent: MapEventsScrollView
        var lastVelocityXSign = 0
        var currentDirection: ScrollingDirection = .right
        
        init(_ parent: MapEventsScrollView) {
            self.parent = parent
            super.init()
        }
        
        func scrollViewDidEndDragging(_ scrollView: UIScrollView, willDecelerate decelerate: Bool) {
            if !decelerate {
                let item = parent.getVisibleItemFromOffset(offset: scrollView.contentOffset.x, direction: currentDirection)
                parent.scrollToWhereId(id: item.id, scrollview: scrollView)
                parent.onDraggingStoped(item.id)
            }
        }
        
        func scrollViewDidEndDecelerating(_ scrollView: UIScrollView) {
            if !scrollView.isDragging {
                let item = parent.getVisibleItemFromOffset(offset: scrollView.contentOffset.x, direction: currentDirection)
                parent.scrollToWhereId(id: item.id, scrollview: scrollView)
                parent.onDecelerating(item.id)
            }
        }
        
        func scrollViewDidScroll(_ scrollView: UIScrollView) {
            let currentVelocityX =  scrollView.panGestureRecognizer.velocity(in: scrollView.superview).x
            let currentVelocityXSign = Int(currentVelocityX).signum()
            if currentVelocityXSign != lastVelocityXSign &&
                currentVelocityXSign != 0 {
                lastVelocityXSign = currentVelocityXSign
            }
            
            if lastVelocityXSign < 0 {
                currentDirection = .right
            } else if lastVelocityXSign > 0 {
                currentDirection = .left
            }
        }
    }
}
