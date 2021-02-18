Storymaker, a Java based Android app under development, is a social app for story telling / creation
made up of text,voice,pictures and video. Story telling here means describing something by means of
assembling one or more of text, voice, video and still picture (the artifacts) into a playback sequence that
represents a message or story. It can be described as a kind of "MMS on steroids" with several twists.

Artifacts are basis of story elements that visually appear and disappear at creator specified
points in time during playback. StoryMaker makes use of a "Scene" concept where multithreaded scheduling of event playback can occur in an overlapped fashion over time where the playback machine "Progressor" manages the progression of the scripted story (artifact playback events that begin and end at specific points in time as specified by the script).

A story is shared as a script that results in playback by the StoryMaker app on the recipient side.  Both someone creating and receiving a story use the same StoryMaker app. This means distribution of the story to friends and others will occur in a very lightweight way where only an URL to the script is passed around. The StoryMaker app is registered as handler of the content type that the StoryMaker script represents. When run the script specifies is processed by StoryMaker which assembles and plays the story at runtime.
Artifacts that make up the story is retrieved on demand by StoryMaker from servers in the cloud.

A major technical challenge of this principle is to assure that delays in retrieving artifacts
needed during playback is not causing playback delays/interruptions. Prefetch operations and caching is still subject to evaluation/experimentation.

A story can also be shared to an NFC tag and in a future versions other proximity triggers such as iBeacons. It means that when a user´s phone that has Storymaker installed comes in proximity of a Storymaker written NFC tag the tagged story will play.

StoryMaker shall include an easy to manage functionality for gathering and cataloging of produced artifacts so that such story material is conveniently at hand when assembling a story.  It shall also be possible to share such an artifact repository among users by means of links to cloud storage.

This app is currently in a stage of research/principal evaluation where certain aspects of the app is
staged for test runs of separate functions for evaluation. The complete app is planned for Q2 2017.
(Note in July 2020: A slight delay occurred. But idea strong enough to motivate renewed interest).

Unimplemented so far:
- The captivating story teller editor
- A uniform artifact management activity
- A richer set of transition animations
- Social sharing mechanisms using Google Cloud Messaging
- NFC functionality
- Some video recording features
- Some still photo features
- Voice recording
