// for cheese bass example. should work with pbinds
//problem seemed t obe purely copy in clip wrapper


PWrapper
{       var <> buffer ;             // no-op
		var <>synth;                //  no-op (and could be groups or sets of synths
		var <> name;               // filled by new
	    var <> wavName;         // filled by new
	    var <> atomicRepeats;  // not yet used
	    var <> loop;
	var <> parentName;
	var<> group;
	var <> siblingGroup;
	    var <> smartDuration;  // not need to play correctly. Need to report duration
	                                         // correctly when controlled by external environment
	   var <> midiClip ;             // defines relevant common protocol
	   var  tempo;    // does this help -seems o be flopping around


	choose {   ^ this  /* just needed for recursion*/}
	free      {  this.midiClip.free  ; /* this.debug(" pwrapper frees midiClip") */    }
	wrap     { arg clip;    this.midiClip_(clip) }
	pause     { 	this.synth.run(false) }
    resume {	this.synth.run(true) }
	duration{^  this.midiClip.duration}

	kopy {
		       ^ this .copy
	}

	copy { var pWrap = PWrapper.new;
		pWrap.wrap ( this.midiClip.copy);
		^ pWrap
	}


printOn { | aStream |

		this.midiClip.isNil.not.if( {   aStream <<  " " << this.midiClip.name.asString << " ";
			                                     aStream << this.midiClip.duration << " " });
		^aStream}




// ============= QUERYING AND SETTING =======
hasLoop { ^this.loop == true}
hasNoLoop { ^this.loop == false}
loopOn {	this.loop_ (true) ; this.midiClip.loopOn   }
loopOff { 	this.loop_ (false) ; this.midiClip.loopOff    }
// loopStatus{ this.hasLoop.if( {^1}, {^0} )}
isChooser {^false}
isSymbol { ^ false}


	tempo { ^  this.midiClip.tempo}

	tempo_  {arg num;
		// num.debug("tempo in clipWrapper");
		tempo = num; //"doesn't do much"
		this.midiClip.tempo_ (num) }


// CREATING & INITIALIZING =========
*new { arg  aName, aMidiClip;
			var me = super.new;
		     me.hasLoop; // only way to allow both looping & not looping
		     me.name_(aName.asSymbol);
		     me.wrap( aMidiClip);
			 ^ me.init}

init { }
warmUp {   }
createBuffer{     }
createSynthDef {     }

// ========== PLAYING ===================


	play {     var  t;
		      t =  TempoClock ( SampleBank.tempo );     // queried by lane & chooser
		  this. hasNoLoop.if
		           {   t.sched( this.duration,   {this.midiClip.free} )  };
		      this.midiClip.play ;
	}


hardPlay{ arg tcDuration;
	     	 var  t =   TempoClock(SampleBank.tempo); // queried by lane & chooser
		//this.debug("hard play in PWrapper");
		     this.hardDuration(tcDuration);  // tough bug to find on new plays
		   //  tcDuration.debug("Hardplay  tcDuration");
		     this.play;
		t.sched( tcDuration,   {this.midiClip.free;
			                               //this.midiClip.name.debug("Pwrapper freeing clip") ;
			                               //tcDuration.debug("after this time");
			                                nil // !!!!!!!!!!


	/*this.debug("Pwrapper freeing clip") */})   }

softPlay{ arg tcDuration;
		     //assume pbind is on in built repeat
	     	 var  t =   TempoClock(SampleBank.tempo); // DANGEROUS?
		     this.softDuration(tcDuration);  // tough bug to find on new plays
		         // really means go & caclulate soft duration based on this & pbind duration
		     (tcDuration <= this.duration).if{this.loopOff };
		       // implemented eg in clap2 - and not yet played, so fine!!!!
		     (tcDuration > this.duration).if  {this.repeats(
			{ this.neededRepeatsFor(tcDuration)}) };
              this.play;
		     }

	             // SOFT PLAY NO BLODDY GOOD FOR PWRAPPERS IN GENERAL


// ============ INTRINSIC DURATION - VERY GENERIC
basicDuration{
		^this.duration }  //Clearer name to communicate this is duration of sample
	                             // when not changedby external factors




//============= SETTING SMART DURATION  =============
// ---- NOT needed to play hard & soft stops right - just for sequencing

hardDuration {arg duration;                                       // misleading name - its a setter
		               this.smartDuration_(duration);
		             ^this.smartDuration }  // queried by lane & chooser


softDuration {arg duration;               // misleading name - really means
		// caclulate soft duration based on tcduration argument  & pbindduration
	 this.smartDuration_(  this.basicDuration *    this.neededRepeatsFor(duration));
		           ^  this.smartDuration }  // queried by lane & chooser

neededRepeatsFor{
		 arg softStopDuration; // here meaning  tcDuration
		 var containedRepeats ;
		  var modulo;
		  containedRepeats=  (softStopDuration/ this.basicDuration).floor;
	      modulo= softStopDuration %  this.basicDuration;
		 (modulo> 0 ).if( { ^ containedRepeats +1}, { ^ containedRepeats })
	}

xSmartDuration{
		^ this.midiClip.smartDuration } // avoid this  - who is asking?


}




