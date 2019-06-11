Server.killAll;
s.boot;
s.freeAll;
SampleBank.populate;
SampleBank.warmUp;
SampleBank.tempo_(122/60);

// ┌──────────────────────────────────────────────────────────────┐
// │        ___         _     ___                     _           │
// │       | _ \___  __| |__ | __|_ ____ _ _ __  _ __| |___       │
// │       |   / _ \/ _| / / | _|\ \ / _` | '  \| '_ \ / -_)      │
// │       |_|_\___/\__|_\_\ |___/_\_\__,_|_|_|_| .__/_\___|      │
// │                                            |_|               │
// └──────────────────────────────────────────────────────────────┘

// 3.1 - simple NIN soundable chooser
(
~ch1 = Xhooser.new;
~ch1.noseCone_(2);
~ch1.addLane( Lane.new.weight_(2).namedSample(\drums).loopOn);
~ch1.addLane( Lane.new.weight_(1).namedSample(\bass).loopOn);
~ch1.addLane( Lane.new.weight_(1).namedSample(\guitar).loopOn);

~ch1.play;
)


// 4.3
(
~ch1 = Xhooser.new;
~ch1.noseCone_(2);
~ch1.addLane( Lane.new.weight_(1).namedSample(\drums).loopOn);
~ch1.addLane( Lane.new.weight_(2).namedSample(\bass).loopOn);
~ch1.addLane( Lane.new.weight_(inf).namedSample(\vox).loopOn.softStopOn);
~tc1 = TimeChooser.new;
~tc1.noseCone_(1);
~tc1.addLane( TimeLane.new.weight_(1).beats_(4*4));
// ~tc1.addLane( TimeLane.new.weight_(2).beats_(4*6));
~ch1.timeChooser_(~tc1);

~ch1.play
)


// 4.8 and 5.0 - ch1
// sequence is 5.1

(
~ch1 = Xhooser.new;
~ch1.noseCone_(2);
~ch1.addLane( Lane.new.weight_(1).namedSample(\drums).loopOn);
~ch1.addLane( Lane.new.weight_(2).namedSample(\bass).loopOn);
~ch1.addLane( Lane.new.weight_(inf).namedSample(\vox).loopOn.softStopOn);
~tc1 = TimeChooser.new;
~tc1.noseCone_(1);
~tc1.addLane( TimeLane.new.weight_(1).beats_(4*4));
~tc1.addLane( TimeLane.new.weight_(2).beats_(4*6));
~ch1.timeChooser_(~tc1);

~ch2 = Xhooser.new;
~ch2.noseCone_(inf);
~ch2.addLane( Lane.new.weight_(2).namedSample(\drums).loopOn);
~ch2.addLane( Lane.new.weight_(2).namedSample(\bass).loopOn);
~ch2.addLane( Lane.new.weight_(1).namedSample(\guitar).loopOn);
~ch2.addLane( Lane.new.weight_(inf).namedSample(\marimba).loopOn);
~ch2.addLane( Lane.new.weight_(1).namedSample(\bvs).loopOn.softStopOn);
~ch2.addLane( Lane.new.weight_(inf).namedSample(\vox).loopOn.softStopOn);
~tc2 = TimeChooser.new;
~tc2.noseCone_(1);
~tc2.addLane( TimeLane.new.beats_(4*6));
~ch2.timeChooser_(~tc2);
)

~ch1.play;
~ch2.play;


(
~s4 = LoopableSequence.new;
~s4.add (~ch1);
~s4.add (~ch2);
)
~s4.play;



// 6.1 and 6.2
// ┌──────────────────────────────────────────────────────────────┐
// │      ___ _                 ___                     _         │
// │     | _ (_)__ _ _ _  ___  | __|_ ____ _ _ __  _ __| |___     │
// │     |  _/ / _` | ' \/ _ \ | _|\ \ / _` | '  \| '_ \ / -_)    │
// │     |_| |_\__,_|_||_\___/ |___/_\_\__,_|_|_|_| .__/_\___|    │
// │                                              |_|             │
// └──────────────────────────────────────────────────────────────┘

Server.killAll;
s.boot;
s.freeAll;
SampleBank.populate;
SampleBank.warmUp;
SampleBank.tempo_(61/60);


(

~lefthand = Xhooser.new;
~lefthand.name_("lefthand");
~lefthand.noseCone_(1);
~lefthand.addLane(Lane.new.namedSample(\lefthand).loopOff.hardStopOn.weight_(3));
~lefthand.addLane(Lane.new.namedSample(\lefthand2).loopOff.hardStopOn.weight_(1));
~lefttime = TimeChooser.new;
~lefttime.noseCone_(1);
~lefttime.addLane(TimeLane.new.beats_(3)); // sample is 3 beats long
~lefthand.timeChooser_(~lefttime);

~righthand = Xhooser.new;
~righthand.name_("righthand");
~righthand.noseCone_(1);
~righthand.addLane(Lane.new.namedSample(\righthand).loopOff.hardStopOn.weight_(2));
~righthand.addLane(Lane.new.namedSample(\righthand2).loopOff.hardStopOn.weight_(1));
~righttime = TimeChooser.new;
~righttime.noseCone_(1);
~righttime.addLane(TimeLane.new.beats_(4)); // sample is 4 beats long, 3.5 is interesting
~righthand.timeChooser_(~righttime);

~piano = Xhooser.new;
~piano.name_("piano");
~piano.noseCone_(2);
~piano.addLane(Lane.new.nest(~lefthand).loopOn.hardStopOn.weight_(1));
~piano.addLane(Lane.new.nest(~righthand).loopOn.hardStopOn.weight_(1));
// ~pianotime = TimeChooser.new;
// ~pianotime.noseCone_(1);
// ~pianotime.addLane(TimeLane.new.beats_(4*128));
// ~piano.timeChooser_(~pianotime);

~violin = Xhooser.new;
~violin.name_("violin");
~violin.noseCone_(1);
~violin.addLane(Lane.new.namedSample(\violin1).loopOff.hardStopOn.weight_(1));
~violin.addLane(Lane.new.namedSample(\violin2).loopOff.hardStopOn.weight_(1));
~violin.addLane(Lane.new.namedSample(\violinsilence).loopOff.hardStopOn.weight_(2));
~violintime= TimeChooser.new;
~violintime.noseCone_(1);
~violintime.addLane(TimeLane.new.beats_(8)); // sample is 2 bars long = 8. 10 works nicely
~violin.timeChooser_(~violintime);

~parent = Xhooser.new;
~parent.name_("pianostrings");
~parent.noseCone_(inf);
~parent.addLane(Lane.new.nest(~piano).loopOn.hardStopOn.weight_(1));
~parent.addLane(Lane.new.nest(~violin).loopOn.hardStopOn.weight_(1));

~parent.play;
)





/////////////// TEST 1
// Fails with Message 'myGroup' not understood
// An attempt to add a sequence for the piano nested in the parent Chooser
(

~lefthand = Xhooser.new;
~lefthand.name_("lefthand");
~lefthand.noseCone_(1);
~lefthand.addLane(Lane.new.namedSample(\lefthand).loopOff.hardStopOn.weight_(3));
~lefthand.addLane(Lane.new.namedSample(\lefthand2).loopOff.hardStopOn.weight_(1));
~lefttime = TimeChooser.new;
~lefttime.noseCone_(1);
~lefttime.addLane(TimeLane.new.beats_(3)); // sample is 3 beats long
~lefthand.timeChooser_(~lefttime);

~righthand = Xhooser.new;
~righthand.name_("righthand");
~righthand.noseCone_(1);
~righthand.addLane(Lane.new.namedSample(\righthand).loopOff.hardStopOn.weight_(2));
~righthand.addLane(Lane.new.namedSample(\righthand2).loopOff.hardStopOn.weight_(1));
~righttime = TimeChooser.new;
~righttime.noseCone_(1);
~righttime.addLane(TimeLane.new.beats_(4)); // sample is 4 beats long, 3.5 is interesting
~righthand.timeChooser_(~righttime);

~piano1 = Xhooser.new;
~piano1.name_("piano");
~piano1.noseCone_(inf);
~piano1.addLane(Lane.new.nest(~lefthand).loopOn.hardStopOn.weight_(0));
~piano1.addLane(Lane.new.nest(~righthand).loopOn.hardStopOn.weight_(1));
~pianotime1 = TimeChooser.new;
~pianotime1.noseCone_(1);
~pianotime1.addLane(TimeLane.new.beats_(4*16));
~piano1.timeChooser_(~pianotime1);

~piano2 = Xhooser.new;
~piano2.name_("piano2");
~piano2.noseCone_(2);
~piano2.addLane(Lane.new.nest(~lefthand).loopOn.hardStopOn.weight_(1));
~piano2.addLane(Lane.new.nest(~righthand).loopOn.hardStopOn.weight_(1));

~violin = Xhooser.new;
~violin.name_("violin");
~violin.noseCone_(1);
~violin.addLane(Lane.new.namedSample(\violin1).loopOff.hardStopOn.weight_(1));
~violin.addLane(Lane.new.namedSample(\violin2).loopOff.hardStopOn.weight_(1));
~violin.addLane(Lane.new.namedSample(\violinsilence).loopOff.hardStopOn.weight_(2));
~violintime= TimeChooser.new;
~violintime.noseCone_(1);
~violintime.addLane(TimeLane.new.beats_(8)); // sample is 2 bars long = 8. 10 works nicely
~violin.timeChooser_(~violintime);
)

(
~piano = LoopableSequence.new;
~piano.add (~piano1);
~piano.add (~piano2);
)

(
~parent = Xhooser.new;
~parent.name_("pianostrings");
~parent.noseCone_(inf);
~parent.addLane(Lane.new.nest(~piano).loopOn.hardStopOn.weight_(1));
~parent.addLane(Lane.new.nest(~violin).loopOn.hardStopOn.weight_(1));
)

~parent.play;


//////////////// TEST 2
// Top level sequence
// Fail; can a Time Chooser control a parent which is nesting two Choosers?
Server.killAll;
s.boot;
s.freeAll;
SampleBank.populate;
SampleBank.warmUp;
SampleBank.tempo_(61/60);

(

~lefthand = Xhooser.new;
~lefthand.name_("lefthand");
~lefthand.noseCone_(1);
~lefthand.addLane(Lane.new.namedSample(\lefthand).loopOff.hardStopOn.weight_(3));
~lefthand.addLane(Lane.new.namedSample(\lefthand2).loopOff.hardStopOn.weight_(1));
~lefttime = TimeChooser.new;
~lefttime.noseCone_(1);
~lefttime.addLane(TimeLane.new.beats_(3)); // sample is 3 beats long
~lefthand.timeChooser_(~lefttime);

~righthand = Xhooser.new;
~righthand.name_("righthand");
~righthand.noseCone_(1);
~righthand.addLane(Lane.new.namedSample(\righthand).loopOff.hardStopOn.weight_(2));
~righthand.addLane(Lane.new.namedSample(\righthand2).loopOff.hardStopOn.weight_(1));
~righttime = TimeChooser.new;
~righttime.noseCone_(1);
~righttime.addLane(TimeLane.new.beats_(4)); // sample is 4 beats long, 3.5 is interesting
~righthand.timeChooser_(~righttime);

~piano = Xhooser.new;
~piano.name_("piano");
~piano.noseCone_(2);
~piano.addLane(Lane.new.nest(~lefthand).loopOn.hardStopOn.weight_(1));
~piano.addLane(Lane.new.nest(~righthand).loopOn.hardStopOn.weight_(1));
// ~pianotime = TimeChooser.new;
// ~pianotime.noseCone_(1);
// ~pianotime.addLane(TimeLane.new.beats_(4*128));
// ~piano.timeChooser_(~pianotime);

~violin = Xhooser.new;
~violin.name_("violin");
~violin.noseCone_(1);
~violin.addLane(Lane.new.namedSample(\violin1).loopOff.hardStopOn.weight_(1));
~violin.addLane(Lane.new.namedSample(\violin2).loopOff.hardStopOn.weight_(1));
~violin.addLane(Lane.new.namedSample(\violinsilence).loopOff.hardStopOn.weight_(2));
~violintime= TimeChooser.new;
~violintime.noseCone_(1);
~violintime.addLane(TimeLane.new.beats_(8)); // sample is 2 bars long = 8. 10 works nicely
~violin.timeChooser_(~violintime);

~parent1 = Xhooser.new;
~parent1.name_("pianostrings");
~parent1.noseCone_(1);
~parent1.addLane(Lane.new.nest(~piano).loopOn.hardStopOn.weight_(1));
~parent1.addLane(Lane.new.nest(~violin).loopOn.hardStopOn.weight_(1));
~p1time = TimeChooser.new;
~p1time.noseCone_(1);
~p1time.addLane(TimeLane.new.beats_(16));
~parent1.timeChooser_(~p1time);  // <----seems to fail here

~parent2 = Xhooser.new;
~parent2.name_("pianostrings1");
~parent2.noseCone_(inf);
~parent2.addLane(Lane.new.nest(~piano).loopOn.hardStopOn.weight_(1));
~parent2.addLane(Lane.new.nest(~violin).loopOn.hardStopOn.weight_(1));


~parent = LoopableSequence.new;
~parent.add (~parent1);
~parent.add (~parent2);

~parent.play;
)










//////////////
// Phase music
/////////////

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