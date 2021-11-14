/* 
Cage takes a lists of start-stop data, durations and sound-numbers as input, 
gets the unique sound and plays it for the correct duration. Keeps track of previous 
sustain-data, to make "informed decisions" of how this performer wants to frase each event. 

Remember what ratio to duration it has previously performed.  
ex. if it can start between 00:00 and 1:00, there is 60 seconds of possibility, if it decides to 
play at 0:17, the ratio is ≈ 0.3, and maybe performes close to that ratio often?
*/

s.waitForBoot({

	var time = 0;
	var totalDur = 1800;
	var numSounds = 12 
	var path = File.getcwd; // Directory of this supercollider file.
	
	var score = #[ // Score of which sound that is supposed to be played.
		// page 1
		2,  4,  9, 11,
		5,  8,  2,  8, 
		// page 2
		8,  4,  8,  5,  2,
		5,  8,  8,  8,
		// page 3
		7,  6,  7,  2,
		7,  8,  3,  8,
		// page 4
		6,  7,  8, 12, 
		4, 12,  7,  6,
		// page 5
	   12,  7,  5,  3,
		9,  7,  5,  5,
		8, 11
	];
	
	var ss = #[ // Start/stop time for each event
		// page 1
		[0, 55], [0, 60], [110, 140], [170, 200],
		[180, 220], [220, 275], [250, 310], [315, 375],
		// page 2
		[370, 430], [450, 480], [490, 550], [495, 555], [635, 655],
		[625, 655], [695, 705], [655, 725], [690, 750], 
		// page 3
		[800, 810], [800, 855], [815, 870], [920, 930],
		[935, 955], [920, 960], [965, 1005], [995, 1050],
		// page 4
		[1045, 1085], [1110, 1130], [1150, 1170], [1115, 1175], 
		[1185, 1215], [1240, 1280], [1245, 1285], [1325, 1355], 
		// page 5
		[1330, 1390], [1380, 1420], [1455, 1510], [1450, 1510], 
		[1555, 1610], [1575, 1630], [1655, 1710], [1685, 1715], 
		[1750, 1780], [1785, 1795],
	];
	
	var dur = #[ // Duration of each start and stop for each event
		[75, 70], [90, 90], [45, 45], [45, 45], // page1
		[60, 60], [75, 75], [90, 90], [90, 90],
		// page 2
		[90, 90], [45, 45], [90, 90], [90, 90], [30, 30],
		[45, 45], [15, 15], [90, 90], [90, 90],
		// page 3
		[75, 75], [75, 70], [75, 70], [75, 75], 
		[30, 30], [60, 60], [60, 60], [75, 70],
		// page 4
		[60, 60], [30, 30], [30, 30], [90, 90],
		[45, 45], [60, 60], [60, 60], [45, 45],
		// page 5
		[90, 90], [60, 60], [75, 70], [90, 90],
		[75, 70], [75, 70], [75, 70], [45, 45], 
		[30, 30], [25, 15]];

	var sounds = Array.new(numSounds);

	for (1, numSounds, {|i| 
		var buf;
		/* 
		Add your samples in Current Working Directory, in a folder called samples
		and name them consecutively, as: 1.wav, 2.wav, 3.wav osv.
		*/
		buf = Buffer.read(s, path +/+ "samples/" ++ i.asString ++ ".wav");
		sounds.add(buf);
	});

	s.sync;

	c = Cage.new(totalDur: totalDur, buffers: sounds, score: score, startStop: ss, startStopDur: dur);


	c.play;
	// c.stop;
		
});
