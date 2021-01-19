# Synth
Synth is CRED's inbuilt library for using Neumorphic components in your app.

What really is Neumorphism? It's an impressionistic style, playing with light, shadow, and depth to create a digital experience inspired by the physical world. That's the definition anyway. Our recommendation is you try it out to see what you make of it. If you do create something with Synth, let us know. We're excited to see where you take it.  

A note for the curious: if you wish to learn more about Synth, we have a post detailing the concept and CRED's philosophy behind it [here](https://blog.cred.club/team-cred/design/world-meet-neumorphism-open-sourcing-our-ui-framework/).

![Banner](https://i.imgur.com/tKZeAwO.png "Banner")

## Install
You can install synth by adding this to your build.gradle file:

```
dependencies {
  implementation 'club.cred.android:synth:1.0.0'
}
```

## Usage & SDK Limitations

To use synth, the parent layout which contains the synth views must specify:

```xml
android:clipChildren="false"
```

Synth renders neumorphic components only on devices running API 28 (Pie) or later. This is because Synth internally uses [`BlurMaskFilter`](https://developer.android.com/reference/android/graphics/BlurMaskFilter) to render shadows and highlights which are drawn outside of the view bounds — this allows you to align Synth views with other views easily.

The issue below API 28, is, to make `BlurMaskFilter` work, we need to use [hardware acceleration](https://developer.android.com/guide/topics/graphics/hardware-accel) on the view which causes the shadows and highlights to be clipped. We could solve for this by adding padding to the views (similar to how CardView does it) but chose not to because of alignment issues.

In lieu of this, we decided to introduce "compat" version of all our views which render a simple single colored background on the view on devices below API 28.

# Buttons

## Soft button
![Soft Button](https://i.imgur.com/ih0WqFz.png "Soft Button")

Soft button renders the elevated neumorphic platform on which the text is drawn. this elevated platform can be customized in two ways:
1. By specifying a color for the platform, synth will attempt to compute the light and dark shadow colors 
```xml
<club.cred.synth.views.SynthButton
    app:neuButtonType="elevated_soft"
    app:neuPlatformColor="@color/button_platform_color"
    ... />
```

2. By specifying a complete appearance for all aspects of the elevated platform
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

## Flat button

![Flat Button](https://i.imgur.com/tgVOK5L.png "Flat Button")

Flat button renders a flat surface on top of the elevated neumorphic platform. This flat surface can be customized in two ways:
1. By specifying a color for the surface, synth will attempt to compute the gradients, borders, etc of the surface
```xml
<club.cred.synth.views.SynthButton
    app:neuButtonType="elevated_flat"
    app:neuFlatButtonColor="@colo/button_surface_color"
    app:neuPlatformAppearance="@style/button_platform_appearance" 
    ... />
```

2. By specifying a complete appearance for all aspects of the flat surface
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

## Image button

![Image Button](https://i.imgur.com/eP17O6M.png "Image Button")

Image button is simply an image view with a neumorphic platform

```xml
<club.cred.synth.views.SynthImageButton
    app:neuButtonType="elevated_soft"
    app:srcCompat="@drawable/ic_chevron_left"
    ... />
```

## Adding drawable to buttons

![Drawable Button](https://i.imgur.com/Bnjb5Cj.png "Drawable Button")

You can add a drawable to a button (to the left of the text). Synth will render a neumorphic pit on which the drawable is rendered. This pit can be customized in two ways:

1. By not specifying anything, synth will take either the `neuPlatformColor` (if it's a soft button) or `neuFlatButtonColor` (if it's a flat button) and compute the gradient colors and pressed colors.

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

2. By specifying a complete appearance for all aspects of the icon pit
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

## All button attributes
| Attribute | Description | Value |
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


# PitView and ElevatedView

![Pit and Elevated Views](https://i.imgur.com/BVirv0g.png "Pit and Elevated Views")

- `PitView` and `ElevatedView` are simple Views that can be used to simulate a debossed or embossed neumorphic platform
-  They are not ViewGroups so ideally they can be used in a `ConstraintLayout` with other Views that are constrained to the `PitView` or `ElevatedView`.
- To use these freely with your views, `PitDrawable` and `ElevatedDrawable` can be set as background for your Views programmatically.

## PitView
`PitView` shadows can be specified in two ways:
1. By specifying a color for the pit, synth will attempt to compute the shadows of the pit
```xml
<club.cred.synth.views.PitView
  app:pitColor="@color/pit_color"  
  app:neuCornerRadius="14dp"
  ... />
```

2. By specifying a complete appearance for all aspects of the pit
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

### PitView attributes

| attribute | description | value |
|--|--|--|
| `app:pitColor`| color of pit from which shadows colors will be computed | color |
| `app:pitViewAppearance` | appearance of pit | style resource |
| `app:neuCornerRadius` | corner radius of pit | dimension |
| `app:pitClipType` | edge(s) of pit which should be clipped (no shadows or corner arc will be drawn) | `no_clip`, `top`, `bottom`, `left`, `right`, `left_right`, `top_bottom` |
| `app:pitDepth` | depth of pit | dimension |

## ElevatedView

`ElevatedView` internally uses the same neumorphic platform that is used to draw the buttons. To specify the appearance and shadows of the `ElevatedView`, the same attributes  of soft button can be used:
1. By specifying `app:neuPlatformColor`, synth will compute the shadows and gradients of the view.
2. By specifying the complete appearance using `app:neuPlatformAppearance` (same as soft button).

### ElevatedView attributes

| attribute | description | value |
|--|--|--|
| `app:neuPlatformColor`| color of elevated view | color |
| `app:neuPlatformAppearance` | appearance of the elevated view (same as soft button) | style resource |
| `app:neuCornerRadius` | corner radius of the elevated view | dimension |
| `app:neuShadowOffset` | shadow offset | dimension |

## Min SDK

We support a minimum SDK of 21. But the neumorphic components will be rendered appropriately only on devices running SDK version 28 or above.

## Contributing

Pull requests are welcome! We'd love help improving this library. Feel free to browse through open issues to look for things that need work. If you have a feature request or bug, please open a new issue so we can track it.

## Contributors

Synth would not have been possible if not for the contributions made by CRED's design and frontend teams. Specifically:
- Rishab Singh Bisht
- Nikhil Panju
- Madhur Kapadia
- Siddharth
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
