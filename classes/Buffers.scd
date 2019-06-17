Buffers
need simple kb recorder midi sample iano I guess


SampleBank.populate;
SampleBank.warmUp;
SampleBank.tempo_(122/60);

a= SampleBank.at(\drums).buffer;
b= SampleBank.at(\vox).buffer;  // b is a buffer
c= SampleBank.at(\bass).buffer;
d= SampleBank.at(\bvs).buffer;

b.bufnum
c.bufnum
a.duration
SampleBank.secsToBeats(a.duration)
[\out, 0, \bufnum, b].inspect
[\out, 0, \bufnum, b].at(3)
[\out, 0, \bufnum, b].at(2)


// read a whole sound into memory
b = Buffer.read(s, Platform.resourceDir +/+ "sounds/a11wlk01.wav");
// remember to free the buffer later.

SynthDef(\help_PlayBuf, {| out = 0, bufnum = 0 |
    Out.ar(out,
        PlayBuf.ar(1, bufnum, BufRateScale.kr(bufnum), doneAction: Done.freeSelf)
    )
}).play(s, [\out, 0, \bufnum, b])

In the above example, note how the doneAction: Done.freeSelf causes the synth to free itself when the buffer reaches its end.

Note again that the number of channels must be fixed for the SynthDef. It cannot vary depending on which buffer you use.

// loop is true
SynthDef(\help_PlayBuf, {| out = 0, bufnum = 0 |
    Out.ar(out,
        PlayBuf.ar(1, bufnum, BufRateScale.kr(bufnum), loop: 0)
    )
}).play(s, [\out, 0, \bufnum, b]);


// WHOLE THING HERE!!!!!! - NO!
// trigger one shot on each pulse
z = SynthDef(\help_PlayBuf, {| out = 0, bufnum = 0 |
    var trig;
    trig = Impulse.kr(freq: 1,  phase: 0);   //Freq and phase
	                                            // like end trim with loop - impulse is the end
	                                            // start pos is the start probably
	                                             //one off start pos  mightwould be anchor//
	                                              // phase is not anchor ?? maybe just use stop?
    Out.ar(out,
        PlayBuf.ar(1, bufnum, BufRateScale.kr(bufnum), trig, 0, 0)
    )
});
z.play(s, [\out, 0, \bufnum,c]);


// trigger one shot on each pulse - exponential!!
SynthDef(\help_PlayBuf, {| out = 0, bufnum = 0 |
    var trig;
    trig = Impulse.kr(XLine.kr(0.1, 100, 30));
    Out.ar(out,
        PlayBuf.ar(1, bufnum, BufRateScale.kr(bufnum), trig, 5000, 0)
    )
}).play(s, [\out, 0, \bufnum, b]);


// mouse HEIGHT control of trigger rate and  X control of start posstartpos
// whole thing here - freq is total trim width
                                     // start pos is start of trim
                                    // phase is anchor
SynthDef(\help_PlayBuf, { arg out=0, bufnum=0;
    var trig;
    trig = Impulse.kr(MouseY.kr(0.25, 200, 1), 0.5);
    Out.ar(out,
        PlayBuf.ar(1, bufnum, BufRateScale.kr(bufnum), trig, MouseX.kr(0, BufFrames.kr(bufnum)), 1)
    )
}).play(s, [\out, 0, \bufnum, b]);


// mouse HEIGHT control of startpos - EH?
SynthDef(\help_PlayBuf, { arg out=0, bufnum=0;
    var trig, trig2;
	 trig = Impulse.kr(freq: 0.5,  phase: 0);
    Out.ar(out,
        PlayBuf.ar(1, bufnum, BufRateScale.kr(bufnum), trig, MouseX.kr(0, BufFrames.kr(bufnum)), 1)
    )
}).play(s, [\out, 0, \bufnum, b]);




// accelerating pitch
SynthDef(\help_PlayBuf, {| out = 0, bufnum = 0 |
    var rate;
    rate = XLine.kr(0.1, 100, 60);
    Out.ar(out,
        PlayBuf.ar(1, bufnum, BufRateScale.kr(bufnum)*rate, 1.0, 0.0, 1.0)
    )
}).play(s, [\out, 0, \bufnum, b]);


// sine wave control of playback rate. negative rate plays backwards
SynthDef(\help_PlayBuf, {| out = 0, bufnum = 0 |
    var rate;
    rate = FSinOsc.kr(XLine.kr(0.2, 8, 30), 0, 3, 0.6);
    Out.ar(out,
        PlayBuf.ar(1, bufnum, BufRateScale.kr(bufnum) * rate, 1, 0, 1)
    )
}).play(s, [\out, 0, \bufnum, b]);


// zig zag around sound
SynthDef(\help_PlayBuf, {| out = 0, bufnum = 0 |
    var rate;
    rate = LFNoise2.kr(XLine.kr(1, 20, 60), 2);
    Out.ar(out,
        PlayBuf.ar(1, bufnum, BufRateScale.kr(bufnum) * rate, 1, 0, 1)
    )
}).play(s, [\out, 0, \bufnum, b]);

b.free;


// https://www.bartetzki.de/docs/kita_sc08/examples_8_(envs+trigs).rtf

// DetectSilence:
(
SynthDef("detect", { arg freq=300;
	var env, trig, sig, thresh;
	thresh = 0.1;
	env = LFNoise2.kr(Rand(1, 2)).range(0, 1);
	DetectSilence.kr(env, thresh, 0, doneAction: 2);
	trig = Impulse.ar(freq * 0.01);
	trig = Decay.ar(trig, (freq * 0.03).reciprocal);
	sig = Resonz.ar(PinkNoise.ar(trig * 20), freq, 0.01) ;
	sig = Pan2.ar(sig * env, Rand(-1.0, 1.0));

	Out.ar(0, sig);
}).play
)

// Might let me do loop off.
// how about the hard stop thing?
/ Linen as a simple and compact fade-in+out envelope generator
(
SynthDef("test1", { arg gate=1;
	var env, sig;
	// a simple envelope:
	env = Linen.kr(gate, 0.1, 1, 1.0, doneAction: 2); 	// doneAction=2  ->  free the synth when done
	sig = BrownNoise.ar(0.1);
	sig = CombN.ar(sig, 0.1, LFTri.kr([0.2, 0.21]).range(0.015, 0.03), 2.0);
	Out.ar(0, sig * env);
}).send(s)
)

