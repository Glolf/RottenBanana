package furhatos.app.movierecomender.nlu

import furhatos.nlu.EnumEntity
import furhatos.nlu.Intent
import furhatos.nlu.ListEntity
import furhatos.util.Language


import java.io.File

// =====   Entities   =====
class Actor : EnumEntity(stemming = true, speechRecPhrases = true){
    override fun getEnum(lang: Language): List<String> {

        val path = System.getProperty("user.dir")
        println("Using: $path/data/actors.csv")
        return File("data/actors.csv").useLines {it.toList()}
        // return listOf("Johnny Depp", "Tom Cruise", "Kiera Nightly",
        // "Jennifer Lawrence") // List imported from database
    }
}

class Genre : EnumEntity(stemming = true, speechRecPhrases = true) {
    override fun getEnum(lang: Language): List<String> {
        return listOf("Romance","Animated","Comedy","Musical","Thriller") //Import from database.
    }
}

// Year

// Runtime

// Gender of lead.

// Keywords ????


class ActorList : ListEntity<Actor>()

// List of genres....




// =====   Intents    =====

class SelectActor(val actor: Actor? = null) : Intent() {
    override fun getExamples(lang: Language): List<String> {
        return listOf("@actor", "I want to see @actor") // Write more examples.
    }
}

// Intents for all entities

// Intents for deSelecting entities.

// RequestOptions

// RequestActorOptions

// RequestXxxxOptions