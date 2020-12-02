package furhatos.app.movierecomender.flow

import furhatos.app.movierecomender.deselectedActors
import furhatos.app.movierecomender.deselectedGenres
import furhatos.nlu.common.*
import furhatos.flow.kotlin.*
import furhatos.app.movierecomender.nlu.*
import furhatos.app.movierecomender.selectedActors
import furhatos.app.movierecomender.selectedGenres

val Start : State = state(Interaction) {
    /**
     * This is the class where the conversation starts.
     */
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
    /**
     * Most states inherits from this class. Due to that the user can ask for a actor when Mr. Rotten is in the state
     * for genres.
     *
     * Intents for:
     *      Selecting an actor      (Transfer to function)
     *      Deselecting an actor    (Transfer to function)
     *      Selecting genre         (Transfer to function)
     *      Deselecting genre       (Transfer to function)
     */

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

    onResponse<DeselectActor>{
        val actors = it.intent.actors
        if (actors != null) {
            goto(DeselectActor(actors))
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

    onResponse<DeselectGenre>{
        val genres = it.intent.genres
        if (genres != null) {
            goto(DeselectGenre(genres))
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
    /**
     * First state after "hello".
     * Inherits from overview state so the user can immediately ask for a actor.
     *
     * Intents for:
     *      OnEntry
     *      Yes
     *      No
     */
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
        furhat.say {
            random{
                + "Great!"
                + "Lovely!"
                + "Wonderful!"
                + "Ok!"
            }

        }
        goto(MainState)
    }

    onResponse<No>{
        furhat.say{
            random{
                + "Okay, have a nice day!"
                + "Oh, too bad. I had a good one for you!"
            }
        }
        goto(Idle)
    }

}

val MainState : State = state(OverviewState){
    /**
     * The main state where the discussion is about movie preferences.
     * Inherits from overview state.
     *
     * Intents for:
     *      OnEntry
     *      OnReentry
     *      Yes
     *      No
     *
     */
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
            }
        })
    }

    onResponse<Yes>{
        furhat.ask({
            random {
                +"What do you wish for?"
                +"What are your preferences?"
            }
        })
    }

    onResponse<No>{
        furhat.say{
            + "You wish to see a movie"
            if (!users.current.selectedGenres.genres.list.isNullOrEmpty()){
                +", with the genre ${users.current.selectedGenres.genres}"
            }
            if (!users.current.selectedActors.actors.list.isNullOrEmpty()) {
                + ", with ${users.current.selectedActors.actors}"
            }
            if (!users.current.deselectedActors.actors.list.isNullOrEmpty()) {
                + ", without ${users.current.deselectedActors.actors}"
            }
            +". "
            + "Enjoy your movie!"
        }
        //goto(movieRecomendation)
        goto(Idle)
    }



}

fun SelectActor(actors : ActorList) : State = state(OverviewState){
    /**
     * If select actor intent detected the state is transfered here.
     * Stores preferred actors in the user data.
     *
     * Intents for:
     *      OnEntry
     *      OnReentry
     *      Yes
     *      No
     *
     */
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

fun DeselectActor(actors : ActorList) : State = state(OverviewState){
    /**
     * If deselect actor intent detected the state is transferred here.
     * Stores which actors that are not preferred in the user data.
     *
     * Intents for:
     *      OnEntry
     *      OnReentry
     *      Yes
     *      No
     *
     */
    onEntry{
        //furhat.say("Ok, I add ${actors.text} to your preferred actors")
        actors.list.forEach{
            print(it)
            users.current.deselectedActors.actors.list.add(it)
        }
        furhat.say("Ok, I have noted that you don't wish to see ${users.current.deselectedActors.actors}")
        furhat.ask({
            random{
                + "Any actors you would like to see?"
                + "Do you have any actors you would like to see?"
            }
        })
    }

    onReentry{
        furhat.ask({
            random{
                + "Any actors you would like to see?"
                + "Do you have any actors you would like to see?"
            }
        })
    }

    onResponse<Yes>{
        furhat.ask({
            random{
                + "Whom would you like to see?"
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
    /**
     * If select genre intent detected the state is transferred here.
     * Stores preferred genres in the user data.
     *
     * Intents for:
     *      OnEntry
     *      OnReentry
     *      Yes
     *      No
     *
     */
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

fun DeselectGenre(genres : GenreList) : State = state(OverviewState){
    /**
     * If deselect genre intent detected the state is transferred here.
     * Stores which genres that are not preferred in the user data.
     *
     * Intents for:
     *      OnEntry
     *      OnReentry
     *      Yes
     *      No
     *
     */
    onEntry{
        //furhat.say("Ok, I add ${genres.text} to your preferred actors")
        genres.list.forEach{
            print(it)
            users.current.deselectedGenres.genres.list.add(it)
        }
        furhat.say("Ok, I have noted that you don't wish to see movies with the genre ${users.current.deselectedGenres.genres}")
        furhat.ask({
            random{
                + "Any genres you would like to see?"
                + "Do you have any genres you would like to see?"
            }
        })
    }

    onReentry{
        furhat.ask({
            random{
                + "Any types of movies you like?"
                + "Any preferred genres?"
            }
        })
    }

    onResponse<Yes>{
        furhat.ask({
            random{
                + "Any types of movies you like?"
                + "Any preferred genres?"
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


// fun DeSelectGenre

// ... Rest of intents.