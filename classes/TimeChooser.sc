// SHOULD  HAVE COMMON SUPERCLASS WITH CHOOSER
TimeChooser {
	var <>noseCone;
	var <>lanes;
	var <>chosenLane;
	var <>name;

init{
	lanes = List.new;
	      }

*new { ^ super.new.init}

addLane{
		arg aLane;
		this.lanes.add(aLane)}


cleanUp {
		this.cleanUpLanes;
		this.cleanUpRest
	}

cleanUpLanes {
		this.chosenLane.isNil.not.if { this.chosenLane.cleanUp} ;
		this.chosenLane_(nil);
		this.lanes.do { arg each ;  each.isNil.not.if{ each.cleanUp }} ;
		this.lanes_(nil)
	}

cleanUpRest {
		this.noseCone_ (nil);
		this.name_(nil);
	}

//======= Printing  ==========


kopy{ var me, nuLanes;
		me = this.copy;
		nuLanes = (this.lanes.collect{ arg eachLane ; eachLane.isNil.not.if {eachLane.kopy}}).asList;
		me.lanes_(nuLanes);
		^ me }





	printOn { | aStream |
		aStream << "a " << this.class.name << " with lanes " << this.lanes;
	    this.chosenLane.isNil.not.if(
			  {   aStream << " chosen lane " <<  this.chosenLane << " " ;

			});
		^aStream}


noseConeIsNil{
		^ this.noseCone == nil
	}

noseConeIsZero{   // still wonder if timechooser noseCones
		                    // should be simply  on or off,  active or inactive
		^this.noseCone == 0}

noseConeTooBig{
		^this.noseCone >1}


zeroWeightedLanes{
		^ (lanes.select { arg eachTimeLane, i ;  eachTimeLane.hasZeroWeight }).asList
	}

allLaneWeightsZero{
		^ (this.zeroWeightedLanes.size  == this.lanes.size)
		}

isActive{
		//this.noseCone.debug("Nose cone") ;
		^ this.noseConeIsZero.not.and(  {this.allLaneWeightsZero.not } )
	        }

isNotActive{
		this.isActive.not}


chosenLaneIsFullyPlayable{
	this.chosenLane.isNil.if {"No timeLane chosen yet". postln ^false};
	^this.chosenLane.isFullyPlayable
	}


priorityBoarders {
			^ this.lanes.select({ arg eachLane, index; eachLane.hasInfiniteWeight})   }

hasPriorityBoarders {
			^ this.priorityBoarders.size>0}

hasTooManyPriorityBoarders{
		      ^ (this.priorityBoarders.size > noseCone)}

finiteNonZeroWeightedLanes{
		^ this.lanes.select({arg eachLane, index ; (eachLane.hasInfiniteWeight.not).and({eachLane.hasZeroWeight.not}) } )   }


chooseWinnerFromPriorityBoarders	 {
		this.priorityBoarders.debug("chooseWinnerFromPriorityBoarders in time chooser");
			 ^ this.priorityBoarders.choose;
			                         }

chooseWinnerFromFiniteNonZeroWeightedLanes{
		 var pool = List.new;
		 var poolWeights = List.new;
		  var normalizedWeights;
		// this.debug("time chooser in chooseWinnerFromFiniteNonZeroWeightedLanes");
		  this.finiteNonZeroWeightedLanes.do(
			       { arg eachLane;
				     pool.add(eachLane);
			         poolWeights.add(eachLane.weight)}); // initialize pool weights
	      normalizedWeights = poolWeights.asArray.normalizeSum; //need to normalize
		  this.chosenLane_(pool.asArray.wchoose(normalizedWeights) )  ;// wchoose only works for arrays
		   (this.chosenLane == nil).if
		{"not enough non zero weighted time lanes available to meet nosecone demand".postln};
			^this.chosenLane
	        }

nonDeterministicLaneChoice {
	// 5 CASES
	//  zero noseCone

		this.noseConeIsNil.if {" Nose cone has no value set - setting to 1".postln ;   this.noseCone_(1);};
			                                 //this.halt

	this.noseConeIsZero.if {
			"nosecone in time chooser is zero- ignoring time chooser".postln;
			^ this.chosenLane_(Lane.empty) } ;//value of chosenLane matters - not return value.

	// If nosecone bigger than 1 (incl inf case) change nose cone to 1 and say doing it.
	// NOT YET IMPLEMENTED
		this.noseConeTooBig.if {"Nose cone in time chooser too big- reducing nosecone value to 1".postln;
			^ this.chooseWinnerFromFiniteNonZeroWeightedLanes};

	// If there are any priority boarders, one of those must win
	     this.hasPriorityBoarders.if({
			^ this.chosenLane_(this.chooseWinnerFromPriorityBoarders;  )});

	//  Nose cone must now  be  1  and there must be no priority boarders
		  ^ this.chooseWinnerFromFiniteNonZeroWeightedLanes;
     }



chooseLane{                                             // compare definition in Xhoosers
		this.nonDeterministicLaneChoice
	}



play {                            //   multiple hits of plain play always produce new choices
		                              // journal  of previous choices done in Xhooser
			this.chooseLane;                         // empties out previous choices
		    this.playChosenLane                  // may be interesting if we go nested

	}

duration{
		//  always duration based on most recent choice
		this.laneNotChosenYet.if {"No choice made yet - should not happen".postln; ^0};
	^	this.chosenLane.duration

}

laneNotChosenYet{
		^ this.choseLaneisNil.if { "chosen time Lane is nil".postln; ^ true};
	}


	}



