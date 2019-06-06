package sample.gde.myapplicationw

import android.os.Bundle
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintLayout.LayoutParams
import androidx.constraintlayout.widget.ConstraintSet
import androidx.transition.ChangeBounds
import androidx.transition.Transition
import androidx.transition.TransitionManager
import androidx.appcompat.app.AppCompatActivity
import android.view.ViewGroup
import android.view.animation.AnticipateOvershootInterpolator
import kotlinx.android.synthetic.main.activity_main.*
import android.os.Handler
import android.view.View
import android.view.LayoutInflater
import kotlin.random.Random


class MainActivity : AppCompatActivity() {

    private var coinsParent : ConstraintLayout? = null
    private val commonTransition : Transition = initializeTransition()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)

        coinsParent = findViewById(R.id.coins_parent)

        fab_coin.setOnClickListener {
            dropCoins()
        }
    }

    private fun dropCoins(){
        fab_coin.isEnabled = false
        val rndCount = Random.nextInt(1,50)
        count_lbl.text = "$rndCount"
        iterateFirstTransition(rndCount)
    }

    private fun iterateFirstTransition(count: Int){
        if (count > 0) {
            val coin = createCoin()
            initializeConstrain(coin)

            Handler().postDelayed( {
                this.runOnUiThread {
                    val constraint2 = ConstraintSet()
                    constraint2.clone(this, R.layout.second_constraints)
                    TransitionManager.beginDelayedTransition(coinsParent as ViewGroup,commonTransition)
                    constraint2.applyTo(coinsParent)
                    iterateFirstTransition(count - 1)
                }
            }, 200 )
        }else
            Handler().postDelayed({
                this.runOnUiThread {
                    val constraint3 = ConstraintSet()
                    constraint3.clone(this, R.layout.third_constraints)
                    TransitionManager.beginDelayedTransition(coinsParent as ViewGroup,commonTransition)
                    constraint3.applyTo(coinsParent)
                }
                Handler().postDelayed({
                    coinsParent?.removeAllViews()
                    fab_coin.isEnabled = true
                    count_lbl.text = ""
                },1000)
            },2200)

    }

    private fun createCoin(): View {
        val item = LayoutInflater.from(this).inflate(R.layout.initial_coin, null)
        coinsParent?.addView(item)
        item.id = R.id.coin_1
        return item
    }

    private fun initializeConstrain(item: View){
        var params = LayoutParams(
            LayoutParams.WRAP_CONTENT,
            LayoutParams.WRAP_CONTENT
        )

        params.topToTop = R.id.coins_parent
        params.bottomToBottom = R.id.coins_parent
        params.leftToLeft = R.id.coins_parent
        params.rightToRight = R.id.coins_parent

        params.verticalBias = 0.5.toFloat()
        params.horizontalBias = 0.5.toFloat()

        params.height = 200
        params.width = 200

        item.layoutParams = params
    }

    private fun initializeTransition(): Transition {

        val transition = ChangeBounds()
        transition.interpolator = AnticipateOvershootInterpolator(1.0f)
        transition.duration = 1200
        return transition
    }

}
