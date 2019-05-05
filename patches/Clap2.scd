// Probably failure to garbage collect
//also dont think free is working
// groups an busses kinda brittle to iterated running of parts

SampleBank.tempo_(3);
PBank.populate;


// SampleBank.tempo_(2); - no good - hacked in clap2.play


//  CREATE 8 FULL Claps chooser (ch1)
~ch1 = Xhooser.new;
~ch1.name_("8 Full Claps");
~ch1.noseCone_(1);
~ch1.addLane( Lane.new.weight_(inf).sample_( PBank.make(\clap12, \loopOn))); // hide this in sample
~tc1 = TimeChooser.new;
~tc1.noseCone_(1);
~tc1.addLane( TimeLane.new.beats_(8*6));
~ch1.timeChooser_(~tc1);
//~ch1.outBus_(-1.0);                                             sort this
//~ch1.play;
// ~ch1.duration


//  CREATE 7 FULL Claps chooser (ch2)
~ch2 = Xhooser.new;
~ch2.name_("7 Full Claps");
~ch2.noseCone_(1);
~ch2.addLane( Lane.new.weight_(inf).sample_( PBank.construct2(\clap12, \loopOn,1))); //


~tc2 = TimeChooser.new;
~tc2.noseCone_(1);
~tc2.addLane( TimeLane.new.beats_(7*6));
~ch2.timeChooser_(~tc2);
//~ch2.outBus_(1.0);                                                  sort this
// ~ch2.play;


//  CREATE  single Short Clap chooser (ch3)
~ch3 = Xhooser.new;
~ch3.name_("One eleven beat clap");
~ch3.noseCone_(1);
~ch3.addLane( Lane.new.weight_(inf).sample_( PBank.make(\clap11, \loopOff))); // hide this in sample;
~tc3 = TimeChooser.new;
~tc3.noseCone_(1);
~tc3.addLane( TimeLane.new.beats_(5.5)); //  ???????? WHY PATHOLOGICAL??
~ch3.timeChooser_(~tc3);

// ~ch3.outBus_(0.0);
//~ch3.play;

//~ch1.duration;
//~ch2.duration;
//~ch3.duration;


// PULSE LoopableSequence
~seq1 = LoopableSequence.new;
~seq1.name_("Pulse");
~seq1.loopTimes_(2);
~seq1.add (~ch1);
//~seq1.play;
//~seq1.duration;
// ~seq1.kill   - does not!!!! - not the one already running
// ~seq1.choosers.inspect
/*  ~wrapper1= XhooserWrapper.new;
      ~wrapper1.wrap(~seq1);
      ~wrapper1.play
      ~wrapper1.kill
*/



// TWELVE SHIFTS LoopableSequence
~seq2 = LoopableSequence.new;
~seq2.name_("Twelve shifts");
~seq2.loopTimes_(12);
~seq2.add (~ch2);
~seq2.add (~ch3);
// ~seq2.play;
//~seq2.duration;
// ~wrapper2 = XhooserWrapper.new;
// ~wrapper2.wrap(~seq2);
// ~wrapper2.play;
// ~wrapper2.kill;

// Chooser - clapping music - ch 4

~ch4 = Xhooser.new;
~ch4.name_("Clapping music");
~ch4.noseCone_(2);
~ch4.addLane( Lane.new.weight_(inf).nest(~seq2));
~ch4.addLane( Lane.new.weight_(inf).nest(~seq1));
//~ch4.play;



~tc4 = TimeChooser.new;
~tc4.noseCone_(1);
~tc4.addLane( TimeLane.new.beats_(96)); // 48*12 = 576
~ch4.timeChooser_(~tc4);
~ch4.choose;
~ch4.play;
//~ch4.duration;

ch4.inspect
//as if pulse  (ch1 sequ1) just looping and time chooser not cutting in
// but just finla time round!!!!
// nb seq1 and seq1 384 vs 380


// lanes have nested seqeucnes- so are held =in xhooser wrappers
// told to stop
// via lane
 //via playWithChosenTimeLaneForParent

//this.hasHardStop.if {this.sample.hardPlay(timeLaneDuration)};
//which is interp by xhooser wrapper
// which pends on for the kill
//this.chooser.kill ; // for the case where its a loopableS
 // this.loopableSequence.kill})
// loopable seqeucne implements as 	kill{ this.free; this.stop; this.clear}
//free { this.basicFree}
//basicFree { this.allSequencedSynths.do{ arg eachSynth, i; eachSynth.free}
// what about any synths not yet seqeucned????????
// - no -  it was choosers not passing on propers kills to pwrapeprs
// ..but sample at vrey bottom us held in pwrapper


// currentEnvironment


// Gottit!!!
// scorcged earth clock stopping is lkilling switch offs
// seq with a loop is fine
//when swqu is nested wrapped in a lane it stops being able to stop
//xhooswer wrapped is damafing swquw stopping mechs

//??? nah - maybe its too many copies....... lots defensive copying....