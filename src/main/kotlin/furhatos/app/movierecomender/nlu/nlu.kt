package furhatos.app.movierecomender.nlu

import furhatos.nlu.ComplexEnumEntity
import furhatos.nlu.EnumEntity
import furhatos.nlu.Intent
import furhatos.nlu.ListEntity
import furhatos.nlu.common.Date
import furhatos.util.Language
import furhatos.nlu.common.Number


import java.io.File

// =====   Entities   =====
class Actor : EnumEntity(stemming = true, speechRecPhrases = true){
    /**
     * Defines what an actor is from file
     */
    override fun getEnum(lang: Language): List<String> {
        val path = System.getProperty("user.dir")
        //println("Using: $path/data/actors.csv")
        return File("data/actors.csv").useLines {it.toList()}
        //return listOf("Johnny Depp", "Tom Cruise", "Kiera Nightly", "Jennifer Lawrence") // List imported from database
    }
}

/*class Genre3 : EnumEntity(stemming = true, speechRecPhrases = true) {
    /**
     * Defines what a genre is from file.
     */

    override fun getEnum(lang: Language): List<String> {
        return listOf("Romance","Animated","Comedy","Musical","Thriller") //Import from database.
    }
}
*/

class Companies : EnumEntity(stemming = true, speechRecPhrases = true){
    /**
     * Defines what an actor is from file
     */
    override fun getEnum(lang: Language): List<String> {
        val path = System.getProperty("user.dir")
        //println("Using: $path/data/actors.csv")
        //return File("data/companies.csv").useLines {it.toList()}
        return listOf("Disney","Pixar","Warner Bros") // List imported from database
    }
}

class origLanguage : EnumEntity(stemming = true, speechRecPhrases = true){
    /**
     * Defines what an actor is from file
     */
    override fun getEnum(lang: Language): List<String> {
        val path = System.getProperty("user.dir")
        //println("Using: $path/data/actors.csv")
        //return File("data/languages.csv").useLines {it.toList()}
        return listOf("English","Swedish","German") // List imported from database
    }
}


class Genre: EnumEntity(stemming = true, speechRecPhrases = true) {
    /**
     * Defines what a genre is from file.
     */
    override fun getEnum(lang: Language): List<String> {
        return listOf("Action:Action",
               "Adventure:Adventure",
                "Animation:Animation",
                "Comedy:Comedy,Funny",
                "Crime:Crime",
                "Documentary:Documentary",
                "Drama:Drama",
                "Family:Family",
                "Fantasy:Fantasy",
                "History:History",
                "Horror:Horror",
                "Music:Music",
                "Mystery:Mystery",
                "Romance:Romance,Romantic",
                "Science Fiction:Science Fiction, Sci-fi",
                "TV_Movie:TV Movie",
                "Thriller:Thriller",
                "War:War",
                "Western:Western") //Import from database.
    }


    override fun toText(): String {
        return generate("$value")
    }
}

// Year


// Runtime

// Gender of lead.

// Keywords ????


class ActorList : ListEntity<Actor>()
/**
 * Creates class with actors as a list.
 */

class GenreList : ListEntity<Genre>()
/**
 * Creates class with genres as a list.
 */

// List of genres....




// =====   Intents    =====

class SelectActor(val actors: ActorList? = null) : Intent() {
    /**
     * Defines what user can say to be recognised as a select actor intent.
     */
    override fun getExamples(lang: Language): List<String> {
        return listOf("@actors", "I want to see @actors", "like @actors", "love @actors") // Write more examples.
    }
}

class DeselectActor(val actors: ActorList? = null) : Intent() {
    /**
     * Defines what user can say to be recognised as a deselect actor intent.
     */
    override fun getExamples(lang: Language): List<String> {
        return listOf("I don't want to see @actors", "Not @actors", "I don't like @actors","hate @actors", "dislike @actors") // Write more examples.
    }
}

class SelectGenre(val genres: GenreList? = null) : Intent() {
    /**
     * Defines what user can say to be recognised as a select genre intent.
     */
    override fun getExamples(lang: Language): List<String> {
        return listOf("@genres", "I want to see a @genres movie","like @genres", "love @genres")
    }
}

class DeselectGenre(val genres: GenreList? = null) : Intent() {
    /**
     * Defines what user can say to be recognised as a deselect genre intent.
     */
    override fun getExamples(lang: Language): List<String> {
        return listOf("Not @genres", "I don't want to see a @genres movie", "I don't like @genres","hate @genres", "dislike @genres")
    }
}

// Year
class PreferredYears(
        val upperYear: Number? = null,
        val lowerYear: Number? = null
        ) : Intent(){
    override fun getExamples(lang: Language): List<String> { // It can't listen to older than, I don't know why?!?!
        return listOf("before @upperYear","In @lowerYear","from @lowerYear","older than @upperYear","after @lowerYear",
                "newer than @lowerYear","Later than @lowerYear",
                "Between @lowerYear and @upperYear","The movie should be before @upperYear", "The movie should be after @lowerYear",
                "I want to see a movie that is newer than @lowerYear","I want to see a movie that is older than @upperYear"

        )
    }
}

class Rating(val rating: Number? = null) : Intent(){ // Borde inte vara Number? ????
    override fun getExamples(lang: Language): List<String> {
        return listOf("@rating","+Rating @rating", "At least @rating in +rating","minimum +rating @rating","@rating +stars", "With a rating of at least @rating")
    }
}




// Before @second year, after @first year. In between @ first year and @ second year.
// val secYear: Date = null ??????

// Intents for all entities

// Intents for deSelecting entities.

// RequestOptions

// RequestActorOptions

// RequestXxxxOptions