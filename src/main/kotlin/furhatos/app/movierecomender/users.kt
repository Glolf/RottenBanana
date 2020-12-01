package furhatos.app.movierecomender

import furhatos.app.movierecomender.nlu.ActorList
import furhatos.records.User

class Actors(var actors : ActorList = ActorList())

val User.selectedActors : Actors
    get() = data.getOrPut(Actors::class.qualifiedName,Actors())

val User.deselectedActors : Actors
    get() = data.getOrPut(Actors::class.qualifiedName,Actors())

// Genre

//year.

// rest of entities.