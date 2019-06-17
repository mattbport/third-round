TrimSample2 : Sample {

// EVIL _ CAReFUL

var <> trimStartGridPoint = 0;
var <> trimEndGridPoint = 16;
var <> anchorGridPoint = 0;     // number between 0 and  grid- playBuf Trigger phase
var <> grid = 16;                       // eg 12 &4  quarter notes  VARIABLE
var  <> printOutCount = 0;      // not really working - different instance?

// synonyms
trimStart { ^ this. trimStartGridPoint}
trimEnd { ^ this. trimEndGridPoint}
anchorPoint { ^ this. anchorGridPoint}
anchorFraction { ^ this. anchorGridFraction}
tempo {^ SampleBank.tempo}

   // Getting values from TRIMTOOL
withTrim {arg aTrimTool;
		this.grid_(aTrimTool.grid);
		this.trimStartGridPoint_ ( aTrimTool.start)  ;
		this.trimEndGridPoint_( aTrimTool.end)    ;
		this.anchorGridPoint_   (aTrimTool.anchor)  ;
        this.debugDump
}

//derived values
trimWidthInGridPoints {  ^ this.trimEnd - this.trimStart}              //OK
trimWidthAsFraction {^ this.trimWidthInGridPoints / this.grid}		// true
sampleDurationInSecs { ^ this.sampleDurationInBeats * this.tempo}
sampleDurationInBeats {  ^ this.buffer.duration*this.tempo}                              // OK
trimDurationInBeats {  ^ this.trimWidthAsFraction* this.sampleDurationInBeats}
trimDurationInSecs {^ this.trimDurationInBeats /this.tempo}
anchorGridFraction { ^ this.anchorPoint / this.grid}
framesInBuffer{^ this.buffer.numFrames}
// fraction between 0 and 1 -  implemented as playBuf Trigger phase

//conversion
gridToBeats{arg gridPoint;
		^ this.buffer.duration* this.tempo *(gridPoint/this.grid)}
gridToFrameNum{arg gridPoint;
		^ this.buffer.numFrames* (gridPoint/this.grid)}


// does the trim but not the soft stop - done by hand
createSynthDef {
			SynthDef(this.name ,
		{
			arg loop=0, volume=0.5, outputBus=0 , grid = this.grid, start = this.trimStart,
				    end = this.trimEnd, anchor = this.anchorPoint, loopTimes =10000 ;
			var trimTrigger = Phasor.ar(
				                              	Impulse.kr(this.framesInBuffer*((end-start)/grid)*SampleRate.ir);
			Out.ar(outputBus,
						BufRd.ar( this.buffer.numChannels,              // stereo
							           this.buffer.bufnum,
					                   trimTrigger,
					                   1,
			                            end* (this.framesInBuffer/grid),
			                            start*(this.framesInBuffer/grid),
			                            this.framesInBuffer*Select.kr ( PulseCount(trimTrigger) <= loopTimes,
							                                                                              [ anchor, grid]) ),
					                     0,               // dont loop
					                     2)              // interpolation - which would sound best?
					*volume );
			}).add;
		}





softPlay{ arg tcDuration; // sent as beats =  so convert to beats....
	     	 var  softPlayKillClock =   TempoClock(SampleBank.tempo);

		     (tcDuration <= this.basicDuration).if{this.loopOff;  this.setSampleLoopTo(0) };
              this.play;
			  softPlayKillClock.sched( this.softDuration(tcDuration),
				{ this.setSampleLoopTo(0); // needed for no trim case
				  this.synth.free; // frees the stored instance
			      this.name.debug(" SOFT STOP client-side — node ID" + synth.asNodeID.asString);
				   tcDuration.debug(                           "    tcDuration");
				   this.sampleDurationInBeats.debug( "    sample duration");
				  this.clockSoftDuration(tcDuration).debug("    soft stop duration " );
				   softPlayKillClock.stop;})
		}


clockSoftDuration {arg duration;

	      synth.set(\loopTimes, this.neededRepeatsFor(duration));
		^  this.sampleDurationInBeats * this.neededRepeatsFor(duration)}

neededRepeatsFor{  arg softStopDuration;
		 var containedRepeats ;
		  var modulo;
		  containedRepeats=  (softStopDuration/ this.basicDuration).floor;
	      modulo = softStopDuration %  this.basicDuration;
		 (modulo> 0 ).if( { ^ containedRepeats +1}, { ^ containedRepeats })
	}



// ============ INTRINSIC DURATION

basicDuration{
		^this.trimDurationInBeats }  //Clearer name to communicate this is duration of sample
	                             // when not changedby external factors

duration{
		^this.basicDuration
	}



debugDump	 {
this.sampleDurationInBeats.debug("sampleDurationInBeats");

//this. playHead.debug("playhead");
this. buffer.debug("buffer");
this. frequency.debug("frequency");
this. buffer.numFrames.debug("buffer.numFrames");
//this. playBufStartPos.debug("playBufStartPos");
// this. triggerPhase.debug("triggerPhase");
this. trimDurationInBeats.debug("trimDuration in beats");
this. trimDurationInSecs.debug("trimDuration in secs");
this.debug ("in sample");

	}






}

