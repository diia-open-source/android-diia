package ua.gov.diia.core.models.media

enum class MediaFileType {
    IMAGE, VIDEO;

    val ime: String
      get() = when(this){
          IMAGE -> "image/*"
          VIDEO -> "video/*"
      }
}