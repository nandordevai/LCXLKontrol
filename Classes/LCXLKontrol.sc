LCXLKontrol {
    var <faders, <knobs;
    var ctls;
    var ccFaders, ccSendAKnobs, ccSendBKnobs, ccPanKnobs;

    *new {
        ^super.new.init;
    }

    init {
        ctls = ();
        faders = List[];
        ccFaders = (77..84);
        ccSendAKnobs = (13..20);
        ccSendBKnobs = (29..36);
        ccPanKnobs = (49..56);

        MIDIClient.init;
        MIDIIn.connectAll;
        this.assignCtls;
    }

    assignCtls {
        ccFaders.do {|cc, i|
            var key = ("fader" ++ (i+1)).asSymbol;
            var lc = LCXLController(key, cc);
            faders.add(lc);
            ctls.put(key, lc);
        };
        ccSendAKnobs.do {|cc, i|
            var key = ("sendA" ++ (i+1)).asSymbol;
            var lc = LCXLController(key, cc);
            faders.add(lc);
            ctls.put(key, lc);
        };
        ccSendBKnobs.do {|cc, i|
            var key = ("sendB" ++ (i+1)).asSymbol;
            var lc = LCXLController(key, cc);
            faders.add(lc);
            ctls.put(key, lc);
        };
        ccPanKnobs.do {|cc, i|
            var key = ("pan" ++ (i+1)).asSymbol;
            var lc = LCXLController(key, cc);
            faders.add(lc);
            ctls.put(key, lc);
        };
    }

    freeAll {
        ctls.do(_.free);
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

    free {
        MIDIdef.cc(key).free;
    }
}
