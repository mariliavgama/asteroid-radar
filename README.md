# Asteroids Radar

Asteroid Radar is an app to view the asteroids detected by NASA that pass near Earth, you can view all the detected asteroids given a period of time with data such as the size, velocity, distance to earth and if they are potentially hazardous. In this project, I demo skills such as fetching data from the internet, saving data to a database, and displaying the data in a clear, compelling UI.

Used the NEoWs API which is a free, open source API provided by NASA JPL Asteroid team, as they explain it: “Is a RESTful web service for near earth Asteroid information. With NeoWs a user can: search for Asteroids based on their closest approach date to Earth, lookup a specific Asteroid with its NASA JPL small body id, as well as browse the overall data-set.”

The project has two screens: a Main screen with a list of detected asteroids with close approach date within this week and a Details screen that displays the data of that asteroid once it's selected in the Main screen list. The main screen will also show the NASA image of the day to make the app more striking. If the image of the day feed returns a video, no image is displayed.

When asteroids are downloaded, they are saved in the local database. By default, asteroids are fetched and displayed from the database sorted by date, only from today onwards, ignoring asteroids before today. Menu items allow viewing asteroid from today, this week or all saved asteroids. There's also a menu item to delete all asteroids. The app also caches the asteroids data by using a worker, downloading and saving today's asteroids in background once a day when the device is charging and wifi is enabled.

## Technologies Used

* Retrofit library to download the data from the Internet
* Moshi to convert the JSON data we are downloading to usable data in the form of custom classes
* WorkManager to download and save asteroids in the background
* Glide library to download and cache images
* RecyclerView to display the asteroids in a list
* ViewModel
* Room
* LiveData
* Data Binding
* Navigation

## Screenshots

![Screenshot_20241001_213619](https://github.com/user-attachments/assets/f476a3c5-fff4-4dae-b32d-982496a01492)
![Screenshot_20241001_213632](https://github.com/user-attachments/assets/bf4f1f9a-2a95-4032-82de-98aa92954894)
![Screenshot_20241001_213644](https://github.com/user-attachments/assets/1371c5c2-ef0a-474b-8d35-9b578aabeb3d)
![Screenshot_20241001_213654](https://github.com/user-attachments/assets/c25cdfcd-0fe6-4772-aeb3-7d2f76836a7f)
