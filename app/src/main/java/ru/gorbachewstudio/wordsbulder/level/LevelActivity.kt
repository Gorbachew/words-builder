package ru.gorbachewstudio.wordsbulder.level

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.widget.Button
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.TextView
import androidx.annotation.RequiresApi
import kotlinx.android.synthetic.main.activity_level.*
import ru.gorbachewstudio.wordsbulder.MainActivity
import ru.gorbachewstudio.wordsbulder.R
import ru.gorbachewstudio.wordsbulder.word.Word
import ru.gorbachewstudio.wordsbulder.save.WordStorage
import ru.gorbachewstudio.wordsbulder.word.WordObj

@Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS", "CAST_NEVER_SUCCEEDS")
class LevelActivity : AppCompatActivity() {

    private lateinit var _parentWord: String
    private var idRow: Int = 1000
    private var idColumn: Int = 2000
    private lateinit var wordsArr: ArrayList<Word>
    private var wordsObjArr: ArrayList<WordObj> = ArrayList()
    private var disabledButtons: ArrayList<Button> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_level)

        _parentWord = intent.getStringExtra("word")
        wordsArr = WordStorage(this).getWords(_parentWord)

        buttonsClick()
        generateWords(wordsArr)
        generateLetters(_parentWord)

        userWord.text = ""
    }

    private fun buttonsClick(){
        btnOk.setOnClickListener{
            checkGuessedWord(wordsArr, userWord.text as String).toString()
            userWord.text = ""
            wordsArr = WordStorage(this).getWords(_parentWord)
            checkOpenedWord(wordsObjArr)

            disabledButtons.forEach {
                it.setBackgroundColor(resources.getColor(R.color.colorLightGrey))
                it.isClickable = true
            }
            disabledButtons.clear()
        }
        backBtn.setOnClickListener{
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left);
        }
        btnDeleteLastLetter.setOnClickListener{
            userWord.text = userWord.text.dropLast(1)
            disabledButtons[disabledButtons.size - 1].isClickable = true;
            disabledButtons[disabledButtons.size - 1].setBackgroundColor(resources.getColor(R.color.colorLightGrey))
            disabledButtons.removeAt(disabledButtons.size - 1)
        }
        btnDeleteLastLetter.setOnLongClickListener {
            userWord.text = ""
            disabledButtons.forEach {
                it.setBackgroundColor(resources.getColor(R.color.colorLightGrey))
                it.isClickable = true
            }
            disabledButtons.clear()
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
                disabledButtons.add(buttonObj)
                buttonObj.isClickable = false
                buttonObj.setBackgroundColor(R.color.colorDarkGray)
            }
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
}

