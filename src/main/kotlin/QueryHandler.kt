class QueryHandler {
    init {

    }

    fun searchAgainQuery(): Boolean {
        println()
        print("Would you like to make another search? (Y/n) >")
        val input = readLine()

        return when (input) {
            "y" -> true
            "Y" -> true
            else -> false
        }
    }

    /// Asks user for query type and query value and returns them in a
    /// string that can be read by the API call.
    fun findQuery(displayHandler: DisplayHandler): String {
        while (true) {
            displayHandler.displaySearchOptions()
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
}