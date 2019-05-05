TestRunner {
var choosers, ch0, ch1, ch2, ch3, ch4, ch5,  tc0, tc1, tc2, tc3, tc4, tc5;
var sequences, s0,s1,s2,s3,s4,s5, seq1, seq2, seq3;

*new { ^ super.new.init}

init{ choosers = List.new;
	    sequences= List.new;
		// SampleBank.populate;
	    ClipBank.populate;       }

	 // warm {  SampleBank.warmUp}

nystedt {
	//  CREATE A CHOOSER 0  - ALL ==================
ch0= Xhooser.new;
ch0.name_("CHOOSER SATB");
ch0.noseCone_(4);
ch0.addLane( Lane.new.weight_(1).sample_( ClipBank.make(\all , \s, 4) ));
ch0.addLane( Lane.new.weight_(1).sample_( ClipBank.make(\all, \a, 4  ) ) );
ch0.addLane( Lane.new.weight_(1).sample_( ClipBank.make(\all, \t ,4 ) ) );
ch0.addLane( Lane.new.weight_(1).sample_( ClipBank.make(\all, \b, 4  )));
tc0 = TimeChooser.new;
tc0.noseCone_(1);
tc0.addLane( TimeLane.new.beats_(32));
ch0.timeChooser_(tc0);

//  CREATE A CHOOSER kom Todd==================
ch1= Xhooser.new;
ch1.name_("CHOOSER Komm Todd");
ch1.noseCone_(5);
ch1.addLane( Lane.new.weight_(1).sample_( ClipBank.construct(\kom1, \all, 4, 8) ));
ch1.addLane( Lane.new.weight_(1).sample_( ClipBank.construct(\kom1, \all, 3, 8 ) ) );
ch1.addLane( Lane.new.weight_(1).sample_( ClipBank.construct(\kom1, \all, 2, 8) ) );
ch1.addLane( Lane.new.weight_(1).sample_( ClipBank.construct(\kom1, \all, 1.5, 8  )));
ch1.addLane( Lane.new.weight_(1).sample_( ClipBank.construct(\kom1, \all, 1 , 8)));
tc1 = TimeChooser.new;
tc1.noseCone_(1);
tc1.addLane( TimeLane.new.beats_(32));
ch1.timeChooser_(~c1);


//  CREATE A CHOOSER kom Ruh==================
ch2= Xhooser.new;
ch2.name_("CHOOSER Komm Ruh");
ch2.noseCone_(5);
ch2.addLane( Lane.new.weight_(1).sample_( ClipBank.construct(\kom2, \all, 4, 8) ));
ch2.addLane( Lane.new.weight_(1).sample_( ClipBank.construct(\kom2, \all, 3, 8 ) ) );
ch2.addLane( Lane.new.weight_(1).sample_( ClipBank.construct(\kom2, \all, 2, 8) ) );
ch2.addLane( Lane.new.weight_(1).sample_( ClipBank.construct(\kom2, \all, 1.5, 8  )));
ch2.addLane( Lane.new.weight_(1).sample_( ClipBank.construct(\kom2, \all, 1 , 8)));
tc2= TimeChooser.new;
tc2.noseCone_(1);
tc2.addLane( TimeLane.new.beats_(32));
ch2.timeChooser_(tc2);

//  CREATE A CHOOSER  *** SINGLE NOTE  **** ==================
ch3= Xhooser.new;
ch3.name_("CHOOSER Komm Ruh");
ch3.noseCone_(5);
ch3.addLane( Lane.new.weight_(1).sample_( ClipBank.construct(\kom2A, \all, 4, 8) ));
ch3.addLane( Lane.new.weight_(1).sample_( ClipBank.construct(\kom2A, \all, 3, 8 ) ) );
ch3.addLane( Lane.new.weight_(1).sample_( ClipBank.construct(\kom2A, \all, 2, 8) ) );
ch3.addLane( Lane.new.weight_(1).sample_( ClipBank.construct(\kom2A, \all, 1.5, 8  )));
ch3.addLane( Lane.new.weight_(1).sample_( ClipBank.construct(\kom2A, \all, 1 , 8)));
tc3= TimeChooser.new;
tc3.noseCone_(1);
tc3.addLane( TimeLane.new.beats_(32));
ch3.timeChooser_(tc3);

//  CREATE A CHOOSER 5==================
ch4= Xhooser.new;
ch4.name_("CHOOSER Komm Ruh");
ch4.noseCone_(5);
ch4.addLane( Lane.new.weight_(1).sample_( ClipBank.construct(\kom2B, \all, 4, 8) ));
ch4.addLane( Lane.new.weight_(1).sample_( ClipBank.construct(\kom2B, \all, 3, 8 ) ) );
ch4.addLane( Lane.new.weight_(1).sample_( ClipBank.construct(\kom2B, \all, 2, 8) ) );
ch4.addLane( Lane.new.weight_(1).sample_( ClipBank.construct(\kom2B, \all, 1.5, 8  )));
ch4.addLane( Lane.new.weight_(1).sample_( ClipBank.construct(\kom2B, \all, 1 , 8)));
tc4= TimeChooser.new;
tc4.noseCone_(1);
tc4.addLane( TimeLane.new.beats_(32));
ch4.timeChooser_(tc4);

//  CREATE A CHOOSER 6==================
		// construct argunebts - clip, voice, temp, holdupercollider
ch5= Xhooser.new;
ch5.name_("CHOOSER Komm Ruh");
ch5.noseCone_(5);
ch5.addLane( Lane.new.weight_(1).sample_( ClipBank.construct(\kom3, \all, 4, 8) ));
ch5.addLane( Lane.new.weight_(1).sample_( ClipBank.construct(\kom3, \all, 3, 8 ) ) );
ch5.addLane( Lane.new.weight_(1).sample_( ClipBank.construct(\kom3, \all, 2, 8) ) );
ch5.addLane( Lane.new.weight_(1).sample_( ClipBank.construct(\kom3, \all, 1.5, 8  )));
ch5.addLane( Lane.new.weight_(1).sample_( ClipBank.construct(\kom3, \all, 1 , 8)));
tc5= TimeChooser.new;
tc5.noseCone_(1);
tc5.addLane( TimeLane.new.beats_(32));
ch5.timeChooser_(tc5);

//  SEQUENCES ===================
s0 = LoopableSequence.new;
s0.add (ch0);
s0.add (ch1);
s0.add (ch2);
s0.add (ch5);
s0.choose;
s0.polyDurations;

s0.play }



clapping {
// for panned version see clapping music .scd
//and see outbus_ in Lane
// and see play in sample.sc
// synth.set(\outputBus, this.outBus);
//and see createSynthDEf in Sample
//for synth equiv for clap2, need to modify sunthDef in clap2
// no - - cant do that cos synthdef knows nothing about the pseq
// more like  softplay in sample - this.synth.set(\loop, 0) };
// but playbuf knows about loop  - well - we can get direct to synh
// but synth  on server is very dim - thats ok - repeat is  just a numebr
//even easier - just get to the pbind stream/ event player
// no good cos clip already cooked in bank
//but see how we do parametertisable clips in clipbank for nystedt!!
// so just copy that
// might also be useful for panning - might try that in synth clap

//  CREATE 8 Long Claps chooser (ch1)
ch1 = Xhooser.new;
ch1.name_("8 Long Claps");
ch1.noseCone_(1);
ch1.addLane( Lane.new.weight_(inf).namedSample(\clap12).loopOn);
tc1 = TimeChooser.new;
tc1.noseCone_(1);
tc1.addLane( TimeLane.new.beats_(8*4));
ch1.timeChooser_(tc1);
//ch1.play;


//  CREATE 7 Long Claps chooser (ch2)
ch2 = Xhooser.new;
ch2.name_("7 Long Claps");
ch2.noseCone_(1);
ch2.addLane( Lane.new.weight_(inf).namedSample(\clap12).loopOn);
tc2 = TimeChooser.new;
tc2.noseCone_(1);
tc2.addLane( TimeLane.new.beats_(7*4));
ch2.timeChooser_(tc2);
// ch2.play;

//  CREATE 3 Short Clap chooser (ch1)
ch3 = Xhooser.new;
ch3.name_("One eleven beat clap");
ch3.noseCone_(1);
ch3.addLane( Lane.new.weight_(inf).namedSample(\clap11));
//ch3.play;

ch1.durations;
ch2.durations;
ch3.durations;

// PULSE LoopableSequence  Glitches -  could just loop chooser - but why glitch - several simul?
seq1 = LoopableSequence.new;
seq1.name_("Pulse");
seq1.loopTimes_(12);
seq1.add (ch1);
//seq1.play;


// TWELVE SHIFTS LoopableSequence  - no glitch now? = phase of moon?
seq2 = LoopableSequence.new;
seq2.name_("Twelve shifts");
seq2.loopTimes_(12);
seq2.add (ch2);
seq2.add (ch3);
// seq2.play;

// Chooser - clapping music - ch 4

ch4 = Xhooser.new;
ch4.name_("Clapping music");
ch4.noseCone_(2);
ch4.addLane( Lane.new.weight_(inf).nest(seq2));
ch4.addLane( Lane.new.weight_(inf).nest(seq1));
ch4.choose;
ch4.durations;
ch4.play;
}

// Pbinds - CHEESY BASS =================

cheeseBass {
ch1 = Xhooser.new;
ch1.name_("Inner");
ch1.noseCone_(2);
ch1.addLane( Lane.new.weight_(1).sample_( PBank.at(\bass1)));
ch1.addLane( Lane.new.weight_(1).sample_( PBank.at(\bass2)));
ch1.addLane( Lane.new.weight_(1).sample_( PBank.at(\bass3)));
ch1.addLane( Lane.new.weight_(1).sample_( PBank.at(\bass4)));
ch1.addLane( Lane.new.weight_(inf).sample_( PBank.at(\fourOnFloor)));
tc2 = TimeChooser.new;
tc2.noseCone_(1);
tc2.addLane( TimeLane.new.beats_(2));
ch1.timeChooser_(tc2);


ch2 = Xhooser.new;
ch2.name_("Outer");
ch2.noseCone_(1);
ch2.addLane( Lane.new.weight_(1).nest(ch1).loopOn);

^ch2

}


cheeseBass2 {
ch1 = Xhooser.new;
ch1.name_("Inner");
ch1.noseCone_(2);
ch1.addLane( Lane.new.weight_(1).sample_( PBank.at(\bass1)));
ch1.addLane( Lane.new.weight_(1).sample_( PBank.at(\bass2)));
ch1.addLane( Lane.new.weight_(1).sample_( PBank.at(\bass3)));
ch1.addLane( Lane.new.weight_(1).sample_( PBank.at(\bass4)));
ch1.addLane( Lane.new.weight_(inf).sample_( PBank.at(\fourOnFloor)));

seq2 = LoopableSequence.new;
seq2.loopTimes_(36);
seq2.add (ch1);

^ seq2
}

// CLAPPING MUSIC synthezised
//  CREATE 8 FULL Claps chooser (ch1)
// Fix external sample looping and panning
// cant see how to externalise looping this way... got to be in synthDef

synthClap	{
ch1 = Xhooser.new;
ch1.name_("8 Full Claps");
ch1.noseCone_(1);
ch1.addLane( Lane.new.weight_(inf).sample_( PBank.make(\clap12, \loopOn)));
tc1 = TimeChooser.new;
tc1.noseCone_(1);
tc1.addLane( TimeLane.new.beats_(8*6));
ch1.timeChooser_(tc1);


//  CREATE 7 FULL Claps chooser (ch2)
ch2 = Xhooser.new;
ch2.name_("7 Full Claps");
ch2.noseCone_(1);
ch2.addLane( Lane.new.weight_(inf).sample_(
			      //PBank.construct(\clap12, \loopOn,\extBusSwap)));
		             PBank.construct2(\clap12, \loopOn,1)));
tc2 = TimeChooser.new;
tc2.noseCone_(1);
tc2.addLane( TimeLane.new.beats_(7*6));
ch2.timeChooser_(tc2);

//  CREATE  single Short Clap chooser (ch3)
ch3 = Xhooser.new;
ch3.name_("One eleven beat clap");
ch3.noseCone_(1);
ch3.addLane( Lane.new.weight_(inf).sample_( PBank.make(\clap11, \loopOff)));
tc3 = TimeChooser.new;
tc3.noseCone_(1);
tc3.addLane( TimeLane.new.beats_(5.5));
ch3.timeChooser_(tc3);

// PULSE LoopableSequence
seq1 = LoopableSequence.new;
seq1.name_("Pulse");
seq1.loopTimes_(2);
seq1.add (ch1);

// TWELVE SHIFTS LoopableSequence
seq2 = LoopableSequence.new;
seq2.name_("Twelve shifts");
seq2.loopTimes_(2);
seq2.add (ch2);
seq2.add (ch3);

// Final Chooser - clapping music
ch4 = Xhooser.new;
ch4.name_("Clapping music");
ch4.noseCone_(2);
ch4.addLane( Lane.new.weight_(inf).nest(seq2));
ch4.addLane( Lane.new.weight_(inf).nest(seq1));
tc4 = TimeChooser.new;
tc4.noseCone_(1);
tc4.addLane( TimeLane.new.beats_(48)); // 48*12 = 576
ch4.timeChooser_(tc4);
^ch4

// soft stops on PWrappers, pbinds  and clap2 now implemented & working!!


}

}




