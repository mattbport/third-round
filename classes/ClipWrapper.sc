// Allows things like nysted ( which holds set of pbinds) to be used as samples
//  Nystedt may be able to manipulate temp, voice selection held notes etc
// nb Nysteds can be asked to repeat  n times by detail
// At some point, rightly or wromgly ,  I may have installed a post-cook cutoff for
 //    nyestedts cooked as i nfinite repeaters


ClipWrapper
{       var <> buffer ;             // no-op
		var <>synth;                //  no-op (and could be groups or sets of synths
		var <> name;               // filled by new
	    var <> wavName;         // filled by new
	    var <> atomicRepeats;  // not yet used
	    var <> loop;
	    var <> smartDuration;  // not need to play correctly. Need to report duration
	                                         // correctly when controlled by external environment
	   var <> midiClip ;             // defines relevant common protocol
	   var  tempo;    // does this help -seems o be flopping around


	choose {   ^ this  /* just needed for recursion*/}
	free      {  this.midiClip.free   }
	wrap     { arg clip;    this.midiClip_(clip) }
	duration { ^ this.translatedDuration }
	pause     { 	this.synth.run(false) }
    resume {	this.synth.run(true) }
	xsmartDuration{ ^ this.midiClip.xsmartDuration}


	copy {
		      var temp;  //TEMPORARY MAKESHIFT
		        temp = ClipWrapper.new ;
		        temp.name_(this.name.copy);
		         temp.smartDuration_(this.smartDuration);
		         temp.loop_(this.loop);
		         temp.midiClip_(this.midiClip.copy);
		         temp.tempo_(this.tempo);
	  ^ temp
	}




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
		           {   t.sched( this.translateDuration,   {this.midiClip.free} )  };
		      this.midiClip.play ;
	}


hardPlay{ arg tcDuration;
	     	 var  t =   TempoClock(SampleBank.tempo); // queried by lane & chooser
		     this.hardDuration(tcDuration);  // tough bug to find on new plays
		    // tcDuration.debug("Hardplay  tcDuration");
		     this.play;
		     t.sched( tcDuration,   {this.midiClip.free})   }

softPlay{ arg tcDuration;
	     	 var  t =   TempoClock(SampleBank.tempo);
		     this.softDuration(tcDuration);  // tough bug to find on new plays
		     (tcDuration <= this.translatedDuration).if{this.loopOff };
              this.play;
		      // tcDuration.debug("softplay  tcDuration");                                           // queried§ by lane & chooser
		      (tcDuration > this.translatedDuration).if{
			t.sched( tcDuration + (this.translatedDuration/100),   {this.synth.set(\loop, 0)})}  }

// ============ INTRINSIC DURATION - VERY GENERIC
basicDuration{
		^this.duration }  //Clearer name to communicate this is duration of sample
	                             // when not changedby external factors


translatedDuration	{ // should his be down a level?
		var  t =   SampleBank.tempo;
		//t.debug ("Tempo from smart Bank");
		//this.midiClip.debug("midiClip");
		//this.midiClip.xSmartDuration.debug("xSmartDuration");
		// this.midiClip.xSmartDuration.asSeconds.debug("asSeconds");
		^ this.midiClip.xSmartDuration.asSeconds/t;
	}



//============= SETTING SMART DURATION  =============
// ---- NOT needed to play hard & soft stops right - just for sequencing

hardDuration {arg duration;                                       // misleading name - its a setter
		               this.smartDuration_(duration);
		             ^this.smartDuration }  // queried by lane & chooser


softDuration {arg duration;                                             // misleading name - its a setter
		              this.smartDuration_(  this.basicDuration *    this.neededRepeatsFor(duration));
		           ^  this.smartDuration }  // queried by lane & chooser

neededRepeatsFor{
		 arg softStopDuration;
		 var containedRepeats ;
		  var modulo;
		  containedRepeats=  (softStopDuration/ this.basicDuration).floor;
	      modulo= softStopDuration %  this.basicDuration;
		 (modulo> 0 ).if( { ^ containedRepeats +1}, { ^ containedRepeats })
	}

xSmartDuration{
		^ this.midiClip.xSmartDuration }


}




