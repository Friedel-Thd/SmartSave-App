package com.example.smartsave


data class Month(val year: Int, val month: Int) {
    init {
        require(month in 1..12) {"bruh das kein monat du lellek"}
    }

    override fun toString() = when (month) {
        1 -> "Januar"
        2 -> "Februar"
        3 -> "März"
        4 -> "April"
        5 -> "Mai"
        6 -> "Juni"
        7 -> "Juli"
        8 -> "August"
        9 -> "September"
        10 -> "Oktober"
        11 -> "November"
        12 -> "Dezember"
        else -> throw IllegalStateException()
    } + " $year"

    operator fun minus(months: Int): Month {
        var m = month - months
        var y = year
        while (m <= 0) {
            --y
            m += 12
        }
        while (m > 12) {
            ++y
            m -= 12
        }
        return Month(y, m)
    }
}
