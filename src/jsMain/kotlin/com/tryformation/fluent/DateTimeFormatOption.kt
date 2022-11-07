package com.tryformation.fluent

/***
 * [mdn docs](https://developer.mozilla.org/en-US/docs/Web/JavaScript/Reference/Global_Objects/Intl/DateTimeFormat/DateTimeFormat#options)
 */
interface DateTimeFormatOption {
    abstract val key: String
    abstract val value: Any


    enum class TimeStyle: DateTimeFormatOption {
        full, long, medium, short;
        override val key: String = "timeStyle"
        override val value: String = name
        override fun toString() = "$key = $value"
    }

    enum class DateStyle: DateTimeFormatOption {
        full, long, medium, short;
        override val key: String = "dateStyle"
        override val value: String = name
        override fun toString() = "$key = $value"
    }

    data class NumberingSystem(override val value: String): DateTimeFormatOption{
        override val key: String = "numberingSystem"
        override fun toString() = "$key = $value"
    }

    data class Timezone(override val value: String): DateTimeFormatOption {
        override val key: String = "timezone"
        override fun toString() = "$key = $value"
    }
    data class Calendar(override val value: String): DateTimeFormatOption {
        override val key: String = "calendar"
        override fun toString() = "$key = $value"
    }

    enum class Weekday : DateTimeFormatOption {
        long, short, narrow;

        override val key: String = "weekday"
        override val value: String = name
        override fun toString() = "$key = $value"
    }

    enum class Era : DateTimeFormatOption {
        long, short, narrow;

        override val key: String = "era"
        override val value: String = name
        override fun toString() = "$key = $value"
    }

    enum class Year(custom_value: String? = null) : DateTimeFormatOption {
        numeric,
        two_digit("2-digit"),
        ;

        override val key: String = "year"
        override val value: String = custom_value ?: name
        override fun toString() = "$key = $value"
    }

    enum class Month(custom_value: String? = null) : DateTimeFormatOption {
        numeric,
        two_digit("2-digit"),
        long,
        short,
        narrow,
        ;

        override val key: String = "month"
        override val value: String = custom_value ?: name
        override fun toString() = "$key = $value"
    }

    enum class Day(custom_value: String? = null) : DateTimeFormatOption {
        numeric,
        two_digit("2-digit"),
        ;

        override val key: String = "day"
        override val value: String = custom_value ?: name
        override fun toString() = "$key = $value"
    }

    enum class Hour(custom_value: String? = null) : DateTimeFormatOption {
        numeric,
        two_digit("2-digit"),
        ;

        override val key: String = "hour"
        override val value: String = custom_value ?: name
        override fun toString() = "$key = $value"
    }

    enum class Minute(custom_value: String? = null) : DateTimeFormatOption {
        numeric,
        two_digit("2-digit"),
        ;

        override val key: String = "minute"
        override val value: String = custom_value ?: name
        override fun toString() = "$key = $value"
    }

    enum class Second(custom_value: String? = null): DateTimeFormatOption {
        numeric,
        two_digit("2-digit"),
        ;

        override val key: String = "second"
        override val value: String = custom_value ?: name
        override fun toString() = "$key = $value"
    }

    enum class TimezoneName(custom_value: String? = null): DateTimeFormatOption {
        long,
        short,
        shortOffset,
        longOffset,
        shortGeneric,
        longGeneric,
        ;

        override val key: String = "timezoneName"
        override val value: String = custom_value ?: name
        override fun toString() = "$key = $value"
    }
}
