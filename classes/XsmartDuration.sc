XsmartDuration {
	var <> beats;
	var <> tempo;

	seconds { ^ beats /tempo}

	asSeconds {
		 ^ this.seconds}



	printOn { | aStream |
		aStream << "beats " << this.beats << " tempo "
		                                <<  this.tempo << " secs " << this.seconds;
		^aStream}


}