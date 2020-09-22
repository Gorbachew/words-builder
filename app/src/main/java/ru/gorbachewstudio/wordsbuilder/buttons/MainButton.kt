package ru.gorbachewstudio.wordsbuilder.buttons

import android.content.Intent
import android.view.Gravity
import android.widget.*
import ru.gorbachewstudio.wordsbuilder.MainActivity
import ru.gorbachewstudio.wordsbuilder.R
import ru.gorbachewstudio.wordsbuilder.levels.LevelActivity
import ru.gorbachewstudio.wordsbuilder.save.WordStorage

class MainButton{

    fun inflateButton(activity: MainActivity, id: Int, idRow: Int, element: String): Int{
        var newIdRow = idRow
        if(id == 0 || id % 3 == 0){
            newIdRow++
            inflateRow(activity, newIdRow)
        }
        val frameLayout = FrameLayout(activity);
        frameLayout.layoutParams = FrameLayout.LayoutParams(300, 200)
        frameLayout.addView(inflateButtonView(activity, element), 0)
        frameLayout.addView(inflateStars(activity), 1)
        frameLayout.addView(inflateTextView(activity, element))
        activity.findViewById<LinearLayout>(newIdRow).addView(frameLayout)
        return newIdRow
    }

    private fun inflateStars(activity: MainActivity): LinearLayout {
        val linearLayout = LinearLayout(activity)
        val params = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.MATCH_PARENT)

        linearLayout.layoutParams = params
        linearLayout.orientation = LinearLayout.HORIZONTAL
        for(i in 1..3){
            inflateStarView(activity, linearLayout)
        }
        return linearLayout
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

    private fun inflateTextView(activity: MainActivity, element: String): TextView{
        val textView = TextView(activity)
        textView.layoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.MATCH_PARENT)
        textView.gravity = Gravity.CENTER
        val name = element + "\n" +
                WordStorage(activity).getOpenWordCount(element) + " / " + WordStorage(activity).getWordCount(element)
        textView.text = name
        return textView
    }

    private fun openLevel(activity: MainActivity, nameBtn: String){
        val intent = Intent(activity, LevelActivity::class.java)
        intent.putExtra("word",nameBtn)
        activity.startActivity(intent)
    }

    private fun inflateStarView(activity: MainActivity, linearLayout: LinearLayout){
        val imageView = ImageView(activity)
        val params = LinearLayout.LayoutParams(50,
            50)
        params.setMargins(10, 0, 10, 0)
        params.weight = 1f
        params.gravity = Gravity.BOTTOM
        imageView.layoutParams = params
        imageView.setBackgroundColor(activity.resources.getColor(R.color.colorPrimaryDark))
        linearLayout.addView(imageView)
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