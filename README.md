# 🚗 Carpool: Rideshare Application

Carpool is a rideshare application designed to facilitate ridesharing services for the Faculty of Engineering community at Ain Shams University. This app allows students to share rides to and from the university campus, focusing on two main destinations: **Gate 3** and **Gate 4**. The app is built as a trusted, closed community where users must sign in with their `@eng.asu.edu.eg` email address. Carpool is operated by students for students, with a unique strategy for recruiting drivers and managing rides.

This project includes two separate apps: one for **riders** 🚶‍♂️ and one for **drivers** 🚕. Both apps are implemented in **Java** using **Android Studio** and utilize **Firebase Authentication**, **Firebase Realtime Database**, and **Room Database** for data storage and management.

---

## ✨ Features

### For Riders:
- **🔐 Login/Signup**: Users can sign up and log in using their `@eng.asu.edu.eg` email address via Firebase Authentication.
- **📍 Available Routes**: View a list of available routes to and from Ain Shams campus using a RecyclerView.
- **🕒 Ride Reservation**: Reserve a seat for morning (7:30 AM) or afternoon (5:30 PM) rides before the specified deadlines.
- **🛒 Review Rides and Payment**: Review ride details and make payments.
- **📜 Order History**: Track ride status and view order history.
- **🔄 Real-Time Updates**: Firebase Realtime Database is used to update ride status and availability.

### For Drivers:
- **🌐 Web Application**: Drivers can confirm orders and update ride status via a web application.
- **✅ Order Confirmation**: Drivers must confirm orders before 11:30 PM for morning rides and before 4:30 PM for afternoon rides.
- **🔄 Real-Time Updates**: Firebase Realtime Database is used to sync ride data between the rider and driver apps.

---

## 📸 Screenshots
Please refer to the project documentation for detailed screenshots: [Project Documentation.pdf]

---

## 🛠️ Technologies Used

- **Frontend**: XML for UI design, Java for backend logic.
- **Backend**: Firebase Authentication, Firebase Realtime Database, Room Database.
- **IDE**: Android Studio.
---

## 🚀 Installation

### Prerequisites
- Android Studio (latest version recommended).
- A physical Android device or emulator with API level 21 or higher.
- A Firebase project set up with Authentication and Realtime Database enabled.

### Steps to Run the Project
1. **Clone the Repository**:
   ```bash
   git clone https://github.com/Basmala-Abdullah/carpool-app.git
   ```
2. **Open the Project in Android Studio**:
   - Open Android Studio and select "Open an Existing Project."
   - Navigate to the cloned repository and open the project.

3. **Set Up Firebase**:
   - Go to the [Firebase Console](https://console.firebase.google.com/).
   - Create a new project and add an Android app.
   - Download the `google-services.json` file and place it in the `app` directory of the project.
   - Enable **Firebase Authentication** and **Firebase Realtime Database** in the Firebase Console.

4. **Build and Run the App**:
   - Connect your Android device or start an emulator.
   - Click on the "Run" button in Android Studio to build and deploy the app.

---

## 🎯 Usage

### For Riders:
1. **Sign up or log in** using your `@eng.asu.edu.eg` email address.
2. **Browse available routes** and select your desired ride.
3. **Reserve your seat** before the deadline (10:00 PM for morning rides, 1:00 PM for afternoon rides).
4. **Review your order** in the cart and complete the payment.
5. **Track your ride status** in the Order History page.

### For Drivers:
1. **Access the application** to view ride requests.
2. **Confirm orders** before the specified deadlines (11:30 PM for morning rides, 4:30 PM for afternoon rides).
3. **Update ride status** as needed.
