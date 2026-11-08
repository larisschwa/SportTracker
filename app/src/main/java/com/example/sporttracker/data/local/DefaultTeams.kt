package com.example.sporttracker.data.local

import com.example.sporttracker.data.local.entity.TeamEntity

object DefaultTeams {

    private fun nhlLogo(abbrev: String): String =
        "https://assets.nhle.com/mws/logos/teams/svg/${abbrev}_light.svg"

    private fun sportsDbLogo(badgePath: String): String =
        "https://www.thesportsdb.com/images/media/team/badge/$badgePath"

    val list = listOf(
        // ==================== USER FAVORITES ====================
        TeamEntity("134856", "Minnesota Wild", "MIN", nhlLogo("MIN"), "Men's Hockey", "NHL", "Central", "Team", isFavorite = true),
        TeamEntity("ncaa_minn_gophers", "Minnesota Golden Gophers", "MINN", "https://assets.sportsdata.io/ncaa/team/123/minnesota.png", "Men's Hockey", "NCAA", "Big Ten", "Team", isFavorite = true),
        TeamEntity("series_supercross", "Monster Energy AMA Supercross", "SX", "https://www.thesportsdb.com/images/media/league/badge/supercross.png", "Motorsport", "Supercross", "Championship", "Racing", isFavorite = true),
        TeamEntity("series_motocross", "AMA Pro Motocross Championship", "MX", "https://www.thesportsdb.com/images/media/league/badge/supercross.png", "Motorsport", "Pro Motocross", "Championship", "Racing", isFavorite = true),
        TeamEntity("series_smx", "SuperMotocross World Championship", "SMX", "https://www.thesportsdb.com/images/media/league/badge/supercross.png", "Motorsport", "SuperMotocross", "World Championship", "Racing", isFavorite = true),

        // ==================== MINNESOTA TEAMS ====================
        TeamEntity("134935", "Minnesota Vikings", "MIN", sportsDbLogo("minnesota-vikings.png"), "American Football", "NFL", "NFC North", "Team"),
        TeamEntity("134878", "Minnesota Timberwolves", "MIN", sportsDbLogo("minnesota-timberwolves.png"), "Basketball", "NBA", "Northwest", "Team"),
        TeamEntity("135269", "Minnesota Twins", "MIN", sportsDbLogo("minnesota-twins.png"), "Baseball", "MLB", "AL Central", "Team"),
        TeamEntity("135160", "Minnesota United FC", "MIN", sportsDbLogo("minnesota-united.png"), "Soccer", "MLS", "Western", "Team"),

        // ==================== SEATTLE TEAMS ====================
        TeamEntity("137494", "Seattle Kraken", "SEA", nhlLogo("SEA"), "Men's Hockey", "NHL", "Pacific", "Team"),
        TeamEntity("134934", "Seattle Seahawks", "SEA", sportsDbLogo("seattle-seahawks.png"), "American Football", "NFL", "NFC West", "Team"),
        TeamEntity("135275", "Seattle Mariners", "SEA", sportsDbLogo("seattle-mariners.png"), "Baseball", "MLB", "AL West", "Team"),
        TeamEntity("135154", "Seattle Sounders FC", "SEA", sportsDbLogo("seattle-sounders.png"), "Soccer", "MLS", "Western", "Team"),
        TeamEntity("138120", "Seattle Storm", "SEA", sportsDbLogo("seattle-storm.png"), "Basketball", "WNBA", "Western", "Team"),

        // ==================== COMPLETE 32 NHL TEAMS ====================
        TeamEntity("134857", "Anaheim Ducks", "ANA", nhlLogo("ANA"), "Men's Hockey", "NHL", "Pacific", "Team"),
        TeamEntity("134837", "Boston Bruins", "BOS", nhlLogo("BOS"), "Men's Hockey", "NHL", "Atlantic", "Team"),
        TeamEntity("134848", "Buffalo Sabres", "BUF", nhlLogo("BUF"), "Men's Hockey", "NHL", "Atlantic", "Team"),
        TeamEntity("134853", "Calgary Flames", "CGY", nhlLogo("CGY"), "Men's Hockey", "NHL", "Pacific", "Team"),
        TeamEntity("134847", "Carolina Hurricanes", "CAR", nhlLogo("CAR"), "Men's Hockey", "NHL", "Metropolitan", "Team"),
        TeamEntity("134836", "Chicago Blackhawks", "CHI", nhlLogo("CHI"), "Men's Hockey", "NHL", "Central", "Team"),
        TeamEntity("134844", "Colorado Avalanche", "COL", nhlLogo("COL"), "Men's Hockey", "NHL", "Central", "Team"),
        TeamEntity("134854", "Columbus Blue Jackets", "CBJ", nhlLogo("CBJ"), "Men's Hockey", "NHL", "Metropolitan", "Team"),
        TeamEntity("134846", "Dallas Stars", "DAL", nhlLogo("DAL"), "Men's Hockey", "NHL", "Central", "Team"),
        TeamEntity("134841", "Detroit Red Wings", "DET", nhlLogo("DET"), "Men's Hockey", "NHL", "Atlantic", "Team"),
        TeamEntity("134842", "Edmonton Oilers", "EDM", nhlLogo("EDM"), "Men's Hockey", "NHL", "Pacific", "Team"),
        TeamEntity("134849", "Florida Panthers", "FLA", nhlLogo("FLA"), "Men's Hockey", "NHL", "Atlantic", "Team"),
        TeamEntity("134850", "Los Angeles Kings", "LAK", nhlLogo("LAK"), "Men's Hockey", "NHL", "Pacific", "Team"),
        TeamEntity("134840", "Montreal Canadiens", "MTL", nhlLogo("MTL"), "Men's Hockey", "NHL", "Atlantic", "Team"),
        TeamEntity("134855", "Nashville Predators", "NSH", nhlLogo("NSH"), "Men's Hockey", "NHL", "Central", "Team"),
        TeamEntity("134851", "New Jersey Devils", "NJD", nhlLogo("NJD"), "Men's Hockey", "NHL", "Metropolitan", "Team"),
        TeamEntity("134852", "New York Islanders", "NYI", nhlLogo("NYI"), "Men's Hockey", "NHL", "Metropolitan", "Team"),
        TeamEntity("134839", "New York Rangers", "NYR", nhlLogo("NYR"), "Men's Hockey", "NHL", "Metropolitan", "Team"),
        TeamEntity("134843", "Ottawa Senators", "OTT", nhlLogo("OTT"), "Men's Hockey", "NHL", "Atlantic", "Team"),
        TeamEntity("134835", "Philadelphia Flyers", "PHI", nhlLogo("PHI"), "Men's Hockey", "NHL", "Metropolitan", "Team"),
        TeamEntity("134834", "Pittsburgh Penguins", "PIT", nhlLogo("PIT"), "Men's Hockey", "NHL", "Metropolitan", "Team"),
        TeamEntity("134859", "San Jose Sharks", "SJS", nhlLogo("SJS"), "Men's Hockey", "NHL", "Pacific", "Team"),
        TeamEntity("134858", "St. Louis Blues", "STL", nhlLogo("STL"), "Men's Hockey", "NHL", "Central", "Team"),
        TeamEntity("134845", "Tampa Bay Lightning", "TBL", nhlLogo("TBL"), "Men's Hockey", "NHL", "Atlantic", "Team"),
        TeamEntity("134838", "Toronto Maple Leafs", "TOR", nhlLogo("TOR"), "Men's Hockey", "NHL", "Atlantic", "Team"),
        TeamEntity("140222", "Utah Hockey Club", "UTA", nhlLogo("UTA"), "Men's Hockey", "NHL", "Central", "Team"),
        TeamEntity("134860", "Vancouver Canucks", "VAN", nhlLogo("VAN"), "Men's Hockey", "NHL", "Pacific", "Team"),
        TeamEntity("135899", "Vegas Golden Knights", "VGK", nhlLogo("VGK"), "Men's Hockey", "NHL", "Pacific", "Team"),
        TeamEntity("134861", "Washington Capitals", "WSH", nhlLogo("WSH"), "Men's Hockey", "NHL", "Metropolitan", "Team"),
        TeamEntity("134862", "Winnipeg Jets", "WPG", nhlLogo("WPG"), "Men's Hockey", "NHL", "Central", "Team"),

        // ==================== COLLEGE MEN'S HOCKEY (NCAA) ====================
        TeamEntity("ncaa_wisc_badgers", "Wisconsin Badgers", "WIS", "https://assets.sportsdata.io/ncaa/team/wisconsin.png", "Men's Hockey", "NCAA", "Big Ten", "Team"),
        TeamEntity("ncaa_mich_wolverines", "Michigan Wolverines", "MICH", "https://assets.sportsdata.io/ncaa/team/michigan.png", "Men's Hockey", "NCAA", "Big Ten", "Team"),
        TeamEntity("ncaa_mich_state", "Michigan State Spartans", "MSU", "https://assets.sportsdata.io/ncaa/team/michiganstate.png", "Men's Hockey", "NCAA", "Big Ten", "Team"),
        TeamEntity("ncaa_nd_fighting_irish", "Notre Dame Fighting Irish", "ND", "https://assets.sportsdata.io/ncaa/team/notredame.png", "Men's Hockey", "NCAA", "Big Ten", "Team"),
        TeamEntity("ncaa_north_dakota", "North Dakota Fighting Hawks", "UND", "https://assets.sportsdata.io/ncaa/team/northdakota.png", "Men's Hockey", "NCAA", "NCHC", "Team"),
        TeamEntity("ncaa_denver_pioneers", "Denver Pioneers", "DEN", "https://assets.sportsdata.io/ncaa/team/denver.png", "Men's Hockey", "NCAA", "NCHC", "Team"),
        TeamEntity("ncaa_boston_college", "Boston College Eagles", "BC", "https://assets.sportsdata.io/ncaa/team/bc.png", "Men's Hockey", "NCAA", "Hockey East", "Team"),
        TeamEntity("ncaa_boston_univ", "Boston University Terriers", "BU", "https://assets.sportsdata.io/ncaa/team/bu.png", "Men's Hockey", "NCAA", "Hockey East", "Team"),
        TeamEntity("ncaa_minn_duluth", "Minnesota Duluth Bulldogs", "UMD", "https://assets.sportsdata.io/ncaa/team/umd.png", "Men's Hockey", "NCAA", "NCHC", "Team"),
        TeamEntity("ncaa_st_cloud", "St. Cloud State Huskies", "SCSU", "https://assets.sportsdata.io/ncaa/team/scsu.png", "Men's Hockey", "NCAA", "NCHC", "Team"),

        // ==================== AMERICAN FOOTBALL (NFL) ====================
        TeamEntity("134932", "Kansas City Chiefs", "KC", sportsDbLogo("kansas-city-chiefs.png"), "American Football", "NFL", "AFC West", "Team"),
        TeamEntity("134942", "Green Bay Packers", "GB", sportsDbLogo("green-bay-packers.png"), "American Football", "NFL", "NFC North", "Team"),
        TeamEntity("134944", "Chicago Bears", "CHI", sportsDbLogo("chicago-bears.png"), "American Football", "NFL", "NFC North", "Team"),
        TeamEntity("134943", "Detroit Lions", "DET", sportsDbLogo("detroit-lions.png"), "American Football", "NFL", "NFC North", "Team"),
        TeamEntity("134940", "Philadelphia Eagles", "PHI", sportsDbLogo("philadelphia-eagles.png"), "American Football", "NFL", "NFC East", "Team"),
        TeamEntity("134939", "Dallas Cowboys", "DAL", sportsDbLogo("dallas-cowboys.png"), "American Football", "NFL", "NFC East", "Team"),
        TeamEntity("134933", "San Francisco 49ers", "SF", sportsDbLogo("san-francisco-49ers.png"), "American Football", "NFL", "NFC West", "Team"),
        TeamEntity("134920", "Buffalo Bills", "BUF", sportsDbLogo("buffalo-bills.png"), "American Football", "NFL", "AFC East", "Team"),
        TeamEntity("134921", "Miami Dolphins", "MIA", sportsDbLogo("miami-dolphins.png"), "American Football", "NFL", "AFC East", "Team"),
        TeamEntity("134922", "New England Patriots", "NE", sportsDbLogo("new-england-patriots.png"), "American Football", "NFL", "AFC East", "Team"),
        TeamEntity("134923", "New York Jets", "NYJ", sportsDbLogo("new-york-jets.png"), "American Football", "NFL", "AFC East", "Team"),
        TeamEntity("134924", "Baltimore Ravens", "BAL", sportsDbLogo("baltimore-ravens.png"), "American Football", "NFL", "AFC North", "Team"),
        TeamEntity("134927", "Pittsburgh Steelers", "PIT", sportsDbLogo("pittsburgh-steelers.png"), "American Football", "NFL", "AFC North", "Team"),
        TeamEntity("134925", "Cincinnati Bengals", "CIN", sportsDbLogo("cincinnati-bengals.png"), "American Football", "NFL", "AFC North", "Team"),
        TeamEntity("134926", "Cleveland Browns", "CLE", sportsDbLogo("cleveland-browns.png"), "American Football", "NFL", "AFC North", "Team"),
        TeamEntity("134930", "Denver Broncos", "DEN", sportsDbLogo("denver-broncos.png"), "American Football", "NFL", "AFC West", "Team"),
        TeamEntity("134931", "Las Vegas Raiders", "LV", sportsDbLogo("las-vegas-raiders.png"), "American Football", "NFL", "AFC West", "Team"),
        TeamEntity("134947", "Los Angeles Rams", "LAR", sportsDbLogo("los-angeles-rams.png"), "American Football", "NFL", "NFC West", "Team"),
        TeamEntity("134948", "Arizona Cardinals", "ARI", sportsDbLogo("arizona-cardinals.png"), "American Football", "NFL", "NFC West", "Team"),
        TeamEntity("134941", "New York Giants", "NYG", sportsDbLogo("new-york-giants.png"), "American Football", "NFL", "NFC East", "Team"),
        TeamEntity("134938", "Washington Commanders", "WAS", sportsDbLogo("washington-commanders.png"), "American Football", "NFL", "NFC East", "Team"),
        TeamEntity("134946", "Tampa Bay Buccaneers", "TB", sportsDbLogo("tampa-bay-buccaneers.png"), "American Football", "NFL", "NFC South", "Team"),
        TeamEntity("134928", "Houston Texans", "HOU", sportsDbLogo("houston-texans.png"), "American Football", "NFL", "AFC South", "Team"),
        TeamEntity("134929", "Indianapolis Colts", "IND", sportsDbLogo("indianapolis-colts.png"), "American Football", "NFL", "AFC South", "Team"),

        // ==================== BASKETBALL (NBA) ====================
        TeamEntity("134865", "Los Angeles Lakers", "LAL", sportsDbLogo("los-angeles-lakers.png"), "Basketball", "NBA", "Pacific", "Team"),
        TeamEntity("134866", "Golden State Warriors", "GSW", sportsDbLogo("golden-state-warriors.png"), "Basketball", "NBA", "Pacific", "Team"),
        TeamEntity("134867", "Boston Celtics", "BOS", sportsDbLogo("boston-celtics.png"), "Basketball", "NBA", "Atlantic", "Team"),
        TeamEntity("134870", "Chicago Bulls", "CHI", sportsDbLogo("chicago-bulls.png"), "Basketball", "NBA", "Central", "Team"),
        TeamEntity("134871", "Milwaukee Bucks", "MIL", sportsDbLogo("milwaukee-bucks.png"), "Basketball", "NBA", "Central", "Team"),
        TeamEntity("134868", "New York Knicks", "NYK", sportsDbLogo("new-york-knicks.png"), "Basketball", "NBA", "Atlantic", "Team"),
        TeamEntity("134869", "Philadelphia 76ers", "PHI", sportsDbLogo("philadelphia-76ers.png"), "Basketball", "NBA", "Atlantic", "Team"),
        TeamEntity("134875", "Miami Heat", "MIA", sportsDbLogo("miami-heat.png"), "Basketball", "NBA", "Southeast", "Team"),
        TeamEntity("134879", "Denver Nuggets", "DEN", sportsDbLogo("denver-nuggets.png"), "Basketball", "NBA", "Northwest", "Team"),
        TeamEntity("134882", "Dallas Mavericks", "DAL", sportsDbLogo("dallas-mavericks.png"), "Basketball", "NBA", "Southwest", "Team"),
        TeamEntity("134863", "Phoenix Suns", "PHX", sportsDbLogo("phoenix-suns.png"), "Basketball", "NBA", "Pacific", "Team"),
        TeamEntity("134864", "Los Angeles Clippers", "LAC", sportsDbLogo("los-angeles-clippers.png"), "Basketball", "NBA", "Pacific", "Team"),
        TeamEntity("134877", "Oklahoma City Thunder", "OKC", sportsDbLogo("oklahoma-city-thunder.png"), "Basketball", "NBA", "Northwest", "Team"),
        TeamEntity("134880", "Portland Trail Blazers", "POR", sportsDbLogo("portland-trail-blazers.png"), "Basketball", "NBA", "Northwest", "Team"),
        TeamEntity("134881", "Utah Jazz", "UTA", sportsDbLogo("utah-jazz.png"), "Basketball", "NBA", "Northwest", "Team"),

        // ==================== BASEBALL (MLB) ====================
        TeamEntity("135271", "New York Yankees", "NYY", sportsDbLogo("new-york-yankees.png"), "Baseball", "MLB", "AL East", "Team"),
        TeamEntity("135270", "Boston Red Sox", "BOS", sportsDbLogo("boston-red-sox.png"), "Baseball", "MLB", "AL East", "Team"),
        TeamEntity("135260", "Los Angeles Dodgers", "LAD", sportsDbLogo("los-angeles-dodgers.png"), "Baseball", "MLB", "NL West", "Team"),
        TeamEntity("135267", "Chicago Cubs", "CHC", sportsDbLogo("chicago-cubs.png"), "Baseball", "MLB", "NL Central", "Team"),
        TeamEntity("135268", "Chicago White Sox", "CWS", sportsDbLogo("chicago-white-sox.png"), "Baseball", "MLB", "AL Central", "Team"),
        TeamEntity("135262", "San Francisco Giants", "SF", sportsDbLogo("san-francisco-giants.png"), "Baseball", "MLB", "NL West", "Team"),
        TeamEntity("135261", "San Diego Padres", "SD", sportsDbLogo("san-diego-padres.png"), "Baseball", "MLB", "NL West", "Team"),
        TeamEntity("135276", "Texas Rangers", "TEX", sportsDbLogo("texas-rangers.png"), "Baseball", "MLB", "AL West", "Team"),
        TeamEntity("135277", "Houston Astros", "HOU", sportsDbLogo("houston-astros.png"), "Baseball", "MLB", "AL West", "Team"),
        TeamEntity("135264", "Philadelphia Phillies", "PHI", sportsDbLogo("philadelphia-phillies.png"), "Baseball", "MLB", "NL East", "Team"),
        TeamEntity("135265", "Atlanta Braves", "ATL", sportsDbLogo("atlanta-braves.png"), "Baseball", "MLB", "NL East", "Team"),
        TeamEntity("135266", "St. Louis Cardinals", "STL", sportsDbLogo("st-louis-cardinals.png"), "Baseball", "MLB", "NL Central", "Team"),
        TeamEntity("135263", "Colorado Rockies", "COL", sportsDbLogo("colorado-rockies.png"), "Baseball", "MLB", "NL West", "Team"),
        TeamEntity("135259", "Arizona Diamondbacks", "ARI", sportsDbLogo("arizona-diamondbacks.png"), "Baseball", "MLB", "NL West", "Team"),

        // ==================== SOCCER (MLS) ====================
        TeamEntity("137532", "Inter Miami CF", "MIA", sportsDbLogo("inter-miami.png"), "Soccer", "MLS", "Eastern", "Team"),
        TeamEntity("135151", "LA Galaxy", "LAG", sportsDbLogo("la-galaxy.png"), "Soccer", "MLS", "Western", "Team"),
        TeamEntity("136125", "Los Angeles FC", "LAFC", sportsDbLogo("lafc.png"), "Soccer", "MLS", "Western", "Team"),
        TeamEntity("135150", "Atlanta United FC", "ATL", sportsDbLogo("atlanta-united.png"), "Soccer", "MLS", "Eastern", "Team"),
        TeamEntity("135153", "Portland Timbers", "POR", sportsDbLogo("portland-timbers.png"), "Soccer", "MLS", "Western", "Team"),

        // ==================== RACING & MOTORSPORTS ====================
        TeamEntity("series_f1", "Formula 1 World Championship", "F1", "https://www.thesportsdb.com/images/media/league/badge/formula_1.png", "Motorsport", "Formula 1", "Global", "Racing"),
        TeamEntity("series_nascar", "NASCAR Cup Series", "NASCAR", "https://www.thesportsdb.com/images/media/league/badge/nascar.png", "Motorsport", "NASCAR", "National", "Racing"),
        TeamEntity("series_indycar", "IndyCar Series", "INDY", "https://www.thesportsdb.com/images/media/league/badge/indycar.png", "Motorsport", "IndyCar", "National", "Racing"),
        TeamEntity("series_motogp", "MotoGP World Championship", "MotoGP", "https://www.thesportsdb.com/images/media/league/badge/motogp.png", "Motorsport", "MotoGP", "Global", "Racing"),
    )
}
