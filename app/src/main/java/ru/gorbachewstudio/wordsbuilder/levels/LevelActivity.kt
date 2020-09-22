package ru.gorbachewstudio.wordsbuilder.levels

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.PorterDuff
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.widget.Button
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.MobileAds
import kotlinx.android.synthetic.main.activity_level.*
import ru.gorbachewstudio.wordsbuilder.MainActivity
import ru.gorbachewstudio.wordsbuilder.R
import ru.gorbachewstudio.wordsbuilder.save.WordStorage
import ru.gorbachewstudio.wordsbuilder.word.Word
import ru.gorbachewstudio.wordsbuilder.word.WordObj


@Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS", "CAST_NEVER_SUCCEEDS")
class LevelActivity : AppCompatActivity() {

    private lateinit var _parentWord: String
    lateinit var mAdView : AdView
    private var idRow: Int = 1000
    private var idColumn: Int = 2000
    private lateinit var wordsArr: ArrayList<Word>
    private var wordsObjArr: ArrayList<WordObj> = ArrayList()
    private var allLetterButton: ArrayList<Button> = ArrayList()
    private var disabledLetterButtons: ArrayList<Button> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_level)

        _parentWord = intent.getStringExtra("word")
        wordsArr = WordStorage(this).getWords(_parentWord)

        buttonsClick()
        generateWords(wordsArr)
        generateLetters(_parentWord)
        activateAllLetterBtn()

        userWord.text = ""

        adBanner()


    }

    private fun buttonsClick(){
        btnOk.setOnClickListener{
            checkGuessedWord(wordsArr, userWord.text as String).toString()
            userWord.text = ""
            wordsArr = WordStorage(this).getWords(_parentWord)
            checkOpenedWord(wordsObjArr)
            activateAllLetterBtn()
            disabledLetterButtons.clear()
        }
        backBtn.setOnClickListener{
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left);
        }
        btnDeleteLastLetter.setOnClickListener{
            userWord.text = userWord.text.dropLast(1)
            disabledLetterButtons[disabledLetterButtons.size - 1].isClickable = true;
            disabledLetterButtons[disabledLetterButtons.size - 1].background.setColorFilter(resources.getColor(R.color.colorLightGrey), PorterDuff.Mode.DARKEN)
            disabledLetterButtons.removeAt(disabledLetterButtons.size - 1)
        }
        btnDeleteLastLetter.setOnLongClickListener {
            userWord.text = ""
            activateAllLetterBtn()
            disabledLetterButtons.clear()
            true
        }
    }

    private fun generateWords(arr: ArrayList<Word>){
        arr.forEachIndexed{index, element -> inflateText(index, element)}
        checkOpenedWord(wordsObjArr)
    }

    private fun generateLetters(word: String){
        val letters = word.split("")
        letters.forEachIndexed{index, element -> inflateButton(index, element)}
    }

    private fun inflateText(id: Int, element: Word){
        if(id == 0 || id % 10 == 0){
            inflateTextColumn()
        }

        val textObj = TextView(this)

        val wordObj = WordObj()
        wordObj.id = id
        wordObj.obj = textObj
        wordsObjArr.add(wordObj)

        textObj.layoutParams = LinearLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT,100)
        textObj.textSize = 20f
        findViewById<LinearLayout>(idColumn).addView(textObj)
    }


    @SuppressLint("ResourceType", "SetTextI18n")
    private fun inflateButton(id: Int, letter: String){
        if(letter != ""){
            if(id == 1 || id % 7 == 0){
                inflateBtnRow()
            }
            val buttonObj = Button(this)

            buttonObj.layoutParams = LinearLayout.LayoutParams(150,150)
            buttonObj.text = letter
            buttonObj.setOnClickListener{
                userWord.text = userWord.text.toString() + buttonObj.text.toString()
                disabledLetterButtons.add(buttonObj)
                buttonObj.isClickable = false
                buttonObj.background.setColorFilter(resources.getColor(R.color.colorDarkGray), PorterDuff.Mode.DARKEN)
            }
            allLetterButton.add(buttonObj)
            findViewById<LinearLayout>(idRow).addView(buttonObj)
        }
    }

    private fun inflateBtnRow(){
        val layout = LinearLayout(this)
        layout.layoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
        idRow++
        layout.id = idRow
        layout.gravity = Gravity.CENTER
        letterPlace.addView(layout)
    }

    private fun inflateTextColumn(){
        val layout = LinearLayout(this)
        layout.layoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.MATCH_PARENT,
            1f
        )
        idColumn++
        layout.id = idColumn
        layout.setPadding(50,0,0,0)
        layout.orientation = LinearLayout.VERTICAL
        wordsPlace.addView(layout)
    }

    private fun checkGuessedWord(words: ArrayList<Word>, word: String){
        words.forEach{
            if(it.word == word){
                WordStorage(this).openWord(it.id)
                return
            }
        }
    }

    private fun checkOpenedWord(wordsObj: ArrayList<WordObj>){
        wordsObj.forEach {
            val word = wordsArr[it.id]
            val textWord = word.word
            val obj = it.obj
            if (word.state == 0) {
                obj.text = textWord.replace(Regex("[А-Яа-я]"), "*")
            } else {
                obj.text = textWord
            }
        }
    }

    private fun adBanner(){
        MobileAds.initialize(this) {}

        val adRequest = AdRequest.Builder().build()
        adView.loadAd(adRequest)
    }

    private fun activateAllLetterBtn(){
        allLetterButton.forEach {
            it.background.setColorFilter(resources.getColor(R.color.colorLightGrey), PorterDuff.Mode.DARKEN)
            it.isClickable = true
        }
    }
}

