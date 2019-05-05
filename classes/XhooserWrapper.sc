// LOTS OF DUPLICATION
// SHOULD EITHER BE SUBCLASS OR HAVE COMMON SUPERCLASS WITH SEQUENCE
XhooserWrapper // Decorator (I think)

{
		var <>buffer ;
		var <>synth;
		var <> name;
	    var <> wavName;
	    var <>atomicRepeats;
	    var  <>loop;
	    var <> smartDuration = 0;   // gets set for me by lane
	    var  <> loopableSequence;
	     var <> chooser;

//====== QUERYING ===========


hasLoop {
		^this.loopableSequence.loopIsOn}

hasNoLoop {
		^this.loopableSequence.loopIsOn }


// CREATING & INITIALIZING =========

*new { arg  aName, aWavName;
			var me = super.new;
		     me.name_(aName.asSymbol);
		     me.wavName_ (aWavName);
			^ me.init}

init {  loopableSequence = LoopableSequence.new;
		 name = "nested chooser"
		}


warmUp { "Nested Chooser need not warm up".postln;   ^nil}

createBuffer{  "Nested Chooser need not create buffer".postln;    ^nil}

createSynthDef {  "Nested Chooser need not create SynthDef".postln;   ^nil}


//========= SETTING VALUES  =======


loopOn {
		this.loopableSequence.loopOn; ^this}  //   how about infinit

loopOff {
		this.loopableSequence.loopOn; ^this}





//========= CORE PROTOCOL  =======

wrap { arg aChooser;
		this.chooser_ (aChooser);
		  this.loopableSequence.add(aChooser)
	}

play { //this.choose
		 // now we know duration so can culculate loopage

		this.loopableSequence.play  }


basicDuration{
		^this.duration }  //Clearer name to communicate this is duration of sample
	                             // when not changedby external factors


duration { ^ this.chooser.duration}
		// basic duration of chooser  this time!!!- when lanes chosen!!!
		 // unlike sample , dont know length until choose before playing.


	kill {this.chooser.debug( "finish coding") }

// ========== PLAYING ===================


hardPlay{ arg tcDuration;
	     	 var  myClock =   TempoClock(SampleBank.tempo);
		     this.play;
		     myClock.sched( tcDuration,   {this.chooser.stop})
	}

//we dont know choosers duration till we chooseLanes in the nested chooser
// but we dont choose lanes in the nested chooser before parent chosoer says playchosen
// i think we need to do the soft play calculations
// whem paremt says play but
// BETWNN parent choosing and playingChosen
// which is possible  - thoigh i dong kno if parent chooser is like that at present
// it prob hasnt needed  to be like that yet since samples dont change basic duration
softPlay{ arg tcDuration;

	     	 var  myClock =   TempoClock(SampleBank.tempo);
		     (tcDuration <= this.basicDuration).if{this.loopOff;   };
              this.play;

		      (tcDuration > this.basicDuration).if{
			   myClock.sched( tcDuration + (this.duration/100),   {this.synth.set(\loop, 0)})}
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
}

// This protocol offers insight for making choosers nestable
// Does not need  protocol
// where  chooser calls  lane with news of presence or absence of time chooser
// and lane queries sample about  its basicduration
// and then lane calculates hard or soft duration based on its settings
// and tells sample  its smart duration, *which either lane or sample
// must tell tells seqeucne for proper sequencing



