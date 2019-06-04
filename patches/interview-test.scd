Server.killAll;
s.boot;
s.freeAll;
SampleBank.populate;
SampleBank.warmUp;
SampleBank.tempo_(122/60);



// Sequencing and Time Choosers

(
~ch1 = Xhooser.new;
~ch1.noseCone_(2);
~ch1.addLane( Lane.new.weight_(1).namedSample(\drums).loopOn);
~ch1.addLane( Lane.new.weight_(2).namedSample(\bass).loopOn);
~ch1.addLane( Lane.new.weight_(inf).namedSample(\vox).loopOn.softStopOn);
~tc1 = TimeChooser.new;
~tc1.noseCone_(1);
~tc1.addLane( TimeLane.new.weight_(1).beats_(4*4));
~tc1.addLane( TimeLane.new.weight_(2).beats_(4*6));
~ch1.timeChooser_(~tc1);
)

(
~ch2 = Xhooser.new;
~ch2.noseCone_(inf);
~ch2.addLane( Lane.new.weight_(2).namedSample(\drums).loopOn);
~ch2.addLane( Lane.new.weight_(2).namedSample(\bass).loopOn);
~ch2.addLane( Lane.new.weight_(1).namedSample(\guitar).loopOn);
~ch2.addLane( Lane.new.weight_(inf).namedSample(\marimba).loopOn);
~ch2.addLane( Lane.new.weight_(1).namedSample(\bvs).loopOn.softStopOn);
~ch2.addLane( Lane.new.weight_(inf).namedSample(\vox).loopOn.softStopOn);
~tc2 = TimeChooser.new;
~tc2.noseCone_(1);
~tc2.addLane( TimeLane.new.beats_(4*6));
~ch2.timeChooser_(~tc2);
)

~ch1.play;
~ch2.play;


(
~s4 = LoopableSequence.new;
~s4.add (~ch1);
~s4.add (~ch2);
)
~s4.play;