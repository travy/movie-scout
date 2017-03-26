# Movie Scout

##  Description

Android application which provides users a way to search through movies and view there reviews and trailers.

##  Configurations

###  Movie DB API Keys

Movie Scout will automatically prompt you for the api key when you start the application if it has not yet been provided.  To perform a simple copy and paste on the computer, open the file `strings.xml` and specify the api key for the string keys `movie_db_v3_settings_default` and `movie_db_v4_settings_default`.  This will use the key you copy and pasted as the default value.  If you run the app before doing this, you will need to uninstall the app on the device to clear out the configuration file; otherwise, you can just type the keys into the app.

##  Tested VM's

The applicaton has been tested on Android x86 emulators utilizing Android Lollipop, Marshmellow and Nougat.

##  Contribution

This application is being developed for a Nano-Degree program which I am currently enrolled in for Udacity.  Because of this I am not looking for assistance at this time.  Thank you for your interest in the project, perhaps in the future once I am finished with the program we can refine the app for an open-source project.

##  Target SDK

Since the requirements for the app did not specify a target SDK, I've decided to target Android 5.0 (Lollipop) as the minimum supported version.

This choice was made after reviewing the [Android Dashboard](https://developer.android.com/about/dashboards/index.html) which shows that most users are running newer versions of Android and reading about several articles explaining of the various security issues with Android Kitkat and lower.  Also since Google Materials seems to be supported by version 5.0 and up, this is what clinched the version for me.
