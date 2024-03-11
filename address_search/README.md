# Description

This module is responsible address search screen and logic around it

# How to install
1. Copy module folder to your project and add module to gradle dependency like this:

```groovy
implementation project(':address_search')
```

2. Module requires next modules to work
```groovy
    implementation project(':core')
    implementation project(':ui_base')
    implementation project(':search')
```
3. Add next nav graphs to main navigation graph to use compound address search
```xml
<include app:graph="@navigation/nav_compound_address_search" />
```

4. Or extend following classes to make your custom address search screen implementation
`ua.gov.diia.address_search.ui.AddressSearchControllerF`
`ua.gov.diia.address_search.ui.AddressSearchControllerVM`