package furhatos.app.movierecomender.flow

import furhatos.nlu.common.*
import furhatos.flow.kotlin.*
import furhatos.app.movierecomender.nlu.*
import furhatos.app.movierecomender.selectedActors
import furhatos.app.movierecomender.selectedGenres

val Start : State = state(Interaction) {

    onEntry {
        furhat.say({
            random{
                + "Hello"
                + "Hey, it's movie time!"
                + "Greetings"
            }
        })
        goto(FirstState)
    }
}

val OverviewState : State = state(Interaction){

    // Actor intent
    onResponse<SelectActor>{
        val actors = it.intent.actors
        if (actors != null) {
            goto(SelectActor(actors))
        }
        else {
            propagate() // To handle null reference, as in fruit example.
        }
    }



    // Genre intent
    onResponse<SelectGenre>{
        val genres = it.intent.genres
        if (genres != null) {
            goto(SelectGenre(genres))
        }
        else {
            propagate() // To handle null reference, as in fruit example.
        }
    }

    // Intents.....

    // RequestOptions - actor, genre, year, ....

    // requestActorOptions - some random actors that are available

    // requestGenreOptions - genres available.

    // Current preferences. (do we want this??)
    // Change preferences (Do we want this??)
}


val FirstState : State = state(OverviewState){
    // User don't want a movie recommendation
    onEntry {
        furhat.ask({
            random {
                +"Do you want a movie recommendation?"
                + "Want a recommendation for what movie to see?"
                + "Can I recommend you a movie?"
            }
        })
    }

    onResponse<Yes> {
        furhat.say({
            random{
                + "Great!"
                + "Lovely!"
                + "Wonderful!"
                + "Ok!"
            }

        })
        goto(MainState)
    }

    onResponse<No>{
        furhat.say({
            random{
                + "Okay, have a nice day!"
                + "Oh, too bad. I had a good one for you!"
            }
        })
        goto(Idle)
    }

}

val MainState : State = state(OverviewState){
    onEntry{
        furhat.ask({
           random{
               if (users.current.selectedActors.actors.list.isNullOrEmpty()) {
                   +"Which actor do you want to see in the movie?"
               }
               if (users.current.selectedGenres.genres.list.isNullOrEmpty()) {
                   +"What genre do you want the movie to be?"
               }
               if (!users.current.selectedActors.actors.list.isNullOrEmpty() && !users.current.selectedGenres.genres.list.isNullOrEmpty()) {
                   +"Any other preferences?"
               }
                // Other entities.
            }
        })
    }

    onReentry{
        furhat.ask({
            random{
                + "Do you wish for something else?"
                + "Any other preferences for your movie?"
            }
            + " "
            random{
                if (users.current.selectedActors.actors.list.isNullOrEmpty()) {
                    +"Maybe a preferred actor?"
                    +"Perhaps an actor?"
                }
                if (users.current.selectedGenres.genres.list.isNullOrEmpty()) {
                    +"Perhaps a genre?"
                    +"Maybe a preferred genre?"
                }
                if (!users.current.selectedActors.actors.list.isNullOrEmpty() && !users.current.selectedGenres.genres.list.isNullOrEmpty()) {
                    +"Any other preferences?"
                }

            }
        })
    }

    onResponse<No>{
        furhat.say{
            + "You wish to see a (${users.current.selectedGenres.genres} | movie) [with ${users.current.selectedActors.actors}]."
            + "Enjoy your movie!"
            + "I will fix this later // Frida."
        }
        //goto(movieRecomendation)
        goto(Idle)
    }



}

fun SelectActor(actors : ActorList) : State = state(OverviewState){
    onEntry{
        //furhat.say("Ok, I add ${actors.text} to your preferred actors")
        actors.list.forEach{
            print(it)
            users.current.selectedActors.actors.list.add(it)
        }
        furhat.say("You wish to see ${users.current.selectedActors.actors}")
        furhat.ask({
            random{
                + "Any other actors you would like to see?"
                + "Do you have more actors you would like to see?"
            }
        })
    }

    onReentry{
        furhat.ask({
            random{
                + "Who else do you want to see?"
                + "Any other preferred actors?"
            }
        })
    }

    onResponse<Yes>{
        furhat.ask({
            random{
                + "Who else do you want to see?"
                + "Any other preferred actors?"
            }
        })
    }

    onResponse<No>{
        furhat.say{
            random{
            +"Ok."
            +"Sure."
        }}

        goto(MainState)
    }

}



// fun DeSelectActor
fun SelectGenre(genres : GenreList) : State = state(OverviewState){
    onEntry{
        //furhat.say("Ok, I add ${genres.text} to your preferred actors")
        genres.list.forEach{
            print(it)
            users.current.selectedGenres.genres.list.add(it)
        }
        furhat.say("You wish to see the folowin genres ${users.current.selectedGenres.genres}")
        furhat.ask({
            random{
                + "Any other genres you would like to see?"
                + "Do you have more genres you would like to see?"
            }
        })
    }

    onReentry{
        furhat.ask({
            random{
                + "Other types of movies you like?"
                + "Any other preferred genres?"
            }
        })
    }

    onResponse<Yes>{
        furhat.ask({
            random{
                + "Other types of movies you like?"
                + "Any other preferred genres?"
            }
        })
    }

    onResponse<No>{
        furhat.say{
            random{
                +"Ok."
                +"Sure."
            }}

        goto(MainState)
    }

}

// fun SelectGenre

// fun DeSelectGenre

// ... Rest of intents.