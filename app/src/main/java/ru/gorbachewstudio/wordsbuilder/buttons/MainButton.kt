package ru.gorbachewstudio.wordsbuilder.buttons

import android.content.Intent
import android.graphics.PorterDuff
import android.util.Log
import android.view.Gravity
import android.view.ViewGroup
import android.widget.*
import androidx.core.view.children
import ru.gorbachewstudio.wordsbuilder.MainActivity
import ru.gorbachewstudio.wordsbuilder.R
import ru.gorbachewstudio.wordsbuilder.levels.LevelActivity
import ru.gorbachewstudio.wordsbuilder.save.WordStorage
import ru.gorbachewstudio.wordsbuilder.word.ParentWord

class MainButton{

    fun inflateButton(activity: MainActivity, id: Int, idRow: Int, element: String): ParentWord{
        var newIdRow = idRow
        if(id == 0 || id % 3 == 0){
            newIdRow++
            inflateRow(activity, newIdRow)
        }
        val frameLayout = FrameLayout(activity);
        frameLayout.layoutParams = FrameLayout.LayoutParams(300, 200)
        val buttonView = inflateButtonView(activity, element)
        frameLayout.addView(buttonView, 0)
        val starsLayout = inflateStars(activity)
        frameLayout.addView(starsLayout, 1)

        val openWords = WordStorage(activity).getOpenWordCount(element)
        val allWords = WordStorage(activity).getWordCount(element)
        val stars = checkOpenStars(activity, starsLayout, Integer.parseInt(allWords), Integer.parseInt(openWords))
        frameLayout.addView(inflateTextView(activity, element, allWords, openWords))

        activity.findViewById<LinearLayout>(newIdRow).addView(frameLayout)
        val parentWord = enrichmentButtonInfo(activity, buttonView, id, newIdRow, element, stars, Integer.parseInt(allWords), Integer.parseInt(openWords))
        checkProgress(activity, parentWord)

        return parentWord
    }

    private fun enrichmentButtonInfo(mainActivity: MainActivity, buttonView: ImageButton, id: Int, idRow: Int, name: String, stars: Int, allWords: Int, openWords: Int): ParentWord{
        val parentWord = ParentWord(mainActivity)
        parentWord.view = buttonView
        parentWord.id = id
        parentWord.idRow = idRow
        parentWord.stars = stars
        parentWord.name = name
        parentWord.allWords = allWords
        parentWord.openedWords = openWords
        return parentWord
    }

    private fun checkProgress(activity: MainActivity, parentWord: ParentWord){
        if(parentWord.id > 0){
            if(activity.getLevelsInfo()[parentWord.id - 1].stars >= 1){
                parentWord.view.isClickable = true
                parentWord.view.background.setColorFilter(activity.resources.getColor(R.color.colorLightGrey), PorterDuff.Mode.DARKEN)
            }
            else{
                parentWord.view.isClickable = false
                parentWord.view.background.setColorFilter(activity.resources.getColor(R.color.colorDarkGray), PorterDuff.Mode.DARKEN)
            }
        }
    }

    private fun inflateButtonView(activity: MainActivity, element: String): ImageButton {
        val buttonView = ImageButton(activity)
        buttonView.layoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.MATCH_PARENT)
        buttonView.setOnClickListener{
            openLevel(activity, element)
        }
        return buttonView
    }

    private fun openLevel(activity: MainActivity, nameBtn: String){
        val intent = Intent(activity, LevelActivity::class.java)
        intent.putExtra("word", nameBtn)
        activity.startActivity(intent)
    }

    private fun inflateTextView(activity: MainActivity, element: String, wordsAll: String, wordsOpen: String): TextView{
        val textView = TextView(activity)
        textView.layoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.MATCH_PARENT)
        textView.gravity = Gravity.CENTER
        val name = element + "\n" +
                wordsOpen  + " / " + wordsAll
        textView.text = name
        return textView
    }

    private fun inflateStars(activity: MainActivity): LinearLayout {
        val linearLayout = LinearLayout(activity)
        val params = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.MATCH_PARENT)
        params.setMargins(40,0,20,0)
        linearLayout.layoutParams = params
        linearLayout.orientation = LinearLayout.HORIZONTAL
        for(i in 1..3){
            inflateStarView(activity, linearLayout, i + 100)
        }
        return linearLayout
    }

    private fun inflateStarView(activity: MainActivity, linearLayout: LinearLayout, id: Int){
        val imageView = ImageView(activity)
        val params = LinearLayout.LayoutParams(50,
            50)
        params.weight = 1f
        params.gravity = Gravity.BOTTOM
        imageView.id = id
        imageView.layoutParams = params
        imageView.setImageDrawable(activity.resources.getDrawable(R.drawable.star_disable))
        linearLayout.addView(imageView)
    }

    private fun checkOpenStars(activity: MainActivity, starsParent: ViewGroup, wordsAll: Int, wordsOpen: Int): Int{
        var progress = (wordsOpen * 100)/wordsAll
        var stars = 0
        for(starView in starsParent.children){
            val star = starsParent.findViewById<ImageView>(starView.id)
            progress -= 25
            if(progress >= 25){
                star.setImageDrawable(activity.resources.getDrawable(R.drawable.star_enable))
                stars++
            }
            else{
                break
            }
        }
        return stars
    }

    private fun inflateRow(activity: MainActivity, idRow: Int){
        val layout = LinearLayout(activity)
        layout.layoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
        layout.id = idRow
        layout.gravity = Gravity.CENTER
        activity.findViewById<LinearLayout>(R.id.lvlBtnLayout).addView(layout)
    }


}