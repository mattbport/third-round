// SHOULD EITHER BE SUBCLASS OR HAVE COMMON SUPERCLASS WITH SAMPLE
DummySample{
		var <>buffer ;
		var <>synth;
		var <> name;
	    var <> wavName;
	    var <>atomicRepeats;
	    var  <>  loop;
	    var <> smartDuration = 0;

//====== QUERYING ===========
hasLoop {	   "DummySample".postln;     ^false}

hasNoLoop {   "DummySample".postln;   ^true}

loopStatus{  "DummySample".postln;   ^ 0}  // translates tp 0 1 for Synth convenience

//========= SETTING VALUES  =======
loopOn {   "DummySample".postln;  ^ nil	}

loopOff {   "DummySample".postln;   ^ nil}

//========= INITIALIZING   =======
warmUp { "DummySample".postln;   ^nil}

createBuffer{  "DummySample".postln;    ^nil}

createSynthDef {  "DummySample".postln;   ^nil}

//========= CORE PROTOCOL  =======
play {  "DummySample".postln;   }

hardPlay{  "DummySample".postln;   }

softPlay{  "DummySample".postln;  }

softDuration {  "DummySample".postln;   ^ 0 }  // misleading name - its a setter

hardDuration {  "DummySample".postln;   ^0 }  // misleading name - its a setter

pause {   "DummySample".postln;   }

resume {  "DummySample".postln;   }

basicDuration{  "DummySample basic duration".postln;  ^0}

duration{    "DummySample duration".postln;  ^ 0 } // CALLED BY SEQUENCE

//=========  Helper methods  =======
neededRepeatsFor{
		 arg softStopDuration;
		  "DummySample".postln;
		 ^nil
	}
}

// This protocol offers insight for making choosers nestable
// Does not need  protocol
// where  chooser calls  lane with news of presence or absence of time chooser
// and lane queries sample about  its basicduration
// and then lane calculates hard or soft duration based on its settings
// and tells sample  its smart duration, *which either lane or sample
// must tell tells seqeucne for proper sequencing



