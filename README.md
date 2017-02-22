# Movie Scout
##  Description
Android application which provides users a way to search through movies and view there reviews and trailers.

##  Contribution
This application is being developed for a Nano-Degree program which I am currently enrolled in for Udacity.  Because of this I am not looking for assistance at this time.  Thank you for your interest in the project, perhaps in the future once I am finished with the program we can refine the app for an open-source project.

##  Target SDK
Since the requirements for the app did not specify a target SDK, I've decided to target Android 5.0 (Lollipop) as the minimum supported version.

This choice was made after reviewing the [Android Dashboard](https://developer.android.com/about/dashboards/index.html) which shows that most users are running newer versions of Android and reading about several articles explaining of the various security issues with Android Kitkat and lower.  Also since Google Materials seems to be supported by version 5.0 and up, this is what clinched the version for me.

##  Configurations
###  Movie DB API Keys
In order to add your API v3/v4 key for use with the app you will need to open app/src/main/res/xml/configurations.xml and fill out the configurations for apiKeyV3 and apiKeyV4.  Once specified the app will be able to make request to the RESTful server.

##  Tested VM's
The applicaton has been tested on Android x86 emulators utilizing Android Lollipop, Marshmellow and Nougat.
