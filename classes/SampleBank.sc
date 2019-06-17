SampleBank{
	classvar <>samples;
	classvar >tempo;
	classvar <> sampleClass;
	classvar <> trimIsEnabled = true;

//=========  ALL CLASS METHODS  =====================

// sample bank  populate stores sample instaces with a  buffer containging the wav
	// warm up runs createsynthdef to run synth def to add the synthdef to server
	// doesnt evey play redo that anyway? - no - just creaes synth
	// but trim does reun create synthdef.
	// why does trim work on second go?


	*purge{ samples = nil; }

	*enableTrim{ trimIsEnabled = true;
		this.populate;

	}

	*disableTrim{
		   trimIsEnabled = false;
			this.populate;
	}



	*tempo{
		tempo.isNil.if {^ tempo = 122/60};  // NB Class (not instance)  holds global tempo
		^ tempo                                           // and acts as sample bank
	}

	*populate{
		//trimIsEnabled.if({sampleClass = TrimSample}, {sampleClass = Sample});
		// sampleClass = TrimSample;
		"///////////////////////////////////".postln;
		this.debug("Creating sample instances and loading into buffers from disk");
		   trimIsEnabled.if(
		{sampleClass = TrimSample4;
		this.sampleClass.debug("Trim is enabled — using")},

	    {sampleClass = Sample;
		this.sampleClass.debug("Trim is disabled — using")});
		samples = Dictionary.new( n: 64) ; //  colon syntax is arg keyword
		                                                   //-based on how args declared
		samples.add(\guitar -> sampleClass.new(\guitar, "gtr8"));
		samples.add(\bass -> sampleClass.new(\bass, "bass4"));   // itry to get rid of redundant
		                                                                                   //repeated symbol parameter

		//this.halt;
		"Loading bass2 sample".postln;
		samples.add(\bass2 -> sampleClass.new(\bass2, "bass8"));   // itry to get rid of redundant
		"Loaded bass2 sample".postln;
		samples.add(\vox -> sampleClass.new(\vox, "vox8"));
		samples.add(\drums -> sampleClass.new(\drums ,"drums8"));
		samples.add(\marimba -> sampleClass.new(\marimba ,"marimba4"));
		samples.add(\synthbass -> sampleClass.new(\synthbass,"synthbass4"));
		samples.add(\bvs -> sampleClass.new(\bvs ,"bv8"));
		samples.add(\strings1 -> sampleClass.new(\strings1 ,"strings1"));
		samples.add(\strings2 -> sampleClass.new(\strings2 ,"strings2"));
		samples.add(\strings3 -> sampleClass.new(\strings3 ,"strings3"));
		samples.add(\pad1 -> sampleClass.new(\pad1 ,"pad1"));
		samples.add(\pad2 -> sampleClass.new(\pad2 ,"pad2"));
		samples.add(\lefthand -> sampleClass.new(\lefthand ,"lefthand"));
		samples.add(\righthand -> sampleClass.new(\righthand ,"righthand"));
		samples.add(\lefthand2 -> sampleClass.new(\lefthand2 ,"lefthand2"));
		samples.add(\righthand2 -> sampleClass.new(\righthand2 ,"righthand2"));
		samples.add(\violin1 -> sampleClass.new(\violin1 ,"violin1"));
		samples.add(\violin2 -> sampleClass.new(\violin2 ,"violin2"));
		samples.add(\violinsilence -> sampleClass.new(\violinsilence ,"violinsilence"));


       this.populateInC ;
	   this.populateClap11;

		samples.keysValuesDo { |eachKey, eachValue|   eachValue.init};
		"Wait before warming up".postln
	}

	*populateInC {
		   samples.add(\0 -> sampleClass.new(\0 ,"0"));
		  34.do { arg n ;
			       var nu = n + 1;
			       var num =   nu.asSymbol ;
		        	samples.add ( num -> sampleClass.new(num , num.asString)) };
	                     }

	*populateClap11 {
		   samples.add(\clap11-> sampleClass.new(\clap11 ,"clap11"));
		    samples.add(\clap12-> sampleClass.new(\clap12 ,"clap12"));
	                     }


	*warmUp{
		this.debug("Creating synthDefs and sending to server - no synth creation yet");
		samples.keysValuesDo { |eachKey, eachValue|   eachValue.createSynthDef};
		          }

	*sampleDef{
		arg sampleSymbol;
		this.debug("Compiling synthDefs and storing on server");
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

