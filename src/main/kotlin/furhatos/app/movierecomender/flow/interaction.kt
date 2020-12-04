package furhatos.app.movierecomender.flow

import furhatos.app.movierecomender.*
import furhatos.nlu.common.*
import furhatos.flow.kotlin.*
import furhatos.app.movierecomender.nlu.*
import furhatos.nlu.common.Number

val Start : State = state(Interaction) {
    /**
     * This is the class where the conversation starts.
     */
    onEntry {
        furhat.say {
            random{
                + "Hello!"
                + "Hey, it's movie time!"
                + "Greetings!"
            }
            + " "
            random{
                + "I'm Mr. Rotten."
                + "My name is Mr. Rotten."
            }
        }
        println(users.current.preferences())
        goto(FirstState)
    }
}

val OverviewState : State = state(Interaction){
    /**
     * Most states inherits from this class. Due to that the user can ask for a actor when Mr. Rotten is in the state
     * for genres.
     *
     * Intents for: (All transfer to other function
     *      Selecting an actor
     *      Deselecting an actor
     *      Selecting genre
     *      Deselecting genre
     *      Rating
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


    //Rating
    onResponse<Rating>{
        val rating = it.intent.rating
        if (rating != null) {
            goto(RatingSelect(rating))
        }
        else {
            propagate() // To handle null reference, as in fruit example.
        }

    }

    // Year
    onResponse<PreferredYears>{
        if (it.text.contains("older")){
            furhat.say{
                + "I have some trouble understanding \"older than\". Please repeat what you said but "
                + "use \"before\", instead of \"older than\". "
            }
            goto(MainState)
        }
        val lowerYear = it.intent.lowerYear
        val upperYear = it.intent.upperYear
        if (lowerYear != null || upperYear != null) {
            goto(YearSelect(lowerYear, upperYear))
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
                + "Do you want a movie recommendation?"
                + "Want a recommendation for what movie to see?"
                + "Can I recommend you a movie?"
            }
        })
    }

    onResponse<Yes> {
        furhat.ask {
            random{
                + "Great!"
                + "Lovely!"
                + "Wonderful!"
                + "Ok!"
            }
            +" "
            random{
                + "What are your movie preferences?"
                + "Any preferences for your movie?"
            }
        }
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
        println(users.current.preferences())
        furhat.ask({
            random{
                + "Any other preferences?"
                + "More preferences?"
                + "Do you have any other preferences for your movie?"
                + "Are you looking for something else?"
            }
           random{
               if (users.current.selectedActors.actors.list.isNullOrEmpty()) {
                   +"Maybe an actor?"
                   +"Perhaps an actor?"
               }
               if (users.current.selectedGenres.genres.list.isNullOrEmpty()) {
                   +"Perhaps a genre?"
                   +"Maybe a genre?"
               }
               // add for rating, "You can choose a minimum rating for your movie in the range 0 to 10.
               if (users.current.rating.ratingVal?.value == null) {
                   + "Perhaps a rating limit?"
                   + "Maybe a rating?"
               }
               if (users.current.yearPreferences.lowerYear?.value == null || users.current.yearPreferences.upperYear?.value == null) {
                   + "Maybe the movie should be released before or after a specific year?"
                   + "Maybe the movie should be released between two years?"
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
                    +"Maybe an actor?"
                    +"Perhaps an actor?"
                }
                if (users.current.selectedGenres.genres.list.isNullOrEmpty()) {
                    +"Perhaps a genre?"
                    +"Maybe a genre?"
                }
                if (users.current.rating.ratingVal?.value == null) {
                    + "Perhaps a rating limit?"
                    + "Maybe a rating?"
                }
                if (users.current.yearPreferences.lowerYear?.value == null || users.current.yearPreferences.upperYear?.value == null) {
                    + "Maybe the movie should be released before or after a specific year?"
                    + "Maybe the movie should be released between two years?"
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
        println(users.current.preferences())
        furhat.say{
            + "Your preferences are the following."
            if (!users.current.selectedGenres.genres.list.isNullOrEmpty()){
                +"The genre should be: ${users.current.selectedGenres.genres}."
            }
            if (!users.current.deselectedGenres.genres.list.isNullOrEmpty()){
                +"The genre shouldn't be: ${users.current.deselectedGenres.genres}."
            }
            if (!users.current.selectedActors.actors.list.isNullOrEmpty()) {
                + "You want to see: ${users.current.selectedActors.actors}."
            }
            if (!users.current.deselectedActors.actors.list.isNullOrEmpty()) {
                + "You don't want to see: ${users.current.deselectedActors.actors}."
            }
            if (users.current.rating.ratingVal?.value != null) {
                + "The rating should be at least ${users.current.rating.ratingVal}."
            }

            if (users.current.yearPreferences.lowerYear?.value != null && users.current.yearPreferences.upperYear?.value != null) {
                +" The movie should be created in between ${users.current.yearPreferences.lowerYear} and ${users.current.yearPreferences.upperYear}."
            } else if (users.current.yearPreferences.lowerYear?.value != null) {
                +" The movie should be created after ${users.current.yearPreferences.lowerYear}."
            } else if (users.current.yearPreferences.upperYear?.value != null) {
                +" The movie should be created before ${users.current.yearPreferences.upperYear}."
            }
            //+ "Enjoy your movie!"
        }
        goto(MovieRecommendation)
        //goto(Idle)
    }



}

val MovieRecommendation : State = state(Interaction){
    onEntry{
        /*TODO - Function to send preferences and recive list of movies. The list of movies shoul be stored in
        *  users.current.movieList.movieList(.list) */

        // suggest movie, does this sound good?
        val movieIndex = users.current.movieIndex
        val movie = users.current.movieList.movieList[movieIndex]

        furhat.say{random{
            +"I have found a movie for you: "
            + "I recommend you to watch this movie: "
        }
            + " ${movie}."
        }
        furhat.ask({random{
            + "Do you think that would be a good one for you?"
            + "Do you want to see that movie?"
        }
        })

    }

    onResponse<Yes> {
        furhat.say{random{
            +"Wonderful, have a lovely time!"
            +"Great! Enjoy your movie!"
            + "Lovely! Have a great time!"
        }}
    }

    onResponse<No> {
        users.current.movieIndex += 1
        val movieIndex = users.current.movieIndex
        val listSize = users.current.movieList.movieList.size

        if (users.current.movieIndex >= listSize) {
            furhat.say("I don't have any more movies for you. You can come back later and we can see if we can find a movie for you. Goodbye.")
            // TODO - FRIDA Expand this text.
            goto(Idle)
        }else if (users.current.movieIndex >= listSize-1) {
            val movie = users.current.movieList.movieList[movieIndex]
            furhat.say("This is the last movie that I can recommend you: $movie")
            furhat.ask({
                random {
                    +"Do you think that would be a good one for you?"
                    +"Do you want to see that movie?"
                }
            })
        } else {
            val movie = users.current.movieList.movieList[movieIndex]
            furhat.say {
                random {
                    +"Oh, sorry to hear that. Here is another movie for you:"
                    +"No worries, here is another movie"
                }
                +" ${movie}."
            }
            furhat.ask({
                random {
                    +"Do you think that would be a good one for you?"
                    +"Do you want to see that movie?"
                }
            })

        }
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
            if (!users.current.selectedActors.actors.list.contains(it)) {
                users.current.selectedActors.actors.list.add(it)
            }

            if (users.current.deselectedActors.actors.list.contains(it)) {
                users.current.deselectedActors.actors.list.remove(it)
                furhat.say{ random{+"You said before that you don't want to see ${it}, I assume you have changed your mind."
                    +"Changing our minds, are we? You said you didn't want to see ${it}. I have changed your preferences. "}}
            }
        }
        furhat.say("You wish to see: ${users.current.selectedActors.actors}.")
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
                + "Whom?"
                + "Who is it?"
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
            if (!users.current.deselectedActors.actors.list.contains(it)) {
                users.current.deselectedActors.actors.list.add(it)
            }

            if (users.current.selectedActors.actors.list.contains(it)) {
                users.current.selectedActors.actors.list.remove(it)
                furhat.say{ random{+"You said before that you want to see ${it}, I assume you have changed your mind."
                    +"Changing our minds, are we? You said you want to see ${it}."}}
            }
        }
        furhat.say("I have noted that you don't wish to see ${users.current.deselectedActors.actors}")
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
                + "Who?"
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
            //println(it)
            //println(it.value)
            if (!users.current.selectedGenres.genres.list.contains(it)) {
                users.current.selectedGenres.genres.list.add(it)
            }

            if (users.current.deselectedGenres.genres.list.contains(it)) {
                users.current.deselectedGenres.genres.list.remove(it)
                furhat.say{ random{+"You said before that you don't want to see ${it}, I assume you have changed your mind."
                                    +"Changing our minds, are we? You said you didn't want to see ${it}. I have changed your preferences. "}}
            }

        }
        furhat.say("You wish to see the following genres: ${users.current.selectedGenres.genres}")
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
                + "What genres?"
                + "Which genre?"
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
            if (!users.current.deselectedGenres.genres.list.contains(it)) {
                users.current.deselectedGenres.genres.list.add(it)
            }

            if (users.current.selectedGenres.genres.list.contains(it)) {
                users.current.selectedGenres.genres.list.remove(it)
                furhat.say{ random{+"You said before that you want to see ${it}, I assume you have changed your mind."
                    +"Changing our minds, are we? You said you wanted to see ${it}. "}}
            }
        }
        furhat.say("I have noted that you don't wish to see movies with the genre: ${users.current.deselectedGenres.genres}")
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


fun RatingSelect(rating : Number?) : State = state(OverviewState){
    /**
     * If rating intent detected the state is transferred here.
     * Stores preferred rating in the user data.
     *
     * Intents for:
     *      OnEntry
     *
     */
    onEntry{
        //furhat.say("Ok, I add ${actors.text} to your preferred actors")
        val rate = rating?.value
        if (rate == null){
            furhat.say{"Error!"}
        }
        else if (rate > 10 || rate < 0){
            furhat.ask("The rating must be within 0 to 10. What is the lowest rating you want to see?")
        } else {
            users.current.rating.ratingVal = rating
            furhat.say("You wish to see a movie with at least rating ${users.current.rating.ratingVal}")
        }
        goto(MainState)
    }
}

// ... Rest of intents.

fun YearSelect(lowerYear : Number?, upperYear:Number?) : State = state(OverviewState){
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

        val l = lowerYear?.value
        val u = upperYear?.value
        if (l != null){
            if (l > 2016){
                furhat.say{+ "Sorry, the last movie in my database was created 2016"}
            } else {
                users.current.yearPreferences.lowerYear = lowerYear
            }

        }
        if ( u != null) {
            if (u < 1927 ) {
                furhat.say{+ "Sorry, the first movie in my database was created 1927"}
            } else {
                users.current.yearPreferences.upperYear = upperYear
            }
        }
        var notSayDouble = false // We have not changed order for ll and uu
        val ll = users.current.yearPreferences.lowerYear?.value
        val uu = users.current.yearPreferences.upperYear?.value
        if (uu!=null && ll!=null && uu < ll){
            users.current.yearPreferences.lowerYear = Number(uu)
            users.current.yearPreferences.upperYear = Number(ll)
            furhat.say{
                +"It seems like your year specifications for release year is hard to fulfil. "
                +"I'll reset it and you can start over with your specifications for release year."
            }
            users.current.yearPreferences.upperYear = null
            users.current.yearPreferences.lowerYear = null
            notSayDouble = true
        }

        if (!notSayDouble && (ll != null || uu != null)) {
            furhat.say {
                +"You wish to see a movie"
                if (ll != null && uu != null) {

                    +" that was created in between ${users.current.yearPreferences.lowerYear} and ${users.current.yearPreferences.upperYear}"
                } else if (ll != null) {
                    +" that was created after ${users.current.yearPreferences.lowerYear}"
                } else if (uu != null) {
                    +" that was created before ${users.current.yearPreferences.upperYear}"
                }
            }
        }
        goto(MainState)
    }

}