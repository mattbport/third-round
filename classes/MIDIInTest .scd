// just match ports in ac driver & make sure  kb plugged in - no virtial wores to connect

// in audio midi setup
MIDI.scd

(
var notes, on, off;

MIDIClient.init;
MIDIIn.connectAll;

notes = Array.newClear(128);    // array has one slot per possible MIDI note
                        // in which a synth gets created stored  & played by MIDIFunc.noteOn

on = MIDIFunc.noteOn({ |veloc, num, chan, src|
    notes[num] = Synth(\default, [\freq, num.midicps,
        \amp, veloc * 0.00315]);

// MIDIFunc. is the optimal way to receive the most typical MIDI messages: note on/off, controller, pitch bend, aftertouch, poly-touch and program change.

});

off = MIDIFunc.noteOff({ |veloc, num, chan, src|
    notes[num].release;         // basically frees the synth for that note stored the note array
});

// jus tthe above does midi

MIDIdef.noteOn(\print, {arg val, num, chan, src; [src,chan, num, val].postln});
// Related to MIDIFunc, this class keeps several MIDIFunc objects in global storage, by name. Especially helpful for live or interactive use.

// creates an instance  instance  which responds to a note on with this closure

// BASICALLY WE JUST HACK THIS CLOSURE TO GET WHATEVER BEHAVIOUR WE WANT
//what synths d we have? get some nice ones....


q = { on.free; off.free; };
)

// when done:

MIDIClient.sources;
MIDIClient.destinations;

MIDIFunc and MIDIdef
For most uses, the preferred way to receive MIDI input is using the MIDIFunc and MIDIdef classes. The advantage of this approach is that any number of responders can be registered, using extremely flexible matching.
MIDIFunc has a number of convenience methods allowing you to register for the different MIDI message types. It can filter incoming MIDI messages to respond to a particular device, channel number, or specific message number, or ranges thereof.
See Playing notes on your MIDI keyboard below for a simple example using the note-on and note-off MIDIFuncs.
