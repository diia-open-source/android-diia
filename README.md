

# Diia


This repository provides an overview over the flagship product [**Diia**](https://diia.gov.ua/) developed by the [**Ministry of Digital Transformation of Ukraine**](https://thedigital.gov.ua/).

**Diia** is an app with access to citizen’s digital documents and government services.

The application was created so that Ukrainians could interact with the state in a few clicks, without spending their time on queues and paperwork - **Diia** open source application will help countries, companies and communities build a foundation for long-term relationships. At the heart of these relations are openness, efficiency and humanity.

We're pleased to share the **Diia** project with you.

## Useful Links

|Topic|Link|Description|
|--|--|--|
|Ministry of Digital Transformation of Ukraine|https://thedigital.gov.ua/|The Official homepage of the Ministry of Digital Transformation of Ukraine|
|Diia App|https://diia.gov.ua/|The Official website for the Diia application


## Getting Started

## Build Process

To build you are required to have the dependency [Android Studio](https://developer.android.com/studio) installed. You can then follow these instructions:

1. Clone or download this repository
2. Open the project in Android Studio and run it from there or build an APK directly through Gradle:
   ``` ./gradlew :opensource:assembleGplayDebug```
   *NOTE: Android SDK should be added to PATH environment variable for this to work.*

Deploy to Device/Emulator:
```./gradlew :opensource:installGplayDebug```
*NOTE: You can also replace the "Debug" with "Release" to get an optimized release binary.*

Before building Huawei specific app generate and place agconnect-services.json file in opensource module.

For build Huawei specific APK file use next command:
```./gradlew :opensource:assembleHuaweiDebug```

To deploy to device/emulator for Huawei use this:
```./gradlew :opensource:installHuaweiDebug```

## How to test

To get mock user for testing please refer to the [TESTING.md](https://github.com/diia-open-source/diia-setup-howto/blob/main/TESTING.md) file for details.

## How to contribute

The Diia project welcomes contributions into this solution; please refer to the [CONTRIBUTING.md](./CONTRIBUTING.md) file for details

## Licensing

Copyright (C) Diia and all other contributors.

Licensed under the  **EUPL**  (the "License"); you may not use this file except in compliance with the License. Re-use is permitted, although not encouraged, under the EUPL, with the exception of source files that contain a different license.

You may obtain a copy of the License at  [https://joinup.ec.europa.eu/collection/eupl/eupl-text-eupl-12](https://joinup.ec.europa.eu/collection/eupl/eupl-text-eupl-12).

Questions regarding the Diia project, the License and any re-use should be directed to [modt.opensource@thedigital.gov.ua](mailto:modt.opensource@thedigital.gov.ua).
