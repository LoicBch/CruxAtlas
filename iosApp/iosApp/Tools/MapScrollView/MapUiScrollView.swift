//
//

import Foundation
import UIKit
import SwiftUI

public class MapUiScrollView: UIScrollView {

    static let viewTag = 3124123

    init() {
        super.init(frame: .zero)
    }

    required init?(coder: NSCoder) {
        fatalError("init(coder:) has not been implemented")
    }

    // MARK: - Overrides

    var onGestureShouldBegin: ((_ gestureRecognizer: UIPanGestureRecognizer, _ scrollView: UIScrollView) -> Bool)?

    public override func gestureRecognizerShouldBegin(_ gestureRecognizer: UIGestureRecognizer) -> Bool {
        guard let panGesture = gestureRecognizer as? UIPanGestureRecognizer else { return super.gestureRecognizerShouldBegin(gestureRecognizer)}
        
        return onGestureShouldBegin?(panGesture, self) ?? super.gestureRecognizerShouldBegin(gestureRecognizer)
    }

    // MARK: - SwiftUI updates

    var contentViewController: UIViewController! {
        willSet { assert(contentViewController == nil, "") }
        didSet { attach() }
    }

    var contentView: UIView { contentViewController.view }

    private func attach() {
        guard
            let contentViewController = contentViewController,
                contentViewController.parent == nil
        else {
            return
        }
 
        backgroundColor = .clear
        contentView.backgroundColor = .clear
        addSubview(contentView)
        contentView.sizeToFit()
        contentView.translatesAutoresizingMaskIntoConstraints = false
        NSLayoutConstraint.activate(contentConstraints)
    }

    private lazy var contentConstraints: [NSLayoutConstraint] = {
         
        return [
            contentView.topAnchor.constraint(equalTo: topAnchor),
            contentView.leadingAnchor.constraint(equalTo: leadingAnchor),
            contentView.bottomAnchor.constraint(equalTo: bottomAnchor),
            contentView.trailingAnchor.constraint(equalTo: trailingAnchor),
            contentView.heightAnchor.constraint(equalTo: heightAnchor)
        ]
    }()

    func updateView() {
        NSLayoutConstraint.deactivate(contentConstraints)
        contentView.removeFromSuperview()
        addSubview(contentView)
        contentSize.width = contentView.sizeThatFits(.greatest).width
        NSLayoutConstraint.activate(contentConstraints)
        contentView.sizeToFit()
    }
}

private extension CGSize {
    static var greatest = CGSize(width: CGFloat.greatestFiniteMagnitude, height: CGFloat.greatestFiniteMagnitude)
}
