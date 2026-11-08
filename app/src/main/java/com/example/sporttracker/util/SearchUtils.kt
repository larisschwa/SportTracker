package com.example.sporttracker.util

object SearchUtils {

    private val CITY_ASSOCIATIONS = mapOf(
        "seattle" to listOf("Seattle", "Kraken", "Seahawks", "Mariners", "Sounders", "Storm"),
        "minnesota" to listOf("Minnesota", "Wild", "Gophers", "Vikings", "Timberwolves", "Twins", "United"),
        "twin cities" to listOf("Minnesota", "Wild", "Gophers", "Vikings", "Timberwolves", "Twins", "United"),
        "minneapolis" to listOf("Minnesota", "Wild", "Gophers", "Vikings", "Timberwolves", "Twins", "United"),
        "st paul" to listOf("Minnesota", "Wild", "Gophers", "Vikings", "Timberwolves", "Twins", "United"),
        "st. paul" to listOf("Minnesota", "Wild", "Gophers", "Vikings", "Timberwolves", "Twins", "United"),
        "chicago" to listOf("Chicago", "Blackhawks", "Bears", "Bulls", "Cubs", "White Sox", "Fire"),
        "boston" to listOf("Boston", "New England", "Bruins", "Celtics", "Red Sox", "Patriots"),
        "new england" to listOf("Boston", "New England", "Patriots", "Bruins", "Celtics", "Red Sox"),
        "denver" to listOf("Denver", "Colorado", "Avalanche", "Broncos", "Nuggets", "Rockies"),
        "colorado" to listOf("Colorado", "Denver", "Avalanche", "Broncos", "Nuggets", "Rockies"),
        "dallas" to listOf("Dallas", "Texas", "Stars", "Cowboys", "Mavericks", "Rangers"),
        "detroit" to listOf("Detroit", "Red Wings", "Lions", "Pistons", "Tigers"),
        "los angeles" to listOf("Los Angeles", "Lakers", "Clippers", "Dodgers", "Rams", "Chargers", "Kings", "Angels", "Galaxy", "LAFC"),
        "la" to listOf("Los Angeles", "Lakers", "Clippers", "Dodgers", "Rams", "Chargers", "Kings"),
        "anaheim" to listOf("Anaheim", "Ducks", "Angels"),
        "new york" to listOf("New York", "Brooklyn", "Rangers", "Islanders", "Knicks", "Nets", "Yankees", "Mets", "Giants", "Jets"),
        "nyc" to listOf("New York", "Brooklyn", "Rangers", "Islanders", "Knicks", "Nets", "Yankees", "Mets", "Giants", "Jets"),
        "philadelphia" to listOf("Philadelphia", "Flyers", "Eagles", "76ers", "Phillies", "Union"),
        "philly" to listOf("Philadelphia", "Flyers", "Eagles", "76ers", "Phillies"),
        "pittsburgh" to listOf("Pittsburgh", "Penguins", "Steelers", "Pirates"),
        "vegas" to listOf("Vegas", "Las Vegas", "Golden Knights", "Raiders", "Aces"),
        "las vegas" to listOf("Vegas", "Las Vegas", "Golden Knights", "Raiders", "Aces"),
        "san francisco" to listOf("San Francisco", "San Jose", "Golden State", "49ers", "Giants", "Warriors", "Sharks"),
        "sf" to listOf("San Francisco", "49ers", "Giants", "Warriors"),
        "bay area" to listOf("San Francisco", "San Jose", "Golden State", "49ers", "Sharks", "Warriors", "Giants"),
        "san jose" to listOf("San Jose", "Sharks"),
        "tampa" to listOf("Tampa", "Tampa Bay", "Lightning", "Buccaneers", "Rays"),
        "tampa bay" to listOf("Tampa", "Tampa Bay", "Lightning", "Buccaneers", "Rays"),
        "miami" to listOf("Miami", "Florida", "Dolphins", "Heat", "Marlins", "Panthers", "Inter Miami"),
        "florida" to listOf("Florida", "Miami", "Tampa", "Panthers", "Lightning", "Dolphins", "Heat", "Marlins", "Rays"),
        "phoenix" to listOf("Phoenix", "Arizona", "Cardinals", "Suns", "Diamondbacks"),
        "arizona" to listOf("Arizona", "Phoenix", "Cardinals", "Suns", "Diamondbacks"),
        "milwaukee" to listOf("Milwaukee", "Wisconsin", "Bucks", "Brewers"),
        "green bay" to listOf("Green Bay", "Wisconsin", "Packers"),
        "wisconsin" to listOf("Wisconsin", "Green Bay", "Milwaukee", "Badgers", "Packers", "Brewers", "Bucks"),
        "kansas city" to listOf("Kansas City", "Chiefs", "Royals"),
        "kc" to listOf("Kansas City", "Chiefs", "Royals"),
        "st louis" to listOf("St. Louis", "Blues", "Cardinals"),
        "st. louis" to listOf("St. Louis", "Blues", "Cardinals"),
        "toronto" to listOf("Toronto", "Maple Leafs", "Raptors", "Blue Jays"),
        "vancouver" to listOf("Vancouver", "Canucks", "Whitecaps"),
        "montreal" to listOf("Montreal", "Canadiens"),
        "washington" to listOf("Washington", "Capitals", "Commanders", "Wizards", "Nationals"),
        "dc" to listOf("Washington", "Capitals", "Commanders", "Wizards", "Nationals"),
        "houston" to listOf("Houston", "Texans", "Rockets", "Astros"),
        "atlanta" to listOf("Atlanta", "Falcons", "Hawks", "Braves"),
        "cleveland" to listOf("Cleveland", "Browns", "Cavaliers", "Guardians"),
        "cincinnati" to listOf("Cincinnati", "Bengals", "Reds"),
        "baltimore" to listOf("Baltimore", "Ravens", "Orioles"),
        "indianapolis" to listOf("Indianapolis", "Indiana", "Colts", "Pacers"),
        "indiana" to listOf("Indiana", "Indianapolis", "Colts", "Pacers"),
        "nashville" to listOf("Nashville", "Tennessee", "Predators", "Titans"),
        "carolina" to listOf("Carolina", "Charlotte", "Hurricanes", "Panthers"),
        "charlotte" to listOf("Charlotte", "Carolina", "Hurricanes", "Panthers", "Hornets"),
        "utah" to listOf("Utah", "Salt Lake", "Jazz", "Utah Hockey Club"),
        "salt lake" to listOf("Salt Lake", "Utah", "Jazz", "Utah Hockey Club"),
        "buffalo" to listOf("Buffalo", "Sabres", "Bills"),
        "ottawa" to listOf("Ottawa", "Senators"),
        "calgary" to listOf("Calgary", "Flames"),
        "edmonton" to listOf("Edmonton", "Oilers"),
        "winnipeg" to listOf("Winnipeg", "Jets"),
        "new orleans" to listOf("New Orleans", "Saints", "Pelicans"),
        "portland" to listOf("Portland", "Trail Blazers", "Timbers"),
        "san diego" to listOf("San Diego", "Padres"),
        "supercross" to listOf("Supercross", "AMA Supercross", "Monster Energy", "SMX", "SuperMotocross"),
        "motocross" to listOf("Motocross", "Pro Motocross", "AMA Motocross", "MX", "SuperMotocross", "SMX"),
        "pro motocross" to listOf("Motocross", "Pro Motocross", "AMA Motocross", "MX", "SuperMotocross", "SMX"),
        "mx" to listOf("Motocross", "Pro Motocross", "AMA Motocross", "MX", "SuperMotocross", "SMX"),
        "supermotocross" to listOf("SuperMotocross", "SMX", "Supercross", "Motocross", "Pro Motocross"),
        "smx" to listOf("SuperMotocross", "SMX", "Supercross", "Motocross", "Pro Motocross"),
    )

    /**
     * Given a search query, returns a de-duplicated list of associated search terms.
     * E.g. "seattle" -> ["Seattle", "Kraken", "Seahawks", "Mariners", "Sounders", "Storm"]
     * E.g. "minnesota" -> ["Minnesota", "Wild", "Gophers", "Vikings", "Timberwolves", "Twins", "United"]
     */
    fun getAssociatedTerms(query: String): List<String> {
        val trimmed = query.trim()
        if (trimmed.isEmpty()) return emptyList()

        val lower = trimmed.lowercase()
        val terms = linkedSetOf<String>()
        terms.add(trimmed)

        // Check direct key match or substring match in city map
        for ((cityKey, associated) in CITY_ASSOCIATIONS) {
            if ((lower == cityKey) || lower.contains(cityKey) || cityKey.contains(lower)) {
                terms.addAll(associated)
            } else {
                // Check if the query matches one of the associated team nicknames (reverse lookup)
                val matchesNick = associated.any { it.equals(lower, ignoreCase = true) }
                if (matchesNick) {
                    terms.addAll(associated)
                }
            }
        }

        return terms.toList()
    }
}
