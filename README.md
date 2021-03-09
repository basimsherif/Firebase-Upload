
### Firebase Image Uploader and Gallery:

### Project Architecture:

* I have used Kotlin as the development language and MVVM as an architecture so that there won't be any tight coupling between components and we can test the code easily
* Firebase is used as the backend
* Hilt is used for Dependency Injection to inject all the required modules
* The app is designed as a single Activity architecture
* A Repository class which act as a bridge between View model and Firebase Data Source
* FirebaseSource class to communicate with Firebase and provide results to Repository class
* A Resource class is used to wrap API response and to handle the response with LiveData
* Data Binding is used to bind data to Recyclerview
* Glide is used for loading images and optimizing memory
* Memory leaks are handled properly
* KTX library is used for avoiding lots of boiler plate codes
* A 3rd party library is used for crop and rotate feature - https://github.com/ArthurHub/Android-Image-Cropper

### Testing Architecture:

* Unit test cases are developed using JUnit
* Instrumentation test cases are added for testing Room Database
* UI Automation test cases are added using Espresso and Page Object Model
* We are using Intent stub for testing gallery upload and camera upload features
* Counting Idling resource is used to wait for asyn operation when running Espresso tests


### More Testing Strategy:

* Need to add more unit test cases to test LiveData observers and Viewmodel using Mocking framework (Mockito, Mockk)
* Need to write more Espresso test cases to verify recycler view images after they are uploaded
* Need to add some Espresso UI test cases for testing network related scenarios using UI Automator