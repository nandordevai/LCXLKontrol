# LCXLKontrol

Interface for using Novation LaunchControl XL with SuperCollider. Based on [NanoKontrol2](https://github.com/davidgranstrom/NanoKontrol2) by David Granström

## Basic usage

```
~lc = LCXLKontrol();

// register a function to be evaluted when fader1 is changed
(
~lc.fader1.onChange = {
  arg val;
  (val / 127).postln;
};
)
```

## Incremental assignment

It is possible to incrementally assign faders and knobs.

```
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

```
// assign faders 1 .. 4
~lc.faders[..3].do {
    arg fader, i;
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

#### Collections

* `faders`
* `sendAKnobs`
* `sendBKnobs`
* `panKnobs`
