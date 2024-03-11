package ua.gov.diia.core.util.html

fun convertToLink(url: String, name: String): String {
    return "<a href=\"$url\"><u>$name</u></a>"
}
fun convertToPhone(tel:String, name:String):String{
    return "<a href=\"tel:$tel\"><u>$name</u></a>"
}

fun convertToMail(mail:String, name:String):String{
    return "<a href=\"mailto:$mail\"><u>$name</u></a>"
}