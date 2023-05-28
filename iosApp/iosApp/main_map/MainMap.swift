 
//
//  MapView.swift
//  iosApp
//
//  Created by USER on 16/04/2023.
//  Copyright © 2023 orgName. All rights reserved.
//

 
import SwiftUI
import MapKit
import Combine

struct MainMap: UIViewRepresentable {
    
    @Binding var region: MKCoordinateRegion
    @Binding var mapType : MKMapType
    @Binding var markers: [MarkerAnnotation]
    @Binding var selectedMarkerId: String
    @Binding var mapIsMoving: Bool
    
    var onMapStopMoving: (CLLocationCoordinate2D) -> Void
    var onMapStartMoving: () -> Void
    var onSelectMarker: (MarkerAnnotation) -> Void
    
    
     
    func makeUIView(context: Context) -> MKMapView {
        let mapView = MKMapView()
        mapView.setRegion(MKCoordinateRegion(center: region.center, span: MKCoordinateSpan(latitudeDelta: 0.2, longitudeDelta: 0.2)), animated: false)
        mapView.showsUserLocation = true
        mapView.mapType = mapType
        mapView.delegate = context.coordinator
        return mapView
    }

    func updateUIView(_ mapView: MKMapView, context: Context) {
        
        if (mapView.region.center.latitude != region.center.latitude && !mapIsMoving ){
            mapView.setRegion(region, animated: true)
        }
        
        if (mapType != mapView.mapType){
            mapView.mapType = mapType
        }

        if (annotationsNeedToBeUpdated(mapview: mapView)){
            let hasToFocusOnSelected = needToFocusSelectedMarker(mapview: mapView)
            mapView.removeAnnotations(mapView.annotations)
            mapView.addAnnotations(markers)
            if (hasToFocusOnSelected) {
                moveToSelectedMarker(mapview: mapView)
            }
        }
    }

    func annotationsNeedToBeUpdated(mapview: MKMapView) -> Bool{
        if (!mapview.markerAnnotation().isEmpty){
            if (mapview.markerAnnotation().count != markers.count){
                print("MARKERS UPDATE markers updated because count has changed")
                return true
            }
            
            
            if(!mapview.markerAnnotation().sorted(by: { $0.idPlaceLinked < $1.idPlaceLinked }).elementsEqual(markers.sorted(by: { $0.idPlaceLinked < $1.idPlaceLinked }))){
                 
                print("MARKERS UPDATE markers updated because array of annotations has changed")
                return true
            }
            
            let markerSelected = mapview.markerAnnotation().first(where: {
                return $0.selected
            })
            
            if (markerSelected != nil && selectedMarkerId != markerSelected!.idPlaceLinked){
                print("MARKERS UPDATE markers updated because the id of selected marker has changed")
                return true
            }
            print("MARKERS UPDATE markers not updated")
            return false
        }
        print("MARKERS UPDATE markers updated because there was no annotations on the map")
      return true
    }
    
    func needToFocusSelectedMarker(mapview: MKMapView) -> Bool{
        let markerSelected = mapview.markerAnnotation().first(where: {
            return $0.selected
        })
        if (markerSelected != nil && selectedMarkerId != markerSelected!.idPlaceLinked){
            print("markers updated because the id of selected marker has changed")
            return true
        }
        return false
    }
    
    func moveToSelectedMarker(mapview: MKMapView){
        let selectedMarker = mapview.markerAnnotation().first(where: {
            return $0.selected
        })
        
        mapview.setRegion(MKCoordinateRegion(center: CLLocationCoordinate2D(latitude: selectedMarker!.coordinate.latitude, longitude: selectedMarker!.coordinate.longitude), span: region.span), animated: true)
    }
    
    func makeCoordinator() -> Coordinator {
        Coordinator(self)
    }

    // MARK: - COORDINATOR
    
    class Coordinator: NSObject, MKMapViewDelegate {
        
        var parent: MainMap
        var timer: Timer?
        
        
        init(_ parent: MainMap) {
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

        
//        func mapView(_ mapView: MKMapView, regionDidChangeAnimated animated: Bool) {
//           parent.region = mapView.region
//           parent.onMapStopMoving(mapView.region.center)
//        }
        
        func mapView(_ mapView: MKMapView, regionWillChangeAnimated animated: Bool) {
                parent.onMapStartMoving()
        }
        
        // MARK: - Because regionDidChangeAnimated fires way too much
        
//        Implement this logic if updates gets more complex, now maps dont jump because in the viewmodel we update region after all updates
                func mapViewDidChangeVisibleRegion(_ mapView: MKMapView) {
                    timer?.invalidate()
                    timer = Timer.scheduledTimer(withTimeInterval: 0.3, repeats: false) { [weak self] _ in
                        self!.parent.region = mapView.region
                        self!.parent.onMapStopMoving(mapView.region.center)
                    }
                }
    }
}

