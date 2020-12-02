package furhatos.app.movierecomender

import furhatos.app.movierecomender.nlu.ActorList
import furhatos.app.movierecomender.nlu.Genre
import furhatos.app.movierecomender.nlu.GenreList
import furhatos.records.User

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


//year.

// rest of entities.