# Camperpro 
**Kotlin Multiplatform** project with SwiftUI, Jetpack Compose, Compose destination, Ktor Client,
SqlDelight and Koin
. Currently running on
* Android (Jetpack Compose)
* iOS (SwiftUI)

## Developement design 
[UML Arch](Camper pro architecture.drawio.png)
Project design is largely overkill to benchmark limit of KMM it respects following code concept :  
- Clean architecture  
- Package by feature
- Dependecy injection
- Abstraction over duplication 
- Domain driven design 

# Data direction
Compose x SwiftUI -> ViewModel -> Usecase -> Repository -> Mappers -> DataSource
Compose x SwiftUI <- ViewModel <- Usecase <- Repository <- Mappers <- DataSource

# Dependency Direction keep model free
Compose x SwiftUI -> ViewModel -> Usecase -> Model <- Repository <- Mappers <- DataSource

### Way of improvement


## Shared module

[ResultWrapper.kt](/shared/src/commonMain/kotlin/com/example/camperpro/data/ResultWrapper.kt)
- ResultWrapper.kt should be implement in a way that enable it to be used by iosApp module, 
  because nested generic are not yet handle by KMM   
- Loading state could be directly handle by the resultHandler sealed class
- Error case are not handle for now, we should implement error return logic on server side and 
  handle it in the "When ResultWrapper -> Failure" case of viewModels in both platform 

[FetchSpotsAtLocation.kt](/shared/src/commonMain/kotlin/com/example/camperpro/domain/usecases/FetchSpotsAtLocation.kt)
- UseCase could get rid of the ios replica. It is used only for koin injection because we can't use 
  kotlin  invoke function from Ios module, we could for example make a ios usecase wrapper to 
  simplify.

[MainMapViewModel.kt](/androidApp/src/main/java/com/example/camperpro/android/mainmap/MainMapViewModel.kt)
- We could make viewModels commons in shared platforms by creating customs observers classes 
  that can react in the meantime to Kotlin Flow update and @State Published swift objects 

[managers](/shared/src/commonMain/kotlin/com/example/camperpro/managers)
- Location managers should be fixed for some reasons lambdas block passed by iosModule donc 
  execute in shared module
- Network managers has to be improved 
- Ideally there are still things that could be made by independent managers. To optimize we 
  should make common all the duplicated extensions kotlin and swift functions   


## Android module

- Performance could benchmark and optimized there are views that are still having useless 
  redrawn like the MainMap horizontalList
- Each tabs of the app menu is handle like a separate navigate destination this cause the 
  destination to be redrawn each time wego back to the previous tab. we could treat the menu 
  like a tabView like in ios and start separate navigation tree only inside each one of tabs. 
  this could improve performance
- State hosting should be benchmark and replaced at higher level, for example the current region 
  of the map represented by camerapositionState should be hosted in viewModel like in ios
- ViewModel could be cleaned by having a specific StateHolder object and combining all the flows 
  of the stateHolder
- After loading spots around a place or events map should refocuse to display all visible
  markers to the user

## Ios module
  
- TabView in detailsDealerScreen has strange behavior
- Performance improvement, map might have strange refocus
- After loading spots around a place or events map should refocuse to display all visible 
  markers to the user