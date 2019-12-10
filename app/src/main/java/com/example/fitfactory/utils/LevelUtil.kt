package com.example.fitfactory.utils

import android.graphics.Color
import com.sdsmdg.harjot.vectormaster.VectorMasterView
import com.sdsmdg.harjot.vectormaster.models.PathModel

class LevelUtil(private var view: VectorMasterView?) {

    companion object{
        class Builder{
            private var view: VectorMasterView? = null

            fun setView(view: VectorMasterView): Builder{
                this.view = view
                return this
            }

            fun build() = LevelUtil(view)
        }
    }


    fun setLevel(entriesCount: Int) {
        val first: PathModel? = view?.getPathModelByName("firstFront")
        val second: PathModel? = view?.getPathModelByName("secondFront")
        val third: PathModel? = view?.getPathModelByName("thirdFront")
        val fourth: PathModel? = view?.getPathModelByName("fourthFront")

        val level: Int = getLevel(entriesCount)

        first?.trimPathEnd = 0f
        second?.trimPathEnd = 0f
        third?.trimPathEnd = 0f
        fourth?.trimPathEnd = 0f

        if (level == 8) {
            first?.trimPathEnd = 1f
            second?.trimPathEnd = 1f
            third?.trimPathEnd = 1f
            fourth?.trimPathEnd = 1f
        } else {
            when (val c = entriesCount - (level * 30)) {
                in 0..10 -> first?.let {
                    it.trimPathEnd = 0.1f * c
                }
                in 11..15 -> {
                    first?.let {
                        it.trimPathEnd = 1f
                    }

                    second?.let {
                        it.trimPathEnd = 0.2f * (c - 10)
                    }
                }
                in 16..20 -> {
                    first?.let {
                        it.trimPathEnd = 1f
                    }

                    second?.let {
                        it.trimPathEnd = 1f
                    }

                    third?.let {
                        it.trimPathEnd = 0.2f * (c - 15)
                    }
                }
                in 21..30 -> {
                    first?.let {
                        it.trimPathEnd = 1f
                    }

                    second?.let {
                        it.trimPathEnd = 1f
                    }

                    third?.let {
                        it.trimPathEnd = 1f
                    }

                    fourth?.let {
                        it.trimPathEnd = 0.1f * (c - 20)
                    }
                }

            }
        }

        val levelColorSet = getLevelColors(level)

        first?.strokeColor = levelColorSet[0]
        second?.strokeColor = levelColorSet[1]
        third?.strokeColor = levelColorSet[1]
        fourth?.strokeColor = levelColorSet[2]

        view?.update()
    }

    private fun getLevelColors(level: Int): ArrayList<Int> {
        var levelColorSet = ArrayList<Int>()

        when (level) {
            0 -> {
                levelColorSet.add(Color.parseColor("#B53636"))
                levelColorSet.add(Color.parseColor("#CC8322"))
                levelColorSet.add(Color.parseColor("#F7C745"))
            }
            1 -> {
                levelColorSet.add(Color.parseColor("#F7C745"))
                levelColorSet.add(Color.parseColor("#6ABF45"))
                levelColorSet.add(Color.parseColor("#367A40"))
            }
            2 -> {
                levelColorSet.add(Color.parseColor("#367A40"))
                levelColorSet.add(Color.parseColor("#09A880"))
                levelColorSet.add(Color.parseColor("#2AD4D4"))
            }
            3 -> {
                levelColorSet.add(Color.parseColor("#2AD4D4"))
                levelColorSet.add(Color.parseColor("#259DB8"))
                levelColorSet.add(Color.parseColor("#1D67C2"))
            }
            4 -> {
                levelColorSet.add(Color.parseColor("#1D67C2"))
                levelColorSet.add(Color.parseColor("#2F33AD"))
                levelColorSet.add(Color.parseColor("#662FA1"))
            }
            5 -> {
                levelColorSet.add(Color.parseColor("#662FA1"))
                levelColorSet.add(Color.parseColor("#902FA1"))
                levelColorSet.add(Color.parseColor("#C92B5A"))
            }
            6 -> {
                levelColorSet.add(Color.parseColor("#C92B5A"))
                levelColorSet.add(Color.parseColor("#AB283B"))
                levelColorSet.add(Color.parseColor("#c79a00"))
            }
            7, 8 -> {
                levelColorSet.add(Color.parseColor("#c79a00"))
                levelColorSet.add(Color.parseColor("#ffca28"))
                levelColorSet.add(Color.parseColor("#fffd61"))
            }
        }

        return levelColorSet
    }

    private fun getLevel(entriesCount: Int): Int {
        when (entriesCount) {
            in 0..30 -> return 0
            in 31..60 -> return 1
            in 61..90 -> return 2
            in 91..120 -> return 3
            in 121..150 -> return 4
            in 151..180 -> return 5
            in 181..210 -> return 6
            in 211..240 -> return 7
        }

        return 8
    }
}