import com.tryformation.localization.Locale

enum class TestLocales(
    override val languageCode: String,
    override val countryCode: String?,
    override val aliases: Array<String>,
    override val prefix: String = "locale"
) : Locale {
    EN_GB("en", "GB",arrayOf("en")),
    EN_US("en", "US",arrayOf("en")),
    DE_DE("de", "DE",arrayOf("de")),
    NL_NL("nl","NL",arrayOf("nl")),
    FR_FR("fr","FR",arrayOf("fr")),
}
