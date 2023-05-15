//
//  CustomAnnotation.swift
//  iosApp
//
//  Created by USER on 17/04/2023.
//  Copyright © 2023 orgName. All rights reserved.
//

import Foundation
import MapKit

class MarkerAnnotation: NSObject, MKAnnotation {
    let coordinate: CLLocationCoordinate2D
    let idPlaceLinked: String
    var selected: Bool
    
    init(coordinate: CLLocationCoordinate2D, idPlaceLinked: String, selected: Bool) {
        self.coordinate = coordinate
        self.idPlaceLinked = idPlaceLinked
        self.selected = selected
        super.init()
    }
}

//extension MarkerAnnotation{
//    func hasSameSpots(arr: [MarkerAnnotation]) -> Bool{
//
//    }
//}

class MarkerView: MKAnnotationView {
    func updateImage() {
        CATransaction.begin()
        CATransaction.setAnimationDuration(0.2)
        CATransaction.setAnimationTimingFunction(CAMediaTimingFunction(name: .easeOut))
        UIView.animate(withDuration: 0.2, delay: 0, options: .curveEaseIn, animations: {
            guard let mapAnnotation = self.annotation as? MarkerAnnotation else {return}
            if (mapAnnotation.selected){
                self.image = UIImage(named: "marker_selected")
                self.layer.anchorPoint = CGPoint(x: 0.5, y: 0.6)
            } else {
                self.image = UIImage(named: "marker")
                self.centerOffset = CGPoint(x: 0.5, y: 0.5)
            }
        }, completion: nil)
            CATransaction.commit()
    }
}
