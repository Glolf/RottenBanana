package furhatos.app.movierecomender

import furhatos.app.movierecomender.nlu.ActorList
import furhatos.app.movierecomender.nlu.Genre
import furhatos.app.movierecomender.nlu.GenreList
import furhatos.records.User

class ActorsData(
        var actors : ActorList = ActorList()
)

val User.selectedActors : ActorsData
    get() = data.getOrPut(ActorsData::class.qualifiedName, ActorsData())

//val User.deselectedActors : ActorsData
//    get() = data.getOrPut(ActorsData::class.qualifiedName,ActorsData())

// Genre
class GenreData(
        var genres : GenreList = GenreList()
)

val User.selectedGenres : GenreData
    get() = data.getOrPut(GenreData::class.qualifiedName, GenreData())

//year.

// rest of entities.