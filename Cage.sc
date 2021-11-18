Cage {
	var totalDur, buffers, score, startStop, startStopDur, mode;
	var playRoutine, dict;
	var startRatio = 0, stopRatio = 0;


	*new { | totalDur, buffers, score, startStop, startStopDur, mode = 1|
		// buffers.postln;
		^super.newCopyArgs(totalDur, buffers, score, startStop, startStopDur, mode).init;
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

			1.wait;

			loop{

				// ("second: " ++ count.asString).postln;

				for (0, (score.size - 1), {|i| 


					// if count value correspond to a startvalue
					if (count == startStop[i][0]) { // 
						
						// get random integer from start and stop span.
						startRand = 1.0.sum3rand.abs;
						stopRand = 1.0.sum3rand.abs;


						/*
						Remember the value of ratio previously performed.
						Make "informed" decision by including the previous mean value of all
						previous ratios.
						*/

						if (startRatio == 0){
							startRatio = startRand; stopRatio = stopRand;
						}{ // get mean value abs
							startRatio = ( startRatio + startRand ) / 2;
							stopRatio = ( stopRatio + stopRand ) / 2;
						};

						startTime = startStopDur[i][0] * startRatio;
						stopTime = startStopDur[i][1] * stopRatio;

						
						startRatio = 1 * ( 
							startRatio * (startRand / startStopDur[i][0]) 
						);
						stopRatio = 1 * ( 
							stopRatio * (stopRand / startStopDur[i][1]) 
						);

						// ( stoptime + stopRand ) - ( starttime + startRand ) = Duration
						dur = (
							( startStop[i][1] + stopTime ) - (startStop[i][0] + startTime)
						).abs; // abs is nödlösning

						"--------------------------".postln;
						("SYMBOL [ % ]".format(score[i])).postln;
						("Starting in: % seconds".format(startTime)).postln;
						("Stopping in: % seconds".format((dur+startTime))).postln;
						"--------------------------".postln;

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

					playhead = playhead + 1;
					if ( playhead == score.size || count == totalDur){
						/*
						Check if we reached the last event in score or the
						maximum duration of the piece.
						*/
						thisThread.stop;
					};
				});

				1.wait;
				count = count + 1;
			};
		}).play;
	}

	stop {
		playRoutine.stop;
	}

	synthDef {

		if (mode == 1) {

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



				// LoopBuf is built for use with an Attack - Hold - Release type envelope.
				// Drops sample as soon as its gate is <= 0

				// sig = LoopBuf.ar(
				// 	1, 
				// 	\buf.kr(0), 
				// 	1,
				// 	trigger, // when trigger is 0, it will play rest of buffer and die.
				// 	0.0,
				// 	startLoop: ( BufFrames.kr(\buf.kr) * 0.2 ), 
				// 	endLoop: ( BufFrames.kr(\buf.kr) * 0.3 ),
				// 	interpolation: 2);



				sig = Warp1.ar(
					1, 
					\buf.kr(0),
					Phasor.kr(trigger, SampleDur.ir / BufDur.ir(\buf.kr) / dur ),
					1, 
					0.3,
					-1,
					8,
					0.23,
					4
				);

				// Should there be a Compander.ar here? 

				Out.ar(0, sig!2 * env * 0.32);

			}).add;
		};
		if (mode == 2) {
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

				// PlayBuf works, but for continuous sound, you need long samples.
				// Does not loop seamlessly

				sig = PlayBuf.ar( 1, 
					\buf.kr(0), 
					1,
					trigger,
					loop: 1
				);

				// sig = Warp1.ar(
				// 	1, 
				// 	\buf.kr(0),
				// 	Phasor.kr(trigger, SampleDur.ir / BufDur.ir(\buf.kr) / dur ),
				// 	1, 
				// 	0.3,
				// 	-1,
				// 	8,
				// 	0.23,
				// 	4
				// );

				// Should there be a Compander.ar here? 

				Out.ar(0, sig!2 * env * 0.32);

			}).add;

		};
		" ".postln;
		"SynthDef added to server".postln;
	}
}

