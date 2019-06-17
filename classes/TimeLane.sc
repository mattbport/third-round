// SHOULD  HAVE COMMON SUPERCLASS WITH LANE
TimeLane {
	var <>weight;
    var <> playable;
	var <> beats;
	var <>sample;

*new { ^ super.new.init}

*null { ^super.new}           // might stand for validly no lane can be chosen
	                                      // maybe this is silly - just have an empty list
	                                     // and make the distinctions procedurally


cleanUp {
		this.cleanUpSample;
		this.cleanUpRest
	}

cleanUpSample	 {
		this.sample.isNil.not.if { this.sample.cleanUp}
	}

cleanUpRest {
		this.weight_ (nil); // never assigned - just for sommon protocol
		this.playable_(nil); // never assigned - just for sommon protocol
		this.beats_(nil);
	}


kopy{ var me;
		  /// used in loopablesequecne
		// we just want fresh lanes to get us fresh sample insatcnes
		// to avoid sinadvertent ample instacne reuse
		// and wrong node getting killed
		// got to be kopy or will be infinite loop
		// make a copy of me
		//copy my lanes separaelt using bog standard copy
		// so its kinds semi deep copy
		// copies will share the samples
		// butsamp[les havent been played yet.
		// so have no synths stsoted in synth instacne variable
		//each play will create a new node ID and a  create & stire new synth
		//the two samples will save synths spereately
		// and so handle swithcing on and off propely (play & free)
		me = this.copy;
		this.hasSample.if{ me.sample_(this.sample.kopy)};
		// but what if sample is a wrapper???? SO why does KOPY screw up?
		^ me }



init{
	weight = 1;
	playable = false;
    beats = 16;
	sample = nil;}


isNull{
		^ (this.weight ==nil) }

hasInfiniteWeight {
		^ (this.weight ==inf) }

hasZeroWeight{
		^ (this.weight ==0) }

printOn { | aStream |

		this.isNull.if { aStream << "a " << "null"  << this.class.name; ^aStream};

		aStream << " @@ "  /*<< this.class.name  */ << " beats " << this.beats.asString ;
		this.sample.isNil.not.if( {   aStream <<  " " << this.sample});
		this.isPlayable.if( {   aStream <<  " Playable"});
		^aStream}

isPlayable {
		^this.playable == true }

isNotPlayable {
		^this.isPlayable.not}       // what if its playable but nothing to play
	                                           // - then ignore the Playable?

hasSample { ^ (this.sample == nil).not}

isFullyPlayable {
		^ this.isPlayable.and({this.hasSample})}

namedSample{
		arg aSymbol;
		this.sample_(SampleBank.sampleDef(aSymbol));
	}


play{
		this.isNull.if {^nil};
		this.sample.isNil.if({ ^nil}, {this.sample.play})
		// NOT TESTED & PROB not implemented yet
        	}

duration{
	this.isFullyPlayable.if {^ this.sample.duration};
		this.isNull.if {^0}; // dont worry about return value in this case - timeChooser
		                           // will deal with behaviour based on null property
		^ this.beats;
        	}

 }