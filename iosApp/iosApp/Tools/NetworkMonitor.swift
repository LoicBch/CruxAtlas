//
//  NetworkMonitor.swift
//  iosApp
//
//  Created by USER on 03/05/2023.
//  Copyright © 2023 orgName. All rights reserved.
//

import Foundation
import Network

final class NetworkMonitor: ObservableObject {
    @Published private(set) var isConnected = false
    @Published private(set) var isCellular = false
    
    private let networkMonitor = NWPathMonitor()
    private let workerQueue = DispatchQueue.global()
    
    init(){
        start()
    }
    
    public func start() {
        networkMonitor.start(queue: workerQueue)
        networkMonitor.pathUpdateHandler = { [weak self] path in
            DispatchQueue.main.async {
                self?.isConnected = path.status == .satisfied
                self?.isCellular = path.usesInterfaceType(.cellular)
            }
        }
    }
    
    public func stop() {
        networkMonitor.cancel()
    }
}
