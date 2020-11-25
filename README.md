# LCXLKontrol

Interface for using Novation LaunchControl XL with SuperCollider. Based on [NanoKontrol2](https://github.com/davidgranstrom/NanoKontrol2) by David Granström

The MIDI CC and note numbers are set according to the default template.

## Basic usage

```sclang
~lc = LCXLKontrol();

// register a function to be evaluted when fader1 is changed
(
~lc.fader1.onChange = {|val|
  (val / 127).postln;
};
)
```

## Incremental assignment

It is possible to incrementally assign faders and knobs.

```sclang
~lc = LCXLKontrol();

(
~lc.faders.do {
    arg fader, i;
    fader.onChange = {|val|
        "Fader #% value is %\n".postf(i + 1, val);
    }
};
```

Or just a selection of controls

```sclang
// assign faders 1 .. 4
~lc.faders[..3].do {|fader, i|
    fader.onChange = {|val|
        "Fader #% value is %\n".postf(i + 1, val);
    }
};
```

## Interface

### Methods

`onChange` all controls (faders/knobs/buttons) can register a function using this method

`free` unregisters a MIDI responder

`freeAll` unregisters all MIDI responders

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
