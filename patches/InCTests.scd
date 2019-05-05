Integrity Tests
// rock scenarios NIN

// TEST 1
SampleBank.tempo_(2);
 SampleBank.populate;
 SampleBank.warmUp;
Riley.new.choir.play

//Test2
TestRunner.new.nystedt

//Test3
// see outbus in Lane and Xhooser (also in sample for samples)
 SampleBank.populate;
 SampleBank.warmUp;
TestRunner.new.clapping

//Test 4
PBank.populate;
TestRunner.new.cheeseBass.play;
TestRunner.new.cheeseBass2.play;


// TEST 5
// nested & sequenced looping & softstop for pbinds now implemented
SampleBank.tempo_(3.5);
PBank.populate;
TestRunner.new.synthClap.play;

//clapping music.scd works
//midiclip is in pbank










