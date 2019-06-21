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
	var<> parentName;
	var <> hasParent ; // not used
	var <> outBus;
	var  <> currentChooser;
	var <> smartDuration ; // needed for nesting
	var <> group; // SIngle gtoup for this  loopSeq - easy way to kill future synths (if exist)
	var<> siblingGroup;
	var <> debugMode = false;
	var <> debugMode2 = false;
	var <> synthsHaveBeenFreed = false;
     var <> noActiveTimeChooser = false;  // for protocol consistency with trim4sample




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

reps{ arg aNum;
		this.loopMax_(aNum-1);
		this.loopTimes_(aNum-1)}


init{ choosers = List.new;
		timeline = List.new;
		clocks = List.new;
		name= "unNamed loopSeq";
		//singleClock = TempoClock(SampleBank.tempo);
		//clocks.add(singleClock);
		loop = true;
		// group = Group.new;
		loopTimes = 1;
		loopMax = 256;  // was 16whoa - not getting changed - who sets this? - ah its loop times we want....
	}


myGroup{ arg g;
		// called by lo0pable sequecne
		//ALternative would be to do nothing
		   this.group_(g); // nasty pointless dangerous duplication memo
		this.choosers.do{arg each; each.group(g)}
		   // nb telling samples ie synth defs ie synths not created yet
	}


	kopy {
		// WAIT - DONT WE HAVE TO WRAP EACH???? NO!
		var me, hygene;
		  //this.debug("BetterKOPY  is called!!!!!!");
	      me = this.copy; // what does this do to gtoup? prob keeps the ref
		  hygene = List.new;
		  me.choosers.do {arg each; hygene.add(each.kopy)};
		  me.choosers_(hygene);
		  // NB I HAVE THE SAME GROUP FROM POV of unneeded concrete tail end iterations to kill
		// but from pov of "I am a different sample  instance who may need repeats" - no!!!!
		  //--------
		  //me.group(Group.new);
		// me.clocks(List.new);
		^me
	}
// CLEAN UP
	// Clocks, objects nodes
	// looks like Kopy shares lookseq group & clocks

cleanUp {
		this.cleanUpClocks;
		this.cleanUpChoosers;
		this.cleanUpCurrentChooser;
		this.cleanUpRest
	}

cleanUpClocks {
		this.clocks.do { arg eachClock, i;    eachClock.isNil.not.if{ eachClock.stop}};
		this.clocks_( nil)
	}

cleanUpChoosers {
		this.choosers.do { arg each ;  each.isNil.not.if{ each.cleanUp }} ;
		this.choosers_(nil)
	}

cleanUpCurrentChooser {
		this.currentChooser.isNil.not.if{ this.currentChooser.cleanUp; this.currentChooser_(nil)}
	}

cleanUpRest {
		this.timeline_ (nil);
		this.loop_(nil);
		// this.loopTimes_(nil);
		this.loopMax_(nil);
		this.name.isString.if{this.name_(this.name + "cleaned")};
		this.parentName.isString.if{parentName_(this.name + "cleaned")};
		this.hasParent_(nil);
		this.outBus_(nil);
		this.duration_(nil);
		this.smartDuration_(nil);
		this.group_(nil);
		this.siblingGroup_(nil);
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
		aStream << "a loopSeq named —" <<  this.name << "—" ;
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
		tClock = TempoClock(SampleBank.tempo);  // new instance - not the default
		 clocks.add(tClock);
		tClock.sched ( aPauseInBeats, {
			//this.currentChooser_(aChooser); // unused
			aChooser.name.debug("Wake next repeat of nested item with new choices");
			("      via clock in loopSequ named—" + this.name +" — with following synth(s)").postln;
			aChooser.playChosen;

			              nil  });
		   // was playchosenLanes
		this.debugMode2.if {aChooser.name.debug("Scheduling"+ aChooser.name)}
//  aChooser.name.debug("Scheduling")
// ***	     this.logEntry(aPauseInBeats, aChooser); // play prechosen, else choice not made till scheduled
	}



	//================== STOPPING==========\

/* killJustThisIterationOfLoop{ this.debug("KILL ALLOW REDO "); this.stop; this.stopRun; }
// Note - the synths in both cases probbaly currently all have same  group */

/* killAllowReDoNestedChoosersHidingInLanesOfMyReplicatedChoosers {
		this.choosers.do { arg each; each.allChosenSamples.do
			{ arg each; each.killAllowReoDo}}  /// just going exactly 2 deep?
	                                } */

/* killNoReDoNestedChoosersHidingInLanesOfMyReplicatedChoosers {
		this.choosers.do { arg each; each.allChosenSamples.do
			{ arg each; each.killNoReDo}}  /// just going exactly 2 deep?
	                                } */

/* killInnerGroup{ this.killNoReDoNestedChoosersHidingInLanesOfMyReplicatedChoosers;
		this.debug("OK to KILL no redo inner group - its a diff group ID")
	                   } */


	killNoReDo{  this.debug("KILL NO REDO");  this.stop ; this.stopRun;
		"Freeing SIBLING Group".postln; this.siblingGroup.free;
		            // this.group.free;
		           // REALLY??????? after proper cleanup????
		          /// this.killNoReDoNestedChoosersHidingInLanesOfMyReplicatedChoosers;
		this.name.debug("name of loopSeq");
		//this.clear;
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

free { this.basicFree}


basicFree {      this.synthsHaveBeenFreed.not.if {

		               this.allSequencedSynths.do{ arg eachSynth, i; eachSynth.free; };
		                this.choosers.do { arg each; each.free};
                      //  this.choosers.do { arg each; each.stop};
		               this.synthsHaveBeenFreed_(true);
	              };
		// this.debug("freeing choosers inside loopable sequence")

		}

stopRun { ("Stop any CURRENTLY running synths in loopseq named —" + this.name).postln;
		this.allSequencedSynths.do { arg eachSynth, i;
		               (eachSynth == nil).not.if
		{eachSynth.debug("  stoprun all sequenced synths"); eachSynth.run(false)}};

	}


stop { this.clocks.do{ arg eachClock, i; eachClock.stop};
		("Stopped any FUTURE scheduled choosers by STOPPING —"
			+ this.clocks.size.asString +  " — TempoClocks"). postln;
			("     for loopseq named —" + this.name + "—").postln;
		}


	clear { this.clocks.do{ arg eachClock, i; eachClock.clear}
		}



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
		//NB THESE CONTAIN  LOOP SEQ IN DOUBle NEST
		// cant be!!!! can only hold one (at present)
		// looks like would work for artisinal sequecne
		var current, deep,  sibGroup;
		sibGroup = Group.new;
		this.siblingGroup_(sibGroup);
		 current = this.choosers.copy; // just for hygene
		 (n >1).if {
			( n-1).do { current.addAll(this.choosers)}};
		 deep = current.collect {arg each; each.kopy} ;
		 deep .do { arg each, i;
			each.name_("Chooser"+i.asString);
			each.myGroup(sibGroup);  /// ** Passes via chooser to lane to sample to synth
			                                           /// or via chooser to lane to xhooserwrapper
			                                  /// Cannot be wrapper at present- is always chooser!!!!
			                                            /// to nest raw seqeucnews
			                                            //would need to implement myGroup in xhoooser wrapper
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
           this.choose; // may just  contain single chooser at this stage
		                       // but could already be  an artisinal sequecne
	     // needed for fresh play, sequencing info & recursion
           ^ this.playChosen  // this is where a single clones if loop is on
                                       // but could it work for artisinal sequecne?
	}

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
			 this.debugMode.if{
			 nextStartTime.debug(
					"next duration" + eachChooser.duration.asString +"next start time");};
				nextStartTime + eachChooser.duration;

		} ) }
	    // play returning duration of sequence is needed  for sequence with repeats to work sensibly
	    //and need that for nested choosers to work - OH - read about scheduler basics....

}


