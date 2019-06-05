(
a = Buffer.read(s, "/Users/matt/Documents/GitHub/third-round/classes/audio/clap1.wav");
b = Buffer.read(s, "/Users/matt/Documents/GitHub/third-round/classes/audio/clap2.wav");
c = Buffer.read(s, "/Users/matt/Documents/GitHub/third-round/classes/audio/gtr8.wav");
d = Buffer.read(s, "/Users/matt/Documents/GitHub/third-round/classes/audio/drums8.wav");
e = Buffer.read(s, "/Users/matt/Documents/GitHub/third-round/classes/audio/frozen-theatre-theatre-in-a-way.ogg");
)

// Clapping Music - manual setting the anchor
(
{
	arg now, grid = 12, anchor = 0; // start at 0, move to 12

	var clap1 = PlayBuf.ar(1, a.bufnum, BufRateScale.kr(a.bufnum), now, 0, 1, doneAction:2);
	var clap2 = PlayBuf.ar(2, b.bufnum, BufRateScale.kr(b.bufnum), now, (b.numFrames/grid)*anchor, 1, doneAction:2);
	var sound = Pan2.ar(clap1, -0.4) + Balance2.ar(clap2[0], clap2[1], 0.4);

	sound

}.play
)

// Clapping Music, runs automatically
(
{
	arg now, grid = 12;
	var pulse = Impulse.kr(b.sampleRate/b.numFrames);
	var anchor = Line.kr(0, 12, 120).round(1).poll(label:"phase");
	var clap1 = PlayBuf.ar(1, a.bufnum, BufRateScale.kr(a.bufnum), pulse, 0, 0, doneAction:0);
	var clap2 = PlayBuf.ar(2, b.bufnum, BufRateScale.kr(b.bufnum), pulse, (b.numFrames/grid)*anchor, 1, doneAction:0);
	var sound = Pan2.ar(clap1, -0.4) + Balance2.ar(clap2[0], clap2[1], 0.4);

	sound

}.play
)


(
// Sample from https://www.miskatonic.org/2015/01/27/phase-pieces/
// Uses differential speed, as expected in tape phase
{
	arg now;
	var speed = EnvGen.kr(Env.new([1, 1, 0.995], [5, 5])); // 5 seconds before drift starts
	var voice1 = PlayBuf.ar(2, e.bufnum, BufRateScale.kr(e.bufnum), now, 0, 1, doneAction:0);
	var voice2 = PlayBuf.ar(2, e.bufnum, BufRateScale.kr(e.bufnum)*speed, now, 0, 1, doneAction:0);
	var sound = Balance2.ar(voice1[0], voice1[1], -0.5) + Balance2.ar(voice2[0], voice2[1], 0.5);

	sound

}.play
)

(
// Sample from https://www.miskatonic.org/2015/01/27/phase-pieces/
// Mimics the effects of tape phase
// Not the same mechanic as tape phase as this uses a consistent speed
// and instead introduces phase shifts by starting at incrementing grid points
{
	arg now, grid = 1000;
	var pulse = Impulse.kr(e.sampleRate/e.numFrames);
	var anchor = Line.kr(0, 1000, 360).round(1).poll(label:"phase");
	var voice1 = PlayBuf.ar(2, e.bufnum, BufRateScale.kr(e.bufnum), pulse, 0, 0, doneAction:0);
	var voice2 = PlayBuf.ar(2, e.bufnum, BufRateScale.kr(e.bufnum), pulse, (e.numFrames/grid)*anchor, 1, doneAction:0);
	var sound = Balance2.ar(voice1[0], voice1[1], -0.5) + Balance2.ar(voice2[0], voice2[1], 0.5);

	sound

}.play
)

///////////

// Drums test using anchor grid offset
(
{
	arg now, grid = 8, anchor = 3; // start at 0, move to 12
	var clap1 = PlayBuf.ar(2, d.bufnum, BufRateScale.kr(d.bufnum), now, 0, 1, doneAction:2);

	var clap2 = PlayBuf.ar(2, d.bufnum, BufRateScale.kr(d.bufnum), now, (b.numFrames/grid)*anchor, 1, doneAction:2);

	var sound = Balance2.ar(clap1[0], clap1[1], -0.4) + Balance2.ar(clap2[0], clap2[1], 0.4);

	sound
}.play
)


// Drums test with three drum files and two anchor offsets
(
{
	arg now, grid1 = 16, grid2 = 16, anchor1 = 5, anchor2 = 11; // start at 0, move to 12
	var clap1 = PlayBuf.ar(2, d.bufnum, BufRateScale.kr(d.bufnum), now, 0, 1, doneAction:2);
	var clap2 = PlayBuf.ar(2, d.bufnum, BufRateScale.kr(d.bufnum), now, (b.numFrames/grid1)*anchor1, 1, doneAction:2);
	var clap3 = PlayBuf.ar(2, d.bufnum, BufRateScale.kr(d.bufnum), now, (b.numFrames/grid2)*anchor2, 1, doneAction:2)*0.6;
	var sound = Balance2.ar(clap1[0], clap1[1], -1) + Balance2.ar(clap2[0], clap2[1], 1) + clap3;

	Clip.ar(sound, -0.2, 0.2)

}.play
)


// Random offsets which post choices to audition and then select
(
{
	arg now, grid1 = 16, grid2 = 16;
	var anchor1 = rrand(1, 12).postln;
	var anchor2 = rrand(1,12).postln; // start at 0, move to 12
	var clap1 = PlayBuf.ar(2, d.bufnum, BufRateScale.kr(d.bufnum), now, 0, 1, doneAction:2);
	var clap2 = PlayBuf.ar(2, d.bufnum, BufRateScale.kr(d.bufnum), now, (b.numFrames/grid1)*anchor1, 1, doneAction:2);
	var clap3 = PlayBuf.ar(2, d.bufnum, BufRateScale.kr(d.bufnum), now, (b.numFrames/grid2)*anchor2, 1, doneAction:2)*0.6;
	var sound = Balance2.ar(clap1[0], clap1[1], -1) + Balance2.ar(clap2[0], clap2[1], 1) + clap3;

	Clip.ar(sound, -0.2, 0.2)

}.play
)

