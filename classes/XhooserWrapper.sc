// LOTS OF DUPLICATION
// Holds a loopable sequence  - which may contain a single chooser -  and makes it look like a Sample
// Holds a loopable sequence (ypically of choosers) and makes it look like a sample
// holds a Xhooser and pretends to be a sample -  but for that to work, takes a loopable sequence of xhoosers
// SHOULD EITHER BE SUBCLASS OR HAVE COMMON SUPERCLASS WITH SEQUENCE AND SEQUENCE WRAPPER
//SHOULD THIS BE PLAYABLE WRAPPER?
// but I dont think plain sequence is ever used, and sequence wrapper not needed - just use one of thee to make a (loopable) sequecne look like a samp;e
XhooserWrapper // Decorator (I think)

{
		var <>buffer ;
		var <>synth;
		var <> name;
	    var <> wavName;
	    var <>atomicRepeats; // one more nesting of loopableSequ
	    var  <>loop;
	    var <> smartDuration = 0;   // gets set for me by lane
	    var  <> loopableSequence;
	    var <>parentName;
	    var<>  group;
	    // var <> chooser; // some redundncy? always in loopable? but convenient - now a method
	    var <> clocks;


	kopy { var me;
		      me = this.copy;
		      me.group_(Group.new);
		me.parentName_("kopy wrapper - who is parent?");
		this.loopableSequence_(this.loopableSequence.kopy)
		// shouldnt group be different?  YES - like differetn instance of sample has own node
		^ me
	}



	// should have 'add'  that takes argument Xhooser and automatically puuts its in a loopable sequencer

//====== QUERYING ===========

// ***** BE CAREFUL WITH copy **** !!!!!!!

hasLoop {
		^this.loopableSequence.loopIsOn}

hasNoLoop {
		^this.loopableSequence.loopIsOff }

	isWrapper { ^ true}


// CREATING & INITIALIZING =========

	// why the wav stuff???
*new { arg  aName, aWavName;
			var me = super.new;
		     me.name_(aName.asSymbol);
		     me.wavName_ (aWavName);
			^ me.init}

init {  loopableSequence = LoopableSequence.new;
		 this.group_(Group.new);
		 loopableSequence.group_(this.group);
		  clocks = List.new;
		  name = "Nested chooser wrapper"
		}


warmUp { "Nested Chooser need not warm up".postln;   ^nil}

createBuffer{  "Nested Chooser need not create buffer".postln;    ^nil}

createSynthDef {  "Nested Chooser need not create SynthDef".postln;   ^nil}

choose {this.chooser.choose }


//========= SETTING VALUES  =======


loopOn {
		this.loopableSequence.loopOn; ^this}  //   how about infinit

loopOff {
		this.loopableSequence.loopOff; ^this}


//========= CORE PROTOCOL  =======

wrap { arg aLoopableSequence;
		//this.chooser_ (aChooser);
		//this.chooser.hasParent(true); // not even used - but is defined in Xhooser
		this.loopableSequence.add(aLoopableSequence) // .. holds 'choose r'in 2  suggetsive places
		        // though it is same chooser - not a duplicate - still - smells a bit funny
		        // it may well  actually be a loopable seqeucne
	}

	chooser { ^ 	this.loopableSequence}


play { //this.choose
		 // now we know duration so can calculate loopage
		 this.group_(Group.new); /// Interesting
		this.loopableSequence.play  }


basicDuration{
		^this.duration }  //Clearer name to communicate this is duration of sample
	                             // when not changedby external factors


duration { ^ this.chooser.duration}
		// basic duration of chooser  this time!!!- when lanes chosen!!!
		 // unlike sample , dont know length until choose before playing.


free { this.basicFree}

basicFree { this.chooser.free}

stop { this.loopableSequence.stop}

clear { this.loopableSequence.clear}

kill{ this.loopableSequence.kill } // to be implemeneted as KillnoRedo

killNoReDo{ this.loopableSequence.killNoReDo}
killAllowReDo{ this.loopableSequence.killJustThisIterationOfLoop}
killInnerGroup{ this.loopableSequence.killInnerGroup}
// Note - the synths in both cases probbaly currently all have same  group



deepKill {this.loopableSequence.deepKill}

	bugFix { arg num;
		this.debug("bugFix not yet implemented for Xhooser wrapperor loopable sequences")}


//  ========== PLAYING ===================


hardPlay{ arg tcDuration;
	     	 var  myClock =   TempoClock(SampleBank.tempo);
		   // this.clocks.add(myClock); // - OOPS dont want this being killed
		     this.hardDuration(tcDuration);  // tough bug to find on new plays
		     this.play;
		//*** this.debug("Playing nested loopable sequence ");
		myClock.sched( ( tcDuration   /*- 0.005 - 0.05 */)  ,
			     // should not be needed - even if killnoredo comes late
			    // should have the means to kill unneeded  next shooser
			    // that  has already started

			    // NO!!! DONT - but maybe do want    - 0.03 or maybe 0.05
			    //this.chooser.kill - 3 ms early to give it time to kill
			       {// this.group.free;
				    this.chooser.killNoReDo;
				    this.debug(" NESTED HARD STOP loopSeq kill - group" + group.asString);
				   nil})
	}
			           //this.loopableSequence.deepKill}) // for the case where its a loopable Seq

// this is excatly how time choosers (pre-) stop sample slots in lanes that contain  (wrappd) loopable sequecnes
// note that at the time the top level time choosers is influencing this call,
	// the xN choosers in the loopeable xequence contained in this xgossewrapper
	// have not yet been created or echeduled by the play - acually false &
	// doesnt matter anyway - see above code
	// AHAH!!!!! DONT KILL MY CLOCK!!!!!


//we dont know choosers duration till we chooseLanes in the nested chooser
// but we dont choose lanes in the nested chooser before parent chosoer says playchosen
// i think we need to do the soft play calculations
// whem paremt says play but
// BETWNN parent choosing and playingChosen
// which is possible  - thoigh i dong kno if parent chooser is like that at present
// it prob hasnt needed  to be like that yet since samples dont change basic duration
softPlay{ arg tcDuration;
	     	 var  myClock =   TempoClock(SampleBank.tempo);
		// this.clocks.add(myClock);    - dont want this being killed
		     this.softDuration(tcDuration);  // tough bug to find on new plays
		     (tcDuration <= this.basicDuration).if{this.loopOff;   };
              this.play;

		      (tcDuration > this.basicDuration).if{
			   myClock.sched( tcDuration + (this.duration/100),   {this.loopOff})}
	}

	/*
pause {
			this .synth.run(false) }

resume {
			this.synth.run(true) }
*/

// ============ INTRINSIC DURATION


//============= SETTING SMART DURATION  =============
// ---- NOT needed to play hard & soft stops right - just for sequencing

hardDuration {arg duration;                                       // misleading name - its a setter
		            this.smartDuration_(duration);
		           ^  duration }  // queried by lane & chooser

softDuration {arg duration;                                             // misleading name - its a setter
		              this.smartDuration_(  this.basicDuration *    this.neededRepeatsFor(duration));
		           ^  this.smartDuration }  // queried by lane & chooser

//=========  Helper methods  =======

neededRepeatsFor{
		 arg softStopDuration;
		 var containedRepeats ;
		  var modulo;
		  containedRepeats=  (softStopDuration/ this.basicDuration).floor;
	      modulo= softStopDuration %  this.basicDuration;
		 (modulo> 0 ).if( { ^ containedRepeats +1}, { ^ containedRepeats })
	}


// This protocol offers insight for making choosers nestable
// Does not need  protocol
// where  chooser calls  lane with news of presence or absence of time chooser
// and lane queries sample about  its basicduration
// and then lane calculates hard or soft duration based on its settings
// and tells sample  its smart duration, *which either lane or sample
// must tell tells seqeucne for proper sequencing

xSmartDuration{
	 this.chosenLanes.collect { arg eachLane; eachLane.xSmartDuration}
}
}

