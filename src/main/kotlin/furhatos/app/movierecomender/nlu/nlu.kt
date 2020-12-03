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

class Genre : EnumEntity(stemming = true, speechRecPhrases = true) {
    /**
     * Defines what a genre is from file.
     */
    override fun getEnum(lang: Language): List<String> {
        return listOf("Romance","Animated","Comedy","Musical","Thriller") //Import from database.
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
        return listOf("@actors", "I want to see @actors") // Write more examples.
    }
}

class DeselectActor(val actors: ActorList? = null) : Intent() {
    /**
     * Defines what user can say to be recognised as a deselect actor intent.
     */
    override fun getExamples(lang: Language): List<String> {
        return listOf("I don't want to see @actors", "Not @actors", "I don't like @actors") // Write more examples.
    }
}

class SelectGenre(val genres: GenreList? = null) : Intent() {
    /**
     * Defines what user can say to be recognised as a select genre intent.
     */
    override fun getExamples(lang: Language): List<String> {
        return listOf("@genres", "I want to see a @genres movie")
    }
}

class DeselectGenre(val genres: GenreList? = null) : Intent() {
    /**
     * Defines what user can say to be recognised as a deselect genre intent.
     */
    override fun getExamples(lang: Language): List<String> {
        return listOf("Not @genres", "I don't want to see a @genres movie", "I don't want to see @genres","I don't like @genres")
    }
}

// Year
class PreferredYears(
        val upperYear: Number? = null,
        val lowerYear: Number? = null
        ) : Intent(){
    override fun getExamples(lang: Language): List<String> { // It can't listen to older than, I don't know why?!?!
        return listOf("In @lowerYear","from @lowerYear","older than @upperYear","after @lowerYear", "newer than @lowerYear","before @upperYear","Later than @lowerYear",
                "Between @lowerYear and @upperYear")/*,"The movie should be before @upperYear", "The movie should be after @lowerYear", ,
                "I want to see a movie that is newer than @lowerYear","I want to see a movie that is older than @upperYear",

        )*/
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