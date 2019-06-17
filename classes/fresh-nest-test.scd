// SampleBank.enableTrim; // SampleBank.disableTrim;
SampleBank.populate;
SampleBank.warmUp;
SampleBank.tempo_(122/60);
s.notify = false; // does not stop failure in server messages
s.notify = true;
s.error = false;  // not valid


(
~bass = Xhooser.new;
~bass.name_("4 bars of bass");
~bass.noseCone_(1);
~bass.addLane(Lane.new.namedSample(\bass).loopOn.hardStopOn.weight_(1));  // hard stops now
~bass.addLane(Lane.new.namedSample(\bass2).loopOn.hardStopOn.weight_(1));
~bass.addLane(Lane.new.namedSample(\synthbass).loopOn.hardStopOn.weight_(1));
~basstc = TimeChooser.new;
~basstc.noseCone_(1);
~basstc.addLane(TimeLane.new.beats_(4*4));   //back to 4 bars with a hard stop
~bass.timeChooser_(~basstc);

~trim = TrimTool.new;
~trim.grid_(16).start_(8).end_(16).anchor_(14);

~trim2 = TrimTool.new;
~trim2.grid_(16).start_(4).end_(12).anchor_(8);

~nest = Xhooser.new;
~nest.name_("single nest");
~nest.noseCone_(3);
~nest.addLane(Lane.new.nest(~bass).loopOn.hardStopOn.weight_(1));  /// BUG - bass not stopped by hard stop
~nest.addLane(Lane.new.namedSample(\vox).loopOff.hardStopOn.weight_(1).trim(~trim));
~nest.addLane(Lane.new.namedSample(\marimba).loopOn.hardStopOn.weight_(1).trim(~trim2));
~nest.addLane(Lane.new.namedSample(\drums).loopOn.hardStopOn.weight_(1));
~nesttc = TimeChooser.new;
~nesttc.noseCone_(1);
~nesttc.addLane(TimeLane.new.beats_(4*16));
~nest.timeChooser_(~nesttc);

//(Lane.new.namedSample(\vox).loopOff.hardStopOn.weight_(1).trim(~trim)).sample.inspect

~nest2 = Xhooser.new;
~nest2.name_("double nest");
~nest2.noseCone_(2);
~nest2.addLane(Lane.new.namedSample(\bvs).loopOff.hardStopOn.weight_(0));
~nest2.addLane(Lane.new.nest(~nest).loopOn.hardStopOn.weight_(inf));
~nest2tc = TimeChooser.new;
~nest2tc.addLane(TimeLane.new.beats_(4*32));
~nest2.timeChooser_(~nest2tc);

// SampleBank.warmUp;
~nest2.play;
)

///// ****************************


SampleBank.populate;
SampleBank.warmUp;
SampleBank.tempo_(100/60);
s.freeAll;

(
~strings = Xhooser.new;
~strings.name_("strings");
~strings.noseCone_(1);
~strings.addLane(Lane.new.namedSample(\strings1).loopOff.hardStopOn.weight_(1));
~strings.addLane(Lane.new.namedSample(\strings2).loopOff.hardStopOn.weight_(0));
~strings.addLane(Lane.new.namedSample(\strings3).loopOff.hardStopOn.weight_(0));
~stringstime = TimeChooser.new;
~stringstime.noseCone_(1);
~stringstime.addLane(TimeLane.new.beats_(4));
~strings.timeChooser_(~stringstime);

~strings2 = Xhooser.new;
~strings2.name_("strings");
~strings2.noseCone_(1);
~strings2.addLane(Lane.new.namedSample(\strings1).loopOn.hardStopOn.weight_(1));
~strings2.addLane(Lane.new.namedSample(\strings2).loopOn.hardStopOn.weight_(0));
~strings2.addLane(Lane.new.namedSample(\strings3).loopOn.hardStopOn.weight_(0));
~stringstime2 = TimeChooser.new;
~stringstime2.noseCone_(1);
~stringstime2.addLane(TimeLane.new.beats_(64));
~strings2.timeChooser_(~stringstime2);

~strongs2 = Xhooser.new;
~strongs2.name_("strongs");
~strongs2.noseCone_(2);
~strongs2.addLane(Lane.new.namedSample(\strings1).loopOn.hardStopOn.weight_(0));
~strongs2.addLane(Lane.new.namedSample(\strings2).loopOn.hardStopOn.weight_(1));
~strongs2.addLane(Lane.new.namedSample(\strings3).loopOn.hardStopOn.weight_(1));
~strongstime2 = TimeChooser.new;
~strongstime2.noseCone_(1);
~strongstime2.addLane(TimeLane.new.beats_(32));
~strongs2.timeChooser_(~strongstime2);



~strongs = Xhooser.new;
~strongs.name_("strongs");
~strongs.noseCone_(2);
~strongs.addLane(Lane.new.namedSample(\strings1).loopOff.hardStopOn.weight_(0));
~strongs.addLane(Lane.new.namedSample(\strings2).loopOff.hardStopOn.weight_(1));
~strongs.addLane(Lane.new.namedSample(\strings3).loopOff.hardStopOn.weight_(1));
~strongstime = TimeChooser.new;
~strongstime.noseCone_(1);
~strongstime.addLane(TimeLane.new.beats_(4));
~strongs.timeChooser_(~strongstime);


~pad = Xhooser.new;
~pad.name_("pad");
~pad.noseCone_(1);
~pad.addLane(Lane.new.namedSample(\pad1).loopOn.softStopOn.weight_(1));
~pad.addLane(Lane.new.namedSample(\pad2).loopOn.softStopOn.weight_(1));
~padtime = TimeChooser.new;
~padtime.noseCone_(1);
~padtime.addLane(TimeLane.new.beats_(4));
~pad.timeChooser_(~padtime);


~nest = Xhooser.new;
~nest.name("Nest");
~nest.noseCone_(inf);
~nest.addLane(Lane.new.nest(~strings).loopOn.hardStopOn.weight_(1));
~nest.addLane(Lane.new.nest(~strongs).loopOn.hardStopOn.weight_(1));
~nest.addLane(Lane.new.nest(~pad).loopOn.hardStopOn.weight_(1));
~nesttc = TimeChooser.new;
~nesttc.noseCone_(1);
~nesttc.addLane(TimeLane.new.beats_(4*32));
~nest.timeChooser_(~nesttc);

~nest.play;
)


/////////////

// Terry Riley in C
SampleBank.tempo_(2);
 SampleBank.populate;
 SampleBank.warmUp;
Riley.new.choir.play


/////////////////////
2.value
[ 1,2,3,4].first