// ( duration apttaerns
// pitych patterns - pbind
// trim


Server.killAll;
s.boot;
s.freeAll;
s.killAll;
s.quit;
s.boot;

SampleBank.populate;
SampleBank.warmUp;
SampleBank.tempo_(122/60);

p = Lane.new.namedSample(\vox).loopOff.hardStopOn.weight_(1);
p.sample.play;
q = Lane.new.namedSample(\bvs).loopOff.hardStopOn.weight_(1);
q.sample.play

//TWO samples not loading..... DEBUGGING>>>
SampleBank.samples.at(\bass) ;   .inspect
SampleBank.samples.at(\bass2)
SampleBank.samples.add(\bass2 -> Sample.new(\bass2, "bass8"))
SampleBank.samples.at(\bass2)
SampleBank.samples.add(\synthbass -> Sample.new(\synthbass,"synthbass4"));
SampleBank.samples.at(\synthbass)
SampleBank.tempo;  // was 100/60 = 1.666666
SampleBank.tempo_(122/60);  Better!!!!
SampleBank.tempo;


SampleBank.samples.at(\bass).play; // canonical 4 bars
SampleBank.samples.at(\bass2).play; // jangly 8 bars
SampleBank.samples.at(\synthbass).play; // 4 bars
SampleBank.samples.at(\vox).play; //
SampleBank.samples.at(\bvs).play; //

122/60

// First test; works fine as intended, but...
// Changing the bass duration to be shorter
// leads to FAILURE IN SERVER error - well - that is not really an error - its just how you get nested playables to stop...
// e.g. try setting the bass Chooser to a duration of 1 bar
// samples at 122bpm
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
~nest.name_("Singly nested");
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

// DEBUGGING
// maybe they just got tobe sequecned....
//see schedule
//think alsequencesynths
// maybe needs to ve fro bottom up....

/// its all happening in sample play...


//can gace same samople being played simul many times as diff nodes - ALL OK
SampleBank.sampleDef(\drums).play


~bass.inspect
~bass.lanesWithCleanSamples.inspect
~bass.lanesWithCleanSamples.size
~bass.lanes.inspect
~bass.cleanAllSamples(3).play;
~bass.lanesWithCleanSamples
~bass.lanes.inspect
~bass.clean.lanes.inspect
~la = Lane.new.namedSample(\bass).loopOn.hardStopOn.weight_(1);
~la.inspect
~la.copy

~la.sample == ~la.copy.sample
~la.sample == ~la.sample
~la.sample.equals( ~la.copy.sample)

~la.copy.inspect

~bass.journal;
~bass.journal.inspect;
~nest.chosenLanes
~nest.chosenLanesThatContainChoosers
~nest.chosenLanesThatContainChoosers.first
// Interesting
("Chooser" + 3.asString).inspect
~nest.chosenLanesThatContainChoosers.first.sample.inspect
~nest.chosenLanesThatContainChoosers.first.sample.clocks
~nest.chosenLanesThatContainChoosers.first.sample.loopableSequence.clocks
~nest.chosenLanesThatContainChoosers.first.sample.loopableSequence
~nest.chosenLanes.at(0)
~nest.chosenLanes.at(1)
~nest.chosenLanes.at(0).sample.loopableSequence.clocks
~nest.chosenLanes.at(0).sample.loopableSequence.clocks.size
~nest.chosenLanes.at(0).sample.loopableSequence.choosers
~nest.chosenLanes.at(0).sample.loopableSequence.choosers

~nest.chosenLanes.at(1).sample.loopableSequence.choosers.size
~nest.chosenLanes.at(1).sample.loopableSequence.choosers.inspect
~nest.chosenLanes.at(1).sample.loopableSequence.choosers.do {arg each; each.kill}
~nest.chosenLanes.at(1).sample.loopableSequence.allSequencedSynths.do{arg each; each.free}
~nest.chosenLanes.at(1).sample.loopableSequence.stopRun
~nest.chosenLanes.at(1).sample.loopableSequence.stop
~nest.chosenLanes.at(0).sample.loopableSequence
~nest.chosenLanes.at(0).sample.loopableSequence.stopRun
~nest.chosenLanes.at(0).sample.loopableSequence.stop
~nest.chosenLanes.at(0).sample.loopableSequence.free
~nest.chosenLanes.at(0).sample.loopableSequence.stop
~nest.chosenLanes.at(0).sample.loopableSequence.clear
~nest.chosenLanes.at(0).sample.loopableSequence.kill
~nest.chosenLanes.at(0).sample.loopableSequence.basicKill
~nest.chosenLanes.at(0).sample.loopableSequence.deepkill
~nest.chosenLanes.at(0).sample.loopableSequence.allSequencedSynths;
~nest.choosers;




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
~basstc.addLane(TimeLane.new.beats_(4)); // just one bar - but each sample is soft stop
                                                                  // so the samples of diff length each just get played once
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
~bass.play;
)

Lane.new.nest(~bass).loopOn.hardStopOn.weight_(1).play;
~bass.debug("Scheduling"+ ~bass.name);


// Back to the first example, but this time nest it in another Chooser
// Does not work
SampleBank.populate;
SampleBank.warmUp;
SampleBank.tempo_(122/60);
s.freeAll;
s.reboot; // not enough - system side?

s.queryAllNodes;
s.numSynths;
s.numSynthDefs;
s.numUGens;

currentEnvironment

.topEnvironment
//clocks createdin ;
//Sample
//xhooser wrapper
//loopsable seq
// varibale in xhooser but prob note used

//////////////////////////////////////
s.freeAll;
TempoClock.clear
////// Working
// TempoClock.stop
// has many instances... at leats one still running
// stop forwards only to the dedault

(
~bass = Xhooser.new;
~bass.name_("4 bars of bass");
~bass.noseCone_(1);
~bass.addLane(Lane.new.namedSample(\bass).loopOn.hardStopOn.weight_(1));  // hard stops now
~bass.addLane(Lane.new.namedSample(\bass2).loopOn.hardStopOn.weight_(1));
~bass.addLane(Lane.new.namedSample(\synthbass).loopOn.hardStopOn.weight_(1));
~basstc = TimeChooser.new;
~basstc.noseCone_(1);
~basstc.addLane(TimeLane.new.beats_(4*4));   //back to 4 bars with a hard stop
~bass.timeChooser_(~basstc);

~nest = Xhooser.new;
~nest.name_("single nest");
~nest.noseCone_(2);
~nest.addLane(Lane.new.nest(~bass).loopOn.hardStopOn.weight_(1));  /// BUG - bass not stopped by hard stop
~nest.addLane(Lane.new.namedSample(\drums).loopOn.hardStopOn.weight_(1));
~nesttc = TimeChooser.new;
~nesttc.noseCone_(1);
~nesttc.addLane(TimeLane.new.beats_(4*16));
~nest.timeChooser_(~nesttc);

~nest2 = Xhooser.new;
~nest2.name_("double nest");
~nest2.noseCone_(2);
~nest2.addLane(Lane.new.namedSample(\vox).loopOff.hardStopOn.weight_(1));
~nest2.addLane(Lane.new.namedSample(\bvs).loopOff.hardStopOn.weight_(1));
~nest2.addLane(Lane.new.nest(~nest).loopOn.hardStopOn.weight_(inf));
~nest2tc = TimeChooser.new;
~nest2tc.addLane(TimeLane.new.beats_(4*32));
~nest2.timeChooser_(~nest2tc);
//~bass.play;
//~nest.play;

~nest2.play;

//Xhooser.allInstances
//Xhooser.inspect
//Xhooser.class.
//Xhooser.instance

)

p = Lane.new.namedSample(\vox).loopOff.hardStopOn.weight_(1);
p.sample.play

a= SampleBank.at(\bvs).buffer;
b= SampleBank.at(\vox).buffer;
a= SampleBank.at(\bvs).buffer;
b= SampleBank.at(\vox).buffer;


~nest2.chosenLanes.inspect
~nest2.chosenLanes.first.sample.inspect
~nest2.chosenLanes.at(1).sample.inspect
~nest2.chosenLanes.first.sample.chooser.inspect
~nest2.chosenLanes.at(1)
sample.inspect
~nest2.chosenLanes.first.synth.inspect
~nest2.chosenLanes.first.sample.loopableSequence.inspect
~nest2.chosenLanes.first.sample.loopableSequence.choosers.inspect
~nest2.chosenLanes.first.sample.loopableSequence.choosers.at(6).inspect
~nest2.chosenLanes.first.sample.loopableSequence.choosers.at(5).lanes.inspect
~nest2.chosenLanes.first.sample.loopableSequence.choosers.at(5).lanes.first.sample.inspect
~nest2.chosenLanes.first.sample.loopableSequence.choosers.at(5).lanes.first.sample.loopableSequence.inspect
~nest2.chosenLanes.first.sample.loopableSequence.choosers.at(5).lanes.first.sample.loopableSequence.choosers.inspect

// strings and pads test - samples at 100bpm

SampleBank.populate;
SampleBank.warmUp;
SampleBank.tempo_(100/60);
s.freeAll;

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

~strings2 = Xhooser.new;
~strings2.name_("strings");
~strings2.noseCone_(1);
~strings2.addLane(Lane.new.namedSample(\strings1).loopOn.hardStopOn.weight_(1));
~strings2.addLane(Lane.new.namedSample(\strings2).loopOn.hardStopOn.weight_(0));
~strings2.addLane(Lane.new.namedSample(\strings3).loopOn.hardStopOn.weight_(0));
~stringstime2 = TimeChooser.new;
~stringstime2.noseCone_(1);
~stringstime2.addLane(TimeLane.new.beats_(64));
~strings2.timeChooser_(~stringstime2);

~strongs2 = Xhooser.new;
~strongs2.name_("strongs");
~strongs2.noseCone_(2);
~strongs2.addLane(Lane.new.namedSample(\strings1).loopOn.hardStopOn.weight_(0));
~strongs2.addLane(Lane.new.namedSample(\strings2).loopOn.hardStopOn.weight_(1));
~strongs2.addLane(Lane.new.namedSample(\strings3).loopOn.hardStopOn.weight_(1));
~strongstime2 = TimeChooser.new;
~strongstime2.noseCone_(1);
~strongstime2.addLane(TimeLane.new.beats_(32));
~strongs2.timeChooser_(~strongstime2);



~strongs = Xhooser.new;
~strongs.name_("strongs");
~strongs.noseCone_(2);
~strongs.addLane(Lane.new.namedSample(\strings1).loopOff.hardStopOn.weight_(0));
~strongs.addLane(Lane.new.namedSample(\strings2).loopOff.hardStopOn.weight_(1));
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
~nest.name("Nest");
~nest.noseCone_(inf);
~nest.addLane(Lane.new.nest(~strings).loopOn.hardStopOn.weight_(1));
~nest.addLane(Lane.new.nest(~strongs).loopOn.hardStopOn.weight_(1));
~nest.addLane(Lane.new.nest(~pad).loopOn.hardStopOn.weight_(1));
~nesttc = TimeChooser.new;
~nesttc.noseCone_(1);
~nesttc.addLane(TimeLane.new.beats_(4*32));
~nest.timeChooser_(~nesttc);

~nest.play;
)

~strings.play;
~strongs.play
~pad.play

~strings2.play;
~strongs2.play;





//////////////

SampleBank.populate;
SampleBank.warmUp;
SampleBank.tempo_(100/60);
s.freeAll;


(

a= SampleBank.at(\drums).buffer;
b= SampleBank.at(\drums).buffer;

{
	arg now;
	var grid = 8 * 16; // 8 bar sample * subdivision
	var pulse = Impulse.kr((a.sampleRate/a.numFrames)*grid);
	var anchor = TRand.kr(0, grid-1, pulse).round(1).poll;

	var vox = PlayBuf.ar(2, a.bufnum, BufRateScale.kr(a.bufnum), pulse, (a.numFrames/grid)*anchor, 0, doneAction:0);

	var drums = PlayBuf.ar(2, b.bufnum, BufRateScale.kr(b.bufnum), now, 0, 1, doneAction:0);


	vox + drums

}.play


(
//a = Buffer.read(s, "/Users/matt/Documents/GitHub/third-round/classes/audio/bv8.wav");
//b = Buffer.read(s, "/Users/matt/Documents/GitHub/third-round/classes/audio/vox8.wav");
//)
//(

a= SampleBank.at(\bvs).buffer;
b= SampleBank.at(\vox).buffer;

{
	arg now;
	var grid = 8 * 2;
	var pulse = Impulse.kr((a.sampleRate/a.numFrames)*grid);
	var anchor = TRand.kr(0, grid-1, pulse).round(1).poll;

	var vox = PlayBuf.ar(2, a.bufnum, BufRateScale.kr(a.bufnum), pulse, (a.numFrames/grid)*anchor, 0, doneAction:0)*0.8;

	var drums = PlayBuf.ar(2, b.bufnum, BufRateScale.kr(b.bufnum), now, 0, 1, doneAction:0);


	vox+drums

}.play;

)





(
//a = Buffer.read(s, "/Users/matt/Documents/GitHub/third-round/classes/audio/drums8.wav");
//b = Buffer.read(s, "/Users/matt/Documents/GitHub/third-round/classes/audio/vox8.wav");
//c = Buffer.read(s, "/Users/matt/Documents/GitHub/third-round/classes/audio/bass8.wav");
//d = Buffer.read(s, "/Users/matt/Documents/GitHub/third-round/classes/audio/bv8.wav");
//)
//(
// understadning impulse
//   a= SampleBank.at(\drums).buffer;    a.sampleRate/a.numFrames ; a.duration
// sample rate  44100
// num frames  694033
// a.sampleRate/a.numFrames       0.063541647155106 - why calc that way up???
// a= SampleBank.at(\drums).buffer; (a.numFrames/a.sampleRate)    15.737709750567
// a= SampleBank.at(\drums).buffer; a.duration   15.737709750567
// a= SampleBank.at(\drums).buffer; 1/(a.sampleRate/a.numFrames)*8*16      8.1333308358536
// a= SampleBank.at(\drums).buffer; 8*16/a.duration
// a= SampleBank.at(\drums).buffer; a.duration/ (8*16)
// a= SampleBank.at(\drums).buffer; 1/(a.duration/ (8*16))
// 8*16   divide duration of 15 secs in to  8 bars each of approx 2 secs
// divide each  bar inro 16th notes - durtion about 0.1229508574263
// bu twe want a frequency - so about 8 hz


a= SampleBank.at(\drums).buffer;
b= SampleBank.at(\vox).buffer;
c= SampleBank.at(\bass).buffer;
d= SampleBank.at(\bvs).buffer;

{
	arg now;
	var griddrums = 8 * 16;
	var gridvox = 8 * 8;
	var gridbv = 8 * 2;
	var gridbass = 8 * 1;

	var pulsedrums = Impulse.kr((a.sampleRate/a.numFrames)*griddrums); // isochronous clicks as serverside clock signals - about 8 Hz
	var pulsevox = Impulse.kr((b.sampleRate/b.numFrames)*gridvox);
	// about 4 hz
	var pulsebv = Impulse.kr((d.sampleRate/d.numFrames)*gridbv);
	var pulsebass = Impulse.kr((c.sampleRate/c.numFrames)*gridbass);

	var anchordrums = TRand.kr(0, griddrums-1, pulsedrums).round(1);  //generates random float in given  range with each triggering click from  pulse
	var anchorbass = TRand.kr(0, gridbass-1, pulsebass).round(1);
	var anchorvox = TRand.kr(0, gridvox-1, pulsevox).round(1);
	var anchorbv = TRand.kr(0, gridbv-1, pulsebv).round(1);

	var drums1 = PlayBuf.ar(2, a.bufnum, BufRateScale.kr(a.bufnum), pulsedrums, (a.numFrames/griddrums)*anchordrums, 0, doneAction:0)*0.6;
	var drums2 = PlayBuf.ar(2, a.bufnum, BufRateScale.kr(a.bufnum), now, 0, 1, doneAction:0); // loops
	var bass = PlayBuf.ar(2, c.bufnum, BufRateScale.kr(c.bufnum), pulsebass, (c.numFrames/gridbass)*anchorbass, 1, doneAction:0)*0.7; //loops
	var vox = PlayBuf.ar(2, b.bufnum, BufRateScale.kr(b.bufnum), pulsevox, (b.numFrames/gridvox)*anchorvox, 0, doneAction:0)*0.6;
	var bv = PlayBuf.ar(2, d.bufnum, BufRateScale.kr(d.bufnum), pulsebv, (d.numFrames/gridbv)*anchorbv, 0, doneAction:0)*0.4;


	//PLaybuf has built in provision for a  A trigger that causes a jump to the startPos sample frame.
	// but iscoronous clicks are 8 hz!!!! too fast for what we hear
	// so not the tigger - just the quantisation points

	// a.numFrames/griddrums)*anchordrums
	// (a.numFrames/(8*16)) *anchordrums
	// (b.sampleRate/b.numFrames) * 64

	// (A trigger occurs when a signal changes from negative value to positive value.)
	// startposes are chpsen by anchors which are just random grid points generated by same trigger

bass+bv+drums2


}.play
)

currentEnvironment.inspect
currentEnvironment.array.inspect
valueEnvir.inspect
valueArrayEnvir.inspect
{ arg now; now.debug("hi")}.play
{ arg now; now.inspect}.play

// try losing globals

{ Impulse.ar(800, 0.0, 0.5, 0) }.play
​
{ Impulse.ar(XLine.kr(800,100,5), 0.0,  0.5, 0) }.play
//modulate phase:

{ Impulse.ar(4, [0, MouseX.kr(0, 1)], 0.2) }.play;
//an Impulse with frequency 0 returns a single impulse:

SynthDef(\imp, { OffsetOut.ar(0, Impulse.ar(0)); FreeSelf.kr(Impulse.kr(0)); }).add;
fork { (1 / (1..60).scramble).do { |dt| Synth.grain(\imp);  dt.wait } };

SampleBank.populate;
SampleBank.warmUp;
SampleBank.tempo_(100/60);
b = SampleBank.at(\bass).buffer;
b.numFrames;
b.numFrames.rand
(
 SynthDef(\playBufMonoLoopStart, {| out = 0, bufnum = 0, rate = 1, startPos |
  var scaledRate, player;
  scaledRate = rate * BufRateScale.kr(bufnum);
  player = PlayBuf.ar(2, bufnum, scaledRate, startPos: startPos, loop: 1, doneAction:2);
  Out.ar(out, player)
 }).play(s, [\out, 0, \bufnum, b.bufnum, \rate, 1, \startPos, b.numFrames.rand]);
)


(
x = { arg rate=2;
    BufRd.ar(1, b, Phasor.ar(0, BufRateScale.kr(b) * rate, 0, BufFrames.kr(b))) // nothing of interest  withot a rate argument.....
}.play;
)

{ Impulse.ar(800, 0.0, 0.5, 0) }.play
{ Impulse.ar(XLine.kr(800,20,5), 0.0,  1, 0) }.play

{ Impulse.ar(  440*[MouseX.kr(0, 1)], [MouseX.kr(0, 1)],  0.2) }.play;
{ Impulse.ar(4,  [MouseX.kr(0, 1)], 0.2) }.play;
{ Impulse.ar(4, [MouseX.kr(0, 800)], 0, 0.2) }.play
{ Impulse.ar(XLine.kr(800,100,5), 0.0,  0.5, 0) }.play
{ SinOsc.ar(MouseX.kr(40, 10000, 1), 0, 0.1) }.play;
{ SinOsc.ar(MouseX.kr(500, 10000, 1).poll, 0, 0.1) }.play;

SynthDef(\imp, { OffsetOut.ar(0, Impulse.ar(0)); FreeSelf.kr(Impulse.kr(0)); }).add;
fork { (1 / (1..60).scramble).do { |dt| Synth.grain(\imp);  dt.wait } };

{Dust.ar(MouseX.kr(1,100))}.play

// trigger one shot on each pulse
SynthDef(\help_PlayBuf, {| out = 0, bufnum = 0 |
    var trig, anchorPoint;
    trig = Impulse.kr((b.sampleRate/b.numFrames)*8);
	anchorPoint = TRand.kr(0, 3, trig).round(1);
    Out.ar(out,
		PlayBuf.ar(1, bufnum, BufRateScale.kr(bufnum), trig, Rand(0, b.numFrames), 0, doneAction:0); // loops
    )
}).play(s, [\out, 0, \bufnum, b]);



// trigger one shot on each pulse
SynthDef(\help_PlayBuf, {| out = 0, bufnum = 0 |
    var trig, anchorPoint;
    trig = Impulse.kr((b.sampleRate/b.numFrames)*4); // freq = duration *some  multiplier
	anchorPoint = TRand.kr(0, b.numFrames, trig).round(1);
    Out.ar(out,
		PlayBuf.ar(1, bufnum, BufRateScale.kr(bufnum), trig, anchorPoint, 0, doneAction:0); // loops
    )
}).play(s, [\out, 0, \bufnum, b]);


~nest2.lanes.at(2).sample.group


(
~nest2.lanes.at(2).sample.group.queryTree({|msg|
   var i = 1, tabs = 0, dumpFunc;
   ("NODE TREE Group" + msg[1]).postln;
   if(msg[2] > 0, {
       dumpFunc = {|numChildren|
           tabs = tabs + 1;
           numChildren.do({
               i = i + 3;
               tabs.do({ "   ".post });
               msg[i].post;
               (" " ++ msg[i + 2]).postln;
               if(msg[i + 1] > 0, { dumpFunc.value(msg[i + 1]) });
           });
           tabs = tabs - 1;
       };
       dumpFunc.value(msg[2]);
   });
});
)
