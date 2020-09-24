package ru.gorbachewstudio.wordsbuilder

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.firebase.analytics.FirebaseAnalytics
import kotlinx.android.synthetic.main.activity_main.*
import ru.gorbachewstudio.wordsbuilder.buttons.MainButton
import ru.gorbachewstudio.wordsbuilder.save.WordStorage
import ru.gorbachewstudio.wordsbuilder.word.ParentWord

class MainActivity : AppCompatActivity(){

    private var mFirebaseAnalytics: FirebaseAnalytics? = null
    private var levelsInfo = ArrayList<ParentWord>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);

        buttonsClick()
        val parentWords = resources.getStringArray(R.array.parent_words)
        val wordStorage = WordStorage(this)

        if(!wordStorage.checkFullnessDB()){
            wordStorage.createWords()
        }else{
            wordStorage.checkNewWords(parentWords)
        }

        generateLevels(parentWords)

        textStarsCount.text = countStars(levelsInfo).toString()
    }

    fun getLevelsInfo(): ArrayList<ParentWord>{
        return  levelsInfo
    }

    private fun buttonsClick(){
        exitBtn.setOnClickListener{
            System.exit(0)
        }
        resetBtn.setOnClickListener{
            WordStorage(this).resetDb()
        }
    }

    private fun generateLevels(parentWords: Array<String>){
        var idRow = 1000
        parentWords.forEachIndexed{index, element -> idRow = generateLevel(index, idRow, element)}
    }

    private fun generateLevel(index: Int, idRow: Int, element: String): Int{
        val parentWord = MainButton().inflateButton(this, index, idRow, element)
        levelsInfo.add(parentWord)
        return parentWord.idRow
    }

    private fun countStars(levelsInfo: ArrayList<ParentWord>): Int {
        var stars = 0
        for(levelInfo in levelsInfo){
            stars += levelInfo.stars
        }
        return stars
    }
}
