/// see if can work out hard stop!!!!!!
// juts impulse frequency phase
// just pulse count beats with frequency of tempo

s.boot;
s.freeAll;
s.latency
SampleBank.populate;
SampleBank.warmUp;
SampleBank.tempo_(122/60);



a= SampleBank.at(\drums).buffer;
b= SampleBank.at(\vox).buffer;
c= SampleBank.at(\bass).buffer;
d= SampleBank.at(\bvs).buffer;


	SynthDef("trimtest" ,
		{
			arg   bufnum = 0, loop=0, volume=0.5, outputBus=0 , grid = 16, start = 0,
				    end = 16, anchor = 0, loopTimes =1 ;

			var trimTrigger , dualAnchor, startEndFrequency, frames, bit, imp, pulseCount, slice;
			frames = BufFrames.ir(bufnum);  // fixed for given buffer
			slice= frames/grid;                         // fixed for given grid & buffer
			startEndFrequency = (grid/(end-start))*SampleRate.ir/frames; //fixed for given trim
			imp = Impulse.kr( startEndFrequency /*,0, 1, 0.5 */); // pulses one per trimmed loop
			pulseCount =Stepper.kr(imp,0,0,loopTimes+1,1);
			bit =   pulseCount >loopTimes;
			trimTrigger = Phasor.ar(imp, 1, start*slice, end*slice, anchor*slice);
		    FreeSelf.kr(bit);
		     //	frames.poll (0.5);
		     //imp.poll(imp);
		     //	slice.poll(0.5);
		     //    startEndFrequency.poll(0.5);
			 // trimTrigger.poll;
			 //pulseCount.poll;
			// bit.poll;
			Out.ar(outputBus,
						BufRd.ar( 2,              // stereo
							           bufnum,
					                   trimTrigger,
					                     0,               // dont loop
					                     2)              // interpolation - which would sound best?
					       *volume );
			}).add

y= Synth("trimtest", [ \bufnum, b.bufnum, \start,0, \loopTimes, 2, \stop, 16, \anchor, 15])


// ... or even to change a running synth
y= Synth("trimtest")
y.set(\loopTimes,12) ;
y.set(\start, 2)
y.set(\stop, 6)
y.set(\grid, 12,

/////////////////////////////////////////////////

SynthDef("basictest" , {arg loop=0, volume=0.5, outputBus=0;
			Out.ar(outputBus,
				     PlayBuf.ar(2, b.bufnum,
					                     BufRateScale.kr(b.bufnum),
					                   loop:loop, doneAction:2)*volume )
			                                       // WAS done action 2
			}).add;

y= Synth("basictest")

//////////////////////////////////////////////////////////////////

	works, but dont bother....... do that on client
	//////////////////////////////////

SynthDef("hardTrim" ,
		{
			arg   bufnum = 0, loop=0, volume=0.5, outputBus=0 , grid = 16, start = 0,
				    end = 16, anchor = 0, loopTimes =1 , durationInBeats  = 0, pace = SampleBank.tempo;

			var trimTrigger , dualAnchor, startEndFrequency, frames, bit, repeatTrig, pulseCount, slice,
			      durationInSecs, hardTrig, repeatCount, hardBit;
			// hard
			durationInSecs = durationInBeats/pace;
			pace.poll;
			durationInBeats.poll;
			durationInSecs.poll;
			hardTrig = Impulse.kr( 1/durationInSecs/*,0, 1, 0.5 */); // pulses one per trimmed loop
			hardTrig.poll;
		    repeatCount=Stepper.kr( hardTrig,0,0,2,1,nil);
			hardBit =   repeatCount >1;
			hardBit.poll;
			FreeSelf.kr(hardBit);
			// soft
			frames = BufFrames.ir(bufnum);  // fixed for given buffer
			slice= frames/grid;                         // fixed for given grid & buffer
			startEndFrequency = (grid/(end-start))*SampleRate.ir/frames; //fixed for given trim
			repeatTrig = Impulse.kr( startEndFrequency /*,0, 1, 0.5 */); // pulses one per trimmed loop
			//pulseCount =Stepper.kr(repeatTrig ,0,0,loopTimes+1,1);
			//bit =   pulseCount >loopTimes;
			trimTrigger = Phasor.ar(repeatTrig, 1, start*slice, end*slice, anchor*slice);
		    //FreeSelf.kr(bit);
		     //	frames.poll (0.5);
		     //imp.poll(imp);
		     //	slice.poll(0.5);
		     //    startEndFrequency.poll(0.5);
			 // trimTrigger.poll;
			 //pulseCount.poll;
			// bit.poll;
			Out.ar(outputBus,
						BufRd.ar( 2,              // stereo
							           bufnum,
					                   trimTrigger,
					                     0,               // dont loop
					                     2)              // interpolation - which would sound best?
					       *volume );
			}).add


	y= Synth("hardTrim" , [ \bufnum, b.bufnum, \start,0, \loopTimes, 2, \stop, 16, \anchor, 15, \durationInBeats, 32, \pace, SampleBank.tempo])

