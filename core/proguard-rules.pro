# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.kts.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

# Uncomment this to preserve the line number information for
# debugging stack traces.
#-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile
-keep enum * { *; }

-keep public class ua.gov.diia.core.models.ContextMenuField
-keep public class ua.gov.diia.core.models.notification.pull.PullNotificationItemSelection
-keep public class ua.gov.diia.core.models.notification.pull.EmptySelection
-keep public class ua.gov.diia.core.models.SystemDialog
-keep public class ua.gov.diia.core.models.dialogs.TemplateDialogModel
-keep public class ua.gov.diia.core.models.ConsumableItem
-keep public class ua.gov.diia.core.models.DiiaError
-keep public class ua.gov.diia.core.models.acquirer.AcquirerLinkType
-keep public class ua.gov.diia.core.models.rating_service.**{*;}
-keep,allowobfuscation,allowshrinking interface retrofit2.Call
-keep,allowobfuscation,allowshrinking class retrofit2.Response
-keep,allowobfuscation,allowshrinking class kotlin.coroutines.Continuation
