SampleBank{
	classvar <>samples;
	classvar >tempo;

//=========  ALL CLASS METHODS  =====================

	*purge{ samples = nil; }


	*tempo{
		tempo.isNil.if {^ tempo = 120/60};  // NB Class (not instance)  holds global tempo
		^ tempo                                           // and acts as sample bank
	}

	*populate{
		samples = Dictionary.new( n: 64) ; //  colon syntax is arg keyword
		                                                   //-based on how args declared
		samples.add(\guitar -> Sample.new(\guitar, "gtr8"));
		samples.add(\bass -> Sample.new(\bass, "bass4"));   // itry to get rid of redundant
		                                                                                   //repeated symbol parameter
		samples.add(\vox -> Sample.new(\vox, "vox8"));
		samples.add(\drums -> Sample.new(\drums ,"drums8"));
		samples.add(\marimba -> Sample.new(\marimba ,"marimba4"));
		samples.add(\bvs -> Sample.new(\bvs ,"bv8"));

       this.populateInC ;
	   this.populateClap11;

		samples.keysValuesDo { |eachKey, eachValue|   eachValue.init};
		"Wait before warming up".postln
	}

	*populateInC {
		   samples.add(\0 -> Sample.new(\0 ,"0"));
		  34.do { arg n ;
			       var nu = n + 1;
			       var num =   nu.asSymbol ;
		        	samples.add ( num -> Sample.new(num , num.asString)) };
	                     }

	*populateClap11 {
		   samples.add(\clap11-> Sample.new(\clap11 ,"clap11"));
		    samples.add(\clap12-> Sample.new(\clap12 ,"clap12"));
	                     }


	*warmUp{
		samples.keysValuesDo { |eachKey, eachValue|   eachValue.createSynthDef};
		          }

	*sampleDef{
			arg sampleSymbol;
		samples.isNil.if{  "SampleBank not Loaded - using non-playable proxy sample".postln
			^sampleSymbol};
			^ samples.atFail(sampleSymbol, {"playable not found".postln})
		       }

	*at{ arg aKey;
		var target;
		this.samples.isNil.if{  "SampleBank not Loaded - please load first".postln; ^ aKey};
		this.samples.atFail(aKey, {"Sample not found".postln; ^ aKey});
		target = this.samples.at(aKey);
		^ target.copy  // while debugging terry riley
	}



*secsToBeats{ arg secs;
	^ secs*  this.tempo }
	// samples should be exact numbers of beats & bars


*beatsToSecs{ arg beats;
	^ beats/ this.tempo }
	// samples should be exact numbers of beats & bars



       }
