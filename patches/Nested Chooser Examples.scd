// RECURSION EXAMPLES
// JUST DO ONCE - CREATE SAMPLE BANK
     SampleBank.populate;
// Wait for it.....
      SampleBank.warmUp;   // add traces to show what sc classes involved


SampleBank.namedSample(\guitar)

//  CREATE A CHOOSER 1
~ch1 = Xhooser.new;
~ch1.name_("CHOOSER 1");
~ch1.noseCone_(2);
~ch1.addLane( Lane.new.weight_(1).namedSample(\guitar));
~ch1.addLane( Lane.new.weight_(inf).namedSample(\bass).loopOn.softStopOn);
~ch1.addLane( Lane.new.weight_(2).namedSample(\drums));
~ch1.addLane( Lane.new.weight_(inf).namedSample(\vox));
~tc1 = TimeChooser.new;
~tc1.noseCone_(1);
~tc1.addLane( TimeLane.new.beats_(6));
~tc1.addLane( TimeLane.new.beats_(10));
~ch1.timeChooser_(~tc1);
~ch1.choose;
~ch1.play;
~ch1.inspect

//  CREATE A CHOOSER 2
~ch2 = Xhooser.new;
~ch2.name_("CHOOSER 2");
~ch2.noseCone_(2);
~ch2.addLane( Lane.new.weight_(1).namedSample(\guitar));
~ch2.addLane( Lane.new.weight_(inf).namedSample(\bass).loopOn);
~ch2.addLane( Lane.new.weight_(inf).namedSample(\drums).loopOn.softStopOn);
~ch2.addLane( Lane.new.weight_(1).namedSample(\vox));
~tc2 = TimeChooser.new;
~tc2.noseCone_(1);
~tc2.addLane( TimeLane.new.beats_(5));
~tc2.addLane( TimeLane.new.beats_(10));
~ch2.timeChooser_(~tc2);


//  CREATE A CHOOSER 3
~ch3 = Xhooser.new;
~ch3.name_("CHOOSER 3");
~ch3.noseCone_(2);
~ch3.addLane( Lane.new.weight_(inf).namedSample(\guitar));
~ch3.addLane( Lane.new.weight_(inf).namedSample(\bass).loopOn.softStopOn);
~ch3.addLane( Lane.new.weight_(2).namedSample(\drums));
~ch3.addLane( Lane.new.weight_(1).namedSample(\vox));
~tc3 = TimeChooser.new;
~tc3.noseCone_(1);
~tc3.addLane( TimeLane.new.beats_(7));
~tc3.addLane( TimeLane.new.beats_(10));
~ch3.timeChooser_(~tc3);

..
~seq.playChosen; // To study a single choice,  do play once first then use  playChosen,
                            // not play - freezes the choices.
~seq.duration;
~seq.kill;
~seq.allSequencedSynths;
~seq.clocks;
~seq.stop;
~seq.free;
~seq.clear;

~ch1.playChosenLanes;  // aha!!!
~seq.inspect;
~seq.explore;
~seq.choosers.size;


// Nested Chooser
// Needs more testing but looks good
~ch3 = Xhooser.new;
~ch3.noseCone_(2);
~ch3.addLane( Lane.new.weight_(0).namedSample(\guitar));
~ch3.addLane( Lane.new.weight_(0).namedSample(\bass).loopOn.softStopOn);
~ch3.addLane( Lane.new.weight_(inf).loopOff.nest(~ch1));
~ch3.addLane( Lane.new.weight_(1).namedSample(\bvs));
~tc3 = TimeChooser.new;
~tc3.noseCone_(1);
~tc3.addLane( TimeLane.new.beats_(6));
~tc3.addLane( TimeLane.new.beats_(10));
~ch3.timeChooser_(~tc3);
~ch3.play

~ch3.playChosenLanes;
~ch1.play;

// CAN WE NEST TWICE??
// Needs testing & maybe debugging

// Nest Once
~ch3 = Xhooser.new;
~ch3.noseCone_(1);
~ch3.addLane( Lane.new.weight_(0).namedSample(\guitar));
~ch3.addLane( Lane.new.weight_(0).namedSample(\bass).loopOn.softStopOn);
~ch3.addLane( Lane.new.weight_(inf).loopOff.nest(~ch1));
~ch3.addLane( Lane.new.weight_(0).namedSample(\vox));
~tc3 = TimeChooser.new;
~tc3.noseCone_(1);
~tc3.addLane( TimeLane.new.beats_(6));
//~tc3.addLane( TimeLane.new.beats_(10));
~ch3.timeChooser_(~tc3);
~ch3.play

// Nest twice
// Seems to work...
~ch4 = Xhooser.new;
~ch4.noseCone_(3);
~ch4.addLane( Lane.new.weight_(inf).loopOff.nest(~ch3));
~ch4.addLane( Lane.new.weight_(0).namedSample(\bass).loopOn.softStopOn);
~ch4.addLane( Lane.new.weight_(1).namedSample(\drums));
~ch4.addLane( Lane.new.weight_(0).namedSample(\vox));
~tc4 = TimeChooser.new;
~tc4.noseCone_(1);
~tc4.addLane( TimeLane.new.beats_(5));
//~tc4.addLane( TimeLane.new.beats_( 10));
//~tc4.addLane( TimeLane.new.weight_(200).namedSample(\vox).playable_(true));
~ch4.timeChooser_(~tc4);

~ch4.play


// NESTING LOOPABLE SEQUENCE  inside LOOPABLE SEQUENCE
// needed for atomic repeats when nesting
// SEEMS TO WORK

// LoopableSequence
~seq2 = LoopableSequence.new;
~seq2.loopTimes_(3);
~seq2.add (~seq);
//~seq2.add (~ch2);
//~seq2.add (~ch3);
~seq2.play;
~seq2.playChosen; // To study a single choice,  do play once first then use  playChosen,
                            // not play - freezes the choices.
~seq.duration;
~seq.kill;
~seq.allSequencedSynths;
~seq.clocks;
~seq.stop;
~seq.free;
~seq.clear;

~ch1.playChosenLanes;  // aha!!!
~seq.inspect;
~seq.explore;
~seq.choosers.size;


//CAN WE NEST A SEQUENCE in a XHOOSER WRAPPR?

// Nest a SEQUENCE  Once
// hard stop currently ignored?
// free stop clear kill implemented in both but maybe wrong semantics
// PROBABLY hardplay in Xhooser wrapper has overly specific semantics  for Xhoosers
// seems to be working
~ch3 = Xhooser.new;
~ch3.noseCone_(1);
~ch3.addLane( Lane.new.weight_(0).namedSample(\guitar));
~ch3.addLane( Lane.new.weight_(0).namedSample(\bass).loopOn.softStopOn);
~ch3.addLane( Lane.new.weight_(inf).loopOff.nest(~seq));
~ch3.addLane( Lane.new.weight_(0).namedSample(\vox));
~tc3 = TimeChooser.new;
~tc3.noseCone_(1);
~tc3.addLane( TimeLane.new.beats_(6));
//~tc3.addLane( TimeLane.new.beats_(10));
~ch3.timeChooser_(~tc3);
~ch3.play

// NESTABLE ATOMIC REPEATS NEEDS TESTING
// ATOMIC REPEATS GENERALLY
// probably also numbered repeats in sampls
// loogging chocies &  freezable choices - auto reloading proxies for samples?
// command patt?

// in c
SampleBank.samples.inspect
SampleBank.sampleDef(\2).play
// clapping music - steve reich!
// rhythmic stuff
//mozart dice
//https://en.wikipedia.org/wiki/List_of_musical_works_released_in_a_stem_format

// clappingmusic
SampleBank.durations

//error trapping for whatsit not stated

try { 1.blech } { |error| "oops".postln };
try ({ 1.blech }, { |error| "oops".postln });
{ 1.blech }.try { |error| "oops".postln };

/*
Because Functions are such an important concept, here some examples from related programming languages with functions as first class objects:
// returning the first argument itself:
{ |x| x }.value(1) // SuperCollider
[:x | x ] value: 1 // Smalltalk
((lambda (x) x) 1) // Lisp
*/


