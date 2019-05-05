ClipBank{
	classvar <>clips;
// usedto be called midiBank,  for holding wrapped nysteds
// goes with clipWrappers & nystedts   what used to be midi clips - dont really exist now exepct PCilp
//	poential superclass of  nysteds and wrappe pbings
// NB In C and Clapping sample already present in draft version

//=========  ALL CLASS METHODS  =====================

	*purge{ clips = nil; }
	*isPopulated {^this.isNotPopulated.not}
	*isNotPopulated {^this.samples.isNil}

	*populate{
		 // should factor out repetition
	    clips = Dictionary.new( n: 16); //  colon syntax is arg keyword
		clips.add(\all -> ClipWrapper.new.wrap (Nystedt.new.all ));
		clips.add(\kom1 -> ClipWrapper.new.wrap (Nystedt.new.kom1( 24 ))  );
		clips.add(\kom2 -> ClipWrapper.new.wrap (Nystedt.new.kom2 (24) ));
		clips.add(\kom3-> ClipWrapper.new.wrap (Nystedt.new.kom3( 24 ))  );
		clips.add(\kom2A -> ClipWrapper.new.wrap (Nystedt.new.kom2A(8 ) ) );
		clips.add(\kom2B -> ClipWrapper.new.wrap (Nystedt.new.kom2B(24 )) );

		    }

	*make {arg clip, voice,  tempo;
		// Try adding hold next
		^ ClipWrapper.new.wrap  (
			Nystedt.new.solo(voice).perform(clip)).tempo_(tempo)   }

	*construct {arg clip, voice, tempo, hold;
		// Try adding hold next
		^ ClipWrapper.new.wrap  (
			              Nystedt.new.solo(voice).perform(clip, hold).tempo_(tempo)  )  }


   //   MIDIBank.make(\all, \s, 2 ).play

	//   MIDIBank.construct(\all, \s,  2, 24 ).play

	//  MIDIBank.construct(\kom1, \all, 4, 8) .play

	*namedClip { arg aSymbol;
		^ this.clipDef(aSymbol); }

	*clipDef { arg clipSymbol;
	         clips.isNil.if { this.debug ("MIDIBank not Loaded - please load");
		                          ^clipSymbol };
			^ clips.atFail(clipSymbol, {"clip not found".postln}) }

	*at { arg aKey;
		this.clips.isNil.if{  "MidiClips not Loaded - please load first".postln; ^ aKey};
		this.clips.atFail(aKey, {"MidiClips not found".postln; ^ aKey});
		^this.clips.at(aKey)}

*secsToBeats{ arg secs;
	^ secs*  this.tempo }
	// samples should be exact numbers of beats & bars

*beatsToSecs{ arg beats;
	^ beats/ this.tempo }
	// samples should be exact numbers of beats & bars
}

/*
Nystedt2.new.all
Nystedt2.new.kom1(4)
Nystedt2.new.kom3(4 )

n= Nystedt2.new;
n.rate(1);
n.kom1(4);
*/