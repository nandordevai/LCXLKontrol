# LCXLKontrol

Interface for using Novation LaunchControl XL with SuperCollider. Based on [NanoKontrol2](https://github.com/davidgranstrom/NanoKontrol2) by David Granström

The MIDI CC and note numbers are set according to the default template.

## Basic usage

Instantiate the class:

```sclang
~lc = LCXLKontrol();
```

Register a function to be evaluated when fader1 is changed:

```sclang
(
~lc.fader1.onChange = {|val|
  (val / 127).postln;
};
)
```

Map a global variable to a controller with optional value interpolation:

```sclang
(
~octave = 5;
~lc.fader2.mapTo("octave", 4, 7);
)
```

## Incremental assignment

It is possible to incrementally assign faders and knobs.

```sclang
(
~lc.faders.do {
    arg fader, i;
    fader.onChange = {|val|
        "Fader #% value is %\n".postf(i + 1, val);
    }
};
)
```

Or just a selection of controls

```sclang
~lc.faders[..3].do {|fader, i|
    fader.onChange = {|val|
        "Fader #% value is %\n".postf(i + 1, val);
    }
};
```

## GUI

The `LCXLGUI.sc` file contains a basic GUI implementation. It's not really usable for anything (you have your physical controller after all) just a test.

## Interface

### Methods

`onChange(func)` Calls *func* when the controller's value change.

`mapTo(variable, min, max, round)` Maps controller value to global variable *variable*, given as a string. Converts values with *linlin* to be between *min* and *max*. If *round* is true (default), rounds the value to the nearest integer.

`free()` Unregisters a MIDI responder.

`freeAll()` Unregisters all MIDI responders.

*Note: `Cmd-.` removes all MIDI responders by default in SuperCollider*

### Controller names

The following controllers are supported:

#### Faders

* `fader1 .. 8`

#### Knobs

* `sendA1 .. 8`
* `sendB1 .. 8`
* `pan1 .. 8`
* `focus1 .. 8`
* `control1 .. 8`

#### Collections

* `faders`
* `sendAKnobs`
* `sendBKnobs`
* `panKnobs`
* `focusButtons`
* `controlButtons`
