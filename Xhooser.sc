// SHOULD  HAVE COMMON SUPERCLASS WITH TIMECHOOSER
Xhooser {
	var <>noseCone;
	var <>lanes;
	var <>chosenLanes;
	var <> timeChooser;
	var <> journal ;
	var <> name;
	var <> hasParent;
	var <> outBus;
	var <> myclocks;  // maye be needed for nesting -NO !! in wrapper!!
	   // but could record an event stream - instance of non det command pattern?
	   // approx 1000 LOC total

// =====  INITIALIZATION	 =======
init{
	lanes = List.new;
		chosenLanes = List.new;
		journal = List.new;
		hasParent = false;
		name ="Unnamed Chooser";
	}

*new { ^ super.new.init}

/*
	// Royally screws up clapping and loopable seqnecne
copy {
	var me;
	me = Xhooser.new;
		me.noseCone_(this.noseCone);
				me.lanes_(this.lanes.copy); //deepcopy?
				me.chosenLanes_(this.chosenLanes.copy); //deepcopy?
				me.timeChooser_(this.timeChooser.copy); // copy
				me.journal_(this.journal.copy);
		         me.hasParent_(this.hasParent.copy);
				me.name_(this.name);
		^ me
       // define copy for lanes & sample & time chooser -  all needed for loopableSequence
	}
*/


// ====== LANE ADDING & REMOVAL  =========
addLane{
		arg aLane;
		this.lanes.add(aLane);
		// this.lanes.debug("lanes")
	}

//======= Printing  ==========
	printOn { | aStream |
		aStream << "a " << this.class.name << " chosen lanes " <<  this.chosenLanes;
		^aStream}

	journalAsArray {
		^ this.journal.asArray}

	chosenLanesAsArray {
		^ this.chosenLanes.asArray}

	explore{ ^ this.chosenLanesAsArray.inspect}


// ========TESTING  QUERYING & Accessors ========

isChooser {^true}

noseConeIsInfinite{
		^this.noseCone == inf}

noseConeIsZero{
		^this.noseCone == 0}

hasPriorityBoarders {
			^ this.priorityBoarders.size>0}

hasTooManyPriorityBoarders{
		      ^ (this.priorityBoarders.size > noseCone)}

hasTimeChooser{
		// this.timeChooser.debug("time chooser");
		^(this.timeChooser==nil).not
	}

hasActiveTimeChooser{
		^ this.hasTimeChooser.and( {this.timeChooser.isActive});
		}

lanesNotChosenYet{
		this.chosenLanes.isNil.if { "chosenLanes not  list - should not happen".postln; ^ true};
		^this.chosenLanes.isEmpty
	}

allChosenSynths{
		^ this.chosenLanes.collect{ arg eachLane, i;  eachLane.synth}
	}


	allChosenSamples{
		var chosenSamples;
		chosenSamples = this.chosenLanes.collect{ arg eachLane, i;  eachLane.sample};
		// chosenSamples.debug("collecting chosen samples for freeing");
		^chosenSamples
	}
integrityCheck{
		this.lanes.do {arg eachLane, index; eachLane.sample.isNil.if { "SampleBank not loaded".postln}}
	}




// ======  DERIVED COLLECTIONS AND NUMBERS ============
priorityBoarders {
			^ this.lanes.select({ arg eachLane, index; eachLane.hasInfiniteWeight})   }

finiteWeightedNonZeroLanes{
		^ this.lanes.select({arg eachLane, index ; (eachLane.hasInfiniteWeight.not).and({eachLane.hasZeroWeight.not}) } )   }

nonZeroWeightedLanes {
		var nzwl;
		nzwl = this.lanes.reject({ arg eachLane, index; eachLane.hasZeroWeight});
		^ nzwl  // RETURNS ARRAY - so could not add playable time lane
	}

noOfLanesStillToPick{
			   // only works with finite noseCone - but
		       // nonDeterministicLaneChoice  ensures never gets called if noseCone is infinite
		this.noseConeIsZero.if { ^0};
		^  (this.noseCone -  this.chosenLanes.size)

	                       }
chosenTimeLane {
		this.hasActiveTimeChooser.if (
			{^ this.timeChooser.chosenLane},
			{  "No active time chooser2".postln; ^nil})
		    // what if none chosen yet?
		     // chooseLanes lgorithn ensures never gets called
		     // but good program defensively in case bugs
	}


// ========    CHOOSING LANES  - Helper Methods    =================

	chooseWinnersFromTooManPriorityBoarders	 {
			| pool|
			 pool = this.priorityBoarders.copy;
			 noseCone.do ({ | choice|
				                     choice = pool.choose;
			                         this.chosenLanes.add (choice);
				                     pool.remove(choice)});
			^ this.chosenLanes}

chooseWinnersFromFiniteNonZeroWeightedLanes{
		     // needs this.chosenLanes to have been initialised by
		      // nonDeterministicLaneChoice - should  chosenLanes have been a set
		var finiteNonZeroWeightedPool = this.finiteWeightedNonZeroLanes;
		 var pool = List.new;
		 var poolWeights = List.new;
		var  normalizedWeights;
		var noOfRemainingPicks;

		this.finiteWeightedNonZeroLanes.do({ arg eachLane; pool.add(eachLane);
			                      poolWeights.add(eachLane.weight)}); // initialize pool weights

		 noOfRemainingPicks = this.noOfLanesStillToPick;
		                                                          // overly cautious - probably not needed
	     noOfRemainingPicks.do({  | choice|
	     normalizedWeights = poolWeights.asArray.normalizeSum; //need to normalize
			                                      choice = pool.asArray.wchoose(normalizedWeights);
			                                                    // wchoose only works for arrays
			                                     (choice==nil).if(
			                       {"Not enough non-zero weighted playable lanes for nosecone".postln;
					                                    // maybe adjust one or the other in response?
			                                    });
			                                      chosenLanes.add(choice);
			                                      pool.remove(choice);
			                                       poolWeights = List.new;
			                                              // no need to remove chosen weight
			                                              //- just recreate weights for shrunken pool
			                                       pool.do ({ arg eachLane; poolWeights.add(eachLane.weight)});
			                                         })
			^ this.chosenLanes  }

// ========    CHOOSING LANES  - MAIN  METHODS   =================
nonDeterministicLaneChoice {
     	this.chosenLanes_(List.new);
		// Must be initialised HERE when this is called - not when creating the Xhooser
	   // 6 cases - zero noseCone
	this.noseConeIsZero.if ({ ^chosenLanes; }) ;//and we are out

	// infinite NoseCone
		this.noseConeIsInfinite.if ({
			       ^ this.chosenLanes.addAll(this.nonZeroWeightedLanes) }) ;//and we are out

    // noseCone is not infinite but there are  too many priority boarders...
	this.hasTooManyPriorityBoarders.if({ this.chooseWinnersFromTooManPriorityBoarders;  ^ chosenLanes  });
		// and we are out

	// Chooser does not have too many priority boarders
	//- so if there are any at all should add them all
	// but must go on next  to add any non zero weighted finite ones
	 this.hasPriorityBoarders.if({ chosenLanes.addAll(this.priorityBoarders) ;  });

     //  If no of priority boarders exactly matches number of seats, we are done
	    (this.noOfLanesStillToPick== 0).if({  ^ chosenLanes }) ; //and we are out

	//  make all (or any remaining) choices from from  solely finitely weighted lanes
		 ^ this.chooseWinnersFromFiniteNonZeroWeightedLanes
	         }

choose{
		^ this.chooseLanes}  // needed to let loopableSequence nest cleanly
	                                     // protocol polymorphism


	chooseLanes{
		//this.name.debug("in chooseLanes in Chooser");

		this.cleanLanes; // remove any nils from lists of lanes
		this.hasActiveTimeChooser.if {  this.timeChooser.chooseLane  }; // EXCELLENT
		this.nonDeterministicLaneChoice;
		this.cleanChosenLanes;
		this.hasActiveTimeChooser.if (
			{this.calculateSmartDurationWithChosenTimeLane},  // not needed to play it right - need for sequencing
			{this.calculateSmartDurationWithNoActiveTimeLane}); // not needed to play it right - need for sequencing

		this.chosenTimeLaneIsFullyPlayable.if {this.addPlayableTimeLaneToChosenLanes};
		^ this.chosenLanes
        // Lane not scheduled  or played yet - just chosen
		}

	cleanLanes {
				this.lanes.removeEvery(List[nil]);

	}


	cleanChosenLanes {
				this.chosenLanes.removeEvery(List[nil]);
		       this.chosenLanes.isEmpty.if{ this.chosenLanes.add(Lane.null)};
	}


// =========== PLAYABLE TIME LANES  ========================
	chosenTimeLaneIsFullyPlayable {
		this.hasActiveTimeChooser.not.if { "No active time chooser".postln; ^false};
		^ this.timeChooser.chosenLaneIsFullyPlayable
	}

	addPlayableTimeLaneToChosenLanes {
		 var newLane;  //  will this displace one lane from the count? nope
		this.hasActiveTimeChooser.and({this.chosenTimeLaneIsFullyPlayable}).if
		(    newLane = Lane.new.weight_(inf).sample_(this.timeChooser.chosenLane.sample);
			{ this.chosenLanes.add(newLane)}, {^ "No playableTimeLane available".postln})
	}


// =========== PLAYING  AND SCHEDULING =====================

playChosen{
		^ this.playChosenLanes}  // needed for neat nesting


playChosenLanes{  // doesn't choose fresh Lanes -  sticks with last choices
		                        this.integrityCheck; // dont think has opportunity to do anything
		          		       this.hasActiveTimeChooser.if (
		                         	{      this.journal.add( \hasActiveTimeChooser -> this.timeChooser);
				                            this.journal.add( \activeTimeLane -> this.chosenTimeLane);
				                           this.chosenLanes.do {|eachLane |
				                            eachLane.playWithChosenTimeLaneForParent(this.chosenTimeLane, this)}},
				                             {this.chosenLanes.do  { |eachLane | eachLane.play }     }  )
	                                }


play {                 //  PRINCIPLE: multiple hits of plain play always produce new choices
		                  // journals previous choices,
			this.chooseLanes;                         // empties out previous choices
		    this.journal.add( \chosenLanes -> this.chosenLanes.asArray);
		    this.outBus.debug("In Chooser"); // stereo
		    this.outBus.isNil.not.if {
			this.chosenLanes.debug("Hit chosen Lanes");
			this.chosenLanes.do
			{arg eachLane; eachLane.outBus_(this.outBus)}}; // stereo
		    this.playChosenLanes                  // may be interesting if we go nested
	}



pause{
		this.chosenLanes.do  { |eachLane | eachLane.pause} }

resume{
		this.chosenLanes.do  { |eachLane | eachLane.resume} }

stop { this.free}
kill {this.stop  } //to give uniform nesting protovol in wrapper for nester Loopable S's }

free { this.allChosenSynths.free;
		this.allChosenSamples.do { arg each; each.free } } // Fixed clap2 !!!!


//========= DURATION =========

// smart durations not needed for lane to DO right thing but needed
// in case this chooser is nested and it needs  to REPORT right thing
// to enclosing seqiences or choosers

calculateSmartDurationWithChosenTimeLane {
		// this.debug("alculateSmartDurationWithChosenTimeLane");
	   this.chosenLanes.do {|eachLane |
			// eachLane.debug(" about to calculateSmartDurationWithChosenTimeLane");
	         eachLane.calculateSmartDurationWithChosenTimeLaneForParent(this.chosenTimeLane)}}

calculateSmartDurationWithNoActiveTimeLane{
	             this.chosenLanes.do {|eachLane |
	                     eachLane.calculateSmartDurationWithNoActiveTimeLane}}

duration{                                   // sequencer calls this to find out when to sequence
		this.lanesNotChosenYet.if
		    {this.debug("No choices made yet  when querying Chooser duration- should not happen"); ^0};
	^	this.maxLaneDuration  // looks dubious - what if cut by timelane? - nah - looks OK
	}

durations{
		this.lanes.do{ |eachLane| eachLane.sample.name.postln;
			eachLane.sample.duration.postln;}}


durationOfChosenTimeLane{                     //just  to lower coupling in lane
		^this.chosenTimeLane.duration
	}


maxLaneDuration{
		var durations;
		var max;
		durations = chosenLanes.collect{ arg eachLane, i;  //eachLane.debug("max laneduration");
			//eachLane.sample.name.debug("lane name in MAX");
			//eachLane.smartDuration.debug(" lane smartduration in MAX");
			eachLane.smartDuration};
			//durations.debug("SMART DURATIONS OF EACH LANE GIVEN TO FIND MAX")
		max = durations.maxItem{arg item, i; item};
		// max.debug("max duration in chooser when checking duration");
		//(max==inf).if {this.halt};
		^max
	}




// PROTOCOL NEEDS CHECKING FOR UNNEEDED FLUFF & CRUFT

// used where different lanes have samples using different tempi,
// so that smart durations in beats translate differently into seconds
// as in clip wwrappers and nysteds. For obvious reasons, Lanes &
//(why chooser wrappers?) also need to undersand

xSmartDuration {
		var  smartDurations;
		var  inSeconds;
		var  max;
		//this.debug("in xhooser");
	    smartDurations = this.chosenLanes.collect
		        { arg eachLane ; eachLane.xSmartDuration.postln};
        inSeconds =  smartDurations.collect
		        { arg eachSmartDuration ; eachSmartDuration.asSeconds };
	    max =  inSeconds.maxItem({ arg item, i; item });
		// max.debug("seconds");
		^max
}

}



	