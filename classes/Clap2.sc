Clap2 {
	//see PClip.sc
	var <> focus;
	var<> stream  ; //better way of killing pbind related samples
	var <> clock;
	var <> name;
	var <> duration;
	var  <> loop = false;
	var thisGroup;  // better than this group - not used here
    var s ;
	var reps = 1;
	var outBusIndex1;
	var outBusIndex2;
	var <> extBus = 0;
	var clapPattern, accPattern,  effDef, bus1, bus2, group1, group2;

*new { ^ super.new.init}

	play {	clock = TempoClock(SampleBank.tempo);
		         stream =  focus.play(clock) } // sneaky

   free { clock.stop;
		stream.stop ; name.debug("Free in clap2") }   //perhaps sjould be called stop?


repeats{ arg aNum; reps = aNum}

hasLoop     {  ^ loop== true    }
hasNoLoop {   ^ loop== false    }
loopOn       {	  loop = true; this.makeLoopOn }
loopOff       {    loop = false; this.makeLoopOff }



makeLoopOn{ reps= 96}  // lets make it 128  rather than inf or 48 for now for safety
	                                    // if say 96 wont be killed?

makeLoopOff { reps = 1}  // seems unkillable if higher

extBusSwap { extBus  = 1}

outBusIndex{
		extBus.debug("extBus");
		(extBus == 0).if { outBusIndex1 = bus1.index;
			                        outBusIndex2 = bus2.index};
	   ( extBus == 1).if { outBusIndex1 = bus2.index;
			                        outBusIndex2 = bus1.index}
	}

init {
	s = Server.default;
	thisGroup = Group.new;

	SynthDef(\clap,{
		arg output=0, vari=0, acc = 1;
		var noiz, amp, filt1, filt2, aEnv, fEnv, tap;

		noiz = WhiteNoise.ar;
		aEnv = EnvGen.kr(Env.perc(0.01, 0.4, 1, -10).delay(Rand(0,0.02)), 1, doneAction:2);
		fEnv = EnvGen.kr(Env.perc(0, 0.2, 1, -8), 1, doneAction:0)*600
		+ ((1100 + Rand(-100,300)) - (vari*100));

		filt1 = BPF.ar(noiz, fEnv, 0.4 - (vari * 0.1) )*0.4;
		filt2 = BPF.ar(noiz, 1000+ Rand(-50,50), 0.2)*1;

		acc = clip2((acc + 0.5) ,1);
		amp = ( (filt1+filt2) ) ;
		amp = RLPF.ar( amp, 10000 , 0.9)*2;
		amp = amp.softclip * aEnv * acc;
		Out.ar(output, amp);
	}).add;

// only this synth needs buses cos its the effect stage!
// suggests pussible way to postfit pan to other synthdefs


	SynthDef(\reverb,{
		arg bus1, bus2;
		var in1, in2, mix;
		in1 = In.ar(bus1);
		in2 = In.ar(bus2);
		mix = FreeVerb2.ar(in1, in2, 0.2, 0.7, 0.98);
		Out.ar(0, mix);
	}).add;


		"Forking".postln;
		{1.wait; this.warmUp}.fork
	}

	warmUp{
	bus1 = Bus.audio(s,1);      bus1.index.debug("warmup bus 1 index");
	bus2 = Bus.audio(s,1);       bus2.index.debug("warmup bus2 index");
	group1 = Group.head(s);
	group2 = Group.after(group1);


  // THIS is where the two prev created busses get assigned to steroo output stage
	effDef = Synth.head(group2, \reverb, [\bus1, bus1.index, \bus2, bus2.index]);

/* Convenience methods for add actions
Convenience method corresponding to the ADD actions of Synth.new :
			Synth.head(aGroup, defName, args)
Create and ass a Synth.  If aGroup is a Group add it at the head of THAT group.
	       in this case create the reverb, which takes 2 args	 - here the audio busses we created - gets joined up below in clap 12 */

		}


	clap12 {
		name=  \clap12;
		duration = 6  ;          // not 12 or 4
        this.outBusIndex;
	    outBusIndex1.debug("  outBusIndex1");
		focus=
		Pbind(
		\instrument, \clap,
		\group, group1,
		\output, outBusIndex1, //connect to the reverb bus 1 STEREO
		\vari, 1,
		\degree,Pseq( [1,1,1,\, 1,1,\, 1,\, 1,1,\], reps) ,
		\acc,Pseq( [1,0,0,0, 0,0,0, 0,0, 0,0,0],reps),
		\dur, 0.5)
	}


		clap11 {
		name=  \clap11;
		duration = 5.5;       // important
		this.outBusIndex;
	    outBusIndex2.debug("  outBusIndex2");
		focus=
		Pbind(
		\instrument, \clap,
		\group, group1,
		\output, outBusIndex2, //connect to the reverb  bus 2 STEREO
		\vari, 1,
		\degree,Pseq( [1,1,1,\, 1,1,\, 1,\, 1,1],1) ,
		\acc,Pseq( [1,0,0,0, 0,0,0, 0,0, 0,0],1),
		\dur, 0.5)
	}







}

// synthdefs and some code  borrowed from github 44kwm20/ClappingMusic.sc
// c = Clap2.new
// c.warmUp
 //   z= c. loopOn.clap12.focus.play     (    TempoClock(2));
//  z.stop
// PBank.populate
// PBank.warmUp
// PBank.clips.at(\clap12).inspect
// c= PBank.make(\clap11, \loopOn)
// c = PBank.make(\clap11, \loopOff)
// c.inspect
// c. midiClip.focus.play(TempoClock(2));
//  c =  Pbind(\freq, Prand([300, 500, 231.2, 399.2], inf), \dur, 0.1).play;
//c.stop
// c.at(\name)
// c.inspect
// c.patternpairs
//    c= Clap2.new
// c.perform(\extBus_,1)
//  c.extBusSwap
//  c.extBus


// c.perform(\extBus_,1).perform(\loopOn).perform(\clap12).play
// c.free
// c.perform(\extBusSwap).perform(\loopOn).perform(\clap12)
// makeExtBusOn