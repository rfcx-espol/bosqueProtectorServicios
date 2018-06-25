# Bosque protector servicios

These proyect have the objective to contain all the new functionalities added by ESPOL's people.

## Module SendingAudio

These module is to send the audios that are in the phone to the server located in ESPOL. For these we used a service named SendingAudioService that used 
OkHTTP library to communicate with the server. Also there is a broadcast receiver that initialize the service at boot.

Also there is an app which function is to modify some features of the service

* APIKey

Key used to authenticate the station in the server

* NumberOfIntents

Number of intents allowed to try to upload to the server

* SendingAudioTime

Time between the service is "restarted"

* IsOnService

Switch to deactivate or activate the service

* OnDestroyAudio

Switch to delete the audios, if they are correctly sent to the server