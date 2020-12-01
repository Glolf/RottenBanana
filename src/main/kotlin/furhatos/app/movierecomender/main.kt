package furhatos.app.movierecomender

import furhatos.app.movierecomender.flow.*
import furhatos.skills.Skill
import furhatos.flow.kotlin.*

class MovierecomenderSkill : Skill() {
    override fun start() {
        Flow().run(Idle)
    }
}

fun main(args: Array<String>) {
    Skill.main(args)
}
