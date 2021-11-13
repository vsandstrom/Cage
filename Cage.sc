Cage {
	var totalDur, buffers, score, startStop, startStopDur;
	var playRoutine, dict;
	var startRatio = 0, stopRatio = 0;


	*new { | totalDur, buffers, score, startStop, startStopDur |
		// buffers.postln;
		^super.newCopyArgs(totalDur, buffers, score, startStop, startStopDur).init;
	}

	init {
		var num = score.asSet.size;

		if (num > buffers.size){
			^Error("Number of supplied buffers does not match number of unique numbers in score").throw;
		}{

			// Scramble sounds supplied before hashing.
			buffers = buffers.scramble; 
			dict = Dictionary.new(num);

			// create dictionary to associate one sound to each unique identifier in score.
			for (0, ( num - 1 ), {|i| 
				dict.add(i -> buffers[i]);
			});

			this.synthDef;

		};
		

	} 

	play {
		playRoutine = Routine({
			var count = 0;
			var playhead = 0;
			var startRand = 0;
			var stopRand = 0;
			var startTime = 0;
			var stopTime = 0;
			var dur = 0;
			var final = false;

			1.wait;

			loop{
				("second: " ++ count.asString).postln;
				for (0, ( score.size - 1 ), {|i|


					// if count value correspond to a startvalue
					if (count == startStop[i][0]) { // 
						
						// get random integer from start and stop span.
						startRand = 1.0.sum3rand.abs;
						stopRand = 1.0.sum3rand.abs;
						if (startRatio == 0){
							startRatio = startRand; stopRatio = stopRand;
						}{
							// get mean value abs
							startRatio = ( startRatio + startRand ) / 2;
							stopRatio = ( stopRatio + stopRand ) / 2;
						};

						startTime = startStopDur[i+1][0] * startRatio;
						stopTime = startStopDur[i+1][1] * stopRatio;

						playhead = i;
						
						startRatio = 1 * ( 
							startRatio * (startRand / startStopDur[i+1][0]) 
						);
						stopRatio = 1 * ( 
							stopRatio * (stopRand / startStopDur[i+1][1]) 
						);

						// ( stoptime + stopRand ) - ( starttime + startRand ) = Duration
						dur = ( startStop[i][1] + stopTime ) - (startStop[i][0] + startTime);
						"--------------------------".postln;
						("SYMBOL %".format(score[i])).postln;
						("Starting in " ++ ( startTime ) ++ "seconds").postln;
						("Stopping in: " ++ (dur + startTime) ++ "seconds").postln;
						"--------------------------".postln;

						// // REMEMBER SUBTRACTING FROM TOTALDUR!!!!
						totalDur = totalDur - (startTime + dur);



						
						// Create synth with delaytime within synthdef.
						Synth(\cage, 
							[
								\buf, dict.at(score[i]),  
								\delay, startTime,
								\t_trig, 1,
								\dur, dur
							]
						);
					};

					if (playhead == ( score.size - 1 )) {
						final = True;
					};

					if (count == 1800 || final == True) {	
						thisFunction.stop;
					};
				});
				// ("second: " ++ count.asString).postln;

				1.wait;

				count = count + 1;

			};
		}).play;
	}

	stop {
		playRoutine.stop;
	}

	synthDef {
		SynthDef(\cage, {
			var sig, env, buf, trigger, atk, dur, rel;
			trigger = 0;

			dur = \dur.kr(0);
			atk = 0.35;
			rel = 0.45;
			dur = dur - (atk + rel);

			trigger = TDelay.kr(\t_trig.kr(0), \delay.kr(0));

			env = Env.new([0, 1, 1, 0], [atk, dur, rel], [-4, 0, 0, 4]);

			env = EnvGen.kr(env, trigger, doneAction: 2);

			// Tried to use LoopBuf.ar, but behaves weirdly.

			// sig = LoopBuf.ar(
			// 	1, 
			// 	\buf.kr(0), 
			// 	1,
			// 	trigger,
			// 	startLoop: ( BufFrames.kr(\buf.kr) * 0.2 ), 
			// 	endLoop: ( BufFrames.kr(\buf.kr) * 0.8 ),
			// 	interpolation: 2);


			// PlayBuf works, but for continuous sound, you need long samples, 
			// where they do not need to loop. 

			sig = PlayBuf.ar(
				1, 
				\buf.kr(0), 
				1,
				trigger,
				loop: 1
			);

			Out.ar(0, sig * env * 0.32);

		}).add;

		"SynthDef added to server".postln
	}
}

