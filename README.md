## EMI Calculator App
Ever wanted a EMI calculator app which doesn't have any ads and is really fast? well this project is for that exact reason. 
It uses the formula
`EMI = P x R x (1+R)^N / [(1+R)^N-1]`
locally and provides you with an instant result .  This project also shows the amortization schedule for you to check how much interest is 
levied in which month.


This is a Kotlin Multiplatform project targeting Android, iOS, Desktop.

* `/composeApp` is for code that will be shared across your Compose Multiplatform applications.
  It contains several subfolders:
  - `commonMain` is for code that’s common for all targets.
  - Other folders are for Kotlin code that will be compiled for only the platform indicated in the folder name.
    For example, if you want to use Apple’s CoreCrypto for the iOS part of your Kotlin app,
    `iosMain` would be the right folder for such calls.

* `/iosApp` contains iOS applications. Even if you’re sharing your UI with Compose Multiplatform, 
  you need this entry point for your iOS app. This is also where you should add SwiftUI code for your project.


Learn more about [Kotlin Multiplatform](https://www.jetbrains.com/help/kotlin-multiplatform-dev/get-started.html)…


For creating a dmg for your macbook run `./gradlew packageDmg` . once the build is done it will open the dmg and ask you to 
move the app into your applications folder.

Would you love to contribute? raise a PR 