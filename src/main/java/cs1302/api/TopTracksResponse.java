package cs1302.api;

import java.util.List;

public class TopTracksResponse {
    TopTracks toptracks;
}

class TopTracks {
    List<Track> track;
}

class Track {
    Artist artist;
    String name;
    String playcount;
    String listeners;
    String streamable;
    String url;

}
