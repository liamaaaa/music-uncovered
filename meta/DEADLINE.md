# Deadline

Modify this file to satisfy a submission requirement related to the project
deadline. Please keep this file organized using Markdown. If you click on
this file in your GitHub repository website, then you will see that the
Markdown is transformed into nice-looking HTML.

## Part 1.1: App Description

> Please provide a friendly description of your app, including
> the primary functions available to users of the app. Be sure to
> describe exactly what APIs you are using and how they are connected
> in a meaningful way.

> **Also, include the GitHub `https` URL to your repository.**

This app takes user input through the search bar to find the most popular artist related to that
    search using MusicBrainz API of musical artists. Then, after clicking the "Go" button, a user will
    be met with basic information about the artist, including their stage name, country of origin, and
    top "tags", which are words relating to their music or performance style. Users have the option to
    click on 3 buttons. The first one adds the top 5 most popular tracks for the saved artist to the information
    box on the side of the scene, using the LastFM API to search for top tracks from the artist saved from
    the MusicBrainz API. The second button adds the top 3 most popular albums/EPs/Singles from the saved
    artist to the information box. The third button adds the top 3 most popular related artists to the saved
    artist to the information box as well. These three sets of information can be added to the screen as many
    times as the user would like. There is also a "clear" button on the top right to clear the information box
    of any previously existing text. When a user searches up an artist, the right-side of the screen will show
    a related release artwork from the artist, pulled from the iTunes API.

## Part 1.2: APIs

> For each RESTful JSON API that your app uses (at least two are required),
> include an example URL for a typical request made by your app. If you
> need to include additional notes (e.g., regarding API keys or rate
> limits), then you can do that below the URL/URI. Placeholders for this
> information are provided below. If your app uses more than two RESTful
> JSON APIs, then include them with similar formatting.

### API 1 -- MusicBrainz API

```
https://musicbrainz.org/ws/2/artist?query=artist%3AAriana+Grande&limit=1&fmt=json
```


### API 2 - LastFM API

```
http://ws.audioscrobbler.com/2.0/?method=artist.gettoptracks&artist=Ariana+Grande&api_key=MY_API_KEY&format=json
```

### API 3 - iTunes API

```
https://itunes.apple.com/search?term=Ariana+Grande&limit=200&media=music
```


## Part 2: New

> What is something new and/or exciting that you learned from working
> on this project?

I think I developed a much better understanding of JavaFX and how to access different APIs.
    I know know how to create related classes for json responses from an Http Response myself!
    I also learned how to apply different databases to query other databases.

## Part 3: Retrospect

> If you could start the project over from scratch, what do
> you think might do differently and why?

I'd work on my efficiency and simplification of the code. There's definitely more helper methods
    that could have been implemented to get this to work. I'd also try finding a different API to
    retrieve images from. I tried using LastFM, but they blocked image usage and the iTunes images
    are blurred.
