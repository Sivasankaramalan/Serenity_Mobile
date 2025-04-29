@echo off
echo Cleaning previous runs...
call gradlew clean

echo Starting Appium servers...
start /b cmd /c appium -p 4723 --base-path /wd/hub > appium1.log 2>&1
start /b cmd /c appium -p 4725 --base-path /wd/hub > appium2.log 2>&1

echo Waiting for Appium servers to start...
timeout /t 5

echo Running tests on multiple devices in parallel...
start /b cmd /c gradlew test -DdeviceName="Android Emulator 1" -DplatformName=Android -DplatformVersion=11 -Dudid=emulator-5554 -DappiumPort=4723 -DappPackage=com.example.android -DappActivity=com.example.android.MainActivity -DautomationName=UiAutomator2 > device1.log 2>&1
start /b cmd /c gradlew test -DdeviceName="Android Emulator 2" -DplatformName=Android -DplatformVersion=11 -Dudid=emulator-5556 -DappiumPort=4725 -DappPackage=com.example.android -DappActivity=com.example.android.MainActivity -DautomationName=UiAutomator2 > device2.log 2>&1

echo Waiting for tests to complete...
timeout /t 120

echo Generating Serenity reports...
call gradlew reports

echo Done!