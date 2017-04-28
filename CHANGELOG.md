# Change Log
All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](http://keepachangelog.com/) 
and this project adheres to [Semantic Versioning](http://semver.org/).

## [UNRELEASED]

###  Added

-  Will display the backdrop image for the selected movie where the app bar used to be
-  Can view trailers for the selected movie
-  User reviews
-  Users Favorites

###  Changed

-  Gridlayout column count differs between portrait and landscape views
-  Made enhancements which were suggested by Udacity
-  Sort options will be hidden when there is no network connection
-  API Key passed through strings resource and removed from settings

## [v1.2.0] - 2017-04-01

###  Changed

-  Removed the configurations reader package.
-  Users can now enter movie db api key on the app itself through the
settings page
-  AsyncTask has been replaced by a LoadManager.
-  The state of the application will be saved and restored as the lifecycle for the app changes.
-  Movie no longer requires a context to be provided as an instance field

## [v1.1.1] - 2017-02-21

###  Changed

-  Renamed the Rating tag to Vote Average to make it's information more clear.
-  Cleaned up the interface for movie lists

## [v1.1.0] - 2017-02-21

### Added

-  Ability to scroll through all Movies under MovieDB API

### Changed

-  Enhanced UI of Movie Detail page
-  Display language, rating and popularity details
-  Repaired alignment of posters in movie list

## [v1.0.0] - 2017-02-19

###  Added

-  Read information from Movie DB API resource
-  Displays a list of Movies that can be sorted by either Popularity or Rating
-  Display information on a specific movie when clicked

## [v0.0.1] - 2017-02-09

### Added

-  Created Android Project with Android Studio
-  Added README file
-  Added a Change Log

[v1.0.0]: https://github.com/travy/movie-scout/compare/v0.0.1...v1.0.0
[v1.1.0]: https://github.com/travy/movie-scout/compare/v1.0.0...v1.1.0
[v1.1.1]: https://github.com/travy/movie-scout/compare/v1.1.0...v1.1.1
[v1.2.0]: https://github.com/travy/movie-scout/compare/v1.1.1...v1.2.0
[UNRELEASED]: https://github.com/travy/movie-scout/compare/v1.2.0...develop
