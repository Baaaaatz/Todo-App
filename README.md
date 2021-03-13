# Todo-App

## Libraries

- Kotlin
- AndroidX
- Material Components
- Android Architecture Components
    - Lifecycle and ViewModel
    - Navigation
        - SafeArgs
- Cloud Firestore
- Kotlinx Serialization
- Kotlin Coroutines
- Coil
- Dagger Hilt

## Clean Architecture

This app demonstrates the use of clean architecture which consists of the following layers:

*presentation* - responsible for having Activities or Fragments with its corresponding ViewModels and Adapter

*domain* - responsible for having the entities and use cases for the presentation layer to connect with the data layer

*data* - responsible for having data models and repositories for getting data

*di* - responsible for dependency injection. In this project we used Dagger Hilt

## Model - View - ViewModel

This app uses an architectural pattern that is called MVVM that facilitates the separation of the development of the graphical user interface (the view), from the development of the business logic or back-end logic (the model) so that the view is not dependent on any specific model platform, while the view model is an abstraction of the view exposing public properties and commands.

## Authors

- Jayzon Jorge F. Alcancia - [jayzonjorgealcancia@gmail.com](mailto:jayzonjorgealcancia@gmail.com)
