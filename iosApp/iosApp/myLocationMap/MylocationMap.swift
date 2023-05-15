//
//  MylocationMap.swift
//  iosApp
//
//  Created by USER on 02/05/2023.
//  Copyright © 2023 orgName. All rights reserved.
//

import Foundation
import shared
import MapKit
import SwiftUI

struct MyLocationMap: UIViewRepresentable {
     
    @Binding var currentLocation: MarkerAnnotation
    @Binding var parkingLocation: MarkerAnnotation
    @Binding var region: MKCoordinateRegion
    
    var onMapStopMoving: (CLLocationCoordinate2D) -> Void
    var onSelectMarker: (MarkerAnnotation) -> Void
    
    func makeUIView(context: Context) -> MKMapView {
        let mapView = MKMapView()
        mapView.setRegion(region, animated: false)
        mapView.mapType = MKMapType.standard
        mapView.addAnnotation(currentLocation)
        mapView.addAnnotation(parkingLocation)
        mapView.delegate = context.coordinator
        return mapView
    }
    
    
    func updateUIView(_ mapView: MKMapView, context: Context) {
        mapView.mapType = MKMapType.standard
          
        if (annotationsNeedToBeUpdated(mapview: mapView)){
            mapView.removeAnnotations(mapView.annotations)
            mapView.addAnnotation(currentLocation)
            mapView.addAnnotation(parkingLocation)
        }
    }
    
    func annotationsNeedToBeUpdated(mapview: MKMapView) -> Bool{
        if (!mapview.markerAnnotation().isEmpty){
            if (mapview.markerAnnotation().count != markersCount()){
                print("markers updated because count has changed")
                return true
            }

            let markerSelected = mapview.markerAnnotation().first(where: {
                return $0.selected
            })
            
            if (markerSelected == nil && idOfSelectedMarker() != ""){
                print("markers updated because first selection of marker")
                return true
            }
            
            if (markerSelected != nil && idOfSelectedMarker() != markerSelected!.idPlaceLinked){
                print(markerSelected!.idPlaceLinked)
                print(idOfSelectedMarker())
                print("markers updated because the id of selected marker has changed")
                return true
            }
            print("markers not updated")
            return false
        }
        print("markers updated because there was no annotations on the map")
        return true
    }
    
    func parkingLocationIsPresentOnMap(mapview: MKMapView) -> Bool{
        let parkingAnnot = mapview.markerAnnotation().first(where: {
            $0.idPlaceLinked == "parking_location"
        })
        
        if (parkingAnnot == nil){
            return false
        }else{
            return true
        }
    }
    
    func idOfSelectedMarker() -> String {
        if (parkingLocation.selected){
            return "parking_location"
        }else if (currentLocation.selected){
            return "current_location"
        }else {
            return ""
        }
    }
    
    func markersCount() -> Int {
        var count = 0
        if (parkingLocation.coordinate.latitude != 0.0){
            count += 1
        }
        if (currentLocation.coordinate.latitude != 0.0){
            count += 1
        }
        return count
    }
    
    func makeCoordinator() -> Coordinator {
        Coordinator(self)
    }
    
    class Coordinator: NSObject, MKMapViewDelegate {
        var parent: MyLocationMap
        init(_ parent: MyLocationMap) {
            self.parent = parent
        }
        
        
        func mapView(_ mapView: MKMapView, viewFor annotation: MKAnnotation) -> MKAnnotationView? {
            let identifier = "marker_annotation"
            
            if (annotation is MarkerAnnotation){
                let markerAnnotation = annotation as! MarkerAnnotation
                guard let annotationView = mapView.dequeueReusableAnnotationView(withIdentifier: identifier) else {
                    let view = MarkerView(annotation: markerAnnotation, reuseIdentifier: identifier)
                    if (markerAnnotation.selected){
                        view.image = UIImage(named: "marker_selected")
                    }else{
                        view.image = UIImage(named: "marker")
                    }
                    view.canShowCallout = false
                    view.centerOffset = CGPointMake(0, -(view.image?.size.height)! / 2)
                    return view
                }
                if (markerAnnotation.selected){
                    annotationView.image = UIImage(named: "marker_selected")
                }else{
                    annotationView.image = UIImage(named: "marker")
                }
                annotationView.canShowCallout = false
                annotationView.centerOffset = CGPointMake(0, -(annotationView.image?.size.height)! / 2)
                return annotationView
            }else {
                return nil
            }
        }
        
        
        func mapView(_ mapView: MKMapView, didSelect view: MKAnnotationView) {
            let annotationSelected = view.annotation as! MarkerAnnotation
            parent.onSelectMarker(annotationSelected)
        }
        
        func mapView(_ mapView: MKMapView, regionDidChangeAnimated animated: Bool) {
            parent.region = mapView.region
            parent.onMapStopMoving(mapView.region.center)
        }
        
    }
}

