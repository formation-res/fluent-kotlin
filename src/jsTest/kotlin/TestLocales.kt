import com.tryformation.localization.Locale

enum class TestLocales(
    override val languageCode: String,
    override val countryCode: String?,
    override val aliases: Array<String>,
    override val prefix: String = "locale"
) : Locale {
    EN_GB("en", "GB",arrayOf("en")),
    NL_NL("nl","NL",arrayOf("nl")),
    FR_FR("fr","FR",arrayOf("fr")),
}
