package ru.gorbachewstudio.wordsbulder

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Gravity
import android.widget.Button
import android.widget.FrameLayout
import android.widget.LinearLayout
import kotlinx.android.synthetic.main.activity_main.*
import ru.gorbachewstudio.wordsbulder.save.WordStorage
import ru.gorbachewstudio.wordsbulder.level.LevelActivity

class MainActivity : AppCompatActivity(){

    private var idRow: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
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

    private fun openLevel(nameBtn: String){
        val intent = Intent(this, LevelActivity::class.java)
        intent.putExtra("word",nameBtn)
        startActivity(intent)
    }


    private fun generateLevels(){
        val parentWords = resources.getStringArray(R.array.parent_words)
        parentWords.forEachIndexed{index, element -> inflateButton(index, element)}
    }

    private fun inflateButton(id: Int, element: String){

        if(id == 0 || id % 3 == 0){
            inflateRow()
        }
        val buttonLayout = Button(this)
        buttonLayout.layoutParams = FrameLayout.LayoutParams(300,200)
        buttonLayout.text = element
        buttonLayout.setOnClickListener{
            val b = it as Button
            openLevel(b.text as String)
        }
        findViewById<LinearLayout>(idRow).addView(buttonLayout)
    }

    private fun inflateRow(){
        val layout = LinearLayout(this)

        layout.layoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
        idRow++
        layout.id = idRow
        layout.gravity = Gravity.CENTER
        lvlBtnLayout.addView(layout)
    }
}
