
# Notes App

This Android application is a feature-rich notes app, providing users with multiple functionalities, including grammar checking, note saving, and PDF export. It also features a clean, modern UI compatible with edge-to-edge Android devices. 

## Key Features

### 1. **Grammar Check**
- Integrated with **LanguageTool API**, this feature highlights grammatical mistakes in red within the note-taking editor.
- Users can click on the highlighted errors to see suggestions and correct mistakes in real-time.

### 2. **Note Saving**
- Notes are stored in a local **Room Database**, allowing users to create new notes or update existing ones.
- All changes are saved persistently, ensuring no data loss between app sessions.

### 3. **PDF Export**
- Users can easily export notes as PDF documents.
- The title and content are formatted centrally on the PDF, with the document saved using the **MediaStore** API.

### 4. **Edge-to-Edge UI**
- The app leverages the **EdgeToEdge** helper to seamlessly integrate window insets, ensuring a modern, full-screen experience on edge-to-edge displays.

### 5. **User Interaction**
- The app provides a responsive UI that allows users to:
  - Save notes to the database.
  - Convert notes into PDFs.
  - Check grammar errors with real-time feedback and clickable suggestions.

## Firebase Authentication

### 1. **User Registration and Login**
- Users can **register** a new account or **log in** with an existing account using **Firebase Authentication**.
- The app handles errors and logs all registration and login attempts, ensuring a smooth user experience.

### 2. **Input Validation**
- Before registration or login, the app checks that both the email and password fields are filled in, preventing invalid inputs.

### 3. **Persistent Login State**
- Once logged in, the app stores the login state using **SharedPreferences**. This enables the app to check if the user is already logged in on subsequent launches and skip the login screen.

### 4. **Edge-to-Edge UI**
- Consistent with the rest of the app, the **Login** and **Registration** screens also apply window insets for a modern, immersive experience.

## Main Application Features

### 1. **Login and First Launch Handling**
- The app checks if the user is logged in and whether it is their first time launching the app via **SharedPreferences**. This ensures proper user flow and navigation.

### 2. **Floating Action Button (FAB) for Adding Notes**
- The FAB is used to quickly access the **write_note** activity, allowing users to start writing a new note or edit an existing one.

### 3. **RecyclerView Setup**
- The **RecyclerView** is integrated to display all saved notes.
- A **LinearLayoutManager** ensures smooth scrolling and a user-friendly layout for viewing multiple notes.
- Notes are loaded in the background for a more responsive user experience.

### 4. **Menu Button**
- The **Menu** button allows users to access the settings or any other menu-related features from the app's main screen.

## Installation

1. Clone the repository:
   ```
   git clone https://github.com/shreyas6578/NotesAPP.git
   ```
2. Open the project in **Android Studio**.
3. Sync Gradle and build the project.
4. Run the app on an emulator or physical device.

## License
This project is licensed under the MIT License - see the LICENSE file for details.

---

This README provides an overview of the app’s features, focusing on user experience, functionality, and performance. You can add more details depending on the specific features or future updates!
