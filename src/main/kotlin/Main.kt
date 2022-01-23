import com.adamratzman.spotify.SpotifyAppApi
import com.adamratzman.spotify.SpotifyUserAuthorization
import com.adamratzman.spotify.models.SpotifyPublicUser
import com.adamratzman.spotify.models.SpotifySearchResult
import com.adamratzman.spotify.models.SpotifyUri
import com.adamratzman.spotify.spotifyAppApi
import com.adamratzman.spotify.spotifyClientApi
import com.adamratzman.spotify.utils.Market
import java.lang.Math.floor

suspend fun main(args: Array<String>) {
    val builtApi = buildMyApi()
    val searchQuery = findQuery()

    // The library I'm using makes the results of each query type its
    // own data type. This meant that user searching was different from
    // track searching. This way or parsing isn't the most elegant, but
    // it works.
    if (searchQuery.contains("user")) {
        val userQuery = searchQuery.substringAfter(":")
        val searchResults = searchSpotifyUser(builtApi, userQuery)

        // Error handling in case the user does not exist.
        val bool = if (searchResults == null) {
            println()
            println("There is no user with this name.")
        } else {
            val cleanResults = parseUserResults(searchResults)
            println(cleanResults)
        }

    } else {
        val searchResults = searchSpotify(builtApi, searchQuery)
        val cleanResults = parseSearchResults(searchResults)

        // Wanted to make one call outside of if statement, but the
        // variable can't be seen.
        // Currently the print is inside the parse function.
        // println(cleanResults)
    }
}

/// Pulls the developer ClientID and ClientSecret tokens provided
/// by Spotify and builds them into an object that can easily
/// call public Spotify APIs.
suspend fun buildMyApi (): SpotifyAppApi {
    val clientID = "47a94c257a514a73909a2f544d9d2641"
    val clientSecret = "77479014b36a4489872c486853c5b1c3"

    val api = spotifyAppApi(clientID, clientSecret).build()

    return api;
}

/// Basic option table to tell user what searches they can do.
fun displaySearchOptions() {
    println()
    println("==================")
    println("(1) Artist Name")
    println("(2) Track Name")
    println("(3) User Profile")
    println("==================")
    println()
}

/// Asks user for query type and query value and returns them in a
/// string that can be read by the API call.
fun findQuery(): String {
    while (true) {
        displaySearchOptions()
        print("Enter the number that matches your desired search: ")
        val searchType = readLine()!!
        println()

        // Used a when instead of if else if statements because it is
        // much cleaner and easier to understand.
        when (searchType) {
            "1" -> {
                print("Enter the name of the artist: ")
                val userSearch = readLine()!!
                return "artist:$userSearch"
            }
            "2" -> {
                print("Enter the name of the track: ")
                val userSearch = readLine()!!
                return "track:$userSearch"
            }
            "3" -> {
                print("Enter the name of the user: ")
                val userSearch = readLine()!!
                return "user:$userSearch"
            }
            else -> continue
        }
    }
}

// Performs Spotify database query for queries related to track information. Returns
// the results as a SpotifySearchResult object.
suspend fun searchSpotify(api: SpotifyAppApi, searchQuery: String): SpotifySearchResult {
    return api.search.searchAllTypes(searchQuery, 50, 1, market = Market.US)
}

// Performs Spotify database query for queries related to user information. Returns
// the results as a SpotifyPublicUser object.
suspend fun searchSpotifyUser(api: SpotifyAppApi, userQuery: String): SpotifyPublicUser? {
    return api.users.getProfile(userQuery)
}

// Parses through all the results of a track search and presents them in a way
// that is digestible by a person.
fun parseSearchResults(searchResults: SpotifySearchResult): List<List<String>> {
    var fullResultSet: MutableList<List<String>> = mutableListOf(listOf())

    for (t in searchResults.tracks!!.items) {
        var singleResultSet = mutableListOf<String>()
        singleResultSet.add(t.artists[0].name)
        singleResultSet.add(t.name)
        val timeInSeconds = (t.length/1000).toInt()
        val minutes = (timeInSeconds/60).toInt()
        val remainderSeconds = (timeInSeconds%60).toInt()
        val timeString = "$minutes:$remainderSeconds"
        singleResultSet.add(timeString)
        singleResultSet.add(t.externalUrls.spotify.toString())

        fullResultSet.add(singleResultSet)
    }

    println()
    for (printedList in fullResultSet) {
        // For some reason the first list is empty every time. So this
        // just skips it.
        if (printedList.isNullOrEmpty()) {
            continue
        }
        println(printedList)
    }

    return fullResultSet
}

fun parseUserResults(searchResults: SpotifyPublicUser): List<String> {
    var userProfileInfo: MutableList<String> = mutableListOf(String())

    userProfileInfo.add(searchResults.displayName.toString())
    userProfileInfo.add(searchResults.id)
    userProfileInfo.add(searchResults.followers.total.toString())
    userProfileInfo.add(searchResults.externalUrls.spotify.toString())

    return userProfileInfo
}
//    val apiResults =
//
////    println(apiResults.tracks?.items?.get(0)?.name)
////    println(api.token)
////    println(api.browse.getNewReleases())
//
//    for (t in apiResults.tracks!!.items) {
//        val artistName = t?.artists[0].name
//        val trackName = t?.name
//        val trackId = t?.id
//        val trackSomething = t?.isPlayable
//
//
//
//        println(trackSomething)
//
//        println(artistName)
//    }
//    val thing = (api.users.getProfile("chvia223"))
//    println("${api!!::class.simpleName}")
//}

