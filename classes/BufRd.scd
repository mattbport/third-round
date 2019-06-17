BufRd		buffer reading oscillator

read the content of a buffer at an index.
see also BufWr


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



BufRd.ar(numChannels, bufnum, phase, loop, interpolation)

	numChannels 	number of channels that the buffer will be.
				this must be a fixed integer. The architecture of the SynthDef
				cannot change after it is compiled.
				warning: if you supply a bufnum of a buffer that has a different
				numChannels then you have specified to the BufRd, it will
				fail silently.

	bufnum		the index of the buffer to use
	phase   		audio rate modulatable index into the buffer.
	loop      		1 means true, 0 means false.  this is modulatable.
	interpolation    1 means no interpolation, 2 is linear, 4 is cubic interpolation



in comparison to PlayBuf:
	PlayBuf plays through the buffer by itself,
	BufRd only moves its read point by the phase input
		and therefore has no pitch input

	BufRd has variable interpolation


(
// read a whole sound into memory
s = Server.local;
// note: not *that* columbia, the first one
s.waitForBoot{
	s.sendMsg("/b_allocRead", 0, "sounds/a11wlk01.wav");
}
)

//use any AUDIO rate ugen as an index generator

{ BufRd.ar(1, 86, SinOsc.ar(0.1) * BufFrames.ir(0),1) }.play;
{ BufRd.ar(1, 86, SinOsc.ar(0.1) * BufFrames.ir(0),0) }.play;

{ BufRd.ar(1, 0, LFNoise1.ar(1) * BufFrames.ir(0)) }.play;
{ BufRd.ar(1, 0, LFNoise1.ar(10) * BufFrames.ir(0)) }.play;
{ BufRd.ar(1, 0, LFTri.ar(0.1) + LFTri.ar(0.23) * BufFrames.ir(0)) }.play;
// original duration
{ BufRd.ar(1, 0, LFSaw.ar(BufDur.ir(0).reciprocal).range(0, BufFrames.ir(0)) ) }.play;


//use a phasor index into the file

{ BufRd.ar(1, 95, Phasor.ar(0, BufRateScale.kr(0), 0, BufFrames.kr(0) ), 1) }.play;  //repeats immediatly on hitting end
{ BufRd.ar(1, 95, Phasor.ar(0, BufRateScale.kr(0), 0, BufFrames.kr(0) ), 0) }.play;  // repeats after equiv period silence

//change rate and interpolation
(
x = { arg rate=1, inter=2;
	BufRd.ar(1, 86, Phasor.ar(0, BufRateScale.kr(0) * rate, 0, BufFrames.kr(0)), 1, inter)
}.play;
)

x.set(\rate, 0.9);
x.set(\rate, 0.6);
x.set(\inter, 1);
x.set(\inter, 0);


//write into the buffer with a BufWr
(
y = { arg rate=1;
	var in;
	in = SinOsc.ar(LFNoise1.kr(2, 300, 400), 0, 0.1);
	BufWr.ar(in, 0, Phasor.ar(0, BufRateScale.kr(0) * rate, 0, BufFrames.kr(0)));
	0.0 //quiet
}.play;
)

//read it with a BufRd
(
x = { arg rate=1;
	BufRd.ar(1, 0, Phasor.ar(0, BufRateScale.kr(0) * rate, 0, BufFrames.kr(0)))
}.play;
)



x.set(\rate, 5);
y.set(\rate, 2.0.rand);
x.set(\rate, 2);







(
// read a whole sound into memory
s = Server.local;
// note: not *that* columbia, the first one
b = Buffer.read(s, Platform.resourceDir +/+ "sounds/a11wlk01.wav");
)

//use any AUDIO rate ugen as an index generator

{ BufRd.ar(1, b, SinOsc.ar(0.1) * BufFrames.ir(b)) }.play;
{ BufRd.ar(1, b, LFNoise1.ar(1) * BufFrames.ir(b)) }.play;
{ BufRd.ar(1, b, LFNoise1.ar(10) * BufFrames.ir(b)) }.play;
{ BufRd.ar(1, b, LFTri.ar(0.1) + LFTri.ar(0.23) * BufFrames.ir(b)) }.play;
// original duration
{ BufRd.ar(1, b, LFSaw.ar(BufDur.ir(b).reciprocal).range(0, BufFrames.ir(b)) ) }.play;


//use a phasor index into the file

{ BufRd.ar(1, b, Phasor.ar(0, BufRateScale.kr(b), 0, BufFrames.kr(b))) }.play;


//change rate and interpolation
(
x = { arg rate=1, inter=2;
    BufRd.ar(1, b, Phasor.ar(0, BufRateScale.kr(b) * rate, 0, BufFrames.kr(b)), 1, inter)
}.play;
)

x.set(\rate, 0.9);
x.set(\rate, 0.6);
x.set(\inter, 1);
x.set(\inter, 0);


//write into the buffer with a BufWr
(
y = { arg rate=1;
    var in;
    in = SinOsc.ar(LFNoise1.kr(2, 300, 400), 0, 0.1);
    BufWr.ar(in, b, Phasor.ar(0, BufRateScale.kr(b) * rate, 0, BufFrames.kr(b)));
    0.0 //quiet
}.play;
)

//read it with a BufRd
(
x = { arg rate=1;
    BufRd.ar(1, b, Phasor.ar(0, BufRateScale.kr(b) * rate, 0, BufFrames.kr(b)))
}.play;
)



x.set(\rate, 5);
y.set(\rate, 2.0.rand);
x.set(\rate, 2);

b.free