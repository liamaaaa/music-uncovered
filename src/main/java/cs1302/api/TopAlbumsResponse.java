package cs1302.api;

import java.util.List;

public class TopAlbumsResponse {
    TopAlbums topalbums;
}

class TopAlbums {
    List<Album> album;
}

class Album {
    Artist artist;
    String name;
}
