

// +-+-+-+-+-+-+ +-+-+-+-+-+-+ +-+-+-+-+-+-+-+
// |T|r|i|p|l|e| |n|e|s|t|e|d| |e|x|a|m|p|l|e|
// +-+-+-+-+-+-+ +-+-+-+-+-+-+ +-+-+-+-+-+-+-+


SampleBank.populate;
SampleBank.warmUp;
SampleBank.tempo;
~frac = Xhooser.new;
~frac.name_("fractional beat hard stop");
~frac.noseCone_(1);
~frac.addLane(Lane.new.namedSample(\vox).loopOff.hardStopOn.weight_(1));
//~frac.addLane(Lane.new.namedSample(\n2).loopOff.hardStopOn.weight_(1));
//~frac.addLane(Lane.new.namedSample(\n3).loopOff.hardStopOn.weight_(1));
~fractime = TimeChooser.new;
~fractime.noseCone_(1);
~fractime.addLane(TimeLane.new.beats_(0.1));
// ~beeptime.addLane(TimeLane.new.beats_(1.5));
//~beeptime.addLane(TimeLane.new.beats_(2));
// ~beeptime.addLane(TimeLane.new.beats_(2.5));
//~beeptime.addLane(TimeLane.new.beats_(4));
~frac.timeChooser_(~fractime);

~frac.play




BOTH WORK OK NOW, but double click on trim4??


SampleBank.disableTrim
SampleBank.enableTrim

SampleBank.populate;
a= SampleBank.at(\n1).buffer;
b= SampleBank.at(\n2).buffer;
c= SampleBank.at(\n3).buffer;
d= SampleBank.at(\bvs).buffer;

SynthDef("basictest" , {arg loop=0, volume=0.5, outputBus=0;
			Out.ar(outputBus,
				     PlayBuf.ar(2, a.bufnum,
					                     BufRateScale.kr(b.bufnum),
					                   loop:loop, doneAction:2)*volume )
			                                       // WAS done action 2
			}).add;
a.duration
b.duration
c.duration
d.duration
y = Synth("basictest"); // 1 click
y.inspect



SampleBank.populate;
SampleBank.warmUp;
SampleBank.tempo;
z= Lane.new.namedSample(\n1).loopOff.hardStopOn.weight_(1);
z.play    // 2 clicks (one 2 secs later - why? ) - just one with SAMPLE


SampleBank.at(\n1).play   // all have second click (\n2 is quiet)
SampleBank.at(\n2).play
SampleBank.at(\n3).play
SampleBank.at(\n4).play


~beep.chosenLanes.first.sample.play
~beep.lanes.first.sample.play
// beep - WRONG SAMPLE!!!!!!! WHAY? bufferer zero or 1 or something?
Tomething about trim gen probably - try disable TRim4. Maybe not...

Server.killAll;
s.boot;
s.freeAll;
SampleBank.populate;

//SampleBank.populateBank1;
//SampleBank.populateBank2;
//SampleBank.createAndReadInBuffers


SampleBank.warmUp;
SampleBank.tempo_(500/60);     // was 500 - 120 is good


(
~beep = Xhooser.new;
~beep.name_("beep");
~beep.noseCone_(1);
~beep.addLane(Lane.new.namedSample(\n1).loopOff.hardStopOn.weight_(1));
~beep.addLane(Lane.new.namedSample(\n2).loopOff.hardStopOn.weight_(1));
~beep.addLane(Lane.new.namedSample(\n3).loopOff.hardStopOn.weight_(1));
~beeptime = TimeChooser.new;
~beeptime.noseCone_(1);
~beeptime.addLane(TimeLane.new.beats_(0.5));
 ~beeptime.addLane(TimeLane.new.beats_(1.5));
~beeptime.addLane(TimeLane.new.beats_(2));
 ~beeptime.addLane(TimeLane.new.beats_(2.5));
~beeptime.addLane(TimeLane.new.beats_(4));
~beep.timeChooser_(~beeptime);



//~beep.play  // some pesky global I left assigned? currentEnvironemnt

~kick = Xhooser.new;
~kick.name_("kick");
~kick.noseCone_(1);
~kick.addLane(Lane.new.namedSample(\k1).loopOff.hardStopOn.weight_(1));
~kick.addLane(Lane.new.namedSample(\k2).loopOff.hardStopOn.weight_(1));
~kicktime = TimeChooser.new;
~kicktime.noseCone_(1);
~kicktime.addLane(TimeLane.new.beats_(4).weight_(1));
~kicktime.addLane(TimeLane.new.beats_(8).weight_(1));
~kick.timeChooser_(~kicktime);

//~kick.play

~toms = Xhooser.new;
~toms.name_("toms");
~toms.noseCone_(1);
~toms.addLane(Lane.new.namedSample(\ht).loopOff.hardStopOn.weight_(1));
~toms.addLane(Lane.new.namedSample(\mt).loopOff.hardStopOn.weight_(1));
~toms.addLane(Lane.new.namedSample(\lt).loopOff.hardStopOn.weight_(1));
~tomtime = TimeChooser.new;
~tomtime.noseCone_(1);
~tomtime.addLane(TimeLane.new.beats_(10).weight_(2));
~tomtime.addLane(TimeLane.new.beats_(5).weight_(1));
~toms.timeChooser_(~tomtime);

// ~toms.play // plays twice!! maybe 1 too many thing?

~perc = Xhooser.new;
~perc.name_("percussion");
~perc.noseCone_(inf);
~perc.addLane(Lane.new.nest(~beep).loopOn.hardStopOn.weight_(1));
~perc.addLane(Lane.new.nest(~kick).loopOn.hardStopOn.weight_(1));
~perc.addLane(Lane.new.nest(~toms).loopOn.hardStopOn.weight_(1));

//  .


~bass = Xhooser.new;
~bass.name_("bass");
~bass.noseCone_(1);
~bass.addLane(Lane.new.namedSample(\ep1).loopOff.hardStopOn.weight_(1));
	~bass.addLane(Lane.new.namedSample(\ep2).loopOff.hardStopOn.weight_(1));
		~bass.addLane(Lane.new.namedSample(\ep3).loopOff.hardStopOn.weight_(1));
// ~basstime = TimeChooser.new;
// ~basstime.noseCone_(1);
// ~basstime.addLane(TimeLane.new.beats_(8));
// ~basstime.addLane(TimeLane.new.beats_(10));
// ~basstime.addLane(TimeLane.new.beats_(12));
// ~basstime.addLane(TimeLane.new.beats_(6));
// ~basstime.addLane(TimeLane.new.beats_(7));
// ~bass.timeChooser_(~basstime);

// ~bass.play

~top = Xhooser.new;
~top.name_("top");
~top.noseCone_(1);
~top.addLane(Lane.new.namedSample(\ep4).loopOff.hardStopOn.weight_(1));
~top.addLane(Lane.new.namedSample(\ep5).loopOff.hardStopOn.weight_(1));
~top.addLane(Lane.new.namedSample(\ep6).loopOff.hardStopOn.weight_(1));
// ~toptime = TimeChooser.new;
// ~toptime.noseCone_(1);
// ~toptime.addLane(TimeLane.new.beats_(4));
// ~toptime.addLane(TimeLane.new.beats_(6));
// ~top.timeChooser_(~toptime);


~ep = Xhooser.new;
~ep.name_("ep");
~ep.noseCone_(2);
~ep.addLane(Lane.new.nest(~bass).loopOff.hardStopOn.weight_(1));
~ep.addLane(Lane.new.nest(~top).loopOff.hardStopOn.weight_(1));
~eptime = TimeChooser.new;
~eptime.noseCone_(1);
~eptime.addLane(TimeLane.new.beats_(24));
~eptime.addLane(TimeLane.new.beats_(16));
~ep.timeChooser_(~eptime);



~glock = Xhooser.new;
~glock.name_("glock");
~glock.noseCone_(1);
~glock.addLane(Lane.new.namedSample(\g1).loopOff.hardStopOn.weight_(1));
~glock.addLane(Lane.new.namedSample(\g2).loopOff.hardStopOn.weight_(1));
~glock.addLane(Lane.new.namedSample(\g3).loopOff.hardStopOn.weight_(1));
~gtime = TimeChooser.new;
~gtime.noseCone_(1);
~gtime.addLane(TimeLane.new.beats_(64));
~gtime.addLane(TimeLane.new.beats_(48));
~glock.timeChooser_(~gtime);


~parent = Xhooser.new;
~parent.name_("parent");
~parent.noseCone_(inf);
~parent.addLane(Lane.new.nest(~perc).loopOn.hardStopOn.weight_(1));
~parent.addLane(Lane.new.nest(~ep).loopOn.hardStopOn.weight_(1));
~parent.addLane(Lane.new.nest(~glock).loopOn.hardStopOn.weight_(1));
~parenttime = TimeChooser.new;
~parenttime.noseCone_(1);
~parenttime.addLane(TimeLane.new.beats_(256));
~parent.timeChooser_(~parenttime);


~parent.play

)
