#!/bin/bash

# Clean up previous runs
./gradlew clean

# Start Appium servers
echo "Starting Appium servers..."
appium -p 4723 --base-path /wd/hub > appium1.log 2>&1 &
APPIUM1_PID=$!
appium -p 4725 --base-path /wd/hub > appium2.log 2>&1 &
APPIUM2_PID=$!

# Wait for Appium servers to start
echo "Waiting for Appium servers to start..."
sleep 5

# Run tests in parallel
echo "Running tests on multiple devices in parallel..."
./gradlew test -DdeviceName="Android Emulator 1" -DplatformName=Android -DplatformVersion=11 -Dudid=emulator-5554 -DappiumPort=4723 -DappPackage=com.example.android -DappActivity=com.example.android.MainActivity -DautomationName=UiAutomator2 > device1.log 2>&1 &
TASK1_PID=$!

./gradlew test -DdeviceName="Android Emulator 2" -DplatformName=Android -DplatformVersion=11 -Dudid=emulator-5556 -DappiumPort=4725 -DappPackage=com.example.android -DappActivity=com.example.android.MainActivity -DautomationName=UiAutomator2 > device2.log 2>&1 &
TASK2_PID=$!

# Wait for tests to complete
echo "Waiting for tests to complete..."
wait $TASK1_PID
wait $TASK2_PID

# Generate reports
echo "Generating Serenity reports..."
./gradlew reports

# Stop Appium servers
echo "Stopping Appium servers..."
kill $APPIUM1_PID
kill $APPIUM2_PID

echo "Done!"