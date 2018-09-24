# Bosque protector servicios

This app allows to send the audio files in the device to the server located in ESPOL. 

## System requirements

- Minimum Android API: 10 <br/>
- Android version: 2.3.5

## Libraries

The libraries used were:

**- JSONObject:** Library that allows to create JSON objects with the data that's going to be send to the server. <br/>
**- OkHTTP:** This library is used to send the JSON object to the server.

## Directories

The repository has the following directory structure (Android setting files and similar won't be mentioned):

- java directory:

  - Activities: <br/>
    **- MainActivity:** Starts the service for the first time. <br/>
    **- SettingsActivity:** Starts the preference menu and detect the preferences' changes made by the user. <br/>
  - BroadcastReceivers: <br/>
    **- BootBroadcastReceiver:** Starts the service after the device's restart. <br/>
  - Services: <br/>
    **- SendingAudiosService:** Detects the internet connection and manage the upload intents. <br/>
  - Utils: <br/>
    **- FolderIterator:** Access the audio files storaged in the device. <br/>
    **- Identifiers:** Contains the global variables and static variables such as server url, APIKey, etc. <br/>
    **- Utils:** Validates the device to the service, creates the JSON object with the audio file and other information and sends it to the server. <br/>
   
- res directory (layouts):

  - layout: <br/>
    **- activity_main:** Blank layout to start the app. <br/>
  - menu: <br/>
    **- menu_main:** Contains the reboot and preferences options. <br/>
  - xml: <br/>
    **- prefs:** Allows to see and modify the preferences. <br/>
    
## Preferences

There is a preferences menu that allows to modify the behaviour of the app. This menu includes the following parameters:

**- APIKey:** Key used to authenticate the station in the server. This parameter can't be modified. <br/>
**- Number of intents:** Number of intents the app is allowed to try to upload the data to the server. <br/>
**- Time between intents:** Time in seconds between each upload intent. <br/>
**- Sleep time:** Time in seconds between the start of each execution of the service. <br/>
**- Destroy Audio:** If this parameter is activated, the audio file will be deleted from the device after it's been successfully uploaded to the server.

**NOTE:** Due to the Android version of the devices, alarms were used to send the audios. This makes necessary to change the sleep time with caution because it could make the app to try to upload the same file twice in case there's a slow internet connection and the alarm starts when it's uploading a file.
