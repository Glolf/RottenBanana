package furhatos.app.movierecomender

import furhatos.app.movierecomender.nlu.*
import furhatos.flow.kotlin.NullSafeUserDataDelegate
import furhatos.nlu.common.Date
import furhatos.records.User
import furhatos.nlu.common.Number

import java.net.HttpURLConnection
import java.net.URL



class ActorsData(
        /**
         * "Variable" for storing selected actors
         */
        var actors : ActorList = ActorList()
)

class NotActorsData(
        /**
         * "Variable" for storing deselected actors
         */
        var actors : ActorList = ActorList()
)

val User.selectedActors : ActorsData
    /**
     * "Variable" for storing selected actors
     */
    get() = data.getOrPut(ActorsData::class.qualifiedName, ActorsData())

val User.deselectedActors : NotActorsData
    /**
     * "Variable" for storing deselected actors (can't have same class as selectedActors).
     */
    get() = data.getOrPut(NotActorsData::class.qualifiedName,NotActorsData())


// Genre
class GenreData(
        /**
         * "Variable" for storing selected genres
         */
        var genres : GenreList = GenreList()
)

class NotGenreData(
        /**
         * "Variable" for storing deselected genres
         */
        var genres : GenreList = GenreList()
)

val User.selectedGenres : GenreData
    /**
     * "Variable" for storing selected genres
     */
    get() = data.getOrPut(GenreData::class.qualifiedName, GenreData())

val User.deselectedGenres : NotGenreData
    /**
     * "Variable" for storing deselected genres (can't have same class as selectedGenres).
     */
    get() = data.getOrPut(NotGenreData::class.qualifiedName, NotGenreData())



//Rating
class RatingData(
        /**
         * "Variable" for storing rating, defaults to 0
         */
        var ratingVal : Number? = Number()
)

val User.rating : RatingData
    /**
     * "Variable" for storing rating
     */
    get() = data.getOrPut(RatingData::class.qualifiedName, RatingData())

//year.
class YearData(
        var lowerYear : Number? = Number(),
        var upperYear : Number? = Number()
)

val User.yearPreferences : YearData
    get() = data.getOrPut(YearData::class.qualifiedName, YearData())


class MovieListData(
        var movieList : List<String> = listOf("Movie1", "Movie2", "Movie3")
)
val User.movieList : MovieListData
    get() = data.getOrPut(MovieListData::class.qualifiedName, MovieListData())

var User.movieIndex by NullSafeUserDataDelegate { 0 }
var User.anyPreferences by NullSafeUserDataDelegate { false }

// Companies
class CompanyData(
        /**
         * "Variable" for storing selected actors
         */
        var companies : CompanyList = CompanyList()
)

class NotCompanyData(
        /**
         * "Variable" for storing deselected actors
         */
        var companies : CompanyList = CompanyList()
)

val User.selectedCompanies : CompanyData
    /**
     * "Variable" for storing selected actors
     */
    get() = data.getOrPut(CompanyData::class.qualifiedName, CompanyData())

val User.deselectedCompanies : NotCompanyData
    /**
     * "Variable" for storing deselected actors (can't have same class as selectedActors).
     */
    get() = data.getOrPut(NotCompanyData::class.qualifiedName,NotCompanyData())


// Languages
class LanguageData(
        /**
         * "Variable" for storing selected actors
         */
        var myLanguages : OrigLanguageList = OrigLanguageList()
)

class NotLanguageData(
        /**
         * "Variable" for storing deselected actors
         */
        var myLanguages : OrigLanguageList = OrigLanguageList()
)

val User.selectedLanguages : LanguageData
    /**
     * "Variable" for storing selected actors
     */
    get() = data.getOrPut(LanguageData::class.qualifiedName, LanguageData())

val User.deselectedLanguages : NotLanguageData
    /**
     * "Variable" for storing deselected actors (can't have same class as selectedActors).
     */
    get() = data.getOrPut(NotLanguageData::class.qualifiedName,NotLanguageData())


// Directors
class DirectorData(
        /**
         * "Variable" for storing selected actors
         */
        var directors : DirectorList = DirectorList()
)

class NotDirectorData(
        /**
         * "Variable" for storing deselected actors
         */
        var directors : DirectorList = DirectorList()
)

val User.selectedDirectors : DirectorData
    /**
     * "Variable" for storing selected actors
     */
    get() = data.getOrPut(DirectorData::class.qualifiedName, DirectorData())

val User.deselectedDirectors : NotDirectorData
    /**
     * "Variable" for storing deselected actors (can't have same class as selectedActors).
     */
    get() = data.getOrPut(NotDirectorData::class.qualifiedName,NotDirectorData())



fun mapToString(m : Map <String, String>) : String {
  var out = "{"
  var first = true;
  m.forEach {
      k,v -> run {  if (first){
                      first = false;
                    } else {
                      out += ","
                    }
                    out += "\"$k\": $v" }
  }
  out += "}"
  return out
}

fun User.preferences() : String {
    var prefs = mapOf(
        "actors" to mapToString(mapOf(
           "selected" to this.selectedActors.actors.list.map({"\"$it\""}).toString(),
           "deselected" to this.deselectedActors.actors.list.map({"\"$it\""}).toString()
        )),
        "genres" to mapToString(mapOf(
           "selected" to this.selectedGenres.genres.list.map({"\"$it\""}).toString(),
           "deselected" to this.deselectedGenres.genres.list.map({"\"$it\""}).toString()
        )),
        "company" to mapToString(mapOf(
                "selected" to this.selectedCompanies.companies.list.map({"\"$it\""}).toString(),
                "deselected" to this.deselectedCompanies.companies.list.map({"\"$it\""}).toString()
        )),
        "language" to mapToString(mapOf(
                "selected" to this.selectedLanguages.myLanguages.list.map({"\"$it\""}).toString(),
                "deselected" to this.deselectedLanguages.myLanguages.list.map({"\"$it\""}).toString()
        )),
        "director" to mapToString(mapOf(
                "selected" to this.selectedDirectors.directors.list.map({"\"$it\""}).toString(),
                "deselected" to this.deselectedDirectors.directors.list.map({"\"$it\""}).toString()
        )),
        "years" to mapToString(mapOf(
           "lower" to this.yearPreferences.lowerYear.toString(),
           "upper" to this.yearPreferences.upperYear.toString()
        )),
        "rating" to this.rating.ratingVal.toString()
        )

    return mapToString(prefs)
};

fun User.recommendations() : List<String> {
  var prefs = this.preferences()
  var url = URL("http://localhost:8042/")
  var con = url.openConnection() as HttpURLConnection;
  con.setRequestMethod("POST")
  con.setRequestProperty("Content-Type", "application/json")
  con.setRequestProperty("Accept", "application/json")
  con.setDoOutput(true)
  var os = con.getOutputStream()
  var bp = prefs.toByteArray(Charsets.UTF_8)
  os.write(bp, 0, bp.size)
  var reader = con.getInputStream().reader()
  var res = reader.readText()
  reader.close()
  println(res)
  return res.lines()
}