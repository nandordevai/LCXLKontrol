LCXLKontrol {
    var <faders, <sendAKnobs, <sendBKnobs, <panKnobs;
    var ctls;
    var ccFaders, ccSendAKnobs, ccSendBKnobs, ccPanKnobs;

    *new {
        ^super.new.init;
    }

    init {
        ctls = ();
        faders = List[];
        sendAKnobs = List[];
        sendBKnobs = List[];
        panKnobs = List[];
        ccFaders = (77..84);
        ccSendAKnobs = (13..20);
        ccSendBKnobs = (29..36);
        ccPanKnobs = (49..56);

        MIDIClient.init;
        MIDIIn.connectAll;
        this.assignCtls;
    }

    addControl {
        arg groupName, ctlGroup, cc, i;

        var key = (groupName ++ (i + 1)).asSymbol;
        var lc = LCXLController(key, cc);
        ctlGroup.add(lc);
        ctls.put(key, lc);
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

    *new {
        arg key, cc;

        ^super.newCopyArgs(("lcxl_" ++ key).asSymbol, cc);
    }

    onChange_ {
        arg func;

        MIDIdef.cc(key, func, cc);
    }

    free {
        MIDIdef.cc(key).free;
    }
}
