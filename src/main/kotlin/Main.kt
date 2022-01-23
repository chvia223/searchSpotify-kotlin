suspend fun main(args: Array<String>) {

    val builtAPI = SpotifyApiHandler()
    builtAPI.buildSearchApi()

    val displayHandler = DisplayHandler()
    val queryHandler = QueryHandler()
    var searchAgain = true


    while (searchAgain) {
        val searchQuery = queryHandler.findQuery(displayHandler)

        // Determines whether the query is a user query or track query
        if (searchQuery.contains("user")) {
            val userQuery = searchQuery.substringAfter(":")
            val searchResults = builtAPI.userSearch(userQuery)

            // Error handling in case the user does not exist.
            val bool = if (searchResults == null) {
                println()
                println("There is no user with this name.")
                continue
            } else {
                displayHandler.displayUserSearchResults(searchResults)
            }

        } else {
            val searchResults = builtAPI.trackSearch(searchQuery)
            displayHandler.displayTrackSearchResults(searchResults)
        }

        searchAgain = queryHandler.searchAgainQuery()
    }
}