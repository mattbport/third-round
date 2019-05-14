CheeseBass {
	//see PClip.sc
	var <> focus;
	var <> name;
	var <> duration;
	var  <> loop = false;
	var thisGroup;

*new { ^ super.new.init}

	play {	focus.play}


hasLoop     {  ^ loop== true    }
hasNoLoop {   ^ loop== false    }
loopOn       {	  loop = true; this.makeLoopOn }
loopOff       {    loop = false; this.makeLoopOff }

	makeLoopOn{ "leave for now".postln}

	makeLoopOff { "leave for now".postln}
	 // something to do with set

// CheeseBass.new
// CheeseBass.new.bass1
// Done.freeSelf
// Done.inspect

init {
	thisGroup = Group.new;

	SynthDef.new (\bass,
		{ |out, freq = 440, gate = 1, amp = 0.5,
		    slideTime = 0.17, ffreq = 1100, width = 0.15,
            detune = 1.005, preamp = 4|
			var
			    sig,
				 env = Env.adsr(0.01, 0.3, 0.4, 0.1);

			 freq = Lag.kr(freq, slideTime);
             sig = Mix(VarSaw.ar([freq, freq * detune],
					     0, width, preamp)).distort * amp
				* EnvGen.kr(env, gate, doneAction: Done.freeSelf);
               sig = LPF.ar(sig, ffreq);
               Out.ar(out, sig ! 2)
		}
		                   ).add;


// http://depts.washington.edu/dxscdoc/Help/Tutorials/Getting-Started/16-Sequencing-with-Patterns.html

	SynthDef.new (\kik,
			{ |out, preamp = 1, amp = 1|
               var
				  freq = EnvGen.kr(Env([400, 66], [0.08], -3)),
                 sig = SinOsc.ar(freq, 0.5pi, preamp).distort * amp
                          * EnvGen.kr(Env([0, 1, 0.8, 0], [0.01, 0.1, 0.2]),
						   doneAction:

				Done.freeSelf );
                   Out.ar(out, sig ! 2);
               }).add;
       }


	bass1 {
		name= \bass1;
		duration = 4;
		focus=
		 Pbind (
        \instrument, \bass,
        \midinote, 36,
        \dur, Pseq([0.75, 0.25, 0.25, 0.25, 0.5], 1),
        \legato, Pseq([0.9, 0.3, 0.3, 0.3, 0.3], 1),
		\amp, 0.5, \detune, 1.005,
		thisGroup, \addToHead,
		);
	}

bass2 {
		name= \bass2;
		duration = 4;
		focus=
		 Pmono (
		\bass,
        \midinote, Pseq([36, 48, 36], 1),
        \dur, Pseq ( [0.25, 0.25, 0.5], 1),
        \amp, 0.5,
		\detune, 1.005,
		thisGroup, \addToHead,
		)
	}

bass3 {
		name= \bass3;
		duration = 4;
		focus=
	    Pmono(
		\bass,
        \midinote, Pseq([36, 42, 41, 33], 1),
        \dur, Pseq([0.25, 0.25, 0.25, 0.75], 1),
        \amp, 0.5, \detune, 1.005,
		thisGroup, \addToHead,
		)
	}

bass4 {
		name= \bass4;
		duration = 4;
		focus=
		  Pmono(\bass,
        \midinote, Pseq([36, 39, 36, 42], 1),
        \dur, Pseq([0.25, 0.5, 0.25, 0.5], 1),
        \amp, 0.5, \detune, 1.005,
		thisGroup, \addToHead,
		)
	}

	fourOnFloor {
		name= \fourOnFloor;
		duration = 2;
		focus=
		 Pbind(\instrument, \kik, \dur, Pseq([1,1], 1), \preamp, 4.5, \amp, 0.32,
		thisGroup, \addToHead,
		)
	}

fourOnFloor2 {
		name= \fourOnFloor;
		duration = 2;
		focus=
		 Pbind(\instrument, \kik, \delta, 1, \preamp, 4.5, \amp, 0.32,
		thisGroup, \addToHead,
			// delta is lethal - free cant kill it
		)
	}

free {
		thisGroup.free; /* this.debug("Free in cheesy bass") */}

}