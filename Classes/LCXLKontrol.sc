LCXLKontrol {
    var <faders, <sendAKnobs, <sendBKnobs, <panKnobs;
    var <focusButtons, <controlButtons;
    var ctls, midiOut;
    var ccFaders, ccSendAKnobs, ccSendBKnobs, ccPanKnobs;
    var noteFocusButtons, noteControlButtons;

    *new {
        ^super.new.init;
    }

    init {
        ctls = ();
        faders = List[];
        sendAKnobs = List[];
        sendBKnobs = List[];
        panKnobs = List[];
        focusButtons = List[];
        controlButtons = List[];
        ccFaders = (77..84);
        ccSendAKnobs = (13..20);
        ccSendBKnobs = (29..36);
        ccPanKnobs = (49..56);
        noteFocusButtons = (41..44) ++ (57..60);
        noteControlButtons = (73..80);

        MIDIClient.init;
        MIDIIn.connectAll;
        midiOut = MIDIOut.newByName("Launch Control XL", "Launch Control XL");

        this.assignCtls;
    }

    addControl {|groupName, ctlGroup, cc, i|
        var key = (groupName ++ (i + 1)).asSymbol;
        var lc = LCXLController(key, cc);
        ctlGroup.add(lc);
        ctls.put(key, lc);
    }

    addButton {|groupName, ctlGroup, note, i|
        var key = (groupName ++ (i + 1)).asSymbol;
        var lb = LCXLButton(key, note, midiOut);
        ctlGroup.add(lb);
        ctls.put(key, lb);
    }

    assignCtls {
        ccFaders.do {|cc, i|
            this.addControl("fader", faders, cc, i);
        };
        ccSendAKnobs.do {|cc, i|
            this.addControl("sendA", sendAKnobs, cc, i);
        };
        ccSendBKnobs.do {|cc, i|
            this.addControl("sendB", sendBKnobs, cc, i);
        };
        ccPanKnobs.do {|cc, i|
            this.addControl("pan", panKnobs, cc, i);
        };
        noteFocusButtons.do {|note, i|
            this.addButton("focus", focusButtons, note, i);
        };
        noteControlButtons.do {|note, i|
            this.addButton("control", controlButtons, note, i);
        }
    }

    freeAll {
        ctls.do(MIDIdef.cc(_.key).free);
    }

    doesNotUnderstand {|selector ... args|
        ^ctls[selector] ?? { ^super.doesNotUnderstand(selector, args) }
    }
}

LCXLController {
    var key, cc;

    *new {|key, cc|
        ^super.newCopyArgs(("lcxl_" ++ key).asSymbol, cc);
    }

    onChange_ {|func|
        MIDIdef.cc(key, func, cc);
    }

    mapTo {|value, min=0, max=127, round=true|
        MIDIdef.cc(key, {|v|
            var newValue = v.linlin(0, 127, min, max);
            if (round, { newValue.round });
            currentEnvironment[value.asSymbol] = newValue;
            newValue.postln;
            value;
        }, cc);
    }
}

LCXLButton {
    var key, note, midiOut;

    *new {|key, note, midiOut|
        ^super.newCopyArgs(("lcxl_" ++ key).asSymbol, note, midiOut);
    }

    onPress_ {|func|
        MIDIdef.noteOn(key, func, note);
    }
}
