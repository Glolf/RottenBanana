package furhatos.app.movierecomender.flow

import furhatos.nlu.common.*
import furhatos.flow.kotlin.*
import furhatos.app.movierecomender.nlu.*

val Start : State = state(Interaction) {

    onEntry {
        random({furhat.say("Hello! Do you want a movie recomendation?")},
                {furhat.say("Hey it's Movietime! Want a recomendation for what to see?")},
                {furhat.say("Greetings! Can I recomend you a movie?")}
        )
        goto(FirstState)
    }
}

val OverviewState : State = state(Interaction){
    // Yes - ok, any preferences for what to watch? / ok prefered actor, genre...
    onResponse<Yes> {
        furhat.say({
            random{
                + "Great!"
                + "Lovely!"
                + "Wonderful!"
                + "Ok!"
            }
            + " "
             random{
                + "Which actor do you want to see in the movie?"
                 + "the genre of the movie?"
                // Other entities.
            }
        })
    }

    onResponse<SelectActor>{
        //furhat.say("Which actor would you like to see?")
        furhat.say("Ohh, ${it.intent.actor}?")
        goto(Idle)
    }

    // Actor intent

    // Genre intent

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
        furhat.ask("Do you have any preferenes?")
    }

    onResponse<No>{
        random({furhat.say("Okay, have a nice day!")},
                {furhat.say("Oh, too bad. I had a good one for you!")})
        goto(Idle)
    }

}

val MainState : State = state(OverviewState){
    // No -- done with preferences --> Goto preferences summary.

    // on reEntry - say("Do you have any other preferences for your movie?")
}

fun SelectActor(actor : Actor) : State = state(OverviewState){
    // add actor to user list of selected actors.

    // Ask for more prefered actors
        // Yes - Ask who
        // No return to MainState
}

// fun DeSelectActor

// fun SelectGenre

// fun DeSelectGenre

// ... Rest of intents.