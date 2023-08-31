App starts at port 8080.

There's an endpoint in the app under path: "api/temperature/average/{city}".
The endpoint returns the yearly average temperatures for a given city in the format array of objects with the following fields: year, averageTemperature.


In the resource folder there is an example csv file called "example_file.csv".
To use own source file, paste the path to desired file in the "application.properties" under property "kyotu.temperature.file.path".

By default cache is being pre populated on application startup, which may result in a long application startup time. To change the default behavior set property value of "kyotu.temperature.file.init-cache" to "false" in "applicaiton.properties".