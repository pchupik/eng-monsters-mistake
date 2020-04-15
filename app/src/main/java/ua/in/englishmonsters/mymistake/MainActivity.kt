package ua.`in`.englishmonsters.mymistake

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View.GONE
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

//        buttonUse.setOnClickListener {
//            cardView.visibility = GONE
//        }
        for_me_button.setOnClickListener {
            val intent = Intent(this, GeneratorActivity::class.java)
            startActivity(intent)
        }
        for_friend_button.setOnClickListener {
            val intent = Intent(this, GeneratorActivity::class.java)
            intent.putExtra("for_friend", true)
            startActivity(intent)
        }
    }
}
