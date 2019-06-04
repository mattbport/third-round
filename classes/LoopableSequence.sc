// SHOULD  HAVE COMMON SUPERCLASS WITH SEQUENCE
LoopableSequence{   // or sequencer better name? NO

	var <>choosers; // COPY would require Kopying each
	var<>clocks;       // COpy would need frosh ones
	//var <> singleClock;
	var<> timeline;
	var <>loop;              // this is for recursion
	var < loopTimes;   // usually inf - but here prudent to always limit repeats to something finite
	                              // else may crash the scheduler - since the server is not taking the load
	                               // but we can still splat fullstop
	var <> loopMax;    // used as a cap to loopTimes
	var<> duration;       // could be basic duration  - but once chosen....
	var<> name;
	var <> hasParent ; // not used
	var <> outBus;
	var  <> currentChooser;
	var <> smartDuration ; // needed for nesting
	var <> group; // COpy would need frosh ones
	var<> siblingGroup;


	/// we really need to  be clear about & distinguish
	// how long a single run of the  sequecne  of choosers with their specified chocies takes
	// for a single loop
	// then we need to know how many loops - does it choose again for each loop?
	// then we need to know  how long an external hard stop is.

	 //  ********************
	// NB WHEN CREATED BY WRAP IN WRAPPER ADDS one choosers to choosers
	// OOOPS - outers have wrappers - innres dont???? or wrap them now>
	//douple nested- no bother - its just a chooser!!!!
	// but lane holds a wrapper that thinks its a sample
	// ********************

*new{
		var me;
		^ super.new.init;}

init{ choosers = List.new;
		timeline = List.new;
		clocks = List.new;
		name= "unNamed loopSeq";
		//singleClock = TempoClock(SampleBank.tempo);
		//clocks.add(singleClock);
		loop = true;
		// group = Group.new;
		loopTimes = 1;
		loopMax = 32;  // was 16whoa - not getting changed - who sets this? - ah its loop times we want....
	}



	kopy {
		// WAIT - DONT WE HAVE TO WRAP EACH???? NO!
		var me, hygene;
		  //this.debug("BetterKOPY  is called!!!!!!");
	      me = this.copy;
		  hygene = List.new;
		  me.choosers.do {arg each; hygene .add(each.kopy)};
		  me.choosers_(hygene);
		  // NB I HAVE THE SAME GROUP FROM POV of unneeded concrete tail end iterations to kill
		// but from pov of "I am a different sample  instance who may need repeats" - no!!!!
		  //--------
		  //me.group(Group.new);
		// me.clocks(List.new);
		^me
	}



   kopyOLD { // this is highly suspect but nneded if I am doiubly nested
		// OK so who actually uses me???? kopy m ight be better for finability...
		// AH!!! users are people who thing they are kopying a chooser
		// -ah - me in nsequecne in this clas......
		// so this should be Kopy


	var me;
			this.debug("COPY  is called!!!!!!");
	me = LoopableSequence.new;

		me.choosers(this.choosers.kopy); // who sets my group & clocks?
		me.clocks(this.clocks.deepCopy); //deepcopy?
		me.singleClock(this.singleClock.copy); //deepcopy?
		me.timeline(this.timeline.deepCopy); //deepcopy?
		me.loop(this.loop);
		// group will be new - which is what we want
		me.loopTimes(this.loopTimes);
		me.loopMax (this.loopMax);
		me.duration (this.duration);
		me.name (this.name);
		^ me
       // define copy for lanes & sample & time chooser -  all needed for loopableSequence
	}

polyDurations{ // when diffetn choosers run at different tempos
		this.cleanChoosers;
		this.choosers.collect { arg eachChooser; eachChooser.xSmartDuration	};
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

unsolvedBug{}



logEntry{	arg beats, aChooser;
		aChooser.isNil.if({"Chooser is nil - should not happen".postln; ^nil});
		                this.timeline.add(beats -> aChooser.chosenLanesAsArray);
	}

explore	{
		this.timeline.asArray.inspect}


printOn { | aStream |
		aStream << "a " << this.class.name << "  " <<  this.name;
		^aStream}


chosenLanesAsArray {
		this.unsolvedBug{}
		/* "needed for when a loopable sequence is playing role of xhooser
             in a  loopeable sequence -  such as occurs in
            riley apparently but should it be wrapped?"
		"maybe second wrapping is so it can be repeated, but no need?" */
		^ "see unsolvedBug"

}

add{arg aChooser;
	this.choosers.add(aChooser)}

addAll{arg aList;
	this.choosers.addAll(aList)}

allChosenSynths {
		this.allSequencedSynths}  /// CAreful - dont think is infiite loop - just recursive

allSequencedSynths{
		var allOfThem = List.new;
		this.choosers.do { arg eachChooser, i;  allOfThem.addAll (eachChooser.allChosenSynths)};
		^ allOfThem
	}

schedule{arg aPauseInBeats,  aChooser;
		 var tClock;
		tClock = TempoClock(SampleBank.tempo);
		 clocks.add(tClock);
		tClock.sched ( aPauseInBeats, {
			this.currentChooser_(aChooser); // unused
			aChooser.playChosen;
			 aChooser.name.debug("synths above woken by loopSequ clock");
			              nil  });
		   // was playchosenLanes
		//aChooser.name.debug("Scheduling"+ aChooser.sample.name);
		aChooser.name.debug("Scheduling")
// ***	     this.logEntry(aPauseInBeats, aChooser); // play prechosen, else choice not made till scheduled
	}



	//================== STOPPING==========

	basicFree { this.allSequencedSynths.do{ arg eachSynth, i; eachSynth.free; };
		                this.choosers.do { arg each; each.free};
                       this.choosers.do { arg each; each.stop};
		// this.debug("freeing choosers inside loopable sequence")

		}

killNoReDo{  this.debug("KILL NO REDO"); this.stop ; this.stopRun; this.group.free; this.siblingGroup.free;  // How to allow both looping and killing of excess
		//maybe clocks vs group..
		            this.killNoReDoNestedChoosersHidingInLanesOfMyReplicatedChoosers}

killJustThisIterationOfLoop{ this.debug("KILL ALLOW REDO "); this.stop; this.stopRun; }
// Note - the synths in both cases probbaly currently all have same  group

killAllowReDoNestedChoosersHidingInLanesOfMyReplicatedChoosers {
		this.choosers.do { arg each; each.allChosenSamples.do
			{ arg each; each.killAllowReoDo}}  /// just going exactly 2 deep?
	                                }
killNoReDoNestedChoosersHidingInLanesOfMyReplicatedChoosers {
		this.choosers.do { arg each; each.allChosenSamples.do
			{ arg each; each.killNoReDo}}  /// just going exactly 2 deep?
	                                }
killInnerGroup{ this.killNoReDoNestedChoosersHidingInLanesOfMyReplicatedChoosers;
		this.debug("OK to KILL no redo inner group - its a diff group ID")
	                   }


killNestedChoosersHidingInLanesOfMyReplicatedChoosers {
		this.choosers.do { arg each; each.allChosenSamples.do
			{ arg each; each.stopRun}} // was each.kill - this no better
	                                }

kill { this.debug("kill"); /*this.stop; */this.stopRun; //this.group.free;
		this.killNestedChoosersHidingInLanesOfMyReplicatedChoosers //  ******* should be pause not free
	}
	// to kill double nesting try using group!!!!!
     // yeah - but which group & when?
		// outer vs inner? - shoyld work automatically?

	stopRun {this.allSequencedSynths.do { arg eachSynth, i;
		               (eachSynth == nil).not.if
		{eachSynth.debug("stoprun all sequenced synths"); eachSynth.run(false)}};
		("stopped any CURRENTLY running synths in loopseq" + this.name).postln;
	}

	free { this.basicFree}

	stop { this.clocks.do{ arg eachClock, i; eachClock.stop};
		("stopped any future scheduled choosers  in loopseq clock collection" + this.name).postln;
		}


	clear { this.clocks.do{ arg eachClock, i; eachClock.clear}
		}



	/*
	kill{ this.free; this.stop; this.clear ; "loopable sequence just got killed".postln;
		   this.basicFree
		// but what if this goes all the way down t oanother level of nesting?
	}
	*/

	deepKill {

		this.choosers.do	{ arg each; each.deepKill};
		"loopable sequence just got DEEP killed".postln;
		"loopable sequence just got DEEP killed".postln;
		 this.kill


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




// =============== ENABLING RECURSION ==========
//Wrapper diverts external play calls here for recursion - REALLY??
//and wrapper renames normal plays as basic play

nSequences { arg n;
		//WHAT DOES THOS DO IF TEHSE ARE LOOP SEQ?
		// cant be!!!! can only hold one (at present)
		var current, deep,  sibGroup;
		sibGroup = Group.new;
		this.siblingGroup_(sibGroup);
		 current = this.choosers.copy; // just for hygene
		 (n >1).if {
			( n-1).do { current.addAll(this.choosers)}};
		 deep = current.collect {arg each; each.kopy} ;
		 deep .do { arg each, i;
			each.name_("Chooser"+i.asString);
			each.myGroup(sibGroup);
			// each.cleanAllSamples(i); synth vs synthdef - cofuxed
			each.choose; /*each.debug("chosenlanes") */ };
		choosers = deep;
		//choosers.choose;
		^choosers


	} // not copies? used by play chosen
	// JUST PUT IN COPY FOR CLAPPING..... DID NOT HELP
	// try other examples


choose {   var sequenceDuration =0 ;
		                       //choosers.debug("choosers in choose in Loopable");
		        choosers.do{ |eachChooser|
			     eachChooser.isNil.if ( {}, {
			eachChooser.choose; // was choose Lanes
			                    //eachChooser.duration.isNil.if { eachChooser.inspect };
				                //eachChooser.debug("The chooser");
			                    //eachChooser.name.debug("Choose before first sequenced play");
			                    // eachChooser.duration.debug("NEW CHOICE");
		   	                   // eachChooser.duration.debug("Chooser duration as chosen");
				sequenceDuration = sequenceDuration + eachChooser.duration} );

		           this.duration_(sequenceDuration);
			// this.duration.debug(" sequence duration ")
		}
		}

		//"===Ready to play above choices ====" .postln;
		//"=========================".postln;
		// duplicated in Xhooser wrapper


	prebake{
		// ah - not nest time - nor at play time...
		// profiling tools?
		this.cleanChoosers;
		this .choose;
		this.prepareToPlayChosen;
	}

	play{
		//"=== FRESH PLAY OF SEQUENCE =====" .postln;
		//  HAS MAJOR OVERHEAD IF DOUBLY NESTED!!!!
		//"=========================".postln;
		   this.cleanChoosers;
           this.choose;
	     // needed for fresh play, sequencing info & recursion
           ^ this.playChosen }

	cleanChoosers {
		this.choosers.removeAllSuchThat { arg eachItem;  eachItem.isNil  } }

playChosen{
		var arrayOfnThis;
     this.loopTimesIsOne.if{ ^ this.basicPlayChosen};
		//this.debug("SEEMS LIKE A FIX");
	 arrayOfnThis= this.nSequences(this.loopTimes);
		this.basicPlayChosen

	/*
	 arrayOfnThis.inject( 0, { arg startTimeInBeats, eachUnusedDummySequence ;
			"start time of sequence iteration".postln;
			 startTimeInBeats.postln;
			this.basicPlayChosenAt(startTimeInBeats);
			startTimeInBeats + this.duration} )   */
	}


basicPlayChosen{
		^ this.basicPlayChosenAt(0) }                       // these all return duration of the sequence

basicPlayChosenAt{
		       // schedules once the basic sequence
		arg initialStartTime;
		var offsetStartTime; //unused
		timeline = List.new;
		                      // choosers.size.debug("Choosers size in basicPlayChosenAt");
		    // choosers.do{ |eachChooser| // eachChooser.name.debug("firing order");
		   choosers.inject(initialStartTime,{ arg nextStartTime, eachChooser;
			                          // var copyChooser =  eachChooser.copy;
			                       	//offsetStartTime = initialStartTime + nextStartTime;
			                        // schedule cumulative starttime with fixed offset from method argument
				                   this.schedule(nextStartTime,eachChooser );   // NOT THE COPY!!!!
					                 // nextStartTime.debug("next item start time ");
			                         // eachChooser.debug("this chooser");
			                         //eachChooser.duration.debug(" next item duration");
			                       nextStartTime.debug("next start time" +
				"next duration" + eachChooser.duration.asString);
			                        nextStartTime + eachChooser.duration;

		} ) }
	    // play returning duration of sequence is needed  for sequence with repeats to work sensibly
	    //and need that for nested choosers to work - OH - read about scheduler basics....

}


