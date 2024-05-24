# Bus Tracking Application
![image](https://github.com/bypriyan/Bus-Tracking-Application/assets/86232180/7a027e51-beb4-4ce4-a0ea-1a793b0ac04c)
![image](https://github.com/bypriyan/Bus-Tracking-Application/assets/86232180/b0837016-7bfd-4f52-afe0-15cd1d0619c1)
![image](https://github.com/bypriyan/Bus-Tracking-Application/assets/86232180/15363cbc-96b0-4695-8a66-ca1910bbc410)

## Overview
The **Bus Tracking Application** is an Android application developed to provide real-time tracking of buses. It allows drivers to share their live location and enables users to track these buses in real-time. The application leverages various technologies such as Firebase Realtime Database for live data updates and Firebase Authentication for phone number verification.

## Features
- **Real-time Bus Tracking**: Users can track the live location of buses.
- **Driver Location Sharing**: Drivers can share their current location with the users.
- **Phone Number Authentication**: Secure login for users and drivers using Firebase Authentication.
- **MVVM Architecture**: The project is structured following the Model-View-ViewModel (MVVM) architecture.
- **LiveData and Coroutines**: Utilized for handling data and asynchronous operations efficiently.

## Technologies Used
- **Kotlin**: Programming language used for Android development.
- **XML**: Used for designing UI layouts.
- **Firebase Realtime Database**: For storing and retrieving live location data.
- **Firebase Authentication**: For phone number authentication.
- **MVVM Architecture**: To separate concerns and manage code better.
- **LiveData**: To handle live data updates.
- **Coroutines**: For managing background threads and asynchronous tasks.

## Screenshots
![Driver Location Sharing](https://github.com/bypriyan/Bus-Tracking-Application/assets/86232180/c79823e4-adc4-4de0-93b8-f5c61416d2df)
![User Tracking](https://github.com/bypriyan/Bus-Tracking-Application/assets/86232180/a86a729d-6d73-423e-aa19-06be6e27a568)
![Phone Authentication](https://github.com/bypriyan/Bus-Tracking-Application/assets/86232180/188f5037-a378-4ad7-b284-fdfbd0cfa0dc)
![Another Screenshot](https://github.com/bypriyan/Bus-Tracking-Application/assets/86232180/e243ff07-7599-42c6-bd94-1bbfe4d3233c)

## Installation and Setup
1. **Clone the Repository**
    ```bash
    git clone https://github.com/bypriyan/bus-tracking-application.git
    cd bus-tracking-application
    ```

2. **Open the Project in Android Studio**
    - Open Android Studio.
    - Select `Open an existing project`.
    - Navigate to the cloned repository directory and select it.

3. **Configure Firebase**
    - Go to the [Firebase Console](https://console.firebase.google.com/).
    - Create a new project or use an existing project.
    - Add an Android app to your Firebase project.
    - Register your app with the package name specified in your project.
    - Download the `google-services.json` file.
    - Place the `google-services.json` file in the `app` directory of your project.

4. **Build the Project**
    - Sync the project with Gradle files.
    - Build and run the application on your device or emulator.

## Usage
1. **Driver**
    - Sign in using phone number authentication.
    - Share current location with users.
    
2. **User**
    - Sign in using phone number authentication.
    - Track the live location of buses shared by drivers.

## Contributing
Contributions are welcome! If you have any ideas, suggestions, or issues, please feel free to open an issue or submit a pull request.

## License
This project is licensed under the MIT License. See the [LICENSE](LICENSE) file for more details.

## Contact
For any queries or support, please contact:
- Email: 104priyanshu@gmail.com
- GitHub: [bypriyan](https://github.com/bypriyan)

---

Thank you for using the Bus Tracking Application! Happy tracking!
