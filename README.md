# Serenity BDD Mobile Automation Framework

Cross-platform Mobile Automation for Android and iOS using Serenity BDD + Appium + Gradle.

## 📦 Tech Stack
- Serenity BDD
- Appium Java Client
- Gradle Build Tool
- JUnit5
- Android + iOS

## 🚀 How To Run

```
For Android
 ./gradlew clean test \
  -DplatformName=Android \
  -DdeviceName="Pixel_5_API_34" \
  -Dapp.path="/path/to/app.apk" ```

for iOS
./gradlew clean test \
  -DplatformName=iOS \
  -DdeviceName="iPhone_15_Pro" \
  -Dapp.path="/path/to/app.app"

```
```
serenity-mobile-automation/
├── .gitignore
├── README.md
├── build.gradle
├── settings.gradle
├── src/
│   ├── main/
│   │   └── java/
│   │       └── mobile/
│   │           ├── drivers/
│   │           │   └── DriverFactory.java
│   │           ├── pages/
│   │           │   ├── BasePage.java
│   │           │   ├── PageObjectFactory.java
│   │           │   ├── LoginPageAndroid.java
│   │           │   ├── LoginPageIOS.java
│   │           │   └── LoginPageProvider.java
│   │           └── utils/
│   │               └── MobileUtilities.java
│   └── test/
│       └── java/
│           └── mobile/
│               ├── runners/
│               │   └── TestRunner.java
│               ├── steps/
│               │   └── MobileAppSteps.java
│               └── tests/
│                   └── SampleTest.java
└── src/test/resources/
└── serenity.properties
```


./gradlew clean test -DplatformName=Android


./gradlew clean test -DplatformName=iOS

adb shell monkey -p com.act.mobile.apps 

adb shell am start -n com.act.mobile.apps/.MainActivity

