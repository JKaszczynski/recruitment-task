App starts at port 8080.

There's an endpoint in the app under path: "api/temperature/average/{city}".
The endpoint returns the yearly average temperatures for a given city in the format array of objects with the following fields: year, averageTemperature.


In the resource folder there is an example csv file called "example_file.csv".
To use own source file, paste the path to desired file in the "application.properties" under property "kyotu.temperature.file.path".

At the application startup cache is being prepopulated, which may result in a long application startup time based on the source file size.
Approximately every 60 seconds app checks if source file has changed, and if so, app repopulates the cache.