a = Buffer.read(s, thisProcess.nowExecutingPath.dirname +/+ "audio/amen-short.wav");

// straight playback start to finish
// uses Line to drag the playhead through
(
{

	var playhead = Line.ar(0, a.numFrames, a.numFrames/a.sampleRate);

	BufRd.ar(2, a.bufnum, playhead, 0)
}.play
)

// slice into 16 -- this gives us trim points
// Needs to be re-run each time
(
{
	var grid = 16;
	var slice = a.numFrames/grid;
	var top = 2;
	var tail = 4;
	var playhead = Line.ar(slice*top, slice*tail, slice*(tail-top)/a.sampleRate);

	BufRd.ar(2, a.bufnum, playhead, 0)
}.play
)



// Phasor test
// As expected, this is a sawtooth wave and so is cyclic
// So, will go back to the start point
// IF BufRd is set to NOT loop there is no audible loop
// BUT if BufRd is set to loop then we have audible loop
(
{

	var top = 0;
	var tail = 16; // try other values
	var playhead = Phasor.ar(0, BufRateScale.kr(a.bufnum), top*a.sampleRate, tail*a.sampleRate);

	BufRd.ar(2, a.bufnum, playhead, 0) // try setting loop to 0
}.play
)


// So, we have trim
// Can we use the resetPos as the anchor point?
// Here's a test using Impulse to set the anchor point
(
{
	var grid = 16;
	var top = 11;
	var tail = 15;
	var anchor = 13;
	var samgrid = a.numFrames/grid;
	// var now = Impulse.kr((a.numFrames/(tail-top))*a.sampleRate);
	var now = Impulse.kr(samgrid*(tail-top)*a.sampleRate);
	var playhead = Phasor.ar(now, BufRateScale.kr(a.bufnum), top*samgrid, tail*samgrid, anchor*a.sampleRate);

	BufRd.ar(2, a.bufnum, playhead, 1)
}.play
)
