package ua.gov.diia.ui_base.components.infrastructure.state

sealed class UIState {

    sealed class Validation : UIState() {
        object NeverBeenPerformed: Validation()
        object Passed : Validation()
        object Failed : Validation()
        object InProgress : Validation()
    }

    sealed class Focus : UIState() {
        object NeverBeenFocused : Focus()
        object FirstTimeInFocus : Focus()
        object InFocus : Focus()
        object OutOfFocus : Focus()
    }

    sealed class Interaction : UIState() {
        object Enabled : Interaction()
        object Disabled : Interaction()

        fun reverse(): Interaction{
            return when (this){
                Disabled -> Enabled
                Enabled -> Disabled
            }
        }
    }

    sealed class Selection : UIState() {
        object Selected : Selection()
        object Unselected : Selection()

        fun reverse(): Selection{
            return when (this){
                Selected -> Unselected
                Unselected -> Selected
            }
        }
    }

    sealed class Expand : UIState() {
        object Expanded : Expand()
        object Collapsed : Expand()

        fun reverse(): Expand {
            return when (this){
                Collapsed -> Expanded
                Expanded -> Collapsed
            }
        }
    }

    sealed class Progress : UIState() {
        object NotDownloaded : Progress()
        object InProgress : Progress()
        object Failed : Progress()
        object Downloaded : Progress()
        object NotAvailable : Progress()
        object UpdateAvailable : Progress()
    }

    sealed class MediaUploadState : UIState() {
        object Loaded : MediaUploadState()
        object InProgress : MediaUploadState()
        object FailedLoading : MediaUploadState()
    }
}

fun UIState.Selection.reverse() = when (this) {
    UIState.Selection.Selected -> UIState.Selection.Unselected
    UIState.Selection.Unselected -> UIState.Selection.Selected
}