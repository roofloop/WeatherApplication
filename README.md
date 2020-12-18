# Diary/WeatherApplication
A simple application for making small diary inputs with the added benefit of getting temperature at the time of posting.

At its final polished form this app wouldn't get much larger in complexity as it is right now. This is why we opted to go for an MVC approach when choosing design pattern. The MVC structure suited us very well because of the apps small footprint which wouldn't benefit from the greater modularity that MVVM or even MVP offers.

To be able to offer a very smooth and responsive app we took the one big workload and segmented it into an offline mode as well as online mode thrue the means of caching. By caching the rather small Diary inputs we cuold now offer it even if the user somehow lost internet connection and by trheading the actual gathering and displaying of the data the main thread stays unhindered and clean.

The app further implements usage of unit tests specifically testing the user input fields for easier possible future expansion.
