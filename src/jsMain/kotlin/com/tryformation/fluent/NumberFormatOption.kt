package com.tryformation.fluent

/***
 * [mdn docs](https://developer.mozilla.org/en-US/docs/Web/JavaScript/Reference/Global_Objects/Intl/NumberFormat/NumberFormat#options)
 */
interface NumberFormatOption {
    abstract val key: String
    abstract val value: Any

    enum class CompactDisplay : NumberFormatOption {
        short, long;

        override val value: String = name
        override val key: String = "compactDisplay"
        override fun toString() = "$key = $value"
    }

    data class Currency(override val value: String) : NumberFormatOption {
        override val key: String = "currency"
        override fun toString() = "$key = $value"
    }

    enum class CurrencyDisplay(customValue: String? = null) : NumberFormatOption {
        symbol, narrowSymbol, code, _name("name");

        override val value: String = customValue ?: name
        override val key: String = "currencyDisplay"
        override fun toString() = "$key = $value"
    }

    enum class CurrencySign : NumberFormatOption {
        accounting, standard;

        override val value: String = name
        override val key: String = "currencySign"
        override fun toString() = "$key = $value"
    }

    enum class LocaleMatcher(customValue: String? = null) : NumberFormatOption {
        lookup, best_fit("best fit");

        override val value: String = customValue ?: name
        override val key: String = "localeMatcher"
        override fun toString() = "$key = $value"
    }

    enum class Notation() : NumberFormatOption {
        standard, scientific, engineering, compact;

        override val value: String = name
        override val key: String = "notation"
        override fun toString() = "$key = $value"
    }

    data class NumberingSystem(override val value: String) : NumberFormatOption {
        override val key: String = "numberingSystem"
        override fun toString() = "$key = $value"
    }

    enum class SignDisplay() : NumberFormatOption {
        auto, always, exceptZero, compact, negative, never;

        override val value: String = name
        override val key: String = "signDisplay"
        override fun toString() = "$key = $value"
    }

    enum class Style() : NumberFormatOption {
        decimal, currency, percent, unit;

        override val value: String = name
        override val key: String = "style"
        override fun toString() = "$key = $value"
    }

    /**
     * [units](https://tc39.es/proposal-unified-intl-numberformat/section6/locales-currencies-tz_proposed_out.html#sec-issanctionedsimpleunitidentifier)
     * also allowed: ${unitA}-per-${unitB}
     */
    data class Unit(override val value: String): NumberFormatOption {
        override val key: String = "unit"
        override fun toString() = "$key = $value"
    }

    enum class UnitDisplay() : NumberFormatOption {
        long, short, narrow;

        override val value: String = name
        override val key: String = "unitDisplay"
        override fun toString() = "$key = $value"
    }

    enum class UseGrouping(customValue: String? = null): NumberFormatOption {
        always, auto, _false("false"), min2, _true("true");

        override val value: String = customValue ?: name
        override val key: String = "useGrouping"
        override fun toString() = "$key = $value"
    }

    enum class RoundingMode : NumberFormatOption {
        ceil, floor, expand, trunc,
        halfCeil, halfFloor, halfExpand, halfTrunc,
        halfEven;
        override val value: String = name
        override val key: String = "roundingMode"
        override fun toString() = "$key = $value"
    }

    enum class RoundingPriority(customValue: Int): NumberFormatOption {
        _1(1),
        _2(2),
        _5(5),
        _10(10),
        _20(20),
        _25(25),
        _50(50),
        _100(100),
        _200(200),
        _250(250),
        _500(500),
        _1000(1000),
        _2000(2000),
        _2500(2500),
        _5000(5000),
        ;

        override val value: Int = customValue
        override val key: String = "roundingPriority"
        override fun toString() = "$key = $value"
    }

    enum class TrailingZeroDisplay: NumberFormatOption {
        auto, stripIfInteger;

        override val value: String = name
        override val key: String = "trailingZeroDisplay"
        override fun toString() = "$key = $value"
    }

    /**
     * valid range of values: 1..21
     */
    data class MinimumIntegerDigits(override val value: Int): NumberFormatOption {
        init {
            require(value in 1..21) {
                "value not in valid range 1..21"
            }
        }
        override val key: String = "minimumIntegerDigits"
        override fun toString() = "$key = $value"
    }

    /**
     * valid range of values: 0..20
     */
    data class MinimumFractionDigits(override val value: Int): NumberFormatOption {
        init {
            require(value in 0..20) {
                "value not in valid range 0..20"
            }
        }
        override val key: String = "minimumFractionDigits"
        override fun toString() = "$key = $value"
    }

    /**
     * valid range of values: 0..20
     */
    data class MaximumFractionDigits(override val value: Int): NumberFormatOption {
        init {
            require(value in 0..20) {
                "value not in valid range 0..20"
            }
        }
        override val key: String = "maximumFractionDigits"
        override fun toString() = "$key = $value"
    }

    /**
     * valid range of values: 1..21
     */
    data class MinimumSignificantDigits(override val value: Int): NumberFormatOption {
        init {
            require(value in 1..21) {
                "value not in valid range 1..21"
            }
        }
        override val key: String = "minimumSignificantDigits"
        override fun toString() = "$key = $value"
    }

    /**
     * valid range of values: 1..21
     */
    data class MaximumSignificantDigits(override val value: Int): NumberFormatOption {
        init {
            require(value in 1..21) {
                "value not in valid range 1..21"
            }
        }
        override val key: String = "maximumSignificantDigits"
        override fun toString() = "$key = $value"
    }
}