////////////////////////////////////////////////////////////////////////////////
//
//									FOUR7
//
////////////////////////////////////////////////////////////////////////////////
s.makeGui


s.waitForBoot({
	
	var sounds;
	var totalDur = 1800;
	var numSounds = 12;
	var path = File.getcwd; // Directory of this supercollider file.

	"setup.scd".load; // load the complete score for 

		
	/* 
	Add your samples in Current Working Directory, in a folder called samples
	and name them consecutively, as: 1.wav, 2.wav, 3.wav osv.
	*/

	sounds = Array.new(numSounds); 
	for (1, numSounds, {|i| 
		var buf;
		buf = Buffer.read(s, path +/+ "samples/" ++ i.asString ++ ".wav");
		sounds.add(buf);
	});

	s.sync;
	// c = Cage.new(totalDur: totalDur, buffers: sounds, score: score, startStop: ss, startStopDur: dur, mode: 1);
	c = Cage.new(
		totalDur: totalDur,
		buffers: sounds, 
		score: ~score0,
		startStop: ~ss0,
		startStopDur: ~dur0,
		mode: 1);
	a = Cage.new(
			totalDur: totalDur,
			buffers: sounds,
			score: ~score1,
			startStop: ~ss1, 
			startStopDur: ~dur3,
			mode: 1
		);
	g = Cage.new(
			totalDur: totalDur,
			buffers: sounds,
			score: ~score2,
			startStop: ~ss2,
			startStopDur: ~dur3,
			mode: 1
		);
	e = Cage.new(
			totalDur: totalDur,
			buffers: sounds,
			score: ~score3,
			startStop: ~ss3,
			startStopDur: ~dur3,
			mode: 1
		);

	c.play;
	a.play;
	g.play;
	e.play;
});
	
c.stop; // emergency break
