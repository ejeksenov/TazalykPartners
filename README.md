# TazalykPartners

TazalykPartners is used to view users statistics who hand over recyclables for recycling to the receiving points via Tazalyk app. 

## Introduction

  This project was written by solid principles of Clean Architecture and divided into three layers:
  
  1. Data layer contains Firebase Auth(authentification by email), Realtime Database(for reading and writing data) and Firebase Storage(for uploading images) implementation.
  
  2. Domain layer contains UseCases, Domain Objects/Models (Kotlin Data Classes), and Repository Interfaces.
  
  3. Presentation layer contains UI, View Objects, Android components, etc. This layer implemented MVVM and used Dagger2 for dependency injection.

## Libraries used
* [Cicerone](https://github.com/terrakok/Cicerone) - A lightweight library that makes the navigation in an Android app easy.
* [Picasso](https://github.com/square/picasso) - An image loading and caching library for Android
* [Dagger](https://github.com/google/dagger) - A fast dependency injector for Android and Java
* [RxJava](https://github.com/ReactiveX/RxJava) - A library for composing asynchronous and event-based programs by using observable sequences.
* [Mapbox](https://docs.mapbox.com/android/maps/overview/) - A customizable Maps Api

## Screenshots
![](https://raw.github.com/ejeksenov/TazalykPartners/master/tazalyk_partners_screens.png)
