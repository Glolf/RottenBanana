package furhatos.app.movierecomender.nlu

import furhatos.nlu.EnumEntity
import furhatos.nlu.Intent
import furhatos.nlu.ListEntity
import furhatos.util.Language


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
        return listOf("I don't want to see @actors", "Not @actors") // Write more examples.
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
        return listOf("Not @genres", "I don't want to see a @genres movie")
    }
}

// Intents for all entities

// Intents for deSelecting entities.

// RequestOptions

// RequestActorOptions

// RequestXxxxOptions