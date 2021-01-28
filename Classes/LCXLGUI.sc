~lc = LCXLKontrol.new;
~knobColor = Color.hsv(0, 0, 0.65);

(
w = Window.new("LCXLControl").front;
w.addFlowLayout(15@15, 7@7);
3.do { |row|
    8.do { |item, i|
        var knob;
        var color;
        var colors = [Color.red, Color.yellow, Color.green, Color.hsv(0.125, 0.9, 1)];
        color = colors[(i / 2).floor];
        knob = Knob(w, 40@40);
        knob.color = [~knobColor, Color.hsv(0, 0, 0.15), ~knobColor, color];
        switch (row,
            0, {
                ~lc.sendAKnobs[i].onChange = {|val|
                    {knob.value = val / 127}.defer;
                }
            },
            1, {
                ~lc.sendBKnobs[i].onChange = {|val|
                    {knob.value = val / 127}.defer;
                }
            },
            2, {
                ~lc.panKnobs[i].onChange = {|val|
                    {knob.value = val / 127}.defer;
                }
            }
        )
    };
};
8.do {|i|
    var slider;
    slider = Slider(w, 40@180);
    ~lc.faders[i].onChange = {|val|
        {slider.value = val / 127}.defer;
    };
};
2.do {|row|
    8.do {|i|
        var button;
        button = Button(w, 40@20)
        .states_([
            [""],
            ["", Color.black, Color.green]
        ]);
        if (row == 0, {
            ~lc.focusButtons[i].onPress = {|note|
                {if (button.value == 0, {
                    button.value = 1;
                }, {
                    button.value = 0;
                })}.defer;
            };
        }, {
            ~lc.controlButtons[i].onPress = {|note|
                {if (button.value == 0, {
                    button.value = 1;
                }, {
                    button.value = 0;
                })}.defer;
            };
        })
    };
};
)

(
~lc.sendA1.onChange = {|val|
    { ~knobs[0].value = val / 127 }.defer;
};
)