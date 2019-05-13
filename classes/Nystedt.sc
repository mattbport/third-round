Nystedt : PClip {
var <> playSet, <> pitchDict, <> sections, <> sect, <> durDict;
var <> sClock, /* why not just rates? */  <> allClock,  aClock,  tClock, bClock;
var <> sop,  <>  alto, <>  tenor,  <>  bass, <>  ensemble, <>  lookup, <> soloState ;
var <> group,  <> loop ; //  due to duplicate pbinds - group free is ggod for silence
var <> duration ;  // needs finite repeats for soft stops
var       tempo;
var       repeats;

	// NB - messages things like kom1 just return  set of pbinds.

*new{
		^ super.new.init}

repeats{ // repeats.debug("repeats");
		     ^ repeats }

repeats_{ arg	 rep;
		       repeats = rep}

rate { arg secs;
		// 4 6 8 10 12
		^ allClock = TempoClock.new(480/(secs*60))   }

tempo{  ^ tempo}

tempo_{ arg num;
		tempo = num;
		//tempo.debug("tempo in nystedt2");
		sClock = TempoClock.new(num);
		aClock = TempoClock.new(num);
		tClock = TempoClock.new(num);
		bClock = TempoClock.new(num);
	    allClock = TempoClock.new(num);
	}

	/*
regenerateTempo {
		// this is nuts - how can we work around this more sanely
		this.tempo.isNumber.if { this.tempo_(this.tempo) }
	}
 */


init{
		sections= Array.fill(14, {Dictionary.new});
		playSet = Set.new;
		ensemble = Set.new;
		lookup = Dictionary.new;
		soloState = \all;
		group = Group.new;
		repeats = 1;
		this.tempo_(240/60);
		this.rate (1);
		this.loadData (8) ; // hold for 8
		sect = this.sections [7];

sop= Pbind( \instrument, "default", \midinote, Pseq( sect.at(\s)),
			                   \dur, Pseq(sect.at(\sd)),  \group ,this.group   );
alto = Pbind( \instrument, "default",  \midinote, Pseq( sect.at(\a) ),
			                   \dur, Pseq(sect.at(\ad)) ,\group, this.group  );
tenor = Pbind( \instrument, "default",  \midinote, Pseq( sect.at(\t) ),
			                  \dur, Pseq( sect.at(\td)) ,\group, this.group  );
bass = Pbind( \instrument, "default",  \midinote, Pseq( sect.at(\b ) ),
			                  \dur, Pseq( sect.at(\bd)) ,\group, this.group  );

		lookup.put(\s, sop);
		lookup.put(\a, alto);
		lookup.put(\t, tenor);
		lookup.put(\b, bass);

		ensemble = Set.new;
		ensemble.add(sop);
		ensemble.add(alto);
		ensemble.add(tenor);
		ensemble.add(bass);

		playSet = ensemble.copy;
		this.all
	}

hasLoop     { ^ this.loop == true     }
hasNoLoop {  ^ this.loop == false   }
loopOn       {	this.loop_ (true)   ;  this.repeats_ (20)  }
loopOff       { this.loop_ (false)  ;  this.repeats_ (1)   }

first {	this.section(1)   ; ^this   }
second {this.section(3)   ; ^this }
third {	this.section(5)  ; ^this  }
all {	this.section(7) ; ^this     }

kom1 { arg hold;
		        this.kommTodd(hold);
		        this.section(1);
	            ^this }

kom2 { arg hold;
		        this.kommRuh(hold);
		        this.section(3) ;
	            ^this }

kom2A { arg hold;
		        this.kommRuhA(hold);
		         this.section(8 ) ;
	            ^this}

kom2B { arg hold;
		        this.kommRuhB(hold);
		         this.section(10)  ;
	            ^this}

kom3 { arg hold;
		        this.kommFriede(hold);
		        this.section(5);
	            ^this}



solo { arg aSym;
		(aSym ==\all).if {^this.allVoices};
		this.playSet.removeAll (this.playSet.copy);
		this.playSet.add ( this.lookup.at(aSym) );
		this.soloState = aSym;
		^ this
	}

allVoices {
		this.playSet.removeAll(this.playSet.copy);
		this.playSet = ensemble;
		this.soloState = \all;
		^ this
	}

play {
		// this.regenerateTempo; //crazy
		//this.playSet.asArray.debug("play");
		//repeats.debug("repeats");
		//allClock.debug("allCLock");

		Ppar( this.playSet.asArray, repeats).play(allClock)  }


free {  this.group.free   }


loadData{ arg hold;
		this.kommTodd( hold);
		this.kommRuh (hold)	;
		this.kommFriede (hold);
		this.immortal(hold);
		this.kommRuhA(hold);
		this.kommRuhB(hold);
	}

section { arg i;
		sect= this.sections[i];
		// sect.debug("Sect");
		playSet.removeAll(playSet.copy);

		sop= Pbind( \instrument, "default", \midinote, Pseq( sect.at(\s)),
			                   \dur, Pseq(sect.at(\sd) ),  \group ,this.group   );
		alto = Pbind( \instrument, "default",  \midinote, Pseq( sect.at(\a)),
			                   \dur, Pseq(sect.at(\ad) ) ,\group, this.group  );
		tenor = Pbind( \instrument, "default",  \midinote, Pseq( sect.at(\t)),
			                  \dur, Pseq( sect.at(\td)) ,\group, this.group  );
		bass = Pbind( \instrument, "default",  \midinote, Pseq( sect.at(\b )),
			                  \dur, Pseq( sect.at(\bd)) ,\group, this.group  );


		lookup.put(\s, sop);
		lookup.put(\a, alto);
		lookup.put(\t, tenor);
		lookup.put(\b, bass);

		ensemble = Set.new;
		ensemble.add(sop);
		ensemble.add(alto);
		ensemble.add(tenor);
		ensemble.add(bass);

		playSet = ensemble.copy;
		this.solo( this.soloState); // got to do last otherwise might lose other voices when switch solo state



		this.calculateDuration(sect);

		^ playSet // so can send them a stop
	}


xSmartDuration { var me;
		me = XsmartDuration.new;
		me.tempo_(this.tempo);
		me.beats_(this.duration);
		^ me;

	}


calculateDuration	{arg sect;
  var durationArraySums = Set.new;
  var durationList;
		[ \sd, \ad, \td,  \db ].do { arg eachSym;
			                                var sum;
			                                var durationArray;
			                                 //sect.debug("Sect");
			                                 durationArray = sect.atFail(eachSym, { [ ]} );
			                                durationList = durationArray.asList;
			                                durationList.removeAllSuchThat { arg each, i;  each.isNumber.not   };
			                                sum = this.sumDurationArray ( durationList);
                                            durationArraySums.add(sum) };
                   this.duration_(durationArraySums.maxItem {arg item, i; item} ) }



sumDurationArray{ arg anArray;
                               ^ anArray.inject( 0,  { arg accum, eachNum;
                                                               accum+ eachNum}) }


appendHoldFor { arg i, hold, adjust;
		[ \sd, \ad, \td,  \bd].do { arg eachSym, index;
			// NB - pitch is never appended - just final pitch DURATION chages
       sections[i]	.put (eachSym, [hold*adjust])};
		   // the held chord of duration hold x adjust

		[ \sd, \ad, \td,  \bd]. do { arg eachVoice, index;  // do the concatenate
			sections[i].put( eachVoice  ,
				(sections[i-1].at(eachVoice) ++ sections[i].at(eachVoice) ))};
     	}

kommTodd { arg hold;
var i =0;
var adjust=1;

sections[i].put( \sd  ,  [ 4,2,1,1] );
sections[i].put( \ad  ,  [ 4,2,2]   );
sections[i].put( \td  ,  [ 4,2,2]   );
sections[i].put( \bd  ,  [4,2,2]   );

i= 1;
sections[i].put( \s  ,  [72, 70, 68, 67, 67 ]  );
sections[i].put( \a  ,  [63, 65, 65, 63,]   );
sections[i].put( \t  ,  [55, 56, 58, 58,]    );
sections[i].put( \b  ,  [48, 50, 50, 51,]    );

this.appendHoldFor(i, hold, adjust);
		}

kommRuh { arg hold;
var i = 2;
var adjust = 1;

sections[i].put( \sd  ,  [4,2,1,1]  );
sections[i].put( \ad  ,  [2,2,2,2]  );
sections[i].put( \td  ,  [2,2,1,1,2]  );
sections[i].put( \bd  , [ 2,2,2,2] );

i= 3 ;
sections[i].put( \s  ,   [75,          74, 72, 71, 71,  ] );
sections[i].put( \a  ,   [\rest ,      67, 65, 68, 67,  ]  );
sections[i].put( \t  ,   [ \rest,       60, 59,60, 63,62, ]   );
sections[i].put( \b  ,  [ \rest ,      48, 56, 53,55,      ]    );

		this.appendHoldFor(i, hold,adjust);
	}


kommRuhA { arg hold;
var i = 8;
var adjust = 1;

sections[i].put( \sd ,  [ hold ] );
sections[i].put( \ad ,  [ hold ] );
sections[i].put( \td ,  [ hold ] );
sections[i].put( \bd , [ hold ] );

sections[i].put( \s  ,   [75,       ] );
sections[i].put( \a  ,   [\rest ,  ] );
sections[i].put( \t  ,   [ \rest,   ] );
sections[i].put( \b  ,  [ \rest ,  ] );
	}


kommRuhB { arg hold;
var i = 9;
var adjust = 1;

sections[i].put( \sd , [4, 2, 1,1]  );
sections[i].put( \ad , [4,2,2]  );
sections[i].put( \td ,  [4,1,1,2]  );
sections[i].put( \bd , [4,2,2] );

i= 10 ;
sections[i].put( \s  ,   [ 75, 74, 72, 71 ,71 ] );
sections[i].put( \a  ,   [ 67, 65, 68, 67 ]  );
sections[i].put( \t  ,   [  60, 59,60, 63, 62]   );
sections[i].put( \b  ,  [  48, 56, 53, 55     ]    );

		this.appendHoldFor(i, hold,adjust);
	}


kommFriede {arg hold;
var i = 4 ;
var adjust = 1;

sections[i].put( \sd  ,  [2,2,2, 2,1,1,2,4,4] );
sections[i].put( \ad  , [2,1,1,2,2,2,2,4,2,2]  );
sections[i].put( \td  , [2,2,1,1,6, 4,1,1,2]  );
sections[i].put( \bd  , [2,2,2,2,2,2,4,4]  );

i= 5 ;
sections[i].put( \s  ,   [72, 74, 75, 68, 67, 65, 67, 65, 65, 63] );
sections[i].put( \a  ,  [ 63, 67,65, 63, 65, 63, 63, 63, 62, 58]  );
sections[i].put( \t  ,   [ 60, 59, 55,56,58,60, 56, 55, 56, 55]   );
sections[i].put( \b  ,  [ 56,55,48,50, 51,43,44,46, 39]    );
	this.appendHoldFor(i, hold,adjust);
	}


immortal {
var i = 7; // ALL concat
		// no adjus or hold - takes basic value of 8

[ \s, \a, \t,  \b].do { arg eachVoice;
			sections[i].put( eachVoice  ,
				(sections[1].at(eachVoice) ++ sections[3].at(eachVoice) ++ sections[5].at(eachVoice) ))};

[ \sd, \ad, \td,  \bd]. do { arg eachVoice;
			sections[i].put( eachVoice  ,
				(sections[1].at(eachVoice) ++ sections[3].at(eachVoice) ++ sections[5].at(eachVoice) ))};
		}


// 8	 12	16   20  24
// 4	  6	8    10    12
//  2	  3	4    5     6
	}






