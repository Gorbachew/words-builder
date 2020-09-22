package ru.gorbachewstudio.wordsbuilder

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.firebase.analytics.FirebaseAnalytics
import kotlinx.android.synthetic.main.activity_main.*
import ru.gorbachewstudio.wordsbuilder.buttons.MainButton
import ru.gorbachewstudio.wordsbuilder.save.WordStorage

class MainActivity : AppCompatActivity(){

    private var mFirebaseAnalytics: FirebaseAnalytics? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);

        buttonsClick()

        val wordStorage = WordStorage(this)

        if(!wordStorage.checkFullnessDB()){
            wordStorage.createWords()
        }

        generateLevels()
    }

    private fun buttonsClick(){
        exitBtn.setOnClickListener{
            System.exit(0)
        }
        resetBtn.setOnClickListener{
            WordStorage(this).resetDb()
        }
    }

    private fun generateLevels(){
        var idRow = 1000
        val parentWords = resources.getStringArray(R.array.parent_words)
        parentWords.forEachIndexed{index, element -> idRow = MainButton().inflateButton(this, index, idRow, element)}
    }
}
