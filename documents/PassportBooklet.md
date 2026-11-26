# Passport booklet template proposal

This adds a reusable Compose presentation component in `:doc:documents` for a
Ukrainian passport booklet. It is a foundation for a document feature, not an
enabled passport document type. It does not add a registry endpoint, persistence,
document verification, sharing, or an entry in the application's document gallery.

The visual arrangement follows an open booklet: a title and issuance page on the
left, and a photograph and identity page on the right. The background and flag are
drawn natively; no sample portrait, personal data, stamp or security artwork is bundled.

## Binding data

The flow is registry/database → authenticated backend → existing document repository
and mapper → `PassportBookletData` plus decoded images → `PassportBooklet`.
The mobile component has no database access. The presentation model below is a proposed
shared Swift/Kotlin shape, not a declaration of an existing Diia backend contract.

```json
{
  "identity": {
    "lastName": "ПРИКЛАД",
    "firstName": "Надія",
    "middleName": "Тестівна",
    "birthDate": "24.08.1991",
    "birthPlace": "м. Київ",
    "sex": null
  },
  "issuance": {
    "series": "АА",
    "number": "000000",
    "issuedAt": "24.08.2007",
    "issuedBy": "Демонстраційний орган видачі",
    "validUntil": null
  },
  "additionalFields": [{"label": "Додаткові дані", "value": "Демонстраційне значення"}]
}
```

The mapper must supply display-ready date strings and localized extra field labels.
Document numbers remain strings to preserve leading zeros. An absent patronymic,
sex, expiry date, signature or extra field list is supported; do not invent values.
Decode registry image content using the existing image pipeline outside composition.
Pass `null` when a photo is missing or fails decoding; the component displays a
localized placeholder. Image loading failures must not reuse a previous person's photo.

```kotlin
// `mappedData`, `decodedPhoto`, and `decodedSignature` come from observable repository
// state and the existing document image pipeline. Images are ImageBitmap? values.
PassportBooklet(
    data = mappedData,
    photo = decodedPhoto,
    signature = decodedSignature,
    modifier = Modifier.fillMaxSize(),
    pageGap = 1.dp
)
```

The model uses the module's existing Moshi code generation. When adapting an actual
server response, map it to this presentation model rather than changing the existing
document parser to assume this JSON schema. Replace inputs on repository updates;
no personal data is retained in component state. The host must distinguish loading,
unavailable, expired or unverified data before presenting the template. Rendering
data does not give it document validity.

## Layout and accessibility

The component fills its host and adds no toolbar or tab bar. At 600 dp of usable
width it displays equally sized pages side by side, each with independent vertical
scrolling. Otherwise the pages stack in one scroll view. Text follows system font
scaling; names and issuer details wrap without truncation. Photo and signature
have accessibility labels and field labels are grouped with values.

For a centered separating hinge, the host may set `pageGap` from its window layout
information. For an off-center hinge or horizontal fold, the host must supply a
suitable container; the component does not discover device hinge geometry. Safe
insets, system bars, orientation, screen protection and an accessible dismissal
action remain host concerns. No Activity's fullscreen flags are changed.

## Review and validation

Use the Compose preview for sample data. Also check a narrow phone, a wide tablet,
large accessibility text, long names/issuer/place text, missing photo and signature,
updated images, and optional fields omitted by the server.

```sh
bash gradlew :doc:documents:compileGplayDebugKotlin :doc:documents:testGplayDebugUnitTest
```

`PassportBookletDataTest` covers leading zeros, whitespace, absent optional fields
and immutable data replacement. `PassportBookletJsonTest` exercises the generated
Moshi adapters, Unicode, omitted optional fields, round trips and required-field
failures. Gradle validation requires the Android SDK and the repository's dependencies.

Before enabling an actual document, maintainers need to agree on the document code,
API mapping, status/expiry handling, approved design/assets, feature availability,
gallery routing and verification/share behavior. `CONTRIBUTING.md` currently directs
new feature proposals through Issues; this implementation is intended for discussion.
