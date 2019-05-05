// SHOULD  HAVE COMMON SUPERCLASS WITH SEQUENCE
LoopableSequence{   // or sequencer better name? NO

	var <>choosers;
	var<>clocks;
	var<> timeline;
	var <>loop;              // this is for recursion
	var < loopTimes;   // usually inf - but here prudent to always limit repeats to something finite
	                              // else may crash the scheduler - since the server is not taling laod
	                               // but we can still splat fullstop, it appears from testing  cerrent sequecne
	var <> loopMax;
	var<> duration;       // could be basic duration  - but once chosen....

*new{
		var me;
		^ super.new.init;}

init{ choosers = List.new;
		timeline = List.new;
		clocks = List.new;
		loop = true;
		loopTimes = 1;
		loopMax = 6;
	}

loopTimesIsOne{
		^ (this.loopTimes ==1)  }

loopTimes_ { arg aNum;
		( aNum > loopMax).if { loopTimes = loopMax; ^this};
		 loopTimes = aNum
	}

loopOn {
		loopTimes = loopMax; ^this
	}

loopOff {
		loopTimes = 1; ^this
	}

loopIsOn {
		^(loopTimes >1 )
	}

loopIsOff {
		^(loopTimes ==1)
	}





add{arg aChooser;
	this.choosers.add(aChooser)}

addAll{arg aList;
	this.choosers.addAll(aList)}

allSequencedSynths{
		var allOfThem = List.new;
		this.choosers.do { arg eachChooser, i;  allOfThem.addAll (eachChooser.allChosenSynths)};
		^ allOfThem
	}

schedule{arg aPauseInBeats,  aChooser;
		var tClock;
		tClock = TempoClock(SampleBank.tempo);
		clocks.add(tClock);
		tClock.sched ( aPauseInBeats, {  aChooser.playChosenLanes; nil  });
	     this.logEntry(aPauseInBeats, aChooser); // play prechosen, else choice not made till scheduled
	}



logEntry{	arg beats, aChooser;
		aChooser.isNil.if({"Chooser is nil - should not happen".postln; ^nil});
		                this.timeline.add(beats -> aChooser.chosenLanesAsArray);
	}

explore	{
		this.timeline.asArray.inspect}


printOn { | aStream |
		aStream << "a " << this.class.name << "  " <<  this.timeline;
		^aStream}

	//================== STOPPING==========

	free { this.allSequencedSynths.do{ arg eachSynth, i; eachSynth.free}
		}

	stop { this.clocks.do{ arg eachClock, i; eachClock.stop}
		}


	clear { this.clocks.do{ arg eachClock, i; eachClock.clear}
		}

	kill{ this.free; this.stop; this.clear}


//================== ENABLING RECURSION ==========
	// Hold on - want to nest both choosers and seqeucnes in lanes.... and also to nest sequence in sequecnes (I guess)
// so need to step back & get protocol neat globally

// Maybe  new class repeatable sequence
// to keep sequecne intelligible


hasLoop {
		^this.loop == true}

hasNoLoop {
		^this.loop == false}




// =============== ENABLING RECURSION ==========
//Wrapper diverts external play calls here for recursion
//and wrapper renames normal plays as basic play

nSequences { arg n;
	   ^ Array.fill( n, {arg index; this}) }


choose {   var sequenceDuration =0 ;
		                       //choosers.debug("choosers in choose in Loopable");
		        choosers.do{ |eachChooser| eachChooser.chooseLanes;
			                    //eachChooser.duration.isNil.if { eachChooser.inspect };
			                    //eachChooser.name.debug("Choose before first sequenced play");
			                    // eachChooser.duration.debug("NEW CHOICE");
		   	                    //  eachChooser.duration.debug("Chooser duration as chosen");
			                         sequenceDuration = sequenceDuration + eachChooser.duration};
		           this.duration_(sequenceDuration);
		//this.duration.debug("Unused sequence duration as set");
		//"===Ready to play above choices ====" .postln;
		//"=========================".postln;

	}	// duplicated in Xhooser wrapper

	play{
		//"=== FRESH PLAY OF SEQUENCE =====" .postln;
		//"=========================".postln;

		//this.debug("new choose in sequence");
           this.choose;
	     // needed for fresh play, sequencing info & recursion
           ^ this.playChosen }


playChosen{
		var arrayOfnThis;
     this.loopTimesIsOne.if{ ^ this.basicPlayChosen};
		//this.debug("DOESNOT HAPPEN");
	 arrayOfnThis= this.nSequences(this.loopTimes);
	 arrayOfnThis.inject( 0, { arg startTimeInBeats, eachDummySequence ;
			"start time of iteration".postln;
			 startTimeInBeats.postln;
			(startTimeInBeats + this.basicPlayChosenAt(startTimeInBeats))} )
	}


basicPlayChosen{
		^ this.basicPlayChosenAt(0) }                       // these all return duration of the sequence

basicPlayChosenAt{
		       // schedules once the basic sequence
		arg initialStartTime;
		var offsetStartTime; //unused
		timeline = List.new;
		                      // choosers.size.debug("Choosers size in basicPlayChosenAt");
		choosers.do{ |eachChooser| // eachChooser.name.debug("firing order");
		^ choosers.inject(initialStartTime,{ arg nextStartTime, eachChooser;
			                       	//offsetStartTime = initialStartTime + nextStartTime;
			                        // schedule cumulative starttime with fixed offset from method argument
				                   this.schedule(nextStartTime,  eachChooser );
					               //  nextStartTime.debug("when actually sequenced");
			                       // eachChooser.duration.debug(" replied  durations after  Sequenced");
			                        nextStartTime + eachChooser.duration} ) }
	    // play returning duration of sequence is needed  for sequence with repeats to work sensibly
	    //and need that for nested choosers to work - OH - read about scheduler basics....

	}
}

