package cs1302.api;

import java.util.List;

/**
 * Stores results from Itunes API.
 */
public class ItunesResponse {
    List<ItunesResult> results;


    /**
     * Stores names of image urls.
     */
    static class ItunesResult {
        String artworkUrl100;
    }
}
