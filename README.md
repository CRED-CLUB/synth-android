
# Synth
Synth is a library for using neumorphic components in your app.
Neumorphism is an impressionistic style, playing with light, shadow, and depth to create a digital experience inspired by the physical world. It’s most visible in “plastic” building blocks: extruded, haptic elements that provide visual and tactical cues about their function. We are excited about neumorphism not only for its stunning appearance but also for the ease with which it allows members to use and distinguish between elements.


![Banner](https://i.imgur.com/tKZeAwO.png "Banner")

## install
you can install synth by adding this to your build.gradle file

```
dependencies {
  implementation 'club.cred.android:synth:1.0.0'
}
```

## usage
the neumorphic components are only rendered on devices running API 28 (Pie) or later due to rendering issues on older devices. on these devices synth renders a less sophisticated view with no shadows or blurs (NSB).

to use synth, the parent layout which contains the synth views must specify:
```
android:clipChildren="false"
```

# buttons

## soft button
![Soft Button](https://i.imgur.com/ih0WqFz.png "Soft Button")

soft button renders the elevated neumorphic platform on which the text is drawn. this elevated platform can be customized in two ways:
1. by specifiying a color for the platform, synth will attempt to compute the light and dark shadow colors 
```xml
<club.cred.synth.views.SynthButton
    app:neuButtonType="elevated_soft"
    app:neuPlatformColor="@color/button_platform_color"
    ... />
```

2. by specifiying a complete apperance for all aspects of the elevated platform
```xml
<club.cred.synth.views.SynthButton
    app:neuButtonType="elevated_soft"
    ... />
    
<style name="button_platform_appearance">
    <item name="neuLightShadowColor">...</item>
    <item name="neuDarkShadowColor">...</item>
    <item name="neuFillStartColor">...</item>
    <item name="neuFillEndColor">...</item>
    <item name="neuFillPressedStartColor">...</item>
    <item name="neuFillPressedEndColor">...</item>
    <item name="neuBorderStartColor">...</item>
    <item name="neuBorderEndColor">...</item>
    <item name="neuBorderPressedStartColor">...</item>
    <item name="neuBorderPressedEndColor">...</item>
</style>
```

## flat button

![Flat Button](https://i.imgur.com/tgVOK5L.png "Flat Button")

flat button renders a flat surface on top of the elevated neumorphic platform. this flat surface can be customized in two ways:
1. by specifiying a color for the surface, synth will attempt to compute the gradients, borders, etc of the surface
```xml
<club.cred.synth.views.SynthButton
    app:neuButtonType="elevated_flat"
    app:neuFlatButtonColor="@colo/button_surface_color"
    app:neuPlatformAppearance="@style/button_platform_appearance" 
    ... />
```

2. by specifiying a complete apperance for all aspects of the flat surface
```xml
<club.cred.synth.views.SynthButton
    app:neuButtonType="elevated_flat"
    app:neuFlatButtonAppearance="@style/button_flat_appearance"
    
    app:neuPlatformAppearance="@style/button_platform_appearance" 
    ... />
    
<style name="button_flat_appearance">
    <item name="neuButtonStartColor">...</item>
    <item name="neuButtonEndColor">...</item>
    <item name="neuButtonBorder1StartColor">...</item>
    <item name="neuButtonBorder1EndColor">...</item>
    <item name="neuButtonBorder2StartColor">...</item>
    <item name="neuButtonBorder2EndColor">...</item>
    <item name="neuButtonPressedDarkShadowColor">...</item>
</style>
```

## image button

![Image Button](https://i.imgur.com/eP17O6M.png "Image Button")

image button is simply an image view with a neumorphic platform

```xml
<club.cred.synth.views.SynthImageButton
    app:neuButtonType="elevated_soft"
    app:srcCompat="@drawable/ic_chevron_left"
    ... />
```

## adding drawable to buttons

![Drawable Button](https://i.imgur.com/Bnjb5Cj.png "Drawable Button")

you can add a drawable to a button (to the left of the text). synth will render a neumorphic pit on which the drawable is rendered. this pit can be custonmized in two ways:

1. by not specifiying anything, synth will take either the `neuPlatformColor` (if it's a soft button) or `neuFlatButtonColor` (if it's a flat button) and compute the gradient colors and pressed colors.

```xml
<club.cred.synth.views.SynthButton
    app:neuButtonType="elevated_soft"
    app:neuButtonDrawable="@drawable/ic_plus"
    app:neuButtonDrawablePitRadius="20dp"
    
    app:neuPlatformColor="@color/button_platform_color"
    .. or ..
    app:neuFlatButtonColor="@color/button_surface_color"
    ... />
```

2. by specifiying a complete apperance for all aspects of the icon pit
```xml
<club.cred.synth.views.SynthButton
    app:neuButtonType="elevated_flat"
    app:neuButtonDrawable="@drawable/ic_plus"
    app:neuButtonDrawablePitRadius="20dp"
    
    app:neuButtonIconAppearance="@style/button_icon_appearance"
    ... />
    
<style name="button_icon_appearance">
    <item name="neuFillStartColor">...</item>
    <item name="neuFillEndColor">...</item>
    <item name="neuFillPressedStartColor">...</item>
    <item name="neuFillPressedEndColor">...</item>
    <item name="neuButtonCompatColor">...</item>
</style>
```

## all button attributes
| attribute | description | value |
|--|--|--|
|`app:neuButtonType`| type of the button | `elevated_flat` or `elevated_soft` |
|`app:neuButtonRadius` | corner radius of button | dimension |
| `app:neuPlatformColor` | color of neumorphic platform | color |
| `app:neuPlatformAppearance` | appearance of neumorphic platform | style resource |
| `app:neuFlatButtonColor` | color of flat button surface | color |
| `app:neuFlatButtonAppearance` | appearance of flat button surface | style resource |
| `app:neuButtonDrawable` | drawable resource of left icon | drawable resource |
| `app:neuButtonDrawablePitRadius` | radius of the pit behind the icon | dimension |
| `app:neuButtonIconAppearance` | appearance of the pit behind the icon | style resource |
| `app:neuButtonCompatColor` | color of button on compat devices | color |


## `PitView` and `ElevatedView`

![Pit and Elevated Views](https://i.imgur.com/BVirv0g.png "Pit and Elevated Views")

- `PitView` and `ElevatedView` are simple Views that can be used to simulate a debossed or embossed neumorphic platform
-  they are not ViewGroups so ideally they can be used in a `ConstraintLayout` with other Views that are constrained to the `PitView` or `ElevatedView`.

## `PitView`
`PitView` shadows can be specified in two ways:
1. by specifiying a color for the pit, synth will attempt to compute the shadows of the pit
```xml
<club.cred.synth.views.PitView
  app:pitColor="@color/pit_color"  
  app:neuCornerRadius="14dp"
  ... />
```

2. by specifiying a complete apperance for all aspects of the pit
```xml
<club.cred.synth.views.PitView
  app:pitViewAppearance="@style/pit_view_appearance"  
  app:neuCornerRadius="14dp"
  ... />
    
<style name="pit_view_appearance">
    <item name="neuLightShadowColor">...</item>
    <item name="neuDarkShadowColor">...</item>
    <item name="softEdgeColor">...</item>
    <item name="neuCompatColor">...</item>
</style>
```

### `PitView` attributes

| attribute | description | value |
|--|--|--|
| `app:pitColor`| color of pit from which shadows colors will be computed | color |
| `app:pitViewAppearance` | appeareance of pit | style resource |
| `app:neuCornerRadius` | corner radius of pit | dimension |
| `app:pitClipType` | edge(s) of pit which should be clipped (no shadows or corner arc will be drawn) | `no_clip`, `top`, `bottom`, `left`, `right`, `left_right`, `top_bottom` |
| `app:pitDepth` | depth of pit | dimension |

## `ElevatedView` 

`ElevatedView` internally uses the same neumorphic platform that is used to draw the buttons. To specify the appearance and shadows of the `ElevatedView`, the same attributes  of soft button can be used:
1. By specifying `app:neuPlatformColor`, synth will compute the shadows and gradients of the view.
2. By specifying the complete appearance using `app:neuPlatformAppearance` (same as soft button).

### `ElevatedView` attributes

| attribute | description | value |
|--|--|--|
| `app:neuPlatformColor`| color of elevated view | color |
| `app:neuPlatformAppearance` | appeareance of the elevated view (same as soft button) | style resource |
| `app:neuCornerRadius` | corner radius of the elevated view | dimension |
| `app:neuShadowOffset` | shadow offset | dimension |

## Min SDK

We support a minimum SDK of 21. But the neumorphic components will be rendered appropriately only on devices running SDK version 28 or above.

## Contributing

Pull requests are welcome! We'd love help improving this library. Feel free to browse through open issues to look for things that need work. If you have a feature request or bug, please open a new issue so we can track it.

## Contributors

Synth would not have been possible if not for the contributions made by:
- Rishabh Singh Bisht
- Nikhil Panju
- Madhur Kapadia
- Sidharth
- Hari Krishna

## License

```
Copyright 2020 Dreamplug Technologies Private Limited.

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

   http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```





