PBank{
	classvar <>clips;
// goes with pWRapper and pbinds
// Pbind cheesy  mono bass four on floor  example
// All this does is wrap pbinds in clipwrappers - oops no - pwrappers
// cos pbinds are simpler than nysteds

//=========  ALL CLASS METHODS  =====================

*purge{ clips = nil; }
*isPopulated {^this.isNotPopulated.not}
*isNotPopulated {^this.samples.isNil}

*populate{
 clips = Dictionary.new( n: 16); //  colon syntax is arg keyword

 clips.add(\bass1-> PWrapper.new.wrap (CheeseBass.new.bass1 ));
 clips.add(\bass2 -> PWrapper.new.wrap (CheeseBass.new.bass2));
 clips.add(\bass3 -> PWrapper.new.wrap (CheeseBass.new.bass3));
 clips.add(\bass4-> PWrapper.new.wrap (CheeseBass.new.bass4));
clips.add(\fourOnFloor ->
			PWrapper.new.wrap (CheeseBass.new.fourOnFloor ));
clips.add(\fourOnFloor2 ->
			PWrapper.new.wrap (CheeseBass.new.fourOnFloor2 ));

clips.add(\clap12 -> PWrapper.new.wrap (Clap2.new));
clips.add(\clap11 -> PWrapper.new.wrap (Clap2.new));
clips.add(\clap2 -> PWrapper.new.wrap (Clap2.new));
		"Wait 1 or 2 secs for warming up...".postln;

		      }

	*warmUp{// irrelevant - bypassed by make
		clips.put(\clap12,  clips.at(\clap12).midiClip.copy.clap12);
		clips.put(\clap11,  clips.at(\clap11).midiClip.copy.clap11);
		          }

	*make {arg clipSym, loopSym;
		// try adding pan next
		^ PWrapper.new.wrap  (
			clips.at(\clap2).midiClip.copy.perform(loopSym).perform(clipSym))
		}

	*construct {arg clipSym, loopSym, panSym;
		// try adding pan next
		^ PWrapper.new.wrap  (
		                  clips.at(\clap2).midiClip.copy.perform(panSym).
			                            perform(loopSym).perform(clipSym))
		}

   *construct2 {arg clipSym, loopSym, outbus;
		// try adding pan next
		^ PWrapper.new.wrap  (
		                  clips.at(\clap2).midiClip.copy.perform(\extBus_ , outbus).
			                            perform(loopSym).perform(clipSym))
		}


	*namedClip { arg aSymbol;
		^ this.clipDef(aSymbol); }

	*clipDef { arg clipSymbol;
	         clips.isNil.if { this.debug ("PBank not Loaded - please load");
		                          ^clipSymbol };
			^ clips.atFail(clipSymbol, {"clip not found".postln}) }

	*at { arg aKey;
		this.clips.isNil.if{  "PClips not Loaded - please load first".postln; ^ aKey};
		this.clips.atFail(aKey, {"PClips not found".postln; ^ aKey});
		^this.clips.at(aKey)}

*secsToBeats{ arg secs;
	^ secs*  this.tempo }
	// samples should be exact numbers of beats & bars

*beatsToSecs{ arg beats;
	^ beats/ this.tempo }
	// samples should be exact numbers of beats & bars
}

