LCXLKontrol {
    var <faders, <knobs;
    var <fBtns, <cBtns;
    var ctls, midiOut;
    var ccFaders, ccSendAKnobs, ccSendBKnobs, ccPanKnobs;
    var ccFocusBtns, ccControlBtns;
    var ccDirectionBtns, ccSelectBtns;

    init {
        ctls = ();

        faders = List[];
        knobs = List[];
        focusBtns = List[];
        controlBtns = List[];

        ccFaders = (0..7);
        ccSendAKnobs = (8..15);
        ccSendBKnobs = (16..23);
        ccPanKnobs = (24..31);
        ccFocusBtns = (32..39);
        ccControlBtns = (40..47);
        ccDirectionBtns = (48..51);
        ccSelectBtns = (52..55)

        MIDIClient.init;
        MIDIIn.connectAll;
        midiOut = MIDIOut.newByName("Launch Control XL");

        this.assignCtls;
    }

    assignCtls {
        ccFaders.do {|cc, i|
            var key = ("fader" ++ (i+1)).asSymbol;
            var nk = LCXLController(key, cc);
            // easier incremental assignment
            faders.add(nk);
            // convenience method for accessing individual faders
            ctls.put(key, nk);
        };

        ccSendAKnobs.do {|cc, i|
            var key = ("knob_a" ++ (i+1)).asSymbol;
            var nk = LCXLController(key, cc);
            knobs.add(nk);
            ctls.put(key, nk);
        };

        ccSendBKnobs.do {|cc, i|
            var key = ("knob_b" ++ (i+1)).asSymbol;
            var nk = LCXLController(key, cc);
            knobs.add(nk);
            ctls.put(key, nk);
        };

        ccPanKnobs.do {|cc, i|
            var key = ("knob_pan" ++ (i+1)).asSymbol;
            var nk = LCXLController(key, cc);
            knobs.add(nk);
            ctls.put(key, nk);
        };

        ccFocusBtns.collect {|cc, i|
            var key = ("fBtn" ++ (i+1)).asSymbol;
            var nk = LCXLButton(key, cc, midiOut);
            sBtns.add(nk);
            ctls.put(key, nk);
        };

        ccControlBtns.collect {|cc, i|
            var key = ("cBtn" ++ (i+1)).asSymbol;
            var nk = LCXLButton(key, cc, midiOut);
            mBtns.add(nk);
            ctls.put(key, nk);
        };

        [ [ 'upBtn', 'downBtn', 'leftBtn', 'rightBtn' ], ccTransportBtns ].flopWith {|key, cc|
            ctls.put(key, LCXLButton(key, cc, midiOut));
        };

        [ [ 'deviceBtn', 'muteBtn', 'soloBtn', 'armBtn' ], ccSelectBtns ].flopWith {|key, cc|
            ctls.put(key, LCXLButton(key, cc, midiOut));
        };
    }

    freeAll {
        ctls.do(_.free);
    }

    ledsOff {
        ctls.do(_.ledOff);
    }

    doesNotUnderstand {|selector ... args|
        ^ctls[selector] ?? { ^super.doesNotUnderstand(selector, args) }
    }
}

LCXLController {
    var key, cc, midiOut;
    var state = 0;

    *new {|key, cc|
        ^super.newCopyArgs(("lcxl_" ++ key).asSymbol, cc);
    }

    onChange_ {|func|
        MIDIdef.cc(key, func, cc);
    }

    free {
        MIDIdef.cc(key).free;
        this.ledOff;
    }
}

LCXLButton : LCXLController {
    var key, cc;

    *new {|key, cc, aMidiOut|
        ^super.newCopyArgs(("lcxl_" ++ key).asSymbol, cc, aMidiOut);
    }

    onPress_ {|func|
        MIDIdef.cc((key ++ "_on").asSymbol, {|val|
            if (val == 127) {
                func.(val, this)
            }
        }, cc);
    }

    onRelease_ {|func|
        MIDIdef.cc((key ++ "_off").asSymbol, {|val|
            if (val == 0) {
                func.(val, this)
            }
        }, cc);
    }

    ledState {
        ^state;
    }

    ledState_ {|val|
        val  = val.clip(0, 1);
        state = val;

        midiOut !? {
            midiOut.control(0, cc, 127 * val);
        };
    }
}
