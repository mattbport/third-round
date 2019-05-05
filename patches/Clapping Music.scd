// CLAPPING MUSIC
// JUST DO ONCE - CREATE SAMPLE BANK

     SampleBank.populate;
// Wait for it.....
      SampleBank.warmUp;
     SampleBank.tempo_(1.9999999999);

// Why do sequences but not repetitions timbrally glitch  when NIN doesnt?


//  CREATE 8 Long Claps chooser (ch1)
~ch1 = Xhooser.new;
~ch1.name_("8 Long Claps");
~ch1.noseCone_(1);
~ch1.addLane( Lane.new.weight_(inf).namedSample(\clap12).loopOn);
~tc1 = TimeChooser.new;
~tc1.noseCone_(1);
~tc1.addLane( TimeLane.new.beats_(8*4));
~ch1.timeChooser_(~tc1);
~ch1.outBus_(-1.0);
//~ch1.play;


//  CREATE 7 Long Claps chooser (ch2)
~ch2 = Xhooser.new;
~ch2.name_("7 Long Claps");
~ch2.noseCone_(1);
~ch2.addLane( Lane.new.weight_(inf).namedSample(\clap12).loopOn);
~tc2 = TimeChooser.new;
~tc2.noseCone_(1);
~tc2.addLane( TimeLane.new.beats_(7*4));
~ch2.timeChooser_(~tc2);
~ch2.outBus_(1.0);
// ~ch2.play;



//  CREATE 3 Short Clap chooser (ch1)
~ch3 = Xhooser.new;
~ch3.name_("One eleven beat clap");
~ch3.noseCone_(1);
~ch3.addLane( Lane.new.weight_(inf).namedSample(\clap11));
~ch3.outBus_(0.0);
//~ch3.play;

~ch1.duration;
~ch2.duration;
~ch3.duration;

//-> 32
//-> 28
//-> 3.66685
// -> 3.6703333333333-  SampleBank.tempo_(2.002);
//3.6663                            SampleBank.tempo_(1.9998);
// > 3.6664833333333      SampleBank.tempo_(1.9999);
// -> 3.666575                   SampleBank.tempo_(1.99995);
// > 3.6665933333333       SampleBank.tempo_(1.99996);
// -> 3.6665951666667     SampleBank.tempo_(1.999961);
//-> 3.6665953701667       SampleBank.tempo_(1.999961111);
//-> 3.6665960833333       SampleBank.tempo_(1.9999615);
// -> 3.6666666664833     SampleBank.tempo_(1.9999999999);

// PULSE LoopableSequence  Glitches -  could just loop chooser - but why glitch - several simul?
~seq1 = LoopableSequence.new;
~seq1.name_("Pulse");
~seq1.loopTimes_(12);
~seq1.add (~ch1);
//~seq1.play;
~seq1.duration;

// TWELVE SHIFTS LoopableSequence  - no glitch now? = phase of moon?
~seq2 = LoopableSequence.new;
~seq2.name_("Twelve shifts");
~seq2.loopTimes_(12);
~seq2.add (~ch2);
~seq2.add (~ch3);
// ~seq2.play;
~seq2.duration;

// Chooser - clapping music - ch 4

~ch4 = Xhooser.new;
~ch4.name_("Clapping music");
~ch4.noseCone_(2);
~ch4.addLane( Lane.new.weight_(inf).nest(~seq2));
~ch4.addLane( Lane.new.weight_(inf).nest(~seq1));

~tc4 = TimeChooser.new;
~tc4.noseCone_(1);
~tc4.addLane( TimeLane.new.beats_(380)); // 380 vs 384
~ch4.timeChooser_(~tc4);
~ch4.choose;
~ch4.play;
~ch4.duration;


//as if pulse  (ch1 sequ1) just looping and time chooser not cutting in
// but just finla time round!!!!
// nb seq1 and seq1 384 vs 380

