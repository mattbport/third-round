SampleBank.populate;
SampleBank.warmUp;

// First test; works fine as intended, but...
// Changing the bass duration to be shorter
// leads to FAILURE IN SERVER error
// e.g. try setting the bass Chooser to a duration of 1 bar
(
~bass = Xhooser.new;
~bass.name_("bass");
~bass.noseCone_(1);
~bass.addLane(Lane.new.namedSample(\bass).loopOn.hardStopOn.weight_(1));
~bass.addLane(Lane.new.namedSample(\bass2).loopOn.hardStopOn.weight_(1));
~bass.addLane(Lane.new.namedSample(\synthbass).loopOn.hardStopOn.weight_(1));
~basstc = TimeChooser.new;
~basstc.noseCone_(1);
~basstc.addLane(TimeLane.new.beats_(4*4));
~bass.timeChooser_(~basstc);

~nest = Xhooser.new;
~nest.noseCone_(2);
~nest.addLane(Lane.new.nest(~bass).loopOn.hardStopOn.weight_(1));
~nest.addLane(Lane.new.namedSample(\drums).loopOn.hardStopOn.weight_(1));
~nesttc = TimeChooser.new;
~nesttc.noseCone_(1);
~nesttc.addLane(TimeLane.new.beats_(4*16));
~nest.timeChooser_(~nesttc);
)

~nest.play;

~bass.play;


// Soft stops on the bass lanes, with a 1-bar time chooser
// Loop on the parent, forcing a new selection when a sample stops
// Problem - new selection *sometimes* made when 16 bars has elapsed
// FAILURE IN SERVER errors always occur at stop
// Timing a little off? Seems to start rushing after a few cycles
(
~bass = Xhooser.new;
~bass.name_("bass");
~bass.noseCone_(1);
~bass.addLane(Lane.new.namedSample(\bass).loopOn.softStopOn.weight_(1));
~bass.addLane(Lane.new.namedSample(\bass2).loopOn.softStopOn.weight_(1));
~bass.addLane(Lane.new.namedSample(\synthbass).loopOn.softStopOn.weight_(1));
~basstc = TimeChooser.new;
~basstc.noseCone_(1);
~basstc.addLane(TimeLane.new.beats_(4));
~bass.timeChooser_(~basstc);

~nest = Xhooser.new;
~nest.noseCone_(2);
~nest.addLane(Lane.new.nest(~bass).loopOn.hardStopOn.weight_(1));
~nest.addLane(Lane.new.namedSample(\drums).loopOn.hardStopOn.weight_(1));
~nesttc = TimeChooser.new;
~nesttc.noseCone_(1);
~nesttc.addLane(TimeLane.new.beats_(4*16));
~nest.timeChooser_(~nesttc);

~nest.play;
)


// Back to the first example, but this time nest it in another Chooser
// Does not work
(
~bass = Xhooser.new;
~bass.name_("bass");
~bass.noseCone_(1);
~bass.addLane(Lane.new.namedSample(\bass).loopOn.hardStopOn.weight_(1));
~bass.addLane(Lane.new.namedSample(\bass2).loopOn.hardStopOn.weight_(1));
~bass.addLane(Lane.new.namedSample(\synthbass).loopOn.hardStopOn.weight_(1));
~basstc = TimeChooser.new;
~basstc.noseCone_(1);
~basstc.addLane(TimeLane.new.beats_(4*4));
~bass.timeChooser_(~basstc);

~nest = Xhooser.new;
~nest.noseCone_(2);
~nest.addLane(Lane.new.nest(~bass).loopOn.hardStopOn.weight_(1));
~nest.addLane(Lane.new.namedSample(\drums).loopOn.hardStopOn.weight_(1));
~nesttc = TimeChooser.new;
~nesttc.noseCone_(1);
~nesttc.addLane(TimeLane.new.beats_(4*16));
~nest.timeChooser_(~nesttc);

~nest2 = Xhooser.new;
~nest2.noseCone_(2);
~nest2.addLane(Lane.new.namedSample(\vox).loopOff.hardStopOn.weight_(1));
~nest2.addLane(Lane.new.namedSample(\bvs).loopOff.hardStopOn.weight_(1));
~nest2.addLane(Lane.new.nest(~nest).loopOn.hardStopOn.weight_(inf));
~nest2tc = TimeChooser.new;
~nest2tc.addLane(TimeLane.new.beats_(4*32));
~nest2.timeChooser_(~nest2tc);

~nest2.play;
)



// strings and pads test

(
~strings = Xhooser.new;
~strings.name_("strings");
~strings.noseCone_(1);
~strings.addLane(Lane.new.namedSample(\strings1).loopOn.weight_(1));
~strings.addLane(Lane.new.namedSample(\strings2).loopOn.weight_(0));
~strings.addLane(Lane.new.namedSample(\strings3).loopOn.weight_(0));
~stringstime = TimeChooser.new;
~stringstime.noseCone_(1);
~stringstime.addLane(TimeLane.new.beats_(4*8));
~strings.timeChooser_(~stringstime);

~strongs = Xhooser.new;
~strongs.name_("strongs");
~strongs.noseCone_(1);
~strongs.addLane(Lane.new.namedSample(\strings1).loopOn.weight_(0));
~strongs.addLane(Lane.new.namedSample(\strings2).loopOn.weight_(1));
~strongs.addLane(Lane.new.namedSample(\strings3).loopOn.weight_(0));
~strongstime = TimeChooser.new;
~strongstime.noseCone_(1);
~strongstime.addLane(TimeLane.new.beats_(3*8));
~strongs.timeChooser_(~strongstime);


~pad = Xhooser.new;
~pad.name_("pad");
~pad.noseCone_(1);
~pad.addLane(Lane.new.namedSample(\pad1).loopOn.weight_(1));
~pad.addLane(Lane.new.namedSample(\pad2).loopOn.weight_(1));
~padtime = TimeChooser.new;
~padtime.noseCone_(1);
~padtime.addLane(TimeLane.new.beats_(3*8));
~pad.timeChooser_(~padtime);


~nest = Xhooser.new;
~nest.noseCone_(2);
~nest.addLane(Lane.new.nest(~strings).loopOn.hardStopOn.weight_(1));
~nest.addLane(Lane.new.nest(~strongs).loopOn.hardStopOn.weight_(1));
~nest.addLane(Lane.new.nest(~pad).loopOn.hardStopOn.weight_(1));
~nesttc = TimeChooser.new;
~nesttc.noseCone_(1);
~nesttc.addLane(TimeLane.new.beats_(4*32));
~nest.timeChooser_(~nesttc);
)

~nest.play;


(
~strings = Xhooser.new;
~strings.name_("strings");
~strings.noseCone_(1);
~strings.addLane(Lane.new.namedSample(\strings1).loopOff.hardStopOn.weight_(1));
~strings.addLane(Lane.new.namedSample(\strings2).loopOff.hardStopOn.weight_(0));
~strings.addLane(Lane.new.namedSample(\strings3).loopOff.hardStopOn.weight_(0));
~stringstime = TimeChooser.new;
~stringstime.noseCone_(1);
~stringstime.addLane(TimeLane.new.beats_(4));
~strings.timeChooser_(~stringstime);

~strongs = Xhooser.new;
~strongs.name_("strongs");
~strongs.noseCone_(1);
~strongs.addLane(Lane.new.namedSample(\strings1).loopOff.hardStopOn.weight_(0));
~strongs.addLane(Lane.new.namedSample(\strings2).loopOff.hardStopOn.weight_(0));
~strongs.addLane(Lane.new.namedSample(\strings3).loopOff.hardStopOn.weight_(1));
~strongstime = TimeChooser.new;
~strongstime.noseCone_(1);
~strongstime.addLane(TimeLane.new.beats_(4));
~strongs.timeChooser_(~strongstime);


~pad = Xhooser.new;
~pad.name_("pad");
~pad.noseCone_(1);
~pad.addLane(Lane.new.namedSample(\pad1).loopOn.softStopOn.weight_(1));
~pad.addLane(Lane.new.namedSample(\pad2).loopOn.softStopOn.weight_(1));
~padtime = TimeChooser.new;
~padtime.noseCone_(1);
~padtime.addLane(TimeLane.new.beats_(4));
~pad.timeChooser_(~padtime);


~nest = Xhooser.new;
~nest.noseCone_(3);
~nest.addLane(Lane.new.nest(~strings).loopOn.hardStopOn.weight_(1));
~nest.addLane(Lane.new.nest(~strongs).loopOn.hardStopOn.weight_(1));
~nest.addLane(Lane.new.nest(~pad).loopOn.hardStopOn.weight_(1));
~nesttc = TimeChooser.new;
~nesttc.noseCone_(1);
~nesttc.addLane(TimeLane.new.beats_(4*128));
~nest.timeChooser_(~nesttc);

~nest.play;

)












//////////////

(
a = Buffer.read(s, "/Users/matt/Documents/GitHub/third-round/classes/audio/drums8.wav");
b = Buffer.read(s, "/Users/matt/Documents/GitHub/third-round/classes/audio/drums8.wav");
)
(

{
	arg now;
	var grid = 8 * 16; // 8 bar sample * subdivision
	var pulse = Impulse.kr((a.sampleRate/a.numFrames)*grid);
	var anchor = TRand.kr(0, grid-1, pulse).round(1).poll;

	var vox = PlayBuf.ar(2, a.bufnum, BufRateScale.kr(a.bufnum), pulse, (a.numFrames/grid)*anchor, 0, doneAction:0);

	var drums = PlayBuf.ar(2, b.bufnum, BufRateScale.kr(b.bufnum), now, 0, 1, doneAction:0);


	vox + drums

}.play
)

(
a = Buffer.read(s, "/Users/matt/Documents/GitHub/third-round/classes/audio/bv8.wav");
b = Buffer.read(s, "/Users/matt/Documents/GitHub/third-round/classes/audio/vox8.wav");
)
(

{
	arg now;
	var grid = 8 * 2;
	var pulse = Impulse.kr((a.sampleRate/a.numFrames)*grid);
	var anchor = TRand.kr(0, grid-1, pulse).round(1).poll;

	var vox = PlayBuf.ar(2, a.bufnum, BufRateScale.kr(a.bufnum), pulse, (a.numFrames/grid)*anchor, 0, doneAction:0)*0.8;

	var drums = PlayBuf.ar(2, b.bufnum, BufRateScale.kr(b.bufnum), now, 0, 1, doneAction:0);


	vox+drums

}.play
)



(
a = Buffer.read(s, "/Users/matt/Documents/GitHub/third-round/classes/audio/drums8.wav");
b = Buffer.read(s, "/Users/matt/Documents/GitHub/third-round/classes/audio/vox8.wav");
c = Buffer.read(s, "/Users/matt/Documents/GitHub/third-round/classes/audio/bass8.wav");
d = Buffer.read(s, "/Users/matt/Documents/GitHub/third-round/classes/audio/bv8.wav");
)
(

{
	arg now;
	var griddrums = 8 * 16;
	var gridvox = 8 * 8;
	var gridbv = 8 * 2;
	var gridbass = 8 * 1;
	var pulsedrums = Impulse.kr((a.sampleRate/a.numFrames)*griddrums);
	var pulsevox = Impulse.kr((b.sampleRate/b.numFrames)*gridvox);
	var pulsebv = Impulse.kr((d.sampleRate/d.numFrames)*gridbv);
	var pulsebass = Impulse.kr((c.sampleRate/c.numFrames)*gridbass);
	var anchordrums = TRand.kr(0, griddrums-1, pulsedrums).round(1);
	var anchorbass = TRand.kr(0, gridbass-1, pulsebass).round(1);
	var anchorvox = TRand.kr(0, gridvox-1, pulsevox).round(1);
	var anchorbv = TRand.kr(0, gridbv-1, pulsebv).round(1);
	var drums1 = PlayBuf.ar(2, a.bufnum, BufRateScale.kr(a.bufnum), pulsedrums, (a.numFrames/griddrums)*anchordrums, 0, doneAction:0)*0.6;
	var drums2 = PlayBuf.ar(2, a.bufnum, BufRateScale.kr(a.bufnum), now, 0, 1, doneAction:0);
	var bass = PlayBuf.ar(2, c.bufnum, BufRateScale.kr(c.bufnum), pulsebass, (c.numFrames/gridbass)*anchorbass, 1, doneAction:0)*0.7;
	var vox = PlayBuf.ar(2, b.bufnum, BufRateScale.kr(b.bufnum), pulsevox, (b.numFrames/gridvox)*anchorvox, 0, doneAction:0)*0.6;
	var bv = PlayBuf.ar(2, d.bufnum, BufRateScale.kr(d.bufnum), pulsebv, (d.numFrames/gridbv)*anchorbv, 0, doneAction:0)*0.4;

bass+bv+drums2

}.play
)

