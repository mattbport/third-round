PClip{
	// ACTUAL superclass of nystedt & cheese bass?
  // cant call them clips cos that class name taken
	// Terrible  misnomer — nystedt is sometimes just a set of pbinds - e.g. kom1
	// so does not know its own tempo. se we manage that in play instead.

play { this.subclassResponsibility(thisMethod);}
stop  { this.subclassResponsibility(thisMethod);}
repeat  { this.subclassResponsibility(thisMethod);}
duration  { this.subclassResponsibility(thisMethod);}  // in beats
tempo  { this.subclassResponsibility(thisMethod);}      // in beats per sec

	// and maybe also (probably get fro free)
pause { this.subclassResponsibility(thisMethod);}
resume { this.subclassResponsibility(thisMethod);}
free { this.subclassResponsibility(thisMethod);}

	// no need -  over complex - better yo have it in the wrapper
name  { this.subclassResponsibility(thisMethod);}


// NB  Big ones are 	play, duration, name free
// this is real superclass



	// maybe - can avoid if use recusion to implmenet(!)
	//so this is a forked subclass
hasLoop     {      }
hasNoLoop {      }
loopOn       {	     }
loopOff       {      }



}