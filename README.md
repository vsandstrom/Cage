# Cage
Supercollider Class for playing John Cage "number pieces"

Class takes 6 arguments:

* Total duration of piece in seconds. <br/>(integer)

* An array of integers representing the score, where each integer will be associated with a unique sound-buffer. <br/>(array of integers)

* An array of buffers of samples. <br/>(array of bufnums)

* An array containing the time where a new synth might me triggered, and when it might end. <br/>(2D array : [ [start0, stop0], [start1, stop1] ])

* An array containing the duration for the possible start and stop point. <br/>(2D array, same as above)

* An integer indicating which synth mode the class should use. *1* == Granular timestretch, *2* == Regular sample playback.

These arrays, *__apart from the array of buffers__*, need to be of the same length. Each element in the score-array needs to have a corresponding element sub-array with a startTime/stopTime and startDur/stopDur.
```supercollider
var score = [1, 2, 3];

var startstop = [[0, 15], [15, 30], [45, 90]];

var startstopDur = [[20, 20], [10, 30], [5, 0]];
```
 

Look at example [.scd-file](https://github.com/vsandstrom/Cage/blob/main/one.scd) for reference
