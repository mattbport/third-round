RChoir {
	var voices;
	// voices = List.new;
}

Riley {
	// this class provides house keeping for what could be a
	// sequence creation tool  allowing different samples to have different time lanes
	// But as coded here, a bit messily,  this class also holds the resulting sequence
	// for one player
	// so init uses the message rileyChooser creates a loopable Sequence for each player
	// each with their own specially timed sequence of samples
	// Then in a piece of appalling code structuring
	// the message choir creates a regular chooser for the whole piece
	//  with each lane holding an instance of this class

	var min =5;  // min number of  beats to play each riff (gone for beats rather than repetitions)
	var range = 20;  // range of number of beats to play each riff (starting from min)
	var dur = 340;  // duration of the whole thing  (actually of the the pulse)  in beats
var randoms;    // 1000 random numbers chosen fresh each time - used by each
	//player to chose how many beats each riff will play (dont really need 1000)
var <> rileyChoosers; // each represents one riff being repeated by a single  player
var <> seq; // this will hold  the timed riff sequence
var <> hasParent ;  // harmless —unused, I think

*new { ^ super.new.init }



init {   this.getRandom; // just generate one set  that we can use
		                              // to reel off a few yards for reach  each sample & each  player
		  seq = LoopableSequence.new; // seq variable of each  riley will soon contain
		                                                   // a sequence of
		                                                   // 33 RileyChoosers, all with a different sample
		  // seq.verbose_(true);
		  33.do { arg i;   seq.add(this.rileyChooser(1+ i) )   }
	       ^ this} // return a Riley using seq to store  a sequ of 33 choosers indexed 1 to 34



rileyChooser { arg i;
		// init calls this to create
		var ch, tc, num; // with  simgle playable lane and a time lane
		(i > 0).if ({ num = min + randoms[i]}, {num = dur})  ; // create random duration
		      //within specified range -  or just the duration of the whole piece if I  am the pulse
		ch = Xhooser.new;
		ch.name_("Player " ++ i.asString); // give me a name
		ch.noseCone_(1);
		ch.addLane( Lane.new.weight_(1).namedSample(i.asSymbol).loopOn); //give me a sample
		tc = TimeChooser.new;
		tc.noseCone_(1);
		tc.addLane( TimeLane.new.weight_(1).beats_(num)); //set duration
		ch.timeChooser_(tc);

		^ch
	}

choir{
		var ch,  tc;
		ch = Xhooser.new;
		ch.name_(" in C Chooser " );
		ch.noseCone_(9);
		ch.addLane( Lane.new.weight_(inf).nest(Riley.new.pulse));
		ch.addLane( Lane.new.weight_(inf).nest(Riley.new.playable));
		ch.addLane( Lane.new.weight_(inf).nest(Riley.new.playable));
		ch.addLane( Lane.new.weight_(inf).nest(Riley.new.playable));
		ch.addLane( Lane.new.weight_(inf).nest(Riley.new.playable));
		ch.addLane( Lane.new.weight_(inf).nest(Riley.new.playable));
		ch.addLane( Lane.new.weight_(inf).nest(Riley.new.playable));
		ch.addLane( Lane.new.weight_(inf).nest(Riley.new.playable));
		ch.addLane( Lane.new.weight_(inf).nest(Riley.new.playable));
		tc = TimeChooser.new;
		tc.noseCone_(1);
		tc.addLane( TimeLane.new.weight_(1).beats_(600));
		ch.timeChooser_(tc);
		^ch
	}

pulse        { 	^ this.rileyChooser(0)}

playable	{ ^ this.seq}
play          { this.seq.play }

test           {  this.rileyChoosers[1].play}

	getRandom { // used by init to return an array of 1000 fresh random numbers
		var num;
		num = ((1000000 * TempoClock.default.beats) % 43).floor;
		    //nothing to do with tempo -  just using current time to get a different random seed
		randoms = Array.fill (num, { range.rand }); // for throwing away - no idea why
		randoms = Array.fill (1000, { range.rand });
		// randoms.debug ( "randoms");
		^ randoms // just an array of 1000 random numbers - we won't actually need that many
}


printOn { | aStream |
		aStream <<   " " << this.seq <<" ";
		^aStream
		}

}

/*
 SampleBank.populate;
 SampleBank.warmUp;

/   WORKS!!!!
Riley.new.choir.play


// WORKS !!!!!
Riley.new.pulse.play;
Riley.new.play;
Riley.new.play;
Riley.new.play;
Riley.new.play;
Riley.new.play;
Riley.new.play;
Riley.new.play;
Riley.new.play;

Riley.new.getRandom.size


*/

