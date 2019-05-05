GeneralChooser{
	// exploring as possible superclass of loopable sequence (wher this  file started)
	 // any commonality wih  xhooser wrapper which is really just a wrapper fro a
	// loopale sequecne ((containing one or mor choosers) to make it look like a sample

	var <>choosers;
	var<>clocks;
	var<> timeline;
	var <>loop;              // this is for recursion
	var < loopTimes;   // usually inf - but here prudent to always limit repeats to something finite
	                              // else may crash the scheduler - since the server is not taking the load
	                               // but we can still splat fullstop
	var <> loopMax;
	var<> duration;       // could be basic duration  - but once chosen....
	var<> name;
	var <> hasParent ; // not used
	var <> smartDuration ; // needed for nesting
	var <> verbose;

*new{}
init{ }
copy {/* define copy for lanes & sample & time chooser -  all needed for loopableSequence */ }
polyDurations{ /* when different choosers run at different tempos */ }

loopTimesIsOne{ }
loopTimes_ { arg aNum;}
loopOn {	}
loopOff {	}
loopIsOn {	}
loopIsOff {}



chosenLanesAsArray {}
add{}
addAll{}
allSequencedSynths{	}
schedule{arg aPauseInBeats,  aChooser;}
logEntry{	arg beats, aChooser;}
explore	{this.timeline.asArray.inspect}
printOn { }

	//================== STOPPING==========

	basicFree {	}
	free { }
	stop { }
	clear { 	}
	kill{ }

//================== ENABLING RECURSION ==========

hasLoop {}
hasNoLoop {}

// =============== ENABLING RECURSION ==========
//Wrapper diverts external play calls here for recursion
//and wrapper renames normal plays as basic play

nSequences { arg n;
		^ Array.fill( n, {arg index;  this.copy  } ) } // not copies? used by play chosen
	// JUST PUT IN COPY FOR CLAPPING..... DID NOT HELP
	// try other examples

choose {     }
play{}
cleanChoosers { }
playChosen{ }

basicPlayChosen{}                       // these all return duration of the sequence
basicPlayChosenAt{	}

}


