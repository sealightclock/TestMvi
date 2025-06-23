# TestMvi

## Introduction

This is a dummy Android app built with the "MVI" or "MVVM Clean + Intent" architecture, for testing and
learning purposes.

## Technologies used

This Android app uses the following technologies:

### Basic technologies

These are some of the latest technologies used in developing an Android app:

- Kotlin
- Jetpack Compose
- libs.versions.toml
- build.gradle.kts

### Specialized technologies

This Android app uses the following specialized technologies:

- MVI: for a clean architecture.
- Preferences DataStore: for storing data locally to achieve data persistence.
- Ktor: for making HTTP requests to remote servers.

## Source file structure for a feature

### <feature>

#### -- ***presentation***/

---- **view**/

------ [Feature]Screen.kt

---- model/

------ [Feature]Model.kt (Presentation layer entity)

------ [Feature]UiMapper.kt (for conversion between Presentation and Domain layers)

---- **viewmodel**/

------ [Feature]Viewmodel.kt

------ [Feature]ViewmodelFactory.kt

#### -- ***domain***/

---- **entity**/

------ [Feature]Entity.kt (Domain layer entity)

---- **usecase**/

------ Get[Feature]FromLocalUseCase.kt

------ Store[Feature]ToLocalUseCase.kt

------ Get[Feature]FromRemoteUseCase.kt

#### -- ***data***/

---- dto/

------ [Feature]DatastoreDto.kt (Data layer entity)

------ [Feature]DatastoreDataMapper.kt (for conversion between Data and Domain layers)

---- **repository**/

------ [Feature]Repository.kt

------ [Feature]RepositoryImpl.kt

---- **datasource**/

------ local/

-------- [Feature]LocalDataSource.kt

-------- datastore/

---------- [Feature]DatastoreDataSource.kt

------ remote/

-------- [Feature]RemoteDataSource.kt

-------- ktor/

---------- [Feature]KtorApi.kt

---------- [Feature]KtorDataSource.kt

(together with some additional files/directories that are not specific to the MVI architecture)

which reflects the chosen design pattern "MVI".

## Testing

#### Testability Infrastructure

The source code has been slightly modified to accommodate automated testing with widget and
integration tests. These additions are not for production features.

The "MVI" architecture also helps to support unit testing.

## Scalability

With the "MVI" architecture, it is relatively easy to add more features to the app.

All of these features, including:

- "users"
- "location"
- "weather"
- "settings"

have been implemented using the same "MVI" design pattern and a similar file
structure. Some minor features may be implemented using a simplified version of the MVI architecture.

This project is evolving based on our understanding of Android, especially of the "MVI" architecture.
