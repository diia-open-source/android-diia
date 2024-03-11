package ua.gov.diia.core.util.extensions.context

import android.app.DownloadManager
import android.content.ClipboardManager
import android.content.Context
import android.content.Context.AUDIO_SERVICE
import android.media.AudioManager
import android.nfc.NfcManager
import android.view.inputmethod.InputMethodManager
import androidx.core.content.ContextCompat

val Context.serviceClipboard: ClipboardManager?
   get() = getSystemService(Context.CLIPBOARD_SERVICE) as? ClipboardManager

val Context.serviceInput: InputMethodManager?
   get() = getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager

val Context.serviceNfc: NfcManager?
  get() = getSystemService(Context.NFC_SERVICE) as? NfcManager

val Context.serviceDownloadManager: DownloadManager
  get() = ContextCompat.getSystemService(this, DownloadManager::class.java)!!

val Context.audioManager: AudioManager
 get() = applicationContext.getSystemService(AUDIO_SERVICE) as AudioManager