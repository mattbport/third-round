TrimSample3 : Sample {

var <> start = 0;
var <> stop = 16;  // NOT END
var <> anchor = 0;     // number between 0 and  grid- playBuf Trigger phase
var <> grid = 16;   // eg 12 &4  quarter notes  VARIABLE
var <> cat = 16;
var <> hardStop = false;
var <> softStop =  false;
var  <> softRepeatNum = 1;



  // Getting values from TRIMTOOL
withTrim {arg aTrimTool;
		this.grid_(aTrimTool.grid);
		this.start_ ( aTrimTool.start)  ;
		this.stop_( aTrimTool.end)    ;     // NOT END
		this.anchor_ (aTrimTool.anchor)  ;
}

//derived values - not used
width {  ^ this.stop - this.start}
widthAsFraction {^ this.width / this.grid}
sampleDurationInSecs { ^ this.sampleDurationInBeats * this.tempo}
sampleDurationInBeats { ^ this.buffer.duration*this.tempo}
trimDurationInBeats {  ^ this.widthAsFraction* this.sampleDurationInBeats}
trimDurationInSecs {^ this.trimDurationInBeats / this.tempo}
anchorFraction { ^ this.anchor/ this.grid}

//conversion
gridToBeats{arg gridPoint;
		^ this.buffer.duration* this.tempo *(gridPoint/this.grid)}
gridToFrameNum{arg gridPoint;
		^ this.buffer.numFrames* (gridPoint/this.grid)}


// ========== PLAYING ===================

softPlay{ //Happens before play
		     arg tcDuration; // sent as beats =  so convert to beats....
	     	 var  softPlayKillClock =   TempoClock(SampleBank.tempo);
			 this.softRepeatNum_(this.neededRepeatsFor(tcDuration));
		     this.softStop_(true);
            this.play;
		}


play {   this.group.isNil.if(  /// if its not nill then place it in server  like this
		          {synth = Synth(this.name)},   // creates & stores a synth instnce
			      {synth = Synth.after(this.group,  this.name) });
           this.setSampleLoopTo(this.loopStatus);
		   synth.set(\outputBus, this.outBus);
	       this.setSampleParameters;
		// this.outBus.debug("In sample");
	        this.name.debug("CREATE synth" +  "node ID" + synth.asNodeID.asString + "with parent" +            this.parentName);
		     }             //starts playing as soon as created

repeatsforTrimUgen{
	   (this.loop).and(this.softStop).if { ^ this.softRepeatNum };
		(this.loop).and((this.softStop).not).if ({^1000 }, {^1}); }

setSampleParameters{
		 super.setSampleParameters;
		( this.loopStatus > 0).if  ( {this.loopStatus.debug("loop is ON")},
			                                   { this.loopStatus.debug("loop is OFF") });

		synth.set ( \bufnum, this.buffer.bufnum,
				          \volume, 0.5 ,    // could use in trim control
				           \loop,  this.loop , // does nothing in UGen
				           \outputBus, this.outBus ,   //also useful
				           \grid, this.grid,
			               \start, this.start,
			               \stop ,this.stop,
			                \anchor, this.anchor,
		                    \loopTimes, this.repeatsforTrimUgen,
			                                                  ) // specific to ugen but could become useful
	}

//=======================================
	// PLAY STUFF ABOVE IS NOT RUN AT WARM UP TIME

createSynthDef {  // run at warmUp time
		//this.debugDump ;
		SynthDef(this.name,
			{                         | bufnum = 0,    // must be numbers - not message replies
				                         volume = 0.5 ,    // could use in trim control
				                         loop = 0 , // does nothing in UGen
				                         outputBus =0 ,   //also useful
				                         grid = 16,
			                             start = 0,
			                             stop = 0,
			                             anchor = 0 ,
			                             loopTimes = 1 | // specific to ugen but could become useful

			var trimTrigger, startEndFrequency, frames, bit, imp, slice, pulseCount;

			frames = BufFrames.ir(bufnum);  // fixed for given buffer
		    slice = frames/grid;    // fixed for given grid & buffer
			startEndFrequency = (grid/(stop-start))*SampleRate.ir/frames; //fixed for given trim
			imp = Impulse.kr( startEndFrequency /*,0, 1, 0.5 */); // pulses one per trimmed loop
			pulseCount =Stepper.kr(imp,0,0,loopTimes+1,1);
			bit =   pulseCount >loopTimes;
				//anchor.poll;
			trimTrigger = Phasor.ar(imp, 1, start*slice, stop*slice, anchor*slice);
		    FreeSelf.kr(bit);


			Out.ar(outputBus,
						BufRd.ar( 2,              // stereo
							           bufnum,
					                   trimTrigger,
					                     0,               // dont loop
					                     2)              // interpolation - which would sound best?
					                  *volume );
			}).add
		}







clockSoftDuration {arg duration;
		^  this.sampleDurationInBeats * this.neededRepeatsFor(duration)}

neededRepeatsFor{  arg softStopDuration;
		 var containedRepeats ;
		  var modulo;
		  containedRepeats=  (softStopDuration/ this.basicDuration).floor;
	      modulo = softStopDuration %  this.basicDuration;
		(modulo> 0 ).if( { this.softRepeatNum_(containedRepeats +1); ^ this.softRepeatNum}, {
			                       this.softRepeatNum_(containedRepeats); ^ this.softRepeatNum })
	}

// ============ INTRINSIC DURATION

basicDuration{
		^this.trimDurationInBeats }  //Clearer name to communicate this is duration of sample
	                             // when not changedby external factors

duration{
		^this.basicDuration
	}


debugDump	 {
	/*
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
*/
	}

}

