package ru.gorbachewstudio.wordsbuilder.word

import android.widget.ImageButton
import ru.gorbachewstudio.wordsbuilder.MainActivity

class ParentWord constructor( mainActivity: MainActivity ){
    var view: ImageButton = ImageButton(mainActivity)
    var id: Int = 0
    var idRow: Int = 0
    var stars: Int = 0
    var name: String = ""
    var allWords: Int = 0
    var openedWords: Int = 0
}