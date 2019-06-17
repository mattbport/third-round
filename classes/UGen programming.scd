UGen prrogramming

UGen.browse /// !!!!!!
SynthDescLib.global.browse

// ------ SyntDef components and format ------
// The basic format of a SynthDef is:
SynthDef(\name, {arg optional; Out.ar(bus_number, UGen_graph)});

//=======================
// =======================

s.boot;
s.freeAll;
SampleBank.populate;
SampleBank.warmUp;
SampleBank.tempo_(122/60);



a= SampleBank.at(\drums).buffer;
b= SampleBank.at(\vox).buffer;
c= SampleBank.at(\bass).buffer;
d= SampleBank.at(\bvs).buffer;

	SynthDef("basictest" , {arg loop=0, volume=0.5, outputBus=0;
			Out.ar(outputBus,
				     PlayBuf.ar(2, b.bufnum,
					                     BufRateScale.kr(b.bufnum),
					                   loop:loop, doneAction:2)*volume )
			                                       // WAS done action 2
			}).add;


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
			bit.poll;
			Out.ar(outputBus,
						BufRd.ar( 2,              // stereo
							           bufnum,
					                   trimTrigger,
					                     0,               // dont loop
					                     2)              // interpolation - which would sound best?
					*volume );
			}).add

y= Synth("trimtest", [ \bufnum, b.bufnum, \start,0, \times, 2, \stop, 16, \anchor, 15])


// ... or even to change a running synth
y= Synth("trimtest")
y.set(\loopTimes,12) ;
y.set(\start, 2)
y.set(\stop, 6)
y.set(\grid, 12, )






If you can't branch, what good is a comparison in the server?
Comparisons have a lot of uses, actually.


	Choosing one of two signals: This is the closest we can get to if-then-else in the server. Both then and else must be running continuously. That's a requirement of how the server works: the number and arrangement of unit generators within a single Synth cannot change. Instead, you can choose which of those signals makes it downstream. One will be used and the other ignored.
Since true is 1 and false is 0, you can use a conditional to index into an array using Select.
 http://supercollider.sourceforge.net/wiki/index.php/If_statements_in_a_SynthDef Page 2 of 4
If statements in a SynthDef - SuperCollider wiki 11/06/2019, 16)12
Select.kr(aKrSignal > anotherKrSignal, [false_signal, true_signal]);


Generating triggers: A trigger occurs whenever a signal is <= 0, and then becomes > 0. Extending this to comparisons, it means that a trigger occurs when a comparison is false for a while, and then becomes true. Comparing a signal to a threshold may then be used anywhere that a trigger is valid. For a simple example, take the case of sending a message to the language when the microphone input's amplitude crosses a threshold.

var mic = In.ar(8, 1),
        amplitude = Amplitude.kr(mic);
SendTrig.kr(amplitude > 0.2, 0, amplitude);



Passing or suppressing triggers: You might need to generate triggers continuously, but permit the triggers to take effect only when a condition is met. Multiplication handles this nicely: condition * trigger. Since the condition evaluates as 0 when false, the trigger will be replaced by 0 and nothing happens, as desired.
For a simple case, let's refine the mic amplitude example by suppressing triggers that occur within 1/4 second after the previous.

var mic = In.ar(8, 1),
        amplitude = Amplitude.kr(mic),
        trig = amplitude > 0.2,
        timer = Timer.kr(trig),  // how long since the last trigger?
        filteredTrig = (timer > 0.25) * trig;
SendTrig.kr(filteredTrig, 0, amplitude);