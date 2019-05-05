// SHOULD HAVE COMMON SUPERCLASS WITH LOOPABLESEQUENCE
Sequence{   // or sequencer better name? NO

	var <>choosers;
	var<> timeline;
	var <>loop;              // this is for recursion
	var < loopTimes;   // usually inf - but here prudent to always limit repeats to something finite
	                              // else may crash the scheduler - since the server is not taling laod
	                               // but we can still splat fullstop, it appears from testing  cerrent sequecne
	var <> loopMax;
	var <> duration;

  // we dont have the equivalent of a pre-choose to work out duration without playing
	// should thsi be duration of basic duration - thing about pattern
	// would be called choose. they should all be called choose for poly

*new{
		var me;
		^ super.new.init;}

init{ choosers = List.new;
		timeline = List.new;
		loopTimes = 1;
		loopMax = 4;
	}

loopTimesIsOne{
		^ (this.loopTimes ==1)  }

loopTimes_ { arg aNum;
		( aNum > loopMax).if { loopTimes = loopMax; ^this};
		 loopTimes = aNum
	}


add{arg aChooser;
	this.choosers.add(aChooser)}

addAll{arg aList;
	this.choosers.addAll(aList)}

allChosenSynths{   // maybe should be named  allSequencedSynths
		                     // is there a PROBLEM if not yet scheduled so not yet created?
		                     // maybe I nee dto flush the scheduler instead. TempoClock .clear or .stop
		                      // - no - this is right - kill some of the live ones. ah no - need partial clear?
		                       // mabe store named scheduler???
		var allOfThem = List.new;
		this.choosers.do { arg eachChooser, i;  allOfThem.addAll (eachChooser.allChosenSynths)};
		^ allOfThem
	}

schedule{arg aPauseInBeats,  aChooser;
		TempoClock(SampleBank.tempo).sched ( aPauseInBeats, {  aChooser.playChosenLanes; nil  });
	     this.logEntry(aPauseInBeats, aChooser); // play prechosen, else choice not made till scheduled
	}


choose { choosers.do{ |eachChooser| eachChooser.chooseLanes}}

play{
		this.choose;                                                                  // needed for recursion to work
		^ this.playChosen }                                                        // and other reasons

playChosen{
		^ this.playChosenAt(0) }                                       // these all return duration of the sequence

playChosenAt{
		arg startTime;
		timeline = List.new;

		^ choosers.inject(startTime,{ arg thisPauseInBeats, eachChooser ;
		                              this.schedule(thisPauseInBeats,  eachChooser );
			                              duration =
			                              thisPauseInBeats + eachChooser.duration} ) }
	    // play returning duration of sequence is needed  for sequence with repeats to work sensibly
	    //and need that for nested choosers to work - OH - read about scheduler basics....




logEntry{	arg beats, aChooser;
		aChooser.isNil.if({"Chooser is nil - should not happen".postln; ^nil});
		                this.timeline.add(beats -> aChooser.chosenLanesAsArray);
	}

explore	{
		this.timeline.asArray.inspect}


printOn { | aStream |
		aStream << "a " << this.class.name << "  " <<  this.timeline;
		^aStream}

	stop{ // there's probably some way - apart from splat fullstop - maybe free everything - or just conatined synths
	}




//================== ENABLING RECURSION ==========
	// Hold on - want to nest both choosers and seqeucnes in lanes.... and also to nest sequence in sequecnes (I guess)
// so need to step back & get protocol neat globally

// Maybe  new class repeatable sequence
// to keep sequecne intelligible


hasLoop {
		^this.loop == true}

hasNoLoop {
		^this.loop == false}

loopOn {
		this.loop_ (true)}

loopOff {
		this.loop_ (false)}


	/*  =============== ENABLING RECURSION ==========
Wrapper diverts external play calls here for recursion
and wrapper renames normal plays as basic play

play{
   choosers.do{ |eachChooser| eachChooser.chooseLanes}; // needed for fresh play, sequencing info & recursion
^ this.playChosen }


playChosen{
     this.loopTimesIsOne.if{ ^ this.basicPlayChosen)}
	// nb each seqeucne is this!!!!!!
	[list with loopTimes many dummyequecnes in].inject{ 0, { arg startTimeInBeats, eachDummySequence ;
	                                                          startTimeInBeats + this.playBasicChosenAt(startTimeInBeats} )


basicPlayChosen{
		^ this.playBasicChosenAt(0) }                                                        // these all return duration of the sequence

basicPlayChosenAt{
		arg startTime;
		timeline = List.new;
		^ choosers.inject(startTime,{ arg thisPauseInBeats, eachChooser ;
		                              this.schedule(thisPauseInBeats,  eachChooser );
			                              thisPauseInBeats + eachChooser.duration} ) }
	    // play returning duration of sequence is needed  for sequence with repeats to work sensibly
	    //and need that for nested choosers to work - OH - read about scheduler basics....



	*/




}