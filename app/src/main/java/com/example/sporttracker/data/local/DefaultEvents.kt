package com.example.sporttracker.data.local

import com.example.sporttracker.data.local.entity.GameEntity
import com.example.sporttracker.util.TimeUtils
import java.time.LocalDate

object DefaultEvents {

    private fun nhlLogo(abbrev: String): String =
        "https://assets.nhle.com/mws/logos/teams/svg/${abbrev}_light.svg"

    private fun sportsDbLogo(badgePath: String): String =
        "https://www.thesportsdb.com/images/media/team/badge/$badgePath"

    fun generateInitialEvents(): List<GameEntity> {
        val today = TimeUtils.currentCentralDate()
        fun dStr(date: LocalDate): String = TimeUtils.toCentralDateString(date)

        // Supercross season begins in January (Jan 9)
        val sxYear = if (today.monthValue >= 11) today.year + 1 else today.year
        val jan9 = LocalDate.of(sxYear, 1, 9)
        val jan16 = LocalDate.of(sxYear, 1, 16)
        val jan23 = LocalDate.of(sxYear, 1, 23)
        val jan30 = LocalDate.of(sxYear, 1, 30)
        val mar27 = LocalDate.of(sxYear, 3, 27)

        // Active November fixtures (in-season for Men's Hockey, NFL, NBA)
        val sunday = today
        val tuesday = today.plusDays(2)
        val wednesday = today.plusDays(3)
        val thursday = today.plusDays(4)
        val friday = today.plusDays(5)
        val saturday = today.plusDays(6)
        val nextSunday = today.plusDays(7)
        val nextTuesday = today.plusDays(9)

        return listOf(
            // ==================== MINNESOTA WILD (NHL) ====================
            GameEntity(
                id = "seed_wild_1",
                title = "Minnesota Wild at Chicago Blackhawks",
                sport = "Men's Hockey",
                league = "NHL",
                date = dStr(tuesday),
                timeCentral = "7:00 PM CT",
                homeTeamId = "134836",
                homeTeamAbbrev = "CHI",
                homeTeamName = "Chicago Blackhawks",
                homeTeamLogo = nhlLogo("CHI"),
                awayTeamId = "134856",
                awayTeamAbbrev = "MIN",
                awayTeamName = "Minnesota Wild",
                awayTeamLogo = nhlLogo("MIN"),
                venue = "United Center, Chicago, IL",
                entityId = "134856",
            ),
            GameEntity(
                id = "seed_wild_2",
                title = "Montreal Canadiens at Minnesota Wild",
                sport = "Men's Hockey",
                league = "NHL",
                date = dStr(thursday),
                timeCentral = "7:00 PM CT",
                homeTeamId = "134856",
                homeTeamAbbrev = "MIN",
                homeTeamName = "Minnesota Wild",
                homeTeamLogo = nhlLogo("MIN"),
                awayTeamId = "134840",
                awayTeamAbbrev = "MTL",
                awayTeamName = "Montreal Canadiens",
                awayTeamLogo = nhlLogo("MTL"),
                venue = "Xcel Energy Center, St. Paul, MN",
                entityId = "134856"
            ),
            GameEntity(
                id = "seed_wild_3",
                title = "Dallas Stars at Minnesota Wild",
                sport = "Men's Hockey",
                league = "NHL",
                date = dStr(saturday),
                timeCentral = "7:00 PM CT",
                homeTeamId = "134856",
                homeTeamAbbrev = "MIN",
                homeTeamName = "Minnesota Wild",
                homeTeamLogo = nhlLogo("MIN"),
                awayTeamId = "134846",
                awayTeamAbbrev = "DAL",
                awayTeamName = "Dallas Stars",
                awayTeamLogo = nhlLogo("DAL"),
                venue = "Xcel Energy Center, St. Paul, MN",
                entityId = "134856"
            ),
            GameEntity(
                id = "seed_wild_4",
                title = "Seattle Kraken at Minnesota Wild",
                sport = "Men's Hockey",
                league = "NHL",
                date = dStr(nextTuesday),
                timeCentral = "7:00 PM CT",
                homeTeamId = "134856",
                homeTeamAbbrev = "MIN",
                homeTeamName = "Minnesota Wild",
                homeTeamLogo = nhlLogo("MIN"),
                awayTeamId = "137494",
                awayTeamAbbrev = "SEA",
                awayTeamName = "Seattle Kraken",
                awayTeamLogo = nhlLogo("SEA"),
                venue = "Xcel Energy Center, St. Paul, MN",
                entityId = "134856"
            ),

            // ==================== SEATTLE KRAKEN (NHL) ====================
            GameEntity(
                id = "seed_kraken_1",
                title = "Vancouver Canucks at Seattle Kraken",
                sport = "Men's Hockey",
                league = "NHL",
                date = dStr(friday),
                timeCentral = "9:00 PM CT",
                homeTeamId = "137494",
                homeTeamAbbrev = "SEA",
                homeTeamName = "Seattle Kraken",
                homeTeamLogo = nhlLogo("SEA"),
                awayTeamId = "134860",
                awayTeamAbbrev = "VAN",
                awayTeamName = "Vancouver Canucks",
                awayTeamLogo = nhlLogo("VAN"),
                venue = "Climate Pledge Arena, Seattle, WA",
                entityId = "137494"
            ),

            // ==================== MINNESOTA GOLDEN GOPHERS (NCAA) ====================
            GameEntity(
                id = "seed_gophers_1",
                title = "Wisconsin Badgers at Minnesota Golden Gophers",
                sport = "Men's Hockey",
                league = "NCAA",
                date = dStr(friday),
                timeCentral = "7:00 PM CT",
                homeTeamId = "ncaa_minn_gophers",
                homeTeamAbbrev = "MINN",
                homeTeamName = "Minnesota Golden Gophers",
                homeTeamLogo = "https://assets.sportsdata.io/ncaa/team/123/minnesota.png",
                awayTeamId = "ncaa_wisc_badgers",
                awayTeamAbbrev = "WIS",
                awayTeamName = "Wisconsin Badgers",
                awayTeamLogo = "https://assets.sportsdata.io/ncaa/team/wisconsin.png",
                venue = "3M Arena at Mariucci, Minneapolis, MN",
                entityId = "ncaa_minn_gophers"
            ),
            GameEntity(
                id = "seed_gophers_2",
                title = "Wisconsin Badgers at Minnesota Golden Gophers",
                sport = "Men's Hockey",
                league = "NCAA",
                date = dStr(saturday),
                timeCentral = "6:00 PM CT",
                homeTeamId = "ncaa_minn_gophers",
                homeTeamAbbrev = "MINN",
                homeTeamName = "Minnesota Golden Gophers",
                homeTeamLogo = "https://assets.sportsdata.io/ncaa/team/123/minnesota.png",
                awayTeamId = "ncaa_wisc_badgers",
                awayTeamAbbrev = "WIS",
                awayTeamName = "Wisconsin Badgers",
                awayTeamLogo = "https://assets.sportsdata.io/ncaa/team/wisconsin.png",
                venue = "3M Arena at Mariucci, Minneapolis, MN",
                entityId = "ncaa_minn_gophers"
            ),

            // ==================== MINNESOTA VIKINGS (NFL) ====================
            GameEntity(
                id = "seed_vikings_1",
                title = "Minnesota Vikings at Chicago Bears",
                sport = "American Football",
                league = "NFL",
                date = dStr(sunday),
                timeCentral = "12:00 PM CT",
                homeTeamId = "134944",
                homeTeamAbbrev = "CHI",
                homeTeamName = "Chicago Bears",
                homeTeamLogo = sportsDbLogo("chicago-bears.png"),
                awayTeamId = "134935",
                awayTeamAbbrev = "MIN",
                awayTeamName = "Minnesota Vikings",
                awayTeamLogo = sportsDbLogo("minnesota-vikings.png"),
                venue = "Soldier Field, Chicago, IL",
                entityId = "134935"
            ),
            GameEntity(
                id = "seed_vikings_2",
                title = "Green Bay Packers at Minnesota Vikings",
                sport = "American Football",
                league = "NFL",
                date = dStr(nextSunday),
                timeCentral = "12:00 PM CT",
                homeTeamId = "134935",
                homeTeamAbbrev = "MIN",
                homeTeamName = "Minnesota Vikings",
                homeTeamLogo = sportsDbLogo("minnesota-vikings.png"),
                awayTeamId = "134942",
                awayTeamAbbrev = "GB",
                awayTeamName = "Green Bay Packers",
                awayTeamLogo = sportsDbLogo("green-bay-packers.png"),
                venue = "U.S. Bank Stadium, Minneapolis, MN",
                entityId = "134935"
            ),

            // ==================== SEATTLE SEAHAWKS (NFL) ====================
            GameEntity(
                id = "seed_seahawks_1",
                title = "Los Angeles Rams at Seattle Seahawks",
                sport = "American Football",
                league = "NFL",
                date = dStr(sunday),
                timeCentral = "3:05 PM CT",
                homeTeamId = "134934",
                homeTeamAbbrev = "SEA",
                homeTeamName = "Seattle Seahawks",
                homeTeamLogo = sportsDbLogo("seattle-seahawks.png"),
                awayTeamId = "134947",
                awayTeamAbbrev = "LAR",
                awayTeamName = "Los Angeles Rams",
                awayTeamLogo = sportsDbLogo("los-angeles-rams.png"),
                venue = "Lumen Field, Seattle, WA",
                entityId = "134934"
            ),
            GameEntity(
                id = "seed_seahawks_2",
                title = "Seattle Seahawks at San Francisco 49ers",
                sport = "American Football",
                league = "NFL",
                date = dStr(nextSunday),
                timeCentral = "3:05 PM CT",
                homeTeamId = "134933",
                homeTeamAbbrev = "SF",
                homeTeamName = "San Francisco 49ers",
                homeTeamLogo = sportsDbLogo("san-francisco-49ers.png"),
                awayTeamId = "134934",
                awayTeamAbbrev = "SEA",
                awayTeamName = "Seattle Seahawks",
                awayTeamLogo = sportsDbLogo("seattle-seahawks.png"),
                venue = "Levi's Stadium, Santa Clara, CA",
                entityId = "134934"
            ),

            // ==================== MINNESOTA TIMBERWOLVES (NBA) ====================
            GameEntity(
                id = "seed_wolves_1",
                title = "Dallas Mavericks at Minnesota Timberwolves",
                sport = "Basketball",
                league = "NBA",
                date = dStr(wednesday),
                timeCentral = "7:00 PM CT",
                homeTeamId = "134878",
                homeTeamAbbrev = "MIN",
                homeTeamName = "Minnesota Timberwolves",
                homeTeamLogo = sportsDbLogo("minnesota-timberwolves.png"),
                awayTeamId = "134882",
                awayTeamAbbrev = "DAL",
                awayTeamName = "Dallas Mavericks",
                awayTeamLogo = sportsDbLogo("dallas-mavericks.png"),
                venue = "Target Center, Minneapolis, MN",
                entityId = "134878"
            ),

            // ==================== MONSTER ENERGY AMA SUPERCROSS ====================
            GameEntity(
                id = "seed_sx_1",
                title = "Monster Energy AMA Supercross - Round 1 Anaheim 1",
                sport = "Motorsport",
                league = "Supercross",
                date = dStr(jan9),
                timeCentral = "8:00 PM CT",
                homeTeamName = "AMA Supercross Championship",
                homeTeamLogo = "https://www.thesportsdb.com/images/media/league/badge/supercross.png",
                venue = "Angel Stadium, Anaheim, CA",
                isRace = true,
                entityId = "series_supercross"
            ),
            GameEntity(
                id = "seed_sx_2",
                title = "Monster Energy AMA Supercross - Round 2 San Diego",
                sport = "Motorsport",
                league = "Supercross",
                date = dStr(jan16),
                timeCentral = "8:30 PM CT",
                homeTeamName = "AMA Supercross Championship",
                homeTeamLogo = "https://www.thesportsdb.com/images/media/league/badge/supercross.png",
                venue = "Snapdragon Stadium, San Diego, CA",
                isRace = true,
                entityId = "series_supercross"
            ),
            GameEntity(
                id = "seed_sx_3",
                title = "Monster Energy AMA Supercross - Round 3 Anaheim 2",
                sport = "Motorsport",
                league = "Supercross",
                date = dStr(jan23),
                timeCentral = "8:00 PM CT",
                homeTeamName = "AMA Supercross Championship",
                homeTeamLogo = "https://www.thesportsdb.com/images/media/league/badge/supercross.png",
                venue = "Angel Stadium, Anaheim, CA",
                isRace = true,
                entityId = "series_supercross"
            ),
            GameEntity(
                id = "seed_sx_4",
                title = "Monster Energy AMA Supercross - Round 4 Glendale",
                sport = "Motorsport",
                league = "Supercross",
                date = dStr(jan30),
                timeCentral = "7:00 PM CT",
                homeTeamName = "AMA Supercross Championship",
                homeTeamLogo = "https://www.thesportsdb.com/images/media/league/badge/supercross.png",
                venue = "State Farm Stadium, Glendale, AZ",
                isRace = true,
                entityId = "series_supercross"
            ),
            GameEntity(
                id = "seed_sx_seattle",
                title = "Monster Energy AMA Supercross - Round 11 Seattle",
                sport = "Motorsport",
                league = "Supercross",
                date = dStr(mar27),
                timeCentral = "8:00 PM CT",
                homeTeamName = "AMA Supercross Championship",
                homeTeamLogo = "https://www.thesportsdb.com/images/media/league/badge/supercross.png",
                venue = "Lumen Field, Seattle, WA",
                isRace = true,
                entityId = "series_supercross"
            ),

            // ==================== AMA PRO MOTOCROSS CHAMPIONSHIP ====================
            GameEntity(
                id = "seed_mx_1",
                title = "Pro Motocross Championship - Round 1 Fox Raceway National",
                sport = "Motorsport",
                league = "Pro Motocross",
                date = dStr(LocalDate.of(sxYear, 5, 23)),
                timeCentral = "3:00 PM CT",
                homeTeamName = "AMA Pro Motocross Championship",
                homeTeamLogo = "https://www.thesportsdb.com/images/media/league/badge/supercross.png",
                venue = "Fox Raceway, Pala, CA",
                isRace = true,
                entityId = "series_motocross"
            ),
            GameEntity(
                id = "seed_mx_redbud",
                title = "Pro Motocross Championship - Round 5 RedBud National",
                sport = "Motorsport",
                league = "Pro Motocross",
                date = dStr(LocalDate.of(sxYear, 7, 4)),
                timeCentral = "12:00 PM CT",
                homeTeamName = "AMA Pro Motocross Championship",
                homeTeamLogo = "https://www.thesportsdb.com/images/media/league/badge/supercross.png",
                venue = "RedBud MX, Buchanan, MI",
                isRace = true,
                entityId = "series_motocross"
            ),
            GameEntity(
                id = "seed_mx_millville",
                title = "Pro Motocross Championship - Round 7 Spring Creek National",
                sport = "Motorsport",
                league = "Pro Motocross",
                date = dStr(LocalDate.of(sxYear, 7, 18)),
                timeCentral = "1:00 PM CT",
                homeTeamName = "AMA Pro Motocross Championship",
                homeTeamLogo = "https://www.thesportsdb.com/images/media/league/badge/supercross.png",
                venue = "Spring Creek MX Park, Millville, MN",
                isRace = true,
                entityId = "series_motocross"
            ),
            GameEntity(
                id = "seed_mx_washougal",
                title = "Pro Motocross Championship - Round 8 Washougal National",
                sport = "Motorsport",
                league = "Pro Motocross",
                date = dStr(LocalDate.of(sxYear, 7, 25)),
                timeCentral = "3:00 PM CT",
                homeTeamName = "AMA Pro Motocross Championship",
                homeTeamLogo = "https://www.thesportsdb.com/images/media/league/badge/supercross.png",
                venue = "Washougal MX Park, Washougal, WA",
                isRace = true,
                entityId = "series_motocross"
            ),

            // ==================== SUPERMOTOCROSS WORLD CHAMPIONSHIP ====================
            GameEntity(
                id = "seed_smx_final",
                title = "SuperMotocross World Championship - Final",
                sport = "Motorsport",
                league = "SuperMotocross",
                date = dStr(LocalDate.of(sxYear, 9, 19)),
                timeCentral = "7:00 PM CT",
                homeTeamName = "SuperMotocross World Championship",
                homeTeamLogo = "https://www.thesportsdb.com/images/media/league/badge/supercross.png",
                venue = "The Strip at Las Vegas Motor Speedway, Las Vegas, NV",
                isRace = true,
                entityId = "series_smx"
            )
        )
    }
}
