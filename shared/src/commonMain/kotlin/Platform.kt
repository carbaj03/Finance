interface Platform {
  val name: String
}

expect fun getPlatform(): Platform
expect fun localHost(): String

interface Icons {
  val avatar: String get() = "ic_mic.xml"
  val micOff: String get() = "ic_mic_off.xml"
  val micOn: String get() = "ic_mic.xml"
  val send: String get() = "ic_arrow_upward_24.xml"
  val headphones: String get() = "ic_mic.xml"
  val stop: String get() = "ic_mic.xml"
  val close: String get() = "ic_mic.xml"
  val pdf: String get() = "ic_mic.xml"
  val directory: String get() = "ic_mic.xml"
  val gallery: String get() = "ic_add_a_photo_24.xml"
  val addPhoto: String get() = "ic_mic.xml"
  val logo: String get() = "ic_mic.xml"
  val name: String get() = "ic_mic.xml"
  val check: String get() = "ic_mic.xml"
  val voice: String get() = "ic_voice.xml"
  val info: String get() = "ic_mic.xml"
  val share: String get() = "ic_mic.xml"
  val edit: String get() = "ic_mic.xml"
  val delete: String get() = "ic_mic.xml"

  val gpt35: String get() = "ic_mic.xml"
  val gpt4: String get() = "ic_mic.xml"
}

expect fun getIcons(): Icons