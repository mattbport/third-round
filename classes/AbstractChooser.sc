// NOT USED !!! get Xhoosers & TimeChoosers  right & stable first

AbstractChooser{
	var <>noseCone;
	var <>lanes;
	var <> nonZeroWeightedLanes;

	//var <>chosenLanes;      - XHOOSER
	//var <> timeChooser;     - - XHOOSER

 init{
	   lanes = List.new;
	   }

*new { ^ super.new.init}     //  CHooser does a suoer init

addLane{
		arg aLane;
		this.lanes.add(aLane)}

//noseConeIsInfinite{


noseConeIsZero{
		^this.noseCone == 0}

priorityBoarders {
			^ this.lanes.select({ arg eachLane, index; eachLane.hasInfiniteWeight})   }

hasPriorityBoarders {
			^ this.priorityBoarders.size>0}

//hasTooManyPriorityBoarders{


finiteWeightedLanes{
		^ this.lanes.select({arg eachLane, index ; (eachLane.hasInfiniteWeight.not).and(eachLane.hasZeroWeight) } )   }


//chooseWinnersFromTooManPriorityBoarders	 {                     // in time chooser just choose one


nonDeterministicLaneChoice {
		this.subclassResponsibility(thisMethod);
	}

//noOfLanesStillToPick{


	preChoose{
	       this.subclassResponsibility(thisMethod);
	}



	play {
	         this.subclassResponsibility(thisMethod);
	}



	duration{
	             this.subclassResponsibility(thisMethod);
	                }



	}
