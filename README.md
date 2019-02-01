# TrebleMakerCore
[![Build Status](http://35.185.252.88:8080/buildStatus/icon?job=TrebleMakerCore-BUILD)](http://35.185.252.88:8080/job/TrebleMakerCore-BUILD/)

TrebleMaker.ai is a distributed system that procedurally generates royalty free MIDI files. The generated MIDI files are intended to be used by electronic musicians when composing new music.

*For usage instruction please visit:*
https://github.com/shiehn/TrebleMakerDocker

*import project dependencies by running: './scripts/fetch-deps.sh' from the root of this repo

---

#### The system is composed of five related components:
* **Data Parser**
    * This GoLang app converts the MusicXml dataset into a format consumable by the 'Model Trainers' https://github.com/shiehn/MusicXmlGoParser
     
* **Model Trainers**
    * These SpringBoot Java apps are responsible for consuming the dataset and producing prediction models to be used by TrebleMakerCore & TrebleMakerApi.  Currently there are two trainers, one dedicated to melodies and the other for chord progressions https://github.com/shiehn/chords-to-melody-generator

* **TrebleMakerApi**
    * this API is consumed by the TrebleMakerWeb app and TrebleMakerCore.  Its primary purpose is to provide contextual music 'predictions' for TrebleMakerCore https://github.com/shiehn/TrebleMakerApi

* **TrebleMakerCore**
    * as the name suggests this SpringBoot Java app is the core component that assembles the MIDI and audio files.  During the assembly of the files it will constantly call out to TrebleMakerApi with context asking for musical predictions https://github.com/shiehn/TrebleMaker.git
 
* **TrebleMakerRating**
    * this Angular web GUI offers a way to 'rate' the generated audio files and decide which of files will be uploaded to TrebleMakerWeb

* **TrebleMakerWeb**
    * this Angular web GUI offers a user a way to render, listen and download the generated midi https://github.com/shiehn/TrebleMakerClientSynths.git    
