TrimSample4 : Sample {



var <> start = 0;
var <> stop = 16;  // NOT END
var <> anchor = 0;     // number between 0 and  grid- playBuf Trigger phase
var <> grid = 16;   // eg 12 &4  quarter notes  VARIABLE
var <> cat = 16;
var <> hardStop = false;
var <> softStop =  false;
var <> noActiveTimeChooser = false;
var <> softRepeatNum = 1;
var <> hardStopDuration = 1;
var <> singleLoop = false;



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
	//=======================================
	// PLAY STUFF IS NEVER RUN AT WARM UP TIME
	//=======================================


hardPlay{ arg tcDuration;  //Happens before play
		this.debug("hard play route");
		this.hardDuration(tcDuration); //  just records smart duration
		this.hardStopDuration_(tcDuration); // record locally for server-side hatd stop
		this.hardStop_(true); // to co-ordinate client-side parameter setting
		this.softStop_(false);
		this.play; // creates a synth instance

	}

softPlay{ //Happens before play
		    arg tcDuration; // sent as beats =  so convert to beats....
             this.debug("soft play route");
		     this.softDuration(tcDuration);  // just records smart duration
			 this.softRepeatNum_(this.neededRepeatsFor(tcDuration));
		     this.softStop_(true); // to co-ordinate  client-side parameter setting
		     this.hardStop_(false);
             this.play; // creates a synth instance
		}


play {   this.group.isNil.if(  /// if its not nill then place it in server  like this
		          {synth = Synth(this.name)},   // creates & stores a synth instnce
			      {synth = Synth.after(this.group,  this.name) });
           this.setSampleLoopTo(this.loopStatus);
		    synth.set(\outputBus, this.outBus);
	       this.liveUpdateSynthParameters;
	       this.name.debug("CREATE synth" +  "node ID" + synth.asNodeID.asString + "with parent" +            this.parentName);
		     }             //starts playing as soon as created

repeatsforTrimUgen{
		(this.hasNoLoop.and(this.noActiveTimeChooser )).if{   this.singleLoop_(true)     };
	   (this.loop).and(this.softStop).if { ^ this.softRepeatNum };
		(this.loop).and((this.softStop).not).if ({^1000 }, {^1}); }

liveUpdateSynthParameters{
		 super.setSampleParameters;
		( this.loopStatus > 0).if  ( {this.loopStatus.debug("loop is ON")},
			                                   { this.loopStatus.debug("loop is OFF") });
	     this.repeatsforTrimUgen;
		//this.debugDump;
		this.synth.set ( \bufnum, this.buffer.bufnum,
				          \volume, 0.5 ,    // could use in trim control
				           \loop,  this.loop , // does nothing in UGen
				           \outputBus, this.outBus ,   //also useful
				           \grid, this.grid,
			               \start, this.start,
			               \stop ,this.stop,
			                \anchor, this.anchor,
			                \tempo, SampleBank.tempo,  // NOT REALLY GOOD
			                \hardStopBeats, this.hardStopDuration,
			                \hardStop, this.hardStop.asInteger,
			                \softStop, this.softStop.asInteger,
			                \singleLoop, this.singleLoop.asInteger,
		                    \loopTimes, this.repeatsforTrimUgen,
			                                                  ) // specific to ugen but could become useful
	}

//=======================================
	// PLAY STUFF ABOVE IS NEVER RUN AT WARM UP TIME
	//=======================================

createSynthDef {  // run at warmup time - so values must exist then - args may be more strict - just literal nums?
		//this.debugDump ;
		SampleBank.tempo.ifNil {"%%%%%%%%%%%%%%%%%%%%%%%%%%%%%".postln;
			                                   "Warning — SampleBank tempo not set".postln};
		SynthDef(this.name,
			{                         | bufnum = 0,    // must be known at compile time - maybe just numbers
				                         volume = 0.5 ,    // could use in trim control
				                         loop = 0 , // does nothing in UGen
				                         outputBus =0 ,   //also useful
				                         grid = 16,
			                             start = 0,
			                             stop = 0,
			                             anchor = 0 ,
				                         hardStop =0,
				                         softStop = 0,
				                         tempo = 2.0333,
				                         hardStopBeats = 0,
				                         singleLoop = 0,
			                             loopTimes = 1 | // specific to ugen but could become useful

			var trimTrigger, startEndFrequency, frames, softStopBit, hardStopBit,
				   trimImp, beatImp,slice, trimCount,hard,  beatCount, singleLoopCount, singleLoopBit;
            // can assemble using evaluable messages at compiletime, b
			// but nice for debugging to have copious poll points
			frames = BufFrames.ir(bufnum);  // fixed for given buffer
		    slice = frames/grid;    // fixed for given grid & buffer
			startEndFrequency = (grid/(stop-start))*SampleRate.ir/frames; //fixed for given trim
			trimImp = Impulse.kr( startEndFrequency /*,0, 1, 0.5 */); // pulses one per trimmed loop
			beatImp = Impulse.kr(tempo);
			trimCount =Stepper.kr(trimImp,0,0,loopTimes+1,1);
			singleLoopCount =Stepper.kr(trimImp,0,0,2,1);
			beatCount =Stepper.kr(beatImp,0,0, 1000+ hardStopBeats+1,1);
			softStopBit =   trimCount >loopTimes;
			singleLoopBit = singleLoopCount > 2;
			hardStopBit =  beatCount > hardStopBeats;
				//anchor.poll;
			trimTrigger = Phasor.ar(trimImp, 1, start*slice, stop*slice, anchor*slice);
			// default BufRd is indefinite repeat
		    FreeSelf.kr(softStopBit*softStop);  //zero, so wont happen if softStop is off
			FreeSelf.kr(hardStopBit*hardStop);  //zero, so wont happne  if hardStop is off
			FreeSelf.kr(singleLoopBit*singleLoop);
				 //zero, so wont happne  if single loop (no actibe time chooser + no loop is off

		    //  SERVER-SIDE DEBUGGING - careful different sampling rates
			//frames.poll;
			//hardStopBeats.poll;
			//softStop.poll;
			//hardStop.poll;
				//slice.poll;
				//startEndFrequency.poll;
				//trimCount .poll;
				//beatCount.poll;
			//softStopBit.poll;
			//hardStopBit.poll;
		    //start.poll;
			//stop.poll;
			//anchor.poll;
			//trimTrigger.poll;
            // ======================================

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

basicDuration{^this.trimDurationInBeats }  //Clearer name to communicate this is duration of sample
	                                                           // when not changedby external factors
duration{  ^this.basicDuration	}

debugDump	 {
		"^^^^^^^^^^^^^^^^^^^^^^^^^".postln;
		this.hardStopDuration.debug("hard stop duration");
		this.softStop.debug("soft stop");
        this.hardStop.debug("hard stop");
		/*
		    frames = BufFrames.ir(bufnum);  // fixed for given buffer
		    slice = frames/grid;    // fixed for given grid & buffer
			startEndFrequency = (grid/(stop-start))*SampleRate.ir/frames; //fixed for given trim
			trimImp = Impulse.kr( startEndFrequency /*,0, 1, 0.5 */); // pulses one per trimmed loop
			beatImp = Impulse.kr(tempo);
			trimCount =Stepper.kr(trimImp,0,0,loopTimes+1,1);
			beatCount =Stepper.kr(beatImp,0,0, hardStopBeats+1,1);
			softStopBit =   trimCount >loopTimes;
			hardStopBit =  beatCount > hardStopBeats;
				//anchor.poll;
			trimTrigger = Phasor.ar(trimImp, 1, start*slice, stop*slice, anchor*slice);
		    FreeSelf.kr(softStopBit*softStop);  //zero if softStop is off
			FreeSelf.kr(hardStopBit*hardStop);
  */
}

}

